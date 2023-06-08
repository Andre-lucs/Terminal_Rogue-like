package main;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;

import structures.Vector2;
import structures.GameMap;
import structures.Status;

public class Actor extends Entity
{
    protected int life;
    protected Map<String, Integer> attributes;
    protected Status status;
    private Random gen;

    public Actor(int atk, int def, char style)
    {
        super();
        attributes = new HashMap<>();
        attributes.put("ATK", atk);
        attributes.put("DEF", def);
        attributes.put("MHP", 0);
        this.Style = style;
        status = Status.ALIVE;
        gen = new Random();
    }

    public int getLife(){
        return this.life;
    }

    public Status getStatus(){
        return status;
    }

    public int getAtk(){
        return this.attributes.get("ATK");
    }
    public int getDef(){
        return this.attributes.get("DEF");
    }
    public void setAtk(int atk){
        this.attributes.put("ATK", atk);
    }
    public void setDef(int def){
        this.attributes.put("DEF", def);
    }

    public void increaseMaxLife(int amount){
        if(amount < 0){
            decreaseMaxLife(amount*-1);
            return;
        }
        this.attributes.put("MHP", attributes.get("MHP") + amount);
        recoverLife(amount);
    }

    public void decreaseMaxLife(int amount){
        this.attributes.put("MHP", attributes.get("MHP") - amount);
        if(life > attributes.get("MHP")) life = attributes.get("MHP");
    }

    public void recoverLife(int recoverAmount){
        life += recoverAmount;
        if(life > attributes.get("MHP")) life = attributes.get("MHP");
    }

    public boolean takeHit(int rawDamage){
        if(gen.nextDouble() > this.attributes.get("DEF")/100){
            life -= rawDamage;
            if (life <= 0){
                status = Status.DEAD;
            }
            return true;
        }
        return false;
    }

    public void move(int xSpd, int ySpd, GameMap map){
        Vector2 newPos = new Vector2(getPosition());
        newPos.x += xSpd;
        newPos.y += ySpd;
        if(map.canMove(newPos)){
            Position = newPos;
            map.Update();
        }
    }
    public void move(Vector2 spd, GameMap map){
        move(spd.x, spd.y, map);
    }
}
