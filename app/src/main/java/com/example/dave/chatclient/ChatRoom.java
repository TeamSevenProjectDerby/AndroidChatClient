package com.example.dave.chatclient;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class ChatRoom extends ActionBarActivity {

    GlobalResources global;
    final int port = 9000;
    final String serverIP = "31.205.112.109";
    boolean endThread;
    Socket socket;
    ListView messageBox;
    EditText messageField;
    BufferedWriter dataWriter;
    BufferedReader dataReader;
    String incomingMessage;
    boolean msgAdded;
    ArrayAdapter<String> listAdapter;
    ArrayList<String> arrayList;

    /*Creates a new thread which constantly loops to receive any messages sent by the server
      until the loop variable is turned false*/
    Thread receiveDataThread = new Thread() {
        public void run() {
            while (!endThread) {
                try {
                    incomingMessage = dataReader.readLine();
                    msgAdded = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!msgAdded) {
                                listAdapter.add(incomingMessage);
                                messageBox.setSelection(listAdapter.getCount() - 1);
                                msgAdded = true;
                            }
                        }
                    });
                } catch (IOException e) {
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initializeControls();
        global = (GlobalResources)getApplicationContext();
        global.initSocket();
        socket = global.getSocket();
        dataWriter = global.getWriter();
        dataReader = global.getReader();
        endThread = false;
        receiveDataThread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    //Links all activity controls to variables
    public void initializeControls(){
        messageBox = (ListView)findViewById(R.id.messageBox);
        messageField = (EditText)findViewById(R.id.editText);
        arrayList = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        messageBox.setAdapter(listAdapter);
    }

    public void sendMessage(View view) {
        /*Text in the message field control is converted into  a string and sent
          to the server using the socket output stream*/
        try {
            String message = messageField.getText().toString();
            messageField.setText("");
            dataWriter.write("0" + message);
            dataWriter.flush();
        } catch (IOException e) {
            System.err.print("Error");
        }
    }

    //Pauses the receive message thread and launches the user list activity
    public void openUserList(MenuItem menuItem) {
        endThread = true;
        Intent intent = new Intent(this, UserList.class);
        startActivity(intent);
    }

}