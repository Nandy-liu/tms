package com.example.tms.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AlertDialog;

import com.example.tms.R;

public class DialogConfirm {
    private Context context;
    private Handler mHandler;
    public DialogConfirm(Context context, Handler handler) {
        this.context = context;
        this.mHandler=handler;
    }
    public AlertDialog.Builder build() {
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //    设置Title的图标
        builder.setIcon(R.drawable.ic_launcher_foreground);
        //    设置Title的内容
        builder.setTitle("弹出警告框");
        //    设置Content来显示一个信息
        builder.setMessage("确定删除吗？");
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message msg = new Message();
                msg.what=1;
                mHandler.sendMessage(msg);
            }
        });
        //    设置一个NegativeButton
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder;
    }
}
