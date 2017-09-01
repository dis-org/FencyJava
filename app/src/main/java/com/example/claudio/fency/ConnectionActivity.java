package com.example.claudio.fency;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devpaul.bluetoothutillib.SimpleBluetooth;
import com.devpaul.bluetoothutillib.dialogs.DeviceDialog;
import com.devpaul.bluetoothutillib.utils.BluetoothUtility;
import com.devpaul.bluetoothutillib.utils.SimpleBluetoothListener;

import java.nio.charset.Charset;
import java.util.Random;

public class ConnectionActivity extends FencyActivity implements View.OnClickListener {


    //mac ZenPad = "30:5A:3A:94:A3:E8";
    //mac Xperia = "44:D4:E0:28:83:DF";

    private static final int SERVER_WAIT_TIME = 2000; //milliseconds
    public static int REQUEST_ENABLE_BT = 1;
    private static final int SCAN_REQUEST = 119;
    private static final int CHOOSE_SERVER_REQUEST = 120;
    private static final int VISIBILITY_DURATION = 60; //seconds
    private static final String INTERRUPTION_STR = "STOP";

    private String myMacAddress, opponentMacAddress;
    private boolean nfcCompatible, isBtReady, isBtServer, isTryingStableConnection, isInStableConnection;
    private NfcAdapter nfcAdapter;
    private BluetoothAdapter BTAdapter;
    private SimpleBluetooth simpleBluetooth;
    private BtListener btListener;
    private View btnNfc, btnManualBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        cntFullScreen = findViewById(R.id.container_connection);
        btnManualBT = findViewById(R.id.btnManualBT);

        isBtReady = false;
        isBtServer = false;
        isTryingStableConnection = false;
        isInStableConnection = false;

        //START
        boolean res = checkCompatibility();

