package com.example.offlineencountertracker.Spells;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.offlineencountertracker.R;
import com.example.offlineencountertracker.RV.MyAdapter;
import com.example.offlineencountertracker.RV.RVItem;

import java.util.ArrayList;

public class SpRec extends Fragment {
    private RecyclerView.Adapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RVItem> spellList = new ArrayList<>();
    private View view;

    public SpRec() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sprec, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView rvSpNames = view.findViewById(R.id.rvSpNames);
        rvSpNames.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        rvSpNames.setLayoutManager(layoutManager);

        if(spellList == null) {
            RVItem tempRVItem = new RVItem();
            tempRVItem.setTitle("Temp");
            tempRVItem.setDesc("Temp");
            spellList.add(tempRVItem);
        }

        itemAdapter = new MyAdapter(this.getActivity(), spellList);
        rvSpNames.setAdapter(itemAdapter);
    }

    public void putList(ArrayList<RVItem> currentSpells){
        spellList.clear();

        for(int i=0;i < currentSpells.size();i++){

            RVItem tempItem = new RVItem();
            tempItem.setTitle(currentSpells.get(i).getTitle());
            tempItem.setDesc(currentSpells.get(i).getDesc());
            spellList.add(tempItem);
        }
    }

    public void notifyDataChanged(){
        itemAdapter.notifyDataSetChanged();
    }
}
