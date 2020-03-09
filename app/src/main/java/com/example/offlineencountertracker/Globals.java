package com.example.offlineencountertracker;

import android.app.Application;

import com.example.offlineencountertracker.Creatures.CreaItem;
import com.example.offlineencountertracker.Room.EncCreaItem;
import com.example.offlineencountertracker.Room.EncItem;
import com.example.offlineencountertracker.Spells.SpellItem;

import java.util.ArrayList;
import java.util.List;

public class Globals extends Application {
    public static List<EncItem> encounters = new ArrayList<>();
    public static List<EncCreaItem> encCreatures = new ArrayList<>();
    public static ArrayList<CreaItem> allCreatures = new ArrayList<>();
    public static ArrayList<SpellItem> allSpells = new ArrayList<>();
    public static String etCurEnc;
    public static String addName;

}
