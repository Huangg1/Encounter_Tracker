package com.example.offlineencountertracker.EncounterTracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.offlineencountertracker.Globals;
import com.example.offlineencountertracker.R;
import com.example.offlineencountertracker.RV.MyAdapter;
import com.example.offlineencountertracker.RV.RVItem;
import com.example.offlineencountertracker.Room.AsyncCb;
import com.example.offlineencountertracker.Room.EncCreaItem;
import com.example.offlineencountertracker.Room.EncItem;
import com.example.offlineencountertracker.RoomFunc.DeleteCrea;
import com.example.offlineencountertracker.RoomFunc.Update;
import com.example.offlineencountertracker.RoomFunc.UpdateCrea;

import java.util.ArrayList;
import java.util.List;

public class DelCrea extends AppCompatActivity implements MyAdapter.ItemClicked {

    RecyclerView rvList;
    RecyclerView.Adapter itemAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextView etCreatureQDel;

    List<EncCreaItem> encCrea = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_crea);

        rvList = findViewById(R.id.rvDelCreaturesEncounters);
        rvList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);

        etCreatureQDel = findViewById(R.id.etCreatureQDel);

        ArrayList<RVItem> rvTempList = new ArrayList<>();
        for (int i = 0; i < Globals.encCreatures.size(); i++) {
            if (Globals.etCurEnc.equals(Globals.encCreatures.get(i).getName())) {
                RVItem newItem = new RVItem();
                newItem.setTitle(Globals.encCreatures.get(i).getCrea());
                newItem.setDesc("Quantity: " + Globals.encCreatures.get(i).getQ());
                rvTempList.add(newItem);
                encCrea.add(Globals.encCreatures.get(i));
            }
        }

        itemAdapter = new MyAdapter(DelCrea.this, rvTempList);
        rvList.setAdapter(itemAdapter);
    }

    public void updateCrea(int index){
        int deleteQ = Integer.parseInt(etCreatureQDel.getText().toString().trim());
        if(deleteQ <= 0){
            Toast.makeText(DelCrea.this,"Please enter a valid number!", Toast.LENGTH_LONG).show();
        }
        else if(deleteQ > encCrea.get(index).getQ()){
            Toast.makeText(DelCrea.this, "You don't have that many creatures!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (deleteQ == encCrea.get(index).getQ()) {
                new DeleteCrea(encCrea.get(index), getApplicationContext(), new AsyncCb<EncCreaItem>() {
                    @Override
                    public void handleResponse(EncCreaItem object) {
                        //Do nothing
                    }

                    @Override
                    public void handleFault(Exception e) {
                        Toast.makeText(DelCrea.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).execute();
                Globals.encCreatures.remove(encCrea.get(index));
            }
            else {
                encCrea.get(index).setQ(encCrea.get(index).getQ() - deleteQ);
                new UpdateCrea(encCrea.get(index), getApplicationContext(), new AsyncCb<EncCreaItem>() {
                    @Override
                    public void handleResponse(EncCreaItem object) {
                        //Do nothing
                    }

                    @Override
                    public void handleFault(Exception e) {
                        Toast.makeText(DelCrea.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).execute();
            }
            for(int i = 0; i < Globals.encounters.size(); i++){
                if(Globals.etCurEnc.equals(Globals.encounters.get(i).getName())){
                    Globals.encounters.get(i).setQ(Globals.encounters.get(i).getQ() - deleteQ);
                    new Update(Globals.encounters.get(i), getApplicationContext(), new AsyncCb<EncItem>() {
                        @Override
                        public void handleResponse(EncItem object) {
                            Toast.makeText(DelCrea.this, "Creatures deleted!", Toast.LENGTH_SHORT).show();
                            DelCrea.this.finish();
                        }

                        @Override
                        public void handleFault(Exception e) {
                            Toast.makeText(DelCrea.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).execute();
                }
            }
        }
    }

    @Override
    public void onItemClicked(int index) {
        if(etCreatureQDel.getText().toString().isEmpty() || Integer.parseInt(etCreatureQDel.getText().toString()) == 0){
            Toast.makeText(DelCrea.this,"Please enter a number!", Toast.LENGTH_LONG).show();
        }
        else {
            updateCrea(index);
        }
    }
}

