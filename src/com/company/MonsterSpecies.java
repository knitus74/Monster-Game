package com.company;

import java.util.HashMap;

public class MonsterSpecies {

    private String name;
    private String type1;
    private String type2;
    private HashMap<String, Double> baseStats;
    private HashMap<String, Double> attackingMonsterImage;
    private HashMap<String, Double> defendingMonsterImage;

    public MonsterSpecies(String name, String type1, String type2, double totalHP, double attack, double defence, double spcAttack, double spcDefence, double speed, double attackingX, double attackingY, double attackingWidth, double attackingHeight, double defendingX, double defendingY, double defendingWidth, double defendingHeight){
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        baseStats = new HashMap();
        attackingMonsterImage = new HashMap();
        defendingMonsterImage = new HashMap();
        baseStats.put("baseHP", totalHP);
        baseStats.put("baseAttack", attack);
        baseStats.put("baseDefence", defence);
        baseStats.put("baseSpcAttack", spcAttack);
        baseStats.put("baseSpcDefence", spcDefence);
        baseStats.put("baseSpeed", speed);
        attackingMonsterImage.put("x", attackingX);
        attackingMonsterImage.put("y", attackingY);
        attackingMonsterImage.put("width", attackingWidth);
        attackingMonsterImage.put("height", attackingHeight);
        defendingMonsterImage.put("x", defendingX);
        defendingMonsterImage.put("y", defendingY);
        defendingMonsterImage.put("width", defendingWidth);
        defendingMonsterImage.put("height", defendingHeight);
    }

    public String getName(){
        return name;
    }

    public String getType1(){
        return type1;
    }

    public String getType2(){
        return type2;
    }

    public HashMap<String, Double> getBaseStats(){
        return baseStats;
    }

    public HashMap<String, Double> getDefendingMonsterImage(){
        return defendingMonsterImage;
    }

    public HashMap<String, Double> getAttackingMonsterImage(){
        return attackingMonsterImage;
    }
}
