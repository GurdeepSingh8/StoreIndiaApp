package com.example.otpverificationusingfirebase;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RecyclerProductAdapter.MyCallBack {
    LottieAnimationView lotCart;
    RecyclerView recview;
    RecyclerProductAdapter productAdapter;
    TextView Uid;
    EventListener<QuerySnapshot> Eq;
    FirebaseFirestore database;
    ArrayList<Product> productList;
    ProgressDialog progressDialog;
    Task<QuerySnapshot> Rq;

    String tagV;
    GridLayout grdlyt;
    TextView t;
    TextView total, cAll, cOil, cDal, cSabun, cSafai, cHousehold, cAnaj, cMasale, cBeauty,
            cVeg, cMedical, cHardware, cElectronics, cFarming, cPets, cOthers, order;
    Button catAll, catOil, catDal, catSabun, catSafai, catHousehold, catAnaj, catMasale, catBeauty,
            catVeg, catMedical, catHardware, catElectronics, catFarming, catPets, catOthers;
    ConstraintLayout placeOrder;
    Toolbar menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        {

            Uid = findViewById(R.id.customerName);
            Uid.setText("6307372169");

            SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);

            String myObject = Uid.getText().toString();

            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(myObject);
            prefsEditor.putString("MyObject", json);
            prefsEditor.apply();

            grdlyt = findViewById(R.id.grdlyt);

            order = findViewById(R.id.order);
            catAll = findViewById(R.id.cat_All);
            catAnaj = findViewById(R.id.Anaaj);
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

            cAll = findViewById(R.id.idAll);
            cAnaj = findViewById(R.id.idAnaaj);
            cBeauty = findViewById(R.id.idBeauty);
            cDal = findViewById(R.id.idDal);
            cMedical = findViewById(R.id.idMedical);
            cElectronics = findViewById(R.id.idElectric);
            cFarming = findViewById(R.id.idFarming);
            cHardware = findViewById(R.id.idHardware);
            cHousehold = findViewById(R.id.idHousehold);
            cMasale = findViewById(R.id.idMasale);
            cOil = findViewById(R.id.idOil);
            cOthers = findViewById(R.id.idOthers);
            cPets = findViewById(R.id.idPets);
            cVeg = findViewById(R.id.idSabji);
            cSafai = findViewById(R.id.idSafai);
            cSabun = findViewById(R.id.idSabun);
            lotCart = findViewById(R.id.lot);
            placeOrder = findViewById(R.id.btnPlaceOrder);
            total = findViewById(R.id.total_purchase);
        }

        recview = findViewById(R.id.idRecyclerView);
        ViewCompat.setNestedScrollingEnabled(recview, false);
        recview.setHasFixedSize(true);
        recview.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseFirestore.getInstance();
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("प्रतीक्षा करें ...");

        }
        tagV = "All";
        updateCatButton(catAll, cAll);
        LoadDefRecView();

        Load();

        listenerMethod();


        placeOrder.setOnClickListener(v -> {
            openCheckoutLayActivity();
        });

        {
            menu = findViewById(R.id.toolbar);
            navigationView = findViewById(R.id.nav_view);
            drawerLayout = findViewById(R.id.drawer_layout);

            setSupportActionBar(menu);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, menu, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorCreamBackground));
            toggle.getDrawerArrowDrawable().setBarThickness(10);
            toggle.getDrawerArrowDrawable().setBarLength(70);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        productAdapter.notifyDataSetChanged();
        productAdapter.notifyItemChanged(4,"");
        listenerMethod();
    }

    private void LoadDefRecView() {
        progressDialog.show();
        productList = new ArrayList<>();
        productAdapter = new RecyclerProductAdapter(this, productList, this);
        recview.setAdapter(productAdapter);
    }

    public void openCheckoutLayActivity() {
        double T = Double.parseDouble(total.getText().toString().replaceAll("[^0.-9]", ""));
        Intent checkOut = new Intent(this, CheckoutLayActivity.class);
        startActivity(checkOut);
    }

    @Override
    public void listenerMethod()
        {placeOrder.setVisibility(View.VISIBLE);
            SharedPreferences Prefs = this.getSharedPreferences("Item details", Context.MODE_PRIVATE);
            Map<String, ?> allEntries = Prefs.getAll();
            ArrayList<Double> b = new ArrayList<>();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

                double d = Double.parseDouble(getItemValue(entry.getKey()));
                b.add(d);
            }
            if (Double.parseDouble(sum(b)) < 0.1){
                placeOrder.setVisibility(View.GONE);
            }
            String a = "कुल योग : ₹ " +sum(b);
            total.setText(a);
        }

    public String sum(ArrayList<Double> values) {
        double result = 0;
        for (double value : values)
            result += value;
        final DecimalFormat df = new DecimalFormat(".00");
        return df.format(result);
    }

    private String getItemValue(String ProductName) {
        SharedPreferences Prefs = this.getSharedPreferences("Item details", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Prefs.getString(ProductName, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> obj = gson.fromJson(json, type);
        return String.valueOf(obj.get("Item Total"));
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void ButtonClicked(View v) {
        tagV = String.valueOf(v.getTag());
        t = grdlyt.findViewWithTag("t" + tagV);
        LoadDefRecView();
        updateCatButton(findViewById(v.getId()), t);

        Load();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateCatButton(Button selected, TextView coText) {

        catAll.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catAnaj.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catBeauty.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catDal.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catMedical.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catElectronics.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catFarming.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catHardware.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catHousehold.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catMasale.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catOil.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catOthers.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catPets.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catVeg.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catSafai.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));
        catSabun.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.category_foreground));

        // t.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        cAll.setTextAppearance(R.style.CategoryText1);
        cAnaj.setTextAppearance(R.style.CategoryText1);
        cBeauty.setTextAppearance(R.style.CategoryText1);
        cDal.setTextAppearance(R.style.CategoryText1);
        cMedical.setTextAppearance(R.style.CategoryText1);
        cElectronics.setTextAppearance(R.style.CategoryText1);
        cFarming.setTextAppearance(R.style.CategoryText1);
        cHardware.setTextAppearance(R.style.CategoryText1);
        cHousehold.setTextAppearance(R.style.CategoryText1);
        cMasale.setTextAppearance(R.style.CategoryText1);
        cOil.setTextAppearance(R.style.CategoryText1);
        cOthers.setTextAppearance(R.style.CategoryText1);
        cPets.setTextAppearance(R.style.CategoryText1);
        cVeg.setTextAppearance(R.style.CategoryText1);
        cSafai.setTextAppearance(R.style.CategoryText1);
        cSabun.setTextAppearance(R.style.CategoryText1);


        if (coText.getTag().equals("t" + tagV)) {
            coText.setTextAppearance(R.style.CategoryText2);
        }
        selected.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cat_fore));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void Load() {
        database.collection("ProductStore").whereArrayContains("tags", tagV).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Product p = document.toObject(Product.class);
                                p.setId(document.getId());
                                productList.add(p);
                                productAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "", task.getException());
                        }
                        progressDialog.dismiss();
                    }
                });
    }

}