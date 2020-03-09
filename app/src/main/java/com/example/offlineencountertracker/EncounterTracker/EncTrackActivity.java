package com.example.offlineencountertracker.EncounterTracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.offlineencountertracker.Globals;
import com.example.offlineencountertracker.R;
import com.example.offlineencountertracker.RV.MyAdapter;
import com.example.offlineencountertracker.RV.RVItem;
import com.example.offlineencountertracker.Room.AsyncCb;
import com.example.offlineencountertracker.Room.EncCreaItem;
import com.example.offlineencountertracker.Room.EncItem;
import com.example.offlineencountertracker.RoomFunc.Delete;
import com.example.offlineencountertracker.RoomFunc.DeleteCrea;
import com.example.offlineencountertracker.Spells.SpellsActivity;

import java.util.ArrayList;
import java.util.List;

public class EncTrackActivity extends AppCompatActivity implements MyAdapter.ItemClicked {
    ETRec fragRecycler;
    ETCrea fragCreature;
    FragmentManager fragmentManager;
    Button btnDelEncounter, btnDelCreature;

    TextView tvType, tvAC, tvHP, tvCR, tvSTR, tvDEX, tvCON, tvINT, tvWIS, tvCHA, tvSTRMod, tvDEXMod, tvCONMod, tvINTMod, tvWISMod, tvCHAMod, tvExtraStuff, tvAttributes, tvSpells, tvActions, tvLegendActions;
    TextView Attributes, Spells, Actions, LegendActions;
    List<EncCreaItem> encCrea = new ArrayList<>();
    String[] creaSpells = {};
    String spellAbility = "";

    @Override
    protected void onResume(){
        super.onResume();
        encCrea.clear();
        getCrea();
        fragRecycler.notifyDataChanged();
        fragmentManager.beginTransaction().hide(fragCreature).commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enc_track);

        fragmentManager = this.getSupportFragmentManager();
        fragRecycler = (ETRec) fragmentManager.findFragmentById(R.id.fragRecyclerET);
        fragCreature = (ETCrea) fragmentManager.findFragmentById(R.id.fragCreatureET);

        tvType = findViewById(R.id.tvTypeFrag);
        tvAC = findViewById(R.id.tvACFrag);
        tvHP = findViewById(R.id.tvHPFrag);
        tvCR = findViewById(R.id.tvCRFrag);
        tvSTR = findViewById(R.id.tvSTRFrag);
        tvDEX = findViewById(R.id.tvDEXFrag);
        tvCON = findViewById(R.id.tvCONFrag);
        tvINT = findViewById(R.id.tvINTFrag);
        tvWIS = findViewById(R.id.tvWISFrag);
        tvCHA = findViewById(R.id.tvCHAFrag);
        tvSTRMod = findViewById(R.id.tvSTRModFrag);
        tvDEXMod = findViewById(R.id.tvDEXModFrag);
        tvCONMod = findViewById(R.id.tvCONModFrag);
        tvINTMod = findViewById(R.id.tvINTModFrag);
        tvWISMod = findViewById(R.id.tvWISModFrag);
        tvCHAMod = findViewById(R.id.tvCHAModFrag);
        tvExtraStuff = findViewById(R.id.tvExtraStuffFrag);
        tvAttributes = findViewById(R.id.tvAttributesFrag);
        tvSpells = findViewById(R.id.tvSpellsFrag);
        tvActions = findViewById(R.id.tvActionsFrag);
        tvLegendActions = findViewById(R.id.tvLegendActionsFrag);

        Attributes = findViewById(R.id.AttributesFrag);
        Spells = findViewById(R.id.SpellsFrag);
        Actions = findViewById(R.id.ActionsFrag);
        LegendActions = findViewById(R.id.LegendaryActionsFrag);

        btnDelEncounter = findViewById(R.id.btnDelEncounter);
        btnDelCreature = findViewById(R.id.btnDelCreature);

