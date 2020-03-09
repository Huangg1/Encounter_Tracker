package com.example.offlineencountertracker.RoomFunc;

import android.content.Context;
import android.os.AsyncTask;

import com.example.offlineencountertracker.Room.AsyncCb;
import com.example.offlineencountertracker.Room.Connections;
import com.example.offlineencountertracker.Room.EncCreaItem;

import java.util.List;

public class GetAllEncCrea extends AsyncTask<Integer, Void, List<EncCreaItem>> {
    private Context context;
    private AsyncCb callback;
    private Exception exception;

    public GetAllEncCrea(Context context, AsyncCb<List<EncCreaItem>> callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected List<EncCreaItem> doInBackground(Integer...integers){
        exception = null;
        List<EncCreaItem> encounters= null;
        try{
            encounters = Connections.getInstance(this.context).getDatabase().getDao().getAllEncCrea();
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
    protected void onPostExecute(List<EncCreaItem> s){
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
