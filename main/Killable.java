package main;
import structures.GameMap;

public interface Killable{
    public boolean takeHit(int rawDamage);
    public void onDeath(GameMap map);
}
