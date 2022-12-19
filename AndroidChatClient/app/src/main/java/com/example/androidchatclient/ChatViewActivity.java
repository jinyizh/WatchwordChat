package com.example.androidchatclient;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.androidchatclient.MainActivity.ws;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatViewActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static ListView listView;
    public static ArrayList<String> messages = new ArrayList<>();
    public static ArrayAdapter<String> adapter;

    private String username;
    private String watchword;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        Intent intent = getIntent();
        username = intent.getStringExtra(MainActivity.usernameExtra);
        watchword = intent.getStringExtra(MainActivity.wordExtra);



        TextView textView = findViewById(R.id.welcome);
        textView.setText("Welcome, chat starts here!");

        listView = findViewById(R.id.listMessages);
        adapter = new ArrayAdapter<String>(ChatViewActivity.this, android.R.layout.simple_list_item_1, messages);
        listView.setAdapter(adapter);

        TextView messageTextView = findViewById(R.id.textInputEditText);
        messageTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    handleSend(view);
                    return true;
                }
                return false;
            }
        });
    }

    public void handleSend(View view) {
        Log.i("Z: ChatViewActivity", "button pressed");
        TextView textView = findViewById(R.id.textInputEditText);
        String msg = textView.getText().toString(); // message to be sent
        ws.sendText(username + " " + watchword + " " + msg);
        textView.setText(""); // clear input
    }

    public void handleLeave(View view) {
        Log.i("Z: ChatViewAcrivityy", "button pressed");
        ws.sendText("leave" + " " + username + " " + watchword);
        Intent leaveIntent = new Intent(this, MainActivity.class);
        startActivity(leaveIntent);
    }
}