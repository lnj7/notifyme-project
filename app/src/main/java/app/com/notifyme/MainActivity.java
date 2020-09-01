package app.com.notifyme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import app.com.common.CheckConnection;
import app.com.common.GlobalMethods;
import app.com.common.Singleton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=4000;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("UserVals", 0); // 0 - for private mode
        editor = pref.edit();
        v = findViewById(R.id.main);
        boolean networkStatus = CheckConnection.getInstance(this).getNetworkStatus();
        if(networkStatus==true) {

            progressDialog = new ProgressDialog(this);
            if (pref.getString("registrationNumber", "") != "") {
                LoginUser();
            } else if (pref.getString("fregistrationNumber", "") != "") {
                LoginUser();
            } else {
                callSplash();
            }
        }
        else{
            Snackbar snackbar;
            snackbar = Snackbar.make(v, "You are not connected to internet",
                    Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();

        }
    }




    private void LoginUser()
    {
        final Intent homeintent=new Intent(MainActivity.this,NoticeDashboard.class);
        final String URL = GlobalMethods.getURL()+"login";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //JSONArray response = null;
                Log.d("HAR",response.toString());
                try {
                    JSONObject j = new JSONObject(response);
                    String data = (String) j.get("code");

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    JSONObject dataobj = dataArray.getJSONObject(0);


                     if(data.toString().equals("100")){
                        editor.putInt("isCoordinator",0);
                        editor.commit();
                        callSplash();

                    }
                    else if(data.equals("200")){
                        editor.putInt("isCoordinator",1);
                        editor.commit();
                        callSplash();
                    }
                    else if(data.equals("300")){
                        editor.putInt("isCoordinator",2);
                        editor.commit();
                        callSplash();
                    }




                }
                catch (JSONException e){
                    progressDialog.dismiss();
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, "Unexpected response from server",
                            Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    snackbar.show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HAR",error.toString());
                progressDialog.dismiss();
                Snackbar.make(v, "Restart Server",
                        Snackbar.LENGTH_LONG)
                        .show();


            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                // Log.d("HAR",String.valueOf(DepartmentID));
                if(pref.getString("registrationNumber","")!=""){
                    parameters.put("reg_id", pref.getString("registrationNumber",""));
                }
                else if(pref.getString("fregistrationNumber","")!=""){
                    parameters.put("reg_id", pref.getString("fregistrationNumber",""));
                }
                parameters.put("password", pref.getString("password",""));
                return parameters;
            }
        };
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void callSplash(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(pref.getInt("isCoordinator",-1)==0 || pref.getInt("isCoordinator",-1)==1
                        || pref.getInt("isCoordinator",-1)==2) {
                    Intent homeintent = new Intent(MainActivity.this, NoticeDashboard.class);
                    startActivity(homeintent);
                    finish();
                }
                else{
                    Intent homeintent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(homeintent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);
    }
}