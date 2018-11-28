package com.techexchange.mobileapps.assignment3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private final String TAG = "MainActivity";
    private GestureDetectorCompat mDetector;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        mDetector = new GestureDetectorCompat(this, this);

        setContentView(R.layout.activity_main);

        // Allow network operations on main thread for simplicity.
        StrictMode.ThreadPolicy policy
                = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initializeComponents();
        executeListener();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
//        int tappedOrSwiped = (int) readBuffer[0];
//        float XpropOrDirection = readBuffer[1];
//        float Yprop = 0;
//        if (readBuffer.length > 2) Yprop = readBuffer[2];

        float[] buffer = new float[2];
        buffer[0] = SWIPED;

        if (velocityX >= 0) {
            // 1st quadrant.
            if (velocityY <= 0) {
                if (Math.abs(velocityX) >= Math.abs(velocityY)) {
                    if (isHost) gameView.onFlingTank1(DIRECTION_RIGHT);
                    else gameView.onFlingTank2(DIRECTION_RIGHT);
                    buffer[1] = DIRECTION_RIGHT;
                    sendReceiveThread.write(buffer);
                }
                else {
                    if (isHost) gameView.onFlingTank1(DIRECTION_TOP);
                    else gameView.onFlingTank2(DIRECTION_TOP);
                    buffer[1] = DIRECTION_TOP;
                    sendReceiveThread.write(buffer);
                }
            } else {
                // 4th quadrant.
                if (Math.abs(velocityX) >= Math.abs(velocityY)) {
                    if (isHost) gameView.onFlingTank1(DIRECTION_RIGHT);
                    else gameView.onFlingTank2(DIRECTION_RIGHT);
                    buffer[1] = DIRECTION_RIGHT;
                    sendReceiveThread.write(buffer);
                }
                else {
                    if (isHost) gameView.onFlingTank1(DIRECTION_DOWN);
                    else gameView.onFlingTank2(DIRECTION_DOWN);
                    buffer[1] = DIRECTION_DOWN;
                    sendReceiveThread.write(buffer);
                }
            }
        } else {
            // 2nd quadrant.
            if (velocityY <= 0) {
                if (Math.abs(velocityX) >= Math.abs(velocityY)) {
                    if (isHost) gameView.onFlingTank1(DIRECTION_LEFT);
                    else gameView.onFlingTank2(DIRECTION_LEFT);
                    buffer[1] = DIRECTION_LEFT;
                    sendReceiveThread.write(buffer);
                }
                else {
                    if (isHost) gameView.onFlingTank1(DIRECTION_TOP);
                    else gameView.onFlingTank2(DIRECTION_TOP);
                    buffer[1] = DIRECTION_TOP;
                    sendReceiveThread.write(buffer);
                }
            } else {
                // 3rd quadrant.
                if (Math.abs(velocityX) >= Math.abs(velocityY)) {
                    if (isHost) gameView.onFlingTank1(DIRECTION_LEFT);
                    else gameView.onFlingTank2(DIRECTION_LEFT);
                    buffer[1] = DIRECTION_LEFT;
                    sendReceiveThread.write(buffer);
                }
                else {
                    if (isHost) gameView.onFlingTank1(DIRECTION_DOWN);
                    else gameView.onFlingTank2(DIRECTION_DOWN);
                    buffer[1] = DIRECTION_DOWN;
                    sendReceiveThread.write(buffer);
                }
            }
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        float x = event.getX();
        float y = event.getY() - 280;

        float[] buffer = new float[3];
        buffer[0] = TAPPED;
        buffer[1] = x / screenWidth;
        buffer[2] = y / screenHeight;
        sendReceiveThread.write(buffer);

        if (isHost) gameView.onSingleTapTank1(x, y);
        else gameView.onSingleTapTank2(x, y);
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        return true;
    }

    // Wifi Direct Logic.

    Button discoverButton;
    ListView listView;
    TextView connectionStatusTextView;

    WifiManager wifiManager;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel channel;

    BroadcastReceiver receiver;
    IntentFilter intentFilter;

    List<WifiP2pDevice> peers = new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    static final int MESSAGE_READ = 1;

    ServerThread serverThread;
    ClientThread clientThread;
    SendReceiveThread sendReceiveThread;

    public boolean isHost = false;

    static final int TAPPED = 1;
    static final int SWIPED = 2;
    static final int DIRECTION_RIGHT = 1;
    static final int DIRECTION_DOWN = 2;
    static final int DIRECTION_LEFT = 3;
    static final int DIRECTION_TOP = 4;

    private float screenHeight;
    private float screenWidth;

    public class ServerThread extends Thread {
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                sendReceiveThread = new SendReceiveThread(socket);
                sendReceiveThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public class ClientThread extends Thread {
        Socket socket;
        String hostAddress;

        ClientThread(InetAddress hostAddress) {
            this.hostAddress = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAddress, 8888), 500);
                sendReceiveThread = new SendReceiveThread(socket);
                sendReceiveThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class SendReceiveThread extends Thread {
        private Socket socket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;

        public SendReceiveThread(Socket socket) {
            this.socket = socket;
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            while (socket != null) {
                // Listen for message
                try {
                    int len = inputStream.readInt();
                    float[] floats = new float[len];
                    for(int i = 0; i < len; i++) {
                        floats[i] = inputStream.readFloat();
                    }

                    if (len > 0) {
                        // We received something
                        handler.obtainMessage(MESSAGE_READ, floats).sendToTarget();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(float[] floats) {
            try {
                int len = floats.length;
                outputStream.writeInt(len);
                for (float i : floats) {
                    outputStream.writeFloat(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case MESSAGE_READ:
                float[] readBuffer = (float[]) msg.obj;
                int tappedOrSwiped = (int) readBuffer[0];
                float XpropOrDirection = readBuffer[1];
                float Yprop = 0;
                if (readBuffer.length > 2) Yprop = readBuffer[2];
                // Opposite since host will only receive message about client's actions and vice
                // versa.
                if (isHost) {
                    switch(tappedOrSwiped) {
                        case TAPPED:
                            // X and Y altered in proportion ot screenHeight and screenWidth.
                            float X = XpropOrDirection * screenWidth;
                            float Y = Yprop * screenHeight;
                            gameView.onSingleTapTank2(X, Y);
                            break;
                        case SWIPED:
                            int direction = (int) XpropOrDirection;
                            gameView.onFlingTank2(direction);
                            break;
                    }
                } else {
                    switch(tappedOrSwiped) {
                        case TAPPED:
                            // X and Y altered in proportion ot screenHeight and screenWidth.
                            float X = XpropOrDirection * screenWidth;
                            float Y = Yprop * screenHeight;
                            gameView.onSingleTapTank1(X, Y);
                            break;
                        case SWIPED:
                            int direction = (int) XpropOrDirection;
                            gameView.onFlingTank1(direction);
                            break;
                    }
                }
                break;
        }
        return true;
    });

    private void executeListener() {
        discoverButton.setOnClickListener(v -> {
            wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    // Successfully started discovering
                    connectionStatusTextView.setText("Searching...");
                }

                @Override
                public void onFailure(int reason) {
                    // Failed to start discovering
                    connectionStatusTextView.setText("Search failed");
                }
            });
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            final WifiP2pDevice device = deviceArray[position];
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;

            wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int reason) {
                    Toast.makeText(getApplicationContext(),
                            "Connection failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register the broadcast receiver
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the broadcast receiver
        unregisterReceiver(receiver);
    }

    WifiP2pManager.PeerListListener peerListListener = peerList -> {
        if (!peerList.getDeviceList().equals(peers)) {
            peers.clear();
            peers.addAll(peerList.getDeviceList());

            deviceNameArray = new String[peers.size()];
            deviceArray = new WifiP2pDevice[peers.size()];

            int index = 0;
            for (WifiP2pDevice device : peers) {
                deviceNameArray[index] = device.deviceName;
                deviceArray[index] = device;
                index++;
            }

            ArrayAdapter<String> arrayAdapter
                    = new ArrayAdapter<>(getApplicationContext(),
                    R.layout.device_list_item, deviceNameArray);

            listView.setAdapter(arrayAdapter);

            if (peers.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        "No Enemies Found!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner) {
                // The host.
                isHost = true;
                serverThread = new ServerThread();
                setContentView(gameView);

                serverThread.start();
            } else if (info.groupFormed) {
                // The client.
                clientThread = new ClientThread(groupOwnerAddress);
                setContentView(gameView);

                clientThread.start();
            }
        }
    };

    private void initializeComponents() {
        discoverButton = findViewById(R.id.discover);
        listView = findViewById(R.id.peerListView);
        connectionStatusTextView = findViewById(R.id.connectionStatus);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            // Notify user that they are currently offline.
        }

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(wifiP2pManager, channel, this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }
}
