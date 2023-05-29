package main;

import java.util.Random;

import structures.Vector2;
import structures.GameMap;
import structures.Status;

public class Actor extends Entity
{
    protected int life;
    protected int MaxLife;
    protected int Atk;
    protected int Def;
    protected Status status;
    private Random gen;
    
    public Actor(int atk, int def, char style)
    {
        super();
        this.Atk = atk;
        this.Def = def;
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
        return this.Atk;
    }
    public int getDef(){
        return this.Def;
    }
    public void setAtk(int atk){
        this.Atk = atk;
    }
    public void setDef(int def){
        this.Def = def;
    }
    
    public void increaseMaxLife(int amount){
        MaxLife += amount;
        recoverLife(amount);
    }
    
    public void recoverLife(int recoverAmount){
        life += recoverAmount;
        if(life > MaxLife) life = MaxLife;
    }
    
    public boolean attack(int damage, int dirX, int dirY, GameMap map){
        Entity ent = map.getCell(Position.x+dirX, Position.y+dirY);
        if(ent instanceof Enemy){
            Enemy en = (Enemy) ent;
            if(this instanceof Player){
                if(en.player == null) en.player = (Player) this;
            }
            if(en.takeHit(damage)){
                if(en.status == Status.DEAD){
                    map.Remove(en);
                    en.player = null;
                }
                return true;
            }
        }
        return false;
    }
    public boolean attack(int damage, Vector2 dir, GameMap map){
        return attack(damage, dir.x, dir.y, map);
    }

    public boolean takeHit(int rawDamage){
        if(gen.nextDouble() > this.Def/100){
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
