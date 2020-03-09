package com.example.offlineencountertracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.offlineencountertracker.Creatures.CreaItem;
import com.example.offlineencountertracker.Creatures.CreaturesActivity;
import com.example.offlineencountertracker.EncounterTracker.EncTrackActivity;
import com.example.offlineencountertracker.RV.MyAdapter;
import com.example.offlineencountertracker.RV.RVItem;
import com.example.offlineencountertracker.Room.AsyncCb;
import com.example.offlineencountertracker.Room.EncCreaItem;
import com.example.offlineencountertracker.Room.EncItem;
import com.example.offlineencountertracker.RoomFunc.GetAllEnc;
import com.example.offlineencountertracker.RoomFunc.GetAllEncCrea;
import com.example.offlineencountertracker.RoomFunc.Insert;
import com.example.offlineencountertracker.Spells.SpellItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClicked{

    RecyclerView rvList;
    RecyclerView.Adapter itemAdapter;
    RecyclerView.LayoutManager layoutManager;

    Button btnCreatures, btnAdd;
    EditText etNewEnc;

    @Override
    protected void onResume(){
        super.onResume();
        populateRec();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvList = findViewById(R.id.rvEncounters);
        rvList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);

        btnCreatures = findViewById(R.id.btnCreatures);
        btnAdd = findViewById(R.id.btnAdd);

        btnCreatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreaturesActivity.class));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("Enter your encounter name!");

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_encounter, null);

            dialog.setView(dialogView);
            etNewEnc = dialogView.findViewById(R.id.etNewEncounter);

            dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (etNewEnc.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please enter an encounter name!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        EncItem newEnc = new EncItem();
                        newEnc.setName(etNewEnc.getText().toString().trim());
                        newEnc.setQ(0);
                        new Insert(newEnc, getApplicationContext(), new AsyncCb<EncItem>() {
                            @Override
                            public void handleResponse(EncItem object) {
                                Globals.encounters.add(object);
                                Toast.makeText(MainActivity.this, "Encounter was created!", Toast.LENGTH_SHORT).show();
                                populateRec();
                            }

                            @Override
                            public void handleFault(Exception e) {
                                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).execute();
                    }
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this,"Cancelled!", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();
            }
        });

        populateEnc();
        try {
            populateCrea();
            populateSpells();
        }
        catch(Exception e){
            Toast.makeText(MainActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void populateEnc(){
        new GetAllEnc(getApplicationContext(), new AsyncCb<List<EncItem>>() {
            @Override
            public void handleResponse(List<EncItem> object) {
                Globals.encounters = object;
                populateRec();
            }

            @Override
            public void handleFault(Exception e) {
                Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).execute();

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

    public void populateCrea() throws XmlPullParserException, IOException {

        XmlResourceParser xml = getResources().getXml(R.xml.bestiary);
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);

        int eventType = xml.getEventType();
        String name="none", alignment="none", ac="none", hp="none", speed="none", save="none", resist="none", skill="none", vuln="none", imm="", conImm="none", passive="none", lang="none", cr="none";
        int str=-1, dex=-1, con=-1, intel=-1, wis=-1, cha=-1;
        String size = "", type = "", sense = "";
        boolean flagT = false, flagA = false, flagR = false, flagS = false, flagL = false;
        String t = "", a = "", s = "", l = "";
        String[] spellList = {};
        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG){
                switch(xml.getName()) {
                    case "compendium":
                        //Do nothing
                        break;
                    case "monster":
                        size = "";type = "";sense = "";
                        t = "";s = "";a = "";l = "";
                        save="none";resist="none";skill="none";vuln="none";imm="none";conImm="none";passive="none";lang="none";
                        break;
                    case "name":
                        xml.next();
                        if (flagT) {
                            if (xml.getText().equals("Spellcasting")){
                                flagS = true;
                                flagT = false;
                            }
                            else{
                                t = t + xml.getText() + ": ";
                            }
                        }
                        else if (flagA) {
                            a = a + xml.getText() + ": ";
                        }
                        else if (flagR){
                            a = a + xml.getText() + " (Reaction): ";
                        }
                        else if (flagL) {
                            l = l + xml.getText() + ": ";
                        }
                        else {
                            name = xml.getText();
                        }
                        break;
                    case "text":
                        xml.next();
                        if (flagT) {
                            t = t + xml.getText() + " \n";
                        }
                        else if (flagS) {
                            s = s + xml.getText() + " \n";
                        }
                        else if (flagA) {
                            a = a + xml.getText() + " \n";
                        }
                        else if (flagR) {
                            a = a + xml.getText() + " \n";
                        }
                        else if (flagL) {
                            l = l + xml.getText() + " \n";
                        }
                        else {
                            throw new XmlPullParserException("Text error!");
                        }
                        break;
                    case "size":
                        xml.next();
                        switch (xml.getText()) {
                            case ("T"):
                                size = "Tiny ";
                                break;
                            case ("S"):
                                size = "Small ";
                                break;
                            case ("M"):
                                size = "Medium ";
                                break;
                            case ("L"):
                                size = "Large ";
                                break;
                            case ("H"):
                                size = "Huge ";
                                break;
                            case ("G"):
                                size = "Gargantuan ";
                                break;
                            default:
                                throw new XmlPullParserException("Error in size!");
                        }
                        break;
                    case "type":
                        xml.next();
                        type = xml.getText().split(",")[0];
                        break;
                    case "alignment":
                        xml.next();
                        alignment = size + type + ", " + xml.getText();
                        break;
                    case "ac":
                        xml.next();
                        ac = xml.getText();
                        break;
                    case "hp":
                        xml.next();
                       hp = xml.getText();
                        break;
                    case "speed":
                        xml.next();
                        speed = xml.getText();
                        break;
                    case "str":
                        xml.next();
                        str = Integer.parseInt(xml.getText());
                        break;
                    case "dex":
                        xml.next();
                        dex = Integer.parseInt(xml.getText());
                        break;
                    case "con":
                        xml.next();
                        con = Integer.parseInt(xml.getText());
                        break;
                    case "int":
                        xml.next();
                       intel = Integer.parseInt(xml.getText());
                        break;
                    case "wis":
                        xml.next();
                        wis = Integer.parseInt(xml.getText());
                        break;
                    case "cha":
                        xml.next();
                        cha = Integer.parseInt(xml.getText());
                        break;
                    case "save":
                        xml.next();
                        if(!xml.getText().equals("")){
                           save = xml.getText();
                        }
                        break;
                    case "skill":
                        xml.next();
                        if(!xml.getText().equals("")){
                            skill = xml.getText();
                        }
                        break;
                    case "resist":
                        xml.next();
                        if(!xml.getText().equals("")){
                            resist = xml.getText();
                        }
                        break;
                    case "vulnerable":
                        xml.next();
                        if(!xml.getText().equals("")){
                           vuln = xml.getText();
                        }
                        break;
                    case "immune":
                        xml.next();
                        if(!xml.getText().equals("")){
                            imm = xml.getText();
                        }
                        break;
                    case "conditionImmune":
                        xml.next();
                        if(!xml.getText().equals("")){
                            conImm = xml.getText();
                        }
                        break;
                    case "senses":
                        xml.next();
                        sense = xml.getText() + ", ";
                    case "passive":
                        xml.next();
                        sense = sense + "passive perception" + xml.getText();
                        break;
                    case "languages":
                        xml.next();
                        if(!xml.getText().equals("")){
                            lang = xml.getText();
                        }
                        break;
                    case "cr":
                        xml.next();
                        cr = xml.getText();
                        break;
                    case "trait":
                        flagT = true;
                        break;
                    case "action":
                        flagA = true;
                        break;
                    case "reaction":
                        flagR = true;
                        break;
                    case "legendary":
                        flagL = true;
                        break;
                    case "spells":
                        xml.next();
                        spellList = xml.getText().trim().split(",");
                        break;
                    case "attack":
                        //Do nothing
                    case "slots":
                        //Do nothing
                        break;
                    case "description":
                        //Do nothing
                        break;
                    default:
                        throw new XmlPullParserException("Start tag not found");
                }
            }
            else if(eventType == XmlPullParser.END_TAG){
                switch (xml.getName()){
                    case "trait":
                        if(flagS){
                            s = s + "\n";
                            flagS = false;
                        }
                        else if(flagT){
                            t = t + "\n";
                            flagT = false;
                        }
                        else{
                            throw new XmlPullParserException("Trait flag error!");
                        }
                        break;
                    case "action":
                        a = a + "\n";
                        flagA = false;
                        break;
                    case "reaction":
                        a = a + "\n";
                        flagR = false;
                        break;
                    case "legendary":
                        l = l + "\n";
                        flagL = false;
                        break;
                    case "monster":
                        CreaItem crea = new CreaItem();
                        crea.setName(name);
                        crea.setType(alignment);
                        crea.setAC(ac);
                        crea.setHP(hp);
                        crea.setSpd(speed);
                        crea.setSaves(save);
                        crea.setSkills(skill);
                        crea.setResistances(resist);
                        crea.setVulnerabilities(vuln);
                        crea.setImmunities(imm);
                        crea.setConImmunities(conImm);
                        crea.setSenses(passive);
                        crea.setLanguages(lang);
                        crea.setCR(cr);
                        crea.setSTR(str);
                        crea.setDEX(dex);
                        crea.setCON(con);
                        crea.setINT(intel);
                        crea.setWIS(wis);
                        crea.setCHA(cha);
                        crea.setAttributes(t);
                        crea.setSpells(s);
                        crea.setActions(a);
                        crea.setLegendActions(l);
                        crea.setSpellList(spellList);
                        Globals.allCreatures.add(crea);
                        break;
                    default:
                        //Do nothing
                }
            }
            eventType = xml.next();
        }
    }

    public void populateSpells () throws XmlPullParserException, IOException {
        XmlResourceParser xml = getResources().getXml(R.xml.spells);
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);

        int eventType = xml.getEventType();

        String name = "", level = "", school = "", ritual = "", time = "", range = "", comp = "", duration = "", classes = "", description = "";
        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG){
                switch(xml.getName()) {
                    case "compendium":
                        //Do nothing
                        break;
                    case "spell":
                        name = ""; level = ""; school = ""; ritual = ""; time = ""; range = ""; comp = ""; duration = ""; classes = ""; description = "";
                        break;
                    case "name":
                        xml.next();
                        name = xml.getText();
                        break;
                    case "level":
                        xml.next();
                        if(xml.getText().equals("0")){
                            level = "Cantrip";
                        }
                        else {
                            level = xml.getText();
                        }
                        break;
                    case "school":
                        xml.next();
                        String sch = xml.getText();
                        switch(sch){
                            case "A":
                                school = "Abjuration";
                                break;
                            case "C":
                                school = "Conjuration";
                                break;
                            case "D":
                                school = "Divination";
                                break;
                            case "EN":
                                school = "Enchantment";
                                break;
                            case "EV":
                                school = "Evocation";
                                break;
                            case "I":
                                school = "Illusion";
                                break;
                            case "N":
                                school = "Necromancy";
                                break;
                            case "T":
                                school = "Transmutation";
                                break;
                            default:
                                throw new XmlPullParserException("Error in school!");
                        }
                        break;
                    case "ritual":
                        xml.next();
                        ritual = xml.getText();
                        break;
                    case "time":
                        xml.next();
                        time = xml.getText();
                        break;
                    case "range":
                        xml.next();
                        range = xml.getText();
                        break;
                    case "components":
                        xml.next();
                        comp = xml.getText();
                        break;
                    case "duration":
                        xml.next();
                        duration = xml.getText();
                        break;
                    case "classes":
                        xml.next();
                        classes = xml.getText();
                        break;
                    case "text":
                        xml.next();
                        description = description + xml.getText();
                        break;
                    case "roll":
                        //Do nothing
                        break;
                    default:
                        throw new XmlPullParserException("Start tag not found");
                }
            }
            else if(eventType == XmlPullParser.END_TAG){
                switch (xml.getName()){
                    case "spell":
                        SpellItem spell = new SpellItem();
                        Globals.allSpells.add(spell);
                        spell.setName(name);
                        spell.setLevel(level);
                        spell.setSchool(school);
                        spell.setRitual(ritual);
                        spell.setTime(time);
                        spell.setRange(range);
                        spell.setComp(comp);
                        spell.setDuration(duration);
                        spell.setClasses(classes);
                        spell.setDescription(description);
                }
            }
            eventType = xml.next();
        }
    }

    public void populateRec(){
        List<RVItem> rvTempList = new ArrayList<>();
        for (int i = 0; i < Globals.encounters.size(); i++) {
            RVItem newItem = new RVItem();
            newItem.setTitle(Globals.encounters.get(i).getName());
            newItem.setDesc("Total Creatures: " + Globals.encounters.get(i).getQ());
            rvTempList.add(newItem);
        }
        itemAdapter = new MyAdapter(MainActivity.this, rvTempList);
        rvList.setAdapter(itemAdapter);
    }

    @Override
    public void onItemClicked(int index) {
        Intent intent = new Intent(MainActivity.this, EncTrackActivity.class);
        Globals.etCurEnc = Globals.encounters.get(index).getName();
        startActivity(intent);
    }
}
