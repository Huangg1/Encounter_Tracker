package com.example.offlineencountertracker.Creatures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.offlineencountertracker.EncounterTracker.EncTrackActivity;
import com.example.offlineencountertracker.Globals;
import com.example.offlineencountertracker.R;
import com.example.offlineencountertracker.Spells.SpellsActivity;

public class CreatureBlock extends AppCompatActivity {

    TextView tvCreatureName, tvType, tvAC, tvHP, tvCR, tvSTR, tvDEX, tvCON, tvINT, tvWIS, tvCHA, tvSTRMod, tvDEXMod, tvCONMod, tvINTMod, tvWISMod, tvCHAMod, tvExtraStuff, tvAttributes, tvSpells, tvActions, tvLegendActions;
    TextView Attributes, Spells, Actions, LegendActions;
    Button btnAddCreature;

    String[] creaSpells = {};
    String spellAbility = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creature_block);

        tvCreatureName = findViewById(R.id.tvCreatureName);
        tvType = findViewById(R.id.tvType);
        tvAC = findViewById(R.id.tvAC);
        tvHP = findViewById(R.id.tvHP);
        tvCR = findViewById(R.id.tvCR);
        tvSTR = findViewById(R.id.tvSTR);
        tvDEX = findViewById(R.id.tvDEX);
        tvCON = findViewById(R.id.tvCON);
        tvINT = findViewById(R.id.tvINT);
        tvWIS = findViewById(R.id.tvWIS);
        tvCHA = findViewById(R.id.tvCHA);
        tvSTRMod = findViewById(R.id.tvSTRMod);
        tvDEXMod = findViewById(R.id.tvDEXMod);
        tvCONMod = findViewById(R.id.tvCONMod);
        tvINTMod = findViewById(R.id.tvINTMod);
        tvWISMod = findViewById(R.id.tvWISMod);
        tvCHAMod = findViewById(R.id.tvCHAMod);
        tvExtraStuff = findViewById(R.id.tvExtraStuff);
        tvAttributes = findViewById(R.id.tvAttributes);
        tvSpells = findViewById(R.id.tvSpells);
        tvActions = findViewById(R.id.tvActions);
        tvLegendActions = findViewById(R.id.tvLegendActions);

        Attributes = findViewById(R.id.Attributes);
        Spells = findViewById(R.id.Spells);
        Actions = findViewById(R.id.Actions);
        LegendActions = findViewById(R.id.LegendaryActions);

        btnAddCreature = findViewById(R.id.btnAddCreature);

        int index = 0;
        boolean found;
        String name = getIntent().getStringExtra("name");
        for(int i = 0; i < Globals.allCreatures.size(); i++){
            found = false;
            try{
                found = name.equals(Globals.allCreatures.get(i).getName());
            }
            catch(NullPointerException exception){
                Toast.makeText(CreatureBlock.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if(found){
                index = i;
                break;
            }
        }

        tvCreatureName.setText(Globals.allCreatures.get(index).getName());
        tvType.setText(Globals.allCreatures.get(index).getType());

        tvAC.setText(Globals.allCreatures.get(index).getAC());
        tvHP.setText(Globals.allCreatures.get(index).getHP());
        tvCR.setText(Globals.allCreatures.get(index).getCR());

        String STRText = "" + Globals.allCreatures.get(index).getSTR();
        tvSTR.setText(STRText);
        String DEXText = "" + Globals.allCreatures.get(index).getDEX();
        tvDEX.setText(DEXText);
        String CONText = "" + Globals.allCreatures.get(index).getCON();
        tvCON.setText(CONText);
        String INTText = "" + Globals.allCreatures.get(index).getINT();
        tvINT.setText(INTText);
        String WISText = "" + Globals.allCreatures.get(index).getWIS();
        tvWIS.setText(WISText);
        String CHAText = "" + Globals.allCreatures.get(index).getCHA();
        tvCHA.setText(CHAText);

        String STRModText = "(" + (Globals.allCreatures.get(index).getSTR()/2-5)+ ")";
        tvSTRMod.setText(STRModText);
        String DEXModText = "(" + (Globals.allCreatures.get(index).getDEX()/2-5)+ ")";
        tvDEXMod.setText(DEXModText);
        String CONModText = "(" + (Globals.allCreatures.get(index).getCON()/2-5)+ ")";
        tvCONMod.setText(CONModText);
        String INTModText = "(" + (Globals.allCreatures.get(index).getINT()/2-5)+ ")";
        tvINTMod.setText(INTModText);
        String WISModText = "(" + (Globals.allCreatures.get(index).getWIS()/2-5)+ ")";
        tvWISMod.setText(WISModText);
        String CHAModText = "(" + (Globals.allCreatures.get(index).getCHA()/2-5)+ ")";
        tvCHAMod.setText(CHAModText);

        StringBuilder extraString = new StringBuilder();
        if(Globals.allCreatures.get(index).getSaves()!= null) {
            extraString.append("Saves: ");
            extraString.append(Globals.allCreatures.get(index).getSaves());
            extraString.append("\n");
        }
        if(Globals.allCreatures.get(index).getSkills()!= null) {
            extraString.append("Skills: ");
            extraString.append(Globals.allCreatures.get(index).getSkills());
            extraString.append("\n");
        }
        if(Globals.allCreatures.get(index).getVulnerabilities()!= null) {
            extraString.append("Vulnerabilities: ");
            extraString.append(Globals.allCreatures.get(index).getVulnerabilities());
            extraString.append("\n");
        }
        if(Globals.allCreatures.get(index).getResistances()!= null) {
            extraString.append("Resistances: ");
            extraString.append(Globals.allCreatures.get(index).getResistances());
            extraString.append("\n");
        }
        if(Globals.allCreatures.get(index).getImmunities()!= null) {
            extraString.append("Immunities: ");
            extraString.append(Globals.allCreatures.get(index).getImmunities());
            extraString.append("\n");
        }
        if(Globals.allCreatures.get(index).getConImmunities()!= null) {
            extraString.append("Condition Immunities: ");
            extraString.append(Globals.allCreatures.get(index).getConImmunities());
            extraString.append("\n");
        }
        if(Globals.allCreatures.get(index).getSenses()!= null) {
            extraString.append("Senses: ");
            extraString.append(Globals.allCreatures.get(index).getSenses());
            extraString.append("\n");
        }
        if(Globals.allCreatures.get(index).getLanguages()!= null) {
            extraString.append("Languages: ");
            extraString.append(Globals.allCreatures.get(index).getLanguages());
            extraString.append("\n");
        }
        tvExtraStuff.setText(extraString.toString());

        if(!Globals.allCreatures.get(index).getAttributes().isEmpty()) {
            tvAttributes.setText(Globals.allCreatures.get(index).getAttributes());
            Attributes.setVisibility(View.VISIBLE);
            tvAttributes.setVisibility(View.VISIBLE);
        }
        else{
            Attributes.setVisibility(View.GONE);
            tvAttributes.setVisibility(View.GONE);
        }

        if(!Globals.allCreatures.get(index).getSpells().isEmpty()) {
            Spells.setText(R.string.Spells);
            tvSpells.setText(Globals.allCreatures.get(index).getSpells());
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
        if(!Globals.allCreatures.get(index).getActions().isEmpty()) {
            Actions.setText(R.string.Actions);
            tvActions.setText(Globals.allCreatures.get(index).getActions());
            Actions.setVisibility(View.VISIBLE);
            tvActions.setVisibility(View.VISIBLE);
        }
        else{
            Actions.setVisibility(View.GONE);
            tvActions.setVisibility(View.GONE);
        }

        if(!Globals.allCreatures.get(index).getLegendActions().isEmpty()) {
            LegendActions.setText(R.string.Legendary_Actions);
            tvLegendActions.setText(Globals.allCreatures.get(index).getLegendActions());
            LegendActions.setVisibility(View.VISIBLE);
            tvLegendActions.setVisibility(View.VISIBLE);
        }
        else{
            LegendActions.setVisibility(View.GONE);
            tvLegendActions.setVisibility(View.GONE);
        }

        btnAddCreature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreatureBlock.this, CreatureAdd.class);
                Globals.addName = tvCreatureName.getText().toString();
                startActivity(intent);
            }
        });
    }

    public void startSpells(){
        Intent intent = new Intent(CreatureBlock.this, SpellsActivity.class);
        intent.putExtra("spellAbility", spellAbility);
        intent.putExtra("spellList", creaSpells);
        startActivity(intent);
    }
}
