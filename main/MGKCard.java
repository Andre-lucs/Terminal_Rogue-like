package main;

import structures.Vector2;

public class MGKCard extends Card{
    protected MGKCard(){
        this.setValue(8);
        setDur(3);
        setHudStyle();
    }

    @Override
    protected void setHudStyle(){
        this.setStyle('M');
        String typeString = "#  "+this.getType()+"  #";
        String valueString = (value<10) ? ("#   "+value+"    #"):("#   "+value+"   #");
        String[] s = {"##########",
                      "#        #",
                      typeString,
                      valueString,
                      "#        #",
                      "##########"};
        this.HudStyle = s;
    }

    public static MGKCard CreateHeal(Vector2 pos){
        MGKCard c = new MGKCard(){
            @Override
            public boolean Use(Player p){
                p.recoverLife(this.getValue());
                this.increaseUses();
                return false;
            }
        };
        c.setType("HEAL");
        c.setPosition(new Vector2(pos));
        c.setHudStyle();
        //c.HudStyle[2] = "#  HEAL  #";
        c.setDesc("Cura certa quantidade de vida.");
        return c;
    }

}
