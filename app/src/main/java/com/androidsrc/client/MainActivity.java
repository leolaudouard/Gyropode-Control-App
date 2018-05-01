package com.androidsrc.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements  JoystickView.JoystickListener{

    private Socket socket;

    private static final int SERVERPORT = 8888;
    private static final String SERVER_IP = "192.168.1.96";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new ClientThread()).start();

    }

    @Override
    public void onJoystickMoved(float Puissance, float Angle, int Sens, int id)
    {
        String P = ""+Puissance;
        if (P.length() >= 4) {
            P = P.substring(0, 4);
        } else {
            P = P + "0";
        }
        String A = ""+ Angle;
        if (A.length() >= 4) {
            A = A.substring(0, 4);
        } else {
            A = A + "0";
        }
        String S = "" + Sens;
        if (S.length() == 1){
            S = S + ".";
        }
        String msg = "<p" + P + "\n" + "<a" + A + "\n" + "<s" + S + "..\n";
        Log.d("Main Method", msg);
        new Thread(new MessageThread(socket, msg)).start();
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
                out.println("ohhhh");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}