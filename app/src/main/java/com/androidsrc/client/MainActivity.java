package com.androidsrc.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements JoystickView.JoystickListener {

    private Socket socket;
    private static final int SERVERPORT = 8888;
    private static final String SERVER_IP = "192.168.1.96";
    DisplayMetrics screen;
    float screenWidth, screenHeight;
    JoystickView myJoystick;
    Button connect, disconnect;
    Boolean connected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screen = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screen);
        screenWidth = screen.widthPixels;
        screenHeight = screen.heightPixels;

        myJoystick = (JoystickView) findViewById(R.id.testJoystick);
        myJoystick.getLayoutParams().height = (int) (0.7f * screenWidth);
        myJoystick.getLayoutParams().width = (int) (screenWidth);

        RelativeLayout.LayoutParams marginParams = (RelativeLayout.LayoutParams) myJoystick.getLayoutParams();
        marginParams.setMargins(0, 0, 0, (int) (0.1f * screenWidth));
        myJoystick.setLayoutParams(marginParams);

        connect = (Button) findViewById(R.id.button_connect);
        connect.getLayoutParams().width = (int) (0.4f * screenWidth);
        connect.setOnClickListener(connectionManager);

        disconnect = (Button) findViewById(R.id.button_disconnect);
        disconnect.getLayoutParams().width = (int) (0.4f * screenWidth);
        disconnect.setOnClickListener(connectionManager);
        disconnect.setEnabled(false);
    }

    View.OnClickListener connectionManager = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == connect.getId()) {
                socket = new Socket();
                Thread clientThread = new Thread(new ClientThread());
                connect.setEnabled(false);
                Toast.makeText(MainActivity.this, "Trying to connect to" + SERVER_IP + "...", Toast.LENGTH_SHORT).show();
                clientThread.start();
                Thread.State state = clientThread.getState();
                while (state != Thread.State.TERMINATED) {
                    state = clientThread.getState();
                }
                if (connected) {
                    disconnect.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                } else {
                    connect.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Failed to connect to " + SERVER_IP, Toast.LENGTH_SHORT).show();
                }
            }


            if (v.getId() == disconnect.getId()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connected = false;
                connect.setEnabled(true);
                disconnect.setEnabled(false);

            }
        }
    };


    @Override
    public void onJoystickMoved(float Puissance, float Angle, int Sens, int id) {
        String P = "" + Puissance;
        if (P.length() >= 4) {
            P = P.substring(0, 4);
        } else {
            P = P + "0";
        }
        String A = "" + Angle;
        if (A.length() >= 4) {
            A = A.substring(0, 4);
        } else {
            A = A + "0";
        }
        String S = "" + Sens;
        if (S.length() == 1) {
            S = S + ".";
        }
        String msg = "<p" + P + "\n" + "<a" + A + "\n" + "<s" + S + "..\n";
        new Thread(new MessageThread(socket, msg)).start();
    }


    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                // Creates an unconnected socket
                int timeout = 5000;   // 5000 millis = 5 seconds
                SocketAddress sockaddr = new InetSocketAddress(serverAddr, SERVERPORT);
                // Connects this socket to the server with a specified timeout value
                // If timeout occurs, SocketTimeoutException is thrown
                socket.connect(sockaddr, timeout);

                //socket = new Socket(serverAddr, SERVERPORT);
                if (socket.isConnected()) {
                    connected = true;
                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}