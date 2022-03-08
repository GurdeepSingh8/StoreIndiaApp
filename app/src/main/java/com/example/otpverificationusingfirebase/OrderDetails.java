package com.example.otpverificationusingfirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class OrderDetails extends AppCompatActivity {

    TextView orderArr, youSaved, packed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        orderArr = findViewById(R.id.orderArriving);
        youSaved = findViewById(R.id.youSaved);
        packed = findViewById(R.id.packed);

        orderArr.setText("Order Placed");
        youSaved.setText("You saved rs. 25 with this order");

        packed.setText("Aapka Order Sham 7 se 8 baje ke bich aapke ghar pahunch jayega, kripya dhan tayar rakhen");
    }
}