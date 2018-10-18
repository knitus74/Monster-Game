package com.company;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

public class Monster {
    private HashMap<String, MonsterMove> moveSet = new HashMap();
    private int hpLeft;
    private Random rand;
    private ArrayList<Integer> monsterStatIVs;
    private HashMap<String, Integer> monsterStats = new HashMap();
    private int level;
    private int monsterId;
    private MonsterSpecies species;
    private double accuracy = 1.0;
    private double evasiveness = 1.0;

    public Monster(int level, int monsterId, ArrayList<String> moves){
        this.monsterId = monsterId;
        this.species = DifferentMonsters.getMonsterSpecies().get(monsterId);
        this.level = level;
        monsterStatIVs = new ArrayList();
        rand = new Random();
        //create the new monster's randomly generated IVs, might need to change this later if I implement breeding
        for(int i = 0; i < 7; i++){
            monsterStatIVs.add(rand.nextInt(30) + 1);
        }
        //calculate stats, taking into account the monster's base stats and IVs
        calculateAllStats();
        //set the amount of hp left to the full amount the monster can have
        this.hpLeft = monsterStats.get("hp");
        for(String move: moves){
            moveSet.put(move, DifferentMonsterMoves.getAllMonsterMoves().get(move));
        }
    }

    public MonsterSpecies getSpecies(){
        return species;
    }

    public HashMap<String, Integer> getMonsterStats(){
        return monsterStats;
    }

    public String getName() { return species.getName();}

    public String getType1(){
        return species.getType1();
    }

    public String getType2(){
        return species.getType2();
    }

    public int getLevel(){
        return level;
    }

    public double getAccuracy(){
        return accuracy;
    }

    public double getEvasiveness(){
        return evasiveness;
    }

    public double calculateHPStat(){
        double baseHP = species.getBaseStats().get("baseHP");
        return (Math.floor( ((2 * baseHP) + monsterStatIVs.get(0)) * level/100 ) + level + 10);
    }

    public double calculateStat(int statType){
        String statName = "";
        switch(statType){
            case 1:
                    statName = "baseAttack";
                    break;
            case 2:
                    statName = "baseDefence";
                    break;
            case 3:
                    statName = "baseSpcAttack";
                    break;
            case 4: statName = "baseSpcDefence";
                    break;
            case 5:
                    statName = "baseSpeed";
                    break;
        }
        double baseStat = species.getBaseStats().get(statName);
        return (Math.floor( ((2*baseStat) + monsterStatIVs.get(statType)) * level/100)) + 5;
    }

    private void calculateAllStats(){
        monsterStats.put("hp",(int)calculateHPStat());
        monsterStats.put("attack", (int)calculateStat(1));
        monsterStats.put("defence", (int)calculateStat(2));
        monsterStats.put("spcAttack", (int)calculateStat(3));
        monsterStats.put("spcDefence", (int)calculateStat(4));
        monsterStats.put("speed", (int)calculateStat(5));
    }

    //function that alters the hp left on the monster, based on whether it was healed/took damage
    public void changeHP(int amount, boolean isDamage, boolean isFullRestore){
        if(isDamage){
            hpLeft -= amount;
            //don't let the hp reach negative values when the monster takes damage
            if(hpLeft < 0){
                hpLeft = 0;
            }
        }
        //gives the monster a full restore
        else if(isFullRestore && !isDamage){
            hpLeft = monsterStats.get("hp");
        }
        else{
            hpLeft += amount;
            //don't let the monster get more hp than its total hp, if it is healed
            if(hpLeft > monsterStats.get("hp")){
                hpLeft = monsterStats.get("hp");
            }
        }
    }

    public HashMap<String, MonsterMove> getMoveSet(){
        return moveSet;
    }

    public int getHPLeft(){
        return hpLeft;
    }

}
