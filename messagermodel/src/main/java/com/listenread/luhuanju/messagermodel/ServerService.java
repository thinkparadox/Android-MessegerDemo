package com.listenread.luhuanju.messagermodel;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by luhuanju on 16/5/27.
 * 服务器端的Service  处理客户端的链接请求
 */

public class ServerService extends Service {
    private static final int MESSAGE = 0;
    private Messenger messenger = new Messenger(new MessengerHanlder());
    private static class MessengerHanlder extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE: {
                    //接收到客户端的消息
                    System.out.println(msg.getData().get("tag"));
                    Message message = Message.obtain(null, 0);
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", "收到消息,这是回复");
                    message.setData(bundle);
                    try {
                        msg.replyTo.send(message); //  msg.replyTo 返回的就是承载MESSAGE 消息的Messenger
                    } catch (RemoteException e) {
                    }


                    break;
                }
                default:
                    super.handleMessage(msg);

            }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { //
        return messenger.getBinder();  //


    }
}
