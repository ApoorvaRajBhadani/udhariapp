package com.arb222.udhari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.arb222.udhari.Authentication.PhoneNoEntryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);


        if (checkSelfPermission(Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                //Toast.makeText(MainActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);

            } else {
                Intent intent = new Intent(StartActivity.this, PhoneNoEntryActivity.class);
                startActivity(intent);
            }
            finish();
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        //Toast.makeText(MainActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(StartActivity.this, PhoneNoEntryActivity.class);
                        startActivity(intent);
                    }
                    finish();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }
}
