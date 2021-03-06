package com.arb222.udhari.Transactions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.arb222.udhari.NewTransactionActivity;
import com.arb222.udhari.Notification.NotificationAdapter;
import com.arb222.udhari.Notification.NotificationModel;
import com.arb222.udhari.POJO.Transaction;
import com.arb222.udhari.POJO.UserConnection;
import com.arb222.udhari.POJO.UserInfo;
import com.arb222.udhari.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConnectionActivity extends AppCompatActivity {

    private static final String TAG = "ConnectionActivity";

    private CircleImageView connectionDPImageView;
    private TextView toPayTextView;
    private RecyclerView txnRecyclerView;
    private FloatingActionButton fab;
    private double meToPay;
    private String displayName;

    private List<TransactionModel> txnList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        getSupportActionBar().hide();

        //Getting Intent extras
        Bundle bundle = getIntent().getExtras();
        displayName = bundle.getString("display_name");
        final String connectionUid = bundle.getString("connection_uid"),
        connId = bundle.getString("conn_id");
        final int myType = bundle.getInt("my_type");
        meToPay = bundle.getDouble("me_to_pay");



        //Setting collapsing toolbar
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.connection_activity_collapsingtoolbar);
        collapsingToolbarLayout.setTitleEnabled(true);

        //Initializing UI elements
        toPayTextView = findViewById(R.id.connection_activity_topay_textView);
        connectionDPImageView = findViewById(R.id.connection_activity_image);
        fab = findViewById(R.id.fab_newtxn);
        txnRecyclerView = (RecyclerView) findViewById(R.id.transaction_recyclerview);
        txnRecyclerView.addItemDecoration(new DividerItemDecoration(txnRecyclerView.getContext(),DividerItemDecoration.VERTICAL));

        //Initializing Adapter for RV
        final TransactionAdapter adapter = new TransactionAdapter(this, txnList);


        //Setting up UI elements data
        initPay();
        DatabaseReference conRef = FirebaseDatabase.getInstance().getReference("userconnection");
        conRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(connId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserConnection pojoObj = dataSnapshot.getValue(UserConnection.class);
                try {
                    meToPay = pojoObj.getPay();
                    
                }catch (NullPointerException e){
                    Log.d(TAG, "onDataChange: NullPointerExc in new pay amount conn activity");
                }
                initPay();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: New Transaction");
                Intent txnIntent = new Intent(ConnectionActivity.this, NewTransactionActivity.class);
                txnIntent.putExtra("uid_of_connection",connectionUid);
                txnIntent.putExtra("display_name",displayName);
                txnIntent.putExtra("connection_id",connId);
                txnIntent.putExtra("my_type",myType);
                startActivity(txnIntent);
            }
        });

        //Setting DP for connection
        DatabaseReference connectedToUserInfoRef = FirebaseDatabase.getInstance().getReference("userinfo").child(connectionUid);
        connectedToUserInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo connectedToUserInfo = dataSnapshot.getValue(UserInfo.class);
                if(connectedToUserInfo.getProfileStatus()==3) {
                    Picasso.get().load(connectedToUserInfo.getProfilePictureLink())
                            .placeholder(R.drawable.user)
                            .fit()
                            .centerCrop()
                            .into(connectionDPImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Getting data for the recycler view
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("connectiontxns").child(connId);
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txnList.clear();
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    Transaction newTransaction = childSnapshot.getValue(Transaction.class);
                    double paidByMe=0;
                    if(myType==1){
                        paidByMe = newTransaction.getPaidBy1() - newTransaction.getPaidFor1();
                    }else if(myType==2){
                        paidByMe = newTransaction.getPaidBy2() - newTransaction.getPaidFor2();
                    }
                    String notice="";
                    if(paidByMe>0){
                        int firstSpace = displayName.indexOf(' ');
                        if(firstSpace>0)
                            notice = "You paid for "+displayName.substring(0,firstSpace)+" ₹"+paidByMe;
                        else
                            notice = "You paid for "+displayName+" ₹"+paidByMe;
                    }else if(paidByMe<0){
                        int firstSpace = displayName.indexOf(' ');
                        if(firstSpace>0)
                            notice = displayName.substring(0,firstSpace)+" paid for you ₹"+Math.abs(paidByMe);
                        else
                            notice = displayName+" paid for you ₹"+Math.abs(paidByMe);
                    }else {
                        notice = "No amount was exchanged";
                    }
                    TransactionModel model = new TransactionModel(notice,newTransaction.getDesc(),newTransaction.getTransactionId(),connId,connectionUid,newTransaction.getTimestamp(),newTransaction.getDeleteOther());
                    txnList.add(model);
                }
                Collections.sort(txnList,TransactionModel.BY_TIMESTAMP);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        txnRecyclerView.setLayoutManager(new LinearLayoutManager(ConnectionActivity.this));
        txnRecyclerView.setAdapter(adapter);
    }

    private void initPay() {
        if(meToPay>0){
            toPayTextView.setText("You have to pay "+displayName+" ₹"+meToPay);
        }else if(meToPay<0){
            toPayTextView.setText(displayName+ " have to pay you ₹"+Math.abs(meToPay));
        }else {
            toPayTextView.setText("You both are settled up");
        }
    }
}

