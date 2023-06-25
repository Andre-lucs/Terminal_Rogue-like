package main;

import java.util.Random;

import structures.Vector2;
import structures.GameMap;
import structures.Status;

public class Enemy extends Actor implements Killable
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
    public Enemy(int atk,int def, Vector2 pos){
        this(atk, def, 'E');
        this.setPosition(pos);
    }

    @Override
    public boolean takeHit(int rawDamage){
        if(super.gen.nextDouble() > this.attributes.get("DEF")/100){
            life -= (int) (rawDamage*1.2);
            if (life <= 0){
                status = Status.DEAD;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDeath(GameMap map){
        Random rdn = new Random(System.nanoTime());
        Vector2 pos = this.getPosition();
        int levelDiff = this.attributes.get("MHP")/20;
        Item item = (rdn.nextInt(2) == 0) ? Card.CreateRandom(levelDiff, pos) : Item.CreateRandom(levelDiff, pos);
        map.Insert(item);
        System.out.println("Inimigo foi morto");
    }

    public void Update(GameMap map){
        if(waitTime-- == 0){
            if(player != null){
                Vector2 dis = Vector2.getDistance(this.Position, player.Position);
                if(((dis.x <= 1 && dis.x >= -1) && (dis.y <= 1 && dis.y >= -1))){
                    player.takeHit(this.attributes.get("ATK"));
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
        System.out.println("Enemy life: "+this.life+"/"+this.attributes.get("MHP"));
    }
}
