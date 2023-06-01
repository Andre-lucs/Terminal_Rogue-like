package main;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import structures.GameMap;
import structures.Vector2;

public class Player extends Actor
{

    private List<Card> Cards;

    private int BaseDef;
    public int GuardUp;

    private Card cardtopreview = null;
    public Map<String, Item> Equip;

    public Player(int atk, int def, Vector2 startPosition)
    {
        super(atk, def, 'P');
        BaseDef = def;
        GuardUp = 0;
        increaseMaxLife(20);
        status = status.ALIVE;
        Position = new Vector2(startPosition);
        Cards = new ArrayList<>();
        Equip = new HashMap<>();
        Equip.put("Helmet", null);
        Equip.put("ChestPlate", null);
        Equip.put("Shoe", null);
        Equip.put("Glove", null);
    }
    public Player(){
        this(5,5, new Vector2(2));
    }

    public List<Card> getCards(){
        return new ArrayList(Cards);
    }
    public List<Card> getCardsRef(){
        return Cards;
    }
    public Map<String, Item> getEquip(){
        return new HashMap<>(Equip);
    }

    public void Update(){
        if (GuardUp > 0) GuardUp--;
        else if (GuardUp == 0) setDef(BaseDef);//reseta o valor da defesa apos usar carta de defesa
    }

    public void pickup(Item item){
        if (item instanceof Card){
            Cards.add((Card)item);
            item.setPosition(new Vector2());
            return;
        }
        switch(item.getType()){
            case "Helmet":
            if(Equip.get("Helmet") != null){
                changeItem(item);
            }
            Equip.put("Helmet", item);
            break;
            case "ChestPlate":
            if(Equip.get("ChestPlate") != null){
                changeItem(item);
            }
            Equip.put("ChestPlate", item);
            break;
            case "Glove":
            if(Equip.get("Glove") != null){
                changeItem(item);
            }
            Equip.put("Glove", item);
            break;
            case "Shoe":
            if(Equip.get("Shoe") != null){
                changeItem(item);
            }
            Equip.put("Shoe", item);
            break;
        }
        handleAttribute(item);
        item.setPosition(new Vector2());
    }

    private void changeItem(Item item){
        Item atual = null;
        switch(item.getType()){
            case "Helmet":
            atual = Equip.get("Helmet");
            break;
            case "ChestPlate":
            atual = Equip.get("ChestPlate");
            break;
            case "Glove":
            atual = Equip.get("Glove");
            break;
            case "Shoe":
            atual = Equip.get("Shoe");
            break;
        }
        if(atual != null){
            atual.value = atual.value * -1;
            handleAttribute(atual);
        }
    }

    private void handleAttribute(Item item){
        switch(item.getAttribute()){
            case "ATK":
            this.setAtk(attributes.get("ATK")+item.getValue());
            break;
            case "DEF":
            this.setDef(attributes.get("DEF")+item.getValue());
            break;
            case "MHP":
            this.increaseMaxLife(item.getValue());
            break;
        }
    }

    public void PrintInfo(){
        System.out.println("Player Life: " + this.life+"/"+this.attributes.get("MHP"));
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
