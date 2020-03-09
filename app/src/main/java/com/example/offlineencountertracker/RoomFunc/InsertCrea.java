package com.example.offlineencountertracker.RoomFunc;

import android.content.Context;
import android.os.AsyncTask;

import com.example.offlineencountertracker.Room.AsyncCb;
import com.example.offlineencountertracker.Room.Connections;
import com.example.offlineencountertracker.Room.EncCreaItem;

public class InsertCrea extends AsyncTask<Integer, Void, EncCreaItem> {
    private Context context;
    private AsyncCb callback;
    private Exception exception;
    private EncCreaItem encCreaItem;

    public InsertCrea(EncCreaItem encCreaItem, Context context, AsyncCb<EncCreaItem> callback){
        this.context = context;
        this.callback = callback;
        this.encCreaItem = encCreaItem;
    }

    @Override
    protected EncCreaItem doInBackground(Integer ...integers){
        exception = null;
        try{
            EncCreaItem encCreaItem = Connections.getInstance(context).getDatabase().getDao().getEncCreaById(this.encCreaItem.getId());
            if(encCreaItem == null){
                Connections.getInstance(context).getDatabase().getDao().insertCrea(this.encCreaItem);
            }
            else{
                throw new Exception("This encounter already exists!");
            }
        }
        catch(Exception e){
            exception = e;
        }
        return this.encCreaItem;
    }

    @Override
    protected void onPostExecute(EncCreaItem s){
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
