package com.example.offlineencountertracker.Spells;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.offlineencountertracker.Globals;
import com.example.offlineencountertracker.R;
import com.example.offlineencountertracker.RV.MyAdapter;
import com.example.offlineencountertracker.RV.RVItem;

import java.util.ArrayList;

public class SpellsActivity extends AppCompatActivity implements MyAdapter.ItemClicked {
    String[] spellList = {};
    ArrayList<SpellItem> curSpells = new ArrayList<>();
    String ability;

    SpRec fragRecycler;
    SpDes fragSpellDes;
    FragmentManager fragmentManager;
    TextView tvSpellAbility, tvSpellDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spells);

        ability = getIntent().getStringExtra("spellAbility");
        spellList = getIntent().getStringArrayExtra("spellList");

        fragmentManager = this.getSupportFragmentManager();
        fragRecycler = (SpRec) fragmentManager.findFragmentById(R.id.fragRecyclerSpell);
        fragSpellDes = (SpDes) fragmentManager.findFragmentById(R.id.fragSpellDes);

        tvSpellAbility = findViewById(R.id.tvSpellAbility);
        tvSpellDes = findViewById(R.id.tvSpellDes);

        getSpells();

        fragmentManager.beginTransaction().hide(fragSpellDes).commit();
    }

    public void getSpells(){
        String type;
        ArrayList<RVItem> rvTempList = new ArrayList<>();
        for(int i = 0; i < spellList.length; i++){
            type = "";
            for(int j = 0; j < Globals.allSpells.size(); j++) {
                if (spellList[i].trim().equalsIgnoreCase(Globals.allSpells.get(j).getName())) {
                    curSpells.add(Globals.allSpells.get(j));
                    RVItem newItem = new RVItem();
                    newItem.setTitle(Globals.allSpells.get(j).getName());
                    if(Globals.allSpells.get(j).getLevel().trim().equals("Cantrip")||Globals.allSpells.get(j).getLevel().trim().equals("0")) {
                        type = Globals.allSpells.get(j).getSchool() + " Cantrip";
                    }
                    else{
                        type = "Level " + Globals.allSpells.get(j).getLevel() + " " + Globals.allSpells.get(j).getSchool() + " Spell";
                    }
                    newItem.setDesc(type);
                    rvTempList.add(newItem);
                    break;
                }
            }
        }
        fragRecycler.putList(rvTempList);
    }

    @Override
    public void onItemClicked(int index) {
        fragmentManager.beginTransaction().show(fragSpellDes).commit();
        SpellItem currentSpell = curSpells.get(index);

        tvSpellAbility.setText(ability);
        StringBuilder desString = new StringBuilder();
        desString.append("Casting Time: " + currentSpell.getTime() + "\n");
        desString.append("Range: " + currentSpell.getRange() + "\n");
        desString.append("Components: " + currentSpell.getComp() + "\n");
        desString.append("Duration: " + currentSpell.getDuration() + "\n");
        if(currentSpell.getRitual().trim().equalsIgnoreCase("YES")){
            desString.append("Ritual Casting: Yes \n");
        }
        desString.append("\n");
        desString.append(currentSpell.getDescription());
        tvSpellDes.setText(desString.toString());
    }
}
