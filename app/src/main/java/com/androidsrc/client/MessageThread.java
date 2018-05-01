package com.androidsrc.client;


import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

public class MessageThread implements Runnable {

    private Socket _socket;
    private String _msg;
    //private float _puissance;
    //private float _angle;
    //private int _sens;


    public MessageThread(Socket socket, String msg) {
        _socket = socket;
        _msg = msg;
        //_angle = angle;
        //_puissance = puissance;
        //_sens = sens;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(_socket.getOutputStream())),
                    true);
            out.println(_msg);
            Log.d("Main Method", "Message sent.\n");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}