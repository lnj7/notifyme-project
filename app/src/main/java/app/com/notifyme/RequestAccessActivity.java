package app.com.notifyme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import app.com.common.GlobalMethods;
import app.com.common.Singleton;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RequestAccessActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private TextView navbarusername, navbaryearcourse, navbardesignation, plusButton;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ConstraintLayout parentLayout;
    private ConstraintSet set;
    private ArrayList<ImageView> imageviews;
    private ArrayList<TextInputLayout> inputfields;
    private ArrayList<EditText> inputfieldsinside;
    private ArrayList<TextView> buttons;
    private Button requestAccess;
    private int countrows = 0;
    private ProgressDialog progressDialog;
    private DrawerLayout drawer;

    private static final String TAG = "REQUEST_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_access);
        CommonWorkOfMenuItems();
        imageviews = new ArrayList<>();
        inputfields = new ArrayList<>();
        buttons = new ArrayList<>();
        inputfieldsinside = new ArrayList<>();
        ImageView firstView = findViewById(R.id.image1);
        imageviews.add(firstView);
        TextInputLayout firstEditField = findViewById(R.id.text);
        EditText editText = findViewById(R.id.registration);
        inputfieldsinside.add(editText);
        inputfields.add(firstEditField);
        plusButton = findViewById(R.id.plusbuttton);
        buttons.add(plusButton);
        progressDialog = new ProgressDialog(this);
        parentLayout = findViewById(R.id.mainlayout);
        set = new ConstraintSet();
        requestAccess = findViewById(R.id.requestaccessbtn);
        requestAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialogForValidity();
            }
        });


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRow(countrows);
            }
        });

    }

    private void OpenDialogForValidity() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.duration_layout, null);
        alert.setTitle(R.string.validity);
        alert.setIcon(R.drawable.ic_access_time_black_24dp);

        final EditText dialog_editText = mView.findViewById(R.id.dialog_editText);
        alert.setView(mView);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SaveRequestNetworkCall(dialog_editText.getText().toString());
                dialogInterface.cancel();
                progressDialog.show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog d=alert.create();
        d.show();
    }

    private void SaveRequestNetworkCall(final String duration) {
        String URL = GlobalMethods.getURL()+"make_requests";
        Gson gson = new Gson();
        String[] array = new String[inputfieldsinside.size()];
        int tempvar = 0;
        for(EditText edittext: inputfieldsinside){
            if(edittext.getText().toString()!=""){
            array[tempvar++]=edittext.getText().toString();
            }
        }
        final String finalArray = gson.toJson(array);

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //JSONArray response = null;
                JSONObject j;
                Log.d("HAR",response.toString());
                try {
                    j = new JSONObject(response);
                    String data = (String) j.get("code");
                }

                catch (JSONException e){
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HAR",error.toString());
                progressDialog.dismiss();


            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                // Log.d("HAR",String.valueOf(DepartmentID));
                parameters.put("sender_id",pref.getString("fregistrationnumber",""));
                parameters.put("student_id", finalArray);
                parameters.put("duration", duration);
                return parameters;
            }
        };
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void addNewRow(int count){

        ImageView image = new ImageView(this);
        image.setId(View.generateViewId());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int)getResources().getDimension(R.dimen.requestimage),(int)getResources().getDimension(R.dimen.requestimage));
        image.setLayoutParams(layoutParams);
        image.setImageResource(R.drawable.faculty);
        parentLayout.addView(image);

        TextInputLayout textInputLayout = new TextInputLayout(this);
        textInputLayout.setId(View.generateViewId());
        ConstraintLayout.LayoutParams textinputparams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        textInputLayout.setLayoutParams(textinputparams);
        parentLayout.addView(textInputLayout);

        EditText maintext = new EditText(this);

        maintext.setId(View.generateViewId());
        maintext.setHint("Registration Number");
        maintext.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        maintext.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        maintext.setMaxLines(1);
        textInputLayout.addView(maintext);

        TextView plus = new TextView(this);
        plus.setId(View.generateViewId());
        ConstraintLayout.LayoutParams plusButtonparams = new ConstraintLayout.LayoutParams((int)getResources().getDimension(R.dimen.plusbutton),(int)getResources().getDimension(R.dimen.plusbutton));
        plusButtonparams.setMarginEnd((int)getResources().getDimension(R.dimen.plusbuttonend));
        plus.setLayoutParams(plusButtonparams);
        plus.setBackgroundResource(R.drawable.plus);
        plus.setGravity(Gravity.RIGHT);
        plus.setMinHeight((int)getResources().getDimension(R.dimen.requestminheight));
        parentLayout.addView(plus);
        set.clone(parentLayout);
        set.connect(image.getId(),ConstraintSet.TOP,inputfields.get(count).getId(),ConstraintSet.BOTTOM,((ConstraintLayout.LayoutParams)imageviews.get(count).getLayoutParams()).topMargin);
        set.connect(image.getId(),ConstraintSet.START,parentLayout.getId(),ConstraintSet.START,((ConstraintLayout.LayoutParams)imageviews.get(count).getLayoutParams()).getMarginStart());
        set.applyTo(parentLayout);

        ConstraintSet tempset = new ConstraintSet();
        tempset.clone(parentLayout);
        tempset.clear(inputfields.get(count).getId(),ConstraintSet.BOTTOM);
        tempset.connect(textInputLayout.getId(),ConstraintSet.END,parentLayout.getId(),ConstraintSet.END,((ConstraintLayout.LayoutParams)inputfields.get(count).getLayoutParams()).getMarginEnd());
        tempset.connect(textInputLayout.getId(),ConstraintSet.START,image.getId(),ConstraintSet.END,((ConstraintLayout.LayoutParams)inputfields.get(count).getLayoutParams()).getMarginStart());
        tempset.connect(textInputLayout.getId(),ConstraintSet.BOTTOM,parentLayout.getId(),ConstraintSet.BOTTOM,((ConstraintLayout.LayoutParams)inputfields.get(count).getLayoutParams()).bottomMargin);
        tempset.connect(textInputLayout.getId(),ConstraintSet.TOP,inputfields.get(count).getId(),ConstraintSet.BOTTOM,((ConstraintLayout.LayoutParams)inputfields.get(count).getLayoutParams()).topMargin);
        tempset.applyTo(parentLayout);

        ConstraintSet hset = new ConstraintSet();
        hset.clone(parentLayout);
        hset.clear(buttons.get(count).getId(),ConstraintSet.BOTTOM);
        hset.connect(plus.getId(),ConstraintSet.START,textInputLayout.getId(),ConstraintSet.END);
        hset.connect(plus.getId(),ConstraintSet.BOTTOM,parentLayout.getId(),ConstraintSet.BOTTOM);
        hset.connect(plus.getId(),ConstraintSet.END,parentLayout.getId(),ConstraintSet.END,((ConstraintLayout.LayoutParams)buttons.get(count).getLayoutParams()).getMarginEnd());
        hset.connect(plus.getId(),ConstraintSet.TOP,textInputLayout.getId(),ConstraintSet.TOP, (int) (((ConstraintLayout.LayoutParams)buttons.get(count).getLayoutParams()).topMargin));

        hset.applyTo(parentLayout);
        imageviews.add(image);
        inputfields.add(textInputLayout);
        buttons.add(plus);
        inputfieldsinside.add(maintext);
        buttons.get(count).setVisibility(View.INVISIBLE);
        countrows++;
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRow(countrows);
            }
        });



    }

    private void CommonWorkOfMenuItems() {
        
        Toolbar toolbar = findViewById(R.id.requesttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Request Student Access</small>"));
         drawer = findViewById(R.id.drawer_layout_request);
        navigationView = findViewById(R.id.nav_view_request);

        View viewNav = navigationView.getHeaderView(0);
        navbarusername = viewNav.findViewById(R.id.userName);
        navbaryearcourse = viewNav.findViewById(R.id.courseyear);
        navbardesignation = viewNav.findViewById(R.id.designation);
        pref = getApplicationContext().getSharedPreferences("UserVals", 0); // 0 - for private mode
        editor = pref.edit();

        if(pref.getString("name","")!="" ){
            navbarusername.setText(pref.getString("name",""));
        }
        else if(pref.getString("fname","")!=""){
            navbarusername.setText(pref.getString("fname",""));
        }

        if(pref.getString("fregistrationNumber","")==""){
            navbaryearcourse.setText(pref.getString("departmentname","")+", "+
                    pref.getString("coursename","")+", "+String.valueOf(pref.getInt("year",0))+" year");
        }
        else{
            navbardesignation.setVisibility(View.VISIBLE);
            navbardesignation.setText(pref.getString("fdesignation","")+",");
            navbaryearcourse.setText(pref.getString("fdepartmentname","")+" department");
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dash, R.id.nav_notice, R.id.nav_profile, R.id.nav_access,
                R.id.nav_logout, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(RequestAccessActivity.this);
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sharedPreferences = getSharedPreferences("UserVals",
                                        0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                finish();
                                startActivity(new Intent(RequestAccessActivity.this, LoginActivity.class));
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return true;
                    case R.id.nav_access:
                        startActivity(new Intent(RequestAccessActivity.this,RequestAccessActivity.class));
                        return true;
                    case R.id.nav_dash:
                        startActivity(new Intent(RequestAccessActivity.this,NoticeDashboard.class));
                        return true;
                    default:return false;
                }
            }
        });
        navigationView.getMenu().getItem(3).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notice_dashboard, menu);
        MenuItem menuItem = menu.findItem(R.id.ic_group);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        GlobalMethods.setCountForNotifcation(icon,"3",this);
        return true;

    }

}
