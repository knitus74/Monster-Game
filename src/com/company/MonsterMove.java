package com.company;

public class MonsterMove {
    private final int power;
    private final String type;
    private final int accuracy;
    private final boolean isPhysical;
    private final int totalPP;
    private final String name;


    public MonsterMove(int power, String type, int accuracy, boolean isPhysical, int totalPP, String name){
        this.power = power;
        this.type = type;
        this.accuracy = accuracy;
        this.isPhysical = isPhysical;
        this.totalPP = totalPP;
        this.name = name;
    }

    public int getPower(){
        return power;
    }

    public String getType(){
        return type;
    }

    public int getAccuracy(){
        return accuracy;
    }

    public boolean isPhysical(){
        return isPhysical;
    }

    public String getName(){return name;}
}
