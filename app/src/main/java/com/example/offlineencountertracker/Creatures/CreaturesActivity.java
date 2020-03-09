package com.example.offlineencountertracker.Creatures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.offlineencountertracker.Globals;
import com.example.offlineencountertracker.R;
import com.example.offlineencountertracker.RV.MyAdapter;
import com.example.offlineencountertracker.RV.RVItem;

import java.util.ArrayList;
import java.util.List;

public class CreaturesActivity extends AppCompatActivity implements MyAdapter.ItemClicked{

    RecyclerView rvList;
    RecyclerView.Adapter itemAdapter;
    RecyclerView.LayoutManager layoutManager;

    Button btnSearch;
    EditText etCreaSearch;

    ArrayList<CreaItem> filtCrea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatures);

        btnSearch = findViewById(R.id.btnSearch);
        etCreaSearch = findViewById(R.id.etCreaSearch);

        rvList = findViewById(R.id.rvCreatures);
        rvList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);

        filtCrea = Globals.allCreatures;
        popCrea(filtCrea);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (etCreaSearch.getText().toString().isEmpty()) {
                filtCrea = Globals.allCreatures;
                popCrea(filtCrea);
            }
            else {
                filtCrea = new ArrayList<>();
                String query = etCreaSearch.getText().toString().trim();
                for(int i = 0; i < Globals.allCreatures.size(); i++){
                    String name = Globals.allCreatures.get(i).getName().toLowerCase();
                    if (name.contains(query.toLowerCase())){
                        filtCrea.add(Globals.allCreatures.get(i));
                    }
                }
                if(filtCrea.size() == 0){
                    filtCrea = Globals.allCreatures;
                    Toast.makeText(CreaturesActivity.this, "No results!", Toast.LENGTH_SHORT).show();
                }
                else{
                    popCrea(filtCrea);
                }
            }
            hideKeyboard(CreaturesActivity.this);
            }
        });
    }

    public void popCrea(ArrayList<CreaItem> crea){
        List<RVItem> rvTempList = new ArrayList<>();

        for(int i = 0; i < crea.size(); i++){
            RVItem newItem = new RVItem();
            newItem.setTitle(crea.get(i).getName());
            newItem.setDesc(crea.get(i).getType());
            rvTempList.add(newItem);
        }

        itemAdapter = new MyAdapter(CreaturesActivity.this, rvTempList);
        rvList.setAdapter(itemAdapter);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onItemClicked(int index) {
        Intent intent = new Intent(CreaturesActivity.this, com.example.offlineencountertracker.Creatures.CreatureBlock.class);
        intent.putExtra("name", filtCrea.get(index).getName());
        startActivity(intent);
    }
}
