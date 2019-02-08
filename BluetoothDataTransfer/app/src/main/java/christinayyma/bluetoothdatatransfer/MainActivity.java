package christinayyma.bluetoothdatatransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.UUID;
import java.io.InputStream;
import java.io.OutputStream;
import android.os.Handler;
import 	android.os.Message;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.StringBuilder;


public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mDevice;
    ConnectThread mConnectThread;
    ConnectedThread mConnectedThread;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null){
            Log.d("myTag","Device does not Support Bluetooth");
        }

        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for (BluetoothDevice device : pairedDevices) {
                mDevice = device;
            }
            Log.d("myTag", mDevice.getAddress());
        }
        else {
            Log.d("myTag", "No connected devices");
        }
        mConnectThread = new ConnectThread(mDevice);
        mConnectThread.start();
    }

    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        //deleted static in the above line
        public ConnectThread(BluetoothDevice device){
            BluetoothSocket tmp = null;
            mmDevice = device;
            try{
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch(IOException e){
                Log.d("myTag", "Cannot create RfcommSocketToServiceRecord");
            }
            mmSocket = tmp;
            Log.d("myTag", "End of Constructor");
        }

        public void run(){
            Log.d("myTag", "Running Connect Thread!");
            mBluetoothAdapter.cancelDiscovery();
            try{
                mmSocket.connect();
            } catch (IOException connectException){
                try{
                    mmSocket.close();
                } catch (IOException closeException){
                    Log.d("myTag", "Cannot close socket1");
                }
                //return;
            }
            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        }

        public void cancel(){
            try{
                mmSocket.close();
            } catch (IOException e) {
                Log.d("myTag", "Cannot close socket2");
            }
        }
    }

    private static class mHandler extends Handler {
        private final WeakReference<MainActivity> MainActivityWeakReference;

        public mHandler(MainActivity mainActivityInstance){
            MainActivityWeakReference = new WeakReference<MainActivity>(mainActivityInstance);
        }

        @Override
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int)msg.arg1;
            int end = (int)msg.arg2;
            switch(msg.what) {
                case 1:
                    String writeMessage = new String(writeBuf);
                    writeMessage = writeMessage.substring(begin, end);
                    break;
            }
        }
    };


    private mHandler myHandler = new mHandler(this);

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d("myTag", "ConnectedThread constructor");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            TextView Flex1Readings = findViewById(R.id.flex1);
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.d("myTag", "Cannot create input stream or output stream");
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            Log.d("myTag", "Connected Thread Run");
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            while (true) {
                try {
                    //Log.d("myTag", "Before Read");
                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
                    final StringBuilder sb = new StringBuilder();
                    for(int i = begin; i < bytes; i++) {
                        sb.append(String.valueOf((char)buffer[i]));
                        if(buffer[i] == "#".getBytes()[0]) {

                            /*runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    TextView Flex1Readings = findViewById(R.id.flex1);
                                    Flex1Readings.setText(sb.toString());

                                }
                            });*/

                            Log.d("myTag", sb.toString());
                            sb.delete(0, sb.toString().length());
                            myHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if(i == bytes - 1) {
                                bytes = 0;
                                begin = 0;
                            }

                        }
                    }
                } catch (IOException e) {
                    Log.d("myTag", "Error recieving data");
                    //break;
                }
                catch (NullPointerException e){
                    Log.d("myTag", "Null Pointer Exception");
                }
            }

        }
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.d("myTag", "Cannot write bytes");
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.d("myTag", "Cannot close socket3");
            }
        }
    }
}
