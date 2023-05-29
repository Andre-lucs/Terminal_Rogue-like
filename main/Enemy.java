package main;

import structures.Vector2;
import structures.GameMap;

public class Enemy extends Actor
{
    private int waitTime;
    private final int BaseWaitTime = 2;
    public Player player;
    
    public Enemy(int atk,int def,char style){
        super(atk,def,style);
        increaseMaxLife(20);
        waitTime = BaseWaitTime;
    }
    public Enemy(int atk,int def){
        this(atk, def, 'E');
    }
    
    public void Update(GameMap map){
        if(waitTime-- == 0){
            if(player != null){
                Vector2 dis = Vector2.getDistance(this.Position, player.Position);
                if(((dis.x <= 1 && dis.x >= -1) && (dis.y <= 1 && dis.y >= -1))){   
                    player.takeHit(this.Atk);
                }else{
                    if (dis.x > 1) dis.x = 1;
                    else if (dis.x < -1) dis.x = -1;
                    if (dis.y > 1) dis.y = 1;
                    else if (dis.y < -1) dis.y = -1;
                    this.move(dis, map);
                }
            }
            waitTime = BaseWaitTime;
        }
    }
    
    public boolean KnowsPlayer(){
        return player != null;
    }
    
    public void PrintInfo(){
        System.out.println("Enemy life: "+this.life+"/"+this.MaxLife);
    }
}
