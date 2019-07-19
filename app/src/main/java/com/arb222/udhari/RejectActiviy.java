package com.arb222.udhari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.arb222.udhari.POJO.Notification;
import com.arb222.udhari.POJO.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RejectActiviy extends AppCompatActivity {

    private int rejectingUserType = 0;
    private Transaction originalTxn;

    public void init(final String originalTxnId, final String connId, final String connectedTo, final int status){
        Toast.makeText(RejectActiviy.this,"init",Toast.LENGTH_SHORT).show();
        DatabaseReference rejectingUserConcnsRef = FirebaseDatabase.getInstance().getReference("userconnection").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        rejectingUserConcnsRef.child(connId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rejectingUserType = Integer.parseInt(dataSnapshot.child("myType").getValue().toString());
                init2(originalTxnId,connId,connectedTo,status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void init2(final String originalTxnId, final String connId, final String connectedTo, final int status){
        DatabaseReference txnsOfConnIdRef = FirebaseDatabase.getInstance().getReference("connectiontxns").child(connId);
        txnsOfConnIdRef.child(originalTxnId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                originalTxn = dataSnapshot.getValue(Transaction.class);
                rejectATransaction(originalTxnId,connectedTo,connId,status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void rejectATransaction(String originalTxnId, String connectedTo, String connId, int originalStatusRejectingUser) {
        Log.d("HI","rejecting");
        long timestampRejection = System.currentTimeMillis();
        String rejectingUserUid = FirebaseAuth.getInstance().getUid();
        String otherUserUid = connectedTo;
        //rejectingUserType is set by Firebase Data by init method
        int otherUserType;
        if (rejectingUserType == 1) {
            otherUserType = 2;
        } else if (rejectingUserType == 2) {
            otherUserType = 1;
        }
        Transaction newTxn = new Transaction(rejectingUserType,
                originalTxn.getAmount(),
                originalTxn.getPaidBy2(),
                originalTxn.getPaidBy1(),
                originalTxn.getPaidFor2(),
                originalTxn.getPaidFor1(),
                timestampRejection,
                "\""+originalTxn.getDesc()+"\" rejected",
                originalTxnId,
                "yes",
                "null");
        String newTxnId= updateTransaction(newTxn,connId);
        String onesUid="",twosUid="";
        if(rejectingUserType==1){
            onesUid = rejectingUserUid;
            twosUid = otherUserUid;
        }
        else if(rejectingUserType==2){
            twosUid = rejectingUserUid;
            onesUid = otherUserUid;
        }
        updatePayForOne(newTxn,onesUid,twosUid,connId);
        updatePayForTwo(newTxn,onesUid,twosUid,connId);
        DatabaseReference originalTxnRef = FirebaseDatabase.getInstance().getReference("connectiontxns").child(connId).child(originalTxnId);
        originalTxnRef.child("deleteOther").setValue("myself");
        originalTxnRef.child("reference").setValue(newTxnId);

    }

    private String updateTransaction(Transaction newTxn,String connId){
        DatabaseReference txnsOfConnIdRef = FirebaseDatabase.getInstance().getReference("connectiontxns").child(connId);
        String newTxnId = txnsOfConnIdRef.push().getKey();
        newTxn.setTransactionId(newTxnId);
        txnsOfConnIdRef.child(newTxnId).setValue(newTxn);
        Toast.makeText(RejectActiviy.this,"Hi",Toast.LENGTH_SHORT).show();
        return newTxnId;
    }

    private void updatePayForOne(final Transaction newTxn, final String onesUid, final String twosUid, final String connId){
        final DatabaseReference onesUserConnRef = FirebaseDatabase.getInstance().getReference("userconnection").child(onesUid);
        onesUserConnRef.child(connId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double payAddnl = newTxn.getPaidFor1() - newTxn.getPaidBy1();
                Double initialPay = Double.parseDouble(dataSnapshot.child("pay").getValue().toString());
                long lastContacted = newTxn.getTimestamp();
                onesUserConnRef.child(connId).child("pay").setValue(initialPay+payAddnl);
                onesUserConnRef.child(connId).child("lastContacted").setValue(lastContacted);
                String notice="";
                if(payAddnl>0) notice = "You are charged-back by";
                else if(payAddnl<0) notice="You are paid-back by";
                Notification newNotifForOne = new Notification(notice,newTxn.getTransactionId(),twosUid,connId,newTxn.getDesc(),Math.abs(payAddnl),newTxn.getTimestamp(),4);
                updateNotifForOne(newNotifForOne,onesUid,newTxn.getReference());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateNotifForOne(Notification newNotifForOne,String onesUid,String orgnlTxnId){
        DatabaseReference onesUsernotificationRef = FirebaseDatabase.getInstance().getReference("usernotification").child(onesUid);
        onesUsernotificationRef.child(newNotifForOne.getTxnId()+"").setValue(newNotifForOne);
        onesUsernotificationRef.child(orgnlTxnId).child("status").setValue(3);
    }

    private void updatePayForTwo(final Transaction newTxn, final String onesUid, final String twosUid, final String connId){
        final DatabaseReference onesUserConnRef = FirebaseDatabase.getInstance().getReference("userconnection").child(twosUid);
        onesUserConnRef.child(connId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double payAddnl = newTxn.getPaidFor2() - newTxn.getPaidBy2();
                Double initialPay = Double.parseDouble(dataSnapshot.child("pay").getValue().toString());
                long lastContacted = newTxn.getTimestamp();
                onesUserConnRef.child(connId).child("pay").setValue(initialPay+payAddnl);
                onesUserConnRef.child(connId).child("lastContacted").setValue(lastContacted);
                String notice="";
                if(payAddnl>0) notice = "You are charged-back by";
                else if(payAddnl<0) notice="You are paid-back by";
                Notification newNotifForTwo = new Notification(notice,newTxn.getTransactionId(),onesUid,connId,newTxn.getDesc(),Math.abs(payAddnl),newTxn.getTimestamp(),4);
                updateNotifForTwo(newNotifForTwo,twosUid,newTxn.getReference());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateNotifForTwo(Notification newNotifForTwo,String twosUid,String orgnlTxnId){
        DatabaseReference twosUsernotificationRef = FirebaseDatabase.getInstance().getReference("usernotification").child(twosUid);
        twosUsernotificationRef.child(newNotifForTwo.getTxnId()+"").setValue(newNotifForTwo);
        twosUsernotificationRef.child(orgnlTxnId).child("status").setValue(3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_activiy);

        Bundle bundle = getIntent().getExtras();
        final String connectedTo = bundle.getString("conto"),
                txid = bundle.getString("txid"),
                connectionId = bundle.getString("conid");
        final int status = bundle.getInt("status");

        init(txid,connectionId,connectedTo,status);
        finish();
    }
}