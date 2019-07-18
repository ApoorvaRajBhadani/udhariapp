package com.arb222.udhari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arb222.udhari.POJO.Notification;
import com.arb222.udhari.POJO.Transaction;
import com.arb222.udhari.POJO.UserConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewTransactionActivity extends AppCompatActivity {
    EditText amtEditText, descEditText;
    Button cancelButton, doneButton;
    RadioGroup paidbyRadioGroup;
    RadioButton paidbyotherRadioButton;
    CheckBox splitequallyCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        Bundle bundle = getIntent().getExtras();
        final String connectedTo = bundle.getString("uid_of_connection"),
                displayName = bundle.getString("display_name"),
                connectionId = bundle.getString("connection_id");
        final int myType = bundle.getInt("my_type");

        amtEditText = (EditText) findViewById(R.id.transaction_amt_edittext);
        descEditText = (EditText) findViewById(R.id.transaction_desc_edittext);
        cancelButton = (Button) findViewById(R.id.cancel_transaction_button);
        doneButton = (Button) findViewById(R.id.complete_transaction_button);
        paidbyRadioGroup = (RadioGroup) findViewById(R.id.transaction_paidby_radiogroup);
        paidbyotherRadioButton = (RadioButton) findViewById(R.id.transaction_paidbyother_radiobutton);
        splitequallyCheckBox= (CheckBox) findViewById(R.id.transaction_splitequally_checkbox);

        paidbyotherRadioButton.setText(displayName);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timestamp = System.currentTimeMillis();
                double paidby1 = 0, paidby2 = 0, paidfor1=0, paidfor2=0, amount = Double.parseDouble(amtEditText.getText().toString());
                String txnDescription = descEditText.getText().toString();
                int radioId = paidbyRadioGroup.getCheckedRadioButtonId();
                switch (radioId) {
                    case (R.id.transaction_paidbyme_radiobutton):
                        if(myType==1){
                            paidby1 = amount;
                            paidby2=0;
                            paidfor2=amount;
                            paidfor1=0;
                        }
                        if(myType==2) {
                            paidby2=amount;
                            paidby1=0;
                            paidfor1=amount;
                            paidfor2=0;
                        }
                        break;
                    case (R.id.transaction_paidbyother_radiobutton):
                        if(myType==1){
                            paidby1=0;
                            paidby2=amount;
                            paidfor1=amount;
                            paidfor2=0;
                        }
                        if(myType==2){
                            paidby1=amount;
                            paidby2=0;
                            paidfor1=0;
                            paidfor2=amount;
                        }
                        break;
                    default:
                        Toast.makeText(NewTransactionActivity.this,"Select paid by",Toast.LENGTH_SHORT).show();
                        break;
                }
                if(splitequallyCheckBox.isChecked()){
                    paidfor1=amount/2.0;
                    paidfor2=amount/2.0;
                }
                Transaction transaction = new Transaction(myType,amount,paidby1,paidby2,paidfor1,paidfor2,timestamp,txnDescription,"null","null","null");
                String txnId= updateTransaction(transaction,connectionId);
                transaction.setTransactionId(txnId);
                updatePaymentMyself(connectionId,myType,paidby1,paidby2,paidfor1,paidfor2,connectedTo,txnId,txnDescription,timestamp);
                updatePaymentOther(connectionId,myType,paidby1,paidby2,paidfor1,paidfor2,connectedTo,txnId,txnDescription,timestamp);
                Toast.makeText(NewTransactionActivity.this,"Transaction successful",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String updateTransaction(Transaction transaction,String connId){
        DatabaseReference conIdref = FirebaseDatabase.getInstance().getReference("connectiontxns").child(connId);
        String transactionId = conIdref.push().getKey();
        transaction.setTransactionId(transactionId);
        conIdref.child(transactionId).setValue(transaction);
        return transactionId;
    }
    private void updatePaymentMyself(final String connId,final int myType,final double paidby1,final double paidby2,final double paidfor1,final double paidfor2,final String connectedTo,final String txnId,final String desc,final long timestamp){
        final DatabaseReference myUserconnectionRef = FirebaseDatabase.getInstance().getReference("userconnection").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        myUserconnectionRef.child(connId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserConnection myDirectoryConnId = dataSnapshot.getValue(UserConnection.class);
                double toPayAddnl=0,initialPay = myDirectoryConnId.getPay();
                if(myType==1){
                    toPayAddnl = paidfor1 - paidby1;
                }
                if(myType==2){
                    toPayAddnl = paidfor2-paidby2;
                }
                myUserconnectionRef.child(connId).child("pay").setValue(initialPay+toPayAddnl);
                myUserconnectionRef.child(connId).child("lastContacted").setValue(timestamp);
                String notice = "";
                if(toPayAddnl>0) notice = "You were paid by";
                if(toPayAddnl<0) notice = "You paid to";
                Notification myNotif = new Notification(notice,txnId,connectedTo,connId,desc,Math.abs(toPayAddnl),timestamp,1);
                updateMyNotif(myNotif);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateMyNotif(Notification myNotif) {
        DatabaseReference myuidUsernotificationRef = FirebaseDatabase.getInstance().getReference("usernotification").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        myuidUsernotificationRef.child(myNotif.getTxnId()+"").setValue(myNotif);
    }

    private void updatePaymentOther(final String connId,final int myType,final double paidby1,final double paidby2,final double paidfor1,final double paidfor2,final String connectedTo,final String txnId,final String desc,final long timestamp){

        final DatabaseReference otherUserconnectionRef = FirebaseDatabase.getInstance().getReference("userconnection").child(connectedTo);
        otherUserconnectionRef.child(connId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserConnection otherDirectoryConnId = dataSnapshot.getValue(UserConnection.class);
                int otherType;
                double toPayAddnl=0,initialPay=otherDirectoryConnId.getPay();
                if(myType==1) {
                    otherType = 2;
                    toPayAddnl = paidfor2-paidby2;
                }
                if(myType==2){
                    otherType=1;
                    toPayAddnl = paidfor1 - paidby1;
                }
                otherUserconnectionRef.child(connId).child("pay").setValue(initialPay+toPayAddnl);
                otherUserconnectionRef.child(connId).child("lastContacted").setValue(timestamp);
                String notice = "";
                if(toPayAddnl>0)notice = "You were paid by";
                if(toPayAddnl<0) notice = "You paid to";
                Notification otherNotif = new Notification(notice,txnId,FirebaseAuth.getInstance().getCurrentUser().getUid(),connId,desc,Math.abs(toPayAddnl),timestamp,1);
                updateOtherNotif(otherNotif,connectedTo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateOtherNotif(Notification otherNotif, String connectedTo) {
        DatabaseReference otheruidUsernotificationRef = FirebaseDatabase.getInstance().getReference("usernotification").child(connectedTo);
        otheruidUsernotificationRef.child(otherNotif.getTxnId()+"").setValue(otherNotif);
    }
}
