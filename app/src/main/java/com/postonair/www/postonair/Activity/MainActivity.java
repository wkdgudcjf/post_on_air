package com.postonair.www.postonair.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.postonair.www.postonair.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity implements View.OnClickListener {
    private final int REQUEST_BLUETOOTH_ENABLE = 100;
    private  AlertDialog.Builder alert;
    private ProgressDialog pdialog;
    private ConnectedTask mConnectedTask = null;
    static BluetoothAdapter mBluetoothAdapter;
    private String mConnectedDeviceName = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    static boolean isConnectionError = false;
    private static final String TAG = "BluetoothClient";
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton ImageButton_main_coupon = (ImageButton) findViewById(R.id.ImageButton_main_coupon);
        ImageButton_main_coupon.setOnClickListener(this);

        ImageButton ImageButton_main_accountconnect = (ImageButton) findViewById(R.id.ImageButton_main_accountconnect);
        ImageButton_main_accountconnect.setOnClickListener(this);

        ImageButton ImageButton_main_stamp = (ImageButton) findViewById(R.id.ImageButton_main_stamp);
        ImageButton_main_stamp.setOnClickListener(this);

        ImageButton ImageButton_main_receive = (ImageButton) findViewById(R.id.ImageButton_main_receive);
        ImageButton_main_receive.setOnClickListener(this);

        Button Button_main_withdraw = (Button) findViewById(R.id.Button_main_withdraw);
        Button_main_withdraw.setOnClickListener(this);
        Button Button_main_send = (Button) findViewById(R.id.Button_main_send);
        Button_main_send.setOnClickListener(this);
        Button Button_main_filup = (Button) findViewById(R.id.Button_main_filup);
        Button_main_filup.setOnClickListener(this);

        //
        //bluetooth
        //
        mConversationArrayAdapter = new ArrayAdapter<>( this,
                android.R.layout.simple_list_item_1 );

        Log.d( TAG, "Initalizing Bluetooth adapter...");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showErrorDialog("This device is not implement Bluetooth.");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
        }
        else {
            Log.d(TAG, "Initialisation successful.");
            showPairedDevicesListDialog();
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ImageButton_main_coupon:
                intent = new Intent(this, CouponActivity.class);
                startActivity(intent);
                break;
            case R.id.ImageButton_main_accountconnect:
                intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.ImageButton_main_stamp:
                intent = new Intent(this, StampActivity.class);
                startActivity(intent);
                break;
            case  R.id.Button_main_withdraw:
                intent = new Intent(this, WithdrawActivity.class);
                startActivity(intent);
                break;
            case  R.id.Button_main_send:
                intent = new Intent(this, SendActivity.class);
                startActivity(intent);
                break;
            case  R.id.Button_main_filup:
                intent = new Intent(this, FilupActivity.class);
                startActivity(intent);
                break;
            case R.id.ImageButton_main_receive:
                alert = new AlertDialog.Builder(this);

                alert.setTitle("받기 설정");
                alert.setMessage("받을 금액을 설정하세요.");

                final EditText name = new EditText(this);
                alert.setView(name);

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        showProgressDialog();
                        String username = name.getText().toString();
                        Handler mHandler = new Handler();
                        //bluetoothsend
                        sendMessage("receive\n");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                pdialog.dismiss();
                                sendMessage("stop\n");
                            }
                        }, 7000);
                    }
                });

                alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
                break;
            /*case R.id.ImageButton_main_donation:
                intent = new Intent(this, IdPwFindActivity.class);
                startActivity(intent);
                break;
            case R.id.TextView_main_charge:
                intent = new Intent(this, IdPwFindActivity.class);
                startActivity(intent);
                break;
            default:
                break;*/
        }
    }
    private void showProgressDialog() {
        pdialog = new ProgressDialog(this);
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("받기 모드 실행중");
        pdialog.show();
    }
    //
    //bluetooth
    //
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if ( mConnectedTask != null ) {

            mConnectedTask.cancel(true);
        }
    }


    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        private BluetoothSocket mBluetoothSocket = null;
        private BluetoothDevice mBluetoothDevice = null;

        ConnectTask(BluetoothDevice bluetoothDevice) {
            mBluetoothDevice = bluetoothDevice;
            mConnectedDeviceName = bluetoothDevice.getName();

            //SPP
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            try {
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                Log.d( TAG, "create socket for "+mConnectedDeviceName);

            } catch (IOException e) {
                Log.e( TAG, "socket create failed " + e.getMessage());
            }
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mBluetoothSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mBluetoothSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " +
                            " socket during connection failure", e2);
                }

                return false;
            }

            return true;
        }


        @Override
        protected void onPostExecute(Boolean isSucess) {

            if ( isSucess ) {
                connected(mBluetoothSocket);
            }
            else{

                isConnectionError = true;
                Log.d( TAG,  "Unable to connect device");
                showErrorDialog("Unable to connect device");
            }
        }
    }


    public void connected( BluetoothSocket socket ) {
        mConnectedTask = new ConnectedTask(socket);
        mConnectedTask.execute();
    }



    private class ConnectedTask extends AsyncTask<Void, String, Boolean> {

        private InputStream mInputStream = null;
        private OutputStream mOutputStream = null;
        private BluetoothSocket mBluetoothSocket = null;

        ConnectedTask(BluetoothSocket socket){

            mBluetoothSocket = socket;
            try {
                mInputStream = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "socket not created", e );
            }

            Log.d( TAG, "connected to "+mConnectedDeviceName);
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            byte [] readBuffer = new byte[1024];
            int readBufferPosition = 0;


            while (true) {

                if ( isCancelled() ) return false;

                try {

                    int bytesAvailable = mInputStream.available();

                    if(bytesAvailable > 0) {

                        byte[] packetBytes = new byte[bytesAvailable];

                        mInputStream.read(packetBytes);

                        for(int i=0;i<bytesAvailable;i++) {

                            byte b = packetBytes[i];
                            if(b == '\n')
                            {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                        encodedBytes.length);
                                String recvMessage = new String(encodedBytes, "UTF-8");
                                readBufferPosition = 0;

                                Log.d(TAG, "recv message: " + recvMessage);
                                publishProgress(recvMessage);
                            }
                            else
                            {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (IOException e) {

                    Log.e(TAG, "disconnected", e);
                    return false;
                }
            }

        }

        @Override
        protected void onProgressUpdate(String... recvMessage) {

            //tvTest.setText(recvMessage);
            //mConversationArrayAdapter.insert(mConnectedDeviceName + ": " + recvMessage[0], 0);
        }

        @Override
        protected void onPostExecute(Boolean isSucess) {
            super.onPostExecute(isSucess);

            if ( !isSucess ) {


                closeSocket();
                Log.d(TAG, "Device connection was lost");
                isConnectionError = true;
                showErrorDialog("Device connection was lost");
            }
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);

            closeSocket();
        }

        void closeSocket(){

            try {

                mBluetoothSocket.close();
                Log.d(TAG, "close socket()");

            } catch (IOException e2) {

                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }
        }

        void write(String msg){

            msg += "\n";

            try {
                mOutputStream.write(msg.getBytes());
                mOutputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Exception during send", e );
            }

            //mInputEditText.setText(" ");
        }
    }


    public void showPairedDevicesListDialog()
    {
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        final BluetoothDevice[] pairedDevices = devices.toArray(new BluetoothDevice[0]);

        if ( pairedDevices.length == 0 ){
            showQuitDialog( "No devices have been paired.\n"
                    +"You must pair it with another device.");
            return;
        }

        String[] items;
        items = new String[pairedDevices.length];
        for (int i=0;i<pairedDevices.length;i++) {
            items[i] = pairedDevices[i].getName();
/*            if(items[i].contains("raspberrypi")){
                ConnectTask task = new ConnectTask(pairedDevices[i]);
                task.execute();
            }*/
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select device");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                ConnectTask task = new ConnectTask(pairedDevices[which]);
                task.execute();
            }
        });
        builder.create().show();
    }



    public void showErrorDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage("우표의 전원이 꺼져있습니다.");
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if ( isConnectionError  ) {
                    isConnectionError = false;
                    return ;
                }
            }
        });
        builder.create().show();
    }


    public void showQuitDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    void sendMessage(String msg){

        if ( mConnectedTask != null ) {
            mConnectedTask.write(msg);
            Log.d(TAG, "send message: " + msg);
            mConversationArrayAdapter.insert("Me:  " + msg, 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_BLUETOOTH_ENABLE){
            if (resultCode == RESULT_OK){
                //BlueTooth is now Enabled
                showPairedDevicesListDialog();
            }
            if(resultCode == RESULT_CANCELED){
                showQuitDialog( "You need to enable bluetooth");
            }
        }
    }
}
