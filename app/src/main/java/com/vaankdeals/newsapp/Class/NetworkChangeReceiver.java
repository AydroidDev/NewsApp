package com.vaankdeals.newsapp.Class;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private ConnectionChangeCallback connectionChangeCallback;

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null)
                && activeNetwork.isConnected();


        if (connectionChangeCallback != null) {
            connectionChangeCallback.onConnectionChange(isConnected);
        }

    }

    public void setConnectionChangeCallback(ConnectionChangeCallback
                                                    connectionChangeCallback) {
        this.connectionChangeCallback = connectionChangeCallback;
    }


    public interface ConnectionChangeCallback {

        void onConnectionChange(boolean isConnected);

    }


}