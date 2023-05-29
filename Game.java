import java.util.Scanner;
import java.util.ArrayList;
import structures.*;
import main.*;

public class Game
{
    private Player p;
    private ArrayList<GameMap> maps;

    public Game(){
        maps = new ArrayList<>();
        maps.add(new GameMap());
        p = new Player(5, 5, new Vector2(2));
    }

    public void Start(){
        Scanner scanner = new Scanner(System.in);
        Enemy e = new Enemy(20,2), e2 = new Enemy(3,3);
        e.setPosition(new Vector2(5));
        e2.setPosition(new Vector2(3));
        Card c = new AtkCard(new Vector2(1),5,4);
        Card c1 = new DefCard(new Vector2(1,2));
        maps.get(0).Insert(p, e, e2, c, c1);
        maps.get(0).show();

        while(true){
            PlayerControls(maps.get(0), scanner);
        }
    }

    public void PlayerControls(GameMap map, Scanner in){
        map.Update();
        p.Update();

        boolean hitted = false;

        String command = in.next();//Pega a linha de commandos
        command = (command.length() != 0) ? command.substring(0, command.length()) : " ";//organiza ela
        String[] TempCommands = command.split("");//divide em varias Strings

        ArrayList<String> Commands = new ArrayList<>();//junta os comandos em comandos maiores
        String MainCommand = "";
        for(String c : TempCommands){
            if(c.equals(Controls.MOVE.get()) || c.equals(Controls.CARD.get()) || 
            c.equals(Controls.PICKUP.get()) || c.equals(Controls.HELP.get()) ||
            c.equals(Controls.INSPECIONATE.get())){
                if(!MainCommand.equals("")){
                    Commands.add(MainCommand);
                }
                MainCommand = c;
            }
            else {
                MainCommand += c;
            }

        }
        if(!MainCommand.equals("")){
            Commands.add(MainCommand);
        }

        for(String c : Commands){
            String key = c.substring(0,1);
            String actions = c.substring(1);
            if(key.equals(Controls.MOVE.get())){//vai se mover
                for(String i : actions.split("")){
                    Vector2 dir = Controls.CheckDir(i);
                    p.move(dir, map);
                }
            } else if(key.equals(Controls.CARD.get())){//vai usar uma carta ///// c2wd c3 c1a
                Card card = p.getCards().get(Integer.parseInt(actions.substring(0,1))-1);//pega a carta que sera usada
                if (card instanceof AtkCard){//se for uma carta de ataque
                    String directions = actions.substring(1);
                    for(String d : directions.split("")){
                        Vector2 dir = Controls.CheckDir(d);
                        int damage = (((AtkCard) card).getDamage()+p.getAtk())/2;
                        hitted = p.attack(damage, dir, map);
                    }
                }else if(card instanceof DefCard){//se for uma carta de defesa
                    System.out.println("Usou carta de defesa");
                    p.setDef(p.getDef() + ((DefCard) card).getDefense());
                    p.GuardUp = ((DefCard) card).getTime();
                }

                if(hitted) System.out.println("Acertou");
            } else if(key.equals(Controls.PICKUP.get())){//pegar um item
                for(Entity i : map.getInstances()){
                    if(Vector2.compareVectors(p.getPosition(), i.getPosition())){
                        if(i instanceof Item){
                            p.pickup((Item)i);
                            map.Remove(i);
                            break;
                        }
                    }
                }
            } else if(key.equals(Controls.INSPECIONATE.get())){
                for(Entity i : map.getInstances()){
                    if(Vector2.compareVectors(p.getPosition(), i.getPosition())){
                        if(i instanceof Card){
                            p.checkCard((Card)i);
                            break;
                        }else if(i instanceof Item){
                            
                        }
                        
                    }
                }
            } else if(key.equals(Controls.HELP.get())){//ver tela de ajuda sobre comandos

            }
        }

    }

}
