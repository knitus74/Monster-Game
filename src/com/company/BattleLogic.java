package com.company;

import java.util.ArrayList;
import java.util.Random;

public class BattleLogic {
    private int numberOfDefenderMonsters;
    private int numberOfAIMonsters;

    TypeEffectivenessTable typeEffectivenessTable = Main.typeEffectivenessTable;
    double typeEffectivenessAgainstPlayer = 0.0;
    double getTypeEffectivenessAgainstAI = 0.0;
    private Monster attacker;
    private Monster defender;
    private Random tieBreaker = new Random();
    private MonsterMove defenderMove;
    private MonsterMove attackerMove;
    private BattleDialogue textController;


    public BattleLogic(Monster attacker, Monster defender, BattleDialogue textController, int numberOfAIMonsters, int numberOfDefenderMonsters){
        this.attacker = attacker;
        this.numberOfAIMonsters = numberOfAIMonsters;
        this.numberOfDefenderMonsters = numberOfDefenderMonsters;
        this.defender = defender;
        this.textController = textController;
    }

    public void setAttacker(Monster attacker){
        this.attacker = attacker;
    }

    public void setDefender(Monster defender){
        this.defender = defender;
    }

    //returns true if defender goes first
    private boolean getOrderOfAttacks(boolean isAttackerUseItem, boolean isDefenderUseItem){
        if(isDefenderUseItem){
            return true;
        }
        else if(isAttackerUseItem && !isDefenderUseItem){
            return false;
        }
        else{
            int attackerSpeed = attacker.getMonsterStats().get("speed");
            int defenderSpeed = defender.getMonsterStats().get("speed");
            if(defenderSpeed > attackerSpeed){
                return true;
            }
            else if(attackerSpeed > defenderSpeed){
                return false;
            }
            else{
                if(tieBreaker.nextInt(1) == 1){return true;}
                else{return false;}
            }
        }
    }

    public void doTurn(boolean isDefenderUseItem, MonsterMove defenderMove){
        try{
            this.defenderMove = defenderMove;
            //need to make AI better than just choosing first move
            boolean isAttackerUseItem = simpleAI();
            boolean isDefenderFirst = getOrderOfAttacks(isAttackerUseItem, isDefenderUseItem);

            if(defenderMove == null && !isDefenderUseItem){
                System.out.println("Null happens");
                boolean attackerHits = calculateIfHit(attackerMove, attacker, defender);
                int damageFromAttacker = calculateDamage(attacker, defender, attackerMove, true);
                inflictDamage(false, typeEffectivenessAgainstPlayer, attackerHits, damageFromAttacker, attacker, defender, true, false, attackerMove);
            }
            else {
                boolean defenderHits = calculateIfHit(defenderMove, defender, attacker);
                boolean attackerHits = calculateIfHit(attackerMove, attacker, defender);
                int damageFromDefender = calculateDamage(defender, attacker, defenderMove, false);
                int damageFromAttacker = calculateDamage(attacker, defender, attackerMove, true);

                if (isDefenderFirst) {
                    inflictDamage(true, getTypeEffectivenessAgainstAI, defenderHits, damageFromDefender, defender, attacker, true, false, defenderMove);
                    if (attacker.getHPLeft() != 0) {
                        inflictDamage(false, typeEffectivenessAgainstPlayer, attackerHits, damageFromAttacker, attacker, defender, true, false, attackerMove);
                    }
                } else {
                    inflictDamage(false, typeEffectivenessAgainstPlayer, attackerHits, damageFromAttacker, attacker, defender, true, false, attackerMove);
                    if (defender.getHPLeft() != 0) {
                        inflictDamage(true, getTypeEffectivenessAgainstAI, defenderHits, damageFromDefender, defender, attacker, true, false, defenderMove);
                    }
                }
            }
            textController.doTurnDialogue();
            textController.addToTextAndActionSequence("", "battleOptions");
            textController.slowText();
        }
        catch (NullPointerException e){
            e.printStackTrace();
            //in case defenderMove not specified
        }
    }


    //returns true if attacker uses an item
    private boolean simpleAI(){
        Object[] monsterMovesObjectArr = attacker.getMoveSet().keySet().toArray();
        ArrayList<String> monsterMoves = new ArrayList();
        for(Object monsterMove: monsterMovesObjectArr){
            monsterMoves.add((String)monsterMove);
        }
        this.attackerMove = attacker.getMoveSet().get(monsterMoves.get(0));
        return false;
    }

    public boolean calculateIfHit(MonsterMove move, Monster attacking, Monster defending){
        //change this
        double accuracyRatio = Math.ceil(move.getAccuracy() * (attacking.getAccuracy()/ defending.getEvasiveness()));
        int accuracyProb = tieBreaker.nextInt(100) + 1;
        if(accuracyRatio >= accuracyProb){
            return true;
        }
        else{
            return false;
        }
    }

