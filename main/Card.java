package main;

import structures.Vector2;
import structures.GameMap;

public class Card extends Item
{
    public String[] HudStyle = {"#########",
                                "#       #",
                                "# CARD  #",
                                "#       #",
                                "#       #",
                                "#########"};

    protected int durability;
    protected int uses;
    protected String type;
    protected String Desc;
    //se for carta de defesa
    protected int time;

    protected Card(){}

    public Card(String Type){ //gerar carta padrao
        this.type = Type;
        switch(type){
            case "ATK":
                setValue(5);
            break;
            case "DEF":
                setValue(50);
            break;
        }
        setDur(3);
        setHudStyle();
        time = (Type == "DEF") ? 3 : 0;
    }
    protected Card(String Type, int value, int dur){//gerar carta customizada sem posicao
        type = Type;
        setValue(value);
        setDur(dur);
        setHudStyle();
    }

    public static Card CreatePunchATK(){
        Card c = new Card("ATK", 3, -1);
        c.setDesc("Pode atacar em 4 direcoes e tem 1 de distancia (Nao gasta).");
        return c;
    }
    public static Card CreateATK(int damage, int dur, Vector2 pos){
        Card c = new Card("ATK", damage, dur);
        c.setPosition(new Vector2(pos));
        c.setDesc("Pode atacar em 4 direcoes e tem 1 de distancia.");
        return c;
    }
    public static Card CreateDEF(int defense, int dur, int time, Vector2 pos){
        Card c = new Card("DEF", defense, dur){
            @Override
            public boolean Use(Player p){
                if(this.getType() == "DEF"){
                    p.setDef(p.getDef() + (getValue()));
                    p.GuardUp = time;
                    increaseUses();
                }
                return false;
            }
        };
        c.time = time;
        c.setHudStyle();
        c.setPosition(new Vector2(pos));
        c.setDesc("Aumenta a sua chance de esquivar de um ataque inimigo por algum tempo.");
        return c;
    }

    public boolean Use(Player p, Vector2 dir, GameMap map ){
        boolean hitted = false;
        if(this.type == "ATK"){
            int damage = ((getValue()+p.getAtk())/2);
            hitted = p.attack(damage, dir, map);
        }
        if(getDur() > 0)increaseUses();
        return hitted;
    }
    public boolean Use(Player p){return false;}

    public void Print(){
        for(String s : HudStyle){
            System.out.println(s);
        }
        System.out.println("   "+getUses()+"/"+getDur()+"    ");
    }

    protected void setHudStyle(){
        String typeString = "#  "+this.type+"  #";
        String valueString = (value<10) ? ("#   "+value+"   #"):("#  "+value+"   #");
        String[] s = {"#########",
                      "#       #",
                      typeString,
                      valueString,
                      "#       #",
                      "#########"};
        if(this.type == "DEF"){
            String timeString = (time<10) ? ("#   "+time+"   #"):("#  "+time+"   #");
            s[4] = timeString;
        }
        this.HudStyle = s;
        switch(this.type){
            case "ATK":
                this.setStyle('A');
            break;
            case "DEF":
                this.setStyle('D');
            break;
        }
    }

    protected void setDur(int dur){
        this.durability = dur;
        uses = 0;
    }
    protected void setDesc(String newDesc){this.Desc = newDesc;}
    protected void setType(String type){this.type = type;}

    public int increaseUses(){return ++uses;}
    public int getDur(){return durability;}
    public int getUses(){return uses;}
    public String getType(){return this.type;}
    public String GetDesc(){return this.Desc;}
    public boolean wasFullyUsed(){return uses == durability;}

}
