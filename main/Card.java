package main;

public abstract class Card extends Item
{
    public String[] HudStyle = {"#########",
                                "#       #",
                                "# CARD  #",
                                "#       #",
                                "#       #",
                                "#########"};
                             
    protected int value;
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
    
    public void PrintHud(){
        for(String s : HudStyle){
            System.out.println(s);
        }
        System.out.println("   "+getUses()+"/"+getDur()+"    ");
    }
}
