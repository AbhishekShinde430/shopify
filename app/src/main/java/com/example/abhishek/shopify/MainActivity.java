package com.example.abhishek.shopify;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView listview;
    CustAdaFinStat custAdaFinStat;
    LinearLayout layout,searchLayot,emptyLay,mainLay;
    TextView txtTotal,txtCartTotal;
    EditText editsearch;
    ProgressBar pb;


    Context ctx=this;
    public  static List<GetSet> Arry=new ArrayList<>();
    Double total,totalCart=0.0;


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String shareData="";
    public static List<String> categories;
    DecimalFormat df=new DecimalFormat("0.00");

    public AdView adView;
    public static InterstitialAd interstitialAd ;
    public static String TAG="STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prodcts);


        try {

            listview = findViewById(R.id.list);
            pb = findViewById(R.id.pb);
            layout = findViewById(R.id.totallay);
            searchLayot = findViewById(R.id.serchlayer);
            emptyLay = findViewById(R.id.emptylay);
            mainLay = findViewById(R.id.mainLay);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            editsearch = (EditText) findViewById(R.id.search);
            txtTotal = findViewById(R.id.txttotal);
            txtCartTotal = findViewById(R.id.txtCartTotal);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

            pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            editor = pref.edit();

            Constats.appPackage = getApplicationContext().getPackageName();
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            Constats.versionCode = String.valueOf(packageInfo.versionName);
            Constats.appName=getString(R.string.app_name);

            categories= new ArrayList<String>();
            categories.add("each");
            categories.add("g");
            categories.add("kg");
            categories.add("l");
            categories.add("lbs");
            categories.add("piece");
            categories.add("bag");
            categories.add("bottle");
            categories.add("dozen");
            categories.add("oz");
            categories.add("cup");
            categories.add("gallon");
            categories.add("tbsp");
            categories.add("tsp");
            categories.add("jar");
            categories.add("can");

            //banner ads
            adView = new AdView(this, "IMG_16_9_APP_INSTALL# 299478034602375_299478524602326", AdSize.BANNER_HEIGHT_50);
            //adView = new AdView(this, " 299478034602375_299478524602326", AdSize.BANNER_HEIGHT_50);

            // Find the Ad Container
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

            // Add the ad view to your activity layout
            adContainer.addView(adView);

            // Request an ad


            adView.loadAd();


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();




            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


            editsearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    try {
                        String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                        custAdaFinStat.filter(text);
                    }catch (Exception e){
                        e.getMessage();

                    }
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                    // TODO Auto-generated method stub
                }
            });

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Constats.checkStats = "change";
                    Constats.selectedIndex = String.valueOf(position);
                    Intent it = new Intent(MainActivity.this, AddActivity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);

                }
            });


            final FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fab.setEnabled(false);
                    Intent it = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(it);

                    fab.setEnabled(true);
                }
            });



            registerForContextMenu(listview);

            requestStoragePermission();


        }catch (Exception e){
            e.getMessage();
        }







    }



    public void GetProdctList() {

        try {
            Arry.clear();
            DatabaseOperations dp = new DatabaseOperations(ctx);

            Cursor cr = dp.getInfo(dp);

            cr.moveToFirst();

            Integer cont=cr.getCount();
            do {
                if(cont!=0){
                    Arry.add(new GetSet(cr.getString(0), cr.getString(1), cr.getString(2),cr.getString(3),
                            cr.getInt(4)));
                }else {
                    Toast.makeText(MainActivity.this,"Product List is Empty",Toast.LENGTH_LONG).show();
                }



            } while (cr.moveToNext());

            if(Arry.size()<=0){

                /*prodct list is empty*/
                layout.setVisibility(View.GONE);
                searchLayot.setVisibility(View.GONE);
                mainLay.setVisibility(View.GONE);
                emptyLay.setVisibility(View.VISIBLE);



            }else{
                custAdaFinStat = new CustAdaFinStat(MainActivity.this, Arry);
                listview.setAdapter(custAdaFinStat);
                total=0.0;
                totalCart=0.0;

                for (int i=0;i<Arry.size();i++){
                    Double val=Double.valueOf(Arry.get(i).get_val());
                    Double qty=Double.valueOf(Arry.get(i).get_Qty());
                    Double sum=val*qty;
                    total=total+sum;
                }

                for (int i=0;i<Arry.size();i++){

                    if(Arry.get(i).get_checkedVal().trim().equals("a")){
                        Double val=Double.valueOf(Arry.get(i).get_val());
                        Double qty=Double.valueOf(Arry.get(i).get_Qty());
                        Double sum=val*qty;
                        totalCart=totalCart+sum;
                    }

                }
                layout.setVisibility(View.VISIBLE);
                searchLayot.setVisibility(View.VISIBLE);
                mainLay.setVisibility(View.VISIBLE);
                emptyLay.setVisibility(View.GONE);



                txtTotal.setText(String.format("Rs : " + df.format(total)));
                txtCartTotal.setText("Rs : "+df.format(totalCart));
            }

        }catch (Exception e){
            e.getMessage();
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.list_menu,menu);
//        switch (v.getId())
//        {
//
//        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.opt1:

                AdapterView.AdapterContextMenuInfo menuInfos = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Integer posi=menuInfos.position;

                DatabaseOperations dp = new DatabaseOperations(ctx);
                String name=Arry.get(posi).get_name();
                String qty=Arry.get(posi).get_Qty();
                String val=Arry.get(posi).get_val();
                String measr=String.valueOf(Arry.get(posi).get_measure());
                dp.deleteProd(dp,name,qty,val,measr);
                GetProdctList();


                return true;

            case R.id.opt2:
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Integer pos=menuInfo.position;
                shareData="--------Shopify Product Details--------\n\n"+

                        " Product Name : "+Arry.get(pos).get_name()+
                        "\n Product Qty : "+Arry.get(pos).get_Qty()+
                        "\n Product Price : "+Arry.get(pos).get_val();
                shareText(shareData);
                return true;


            case  R.id.opt3:
                try{

                    AdapterView.AdapterContextMenuInfo menuInfod = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    Integer posd=menuInfod.position;
                    Constats.checkStats = "change";
                    Constats.selectedIndex = String.valueOf(posd);
                    Intent it = new Intent(MainActivity.this, AddActivity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                }catch (Exception e){
                    e.getMessage();
                }




            default:
                return super.onContextItemSelected(item);

        }

    }

    private void shareText(String sharetxt) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Share Text");
        share.putExtra(Intent.EXTRA_TEXT, sharetxt);

        startActivity(Intent.createChooser(share, "Share text!"));
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            CustomDialogClass cdd=new CustomDialogClass(MainActivity.this);
            cdd.show();
            // Handle the camera action
        }
        else if (id == R.id.feed) {

            try {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"infinitesoftsolution108@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback : ");

                startActivity(Intent.createChooser(emailIntent, "Send Feedback:"));
            }catch (Exception e){
                e.getMessage();
            }

        }
        else if (id == R.id.nav_share) {

            String playStoreUrl = "https://play.google.com/store/apps/details?id="+ Constats.appPackage;
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, Constats.appName);
                String shareMessage= "\nPlease download the latest version of "+Constats.appName+" from google play store \n\n";
                shareMessage = shareMessage +playStoreUrl;
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
            catch(Exception e)
            {
                //e.toString();
            }

        } else if (id == R.id.nav_send) {

            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {

                if (item.getItemId() == R.id.share)
                {
                    try {

                        if(Arry.size()<=0){
                            Toast.makeText(MainActivity.this,"Product list is Empty",Toast.LENGTH_LONG).show();

                        }else {
                            String AllDataList="";
                            AllDataList+="--------Shopify Product Details--------\n";
                            for(int i=0;i<Arry.size();i++){
                                AllDataList+= "\n\n Product Name : "+Arry.get(i).get_name()+
                                        "\n Product Qty : "+Arry.get(i).get_Qty()+
                                        "\n Product Price : "+Arry.get(i).get_val();
                            }
                            shareText(AllDataList);


                        }






                    }catch (Exception e){
                        e.getMessage();
                    }

                }



                else if (item.getItemId() == R.id.clear)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    // Setting Dialog Title
                    alertDialog.setTitle("Alert");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Are you sure you want to delete all details from product List ?");
                    alertDialog.setButton("NO", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setButton2("YES", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try{
                                DatabaseOperations dp=new DatabaseOperations(ctx);
                                dp.delete();
                                GetProdctList();
                            }catch (Exception e){
                                e.getMessage();
                            }
                        }
                    });
                    alertDialog.show();




                }


                else if (item.getItemId() == R.id.addList)
                {

                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.custom_alert);
                    dialog.setTitle("Title...");

                    // set the custom dialog components - text, image and button
                    final EditText editText=(EditText) dialog.findViewById(R.id.listName);

                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
                    Button dialogButton2 = (Button) dialog.findViewById(R.id.btn_no);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(editText.getText().toString().equals("")){
                                Toast.makeText(MainActivity.this,"List Name cannot be Empty",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }else {
                                TableData.TABLE_NAME=editText.getText().toString();

                                DatabaseOperations databaseOperations=new DatabaseOperations(MainActivity.this);
                                databaseOperations.createTable(databaseOperations);
                                GetProdctList();
                                dialog.dismiss();
                            }

                        }
                    });

                    dialogButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();



                }

                else if (item.getItemId() == R.id.pdf)
                {

                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.custom_alert);
                    dialog.setTitle("Title...");

                    // set the custom dialog components - text, image and button
                    final EditText editText=(EditText) dialog.findViewById(R.id.listName);
                    final TextView txtDesp=(TextView) dialog.findViewById(R.id.txt_dia);

                    txtDesp.setText("Please enter your name");
                    editText.setHint("Enter Name");

                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
                    Button dialogButton2 = (Button) dialog.findViewById(R.id.btn_no);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pb.setVisibility(View.VISIBLE);

                            Constats.LD_UID=editText.getText().toString();

                            if(!Constats.LD_UID.trim().equals("")){
                                createPDF(MainActivity.this,"shopify");
                                dialog.dismiss();

                            }else {
                                Toast.makeText(MainActivity.this,"Please enter Valid Name",Toast.LENGTH_LONG).show();
                            }


                        }
                    });

                    dialogButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();



                }

                else if (item.getItemId() == R.id.logout)
                {


                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    // Setting Dialog Title
                    alertDialog.setTitle("Alert");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Are you sure you want to logout ?");
                    alertDialog.setButton("NO", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setButton2("YES", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            pref.edit().clear().commit();
                            Toast.makeText(MainActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this,LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    });
                    alertDialog.show();
                }





                return false;
            }


        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        onPrepareOptionsMenu2(popup.getMenu());
        popup.show();
    }

    public boolean onPrepareOptionsMenu2(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.loginName);
        MenuItem itemshare = menu.findItem(R.id.share);
        MenuItem itemclear = menu.findItem(R.id.clear);
        MenuItem itempdf = menu.findItem(R.id.pdf);
        item.setTitle("Welcome "+Constats.LD_UID);

        if(Arry.size()<=0){
            itemshare.setVisible(false);
            itemclear.setVisible(false);
            itempdf.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    public boolean createPDF(Context context, String reportName) {

        try {
            //creating a directory in SD card
            PdfWriter pdfWriter;
            File mydir = new File(Environment.getExternalStorageDirectory()
                    + Constats.PATH_PRODUCT_REPORT); //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            //getting the full path of the PDF report name
            String mPath = Environment.getExternalStorageDirectory().toString()
                    + Constats.PATH_PRODUCT_REPORT //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
                    + reportName+".pdf"; //reportName could be any name

            //constructing the PDF file
            File pdfFile = new File(mPath);

            //Creating a Document with size A4. Document class is available at  com.itextpdf.text.Document
            Document document = new Document(PageSize.A4);

            //assigning a PdfWriter instance to pdfWriter
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

            //PageFooter is an inner class of this class which is responsible to create Header and Footer
            PageHeaderFooter event = new PageHeaderFooter();
            pdfWriter.setPageEvent(event);

            //Before writing anything to a document it should be opened first
            document.open();

            //Adding meta-data to the document
            addMetaData(document);
            //Adding Title(s) of the document
            addTitlePage(document);
            //Adding main contents of the document
            addContent(document);
            //Closing the document
            document.close();





        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        return true;
    }

    private static void addMetaData(Document document) {
        document.addTitle("All Product Names");
        document.addSubject("none");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Infinite Soft Solutions");
        document.addCreator("Abhishek Shinde");
    }

    /**
     * In this method title(s) of the document is added.
     * @param document
     * @throws DocumentException
     */
    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph paragraph = new Paragraph();

        // Adding several title of the document. Paragraph class is available in  com.itextpdf.text.Paragraph

        Paragraph childParagraph = new Paragraph("Shopify", Constats.FONT_TITLE); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        childParagraph = new Paragraph("Product List", Constats.FONT_SUBTITLE); //
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        childParagraph = new Paragraph(Constats.LD_UID, Constats.FONT_SUBTITLE); //
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        childParagraph = new Paragraph("Report generated on: "+formattedDate , Constats.FONT_SUBTITLE);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        addEmptyLine(paragraph, 2);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        //End of adding several titles

    }



    private  void addContent(Document document) throws DocumentException {

        Paragraph reportBody = new Paragraph();
        reportBody.setFont(Constats.FONT_BODY); //

        // Creating a table
        createTable(reportBody);

        // now add all this to the document
        document.add(reportBody);



    }


    private  void createTable(Paragraph reportBody)
            throws BadElementException {

        float[] columnWidths = {5,5,5,2}; //total 4 columns and their width. The first three columns will take the same width and the fourth one will be 5/2.
        PdfPTable table = new PdfPTable(columnWidths);

        table.setWidthPercentage(100); //set table with 100% (full page)
        table.getDefaultCell().setUseAscender(true);


        //Adding table headers
        PdfPCell cell = new PdfPCell(new Phrase("Product Name", //Table Header
                Constats.FONT_TABLE_HEADER)); //
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
        cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
        cell.setFixedHeight(30); //cell height
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Quantity",
                Constats.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Units",
                Constats.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Price",
                Constats.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        //End of adding table headers

        //This method will generate some static data for the table
       // generateTableData();

        //Adding data into table
        for (int i = 0; i < Arry.size(); i++) { //
            cell = new PdfPCell(new Phrase(Arry.get(i).get_name()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(28);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(Arry.get(i).get_Qty()));
            cell.setFixedHeight(28);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            String measure=MainActivity.categories.get(Arry.get(i).get_measure());
            cell = new PdfPCell(new Phrase(measure));
            cell.setFixedHeight(28);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(Arry.get(i).get_val()));
            cell.setFixedHeight(28);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        cell = new PdfPCell(new Phrase("Total",
                Constats.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("",
                Constats.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("",
                Constats.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        Double total=0.00;

        for (int i=0;i<Arry.size();i++){
            Double val=Double.valueOf(Arry.get(i).get_val());
            Double qty=Double.valueOf(Arry.get(i).get_Qty());
            Double sum=val*qty;
            total=total+sum;
        }

        cell = new PdfPCell(new Phrase(total.toString(),
                Constats.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        reportBody.add(table);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                viewPdf(MainActivity.this,"shopify.pdf",Constats.PATH_PRODUCT_REPORT);
            }
        }, 3000);






    }

    /**
     * This method is used to add empty lines in the document
     * @param paragraph
     * @param number
     */
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /**
     * This is an inner class which is used to create header and footer
     * @author XYZ
     *
     */

    class PageHeaderFooter extends PdfPageEventHelper {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);

        public void onEndPage(PdfWriter writer, Document document) {

            /**
             * PdfContentByte is an object containing the user positioned text and graphic contents
             * of a page. It knows how to apply the proper font encoding.
             */
            PdfContentByte cb = writer.getDirectContent();

            /**
             * In iText a Phrase is a series of Chunks.
             * A chunk is the smallest significant part of text that can be added to a document.
             *  Most elements can be divided in one or more Chunks. A chunk is a String with a certain Font
             */
            Phrase footer_poweredBy = new Phrase("Powered By Infinite Soft Solution", Constats.FONT_HEADER_FOOTER); //
            Phrase footer_pageNumber = new Phrase("Page " + document.getPageNumber(), Constats.FONT_HEADER_FOOTER);

            // Header
            // ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, header,
            // (document.getPageSize().getWidth()-10),
            // document.top() + 10, 0);

            // footer: show page number in the bottom right corner of each age
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    footer_pageNumber,
                    (document.getPageSize().getWidth() - 10),
                    document.bottom() - 10, 0);
//			// footer: show page number in the bottom right corner of each age
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer_poweredBy, (document.right() - document.left()) / 2
                            + document.leftMargin(), document.bottom() - 10, 0);
        }
    }

    /**
     * Generate static data for table
     */


    // Method for opening a pdf file
    private void viewPdf(Context context,String file, String directory) {
        pb.setVisibility(View.INVISIBLE);

        File pdfFile = new File(Environment.getExternalStorageDirectory() + directory + file);
        Uri path;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",pdfFile);
        } else {
            path = Uri.fromFile(pdfFile);
        }



        //Uri path = Uri.fromFile(pdfFile);


        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "Please install PDF Viewer App", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetProdctList();
    }

}
