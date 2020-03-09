package com.example.offlineencountertracker.EncounterTracker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.offlineencountertracker.R;
import com.example.offlineencountertracker.RV.MyAdapter;
import com.example.offlineencountertracker.RV.RVItem;

import java.util.ArrayList;


public class ETRec extends Fragment {
    private RecyclerView.Adapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RVItem> encounterList = new ArrayList<>();
    private View view;

    public ETRec() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_etrec, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView rvETCreatures = view.findViewById(R.id.rvETCreatures);
        rvETCreatures.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvETCreatures.setLayoutManager(layoutManager);

        if(encounterList == null) {
            RVItem tempRVItem = new RVItem();
            tempRVItem.setTitle("Temp");
            tempRVItem.setDesc("Temp");
            encounterList.add(tempRVItem);
        }

        itemAdapter = new MyAdapter(this.getActivity(), encounterList);
        rvETCreatures.setAdapter(itemAdapter);
    }

    public void putList(ArrayList<RVItem> currentEncounter){
        encounterList.clear();

        for(int i=0;i < currentEncounter.size();i++){

            RVItem tempItem = new RVItem();
            tempItem.setTitle(currentEncounter.get(i).getTitle());
            tempItem.setDesc(currentEncounter.get(i).getDesc());
            encounterList.add(tempItem);
        }
    }

    public void notifyDataChanged(){
        itemAdapter.notifyDataSetChanged();
    }
}
