package app.com.notifyme;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import app.com.NotificationDrawable;
import app.com.adapters.NoticeAdapter;
import app.com.common.CheckConnection;
import app.com.common.GlobalMethods;
import app.com.common.Singleton;
import app.com.fragments.FilterFragment;
import app.com.fragments.NoticeExtendedFragment;
import app.com.fragments.SortFragment;
import app.com.models.Notice;

import android.view.Menu;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeDashboard extends AppCompatActivity implements NoticeAdapter.OnItemClickListener, SortFragment.ItemClickListener, FilterFragment.ItemClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private List<Notice> noticeListData;
    private NoticeAdapter noticeAdapter;
    private RecyclerView noticeView;
    SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private RecyclerView.LayoutManager SLayout;
    private View v;
    private ArrayList<Notice> noticeModelArrayList, filteredArrayList;
    private TextView writeheretext, navbarusername, navbaryearcourse, navbardesignation;
    private NavigationView navigationView;
    private LayoutInflater inflater;
    private View filterView, applyFilterView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ArrayList<Notice> tempFilterList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean networkStatus = CheckConnection.getInstance(this).getNetworkStatus();
        if(networkStatus==true) {
            setContentView(R.layout.activity_notice_dashboard);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
             navigationView = findViewById(R.id.nav_view);
            getSupportActionBar().setTitle("Notify Me");
             pref = getApplicationContext().getSharedPreferences("UserVals", 0); // 0 - for private mode
            editor = pref.edit();
            View viewNav = navigationView.getHeaderView(0);
            navbarusername = viewNav.findViewById(R.id.userName);
            navbaryearcourse = viewNav.findViewById(R.id.courseyear);
            navbardesignation = viewNav.findViewById(R.id.designation);


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

            /* to remove create notice*/
            if(pref.getInt("isCoordinator",-1)==0){
                TextView notice = findViewById(R.id.writeheretext);
                SearchView view = findViewById(R.id.searchView);
                RecyclerView scrollView = findViewById(R.id.recyclerView);
                ConstraintLayout constraintLayout = findViewById(R.id.inner);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(view.getId(),ConstraintSet.TOP,constraintLayout.getId(),ConstraintSet.TOP,224);
                constraintSet.connect(scrollView.getId(),ConstraintSet.TOP,constraintLayout.getId(),ConstraintSet.BOTTOM,374);
                constraintSet.applyTo(constraintLayout);
                notice.setVisibility(View.GONE);
            }
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                   R.id.nav_dash, R.id.nav_notice, R.id.nav_profile, R.id.nav_access,
                    R.id.nav_logout, R.id.nav_share, R.id.nav_send)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
            if(pref.getInt("isCoordinator",-1)!=2){
                hideItem();
            }
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_logout:
                            AlertDialog.Builder builder = new AlertDialog.Builder(NoticeDashboard.this);
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
                                    startActivity(new Intent(NoticeDashboard.this, LoginActivity.class));
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
                            startActivity(new Intent(NoticeDashboard.this,RequestAccessActivity.class));
                            return true;
                        case R.id.nav_dash:
                            startActivity(new Intent(NoticeDashboard.this,NoticeDashboard.class));
                            return true;
                        default:return false;
                    }
                }
                    });
            navigationView.getMenu().getItem(0).setChecked(true);

            SearchView simpleSearchView = findViewById(R.id.searchView);
            UpdateSearchView(simpleSearchView);
            noticeModelArrayList = new ArrayList<>();

            writeheretext = findViewById(R.id.writeheretext);
            writeheretext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent createNotice =new Intent(NoticeDashboard.this,createNotice.class);
                    startActivity(createNotice);
                }
            });
            progressDialog = new ProgressDialog(this);

            findViewById(R.id.filterbutton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog();
                }
            });
            findViewById(R.id.sortbutton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SortFragment addPhotoBottomDialogFragment =
                            SortFragment.newInstance();
                    addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                            SortFragment.TAG);

                }
            });

            swipeRefreshLayout = findViewById(R.id.swiperefresh_items);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    LoadData();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });



            PopulateData();
        }
        else{
            setContentView(R.layout.nointernet_view);
        }



    }


    /* to remove menu item*/
    private void hideItem()
    {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_access).setVisible(false);
        nav_Menu.findItem(R.id.nav_request_status).setVisible(false);
    }

    private void PopulateData(){
        noticeView = findViewById(R.id.recyclerView);
        noticeView.setHasFixedSize(true);
        SLayout = new LinearLayoutManager(this);
        noticeView.setLayoutManager(SLayout);
        noticeListData = new ArrayList<>();
        LoadData();


    }

    private void LoadData() {
        String URL;
        if(pref.getString("fregistrationNumber","")!=""){
            URL = GlobalMethods.getURL()+"faculty_fetchNotices";
        }
        else {
             URL = GlobalMethods.getURL() + "fetchNotice";
        }
        progressDialog.show();
        v =findViewById(R.id.root);

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("HAR",response);

                 noticeModelArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {

                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        if(dataArray.length()<1){
                            Snackbar.make(v, "No Notices found for you",
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Retry", new View.OnClickListener(){

                                        @Override
                                        public void onClick(View view) {
                                            LoadData();
                                        }
                                    }).show();
                        }
                        else {

                            for (int i = 0; i < dataArray.length(); i++) {

                                Notice noticeModel = new Notice();
                                ArrayList<String> attachmentList = new ArrayList<>();
                                JSONObject dataobj = dataArray.getJSONObject(i);
                                noticeModel.setName("Posted by: " + dataobj.getString("name"));
                                noticeModel.setEmailID(dataobj.getString("email_id"));
                                noticeModel.setContact(dataobj.getString("contact"));
                                noticeModel.setId(dataobj.getString("Notice_id"));
                                noticeModel.setImages(dataobj.getString("Images").split(",")[0]);
                                noticeModel.setPriority(dataobj.getString("priority"));
                                noticeModel.setTitle(dataobj.getString("title"));
                                noticeModel.setDescription(dataobj.getString("description"));
                                noticeModel.setIsCoordinator(dataobj.getString("isCoordinator"));
                                noticeModel.setDepartment(dataobj.getString("department"));
                                noticeModel.setCourse(dataobj.getString("course"));
                                noticeModel.setScope(dataobj.getString("scope"));
                                if(!dataobj.getString("Attachments").equals("null")) {
                                    String[] attachments = dataobj.getString("Attachments").split(",");
                                    for (String path : attachments) {
                                        attachmentList.add(path);
                                    }
                                    noticeModel.setAttachments(attachmentList);
                                }
                                String dateOfCreation = dataobj.getString("date_time");
                                noticeModel.setTimestamp(dateOfCreation);
                                noticeModelArrayList.add(noticeModel);

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(v, "Exception Occured",
                            Snackbar.LENGTH_LONG)
                           .show();
                }




                noticeAdapter = new NoticeAdapter(NoticeDashboard.this,noticeModelArrayList,NoticeDashboard.this);
                noticeView.setAdapter(noticeAdapter);
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HAR",error.toString());
                Snackbar.make(v, "Error Response from Server",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {
                                LoadData();
                            }
                        }).show();
                progressDialog.dismiss();


            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                // Log.d("HAR",String.valueOf(DepartmentID));
                if(pref.getString("fregistrationNumber","")!=""){
                    if (pref.getInt("fdepartmentid", -1) != -1) {
                        parameters.put("dept_id", String.valueOf(pref.getInt("fdepartmentid", -1)));
                    }
                    if (pref.getString("fcourse", "") != "") {
                        parameters.put("course_id", String.valueOf(pref.getString("fcourse", "")));
                    }
                    if (pref.getString("fyear", "") != "") {
                        parameters.put("year", String.valueOf(pref.getString("fyear", "")));
                    }
                }
                else {
                    if (pref.getInt("departmentid", -1) != -1)
                        parameters.put("dept_id", String.valueOf(pref.getInt("departmentid", -1)));
                    if (pref.getInt("courseid", -1) != -1)
                        parameters.put("course_id", String.valueOf(pref.getInt("courseid", -1)));
                    if (pref.getInt("year", -1) != -1)
                        parameters.put("year", String.valueOf(pref.getInt("year", -1)));
                }
                return parameters;
            }
        };
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


    private void UpdateSearchView(SearchView searchView){
        searchView.setActivated(true);
        searchView.setQueryHint("Search Notice..");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.setFocusable(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noticeAdapter.getFilter().filter(newText);
                if(noticeModelArrayList!=null){
                    ArrayList<Notice>temp = noticeAdapter.returnToActivity();
                    noticeModelArrayList.clear();
                    noticeModelArrayList.addAll(temp);
                }
                else{
                    noticeModelArrayList.addAll(noticeAdapter.returnToActivity());
                }
                //noticeModelArrayList = noticeAdapter.returnToActivity()
                //Collections.copy(noticeModelArrayList,noticeAdapter.returnToActivity());

                    //noticeModelArrayList = noticeAdapter.returnToActivity();
                return true;
            }
        });
    }

    private void openDialog() {
        FilterFragment addPhotoBottomDialogFragment =
                FilterFragment.newInstance(this, noticeModelArrayList, noticeAdapter);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                FilterFragment.TAG);
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



   @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onItemClick(ArrayList<Notice> list, View v, int position) {
        Notice rowRecord = list.get(position);
        TextView title = v.findViewById(R.id.title);
        TextView desc = v.findViewById(R.id.desc);
        TextView priority = v.findViewById(R.id.priority);
        TextView name = v.findViewById(R.id.name);
        TextView designation = v.findViewById(R.id.designation);
        TextView date = v.findViewById(R.id.date);
        NoticeExtendedFragment.display(getSupportFragmentManager(),rowRecord, NoticeDashboard.this);
        title.setTextColor(Color.DKGRAY);
        title.setTypeface(null, Typeface.NORMAL);
        desc.setTextColor(Color.DKGRAY);
        desc.setTypeface(null, Typeface.NORMAL);
        priority.setTextColor(Color.DKGRAY);
        priority.setTypeface(null, Typeface.NORMAL);
        name.setTextColor(Color.DKGRAY);
        name.setTypeface(null, Typeface.NORMAL);
        designation.setTextColor(Color.DKGRAY);
        designation.setTypeface(null, Typeface.NORMAL);
        date.setTextColor(Color.DKGRAY);
        date.setTypeface(null, Typeface.NORMAL);
    }

    @Override
    public void onPriorityClick() {
        ArrayList<Notice> tempNoticeArray = new ArrayList<>();
        tempNoticeArray.addAll(noticeModelArrayList);
        Collections.sort(tempNoticeArray,Notice.priorityComparator);
        noticeAdapter.swap(tempNoticeArray);
    }

    @Override
    public void onDateClick() {
        ArrayList<Notice> tempNoticeArray = new ArrayList<>();
        tempNoticeArray.addAll(noticeModelArrayList);
        Collections.sort(tempNoticeArray,Notice.dateCompartor);
        noticeAdapter.swap(tempNoticeArray);
    }


}
