package main;

import java.util.ArrayList;
import java.util.Scanner;
import structures.GameMap;
import structures.Vector2;

public class Player extends Actor
{

    private ArrayList<Card> Cards;
    private ArrayList<Item> Items;

    private int BaseDef;
    public int GuardUp;

    private Card cardtopreview = null;

    public Player(int atk, int def, Vector2 startPosition)
    {
        super(atk, def, 'P');
        BaseDef = def;
        GuardUp = 0;
        increaseMaxLife(20);
        status = status.ALIVE;
        Position = new Vector2(startPosition);
        Cards = new ArrayList<>();
        Items = new ArrayList<>();
    }
    public Player(){
        this(5,5, new Vector2(2));
    }

    public ArrayList<Card> getCards(){
        return new ArrayList(Cards);
    }
    public ArrayList<Item> getItems(){
        return new ArrayList(Items);
    }

    public void Update(){
        if (GuardUp > 0) GuardUp--;
        else if (GuardUp == 0) setDef(BaseDef);//reseta o valor da defesa apos usar carta de defesa
    }

    public void pickup(Item item){
        if (item instanceof Card){
            Cards.add((Card)item);
            item.setPosition(new Vector2());
        }
    }

    public void PrintInfo(){
        System.out.println("Player Life: " + this.life+"/"+this.MaxLife);
    }

    public void PrintCards(){
        if(Cards.size() > 0){
            System.out.println("Cards:");
            for(int i = 0; i < 6; i++){
                int n = 1;
                if(i == 0){
                    for(Card c : Cards){
                        System.out.print("    "+(n++)+"     ");
                    }
                    System.out.println();
                }
                for(Card c : Cards){
                    System.out.print(c.HudStyle[i] + " ");
                };
                System.out.println();
                if(i == 5){
                    for(Card c : Cards){
                        System.out.print("   "+c.getUses()+"/"+c.getDur()+"    ");
                    }
                    System.out.println();
                }
            }
        }
        if(cardtopreview != null){
            System.out.println("Preview:");
            cardtopreview.PrintHud();
            cardtopreview = null;
        }
    }

    public void checkCard(Card card){
        cardtopreview = card;
    }
}
