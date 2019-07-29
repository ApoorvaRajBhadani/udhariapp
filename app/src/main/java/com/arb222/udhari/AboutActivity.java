package com.arb222.udhari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    ImageView linkedinButton;
    Button privacypolicyButton,tutorialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        linkedinButton = findViewById(R.id.linkedinbutton);
        privacypolicyButton = findViewById(R.id.about_privacypolicy_button);
        tutorialButton = findViewById(R.id.about_tutorial_button);

        linkedinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this,"Loading profile...",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/apoorva222g/")));
            }
        });

        privacypolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this,"Loading...",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://moneyfiedapps.blogspot.com/2019/07/privacy-policy.html")));
            }
        });

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this,"Loading...",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://moneyfiedapps.blogspot.com/2019/07/1.html")));
            }
        });
    }
}
