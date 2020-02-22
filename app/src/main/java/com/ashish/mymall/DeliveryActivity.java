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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.ashish.mymall.ui.my_cart.MyCartFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.paytm.pgsdk.PaytmOrder;

import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {

    public static boolean ordered=false;  //my code
    public static List<CartItemModel> cartItemModelList;
    private Toolbar toolbar;
    private RecyclerView deliveryRecyclerView;
    private Button changeORaddNewAddressBtn, continueBtn;
    public final static int SELECT_ADDRESS = 0;
    private TextView totalAmount, fullname, fullAddress, pincode, orderId;
    private Dialog  paymentMethodDialog;
    public static Dialog loadingDialog;
    private ImageButton paytm, cod, continueShoppingBtn;
    private ConstraintLayout orderConfirmationLayout;
    private boolean successResponse = false;
    public static boolean fromCart, codOrderConfirmed = false, getQTYIDs = true;
    private String name, mobileNo,paymentMethod;
    private String order_id;
    private FirebaseFirestore firebaseFirestore;
    public static CartAdapter cartAdapter;
    private TextView codBtnTitle;
    private View divider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        firebaseFirestore = FirebaseFirestore.getInstance();
        getQTYIDs = true;
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
        codBtnTitle = paymentMethodDialog.findViewById(R.id.cod_btn_title);
        divider = paymentMethodDialog.findViewById(R.id.payment_divider);

//////////paymentMethodDialog
        order_id = UUID.randomUUID().toString().substring(0, 28);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Delivery");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullname = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        continueBtn = findViewById(R.id.cart_continue_btn);
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        orderId = findViewById(R.id.order_id);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);

        deliveryRecyclerView = findViewById(R.id.delivery_recyclerview);
        changeORaddNewAddressBtn = findViewById(R.id.change_or_add_address_button);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(linearLayoutManager);

        cartAdapter = new CartAdapter(cartItemModelList, totalAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeORaddNewAddressBtn.setVisibility(View.VISIBLE);
        changeORaddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQTYIDs = false;
                startActivity(new Intent(DeliveryActivity.this, MyAddressesActivity.class).putExtra("MODE", SELECT_ADDRESS));
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allProductsAvailable=true;
                for(CartItemModel cartItemModel:cartItemModelList){
                    if(cartItemModel.isQtyError()){
                        allProductsAvailable=false;
                        break;
                    }
                    if(cartItemModel.getType() == CartItemModel.CART_ITEM) {
                        if (!cartItemModel.isCOD()) {
                            cod.setEnabled(false);
                            cod.setAlpha(0.5f);
                            codBtnTitle.setAlpha(0.5f);
                            break;
                        } else {
                            cod.setEnabled(true);
                            cod.setAlpha(1f);
                            codBtnTitle.setAlpha(1f);
                        }
                    }
                }
                if(allProductsAvailable){
                    paymentMethodDialog.show();
                }
            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod="COD";
                placeOrderDetails();
            }
        });


        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod="PAYTM";
                placeOrderDetails();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ///////accessing quantity
        if (getQTYIDs) {
            loadingDialog.show();
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                for (int y = 0; y < cartItemModelList.get(x).getProductQuantity(); y++) {
                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> timeStamp = new HashMap<>();
                    timeStamp.put("time", FieldValue.serverTimestamp());

                    final int finalX = x;
                    final int finalY = y;
                    firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(quantityDocumentName)
                            .set(timeStamp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       cartItemModelList.get(finalX).getQtyIDs().add(quantityDocumentName);
                                       if (finalY + 1 == cartItemModelList.get(finalX).getProductQuantity()) {
                                           firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductID()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(cartItemModelList.get(finalX).getStockQuantity()).get()
                                                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                           if (task.isSuccessful()) {
                                                               List<String> serverQuantity = new ArrayList<>();

                                                               for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                   serverQuantity.add(queryDocumentSnapshot.getId());
                                                               }
                                                               long availableQty = 0;
                                                               boolean noLongerAvailable = true;
                                                               for (String qtyID : cartItemModelList.get(finalX).getQtyIDs()) {
                                                                   cartItemModelList.get(finalX).setQtyError(false);
                                                                   if (!serverQuantity.contains(qtyID)) {
                                                                       if(noLongerAvailable){
                                                                           cartItemModelList.get(finalX).setInStock(false);
                                                                       }else {
                                                                           cartItemModelList.get(finalX).setQtyError(true);
                                                                           cartItemModelList.get(finalX).setMaxQuantity(availableQty);
                                                                           Toast.makeText(DeliveryActivity.this, "Sorry, Required amount of quantity is not available", Toast.LENGTH_SHORT).show();
                                                                       }

                                                                   } else {
                                                                       availableQty++;
                                                                       noLongerAvailable = false;
                                                                   }
                                                               }
                                                               cartAdapter.notifyDataSetChanged();
                                                           } else {
                                                               String error = task.getException().getMessage();
                                                               Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                           }
                                                           loadingDialog.dismiss();
                                                       }
                                                   });

                                       }

                                   }else {
                                       loadingDialog.dismiss();
                                       String error = task.getException().getMessage();
                                       Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                   }
                                }
                            });

                }
            }
        } else {
            getQTYIDs = true;
        }
        ///////accessing quantity
        name = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getName();
        mobileNo = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getMobileNo();
        if(DBquerries.addressesModelList.get(DBquerries.selectedAddress).getAlternateMobileNo().equals("")){
            fullname.setText(name + " - " + mobileNo);
        }else {
            fullname.setText(name + " - " + mobileNo+" or "+DBquerries.addressesModelList.get(DBquerries.selectedAddress).getAlternateMobileNo());
        }

        String flatNo = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getFlatNo();
        String city = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getCity();
        String locality = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getLocality();
        String landmark = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getLandmark();
        String state = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getState();

        if(landmark.equals("")){
            fullAddress.setText(flatNo+","+locality+","+city+","+state);
        }else {
            fullAddress.setText(flatNo+","+locality+","+landmark+","+city+","+state);
        }
        pincode.setText(DBquerries.addressesModelList.get(DBquerries.selectedAddress).getPincode());

        if (codOrderConfirmed) {
            showConfirmationLayout();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
        if (getQTYIDs) {
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                if (!successResponse) {
                    for (final String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                        final int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (qtyID.equals(cartItemModelList.get(finalX).getQtyIDs().get(cartItemModelList.get(finalX).getQtyIDs().size() - 1))) {
                                            cartItemModelList.get(finalX).getQtyIDs().clear();
                                        }
                                    }
                                });
                    }
                } else {
                    cartItemModelList.get(x).getQtyIDs().clear();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (successResponse) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    private void showConfirmationLayout() {
        codOrderConfirmed = false;
        successResponse = true;
        getQTYIDs = false;
        for (int x = 0; x < cartItemModelList.size() - 1; x++) {

            for (String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("user_ID", FirebaseAuth.getInstance().getUid());
            }

        }

        if (MainActivity.mainActivity != null) {
            MainActivity.mainActivity.finish();
            MainActivity.mainActivity = null;
            MainActivity.showCart = false;
        } else {
            MainActivity.resetMainActivity = true;
        }

        if (ProductDetailsActivity.productDetailsActivity != null) {
            ProductDetailsActivity.productDetailsActivity.finish();
            ProductDetailsActivity.productDetailsActivity = null;
        }

        /////sms for confirmation of order

        String SMS_API = "https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", "B5WzCKYbZuvV7tdGwga2MDs431Oj8xIlciqA9USFQeonfkh6mROPywVDBz6qv2muHjaT1Afibx9QIsRX");

                return headers;

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<String, String>();
                body.put("sender_id", "FSTSMS");
                body.put("language", "english");
                body.put("route", "qt");
                body.put("numbers", mobileNo);
                body.put("message", "23163");
                body.put("variables", "{#FF#}");
                body.put("variables_values", order_id);
                return body;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        requestQueue.add(stringRequest);

        /////sms for confirmation of order

        if (fromCart) {
            loadingDialog.show();
            Map<String, Object> updateCart = new HashMap<>();
            long cartListSize = 0;
            final List<Integer> indexList = new ArrayList<>();
            for (int x = 0; x < DBquerries.cartList.size(); x++) {
                if (!cartItemModelList.get(x).isInStock()) {
                    updateCart.put("product_ID_" + cartListSize, cartItemModelList.get(x).getProductID());
                    cartListSize++;
                } else {
                    indexList.add(x);
                }

            }
            updateCart.put("list_size", cartListSize);

            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA").document("MY_CART").set(updateCart)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                for (int x = 0; x < indexList.size(); x++) {
                                    DBquerries.cartList.remove(indexList.get(x).intValue());
                                    DBquerries.cartItemModelList.remove(indexList.get(x).intValue());
                                    DBquerries.cartItemModelList.remove(DBquerries.cartItemModelList.size() - 1);
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });

        }
        continueBtn.setEnabled(false);
        changeORaddNewAddressBtn.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderId.setText("Order ID " + order_id);
        orderConfirmationLayout.setVisibility(View.VISIBLE);
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void placeOrderDetails() {
        String userId=FirebaseAuth.getInstance().getUid();
        loadingDialog.show();
        for (CartItemModel cartItemModel : cartItemModelList) {
            if (cartItemModel.getType() == CartItemModel.CART_ITEM) {

                Map<String, Object> orderDetails=new HashMap<>();
                orderDetails.put("ORDER ID",order_id);
                orderDetails.put("Product Id",cartItemModel.getProductID());
                orderDetails.put("Product Image",cartItemModel.getProductImage());
                orderDetails.put("Product Title",cartItemModel.getProductTitle());
                orderDetails.put("User Id",userId);
                orderDetails.put("Product quantity",cartItemModel.getProductQuantity());
                if(cartItemModel.getCuttedPrice() != null) {
                    orderDetails.put("Cutted Price", cartItemModel.getCuttedPrice());
                }else {
                    orderDetails.put("Cutted Price","");
                }
                orderDetails.put("Product Price",cartItemModel.getProductPrice());
                if(cartItemModel.getSelectedCoupanId() != null) {
                    orderDetails.put("Coupan Id",cartItemModel.getSelectedCoupanId());
                }else {
                    orderDetails.put("Coupan Id","");
                }
                if(cartItemModel.getDiscountedPrice() != null) {
                    orderDetails.put("Discounted Price", cartItemModel.getDiscountedPrice());
                }else {
                    orderDetails.put("Discounted Price", "");
                }
                orderDetails.put("Ordered Date",FieldValue.serverTimestamp());
                orderDetails.put("Shipped Date",FieldValue.serverTimestamp());
                orderDetails.put("Packed Date",FieldValue.serverTimestamp());
                orderDetails.put("Delivered Date",FieldValue.serverTimestamp());
                orderDetails.put("Cancelled Date",FieldValue.serverTimestamp());
                orderDetails.put("Order Status","Ordered");
                orderDetails.put("Payment Method",paymentMethod);
                orderDetails.put("Address",fullAddress.getText().toString());
                orderDetails.put("FullName",fullname.getText().toString());
                orderDetails.put("Pincode",pincode.getText().toString());
                orderDetails.put("Free Coupens",cartItemModel.getFreeCoupans());
                orderDetails.put("Delivery Price",cartItemModelList.get(cartItemModelList.size()-1).getDeliveryPrice());
                orderDetails.put("Cancellation requested",false);

                firebaseFirestore.collection("ORDERS").document(order_id).collection("ORDER_ITEMS").document(cartItemModel.getProductID())
                        .set(orderDetails)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()){
                                    String error=task.getException().getMessage();
                                    Toast.makeText(DeliveryActivity.this,error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
            else {
                Map<String, Object> orderDetails=new HashMap<>();
                orderDetails.put("Total Items",cartItemModel.getTotalItems());
                orderDetails.put("Total Items Price",cartItemModel.getTotalItemsPrice());
                orderDetails.put("Delivery Price",cartItemModel.getDeliveryPrice());
                orderDetails.put("Total Amount",cartItemModel.getTotalAmount());
                orderDetails.put("Saved Amount",cartItemModel.getSavedAmount());
                orderDetails.put("Payment status","not paid");
                orderDetails.put("Order Status","Cancelled");

                firebaseFirestore.collection("ORDERS").document(order_id)
                        .set(orderDetails)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    if(paymentMethod.equals("PAYTM")){
                                        paytm();
                                    }else {
                                        cod();
                                    }
                                }else {
                                    String error=task.getException().getMessage();
                                    Toast.makeText(DeliveryActivity.this,error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    private void paytm(){
        getQTYIDs = false;
        paymentMethodDialog.dismiss();
        loadingDialog.show();
        if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        final String M_id = "ycfywn51345706323528";
        final String customer_id = FirebaseAuth.getInstance().getUid();
        final String url = "https://mymallapplication.000webhostapp.com/paytm/generateChecksum.php";
        final String callBackUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

        final RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH")) {
                        String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                        PaytmPGService paytmPGService = PaytmPGService.getStagingService();
                        HashMap<String, String> paramMap = new HashMap<>();
                        paramMap.put("MID", M_id);
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", customer_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CALLBACK_URL", callBackUrl);
                        paramMap.put("CHECKSUMHASH", CHECKSUMHASH);

                        PaytmOrder order = new PaytmOrder(paramMap);
                        paytmPGService.initialize(order, null);
                        paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                            @Override
                            public void onTransactionResponse(Bundle bundle) {
//                                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + bundle.toString(), Toast.LENGTH_LONG).show();
                                if (bundle.getString("STATUS").equals("TXN_SUCCESS")) {

                                    Map<String, Object> updateStatus=new HashMap<>();
                                    updateStatus.put("Payment status","Paid");
                                    updateStatus.put("Order Status","Ordered");

                                    firebaseFirestore.collection("ORDERS").document(order_id)
                                            .update(updateStatus)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Map<String, Object> userOrder = new HashMap<>();
                                                        userOrder.put("order_id",order_id);
                                                        userOrder.put("time",FieldValue.serverTimestamp());
                                                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id)
                                                                .set(userOrder)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            ordered=true;  //my code
                                                                            showConfirmationLayout();
                                                                        }else {
                                                                            Toast.makeText(DeliveryActivity.this, "Failed to update user orders list !", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                    }else {
                                                        Toast.makeText(DeliveryActivity.this, "Order Cancelled !", Toast.LENGTH_LONG).show();
                                                    }
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
                                Toast.makeText(getApplicationContext(), "Transaction cancelled " + bundle.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                Toast.makeText(DeliveryActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("MID", M_id);
                paramMap.put("ORDER_ID", order_id);
                paramMap.put("CUST_ID", customer_id);
                paramMap.put("CHANNEL_ID", "WAP");
                paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2));
                paramMap.put("WEBSITE", "WEBSTAGING");
                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                paramMap.put("CALLBACK_URL", callBackUrl);
                return paramMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void cod(){
        getQTYIDs = false;
        paymentMethodDialog.dismiss();
        startActivity(new Intent(DeliveryActivity.this, OTPverificationActivity.class).putExtra("mobileNo", mobileNo.substring(0, 10)).putExtra("order_id",order_id));

    }
}
