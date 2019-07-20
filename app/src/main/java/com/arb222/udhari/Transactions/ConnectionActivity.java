package com.arb222.udhari.Transactions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.arb222.udhari.Notification.NotificationAdapter;
import com.arb222.udhari.Notification.NotificationModel;
import com.arb222.udhari.POJO.UserInfo;
import com.arb222.udhari.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConnectionActivity extends AppCompatActivity {

    private CircleImageView connectionDPImageView;
    private TextView toPayTextView;
    private RecyclerView txnRecyclerView;

    private List<TransactionModel> txnList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        final String displayName = bundle.getString("display_name"),
        connectionUid = bundle.getString("connection_uid"),
        connId = bundle.getString("conn_id");
        int myType = bundle.getInt("my_type");
        double meToPay = bundle.getDouble("me_to_pay");


        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.connection_activity_collapsingtoolbar);
        collapsingToolbarLayout.setTitleEnabled(true);
        toPayTextView = findViewById(R.id.connection_activity_topay_textView);
        connectionDPImageView = findViewById(R.id.connection_activity_image);
        txnRecyclerView = (RecyclerView) findViewById(R.id.transaction_recyclerview);

        final TransactionAdapter adapter = new TransactionAdapter(this, txnList);
        if(meToPay>0){
            toPayTextView.setText("You have to pay "+displayName+" ₹"+meToPay);
        }else if(meToPay<0){
            toPayTextView.setText(displayName+ " have to pay you ₹"+Math.abs(meToPay));
        }else {
            toPayTextView.setText("You both are settled up");
        }
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

        TransactionModel model = new TransactionModel("Sahil paid you ₹786","Pizza at mall ozone galleria dhanbad jharkhand 826004 india asia world solar system milky way","na","na","na",55555555);
        txnList.add(model);txnList.add(model);
        TransactionModel model2 = new TransactionModel("You have to pay Sagar Sinha Ji Jhunjhunwala patel Nagar ISM ₹900","Pizza at mall ozone","na","na","na",55555555);
        txnList.add(model2);txnList.add(model);txnList.add(model2);txnList.add(model2);txnList.add(model2);txnList.add(model);txnList.add(model2);txnList.add(model2);txnList.add(model2);txnList.add(model);txnList.add(model2);txnList.add(model2);
        txnRecyclerView.setLayoutManager(new LinearLayoutManager(ConnectionActivity.this));
        txnRecyclerView.setAdapter(adapter);
    }
}

