package main;

public abstract class Card extends Item
{
    public String[] HudStyle = {"#########",
                                "#       #",
                                "# CARD  #",
                                "#       #",
                                "#       #",
                                "#########"};

    private int durability;
    private int uses;
    
    public int getDur(){
        return durability;
    }
    protected void setDur(int dur){
        this.durability = dur;
        uses = 0;
    }

    public int getUses(){
        return uses;
    }
    public int increaseUses(){
        return ++uses;
    }
    public boolean wasFullyUsed(){
        return uses == durability;
    }

    public void PrintHud(){
        for(String s : HudStyle){
            System.out.println(s);
        }
        System.out.println("   "+getUses()+"/"+getDur()+"    ");
    }
}
