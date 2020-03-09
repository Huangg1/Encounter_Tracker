package com.example.offlineencountertracker.RoomFunc;

import android.content.Context;
import android.os.AsyncTask;

import com.example.offlineencountertracker.Room.AsyncCb;
import com.example.offlineencountertracker.Room.Connections;
import com.example.offlineencountertracker.Room.EncItem;

public class Update extends AsyncTask<Integer, Void, EncItem> {
    private Context context;
    private AsyncCb<EncItem> callback;
    private Exception exception;
    private EncItem encItem;

    public Update (EncItem encItem, Context context, AsyncCb<EncItem> callback){
        this.context = context;
        this.callback = callback;
        this.encItem = encItem;
    }

    @Override
    protected EncItem doInBackground(Integer... integers) {
        exception = null;

        try{
           Connections.getInstance(context).getDatabase().getDao().update(this.encItem);
        }
        catch(Exception e){
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(EncItem s) {
        super.onPostExecute(s);

        if (callback != null) {
            if (exception == null) {
                callback.handleResponse(s);
            }
            else {
                callback.handleFault(exception);
            }
        }
    }
}
