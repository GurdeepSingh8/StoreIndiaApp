package com.example.otpverificationusingfirebase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutLayActivity extends AppCompatActivity implements RecyclerCheckoutAdapter.ReChAdapter {

    RecyclerCheckoutAdapter chAdapter;
    FirebaseFirestore database;
    String CustomNum;
    Button submitButton, cancelCh, EditBtn;
    PopupWindow popupWindow;
    ConstraintLayout consLay;
    LottieAnimationView lottieCh, lottiePopup, emptyCart;
    ImageView complete;
    RecyclerView recViewCh;
    ArrayList<CheckoutModel> chList = new ArrayList<>();
    ArrayList<Double> b = new ArrayList<>();
    TextView totalPrice, proCount, CustomerName, Address, tvEmptyCart, greet1, greet2;
    SharedPreferences mPrefs;
    RelativeLayout reLay, fatherView;
    EditText cusNameEdit, cusAddEdit, cusAddEdit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_layout);

        recViewCh = findViewById(R.id.rv_checkout_list);
        totalPrice = findViewById(R.id.pro_total_price);
        proCount = findViewById(R.id.pro_count);
        complete = findViewById(R.id.chImage);
        lottieCh = findViewById(R.id.confirm_purchase);
        cancelCh = findViewById(R.id.btnCancel);
        CustomerName = findViewById(R.id.customerName);
        Address = findViewById(R.id.address);
        consLay = findViewById(R.id.linear);
        reLay = findViewById(R.id.checkout_cardView);
        cusNameEdit = findViewById(R.id.cusNameEdit);
        cusAddEdit = findViewById(R.id.cusAddressEdit);
        cusAddEdit2 = findViewById(R.id.cusAddEdit);
        submitButton = findViewById(R.id.submit_cus_details);
        fatherView = findViewById(R.id.Fatherch);
        emptyCart = findViewById(R.id.emptyCart);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        EditBtn = findViewById(R.id.EditBtn);
        greet1 = findViewById(R.id.greet1);
        greet2 = findViewById(R.id.greet2);
        lottiePopup = findViewById(R.id.lottieAnimationView);

        database = FirebaseFirestore.getInstance();

        mPrefs = this.getSharedPreferences("Customer details", MODE_PRIVATE);
        CustomerName.setText(mPrefs.getString("Customer name", ""));
        Address.setText(mPrefs.getString("Address", ""));

        listenerMethod();

        mPrefs = this.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        CustomNum = gson.fromJson(json, String.class);

        recViewCh.setLayoutManager(new LinearLayoutManager(this));

        chAdapter = new RecyclerCheckoutAdapter(this, chList, this);
        recViewCh.setAdapter(chAdapter);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CustomerName.getText().toString().isEmpty() || Address.getText().toString().isEmpty()
                ) {
                    EditBtn.performClick();
                } else if (Integer.parseInt(proCount.getText().toString().replaceAll("[^0-9]", "")) < 1) {
                    Toast.makeText(CheckoutLayActivity.this, "List is Empty", Toast.LENGTH_SHORT).show();
                    backButton();
                } else {
                    completeGone();
                    lottieCh.setMinAndMaxProgress(0.0f, 1.0f);
                    lottieCh.playAnimation();
                }
            }
        });

        lottieCh.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                cancelCh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lottieCh.cancelAnimation();
                        completeVisible();
                    }
                });
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onAnimationEnd(Animator animation) {
                cancelCh.setClickable(false);
                PlaceOrder();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckoutLayActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

                cusNameEdit.setVisibility(View.VISIBLE);
                cusNameEdit.setText(CustomerName.getText());
                cusAddEdit.setVisibility(View.VISIBLE);
                cusAddEdit2.setVisibility(View.VISIBLE);
                cusNameEdit.requestFocus();
                if (Address.getText().toString().contains(",")) {
                    cusAddEdit.setText(Address.getText().toString().substring(0, Address.getText().toString().indexOf(",")));
                    cusAddEdit2.setText(Address.getText().toString().substring(Address.getText().toString().indexOf(",") + 1, Address.getText().toString().indexOf("  ")));
                }
                submitButton.setVisibility(View.VISIBLE);

                CustomerName.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                EditBtn.setVisibility(View.GONE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cusNameEdit.getText().toString().isEmpty()) {
                    cusNameEdit.requestFocus();
                } else if (cusAddEdit.getText().toString().isEmpty()) {
                    cusAddEdit.requestFocus();
                } else if (cusAddEdit2.getText().toString().isEmpty()) {
                    cusAddEdit2.requestFocus();
                } else {
                    saveAddress(cusNameEdit.getText().toString(), cusAddEdit.getText().toString() + "," + cusAddEdit2.getText().toString() + "  ");

                    String s = cusAddEdit.getText() + "," + cusAddEdit2.getText() + "  ";
                    CustomerName.setText(cusNameEdit.getText());
                    submitButton.setVisibility(View.GONE);
                    cusNameEdit.setVisibility(View.GONE);
                    cusAddEdit.setVisibility(View.GONE);
                    cusAddEdit2.setVisibility(View.GONE);
                    CustomerName.setVisibility(View.VISIBLE);
                    Address.setVisibility(View.VISIBLE);
                    EditBtn.setVisibility(View.VISIBLE);
                    Address.setText(s);

                }
            }
        });

        emptyCartShow();

    }

    private void emptyCartShow() {
        if (chList.size() < 1) {
            tvEmptyCart.setVisibility(View.VISIBLE);
            emptyCart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        popupGone();
        emptyCartShow();
    }

    private void completeGone() {
        lottieCh.setVisibility(View.VISIBLE);
        cancelCh.setVisibility(View.VISIBLE);
        proCount.setVisibility(View.GONE);
        totalPrice.setVisibility(View.GONE);
        complete.setVisibility(View.GONE);
    }

    private void completeVisible() {
        complete.setVisibility(View.VISIBLE);
        proCount.setVisibility(View.VISIBLE);
        totalPrice.setVisibility(View.VISIBLE);
        lottieCh.setVisibility(View.GONE);
        cancelCh.setVisibility(View.GONE);
    }

    private void saveAddress(String name, String Address) {
        mPrefs = getSharedPreferences("Customer details", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("Customer name", name);
        editor.putString("Address", Address);
        editor.apply();
    }

    private void saveMap(String key, Object obj) {

        SharedPreferences prefs = getSharedPreferences("Last Order", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key, json);
        editor.apply();
    }

    private void PlaceOrder() {
        SharedPreferences refs = getSharedPreferences("Item details", MODE_PRIVATE);
        Map<String, ?> allEntries = refs.getAll();
        HashMap<String, Object> ord = new HashMap<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            ord.put(entry.getKey(), getItemValue2(entry.getKey()));
            saveMap(entry.getKey(),getItemValue2(entry.getKey()));
        }
        ord.put("User",CustomerName.getText().toString() + Address.getText());


        CollectionReference dbProducts = database.collection("New Orders");
        dbProducts.add(ord).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(DocumentReference docRef) {
                ShowPopup();
                chList.clear();
                chAdapter.notifyDataSetChanged();
                totalPrice.setText("कुल योग : ₹ 0.0");
                proCount.setText("कुल प्रोडक्ट : 0");
                completeVisible();
                SharedPreferences sPrefs = getSharedPreferences("Item details", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = sPrefs.edit();
                prefsEditor.clear();
                prefsEditor.apply();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CheckoutLayActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void listenerMethod() {
        SharedPreferences Prefs = this.getSharedPreferences("Item details", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = Prefs.getAll();
        ArrayList<String> or = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            getItemValue(entry.getKey());
        }
        String a = "कुल योग : ₹ " + sum(b);
        totalPrice.setText(a);
        String c = "कुल प्रोडक्ट : " + chList.size();
        proCount.setText(c);
    }

    public String sum(@NonNull ArrayList<Double> values) {
        double result = 0;
        for (double value : values)
            result += value;
        final DecimalFormat df = new DecimalFormat(".00");
        return df.format(result);
    }

    public void backButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
    }

    public void ShowPopup() {
        popupVisible();
        lottiePopup.setMinAndMaxProgress(0.3f, 1.0f);
        lottiePopup.playAnimation();
        lottiePopup.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                popupVisible();
                Intent intent = new Intent(getApplicationContext(), OrderDetails.class);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                popupVisible();
                Intent intent = new Intent(getApplicationContext(), OrderDetails.class);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public void popupVisible() {
        lottiePopup.setVisibility(View.VISIBLE);
        greet1.setVisibility(View.VISIBLE);
        greet2.setVisibility(View.VISIBLE);
    }

    public void popupGone() {
        lottiePopup.setVisibility(View.GONE);
        greet1.setVisibility(View.GONE);
        greet2.setVisibility(View.GONE);
    }

    private String getItemValue2(String ProductName) {
        SharedPreferences Prefs = this.getSharedPreferences("Item details", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Prefs.getString(ProductName, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> obj = gson.fromJson(json, type);
        String price = String.valueOf(obj.get("Item Price"));
        String quantity = String.valueOf(obj.get("Item Quantity"));
        String total = String.valueOf(obj.get("Item Total"));

        return "Price - " + price + ", Quantity - " + quantity + ", Total - " + total;
    }

    private void getItemValue(String ProductName) {
        SharedPreferences Prefs = this.getSharedPreferences("Item details", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = Prefs.getString(ProductName, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> obj = gson.fromJson(json, type);
        String name = String.valueOf(obj.get("Item Name"));
        String price = String.valueOf(obj.get("Item Price"));
        String quantity = String.valueOf(obj.get("Item Quantity"));
        String total = String.valueOf(obj.get("Item Total"));
        chList.add(new CheckoutModel(name, price, quantity, total));
        b.add(Double.parseDouble(total));
    }

    @Override
    public void OnRemove(int pos, Double nm) {
        chList.remove(pos);
        chAdapter.notifyItemRemoved(pos);
        b.remove(nm);
        String a = "कुल योग : ₹ " + sum(b);
        totalPrice.setText(a);
        String c = "कुल प्रोडक्ट : " + chList.size();
        proCount.setText(c);
        emptyCartShow();
    }
}

