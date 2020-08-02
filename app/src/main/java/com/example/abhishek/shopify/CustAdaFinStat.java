package com.example.abhishek.shopify;

/**
 * Created by Manish on 07/07/2016.
 */

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.abhishek.shopify.MainActivity.Arry;

public class CustAdaFinStat extends BaseAdapter
{

    Activity context;
    private ArrayList<GetSet> arraylist;
    List<GetSet> LedgerList;
    private CustAdaFinStat adapter;
  //  private int selectedIndex;
   // private int selectedColor = Color.parseColor("#525050");
    //private final int[] colors = new int[] { 0xAA000000, 0xAA5F5F5F };
    DecimalFormat df = new DecimalFormat("0.00");
    String testListner = "yes";
    Animation scaleUp;
    View itemView;
    LayoutInflater inflater;
    Integer positionSel;


    public CustAdaFinStat(Activity context, List<GetSet> LedgerList)
    {
        this.context = context;
        this.LedgerList = LedgerList;
        //selectedIndex = -1;
        this.arraylist = new ArrayList<GetSet>();
        this.arraylist.addAll(LedgerList);
        this.adapter = this;


       // scaleUp = AnimationUtils.loadAnimation(context, R.anim.translate);
    }



    public void setSelectedIndex(int ind)
    {
       // selectedIndex = ind;
        notifyDataSetChanged();
        testListner = "no";
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return LedgerList.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        LedgerList.clear();
        if (charText.length() == 0) {
            LedgerList.addAll(arraylist);
        }
        else
        {
            for (GetSet wp : arraylist)
            {
                if ((wp.get_name().toLowerCase(Locale.getDefault()).contains(charText))
                        ||(wp.get_val().toLowerCase(Locale.getDefault()).contains(charText)) )
                {
                    LedgerList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }





    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return LedgerList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return LedgerList.indexOf(getItem(position));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub


        try {
            TextView txtDate;
            TextView txtqty;
            TextView txtval;
            TextView txttotval;
            final CheckBox checkBox;


            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.ld_finstst_rows, parent, false);


                txtDate = (TextView) itemView.findViewById(R.id.lblDate);
                txtqty = (TextView) itemView.findViewById(R.id.lblqty);
                txtval = (TextView) itemView.findViewById(R.id.lblval);
                txttotval = (TextView) itemView.findViewById(R.id.lbltotalPrice);
               checkBox = (CheckBox) itemView.findViewById(R.id.ck);


            final GetSet ldLedgerList = (GetSet) getItem(position);
            int part=position+1;




           txtDate.setText(part+". "+ldLedgerList.get_name());
           txtqty.setText(ldLedgerList.get_Qty());

           String measure=MainActivity.categories.get(ldLedgerList.get_measure());
           txtval.setText(ldLedgerList.get_val()+ "/( "+ measure+" )");

           Double total=Double.parseDouble(ldLedgerList.get_Qty())*Double.parseDouble(ldLedgerList.get_val());
           txttotval.setText("Total Price :"+df.format(total));


            if(ldLedgerList.get_checkedVal().trim().equals("a")){
               checkBox.setChecked(true);


            }else{
                checkBox.setChecked(false);

            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    positionSel = position;
                    GetSet ldLedgerList = (GetSet) getItem(positionSel);
                    String name=ldLedgerList.get_name();
                    String state=ldLedgerList.get_checkedVal();
                    String newState="z";
                    if(isChecked){
                        newState="a";

                    }else{
                        newState="z";
                    }
                    updateState(name,state,newState);

                }
            });





        }catch (Exception e){
            e.getMessage();
        }


        return itemView;
    }


    private void updateState(String name,String State,String newState) {


    try {


    DatabaseOperations DB = new DatabaseOperations(context);
    DB.updateState(DB, name, State, newState);

    pdateArray();
   // Toast.makeText(context, "Product Details Updated.", Toast.LENGTH_LONG).show();



}catch (Exception e){
    e.getMessage();
}


    }

    private void pdateArray() {

        try {

            Arry.clear();
            DatabaseOperations dp = new DatabaseOperations(context);

            Cursor cr = dp.getInfo(dp);

            cr.moveToFirst();

            Integer cont = cr.getCount();

            do {
                if (cont != 0) {
                    Arry.add(new GetSet(cr.getString(0), cr.getString(1), cr.getString(2), cr.getString(3),cr.getInt(4)));
                }
            } while (cr.moveToNext());
            Double totalCart=0.0;

            for (int i=0;i<Arry.size();i++){

                if(Arry.get(i).get_checkedVal().trim().equals("a")){
                    Double val=Double.valueOf(Arry.get(i).get_val());
                    Double qty=Double.valueOf(Arry.get(i).get_Qty());
                    Double sum=val*qty;
                    totalCart=totalCart+sum;
                }

            }

            TextView txt=(TextView) context.findViewById(R.id.txtCartTotal);
            txt.setText("Rs. : "+df.format(totalCart));


            adapter.notifyDataSetChanged();


        }catch (Exception e){
            e.getMessage();
        }
    }
}