package com.ashish.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashish.mymall.ui.my_cart.MyCartFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {

    public static List<CartItemModel> cartItemModelList;
    private Toolbar toolbar;
    private RecyclerView deliveryRecyclerView;
    private Button changeORaddNewAddressBtn, continueBtn;
    public final static int SELECT_ADDRESS =0;
    private TextView totalAmount,fullname,fullAddress,pincode,orderId;
    private Dialog loadingDialog, paymentMethodDialog;
    private ImageButton paytm,cod,continueShoppingBtn;
    private ConstraintLayout orderConfirmationLayout;
    private boolean successResponse=false;
    public static boolean fromCart;
    private String name,mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
//////////loading dialog

        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

//////////loading dialog

//////////paymentMethodDialog

        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm = paymentMethodDialog.findViewById(R.id.paytm);
        cod = paymentMethodDialog.findViewById(R.id.cod_btn);

//////////paymentMethodDialog

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Delivery");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        totalAmount=findViewById(R.id.total_cart_amount);
        fullname=findViewById(R.id.fullname);
        fullAddress=findViewById(R.id.address);
        pincode=findViewById(R.id.pincode);
        continueBtn=findViewById(R.id.cart_continue_btn);
        orderConfirmationLayout=findViewById(R.id.order_confirmation_layout);
        orderId=findViewById(R.id.order_id);
        continueShoppingBtn=findViewById(R.id.continue_shopping_btn);

        deliveryRecyclerView=findViewById(R.id.delivery_recyclerview);
        changeORaddNewAddressBtn=findViewById(R.id.change_or_add_address_button);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(linearLayoutManager);

        CartAdapter cartAdapter=new CartAdapter(cartItemModelList,totalAmount,false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeORaddNewAddressBtn.setVisibility(View.VISIBLE);
        changeORaddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeliveryActivity.this,MyAddressesActivity.class).putExtra("MODE",SELECT_ADDRESS));
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethodDialog.show();
            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeliveryActivity.this,OTPverificationActivity.class).putExtra("mobileNo",mobileNo.substring(0,10)));
            }
        });



        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethodDialog.dismiss();
                loadingDialog.show();
                if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }

                final String M_id="ycfywn51345706323528";
                final String customer_id= FirebaseAuth.getInstance().getUid();
                final String order_id= UUID.randomUUID().toString().substring(0,28);
                final String url="https://mymallapplication.000webhostapp.com/paytm/generateChecksum.php";
                final String callBackUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

                final RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.has("CHECKSUMHASH")){
                                String CHECKSUMHASH=jsonObject.getString("CHECKSUMHASH");

                                PaytmPGService paytmPGService = PaytmPGService.getProductionService();
                                HashMap<String, String> paramMap = new HashMap<String, String>();
                                paramMap.put("MID",M_id);
                                paramMap.put("ORDER_ID",order_id);
                                paramMap.put("CUST_ID",customer_id);
                                paramMap.put("CHANNEL_ID","WAP");
                                paramMap.put("TXN_AMOUNT",totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2));
                                paramMap.put("WEBSITE","WEBSTAGING");
                                paramMap.put("INDUSTRY_TYPE_ID","Retail");
                                paramMap.put("CALLBACK_URL",callBackUrl);
                                paramMap.put("CHECKSUMHASH",CHECKSUMHASH);

                                PaytmOrder order=new PaytmOrder(paramMap);
                                paytmPGService.initialize(order,null);
                                paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void onTransactionResponse(Bundle bundle) {
//                                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + bundle.toString(), Toast.LENGTH_LONG).show();
                                        if(bundle.getString("STATUS").equals("TXN_SUCCESS")){
                                            successResponse=true;

                                            if(MainActivity.mainActivity != null){
                                                MainActivity.mainActivity.finish();
                                                MainActivity.mainActivity=null;
                                                MainActivity.showCart=false;
                                            }

                                            if(ProductDetailsActivity.productDetailsActivity != null){
                                                ProductDetailsActivity.productDetailsActivity.finish();
                                                ProductDetailsActivity.productDetailsActivity=null;
                                            }

                                            if(fromCart){
                                                loadingDialog.show();
                                                Map<String, Object> updateCart = new HashMap<>();
                                                long cartListSize=0;
                                                final List<Integer> indexList = new ArrayList<>();
                                                for (int x = 0; x < DBquerries.cartList.size(); x++) {
                                                    if(!cartItemModelList.get(x).isInStock()){
                                                        updateCart.put("product_ID_" + cartListSize, cartItemModelList.get(x).getProductID());
                                                        cartListSize++;
                                                    }else {
                                                        indexList.add(x);
                                                    }

                                                }
                                                updateCart.put("list_size",cartListSize);

                                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                                                        .collection("USER_DATA").document("MY_CART").set(updateCart)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (int x = 0; x < indexList.size(); x++) {
                                                                        DBquerries.cartList.remove(indexList.get(x).intValue());
                                                                        DBquerries.cartItemModelList.remove(indexList.get(x).intValue());
                                                                        DBquerries.cartItemModelList.remove(DBquerries.cartItemModelList.size()-1);
                                                                    }
                                                                } else {
                                                                    String error=task.getException().getMessage();
                                                                    Toast.makeText(DeliveryActivity.this,error,Toast.LENGTH_SHORT).show();
                                                                }
                                                                loadingDialog.dismiss();
                                                            }
                                                        });

                                            }
                                            continueBtn.setEnabled(false);
                                            changeORaddNewAddressBtn.setEnabled(false);
                                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                            orderId.setText("Order ID "+bundle.getString("ORDERID"));
                                            orderConfirmationLayout.setVisibility(View.VISIBLE);
                                            continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    finish();
                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void networkNotAvailable() {
                                        Toast.makeText(getApplicationContext(), "Network connection error!", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String s) {
                                        Toast.makeText(getApplicationContext(), "Authentication failed: server error" + s, Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void someUIErrorOccurred(String s) {
                                        Toast.makeText(getApplicationContext(), "UI error " + s, Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int i, String s, String s1) {
                                        Toast.makeText(getApplicationContext(), "Unable to load web page  " + s.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onBackPressedCancelTransaction() {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled! ", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onTransactionCancel(String s, Bundle bundle) {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled "+bundle.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismiss();
                        Toast.makeText(DeliveryActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID",M_id);
                        paramMap.put("ORDER_ID",order_id);
                        paramMap.put("CUST_ID",customer_id);
                        paramMap.put("CHANNEL_ID","WAP");
                        paramMap.put("TXN_AMOUNT",totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2));
                        paramMap.put("WEBSITE","WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID","Retail");
                        paramMap.put("CALLBACK_URL",callBackUrl);
                        return paramMap;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        name=DBquerries.addressesModelList.get(DBquerries.selectedAddress).getFullname();
        mobileNo=DBquerries.addressesModelList.get(DBquerries.selectedAddress).getMobileNo();
        fullname.setText(name+" - "+mobileNo);
        fullAddress.setText(DBquerries.addressesModelList.get(DBquerries.selectedAddress).getAddress());
        pincode.setText(DBquerries.addressesModelList.get(DBquerries.selectedAddress).getPincode());

    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        if(successResponse){
            finish();
            return;
        }
        super.onBackPressed();
    }
}
