package com.example.dave.chatclient;

import android.app.Application;
import android.content.ContentProvider;
import android.os.Debug;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

//Class used by the entire application to store global variables.
//Useful for objects such as sockets
public class GlobalResources extends Application {
    private final int port = 9000;
    private final String serverIP = "31.205.112.109";
    private String userName;
    private double longitude;
    private double latitude;
    private Socket socket;
    private BufferedWriter dataWriter;
    private BufferedReader dataReader;

    public void initSocket() {
        if (socket == null) {
            try {
                socket = new Socket(serverIP, port);
                dataWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                dataReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //Seems to be combining all data being sent into one send...

                dataWriter.write(userName + "\r\n");
                String msg = dataReader.readLine();
                Log.w(msg, msg);
                dataWriter.write(String.valueOf(longitude) + "\r\n");
                msg = dataReader.readLine();
                Log.w(msg, msg);
                dataWriter.write(String.valueOf(latitude) + "\r\n");
                msg = dataReader.readLine();
                Log.w(msg, msg);
                dataWriter.flush();
            }  catch (Exception e) {
                System.err.print("Error");
            }
        }
    }

    /*public double distanceBetween(double long1,double long2,double lat1,double lat2){
        double radius = 6371000.0;
    }*/

    public void setUserName(String name) {
        userName = name;
    }

    public void setLongitude(double longit) {longitude = longit;}

    public void setLatitude(double lat) {latitude = lat;}

    public Socket getSocket() {
        return socket;
    }

    public BufferedWriter getWriter() {
        return dataWriter;
    }

    public BufferedReader getReader() {
        return dataReader;
    }
}
