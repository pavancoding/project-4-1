package com.example.reminderandtodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class stop_notify extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //dialog.cancel();
        Intent i=new Intent(context,location_service.class);
        context.stopService(i);
    }
}
