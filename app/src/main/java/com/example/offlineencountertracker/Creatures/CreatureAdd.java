package com.example.offlineencountertracker.Creatures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.offlineencountertracker.Globals;
import com.example.offlineencountertracker.R;
import com.example.offlineencountertracker.RV.MyAdapter;
import com.example.offlineencountertracker.RV.RVItem;
import com.example.offlineencountertracker.Room.AsyncCb;
import com.example.offlineencountertracker.Room.EncCreaItem;
import com.example.offlineencountertracker.Room.EncItem;
import com.example.offlineencountertracker.RoomFunc.GetAllEncCrea;
import com.example.offlineencountertracker.RoomFunc.InsertCrea;
import com.example.offlineencountertracker.RoomFunc.Update;
import com.example.offlineencountertracker.RoomFunc.UpdateCrea;

import java.util.ArrayList;
import java.util.List;

public class CreatureAdd extends AppCompatActivity implements MyAdapter.ItemClicked{

    RecyclerView rvList;
    RecyclerView.Adapter itemAdapter;
    RecyclerView.LayoutManager layoutManager;

    EditText etCreatureQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creature_add);

        rvList = findViewById(R.id.rvAddCreaturesEncounters);
        rvList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);

        etCreatureQ = findViewById(R.id.etCreatureQ);

        List<RVItem> rvTempList = new ArrayList<>();
        for (int i = 0; i < Globals.encounters.size(); i++) {
            RVItem newItem = new RVItem();
            newItem.setTitle(Globals.encounters.get(i).getName());
            newItem.setDesc("Total Creatures: " + Globals.encounters.get(i).getQ());
            rvTempList.add(newItem);
        }
        if (rvTempList.size() == 0){
            Toast.makeText(CreatureAdd.this, "You have no encounters! Go to the home page and click \"Add Encounter\" to add one.", Toast.LENGTH_LONG).show();
        }

        itemAdapter = new MyAdapter(CreatureAdd.this, rvTempList);
        rvList.setAdapter(itemAdapter);

    }

    @Override
    public void onItemClicked(int index) {
        if(etCreatureQ.getText().toString().isEmpty() || Integer.parseInt(etCreatureQ.getText().toString()) == 0){
            Toast.makeText(CreatureAdd.this, "Please enter a valid number!", Toast.LENGTH_SHORT).show();
        }
        else {
            Globals.encounters.get(index).setQ(Globals.encounters.get(index).getQ() + Integer.parseInt(etCreatureQ.getText().toString().trim()));
            new Update(Globals.encounters.get(index), getApplicationContext(), new AsyncCb<EncItem>() {
                @Override
                public void handleResponse(EncItem object) {
                    //Do nothing
                }

                @Override
                public void handleFault(Exception e) {
                    Toast.makeText(CreatureAdd.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).execute();

            String searchQ = Globals.encounters.get(index).getName() + Globals.addName;
            boolean found = false;
            for(int i = 0; i <Globals.encCreatures.size(); i++) {
                if(searchQ.equals(Globals.encCreatures.get(i).getId())) {
                    Globals.encCreatures.get(i).setQ(Globals.encCreatures.get(i).getQ() + Integer.parseInt(etCreatureQ.getText().toString().trim()));
                    new UpdateCrea(Globals.encCreatures.get(i), getApplicationContext(), new AsyncCb<EncCreaItem>() {
                        @Override
                        public void handleResponse(EncCreaItem object) {
                            Toast.makeText(CreatureAdd.this, "Creatures added!", Toast.LENGTH_SHORT).show();
                            CreatureAdd.this.finish();
                        }

                        @Override
                        public void handleFault(Exception e) {
                            Toast.makeText(CreatureAdd.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).execute();
                    found = true;
                    break;
                }
            }

            if(!found){
                EncCreaItem newECI = new EncCreaItem();
                newECI.setName(Globals.encounters.get(index).getName());
                newECI.setCrea(Globals.addName);
                newECI.setId(searchQ);
                newECI.setQ(Integer.parseInt(etCreatureQ.getText().toString().trim()));
                Globals.encCreatures.add(newECI);
                new InsertCrea(newECI, getApplicationContext(), new AsyncCb<EncCreaItem>() {
                    @Override
                    public void handleResponse(EncCreaItem object) {
                        Toast.makeText(CreatureAdd.this, "Creatures added!", Toast.LENGTH_SHORT).show();
                        CreatureAdd.this.finish();
                    }

                    @Override
                    public void handleFault(Exception e) {
                        Toast.makeText(CreatureAdd.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).execute();
            }
        }
    }

    public void updateCrea(){
        new GetAllEncCrea(getApplicationContext(), new AsyncCb<List<EncCreaItem>>() {
            @Override
            public void handleResponse(List<EncCreaItem> object) {
                Globals.encCreatures = object;
            }

            @Override
            public void handleFault(Exception e) {
                Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}
