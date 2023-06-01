package main;

import structures.Vector2;

public class Item extends Entity
{
    private String attribute;
    private String type;
    protected int value;

    public Item(){}
    public Item(String type, String attribute, int value, Vector2 pos){
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

    public int getValue(){
        return value;
    }
}
