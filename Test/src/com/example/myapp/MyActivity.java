package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import MySocket.SocketClient;



public class MyActivity extends Activity implements Runnable{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(listener);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 0x110)
                {
                    Toast.makeText(MyActivity.this,msg.getData().getString("mesg"),Toast.LENGTH_LONG).show();
                    Button button=(Button)findViewById(R.id.button);
                    button.setBackgroundColor(0xFF37FF2A);
                    button.setText("连接");
                    button.setEnabled(true);
                }
                else if(msg.what ==0x100)
                {
                    Intent intent = new Intent();
                    intent.setClass(MyActivity.this, DoActivity.class);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(MyActivity.this,msg.getData().getString("mesg"),Toast.LENGTH_LONG).show();
                }
                super.handleMessage(msg);
            }
        };
    }

    private Handler handler;
    private Thread thread;
    //private SocketClient client=(SocketClient)MyActivity.this.getApplication();

    public void run(){
        //client.SocketClient_init("10.0.2.2",12345);
        SocketClient.SocketClient_init("10.0.2.2",12345);
        Message m=handler.obtainMessage();
        Bundle bundle=new Bundle();
        if(SocketClient.IsConnected() == false){
            m.what=0x110;
            bundle.putString("mesg","无法连接目标主机");
        }
        else{
            //SocketClient.sendMsg("nimei1");
            //client.closeSocket();
            m.what=0x100;
            bundle.putString("mesg","true");
        }
        m.setData(bundle);
        handler.sendMessage(m);
        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btnButton =(Button) v;
            switch (btnButton.getId()){
                case R.id.button:
                    btnButton.setEnabled(false);
                    btnButton.setBackgroundColor(0xF85C5A6B);
                    btnButton.setText("连接中...");
                    thread = new Thread(MyActivity.this);
                    thread.start();
                    break;
            }
        }
    };

    protected void onDestroy(){
        if(thread != null)
        {
            thread.interrupt();
            thread = null;
        }
        /*
        if(SocketClient.IsConnected())
        {
            SocketClient.closeSocket();
        }
        */
        super.onDestroy();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
