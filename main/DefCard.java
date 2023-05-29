package main;

import structures.Vector2;

public class DefCard extends Card
{
    private int time;
    public DefCard(){
        setDefense(50);
        setTime(1);
        setDur(3);
        setHudStyle();
    }
    public DefCard(int defense, int time, int durability){
        setDefense(defense);
        setTime(time);
        setDur(durability);
        setHudStyle();
    }
    public DefCard(Vector2 pos, int defense, int time, int durability){
        this(defense, time, durability);
        this.setPosition(new Vector2(pos));
    }
    public DefCard(Vector2 pos){
        this();
        this.setPosition(new Vector2(pos));
    }
    
    private void setHudStyle(){
        String defString = (value<10) ? ("#   "+value+"   #"):("#  "+value+"   #");
        String timeString = (time<10) ? ("#   "+time+"   #"):("#  "+time+"   #");
        String[] s = {"#########",
                      "#       #",
                      "#  DEF  #",
                      defString,
                      timeString,
                      "#########"};
        this.HudStyle = s;
        this.setStyle('D');
    }
    
    public int getDefense(){
        return this.value;
    }
    public void setDefense(int newDefense){
        this.value = newDefense;
    }
    
    public int getTime(){
        return time;
    }
    public void setTime(int time){
        this.time = time;
    }

}
