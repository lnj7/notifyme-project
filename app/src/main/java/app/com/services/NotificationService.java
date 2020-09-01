package app.com.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        pref = getApplicationContext().getSharedPreferences("UserVals", 0); // 0 - for private mode
        editor = pref.edit();
        editor.putString("token",s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getNotification()!=null){
            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();
        }
    }
}
