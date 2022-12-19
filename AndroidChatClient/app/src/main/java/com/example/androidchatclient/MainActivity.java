package com.example.androidchatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static WebSocket ws;

    public static final String usernameExtra = "u";
    public static final String wordExtra = "w";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ws = new WebSocketFactory().createSocket("ws://10.0.2.2:8080/something", 1000);
        } catch (IOException e) {
            Log.e("Z: MainActivity", "WS Error");
        }
        ws.addListener(new myWebSocket());
        ws.connectAsynchronously();
    }

    // on pressing login button
    public void handleClick(View view) {
        Log.i("Z: MainActivity", "button pressed");
        Intent intent = new Intent(this, ChatViewActivity.class);
        EditText editUsername = (EditText) findViewById(R.id.editUsername);
        String usernameString = editUsername.getText().toString();
        intent.putExtra(usernameExtra, usernameString);
        EditText editRoomName = (EditText) findViewById(R.id.editRoomName);
        String roomNameString = editRoomName.getText().toString();
        intent.putExtra(wordExtra, roomNameString);
        startActivity(intent);
        ws.sendText("join " + usernameString + " " + roomNameString);
    }
}