package main;

import structures.Vector2;

public class AtkCard extends Card
{
    public AtkCard(){
        setDamage(5);
        setDur(3);
        setHudStyle();
    }
    public AtkCard(int damage, int dur){
        setDamage(damage);
        setDur(dur);
        setHudStyle();
    }
    public AtkCard(Vector2 pos, int damage, int dur){
        this(damage, dur);
        this.setPosition(new Vector2(pos));
    }
    public AtkCard(Vector2 pos){
        this();
        this.setPosition(new Vector2(pos));
    }
    
    private void setHudStyle(){
        String dmgString = (value<10) ? ("#   "+value+"   #"):("#  "+value+"   #");
        String[] s = {"#########",
                      "#       #",
                      "#  ATK  #",
                      dmgString,
                      "#       #",
                      "#########"};
        this.HudStyle = s;
        this.setStyle('A');
    }
    
    public int getDamage(){
        return value;
    }
    public void setDamage(int newDamage){
        this.value = newDamage;
    }
}
