package com.example.socketio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    EditText mClientInput;
    EditText mServerOutput;
    Button mButtonSend;

    private Socket mSocket;
    {
        try{
            mSocket = IO.socket("http://chat.socket.io");//insert web url
        }catch(URISyntaxException e){}
    }

    private void attemptSend(){
        String message = mClientInput.getText().toString().trim();
        if(TextUtils.isEmpty(message)){
            return;
        }
        mClientInput.setText("");
        mSocket.emit("new message", message);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    JSONObject data = (JSONObject) args[0];
                    String serverString;
                    try{
                        serverString = data.getString("serveroutput");// may need to change to get server output
                    }catch(JSONException e){
                        return;
                    }

                    mServerOutput.setText(serverString);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mClientInput = (EditText) findViewById(R.id.ClientInput);
        mButtonSend = (Button) findViewById(R.id.sendbutton);
        mServerOutput = (EditText) findViewById(R.id.ServerOutput);

        mSocket.on("new message", onNewMessage);
        mSocket.connect();

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }


}
