package com.example.music_app.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.music_app.R;
import com.example.music_app.activities.MainActivity;
import com.example.music_app.activities.NotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FirebaseNotification extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "MY_CHANNEL";

    public static void sendNotificationToUser(String messageBody, String receiverToken) {
        new Thread(() -> {
            try {
                URL url = new URL("https://fcm.googleapis.com/v1/projects/music-app-967da/messages:send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + "ya29.c.c0AY_VpZgECyYGcabQ9uuGz-jMJDJpGAtIUbvJS-CAByZp7a9vs7kkdUkLSFFFopEbQhkY-pIVE7_f_nWrbl8HTA0t7WJpTKFdFJJBM3pklvqlQ-Pn_THUhYxOl-Z3uBou02tt5Pce742zCfTfhhAXA7sNvI6QvO8LY0WGDIHuUV3jrf6roymWGH56ZoiL6L_FsCLO8WzsadMlc-rML8-8lAkmJwfgaxx0m6NgmL1tR644CUS0-_uCClQRhMZMEdN2H55jniVusZQpeyVkEButhZ9ji6BV_IPKu7sqJUsYFwO781MJmY0A45cgG6rZVnaroOnGaNcJkpaOxiYtAqkW9QBs0iqiylio7iuEEvegqJvxpRI09lCEZb1OxQN387Dpwsr01kwV2zexgS3FXzhg6kb-2ecURu0qsd5oXRaMsejBJiIMJh0eSx6vBtZbM73xmoe-unrx3VI79wqx8IYMxX4QMa4uXwpZ3w12kvmhF3V9S1BV5QOo5UjFWmmMRdxBMwfx-uMUb7tJgg2twl4jacmj48t2SSnamV8tBrbitF2mfYqpbawcZ4Z0OzU0i1j-0pcBno_gZrwXggUIe8qY_70mRMQ01UckhSfXb3Xih5uWpttX6skeRkWbafoOxkx608ud_w55dR9SMe0ywgwyri6bhsqMqzMjInfMZc9SI8dXugBRdBIXg6UhokrqUyBSn_s2Zc9n-bx_S3nF4vY26JyZmJQhf5yve9wuY3uveSSYiWw-UhgVWW1WI9_ml48yqdle-6kUOjaf4qd31BhzhWW7kkM1k_bz26UnWw16Fg8J18jiM0Zmf2eRb8dIF4oI6XJyl1Fq-cofF5j8z_Ocopkkp18admSUhRb5cyMt32zrnkb0jxiYhYlwxRoiSU1wMtSyBOe43lar1spocx07v9Vi43zSr1hSy0xdYedo2uxxnB1YjbWr9B07hwyoldb6Fi6B-MlZla-w1YgzMaswWYOn8u5k09jro7pWqeudZa3kul5veamnlnd");
                conn.setRequestProperty("Content-Type", "application/json; UTF-8");

                String payload = "{\n" +
                        "  \"message\": {\n" +
                        "    \"token\": \"" + receiverToken + "\",\n" +
                        "    \"notification\": {\n" +
                        "      \"body\": \"" + messageBody + "\",\n" +
                        "      \"title\": \"Bài hát mới\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";

                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = payload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Log.d("FCM123", "Response: " + response.toString());
                    }
                } else {
                    Log.e("FCM123", "Failed to send notification, response code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here.
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Music App Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

}
