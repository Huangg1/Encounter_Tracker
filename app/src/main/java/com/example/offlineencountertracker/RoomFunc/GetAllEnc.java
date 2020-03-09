package com.example.offlineencountertracker.RoomFunc;

import android.content.Context;
import android.os.AsyncTask;

import com.example.offlineencountertracker.Room.EncItem;
import com.example.offlineencountertracker.Room.AsyncCb;
import com.example.offlineencountertracker.Room.Connections;

import java.util.List;

public class GetAllEnc extends AsyncTask<Integer, Void, List<EncItem>> {
    private Context context;
    private AsyncCb callback;
    private Exception exception;

    public GetAllEnc(Context context, AsyncCb<List<EncItem>> callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected List<EncItem> doInBackground(Integer...integers){
        exception = null;
        List<EncItem> encounters= null;
        try{
            encounters = Connections.getInstance(this.context).getDatabase().getDao().getAllEnc();
            if(encounters.size() == 0){
                //Do nothing
            }
        }
        catch(Exception e){
            exception = e;
        }
        return encounters;
    }

    @Override
    protected void onPostExecute(List<EncItem> s){
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