    private int calculateDamage(Monster attackingMonster, Monster defendingMonster, MonsterMove attack, boolean isAI){
        int attackingMonsterLevel = attackingMonster.getLevel();
        String attackType = attack.getType();
        double attackMovePower = attack.getPower();
        double attackPower;
        double defencePower;
        if(attack.isPhysical()){
            attackPower = attackingMonster.getMonsterStats().get("attack");
            defencePower = attackingMonster.getMonsterStats().get("defence");
        }
        else{
            attackPower = attackingMonster.getMonsterStats().get("spcAttack");
            defencePower = attackingMonster.getMonsterStats().get("spcDefence");
        }
        //multiplier for whether the move is super effective, not very effective, etc
        double typeEffectiveness = getTypeEffectiveness(defendingMonster, attack);
        if(isAI){
             typeEffectivenessAgainstPlayer = typeEffectiveness;
        }
        else{
            getTypeEffectivenessAgainstAI = typeEffectiveness;
        }
        //if the attacking move is the same as the attacking monster's type, it should be more powerful
        String attackingMonsterType1 = attackingMonster.getType1();
        String attackingMonsterType2 = attackingMonster.getType2();
        double stabMultiplier = 1;
        if(attackType.equals(attackingMonsterType1) || attackType.equals(attackingMonsterType2)){
            stabMultiplier = 1.5;
        }
        //add an element of randomness to the attack power, to keep it interesting
        double randomMultiplier = (tieBreaker.nextDouble() * (1-0.85)) + 0.85;
        //combine all the damage multipliers together
        double modifier = typeEffectiveness * stabMultiplier * randomMultiplier;
        //calculate the total damage
        int damage = (int)Math.ceil( (((2*attackingMonsterLevel/5) * attackMovePower * attackPower/defencePower)/50 + 2)* modifier);
        return damage;
    }

    private double getTypeEffectiveness(Monster defendingMonster, MonsterMove attack){
        String monsterType1 = defendingMonster.getType1();
        String monsterType2 = defendingMonster.getType2();
        String attackType = attack.getType();
        double multiplier1 = typeEffectivenessTable.getTableOfTypes().get(attackType).get(monsterType1);
        double multiplier2;
        if(!monsterType2.equals("")){
            multiplier2 = typeEffectivenessTable.getTableOfTypes().get(attackType).get(monsterType2);
        }
        else{
            multiplier2 = 1.0;
        }
        return (multiplier1 * multiplier2);
    }

    private void inflictDamage(boolean hitsAI, double typeEffectiveness, boolean doesItHit, int damage, Monster didTheMove, Monster gettingHit, boolean isDamage, boolean isFullRestore, MonsterMove moveUsed){
        if(doesItHit){
            gettingHit.changeHP(damage, isDamage, isFullRestore);
            setAttackingDialogue(didTheMove, moveUsed, hitsAI);
            setMoveEffectivenessDialogue(typeEffectiveness);
            if(gettingHit.getHPLeft() == 0){
                setFaintedDialogue(hitsAI, gettingHit);
                if(hitsAI){
                    numberOfAIMonsters -= 1;
                    if(numberOfAIMonsters == 0){
                        textController.addToTextAndActionSequence("", "resumeWalking");
                    }
                }
                else{
                    numberOfDefenderMonsters -= 1;
                    if(numberOfDefenderMonsters == 0){
                        textController.addToTextAndActionSequence("You lost bitch", "resumeWalking");
                    }
                }
            }
        }
        else{
            setAttackMissedDialogue(didTheMove);
        }
    }

    private void setMoveEffectivenessDialogue(double typeEffectiveness){
        if(typeEffectiveness == 2.0){
            textController.addToTextAndActionSequence("It was super-effective", null);
        }
        else if(typeEffectiveness > 2.0){
            textController.addToTextAndActionSequence("It was extremely effective", null);
        }
        else if(typeEffectiveness < 1.0){
            textController.addToTextAndActionSequence("It was not very effective", null);
        }
    }

    private void setAttackingDialogue(Monster userOfMove, MonsterMove moveUsed, boolean isAI){
        textController.setHealthBarController(isAI);
        textController.addToTextAndActionSequence(userOfMove.getName() + " used " + moveUsed.getName(), "changeHealth");
    }

    private void setAttackMissedDialogue(Monster userOfMove){
        textController.addToTextAndActionSequence(userOfMove.getName() + "'s attack missed", null);
    }

    private void setFaintedDialogue(boolean isAI, Monster fainted){
        textController.setMonsterToAnimate(isAI);
        textController.addToTextAndActionSequence(fainted.getName() + " fainted", "faintMonster");
    }

    public void changeMonster(Monster selectedMonster){
        textController.addToTextAndActionSequence("That's enough " + defender.getName() + ", you suck", "dropDefendingMonster");
        textController.addToTextAndActionSequence("Finish them off " + selectedMonster.getName(), "monsterFromTheSide");
        textController.addToTextAndActionSequence("", null);
        defender = selectedMonster;
        //basically doTurn(false, null); as soon as the new monster comes on screen, that way the right healthbarcontroller is used
        textController.setTurnOnHold();
        textController.setTextStream();
        textController.doTurnDialogue();
    }

}
