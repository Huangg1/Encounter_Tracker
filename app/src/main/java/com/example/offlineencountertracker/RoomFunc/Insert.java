package com.example.offlineencountertracker.RoomFunc;

import android.content.Context;
import android.os.AsyncTask;

import com.example.offlineencountertracker.Room.EncItem;
import com.example.offlineencountertracker.Room.AsyncCb;
import com.example.offlineencountertracker.Room.Connections;

public class Insert extends AsyncTask<Integer, Void, EncItem> {
    private Context context;
    private AsyncCb callback;
    private Exception exception;
    private EncItem encItem;

    public Insert(EncItem encItem, Context context, AsyncCb<EncItem> callback){
        this.context = context;
        this.callback = callback;
        this.encItem = encItem;
    }

    @Override
    protected EncItem doInBackground(Integer ...integers){
        exception = null;
        try{
            EncItem encItem = Connections.getInstance(context).getDatabase().getDao().getEncById(this.encItem.getName());
            if(encItem == null){
                Connections.getInstance(context).getDatabase().getDao().insert(this.encItem);
            }
            else{
                throw new Exception("This encounter already exists!");
            }
        }
        catch(Exception e){
            exception = e;
        }
        return this.encItem;
    }

    @Override
    protected void onPostExecute(EncItem s){
        super.onPostExecute(s);
        if(callback != null) {
            if (exception == null) {
                callback.handleResponse(s);
            } else {
                callback.handleFault(exception);
            }
        }
    }
}
