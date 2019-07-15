package com.arb222.udhari;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    View view;
    private DatabaseReference userinfoDatabaseReference;
    private UserInfo currentUserInfo;
    private TextView textViewProfileFragment;
    private ImageView profilePicImageView;
    private Button mOpenUpdateProfileActivityButton;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        textViewProfileFragment = (TextView) view.findViewById(R.id.profile_details);
        mOpenUpdateProfileActivityButton = (Button) view.findViewById(R.id.update_profile_openactivity_button);
        profilePicImageView = (ImageView) view.findViewById(R.id.profile_pic_imageview);
        userinfoDatabaseReference = FirebaseDatabase.getInstance().getReference("userinfo");
        currentUserInfo = new UserInfo();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserUid = currentUser.getUid();
        userinfoDatabaseReference.child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserInfo = dataSnapshot.getValue(UserInfo.class);
                Log.d("Test", currentUserInfo.getPhoneNumber());
                updateUi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mOpenUpdateProfileActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UpdateProfile.class));
            }
        });
        updateUi();

        return view;
    }

    private void updateUi() {
        textViewProfileFragment.setText("Name :" + currentUserInfo.getFirstName() + " " + currentUserInfo.getLastName() + "\nPhNo " + currentUserInfo.getPhoneNumber() + "\nStatus " + currentUserInfo.getProfileStatus() + "\nDp url " + currentUserInfo.getProfilePictureLink());
        if (currentUserInfo.getProfileStatus() == 3) {
            Picasso.get().load(currentUserInfo.getProfilePictureLink())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(profilePicImageView);
        }
    }
}
