package com.example.androidchatclient;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class myWebSocket extends WebSocketAdapter {
    String message = "test msg";

    @Override
    public void onConnected(WebSocket webSocket, Map<String, List<String>> headers) throws Exception {
        Log.i("Z: myWebSocket", "WS Connected");
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onTextMessage(WebSocket webSocket, String payload) throws Exception {
        Log.i("Z: myWebSocket", "WS Message");

        JSONObject json = new JSONObject(payload);
        String type = (String) json.get("type");
        String user = (String) json.get("user");
        String word = (String) json.get("watchword");

        String timeStamp = " ";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            timeStamp = new SimpleDateFormat("HH:mm").format(new java.util.Date());
        }
        switch (type) {
            case "join":
                message = timeStamp + "\n" + user + " says " + word + "!";
                Log.i("Z: myWebSocket", message);
                break;
            case "message":
                String realMsg = (String) json.get("message");
                message = timeStamp + "\n" + user + ": " + realMsg;
                Log.i("Z: myWebSocket", message);
                break;
            case "leave":
                message = timeStamp + "\n" + user + " has left.";
                Log.i("Z: myWebSocket", message);
                break;
        }

        ChatViewActivity.messages.add(message);
        ChatViewActivity.listView.post(new Runnable() {
            @Override
            public void run() {
                ChatViewActivity.adapter.notifyDataSetChanged();
                ChatViewActivity.listView.smoothScrollToPosition(ChatViewActivity.adapter.getCount());
            }
        });
    }
}
