package main;

import structures.Vector2;
import java.util.ArrayList;
import java.util.Arrays;

public enum Controls
{
    UP("w"), DOWN("s"), LEFT("a"), RIGHT("d"), //Diretional
    MOVE("m"),CARD("c"), PICKUP("e"), INSPECIONATE("i"), HELP("h");//Main Comandds

    private final String value;

    Controls(String n){
        this.value = n;
    }

    public String get(){
        return this.value;
    }

    public static ArrayList<String> getFilteredValues(){
        ArrayList<Controls> values = new ArrayList<>(Arrays.asList(Controls.values()));
        values.remove(Controls.LEFT);
        values.remove(Controls.RIGHT);
        values.remove(Controls.UP);
        values.remove(Controls.DOWN);
        ArrayList<String> strings = new ArrayList<>();
        values.forEach(i -> {
            strings.add(i.get());
        });
        return strings;
    }

    public static Vector2 CheckDir(String command){
        Vector2 v = new Vector2(0);
        if(command.equals(Controls.DOWN.get())){
            v.y = 1;
        } else if(command.equals(Controls.UP.get())){
            v.y = -1;
        } else if(command.equals(Controls.LEFT.get())){
            v.x = -1;
        } else if(command.equals(Controls.RIGHT.get())){
            v.x = 1;
        }
        return v;
    }
}