        getCrea();
        btnDelEncounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EncTrackActivity.this);
            dialog.setMessage("Are you sure?.");

            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delEncounter();
                }
            });

            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(EncTrackActivity.this,"Cancelled!", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
            }
        });

        btnDelCreature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EncTrackActivity.this, DelCrea.class);
                startActivityForResult(intent, 1);
            }
        });

        fragmentManager.beginTransaction().hide(fragCreature).commit();
    }

    public void getCrea(){
        ArrayList<RVItem> rvTempList = new ArrayList<>();
        for(int i = 0; i < Globals.encCreatures.size(); i++){
            if(Globals.etCurEnc.equals(Globals.encCreatures.get(i).getName())){
                RVItem newItem = new RVItem();
                newItem.setTitle(Globals.encCreatures.get(i).getCrea());
                newItem.setDesc("Quantity: " + Globals.encCreatures.get(i).getQ());
                rvTempList.add(newItem);
                encCrea.add(Globals.encCreatures.get(i));
            }
        }
        if(rvTempList.size() == 0){
            Toast.makeText(EncTrackActivity.this, "This encounter has no creatures! Go to the home page and click \"Creatures\" to add some.", Toast.LENGTH_LONG).show();
        }
        fragRecycler.putList(rvTempList);
    }

    @Override
    public void onItemClicked(int index) {
        fragmentManager.beginTransaction().show(fragCreature).commit();
        for(int i = 0; i < Globals.allCreatures.size(); i++){
            if(encCrea.get(index).getCrea().equals(Globals.allCreatures.get(i).getName())){
                tvType.setText(Globals.allCreatures.get(i).getType());

                tvAC.setText(Globals.allCreatures.get(i).getAC());
                tvHP.setText(Globals.allCreatures.get(i).getHP());
                tvCR.setText(Globals.allCreatures.get(i).getCR());

                String STRText = "" + Globals.allCreatures.get(i).getSTR();
                tvSTR.setText(STRText);
                String DEXText = "" + Globals.allCreatures.get(i).getDEX();
                tvDEX.setText(DEXText);
                String CONText = "" + Globals.allCreatures.get(i).getCON();
                tvCON.setText(CONText);
                String INTText = "" + Globals.allCreatures.get(i).getINT();
                tvINT.setText(INTText);
                String WISText = "" + Globals.allCreatures.get(i).getWIS();
                tvWIS.setText(WISText);
                String CHAText = "" + Globals.allCreatures.get(i).getCHA();
                tvCHA.setText(CHAText);

                String STRModText = "(" + (Globals.allCreatures.get(i).getSTR()/2-5)+ ")";
                tvSTRMod.setText(STRModText);
                String DEXModText = "(" + (Globals.allCreatures.get(i).getDEX()/2-5)+ ")";
                tvDEXMod.setText(DEXModText);
                String CONModText = "(" + (Globals.allCreatures.get(i).getCON()/2-5)+ ")";
                tvCONMod.setText(CONModText);
                String INTModText = "(" + (Globals.allCreatures.get(i).getINT()/2-5)+ ")";
                tvINTMod.setText(INTModText);
                String WISModText = "(" + (Globals.allCreatures.get(i).getWIS()/2-5)+ ")";
                tvWISMod.setText(WISModText);
                String CHAModText = "(" + (Globals.allCreatures.get(i).getCHA()/2-5)+ ")";
                tvCHAMod.setText(CHAModText);

                StringBuilder extraString = new StringBuilder();
                if(Globals.allCreatures.get(i).getSaves()!= null) {
                    extraString.append("Saves: ");
                    extraString.append(Globals.allCreatures.get(i).getSaves());
                    extraString.append("\n");
                }
                if(Globals.allCreatures.get(i).getSkills()!= null) {
                    extraString.append("Skills: ");
                    extraString.append(Globals.allCreatures.get(i).getSkills());
                    extraString.append("\n");
                }
                if(Globals.allCreatures.get(i).getVulnerabilities()!= null) {
                    extraString.append("Vulnerabilities: ");
                    extraString.append(Globals.allCreatures.get(i).getVulnerabilities());
                    extraString.append("\n");
                }
                if(Globals.allCreatures.get(i).getResistances()!= null) {
                    extraString.append("Resistances: ");
                    extraString.append(Globals.allCreatures.get(i).getResistances());
                    extraString.append("\n");
                }
                if(Globals.allCreatures.get(i).getImmunities()!= null) {
                    extraString.append("Immunities: ");
                    extraString.append(Globals.allCreatures.get(i).getImmunities());
                    extraString.append("\n");
                }
                if(Globals.allCreatures.get(i).getConImmunities()!= null) {
                    extraString.append("Condition Immunities: ");
                    extraString.append(Globals.allCreatures.get(i).getConImmunities());
                    extraString.append("\n");
                }
                if(Globals.allCreatures.get(i).getSenses()!= null) {
                    extraString.append("Senses: ");
                    extraString.append(Globals.allCreatures.get(i).getSenses());
                    extraString.append("\n");
                }
                if(Globals.allCreatures.get(i).getLanguages()!= null) {
                    extraString.append("Languages: ");
                    extraString.append(Globals.allCreatures.get(i).getLanguages());
                    extraString.append("\n");
                }
                tvExtraStuff.setText(extraString.toString());

                if(!Globals.allCreatures.get(i).getAttributes().isEmpty()) {
                    tvAttributes.setText(Globals.allCreatures.get(i).getAttributes());
                    Attributes.setVisibility(View.VISIBLE);
                    tvAttributes.setVisibility(View.VISIBLE);
                }
                else{
                    Attributes.setVisibility(View.GONE);
                    tvAttributes.setVisibility(View.GONE);
                }

                if(!Globals.allCreatures.get(i).getSpells().isEmpty()) {
                    Spells.setText(R.string.Spells);
                    tvSpells.setText(Globals.allCreatures.get(i).getSpells());
                    Spells.setVisibility(View.VISIBLE);
                    tvSpells.setVisibility(View.VISIBLE);

                    creaSpells = Globals.allCreatures.get(index).getSpellList();
                    spellAbility = Globals.allCreatures.get(index).getSpells().split("\\.")[0] + ". " +  Globals.allCreatures.get(index).getSpells().split("\\.")[1] + ".";


                    /*Spells.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            startSpells();
                        }
                    });
                     */
                }
                else{
                    Spells.setVisibility(View.GONE);
                    tvSpells.setVisibility(View.GONE);
                }
                if(!Globals.allCreatures.get(i).getActions().isEmpty()) {
                    Actions.setText(R.string.Actions);
                    tvActions.setText(Globals.allCreatures.get(i).getActions());
                    Actions.setVisibility(View.VISIBLE);
                    tvActions.setVisibility(View.VISIBLE);
                }
                else{
                    Actions.setVisibility(View.GONE);
                    tvActions.setVisibility(View.GONE);
                }

                if(!Globals.allCreatures.get(i).getLegendActions().isEmpty()) {
                    LegendActions.setText(R.string.Legendary_Actions);
                    tvLegendActions.setText(Globals.allCreatures.get(i).getLegendActions());
                    LegendActions.setVisibility(View.VISIBLE);
                    tvLegendActions.setVisibility(View.VISIBLE);
                }
                else{
                    LegendActions.setVisibility(View.GONE);
                    tvLegendActions.setVisibility(View.GONE);
                }
            }
        }
    }

    public void startSpells(){
        Intent intent = new Intent(EncTrackActivity.this, SpellsActivity.class);
        intent.putExtra("spellAbility", spellAbility);
        intent.putExtra("spellList", creaSpells);
        startActivity(intent);
    }

    public void delEncounter(){
        for(int i = Globals.encCreatures.size() - 1; i >= 0; i--){
            if(Globals.etCurEnc.equals(Globals.encCreatures.get(i).getName())){
                new DeleteCrea(Globals.encCreatures.get(i), getApplicationContext(), new AsyncCb<EncCreaItem>() {
                    @Override
                    public void handleResponse(EncCreaItem object) {
                        //Do nothing
                    }

                    @Override
                    public void handleFault(Exception e) {
                        Toast.makeText(EncTrackActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).execute();
                Globals.encCreatures.remove(Globals.encCreatures.get(i));
            }
        }
        for(int i = 0; i < Globals.encounters.size(); i++){
            if(Globals.etCurEnc.equals(Globals.encounters.get(i).getName())){
                new Delete(Globals.encounters.get(i), getApplicationContext(), new AsyncCb<EncItem>() {
                    @Override
                    public void handleResponse(EncItem object) {
                        Toast.makeText(EncTrackActivity.this, "Encounter deleted!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleFault(Exception e) {
                        Toast.makeText(EncTrackActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).execute();
                Globals.encounters.remove(Globals.encounters.get(i));
                EncTrackActivity.this.finish();
            }
        }
    }
}