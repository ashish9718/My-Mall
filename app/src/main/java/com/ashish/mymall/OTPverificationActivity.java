package com.ashish.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.DelayQueue;

import es.dmoral.toasty.Toasty;

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


        Random random = new Random();
        final int OTP_no=random.nextInt(999999-111111)+111111;
        String SMS_API="https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                verifyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(otp.getText().toString().equals(String.valueOf(OTP_no))){

                            Map<String, Object> updateStatus=new HashMap<>();
                            updateStatus.put("Order Status","Ordered");
                            final String order_id=getIntent().getStringExtra("order_id");
                            FirebaseFirestore.getInstance().collection("ORDERS").document(order_id)
                                    .update(updateStatus)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Map<String, Object> userOrder = new HashMap<>();
                                                userOrder.put("order_id",order_id);
                                                userOrder.put("time", FieldValue.serverTimestamp());
                                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id)
                                                        .set(userOrder)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    DeliveryActivity.ordered=true;  //my code
                                                                    DeliveryActivity.codOrderConfirmed=true;
                                                                    finish();
                                                                }else {
                                                                    Toasty.warning(OTPverificationActivity.this, "Failed to update user orders list !", Toast.LENGTH_LONG,true).show();
                                                                }
                                                            }
                                                        });
                                            }else {
                                                Toasty.error(OTPverificationActivity.this, "Order Cancelled !", Toast.LENGTH_LONG,true).show();
                                            }
                                        }
                                    });


                        }else {
                            Toasty.error(OTPverificationActivity.this,"OTP incorrect !",Toast.LENGTH_SHORT,true).show();
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                Toast.makeText(OTPverificationActivity.this,"Failed to send OTP verification code!",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization","B5WzCKYbZuvV7tdGwga2MDs431Oj8xIlciqA9USFQeonfkh6mROPywVDBz6qv2muHjaT1Afibx9QIsRX");

                return headers;

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<String, String>();
                body.put("sender_id","FSTSMS");
                body.put("language","english");
                body.put("route","qt");
                body.put("numbers",userNo);
                body.put("message","23115");
                body.put("variables","{#BB#}");
                body.put("variables_values", String.valueOf(OTP_no));
                return body;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue=Volley.newRequestQueue(OTPverificationActivity.this);
        requestQueue.add(stringRequest);
    }
}
