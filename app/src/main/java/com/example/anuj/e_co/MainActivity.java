package com.example.anuj.e_co;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anuj.e_co.BottomSheet.BottomSheetItemObject;
import com.example.anuj.e_co.BottomSheet.BottomSheetRecyclerViewAdapter;
import com.example.anuj.e_co.Coupons.CouponsCardFragmentPagerAdapter;
import com.example.anuj.e_co.Coupons.CouponsCardItem;
import com.example.anuj.e_co.Coupons.CouponsCardPagerAdapter;
import com.example.anuj.e_co.Coupons.CouponsShadowTransformer;
import com.example.anuj.e_co.DatabaseTransaction.PersonDatabaseHelper;
import com.example.anuj.e_co.EcoService.BatteryService;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomSheetBehavior mBottomSheetBehavior;
    TextView swipe,txtname,txtseeds;
    ImageView swipebut;
    CardView maincard,quotecard,homecard,transcard;
    private GridLayoutManager lLayout;
    private Button buybut;
    ImageView qrcsn,pay;
    private ViewPager mViewPager;
    private IntentIntegrator qrScan;

    public Toolbar toolbar;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private PersonDatabaseHelper databaseHelper;

    private CouponsCardPagerAdapter mCardAdapter;
    private CouponsShadowTransformer mCardShadowTransformer;
    private CouponsCardFragmentPagerAdapter mFragmentCardAdapter;
    private CouponsShadowTransformer mFragmentCardShadowTransformer;
    private static final int REQUEST_SELECT_PLACE = 1000;


    public String named="";
    public int seeds = 0,amount=0;

    public String currentDateTime;
    public  TextView homestat;



    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CALL_PHONE},1);
        }


        Toast.makeText(getApplicationContext(),named,Toast.LENGTH_SHORT).show();

        //Database
        databaseHelper = new PersonDatabaseHelper(this);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime = sdf1.format(new Date());


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {
            //connection is avlilable
            //Toast.makeText(getApplicationContext(),"Available",Toast.LENGTH_SHORT).show();
        }else{

            Intent i = new Intent(MainActivity.this,ConnLost_act.class);
            startActivity(i);
            finish();
            //no connection

        }

        homestat = (TextView) findViewById(R.id.txt_original_date);
        swipe =(TextView)findViewById(R.id.swipe);
        maincard=(CardView)findViewById(R.id.maincard);
        quotecard=(CardView)findViewById(R.id.quotecard);
        homecard=(CardView)findViewById(R.id.homecard);
        transcard=(CardView)findViewById(R.id.transcard);

        buybut=(Button)findViewById(R.id.buybut) ;

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Project GI");
        setSupportActionBar(toolbar);


        txtname =(TextView)findViewById(R.id.txt_name);
        txtseeds =(TextView)findViewById(R.id.txt_seeds);


        load();


        List<BottomSheetItemObject> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(MainActivity.this, 2);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        BottomSheetRecyclerViewAdapter rcAdapter = new BottomSheetRecyclerViewAdapter(MainActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);

        View bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(210);



        //options for coupons

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mCardAdapter = new CouponsCardPagerAdapter();
        mCardAdapter.addCardItem(new CouponsCardItem(R.string.title_1, R.string.text_1));
        mCardAdapter.addCardItem(new CouponsCardItem(R.string.title_2, R.string.text_1));
        mCardAdapter.addCardItem(new CouponsCardItem(R.string.title_3, R.string.text_1));
        mCardAdapter.addCardItem(new CouponsCardItem(R.string.title_4, R.string.text_1));
        mCardAdapter.addCardItem(new CouponsCardItem(R.string.title_5, R.string.text_1));
        mCardAdapter.addCardItem(new CouponsCardItem(R.string.title_6, R.string.text_1));
        mFragmentCardAdapter = new CouponsCardFragmentPagerAdapter(getSupportFragmentManager(),
                dpToPixels(2, this));

        mCardShadowTransformer = new CouponsShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new CouponsShadowTransformer(mViewPager, mFragmentCardAdapter);


        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);


        mViewPager.setCurrentItem(1);
        mCardShadowTransformer.enableScaling(true);
        mFragmentCardShadowTransformer.enableScaling(true);


        //Main act cards onClick

        maincard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //databaseHelper.insertData("asd","tht");
                startActivity(new Intent(MainActivity.this,Details.class));

            }
        });
        quotecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //databaseHelper.DatabaseDrop();
                startActivity(new Intent(MainActivity.this,Tips.class));
            }
        });
        homecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HomeAccount.class));
            }
        });
        transcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TransAct.class));
            }
        });


        //services

        //battery
        startService(new Intent(getBaseContext(), BatteryService.class));
        startService(new Intent(getBaseContext(),ServiceIoT.class));
        startService(new Intent(getBaseContext(),ServiceChat.class));

        pay = (ImageView) findViewById(R.id.payment);
        qrScan = new IntentIntegrator(this);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });


        qrcsn = (ImageView) findViewById(R.id.qr);
        qrScan = new IntentIntegrator(this);
        qrcsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });



    }

    private List<BottomSheetItemObject> getAllItemList(){

        List<BottomSheetItemObject> allItems = new ArrayList<BottomSheetItemObject>();

        allItems.add(new BottomSheetItemObject("Carticipate", R.drawable.ic_car_act_imgvctr));
        allItems.add(new BottomSheetItemObject("Recyclers", R.drawable.ic_sync_black_24dp));
        allItems.add(new BottomSheetItemObject("Home", R.drawable.ic_home_black_24dp));
        allItems.add(new BottomSheetItemObject("Vehicle", R.drawable.ic_airline_seat_recline_normal_black_24dp));

        return allItems;
    }

    public void func(View view)
    {
        if(seeds-100>=0) {
            seeds=seeds-100;
            Toast.makeText(MainActivity.this, "You bought card", Toast.LENGTH_SHORT).show();
            databaseHelper.insertData("Bought a card", currentDateTime);
        }
        else
            Toast.makeText(MainActivity.this, "You don't have enough credits for this transaction", Toast.LENGTH_SHORT).show();

        txtseeds.setText(String.valueOf(seeds));


    }



    public void  load()
    {
        try {
            FileInputStream fileInputStream =  openFileInput("Code.txt");
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read =fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            Log.d("Code", buffer.toString());

            String array[] = buffer.toString().split(" ");
            named = array[0];
            seeds = Integer.parseInt(array[1]);

            txtseeds.setText(String.valueOf(seeds));
            txtname.setText(named);

        } catch (Exception e) {
            Toast.makeText(this,"cant do", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        Toast.makeText(this,"Loaded", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    //textViewName.setText(obj.getString("name"));
                    //Toast.makeText(getApplicationContext(),obj.getString("name"),Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getApplicationContext(),obj.getString("address"),Toast.LENGTH_SHORT).show();
                    //   /textViewAddress.setText(obj.getString("address"));

                    if (obj.getString("name").equals("Namespace")) {
                        homestat.setText("Connected to Home");
                    }
                    else if (obj.getString("name").equals("Abhishek")){
                        String amt = obj.getString("amount");
                        amount = Integer.parseInt(amt);



                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle("Payment Confirmation");

                        // Setting Dialog Message
                        alertDialog.setMessage("Confirm your payment with Abhishek of "+ amt);

                        // Setting Icon to Dialog
                       // alertDialog.setIcon(R.drawable.delete);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                                seeds=seeds-amount;
                                // Write your code here to invoke YES event
                                Toast.makeText(getApplicationContext(), "Remaining seeds are "+seeds, Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
