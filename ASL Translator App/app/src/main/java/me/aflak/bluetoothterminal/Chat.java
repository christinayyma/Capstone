package me.aflak.bluetoothterminal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.speech.tts.Voice;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import me.aflak.bluetooth.Bluetooth;

public class Chat extends AppCompatActivity implements Bluetooth.CommunicationCallback {
    private String lGlove, rGlove;
    private Bluetooth b;
    private Bluetooth b2;
    private EditText message;
    private Button send;
    private TextView text;
    private ScrollView scrollView;
    private boolean registered=false;
    TextToSpeech t1;
    boolean turn = false;
    String first;
    String prev_letter = "L";
    String prev_prev_letter = "L";
    String prev_prev_prev_letter = "L";
    boolean button_pressed = false;
    int lGlovePos = 0;
    int rGlovePos = 0;


    private Socket msocket;
    {
        try {
            msocket = IO.socket("https://signlanguageglove.appspot.com");
        } catch(URISyntaxException e){}
    }

    private void attemptSend(String message){
        msocket.emit("msg", message);
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Chat.this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    // May need to be modified

                    String text;
                    if (args.length > 1) {
                        text = args[1].toString();
                        if (!text.equals("0")) {
                            DisplayWord(text);
                        }
                    }
                    else {
                        text = args[0].toString();
                        if (!text.equals("0")) {
                            DisplayWord(text);
                        }
                    }
                    if(!text.equals("0")) {
                        t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Text to speech
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                    Voice voiceobj = new Voice("en-us-x-sfg#male_1-local", Locale.getDefault(), 1, 1, false, null);
                    t1.setVoice(voiceobj);
                }
            }
        });

        text = (TextView)findViewById(R.id.text);
        //message = (EditText)findViewById(R.id.message);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        text.setMovementMethod(new ScrollingMovementMethod());

        b = new Bluetooth(this);
        b.enableBluetooth();

        b2 = new Bluetooth(this);
        b2.enableBluetooth();


        b.setCommunicationCallback(this);
        b2.setCommunicationCallback(this);

        for (int i = 0; i < (b.getPairedDevices()).size(); i++) {
            if ((b.getPairedDevices().get(i).getAddress()).equals("98:D3:91:FD:4A:94")) {
                lGlovePos = i;
            }
            if ((b2.getPairedDevices().get(i).getAddress()).equals("98:D3:91:FD:46:BF")) {
                rGlovePos = i;
            }
        }

        Display("Connecting...");

        b.connectToDevice(b.getPairedDevices().get(lGlovePos));
        b2.connectToDevice(b2.getPairedDevices().get(rGlovePos));



        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered=true;

        //New Socket stuff

        msocket.on("prediction", onNewMessage);
        msocket.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }

        msocket.disconnect();
        msocket.off("prediction", onNewMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.close:
                b.removeCommunicationCallback();
                b.disconnect();
                Intent intent = new Intent(this, Select.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.rate:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   public void Display(final String s){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.append(s + "\n");
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
    public void DisplayWord(final String s){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.append(s + "\n");
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        if(device.getAddress().equals("98:D3:91:FD:4A:94")){
            Display("Left hand Connected!");
        }
        else if(device.getAddress().equals("98:D3:91:FD:46:BF")){
            Display("Right hand Connected!");
        }
        /*this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                send.setEnabled(true);
            }
        });*/
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        if(device.getAddress().equals("98:D3:91:FD:4A:94")){
            Display("Left hand Disconnected!");
        }
        else if(device.getAddress().equals("98:D3:91:FD:46:BF")){
            Display("Right hand Disconnected!");
        }
        Display("Connecting again...");
        onDestroy();
        Intent i = new Intent(Chat.this, Chat.class);
        startActivity(i);

    }

    @Override
    public void onMessage(String message) {
        first = message.substring(0, 1);

        if(first.equals("L")){
            button_pressed = true;
        }
        else if(prev_letter.equals("R") && first.equals("R") && prev_prev_letter.equals("R") && prev_prev_prev_letter.equals("R")){
            button_pressed = false;
        }
        if (button_pressed && first.equals("L")) {
            attemptSend(message);
        }
        else if (!button_pressed && first.equals("R")) {
            attemptSend(message);
        }
        prev_prev_prev_letter = prev_prev_letter;
        prev_prev_letter = prev_letter;
        prev_letter = first;

    }

    @Override
    public void onError(String message) {
        Display("Error: "+message);
    }

    @Override
    public void onConnectError(final BluetoothDevice device, String message) {
//        Display("Error: "+message);
//        Display("Trying again in 3 sec.");
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        b.connectToDevice(device);
//                        b2.connectToDevice(device);
//                    }
//                }, 2000);
//            }
//        });
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                Intent intent1 = new Intent(Chat.this, Select.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        }
    };
}
