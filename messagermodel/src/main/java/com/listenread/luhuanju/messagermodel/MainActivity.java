package com.listenread.luhuanju.messagermodel;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, ServerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    private Messenger messenger;
    private Messenger messengerToReply = new Messenger(new MessagerHanlder());

    //客户端要回复消息,同样需要准备HANDLER
    private static class MessagerHanlder extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println(msg.getData().get("tag"));

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 绑定Service
     */

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) { //此SERVICE 是绑定成功之后,服务器返回的Ibinder
            messenger = new Messenger(iBinder);
            Message msg = Message.obtain(null, 0);
            Bundle bundle = new Bundle();
            bundle.putString("tag", "i am client");
            msg.setData(bundle);
            //注意此句
            msg.replyTo = messengerToReply;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
