package com.infinitesoftsolution.shoppinglist.shopify;

import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.infinitesoftsolution.shoppinglist.shopify.MainActivity.Arry;

public class AddActivity extends AppCompatActivity {

    static ArrayList<GetSet> dataModels=new ArrayList<>();

    EditText edtQty,edtval;

    TextView txttotal;
    ImageView imgBack;

    Button btnAdd,btnClear;

    ImageView imgAdd,imgSbb;

    String name,qty,val;

    Context ctx=this;

    String totalStr="0";
    AutoCompleteTextView edtName;
    Spinner spnContet;


    Double valAdd=0.0;


    String[] fruits = {"Apples", "Avocados","Bananas","Berries", "Broccoli", "Carrots", "Celery", "Cucumbers", "Garlic", "Grapefruit",
            "Grapes","Lemons/Limes", "Lettuce", "Melons", "Mushrooms", "Onions","Oranges", "Peppers", "Potatoes", "Squash/Zucchini", "Bagels", "Bread", "Cake", "Cookies", "Dinner Rolls", "Donuts", "French Bread", "Hamburger Buns", "Hot Dog Buns", "Muffins", "Pastries", "Pie", "Pita Bread", "Cold Cereal", "Oatmeal",
            "Creamed Wheat", "Pancake Mix","Chicken", "Desserts", "Dinners", "Fish", "Fruits", "Ice","Ice Cream", "Ice Pops", "Juice", "Lasagna", "Pie", "Pizza","Vegetables",
            "Waffles", "Tortillas", "Water", "Juice", "Soda", "Sports Drinks", "Coffee", "Tea", "Biscuits", "Butter", "Cheese", "Cookie Dough", "Cream Cheese", "Dips", "Eggs", "Milk", "Sour Cream", "Whip Cream", "Yogurt",
            "Tomatoes","Rice"};
    Integer newMeasuure=0;

    DecimalFormat df=new DecimalFormat("0.00");

    private final String TAG = "NATIVE";

    private NativeAd nativeAd;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        try{

            //edtname=findViewById(R.id.name);
            edtQty=findViewById(R.id.qty);
            edtval =findViewById(R.id.val);
            txttotal=findViewById(R.id.total);
            spnContet=findViewById(R.id.spnContent);

            imgAdd=findViewById(R.id.imgadd);
            imgSbb=findViewById(R.id.imgsb);

            btnAdd=findViewById(R.id.btnadd);
            btnClear=findViewById(R.id.btnclear);
            imgBack=findViewById(R.id.imgback);

            // Spinner Drop down elements



            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.layout_spinner_item, MainActivity.categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spnContet.setAdapter(dataAdapter);



            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.select_dialog_item, fruits);
            //Getting the instance of AutoCompleteTextView
             edtName = (AutoCompleteTextView) findViewById(R.id.name);
            edtName.setThreshold(1);//will start working from first character
            edtName.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
            edtName.setTextColor(Color.BLACK);

            //ads native

            nativeAd = new NativeAd(this, "299478034602375_327900888426756");

            nativeAd.setAdListener(new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    // Native ad finished downloading all assets
                    Log.e(TAG, "Native ad finished downloading all assets.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Native ad failed to load
                    Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Native ad is loaded and ready to be displayed
                    Log.d(TAG, "Native ad is loaded and ready to be displayed!");

                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    // Inflate Native Ad into Container
                    inflateAd(nativeAd);
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Native ad clicked
                    Log.d(TAG, "Native ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Native ad impression
                    Log.d(TAG, "Native ad impression logged!");
                }
            });

            // Request an ad



            nativeAd.loadAd();



            if(Constats.checkStats.equals("change")){

                try {
                    Double qty,val;
                    Integer pos = Integer.valueOf(Constats.selectedIndex);
                    edtName.setText(Arry.get(pos).get_name());
                    edtQty.setText(Arry.get(pos).get_Qty());
                    edtval.setText(Arry.get(pos).get_val());
                    spnContet.setSelection(Arry.get(pos).get_measure());

                    qty = Double.valueOf(edtQty.getText().toString());
                    val = Double.valueOf(Arry.get(pos).get_val());

                    Double total = val* qty;
                    txttotal.setText(" Rs :"+df.format(total));


                    /*qty = Integer.valueOf(edtQty.getText().toString());
                    val = Integer.valueOf(Arry.get(pos).get_val());

                    Integer total = val / qty;
                    edtval.setText(total.toString());


                    totalStr=val.toString();
                    txttotal.setText(" Rs :"+val);*/

                }catch (Exception e){
                    e.getMessage();
                }


            }


            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        btnAdd.setEnabled(false);
                        newMeasuure=spnContet.getSelectedItemPosition();

