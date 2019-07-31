package com.arb222.udhari;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UpdateProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mChooseImageButton;
    private Button mUpdateDataButton,mCancelButton;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private ImageView mProfilePicturePreviewImageView;
    private ProgressBar mUpdateProfileDataProgressBar;

    private Uri mNewProfileImageUri;

    private StorageReference profilePictureFolderStorageReference;
    private DatabaseReference userDataFolderReference;


    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserUid = currentUser.getUid();

        mChooseImageButton = findViewById(R.id.select_new_profilepic_button);
        mCancelButton = findViewById(R.id.cancel_profiledata_button);
        mUpdateDataButton = findViewById(R.id.update_profiledata_button);
        mFirstNameEditText = findViewById(R.id.new_firstname_edittext);
        mLastNameEditText = findViewById(R.id.new_lastname_edittext);
        mProfilePicturePreviewImageView = findViewById(R.id.new_profile_pic_preview_imageview);
        mUpdateProfileDataProgressBar = findViewById(R.id.update_profiledata_progressbar);

        profilePictureFolderStorageReference = FirebaseStorage.getInstance().getReference("profilepicture");
        userDataFolderReference = FirebaseDatabase.getInstance().getReference("userinfo/" + currentUserUid);
        mChooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        mUpdateDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UpdateProfile.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                    //todo: Ghapla--- Can't upload more than once with the same activity
                } else {
                    String firstNameEntered = mFirstNameEditText.getText().toString();
                    if(firstNameEntered.isEmpty()){
                        mFirstNameEditText.setError("Enter name");
                        mFirstNameEditText.requestFocus();
                        return;
                    }
                    if(isNetworkAvailable()) {
                        uploadData();
                    }else{
                        Toast.makeText(UpdateProfile.this,"No Internet",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            mNewProfileImageUri = data.getData();
            Picasso.get().load(mNewProfileImageUri).into(mProfilePicturePreviewImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadData() {
        if (mNewProfileImageUri != null) {
            final StorageReference ref = profilePictureFolderStorageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "." + getFileExtension(mNewProfileImageUri));

            mUploadTask = ref.putFile(mNewProfileImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    mUpdateProfileDataProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(UpdateProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                            Log.d("UpdateProfile", "Picture uploaded to Firebase Storage");

                            String profilePictureUrlAtCloud;
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String profilePictureUrlAtCloud = uri.toString();

                                    String firstName = mFirstNameEditText.getText().toString().trim();
                                    String lastName = mLastNameEditText.getText().toString().trim();

                                    userDataFolderReference.child("firstName").setValue(firstName);
                                    userDataFolderReference.child("lastName").setValue(lastName);
                                    userDataFolderReference.child("profilePictureLink").setValue(profilePictureUrlAtCloud);
                                    userDataFolderReference.child("profileStatus").setValue(3);
                                }
                            });
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mUpdateProfileDataProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }
}