        if(!res){
            // quit duel-mode
        }
        else {
            btListener = new BtListener();

            btnManualBT.setOnClickListener(this);

            if (!BTAdapter.isEnabled()){
                //makeBtPowerOnRequest();
            }
            else {
                initialize();
            }
        }
    }

    public boolean checkCompatibility() {
        boolean res = true;

        // Initialize NFC adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // Phone does not support NFC
        if (nfcAdapter == null) {
            nfcCompatible = false;
            Toast.makeText(getApplicationContext(),"NFC not supported",Toast.LENGTH_LONG).show();
        }
        else nfcCompatible = true;

        // Initialize Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BTAdapter = bluetoothManager.getAdapter();

        // Phone does not support Bluetooth
        if (BTAdapter == null) {
            res = false;
            new AlertDialog.Builder(getApplicationContext())
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return res;
    }

    private void makeBtPowerOnRequest(){
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        //setDeviceDiscoverable();

        // Set power-on callback
        final BroadcastReceiver powerOnReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    switch(state) {
                        case BluetoothAdapter.STATE_OFF:
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            break;
                        case BluetoothAdapter.STATE_ON:
                            initialize();
                            unregisterReceiver(this);
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Toast.makeText(getApplicationContext(),"BT opening...",Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(powerOnReceiver, filter);
    }

    private void initialize(){
        simpleBluetooth = new SimpleBluetooth(this, this);
        simpleBluetooth.initializeSimpleBluetooth();
        simpleBluetooth.setSimpleBluetoothListener(btListener);

        myMacAddress = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), "bluetooth_address");

        isBtReady = true;

        if(nfcCompatible){
            if (!nfcAdapter.isEnabled()){
                Toast.makeText(getApplicationContext(),"Please enable NFC",Toast.LENGTH_LONG).show();
                startActivity( new Intent(Settings.ACTION_NFC_SETTINGS));

            }
            else if (!nfcAdapter.isNdefPushEnabled()){
                Toast.makeText(getApplicationContext(),"Please enable Android Beam",Toast.LENGTH_LONG).show();
                startActivity( new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
            }
            else{
                //JSONObject jsonObj = (JSONObject)JSONObject.wrap(myMacAddress);
                String payload = myMacAddress;
                String mimeType = "application/com.example.claudio.fency";
                byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));

                nfcAdapter.setNdefPushMessage( new NdefMessage(
                        new NdefRecord[] {
                                /*createTextRecord(null, myMacAddress)*/
                                // Create the NFC payload.
                                new NdefRecord(
                                        NdefRecord.TNF_MIME_MEDIA,
                                        mimeBytes,
                                        new byte[0],
                                        payload.getBytes()
                                ),
                                        // Add the AAR (Android Application Record)
                                        NdefRecord.createApplicationRecord("com.example.claudio.fency")
                        }), this);

                Toast.makeText(getApplicationContext(),"NFC message ready", Toast.LENGTH_SHORT).show();
            }

        }
        // Android 6  BT permission request
        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        //setDeviceDiscoverable();
    }

    private void tryStableConnection() {
        isTryingStableConnection = true;
        if (!isBtServer) {
            //need disconnection?
            //TODO
            //re-connection as client
            Toast.makeText(getApplicationContext(),"connection attempt in "+SERVER_WAIT_TIME/1000+" seconds", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Connect client to server only after delay
                    simpleBluetooth.connectToBluetoothServer(opponentMacAddress);
                    //simpleBluetooth.setSimpleBluetoothListener(new BtListener());
                    backToDuel();

                }
            }, SERVER_WAIT_TIME);
        }
    }

    private void backToDuel(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                simpleBluetooth.sendData(INTERRUPTION_STR); //TODO
                onStop();
            }
        }, 1000);
    }

    private void scanToConnect() {

        simpleBluetooth.scan(SCAN_REQUEST);
        setDeviceDiscoverable();

    }

    private void setDeviceDiscoverable(){
        if(BTAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            simpleBluetooth.makeDiscoverable(VISIBILITY_DURATION);
        }
    }

    private String getOpponentMacAddress(){
        return opponentMacAddress;
    }

    private boolean choseServer(){
        boolean res = true;
        if(opponentMacAddress == null){
            res=false;
        }
        else {
            String myMacString = new String(myMacAddress);
            String opMacString = new String(opponentMacAddress);
            // convert mac address to number (long)
            long myMac = Long.parseLong(myMacString.replaceAll(":", ""), 16);
            long opMac = Long.parseLong(opMacString.replaceAll(":", ""), 16);
            //Toast.makeText(getApplicationContext(),"m: "+myMac+"\no: "+opMac, Toast.LENGTH_SHORT).show();

            if(myMac < opMac){
                connectAsServer();
            }
            else
                ((TextView)findViewById(R.id.tvRoleBT)).setText("client");
        }
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SCAN_REQUEST || requestCode == CHOOSE_SERVER_REQUEST) {

            if(resultCode == RESULT_OK) {

                opponentMacAddress = data.getStringExtra(DeviceDialog.DEVICE_DIALOG_DEVICE_ADDRESS_EXTRA);
                if(requestCode == SCAN_REQUEST) {
                    simpleBluetooth.connectToBluetoothDevice(opponentMacAddress);
                } else {
                    simpleBluetooth.connectToBluetoothServer(opponentMacAddress);
                }

            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btnManualBT:
                if(!isBtReady)
                    makeBtPowerOnRequest();
                else {
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                    btnManualBT.setBackgroundColor(color);
                    //connectAsServer();
                    scanToConnect();
                }
                break;
        }
    }

    private void connectAsClient() {
        //temporary TODO
        isBtServer = false;
        isTryingStableConnection = true;
        simpleBluetooth.connectToBluetoothServer(getOpponentMacAddress());
        //simpleBluetooth.setSimpleBluetoothListener(new BtListener());
        ((TextView)findViewById(R.id.tvRoleBT)).setText("client");

    }

    private void connectAsServer() {
        isBtServer = true;
        isTryingStableConnection = true;
        simpleBluetooth.createBluetoothServerConnection();
        //simpleBluetooth.setSimpleBluetoothListener(new BtListener());
        ((TextView)findViewById(R.id.tvRoleBT)).setText("SERVER");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d("MAIN", "OnResume Called");
        //this check needs to be here to ensure that the simple bluetooth is not reset.
        //an issue was occuring when a client would connect to a server. When a client
        // connects they have to select a device, that is another activity, so after they
        //select a device, this gets called again and the reference to the original simpleBluetooth
        //object on the client side gets lost. Thus when send is called, nothing happens because it's
        //a different object.
        if(simpleBluetooth == null) {
            simpleBluetooth = new SimpleBluetooth(this,this);
        }
        simpleBluetooth.initializeSimpleBluetooth();
        simpleBluetooth.setInputStreamType(BluetoothUtility.InputStreamType.BUFFERED);
    }
    @Override
    public void onStop(){

        if(simpleBluetooth!=null) {
            //  simpleBluetooth.cancelScan();
           // simpleBluetooth.endSimpleBluetooth();
            String role = "C";
            if(isBtServer) role = "S";

            Intent data = new Intent();
            //---set the data to pass back---
            //data.setData(Uri.parse(text));
            data.putExtra("key_opponentMacAddress", opponentMacAddress);
            data.putExtra("key_connectionRole", role);
            setResult(RESULT_OK, data);
            //Toast.makeText(getApplicationContext(),"pacchetto ok", Toast.LENGTH_SHORT).show();
        }
        super.onStop();
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(simpleBluetooth!=null) {
            simpleBluetooth.endSimpleBluetooth();
            simpleBluetooth = null;
            /*
            Intent data = new Intent();
            String text = opponentMacAddress;
            //---set the data to pass back---
            data.setData(Uri.parse(text));
            setResult(RESULT_OK, data);
            */
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //onStop();
    }

    public class BtListener extends SimpleBluetoothListener {

        @Override
        public void onBluetoothDataReceived(byte[] bytes, String data) {
            //read the data coming in.
            if(data.equals(INTERRUPTION_STR)){
                //end-of-activity message received
                onStop();
            }
            else {
                //Toast.makeText(getApplicationContext(), "Message received:\n" + data, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),"IN: " + bytes.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onDeviceConnected(BluetoothDevice device) {
            super.onDeviceConnected(device);
            opponentMacAddress = device.getAddress();
            //opponentMacAddress = getOpponentMacAddress();

            if (!isTryingStableConnection) {
                Toast.makeText(getApplicationContext(), "Connected to: " + opponentMacAddress, Toast.LENGTH_SHORT).show();

                // Ask the users to pair the devices if not paired
                if(device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    device.createBond();
                    Toast.makeText(getApplicationContext(), "Please pair the devices", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                }


                boolean ok = choseServer();
                if (!ok) {
                    Toast.makeText(getApplicationContext(), "CONNECTION ERROR.\nPlease try to connect again", Toast.LENGTH_LONG).show();
                } else {
                    tryStableConnection();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Stable",Toast.LENGTH_SHORT).show();
                String msg;
                if (isBtServer) msg = "SERVER: Ciao, sono il Server";
                else msg = "CLIENT: Ciao, sono un Client";

                simpleBluetooth.sendData(msg + "\nmy MAC: "+myMacAddress);
                //simpleBluetooth.getBluetoothUtility().sendData(msg);
            }
        }

        @Override
        public void onDeviceDisconnected(BluetoothDevice device) {
            //isInStableConnection = false;
            //isTryingStableConnection = false;
            Toast.makeText(getApplicationContext(), "Device disconnected\n MAC: "+device.getAddress(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDiscoveryStarted() {
            super.onDiscoveryStarted();
            Toast.makeText(getApplicationContext(),"scanning...", Toast.LENGTH_SHORT).show();
            setDeviceDiscoverable();
        }

        @Override
        public void onDiscoveryFinished() {
            super.onDiscoveryFinished();
            Toast.makeText(getApplicationContext(),"end of scan.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDevicePaired(BluetoothDevice device) {
            super.onDevicePaired(device);
            Toast.makeText(getApplicationContext(),"Paired to: " + device.getAddress(), Toast.LENGTH_SHORT).show();
        }

    }
}
