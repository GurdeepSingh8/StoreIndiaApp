/*package com.example.otpverificationusingfirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    Button catOil,catDal,catSabun,catSafai,catHousehold,catAnaj,catMasale,catBeauty,
            catVeg,catMedical,catHardware,catElectronics,catFarming,catPets,catOthers;
    RecyclerView recview;
    RecyclerProductAdapter productAdapter;
    FirebaseFirestore database;
    ArrayList<Product> productList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard);
        catAnaj = findViewById(R.id.cat_anaaj);
        catBeauty = findViewById(R.id.cat_beauty);
        catDal = findViewById(R.id.cat_dal);
        catMedical = findViewById(R.id.cat_medical);
        catElectronics = findViewById(R.id.cat_electric);
        catFarming = findViewById(R.id.cat_farming);
        catHardware = findViewById(R.id.cat_hardware);
        catHousehold = findViewById(R.id.cat_household);
        catMasale = findViewById(R.id.cat_masale);
        catOil = findViewById(R.id.cat_oil);
        catOthers = findViewById(R.id.cat_others);
        catPets = findViewById(R.id.cat_pets);
        catVeg = findViewById(R.id.cat_sabji);
        catSafai = findViewById(R.id.cat_safai);
        catSabun = findViewById(R.id.cat_sabun);

        catAnaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(CategoryActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("प्रतीक्षा करें ...");
                progressDialog.show();

                recview = findViewById(R.id.idRecyclerView);
                ViewCompat.setNestedScrollingEnabled(recview, false);
                recview.setHasFixedSize(true);
                recview.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));

                database = FirebaseFirestore.getInstance();


                productList = new ArrayList<>();
                LoadAnaj();
                productAdapter = new RecyclerProductAdapter(CategoryActivity.this, productList, productAdapter.myCallback);
                recview.setAdapter(productAdapter);

            }
        });




    }
    private void LoadAnaj() {
        CollectionReference ref = database.collection("ProductStore");
        Query query = ref.whereArrayContains("Tags",  "Anaaj");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                        if (error != null) {

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();


                            Log.e("FireStore Error!", error.getMessage());
                            return;

                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                productList.add(dc.getDocument().toObject(Product.class));

                                productAdapter.notifyDataSetChanged();
                            }
                            if (dc.getType() == DocumentChange.Type.REMOVED) {


                                productAdapter.notifyDataSetChanged();
                            }


                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                        }

                    }
                });
    }
}

 */