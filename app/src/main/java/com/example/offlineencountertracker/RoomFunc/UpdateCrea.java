package com.example.offlineencountertracker.RoomFunc;

import android.content.Context;
import android.os.AsyncTask;

import com.example.offlineencountertracker.Room.AsyncCb;
import com.example.offlineencountertracker.Room.Connections;
import com.example.offlineencountertracker.Room.EncCreaItem;

public class UpdateCrea extends AsyncTask<Integer, Void, EncCreaItem> {
    private Context context;
    private AsyncCb<EncCreaItem> callback;
    private Exception exception;
    private EncCreaItem encCreaItem;

    public UpdateCrea (EncCreaItem encCreaItem, Context context, AsyncCb<EncCreaItem> callback){
        this.context = context;
        this.callback = callback;
        this.encCreaItem = encCreaItem;
    }

    @Override
    protected EncCreaItem doInBackground(Integer... integers) {
        exception = null;

        try{
            Connections.getInstance(context).getDatabase().getDao().updateCrea(this.encCreaItem);
        }
        catch(Exception e){
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(EncCreaItem s) {
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
