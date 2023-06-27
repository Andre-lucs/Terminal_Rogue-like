package main;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import structures.Vector2;
import structures.Status;

public class Player extends Actor implements Killable
{
    public int baseDef;
    public int GuardUp;

    private Card cardtopreview = null;
    private List<Card> Cards;
    public Map<String, Item> Equip;

    public Player(int atk, int def, Vector2 startPosition)
    {
        super(atk, def, "P");
        baseDef = def;
        GuardUp = 0;
        increaseMaxLife(50);//vida base
        status = Status.ALIVE;
        Position = new Vector2(startPosition);
        Cards = new ArrayList<>();
        Equip = new HashMap<>();
        Equip.put("Helmet", null);
        Equip.put("ChestPlate", null);
        Equip.put("Shoe", null);
        Equip.put("Glove", null);
        Cards.add(Card.CreatePunchATK());
    }
    public Player(){
        this(5,5, new Vector2(2));
    }

    public List<Card> getCards(){
        return new ArrayList<>(Cards);
    }
    public List<Card> getCardsRef(){
        return Cards;
    }
    public Map<String, Item> getEquip(){
        return new HashMap<>(Equip);
    }

    public void Update(){
        if (GuardUp-- == 0) setDef(baseDef);//reseta o valor da defesa apos usar carta de defesa
    }

    public boolean attack(int damage, int dirX, int dirY, GameMap map){
        Entity ent = map.getCell(Position.x+dirX, Position.y+dirY);
        if(ent instanceof Killable){
            Enemy en = (Enemy) ent;
            if(en.player == null) en.player = (Player) this;
            if(en.takeHit(damage)){
                if(en.status == Status.DEAD){
                    //map.Remove(en);
                    en.player = null;
                }
                return true;
            }
        }
        return false;
    }
    public boolean attack(int damage, Vector2 dir, GameMap map){
        return attack(damage, dir.x, dir.y, map);
    }

    public void pickup(Item item){
        if (item instanceof Card){
            Cards.add((Card)item);
            return;
        }
        changeItem(item);

    }

    private void changeItem(Item item){
        Item atual = null;
        atual = Equip.get(item.getType());
        handleAttribute(atual, item);
        Equip.put(item.getType(), item);
    }

    private void handleAttribute(Item atualItem ,Item newItem){
        if(atualItem != null){
            String attribute = atualItem.getAttribute();
            if(!attribute.equals("MHP")) {
                int newValue = attributes.get(attribute) - atualItem.getValue();
                attributes.put(attribute, newValue);
            }else {
                this.decreaseMaxLife(atualItem.getValue());
            }
        }
        String attribute = newItem.getAttribute();
        if(!attribute.equals("MHP")) {
            int newValue = attributes.get(attribute) + newItem.getValue();
            attributes.put(attribute, newValue);
        }else {
            this.increaseMaxLife(newItem.getValue());
        }
        baseDef = getDef();
    }

    public void PrintInfo(){
        System.out.println("Player Life: " + this.life+"/"+this.attributes.get("MHP"));
        System.out.println("ATK: "+ this.attributes.get("ATK"));
        System.out.println("DEF: "+ this.attributes.get("DEF"));
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
                        System.out.print((c.getDur()!=-1) ? ("   "+c.getUses()+"/"+c.getDur()+"    ") : ("          "));
                    }
                    System.out.println();
                }
            }
        }
        if(cardtopreview != null){
            System.out.println("Preview:");
            cardtopreview.Print();
            cardtopreview = null;
        }
    }

    public void checkCard(Card card){
        cardtopreview = card;
    }
    @Override
    public boolean takeHit(int dmg){
        if(gen.nextDouble() > this.attributes.get("DEF")/100){
            life -= (int) (dmg*0.8);
            if (life <= 0){
                status = Status.DEAD;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDeath(GameMap map){
        System.out.println("Voce Morreu");
        System.out.println("Deseja recomeçar?(yes/no)");
    }
}
