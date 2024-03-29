package com.mywidget.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mywidget.ui.main.MainActivity;
import com.mywidget.R;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * 구글 토큰을 얻는 값입니다.
     * 아래 토큰은 앱이 설치된 디바이스에 대한 고유값으로 푸시를 보낼때 사용됩니다.
     * **/

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("Firebase", " : " + s);

    }

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage message) {
        if (message.getNotification() == null || message.getNotification().getTag() == null) {
            return;
        }

        SharedPreferences shared = getSharedPreferences(getString(R.string.inChatPush), MODE_PRIVATE);
        String sharedRoomKey = shared.getString("roomKey", "");
        String chatRoomData = message.getNotification().getTag(); //참여중인 방 정보

        if (sharedRoomKey != null && sharedRoomKey.equals(chatRoomData)) {
            //내가 현재 보고있는 방이다 push 를 받아선 안된다
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(getString(R.string.runChat), chatRoomData);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.notification_channel_id);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.launcher_my)
                        .setContentTitle(Objects.requireNonNull(message.getNotification()).getTitle())
                        .setContentText(Objects.requireNonNull(message.getNotification()).getBody())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.notification_channel_id);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}