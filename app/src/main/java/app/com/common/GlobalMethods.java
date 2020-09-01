package app.com.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import app.com.NotificationDrawable;
import app.com.notifyme.R;

public class GlobalMethods {
    static String URL = "https://app--notifyme.herokuapp.com/";
    //static String URL = "http://192.168.43.137:3000/";



    public static String GetSubString(String res)
    {
        return res.substring(13,16);

    }
    public static void print(Context context, String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    public static String getURL()
    {
        return URL;
    }

    public static void setCountForNotifcation(LayerDrawable icon, String count) {
    }

    public static void setCountForNotifcation(LayerDrawable icon, String count, Context ctx){
        NotificationDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof NotificationDrawable) {
            badge = (NotificationDrawable) reuse;
        } else {
            badge = new NotificationDrawable(ctx);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }


    }
