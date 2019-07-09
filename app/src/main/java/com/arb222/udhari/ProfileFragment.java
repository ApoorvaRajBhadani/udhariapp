package com.arb222.udhari;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    View view;
    private DatabaseReference userinfoDatabaseReference;
    private UserInfo currentUserInfo;
    private TextView textViewProfileFragment;
    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile,container,false);
        textViewProfileFragment = (TextView) view.findViewById(R.id.profile_details);
        userinfoDatabaseReference = FirebaseDatabase.getInstance().getReference("userinfo");
        currentUserInfo = new UserInfo();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserUid = currentUser.getUid();
        userinfoDatabaseReference.child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserInfo=dataSnapshot.getValue(UserInfo.class);
                Log.d("Test",currentUserInfo.getPhoneNumber());
                updateUi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        updateUi();

        return view;
    }

    private void updateUi()
    {
        textViewProfileFragment.setText("Name :"+currentUserInfo.getFirstName()+" "+currentUserInfo.getLastName()+"\nPhNo "+currentUserInfo.getPhoneNumber()+"\nStatus "+currentUserInfo.getProfileStatus()+"\nDp url "+currentUserInfo.getProfilePictureLink());

    }
}
