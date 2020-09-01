package app.com.notifyme;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.com.common.GlobalMethods;
import app.com.common.Singleton;
import app.com.models.Course;
import app.com.models.Department;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private Spinner Course_Spinner, Department_Spinner;

    private ArrayList<Department> Department;
    private ArrayList<Course> Course;

    private static int DepartmentID, CourseID;
    private View v;
    private Button Register;
    private EditText Name, Reg_no, year, Email, Contact, password;
    private TextView signinredirect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        v = findViewById(R.id.main);
        InitializeUIComponent();

    }

    private void InitializeUIComponent(){
        Department_Spinner = findViewById(R.id.dept_id);
         Course_Spinner = findViewById(R.id.course_id);
         Name = findViewById(R.id.name);
         Reg_no = findViewById(R.id.Reg_no);
         year = findViewById(R.id.year);
         Email = findViewById(R.id.email_id);
         Contact = findViewById(R.id.contact);
         password = findViewById(R.id.password);
         Register = findViewById(R.id.RegisterUser);
        signinredirect = findViewById(R.id.signin);
         Register.setOnClickListener(this);
         signinredirect.setOnClickListener(this);

        Department = new ArrayList<>();
        Course = new ArrayList<>();

        FillDepartment();

        Department_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(i>0)
            {
                final Department department = (Department) Department_Spinner.getItemAtPosition(i);
                Log.d("HAR", "onItemSelected: country: "+department.GetDepartmentID());
                DepartmentID=department.GetDepartmentID();
                Course.clear();
                FillCourses(department.GetDepartmentID());
            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Course_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0)
                {
                    final Course course = (Course) Course_Spinner.getItemAtPosition(i);
                    CourseID=course.GetCourseID();
                    Log.d("HAR", "onItemSelected: country: "+course.GetCourseID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void FillCourses(final int DepartmentID)
    {
        Course.add(new Course(0, "Choose a Course"));
        final String URL = GlobalMethods.getURL()+"course";
        progressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //JSONArray response = null;
                        Log.d("HAR",response.toString());
                        try {
                            JSONObject j = new JSONObject(response);
                            JSONArray data = j.getJSONArray("data");
                            for(int i=0;i<data.length();i++)
                            {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                Course.add(new Course(jsonObject1.getInt("Course_id"),
                                        jsonObject1.getString("Course_branch")));
                            }
                            Course_Spinner.setAdapter(new ArrayAdapter<Course>(RegisterActivity.this,R.layout.simple_spinner_dropdown_item,Course));
                            progressDialog.dismiss();
                        }
                        catch (JSONException e){

                        }
                        progressDialog.dismiss();
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
                Log.d("HAR",String.valueOf(DepartmentID));
                parameters.put("dept_id", String.valueOf(DepartmentID));
                return parameters;
            }
        };
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void FillDepartment(){
        Department.add(new Department(0, "Choose a Department"));
        final String URL = GlobalMethods.getURL()+"department";
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //JSONArray response = null;
                        Log.d("HAR",response.toString());
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0;i<data.length();i++)
                            {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                Department.add(new Department(jsonObject1.getInt("Dept_id"),
                                        jsonObject1.getString("Dept_name")));
                            }
                            Department_Spinner.setAdapter(new ArrayAdapter<Department>(RegisterActivity.this,R.layout.simple_spinner_dropdown_item,Department));
                            progressDialog.dismiss();
                        }
                        catch (JSONException e){

                        }
                        progressDialog.dismiss();
                            }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HAR",error.toString());
                        progressDialog.dismiss();


                    }
                });



        Singleton.getInstance(this).addToJsonRequestQueue(jsonObjectRequest);
    }

    private void RegisterUser()
    {
        final String URL = GlobalMethods.getURL()+"student_register";
        progressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //JSONArray response = null;
                Log.d("HAR",response.toString());
                try {
                    JSONObject j = new JSONObject(response);
                    int data = (int) j.get("code");

                    //JSONObject jsonObject1 = data.getJSONObject(0);
                    if(data==345){
                        Snackbar.make(v, "Internal Server Error",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                    else if(data==401){
                        Snackbar.make(v, "Registration Success, Check your email for verification mail",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                    else if(data==402){
                        Snackbar.make(v, "Email already exists",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                    else if(data==403){
                        Snackbar.make(v, "Contact number already registered",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                    else if(data==404){
                        Snackbar.make(v, "Email Address is not valid, try with different email address",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                    else if(data==501){
                        Snackbar.make(v, "Internal database error",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                    else if(data==200){
                        Snackbar.make(v, "Registration Success, Check your email for verification email",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }

                    progressDialog.dismiss();
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
                parameters.put("Reg_id", Reg_no.getText().toString());
                parameters.put("name", Name.getText().toString());
                parameters.put("dept_id", String.valueOf(DepartmentID));
                parameters.put("course_id", String.valueOf(CourseID));
                parameters.put("year", year.getText().toString());
                parameters.put("email_id", Email.getText().toString());
                parameters.put("contact", Contact.getText().toString());
                parameters.put("password", password.getText().toString());
                parameters.put("isCoordinator", String.valueOf(0));
                return parameters;
            }
        };
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {
    if(view.getId()==R.id.RegisterUser)
    {
        RegisterUser();
    }
    else if(view.getId()==R.id.signin){
        Intent in=new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(in);
        finish();
    }
    }
}
