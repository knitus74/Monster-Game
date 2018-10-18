package com.company;

import java.util.HashMap;

public class DifferentMonsterMoves {

    private static final HashMap<String, MonsterMove> monsterMoves = new HashMap(){{
        put("nibble", new MonsterMove(40, "NORMAL", 100, true, 35, "nibble"));
        put("bitch-slap", new MonsterMove(80, "NORMAL", 75, true, 20, "bitch-slap"));
        put("water-boi", new MonsterMove(40, "WATER", 100, false, 25, "water-boi"));
        put("flamer-boi", new MonsterMove(70, "FIRE", 50, false, 15, "flamer-boi"));
    }};

    public static HashMap<String, MonsterMove> getAllMonsterMoves(){
        return monsterMoves;
    }

}
