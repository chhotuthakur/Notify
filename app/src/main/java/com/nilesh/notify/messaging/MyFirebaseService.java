package com.nilesh.notify.messaging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nilesh.notify.MainActivity;
import com.nilesh.notify.R;

import java.util.Random;



public class MyFirebaseService extends FirebaseMessagingService {

    private final String CHANNEL_ID="channel_id";
    Context mContext;



    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        mContext = this;

        Intent i = new Intent(this, MainActivity.class);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(manager);
        }

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent ione = PendingIntent.getActivities(this, 0, new Intent[]{i}, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = null;

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(message.getData().get("title"))
                .setContentText(message.getData().get("message"))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setContentIntent(ione)
                .build();

        manager.notify(notificationId, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager manager) {

        NotificationChannel notificationChannel= new NotificationChannel(CHANNEL_ID,"channelName",NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("my desc");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.WHITE);
        manager.createNotificationChannel(notificationChannel);
    }
}
