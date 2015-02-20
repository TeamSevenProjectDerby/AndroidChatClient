package com.example.dave.chatclient;

import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;

//Activity which displays the userList. child of chatroom
public class UserList extends ActionBarActivity {

    GlobalResources global;
    Socket socket;
    BufferedWriter dataWriter;
    BufferedReader dataReader;
    ArrayAdapter<String> listAdapter;
    ArrayList<String> arrayList;
    ListView nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        global = (GlobalResources)getApplicationContext();
        global.initSocket();
        socket = global.getSocket();
        dataWriter = global.getWriter();
        dataReader = global.getReader();
        initializeControls();
        updateUserList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
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
    public void initializeControls() {
        nameList = (ListView)findViewById(R.id.userList);
        arrayList = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        nameList.setAdapter(listAdapter);
    }

    //Refreshes the user list ListView by receiving server messages
    public void updateUserList() {
        try {
            //Request code 1 is sent to the server to request server list
            dataWriter.write("1");
            dataWriter.flush();
            String listName;
            char messageCode;

            /*A message code is the first character of the message. If the server
              sends '0' at the start of the message, it means there are still more
              names to be sent. This keeps the message receive loop going*/
            do {
                listName = dataReader.readLine();
                messageCode = listName.charAt(0);
                //Adds name to list view if message starts with a 0
                if (messageCode == '0') {
                    listAdapter.add(listName.substring(1));
                }
            } while (messageCode == '0');
        } catch (Exception e) {
        }
    }
}
