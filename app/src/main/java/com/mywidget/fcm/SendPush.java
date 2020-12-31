package com.mywidget.fcm;

import android.util.Log;

import com.mywidget.data.apiConnect.ApiConnection;
import com.mywidget.data.model.RoomDataModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class SendPush {

    private CompositeDisposable subscrib;

    public void send(String token, String memo, String myNickname, RoomDataModel roomData) {

        OkHttpClient okHttpClient = new OkHttpClient();

        try {

            MediaType json = MediaType.parse("application/json; charset=utf-8");

            JSONObject root = new JSONObject();
            JSONObject notification = new JSONObject();

            notification.put("title", myNickname+" 메세지");
            notification.put("body", memo);
            //notification.put("clickAction", roomData.getRoomKey());
            notification.put("tag", roomData.getMaster() + "&&" + roomData.getRoomKey());

            JSONObject message = new JSONObject();

            //message.put("message", message);
            root.put("notification", notification);
            root.put("to", token);
            root.put("content_available", true);
            root.put("priority", "high");

            RequestBody body = RequestBody.create(json, String.valueOf(root));
            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "key=AAAAc7jMs94:APA91bEaUUpvn4eGas12VITCoc4zU0dLM3RuXVoulz3Qphx3fHDU_tzxcmgUDmWTI6R7WsPLBTZaTCFVvdmrSTvBEF9iuB_WtTtqf3lFTgX0UpdFo-8i2FxcRQ2QYSRU3ns-uBjONTg_")
                    .url("https://fcm.googleapis.com/fcm/send")
                    .post(body)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("messageResponse", response.body().string());
                }
            });

        } catch (Exception e) {

        }
    }

    public void test(final String token) {
        new Thread(() -> {
            try {
                // FMC 메시지 생성 start
                JSONObject root = new JSONObject();
                JSONObject notification = new JSONObject();

                notification.put("title", "test");
                notification.put("body", "dddd");
                root.put("notification", notification);
                root.put("to", token);
                // FMC 메시지 생성 end

                HashMap<String, String> has = new HashMap<>();
                has.put("Content-Type", "application/json");
                has.put("Authorization", "key=AAAAc7jMs94:APA91bEaUUpvn4eGas12VITCoc4zU0dLM3RuXVoulz3Qphx3fHDU_tzxcmgUDmWTI6R7WsPLBTZaTCFVvdmrSTvBEF9iuB_WtTtqf3lFTgX0UpdFo-8i2FxcRQ2QYSRU3ns-uBjONTg_");

                subscrib.add(ApiConnection.Companion.Instance().getRetrofitService()
                        .fcmTest("https://fcm.googleapis.com/fcm/send", has, root)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());

                /*URL Url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.addRequestProperty("Authorization", "key=AAAAc7jMs94:APA91bEaUUpvn4eGas12VITCoc4zU0dLM3RuXVoulz3Qphx3fHDU_tzxcmgUDmWTI6R7WsPLBTZaTCFVvdmrSTvBEF9iuB_WtTtqf3lFTgX0UpdFo-8i2FxcRQ2QYSRU3ns-uBjONTg_");
                OutputStream os = conn.getOutputStream();
                os.write(root.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
                conn.getResponseCode();*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
