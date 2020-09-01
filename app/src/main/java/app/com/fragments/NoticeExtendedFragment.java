package app.com.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import app.com.models.Notice;
import app.com.notifyme.R;


public class NoticeExtendedFragment extends DialogFragment {
public static final String TAG = "example_dialog";

private Toolbar toolbar;
private AppBarLayout appBarLayout;
private TextView name, designation, course, date, contact, desc, title;
private ImageView banner;
private static Context ctx;
private static Notice record;
private Button attachments;
private ArrayList<String> attachmentPathList, attachmentNameList;
private ListView attachmentList;
private static final int MY_PERMISSIONS_REQUEST_STORAGE=1;
private int position;
private View v;

public static NoticeExtendedFragment display(FragmentManager fragmentManager, Notice record, Context c) {
        NoticeExtendedFragment filterDialog = new NoticeExtendedFragment();
        NoticeExtendedFragment.record = record;
        NoticeExtendedFragment.ctx = c;
        filterDialog.show(fragmentManager, TAG);
        return filterDialog;
        }

@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        }

@Override
public void onStart() throws NullPointerException {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
        }

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.notice_extended_view, container, false);
        toolbar = view.findViewById(R.id.viewtoolbar);
        appBarLayout = view.findViewById(R.id.appbar);
        name = view.findViewById(R.id.name_extended);
        designation = view.findViewById(R.id.designation_extended);
        title = view.findViewById(R.id.title_extended);
        course = view.findViewById(R.id.course_extended);
        contact = view.findViewById(R.id.contact_extended);
        date = view.findViewById(R.id.date_extended);
        banner = view.findViewById(R.id.bannerimg_extended);
        desc = view.findViewById(R.id.desc_extended);
        attachments = view.findViewById(R.id.attachments);
        attachmentList = view.findViewById(R.id.listattachment);
        return view;
        }

@Override
public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        NoticeExtendedFragment.this.dismiss();
        }
        });
        v=view;
        title.setText(record.getTitle());
        name.setText(record.getName());
        if(record.getIsCoordinator().equals("0")||record.getIsCoordinator().equals("1")) {
            designation.setText("Student Coordinator");
        }else{
            designation.setText(record.getIsCoordinator());
        }
        if(!record.getCourse().equals("")) {
            course.setText("(" + record.getCourse() + ")");
        }
        contact.setText("Contact: +91-"+record.getContact());
        String updatedDate = record.getTimestamp().substring(0,record.getTimestamp().indexOf("T"));
        String[] parts = updatedDate.split("-");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parts[2]+"-"+parts[1]+"-"+parts[0]);
        date.setText(stringBuilder);
        desc.setText(record.getDescription());
    if(record.getImages().equals("null")){
        Glide.with(ctx)
                .load(R.drawable.banner)
                .into(banner);
    }

    else {
        Glide.with(ctx)
                .load(record.getImages())
                .into(banner);
    }

    if(record.getPriority().equals("1")){
            appBarLayout.setBackgroundColor(getResources().getColor(R.color.highpriority));
            title.setTextColor(getResources().getColor(R.color.highpriority));
            attachments.setBackgroundColor(getResources().getColor(R.color.highpriority));
            desc.setTextColor(getResources().getColor(R.color.highpriority));
            //download.setCheckMarkDrawable(R.drawable.high_priority);
    }
    else if(record.getPriority().equals("2")){
            appBarLayout.setBackgroundColor(getResources().getColor(R.color.mediumpriority));
            title.setTextColor(getResources().getColor(R.color.mediumpriority));
            attachments.setBackgroundColor(getResources().getColor(R.color.mediumpriority));
        desc.setTextColor(getResources().getColor(R.color.mediumpriority));
    }
    else{
            appBarLayout.setBackgroundColor(getResources().getColor(R.color.lowpriority));
            title.setTextColor(getResources().getColor(R.color.lowpriority));
            attachments.setBackgroundColor(getResources().getColor(R.color.lowpriority));
        desc.setTextColor(getResources().getColor(R.color.lowpriority));
    }
       ArrayList<String> attachmentListContainer = record.getAttachments();
       attachmentPathList = new ArrayList<>();
       attachmentNameList = new ArrayList<>();
       try {
           if(!(attachmentListContainer.size()<1))
           {
           for (String record : attachmentListContainer) {
               attachmentPathList.add(record);
               String tempName = record.substring(record.lastIndexOf("/") + 1);
               String nameLong = tempName.substring(0, tempName.lastIndexOf("."));
               attachmentNameList.add(nameLong.substring(nameLong.lastIndexOf("-") + 1));
           }
           }
       }catch(Exception e){
           Toast.makeText(ctx,"Exception in parsing attachment list",Toast.LENGTH_LONG);
       }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ctx,R.layout.downloadbutton_layout, attachmentNameList);
        attachmentList.setAdapter(arrayAdapter);
        attachmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (CheckPermission()) {
                       DownloadSource(i);
                    }
                    else{
                        position=i;
                       RequestPermission();
                    }
                }
                else{
                    DownloadSource(i);
                }

            }
        });


}

private boolean CheckPermission(){
    int result = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    if (result == PackageManager.PERMISSION_GRANTED) {
        return true;
    } else {
        return false;
    }
}

    private void RequestPermission() {
    ActivityCompat.requestPermissions((Activity) ctx, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        DownloadSource(position);
                } else {
                    Snackbar.make(v, "Permission Denied, Could not download!",
                            Snackbar.LENGTH_LONG)
                            .setAction("Retry", new View.OnClickListener(){

                                @Override
                                public void onClick(View view) {
                                    RequestPermission();
                                }
                            })
                            .show();
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

private void DownloadSource(int i){
    DownloadManager downloadManager = (DownloadManager) ( ctx).getSystemService(Context.DOWNLOAD_SERVICE);
    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(attachmentPathList.get(i)));
    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(attachmentNameList.get(i))
            .setDescription("Downloading Attachment")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, attachmentNameList.get(i))
            .setVisibleInDownloadsUi(true);

    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {
            Snackbar.make(v, "Download success...",
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    };
    ctx.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    downloadManager.enqueue(request);

}
        }

