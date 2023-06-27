package main;

import structures.*;

public class Entity
{
    protected Vector2 Position;
    protected String Style;

    public Entity(){
        Position = new Vector2(0);
    }
    
    public Vector2 getPosition(){
        return Position;
    }
    
    public void setPosition(Vector2 v){
        Position = new Vector2(v);
    }
    
    public String getStyle(){
        return Style;
    }
    
    public void setStyle(String style){
        Style = style;
    }

}
