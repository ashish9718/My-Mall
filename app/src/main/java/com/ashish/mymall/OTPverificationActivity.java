package com.ashish.mymall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OTPverificationActivity extends AppCompatActivity {

    private TextView mobileNo;
    private EditText otp;
    private Button verifyBtn;
    private String userNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        mobileNo=findViewById(R.id.phone_no);
        otp=findViewById(R.id.otp);
        verifyBtn=findViewById(R.id.verify_btn);

        userNo=getIntent().getStringExtra("mobileNo");

        mobileNo.setText("Verification code has been sent to +91 "+userNo);
    }
}
