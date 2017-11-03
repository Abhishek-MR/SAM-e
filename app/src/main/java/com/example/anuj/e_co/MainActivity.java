package com.example.anuj.e_co;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
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
import java.util.Locale;

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


    public String named="User";
    public int seeds = 0,amount=0;

    public String currentDateTime;
    public  TextView homestat;

    int tryname=0;


    private TextToSpeech tts;
    private ArrayList<String> questions;
    private String name, surname, age, asName;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    private static final String SEEDS = "seed";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String AS_NAME = "as_name";



    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ass
        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listen();
            }
        });

        loadQuestions();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    speak("Hello");


                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        //over

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
            load();
            seeds=seeds-100;
            Toast.makeText(MainActivity.this, "You bought card", Toast.LENGTH_SHORT).show();
            databaseHelper.insertData("Bought a card", currentDateTime);
            store();
        }
        else
            Toast.makeText(MainActivity.this, "You don't have enough credits for this transaction", Toast.LENGTH_SHORT).show();

        txtseeds.setText(String.valueOf(seeds));


    }



    public void  load()
    {

            named = preferences.getString(NAME, null);
            seeds = preferences.getInt(SEEDS, 0);
            txtseeds.setText(String.valueOf(seeds));
            txtname.setText(named);
    }

    public void store()
    {
        editor.putString(NAME,named).apply();
        editor.putInt(SEEDS,seeds).apply();
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

            if(requestCode == 100){
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String inSpeech = res.get(0);
                    recognition(inSpeech);
                }
            }
        }
    }

    private void loadQuestions(){
        questions = new ArrayList<>();
        questions.clear();
        questions.add("Hello, what is your name?");
        questions.add("What is your surname?");
        questions.add("How old are you?");
        questions.add("That's all I had, thank you ");
    }

    private void listen(){
        wait4inp();
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak now");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    private void recognition(String text){

        Log.e("Speech",""+text);
        String[] speech = text.split(" ");
        if(text.contains("hello")){
            speak(questions.get(0));
            listen();
        }

        if(tryname==1)
        {
            if (text.contains("yes"))
            {
                speak("Hello "+preferences.getString(NAME,null)+", you are given 200 seeds to start with." );
            }
            else if (text.contains("no"))
            {
                speak(questions.get(0));
                listen();
            }
        }


        //
        if(text.contains("my name is")){
            named = speech[speech.length-1];
            Log.e("THIS", "" + named);
            editor.putString(NAME,named).apply();
            speak("Is your name "+preferences.getString(NAME,null)+" ?" );
            tryname=1;
            listen();


        }
        //This must be the age
        if(text.contains("years") && text.contains("old")){
            String age = speech[speech.length-3];
            Log.e("THIS", "" + age);
            editor.putString(AGE, age).apply();
        }

        if(text.contains("what time is it")){
            SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
            Date now = new Date();
            String[] strDate = sdfDate.format(now).split(":");
            if(strDate[1].contains("00"))
                strDate[1] = "o'clock";
            speak("The time is " + sdfDate.format(now));

        }

        if(text.contains("wake me up at")){
            speak(speech[speech.length-1]);
            String[] time = speech[speech.length-1].split(":");
            String hour = time[0];
            String minutes = time[1];
            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            i.putExtra(AlarmClock.EXTRA_HOUR, Integer.valueOf(hour));
            i.putExtra(AlarmClock.EXTRA_MINUTES, Integer.valueOf(minutes));
            startActivity(i);
            speak("Setting alarm to ring at " + hour + ":" + minutes);
        }

        if(text.contains("thank you")){
            speak("Thank you too " + preferences.getString(NAME, null));
        }

        if(text.contains("how old am I")){
            speak("You are "+preferences.getString(AGE,null)+" years old.");
        }

        if(text.contains("what is your name")){
                speak("I am your Eco buddy");
        }

        if(text.contains("call you")){
            String name = speech[speech.length-1];
            editor.putString(AS_NAME,name).apply();
            speak("I like it, thank you "+preferences.getString(NAME,null));
        }

        if(text.contains("what is my name")){
            speak("Your name is "+preferences.getString(NAME,null));
        }
    }

    void wait4inp()
    {
        try {
            Thread.sleep(1500); //1000 milliseconds is one second.
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
