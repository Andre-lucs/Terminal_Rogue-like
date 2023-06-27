package main;

import structures.Vector2;

import java.util.Random;

public class Card extends Item
{
    public String[] HudStyle;

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
        c.setPosition(pos);
        c.setDesc("Pode atacar em 4 direcoes e tem 1 de distancia.");
        return c;
    }
    public static Card CreateDEF(int defense, int dur, int time, Vector2 pos){
        Card c = new Card("DEF", defense, dur){
            @Override
            public boolean Use(Player p){
                if(p.GuardUp > 0) return false;
                p.baseDef = p.getDef();
                p.setDef(p.getDef() + (int) (getValue()));
                p.GuardUp = time;
                increaseUses();
                return true;
            }
        };
        c.time = time;
        c.setHudStyle();
        c.setPosition(pos);
        c.setDesc("Aumenta a sua chance de esquivar de um ataque inimigo por algum tempo.");
        return c;
    }

    public static Card CreateRandom(float multiplier, Vector2 position){
        Random rdn = new Random(System.nanoTime());
        Card i = CreatePunchATK();
        switch(rdn.nextInt(0,4)){
            case 0:
            i = CreateATK((int)( rdn.nextInt(8,(int)(15*multiplier))), rdn.nextInt(3,6), position);
            break;
            case 1:
            i = CreateDEF((int) (rdn.nextInt(10,(int)(25*multiplier))), rdn.nextInt(3,6), rdn.nextInt(1,4), position);
            break;
            case 2:
            i = MGKCard.CreateHeal((int) (rdn.nextInt( 5,(int)(10*multiplier))), position);
            break;
        }
        return i;
    }

    public boolean Use(Player p, Vector2 dir, GameMap map ){
        boolean hitted = false;
        if(this.type == "ATK"){
            int damage = ((getValue()+p.getAtk())/2);
            hitted = p.attack(damage, dir, map);
            map.Update(false);
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
                this.setStyle("A");
            break;
            case "DEF":
                this.setStyle("D");
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
