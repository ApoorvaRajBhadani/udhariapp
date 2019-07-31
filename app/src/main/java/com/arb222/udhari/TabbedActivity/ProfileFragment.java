package com.arb222.udhari.TabbedActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arb222.udhari.Authentication.PhoneNoEntryActivity;
import com.arb222.udhari.ContactDB.ContactDbHelper;
import com.arb222.udhari.R;
import com.arb222.udhari.UpdateProfile;
import com.arb222.udhari.POJO.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.arb222.udhari.Base.SHAREDPREF_AUTHUSERDISPLAYNAME;
import static com.arb222.udhari.Base.SHAREDPREF_AUTHUSERPHONENUMBER;
import static com.arb222.udhari.Base.SHAREDPREF_FCMTOKEN;
import static com.arb222.udhari.ContactDB.ContactContract.ContactEntry.TABLE_NAME;

public class ProfileFragment extends Fragment {
    View view;
    private DatabaseReference userinfoDatabaseReference;
    private UserInfo currentUserInfo;
    private TextView phoneTextView, nameTextView;
    private ImageView profilePicImageView;
    private CircleImageView mOpenUpdateProfileActivityImageButton,signoutImageButton;
    SharedPreferences pref;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        phoneTextView = (TextView) view.findViewById(R.id.profile_frag_phone_textview);
        nameTextView = (TextView) view.findViewById(R.id.profile_frag_name_textview);
        mOpenUpdateProfileActivityImageButton = (CircleImageView) view.findViewById(R.id.update_profile_openactivity_button);
        signoutImageButton = (CircleImageView) view.findViewById(R.id.signout_imageviewbutton);
        profilePicImageView = (ImageView) view.findViewById(R.id.profile_pic_imageview);
        userinfoDatabaseReference = FirebaseDatabase.getInstance().getReference("userinfo");
        userinfoDatabaseReference.keepSynced(true);
        currentUserInfo = new UserInfo();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserUid = currentUser.getUid();
        userinfoDatabaseReference.child(currentUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserInfo = dataSnapshot.getValue(UserInfo.class);
                Log.d("Profile Fragment", currentUserInfo.getPhoneNumber());
                pref.edit().putString(SHAREDPREF_AUTHUSERDISPLAYNAME,currentUserInfo.getFirstName() + " " + currentUserInfo.getLastName()).apply();
                pref.edit().putString(SHAREDPREF_AUTHUSERPHONENUMBER,currentUserInfo.getPhoneNumber()).apply();
                updateUi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mOpenUpdateProfileActivityImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UpdateProfile.class));
            }
        });
        signoutImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("fcmtoken").child(authUserUid);
                ref.keepSynced(true);
                ref.child("token").setValue("null");
                ContactDbHelper mDbHelper = new ContactDbHelper(getActivity());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                db.delete(TABLE_NAME,null,null);
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(),"Signed Out",Toast.LENGTH_SHORT).show();
                getActivity().finish();
                startActivity(new Intent(getActivity(), PhoneNoEntryActivity.class));
            }
        });
        updateUiinit();

        return view;
    }

    private void updateUi() {
        phoneTextView.setText(currentUserInfo.getPhoneNumber());
        switch (currentUserInfo.getProfileStatus()) {
            case 1:
                nameTextView.setText("Not Available");
                break;
            case 2:
                nameTextView.setText(currentUserInfo.getFirstName() + " " + currentUserInfo.getLastName());
                break;
            case 3:
                nameTextView.setText(currentUserInfo.getFirstName() + " " + currentUserInfo.getLastName());
                Picasso.get().load(currentUserInfo.getProfilePictureLink())
                        .placeholder(R.drawable.user)
                        .fit()
                        .centerCrop()
                        .into(profilePicImageView);
                break;
            default:
                break;
        }
    }

    private void updateUiinit(){
        String displayName = pref.getString(SHAREDPREF_AUTHUSERDISPLAYNAME,"Loading...");
        nameTextView.setText(displayName);
        String phoneNumber = pref.getString(SHAREDPREF_AUTHUSERPHONENUMBER,"Loading...");
        phoneTextView.setText(phoneNumber);
    }
}