                        name = edtName.getText().toString().trim();
                        qty = edtQty.getText().toString().trim();
                        val = edtval.getText().toString().trim();
                        boolean contains=false;
                        for(int i=0;i<Arry.size();i++){
                            if(Arry.get(i).get_name().trim().equalsIgnoreCase(name)){
                                contains=true;
                                Constats.selectedIndex=String.valueOf(i);
                            }
                        }

                        if (name.trim().equals("")) {
                            btnAdd.setEnabled(true);
                            Toast.makeText(AddActivity.this, "Product Name cannot be left empty", Toast.LENGTH_LONG).show();
                        }
                        else if(Constats.checkStats.trim().equals("change") || contains){

                            if(qty.trim().equals("")){
                                qty="0.0";
                            }
                            if(val.trim().equals("")){
                                val="0.0";
                            }
                           // val=String.valueOf(Integer.parseInt(qty)*Integer.parseInt(val));
                            updateProd();
                            btnAdd.setEnabled(true);

                        } else{
                            if(qty.trim().equals("")){
                                qty="0.0";
                            }
                            if(val.trim().equals("")){
                                val="0.0";
                            }
                            //val=String.valueOf(Integer.parseInt(qty)*Integer.parseInt(val));
                            addProdct();
                            btnAdd.setEnabled(true);
                        }
                    }catch (Exception e){
                        e.getMessage();
                        btnAdd.setEnabled(true);
                    }

                }
            });

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edtName.setText("");
                    edtQty.setText("0.0");
                    edtval.setText("0.0");
                }
            });

            edtval.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {

                    try {
                        Double qty,val;
                        if(edtQty.getText().toString().trim().equals("")){
                             qty=0.0;
                        }else {
                             qty = Double.valueOf(edtQty.getText().toString());
                        }

                        if(edtval.getText().toString().trim().equals("")){
                             val=0.0;
                        }else {
                             val = Double.valueOf(edtval.getText().toString());
                        }




                        Double total = qty * val;

                        String abc=total.toString();
                        txttotal.setText(" Rs :"+df.format(total));
                    }catch (Exception e){
                        e.getMessage();
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {


                }
            });


            edtQty.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {

                    try {
                        Double qty,val;
                        if(edtQty.getText().toString().trim().equals("")){
                            qty=0.0;
                        }else {
                            qty = Double.valueOf(edtQty.getText().toString());
                        }

                        if(edtval.getText().toString().trim().equals("")){
                            val=0.0;
                        }else {
                            val = Double.valueOf(edtval.getText().toString());
                        }




                        Double total = qty * val;

                        String abc=total.toString();
                        totalStr=abc;
                        txttotal.setText(" Rs :"+df.format(total));
                    }catch (Exception e){
                        e.getMessage();
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {


                }
            });


            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{



                        if(edtQty.getText().toString().trim().equals("")){

                           edtQty.setText("1.0");
                        }else
                        {
                            valAdd=Double.parseDouble(edtQty.getText().toString());
                            valAdd++;
                            edtQty.setText(valAdd.toString());
                        }

                        edtQty.clearFocus();

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edtQty.getWindowToken(), 0);

                    }catch (Exception e){
                        e.getMessage();
                    }
                }
            });

            imgSbb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{



                        if(edtQty.getText().toString().trim().equals("") ){

                            edtQty.setText("0.0");
                        }else
                        {
                            valAdd=Double.parseDouble(edtQty.getText().toString());
                            if(valAdd<=0){
                                edtQty.setText("0.0");
                            }else {
                                valAdd--;
                                edtQty.setText(valAdd.toString());
                            }


                            edtQty.clearFocus();

                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edtQty.getWindowToken(), 0);
                        }

                    }catch (Exception e){
                        e.getMessage();
                    }
                }
            });


            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });





        }catch (Exception e){
            e.getMessage();
        }
    }

    private void updateProd() {



        DatabaseOperations DB = new DatabaseOperations(ctx);
        Integer pos=Integer.parseInt(Constats.selectedIndex);
        String old_name=Arry.get(pos).get_name();
        String old_qty=Arry.get(pos).get_Qty();
        String old_val=Arry.get(pos).get_val();
        String old_measure=String.valueOf(Arry.get(pos).get_measure());


        DB.updateUserInfo(DB,old_name,old_qty,old_val,old_measure,name,qty,val,String.valueOf(newMeasuure));
        onBackPressed();
        Toast.makeText(AddActivity.this, "Product Details Updated.", Toast.LENGTH_LONG).show();
        Constats.checkStats="";

    }

    private void addProdct() {

        dataModels.add(new GetSet(name, qty, val,"z",newMeasuure));

        DatabaseOperations DB = new DatabaseOperations(ctx);

        DB.putInfo(DB, name, qty, val,newMeasuure);
        onBackPressed();
        Toast.makeText(AddActivity.this, "Product Added to List.", Toast.LENGTH_LONG).show();
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constats.checkStats="";

    }

    private void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(AddActivity.this);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(AddActivity.this, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }


    @Override
    protected void onDestroy() {

        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }
}
