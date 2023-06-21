package main;

import structures.Vector2;
import java.util.Random;

public class Item extends Entity
{
    private String attribute;
    private String type;
    protected int value;

    public Item(){}
    private Item(String type, String attribute, int value, Vector2 pos){
        this.type = type;
        this.attribute = attribute;
        this.value = value;
        this.setPosition(pos);
        this.setStyle('I');
    }

    public String getAttribute(){
        return attribute;
    }

    public String getType(){
        return type;
    }

    public void setValue(int newValue){
        this.value = newValue;
    }
    public int getValue(){
        return value;
    }

    public static Item CreateHelmet(String attribute, int value, Vector2 pos){
        Item i = new Item("Helmet", attribute, value, pos);
        return i;
    }
    public static Item CreateGlove(String attribute, int value, Vector2 pos){
        Item i = new Item("Glove", attribute, value, pos);
        return i;
    }
    public static Item CreateChestPlate(String attribute, int value, Vector2 pos){
        Item i = new Item("ChestPlate", attribute, value, pos);
        return i;
    }
    public static Item CreateShoe(String attribute, int value, Vector2 pos){
        Item i = new Item("Shoe", attribute, value, pos);
        return i;
    }
    public static Item CreateRandom(float multiplier, Vector2 position){
        String[] attributes = {"MHP","DEF","ATK"};
        Random rdn = new Random(System.nanoTime());
        String a = attributes[rdn.nextInt(0,3)];
        Item i = CreateShoe(a,(int) (rdn.nextInt((int)((a.equals("DEF")?8:25)*multiplier))), position);
        switch(rdn.nextInt(0,4)){
            case 0:
            i = CreateHelmet(a,(int)( rdn.nextInt((int)((a.equals("DEF")?8:15)*multiplier))), position);
            break;
            case 1:
            i = CreateChestPlate(a,(int) (rdn.nextInt((int)((a.equals("DEF")?8:25)*multiplier))), position);
            break;
            case 2:
            i = CreateShoe(a,(int) (rdn.nextInt((int)((a.equals("DEF")?5:15)*multiplier))), position);
            break;
            case 3:
            i = CreateGlove(a,(int)( rdn.nextInt((int)((a.equals("DEF")?5:15)*multiplier))), position);
            break;
        }
        return i;
    }
}
