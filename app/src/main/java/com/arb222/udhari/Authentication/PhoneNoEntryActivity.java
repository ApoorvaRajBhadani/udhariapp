package com.arb222.udhari.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arb222.udhari.R;

public class PhoneNoEntryActivity extends AppCompatActivity {

    private EditText phoneNoEditText;
    private Button getOtpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_no_entry);

        phoneNoEditText = (EditText)findViewById(R.id.phone_no_edittext_id);
        getOtpButton = (Button)findViewById(R.id.get_otp_button_id);
        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneNoEditText.getText().toString().trim();

                if(number.isEmpty()){
                    phoneNoEditText.setError("Number is required");
                    phoneNoEditText.requestFocus();
                    return;
                }

                Intent intent = new Intent(PhoneNoEntryActivity.this, OTPVerificationActivity.class);
                intent.putExtra("phonenumber",number);
                startActivity(intent);
            }
        });
    }
}
