package app.com.common;

import android.app.Notification;
import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class Notifications {
    private Context context;
    private String token;
    private static Notifications notifications;

    public Notifications(){

    }


    public String getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            token = task.getResult().getToken();
                        }
                        else{
                            token="nv";
                        }
                    }
                });
            return token;
    }

}
