package com.example.otpverificationusingfirebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.MyViewHolder>
        implements AdapterView.OnItemSelectedListener {
    Context context;
    ArrayList<Product> list;
    MyCallBack myCallback;

    public interface MyCallBack {
        void listenerMethod();
    }

    public RecyclerProductAdapter(Context context, ArrayList<Product> list, MyCallBack myCallBack) {
        this.context = context;
        this.list = list;
        this.myCallback = myCallBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
        /*{
        Product product = list.get(position);

        holder.xName.setText(product.getName());

        SharedPreferences sPrefs = context.getSharedPreferences("Item details", Context.MODE_PRIVATE);

        if (sPrefs.contains(product.getName())) {
            holder.test.setText(product.getName());

            holder.addButton.setProgress(0.70f);
        } else {
            holder.addButton.setProgress(0.03f);
        }
        holder.addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                 if (holder.addButton.getProgress() == 0.03f) {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Item Name", product.getName());
                    saveMap(product.getName(), hashMap);

                    notifyItemChanged(holder.getLayoutPosition());

                } else if (holder.addButton.getProgress() == 0.70f) {

                    SharedPreferences mPrefs = context.getSharedPreferences("Item details", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit().remove(product.getName());
                    prefsEditor.apply();

                    notifyItemChanged(holder.getLayoutPosition(),"");

                }
            }

            private void saveMap(String key, Object obj) {
                SharedPreferences prefs = context.getSharedPreferences("Item details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(obj);
                editor.putString(key, json);
                editor.apply();
            }
        });
    }*/ {
        Product product = list.get(position);

        holder.xName.setText(product.getName());
        holder.xOldPrice.setText(MessageFormat.format("₹{0}", product.getOldPrice()));
        holder.xNewPrice.setText(product.getNewPrice());
        Glide.with(holder.xImage).load(product.getImage()).into(holder.xImage);

        ArrayAdapter<String> adapterKg = new ArrayAdapter<>(context, R.layout.spinner_item_custom, product.getQuantity1());
        adapterKg.setDropDownViewResource(R.layout.spinner_dropdown_text);
        holder.xQuantityKg.setAdapter(adapterKg);
        holder.xQuantityKg.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapterGms = new ArrayAdapter<>(context, R.layout.spinner_item_custom, product.getQuantity2());
        if (product.getQuantity2().isEmpty()) {

        } else {
            adapterGms.setDropDownViewResource(R.layout.spinner_dropdown_text);
            holder.xQuantityGms.setAdapter(adapterGms);
            holder.xQuantityGms.setOnItemSelectedListener(this);
        }

        double B = Integer.parseInt(holder.xNewPrice.getText().toString().replaceAll("[^0-9]", ""));
        int A = Integer.parseInt(holder.xOldPrice.getText().toString().replaceAll("[^0-9]", ""));
        double g = ((A - B) * 100 / A);

        String numberD = String.valueOf(g);
        String numberA = numberD.substring(numberD.indexOf(".")).substring(1, 2);
        int j = Integer.parseInt(numberA);
        String C;
        if (j >= 5) {
            int l = (Integer.parseInt(numberD.substring(0, numberD.indexOf(".")))) + 1;
            C = "छूट " + l + "%";
        } else {
            C = "छूट " + numberD.substring(0, numberD.indexOf(".")) + "%";
        }
        holder.txtOff.setText(C);

        SharedPreferences sPrefs = context.getSharedPreferences("Item details", Context.MODE_PRIVATE);

        if (sPrefs.contains(product.getName())) {
           // holder.test.setText(product.getName());
            String h = getItemValue(product.getName());
            String kg = h.substring(h.indexOf("(") + 1, h.indexOf("|"));
            holder.xQuantityKg.setSelection(Integer.parseInt(kg));
            holder.xQuantityKg.setEnabled(false);
            String gm = h.substring(h.indexOf("|") + 1, h.indexOf(")"));
            holder.xQuantityGms.setSelection(Integer.parseInt(gm));
            holder.xQuantityGms.setEnabled(false);
            holder.addButton.setProgress(0.70f);

        } else {
            holder.xQuantityKg.setEnabled(true);
            holder.xQuantityGms.setEnabled(true);
            holder.addButton.setProgress(0.03f);

        }
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((holder.xQuantityKg.getSelectedItemPosition() == 0) && (product.getQuantity2().isEmpty()
                        || holder.xQuantityGms.getSelectedItemPosition() == 0)) {
                    holder.xQuantityKg.performClick();
                    Toast.makeText(context, "पहले मात्रा चुनें", Toast.LENGTH_SHORT).show();
                } else if (holder.addButton.getProgress() == 0.03f) {

                    final DecimalFormat df = new DecimalFormat(".00");
                    String itQuan;
                    String totalPr;
                    if (holder.xQuantityKg.getSelectedItemPosition() == 0) {
                        itQuan = holder.xQuantityGms.getSelectedItem().toString();
                        totalPr = (df.format(Double.parseDouble(itQuan.replaceAll("[^0.-9]", "")) / 1000F
                                * Double.parseDouble(product.getNewPrice().replaceAll("[^0.-9]", "").replace("/", ""))));

                    } else if (product.getQuantity2().isEmpty() || holder.xQuantityGms.getSelectedItemPosition() == 0) {
                        itQuan = holder.xQuantityKg.getSelectedItem().toString();
                        totalPr = (df.format(Double.parseDouble(itQuan.replaceAll("[^0.-9]", ""))
                                * Double.parseDouble(product.getNewPrice().replaceAll("[^0.-9]", "").replace("/", ""))));

                    } else {
                        int quantity3 = Integer.parseInt(holder.xQuantityGms.getSelectedItem().toString().replaceAll("[^0-9]", ""));
                        String quantity4 = df.format(quantity3 / 1000f) + " ";
                        String Quantity2 = quantity4.replace("0", "");
                        itQuan = holder.xQuantityKg.getSelectedItem().toString().replace(" ", Quantity2);
                        totalPr = (df.format(Double.parseDouble(itQuan.replaceAll("[^0.-9]", ""))
                                * Double.parseDouble(product.getNewPrice().replaceAll("[^0.-9]", "").replace("/", ""))));
                    }

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Item Name", product.getName());
                    hashMap.put("Item Price", product.getNewPrice());
                    hashMap.put("Item Quantity", itQuan);
                    hashMap.put("Item Total", totalPr);
                    hashMap.put("Item Spinner", ("(" + holder.xQuantityKg.getSelectedItemPosition() + "|" + holder.xQuantityGms.getSelectedItemPosition() + ")"));
                    saveMap(product.getName(), hashMap);

                    myCallback.listenerMethod();
                    notifyItemChanged(holder.getLayoutPosition(),"");
                } else if (holder.addButton.getProgress() == 0.70f) {
                    SharedPreferences mPrefs = context.getSharedPreferences("Item details", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    prefsEditor.remove(product.getName());
                    prefsEditor.apply();

                    myCallback.listenerMethod();
                    notifyItemChanged(holder.getLayoutPosition(),"");
                }
            }

            private void saveMap(String key, Object obj) {

                SharedPreferences prefs = context.getSharedPreferences("Item details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(obj);
                editor.putString(key, json);
                editor.apply();
            }
        });
    }

    private String getItemValue(String ProductName) {

        SharedPreferences Pre = context.getSharedPreferences("Item details", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Pre.getString(ProductName, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> obj = gson.fromJson(json, type);
        return String.valueOf(obj.get("Item Spinner"));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View vie, int pos, long id) {

        if (parent.getSelectedItemPosition() == 0) {
            String txtKg = parent.getItemAtPosition(pos).toString();
            parent.setBackgroundResource(R.drawable.cream_bg);
            parent.getSelectedView().setBackgroundResource(R.drawable.cream_bg);
        } else if (parent.getSelectedItemPosition() != 0) {
            parent.setBackgroundResource(R.drawable.yellow_bg);
            parent.getSelectedView().setBackgroundResource(R.drawable.yellow_bg);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LottieAnimationView addButton;
        TextView xName, test;
        TextView xOldPrice, xNewPrice, txtOff;
        ImageView xImage;
        Spinner xQuantityKg, xQuantityGms;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            xName = itemView.findViewById(R.id.product_name);
            addButton = itemView.findViewById(R.id.btnAddItem);
          //  test = itemView.findViewById(R.id.testUid);
            xQuantityKg = itemView.findViewById(R.id.quantity_spinner_kg);
            xQuantityGms = itemView.findViewById(R.id.quantity_spinner_gms);
            xImage = itemView.findViewById(R.id.item_Product_Image);
            xNewPrice = itemView.findViewById(R.id.new_price);
            xOldPrice = itemView.findViewById(R.id.old_price);
            txtOff = itemView.findViewById(R.id.idOff);
        }
    }

}