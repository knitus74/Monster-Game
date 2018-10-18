package com.company;

import java.util.HashMap;

public class DifferentMonsters {

    private static final HashMap<Integer, MonsterSpecies> monsters = new HashMap(){{
        put(3, new MonsterSpecies("VENUSAUR", "GRASS", "POISON", 80, 82, 83, 100, 100, 80, 130, 4, 61, 58, 128, 7, 64, 50));
        put(6, new MonsterSpecies("CHARIZARD", "FIRE", "FLYING", 78, 84, 78, 109, 85, 100, 319, 0, 66, 63, 319, 4, 66, 57));
        put(9, new MonsterSpecies("BLASTOISE", "WATER", "", 79, 83, 100, 85, 105, 78, 511, 8, 63, 56, 511, 6, 65, 52));
        put(10, new MonsterSpecies("Anoleaf", "GRASS", "FIGHTING", 79, 83, 100, 85, 105, 78, 17, 25, 30, 33, 11, 8, 39, 48));
        put(11, new MonsterSpecies("Aardorn", "NORMAL", "", 78, 84, 78, 109, 85, 100, 11, 16, 47, 36, 0, 11, 64, 53));
    }};

    public static HashMap<Integer, MonsterSpecies> getMonsterSpecies(){
        return monsters;
    }
}
