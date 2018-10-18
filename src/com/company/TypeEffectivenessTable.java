package com.company;
import java.util.HashMap;

//static lookup table to see type weaknesses. It is a hashmap, taking the String value of the move type, and outputting another hashmap.
//when you look up the type of the monster getting hit by the move in the second hashmap, it returns an integer value with the weakness/strength multiplier

public class TypeEffectivenessTable {

    private static HashMap<String, HashMap<String, Double>> tableOfTypes;

    public TypeEffectivenessTable(){
        String[] types = new String[]{"FIRE", "WATER", "GRASS", "NORMAL", "FIGHTING", "POISON", "ROCK"};
        tableOfTypes = new HashMap<>();
        //set every type weakness to a default of 1. Just saves typing out
        for(String type: types){
            tableOfTypes.put(type, new HashMap<>());
            for(String typeAgain: types){
                tableOfTypes.get(type).put(typeAgain, 1.0);
            }
        }
        //now we start adding the super-effectivenesses and not very effectivenesses
        HashMap<String, Double> effects;
        //for FIRE
        effects = tableOfTypes.get("FIRE");
        effects.replace("FIRE", 0.5);
        effects.replace("WATER", 0.5);
        effects.replace("ROCK", 0.5);
        effects.replace("GRASS", 2.0);
        //for WATER
        effects = tableOfTypes.get("WATER");
        effects.replace("WATER", 0.5);
        effects.replace("GRASS", 0.5);
        effects.replace("FIRE", 2.0);
        //for GRASS
        effects = tableOfTypes.get("GRASS");
        effects.replace("FIRE", 0.5);
        effects.replace("WATER", 2.0);
        //for NORMAL
        effects = tableOfTypes.get("NORMAL");
        effects.replace("ROCK", 0.5);
        //for FIGHTING
        effects = tableOfTypes.get("FIGHTING");
        effects.replace("ROCK", 2.0);
        effects.replace("NORMAL", 2.0);
    }

    public static HashMap<String, HashMap<String, Double>> getTableOfTypes(){
        return tableOfTypes;
    }
}
