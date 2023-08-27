package team1XuongMobile.fpoly.myapplication.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import team1XuongMobile.fpoly.myapplication.MainActivity;
import team1XuongMobile.fpoly.myapplication.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String CHANNEL_ID = "NewsNotification";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> dataMap = remoteMessage.getData();

            String title = dataMap.get("title");
            String body = dataMap.get("body");
            String image = dataMap.get("image");

            createNotification(body);

            addNotification(title, body, image);

        }

    }

    private void addNotification(String title, String body, String image) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID).
                setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(body);

        if (image != null) {

            if (image.length() > 10 && image.startsWith("http")){
                builder.setLargeIcon(getBitmapFromURL(image));
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(image)));
            }
        }

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);

        Intent pn = new Intent(this, MainActivity.class);
        pn.putExtra("title", title);
        pn.putExtra("body", body);
        pn.putExtra("image", image);
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            pendingIntent = PendingIntent.getActivity(this, 1, pn, PendingIntent.FLAG_IMMUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getActivity(this, 1, pn, PendingIntent.FLAG_UPDATE_CURRENT);

        }

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        int NOTIFICATION_ID = 1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            {
                managerCompat.notify(NOTIFICATION_ID, builder.build());
            }
            else
            {
                managerCompat.notify(NOTIFICATION_ID, builder.build());
            }
        }


    }

    private Bitmap getBitmapFromURL(String image) {

        try{
            URL url = new URL(image);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            return bitmap;
        }catch (Exception e){
            return null;
        }

    }

    private void createNotification(String body) {

        CharSequence name = "FASTTECH";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel chanel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            chanel.setDescription(body);

            NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(chanel);
        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}





