import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import structures.*;
import main.*;

import java.lang.IndexOutOfBoundsException;

public class Game
{
    private Player p;
    private ArrayList<GameMap> maps;
    public String warning = null;

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
        PrintHud(maps.get(0), p);
        while(true){
            Update(maps.get(0), p, scanner);
        }
    }

    private void Update(GameMap map, Player p, Scanner scanner){
        map.Update();
        p.Update();
        PlayerControls(maps.get(0), scanner);
        PrintHud(maps.get(0), p);
    }

    private void PlayerControls(GameMap map, Scanner in){
        boolean hitted = false;

        String command = in.next();//Pega a linha de commandos
        command = (command.length() != 0) ? command.substring(0, command.length()) : " ";//organiza ela
        String[] TempCommands = command.split("");//divide em varias Strings

        ArrayList<String> Commands = new ArrayList<>();//junta os comandos em comandos maiores
        String MainCommand = "";
        for(String c : TempCommands){
            List<String> controlsfiltered = Controls.getFilteredValues();
            boolean foundMainCommand = false;
    	    for(String control : controlsfiltered){//organiza os comandos juntando os caracteres que fazer parte do mesmo comando
    		    if(c.equals(control)){
                    if(!MainCommand.equals("")){
                        Commands.add(MainCommand);
                    }
                    MainCommand = c;
                    foundMainCommand = true;
                }
    	    }
            if (!foundMainCommand) {
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
            }

            else if(key.equals(Controls.CARD.get())){//vai usar uma carta ///// c2wd c3 c1a
                try{
                Card card = p.getCards().get(Integer.parseInt(actions.substring(0,1))-1);//pega a carta que sera usada
                if (card instanceof AtkCard){//se for uma carta de ataque
                    String directions = actions.substring(1);
                    for(String d : directions.split("")){
                        Vector2 dir = Controls.CheckDir(d);
                        int damage = (((AtkCard) card).getDamage()+p.getAtk())/2;
                        hitted = p.attack(damage, dir, map);
                        card.increaseUses();
                    }
                }
                else if(card instanceof DefCard){//se for uma carta de defesa
                    System.out.println("Usou carta de defesa");
                    p.setDef(p.getDef() + ((DefCard) card).getDefense());
                    p.GuardUp = ((DefCard) card).getTime();
                    card.increaseUses();
                }
                if(card.wasFullyUsed()){
                    p.getCardsRef().remove(card);
                }

                if(hitted) System.out.println("Acertou");

                } catch(IndexOutOfBoundsException e){
                    warning = "Carta nao existe.";
                }
            }
            else if(key.equals(Controls.PICKUP.get())){//pegar um item
                for(Entity i : map.getInstances()){
                    if(Vector2.compareVectors(p.getPosition(), i.getPosition())){
                        if(i instanceof Item){
                            p.pickup((Item)i);
                            map.Remove(i);
                            break;
                        }
                    }
                }
            }
            else if(key.equals(Controls.INSPECIONATE.get())){
                for(Entity i : map.getInstances()){
                    if(Vector2.compareVectors(p.getPosition(), i.getPosition())){
                        if(i instanceof Card){
                            p.checkCard((Card)i);
                            break;
                        }else if(i instanceof Item){

                        }
                    }
                }
            }
            else if(key.equals(Controls.HELP.get())){//ver tela de ajuda sobre comandos

            }
        }

    }

    public void PrintHud(GameMap map, Player p){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for(String i : map.getRealMap()){//print map
            System.out.println(i);
        }
        for(Enemy e : map.getEnemies()) {//print enemy info if hitted
            if(e.KnowsPlayer()) e.PrintInfo();
        }
        System.out.print((warning == null) ? "" :warning+"\n");
        warning = null;
        p.PrintInfo();
        p.PrintCards();
    }
}
