package cz.prasoprd.msglogger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Messages {

    public static String deleted = "";
    public static String edited = "";

    public static void addDelete(String message) {
        createNotification(message,"Deleted");
        deleted += "\n" + message;
    }

    public static void addEdit(String message) {
        createNotification(message,"Edited");
        edited += "\n" + message;
    }

    public static void createNotification(String message, String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("prasoprd","prasoprd's apps", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("For applications created by prasoprd");
            MainActivity.clazz.getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        NotificationManagerCompat.from(MainActivity.clazz).notify((int) System.currentTimeMillis(), new NotificationCompat.Builder(MainActivity.clazz,"prasoprd").setSmallIcon(R.mipmap.ic_launcher_foreground).setContentTitle(title).setContentText(message).setPriority(NotificationCompat.PRIORITY_LOW).build());
    }
}
