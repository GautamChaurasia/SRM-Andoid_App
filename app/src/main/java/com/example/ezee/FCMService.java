package com.example.ezee;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FCMService extends FirebaseMessagingService {

    SharedPreferences sp;
    SharedPreferences.Editor edt;

    String title, msg, dt;

    // TODO: Change Notification Limit here
    int uid, limit = 10;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        sp = getSharedPreferences("pointers", MODE_PRIVATE);

        SimpleDateFormat ft = new SimpleDateFormat("dd MMMM, yy-hh:mm aa");
        dt = ft.format(new Date());
//        Log.i("Date", dt);

        if (remoteMessage.getData() != null){
            title = remoteMessage.getData().get("title");
            msg = remoteMessage.getData().get("body");
            if (!sp.getBoolean("ADMIN", false)){
                new dbThread().start();
            }
            showNotification(title, msg);
        }
        else if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            msg = remoteMessage.getNotification().getBody();
            if (!sp.getBoolean("ADMIN", false)){
                new dbThread().start();
            }
            showNotification(title, msg);
        }
    }

    public void showNotification(String title, String message){

        Intent intent = new Intent(this, NotificationHistory.class);
        String channel_id = "fb_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24);

        if (!sp.getBoolean("ADMIN", false)){
            builder.setContentIntent(pendingIntent);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "fb", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(uid, builder.build());
    }

    class dbThread extends Thread{
        @Override
        public void run() {
            super.run();

            sp = getSharedPreferences("pointers", MODE_PRIVATE);
            uid = sp.getInt("END", 0)+1;

            edt = sp.edit();
            edt.putInt("END", uid);
            edt.apply();

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "notifications").build();

            NotiDAO notiDao = db.userDao();
            notiDao.insert(new NotiClass(uid, title, msg, dt));
//            Log.i("Task", "Inserted in DB");

            int beg = sp.getInt("BEG", 0);
            int end = sp.getInt("END", 0);

            if (end-beg+1 > limit){
                notiDao.deleteByUID(beg);
                edt.putInt("BEG", beg+1);
                edt.apply();
            }
        }
    }

}
