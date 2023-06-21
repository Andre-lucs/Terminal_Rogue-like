import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import structures.*;
import main.*;

import java.lang.IndexOutOfBoundsException;
import java.util.concurrent.TimeUnit;

public class Game
{
    public static final String TEXT_RESET = "\u001B[0m";
	public static final String TEXT_RED = "\u001B[31m";
	public static final String TEXT_BLACK = "\u001B[30m";
	public static final String TEXT_GREEN = "\u001B[32m";
	public static final String TEXT_BLUE = "\u001B[34m";
	public static final String TEXT_PURPLE = "\u001B[35m";
	public static final String TEXT_CYAN = "\u001B[36m";
	public static final String TEXT_YELLOW = "\u001B[33m";
	public static final String TEXT_WHITE = "\u001B[37m";

	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private Player p;
    private ArrayList<GameMap> maps;
    public String warning = null;
    private MapGenerator gen;
    private int mapIndex = 0;
    private float levelDiff;

    public Game(){
        levelDiff = 1;
        maps = new ArrayList<>();
        Vector2 startPos = new Vector2(8);
        gen = new MapGenerator(new Vector2(30,20), startPos);
        p = new Player(5, 5, startPos);
        p.increaseMaxLife(90);
    }

    public void Start(){
        String[] labirinto = gen.getNewMap();
        maps.add(new GameMap(labirinto));
        PopulateMap(maps.get(mapIndex));
        Scanner scanner = new Scanner(System.in);

        PrintHud(maps.get(mapIndex), p);
        while(true){
            Update(maps.get(mapIndex), p, scanner);
        }
    }

    private void Update(GameMap map, Player p, Scanner scanner){
        map.Update();
        p.Update();
        PlayerControls(maps.get(mapIndex), scanner);
        PrintHud(maps.get(mapIndex), p);
        if(p.getLife() <= 0){
            System.out.println("Game Over");
            System.exit(0);//Futuramente apenas sai da partida e volta para o main
        }
        if(map.getEnemies().size() == 0){
            System.out.println("moreu geral");
            gen.setPos(new Vector2(p.getPosition()));
            String[] labirinto = gen.getNewMap();
            maps.add(new GameMap(labirinto));
            mapIndex++;
            PopulateMap(maps.get(mapIndex));
            PrintHud(maps.get(mapIndex), p);
        }
    }

    private void PopulateMap(GameMap map){
        map.Insert(p);
        Random rdn = new Random(System.nanoTime());
        Enemy boss = new Enemy((int) (40 * levelDiff),(int) (8*levelDiff), map.getRandomFreePosition());
        boss.setStyle('B');
        List<Enemy> enemiestopick = new ArrayList<>();
        List<Card> cardstopick = new ArrayList<>();
        List<Item> itemstopick = new ArrayList<>();
        for(int i = 0; i <rdn.nextInt(2,8); i++) {
            enemiestopick.add(new Enemy(rdn.nextInt((int)(levelDiff*25)), rdn.nextInt((int)(levelDiff* 5)), map.getRandomFreePosition()));
        }
        for(int i = 0; i <rdn.nextInt(2,8); i++) cardstopick.add(Card.CreateRandom(levelDiff, map.getRandomFreePosition()));
        for(int i = 0; i <rdn.nextInt(2,8); i++) itemstopick.add(Item.CreateRandom(levelDiff, map.getRandomFreePosition()));
        //pegar aleatorio e colocar no mapa
        List<Item> items = new ArrayList<>();
        for(int i = 0; i <rdn.nextInt(2,8); i++){
            items.add((rdn.nextInt(2) == 0) ? cardstopick.get(rdn.nextInt(cardstopick.size())) : itemstopick.get(rdn.nextInt(itemstopick.size())));
        }
        try{
            map.Insert(boss);
            enemiestopick.forEach(e -> {map.Insert(e);});
            items.forEach(i -> {map.Insert(i);});
        }catch(GameMapExeption exep){
            System.out.println("Bas Pos");
        }
        map.Update();
    }

    private void Sleep(){
        try {
            TimeUnit.MILLISECONDS.sleep((long)100);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private void UpdateAndSleep(GameMap map){
        map.Update();
        PrintHud(map, p);
        try {
            TimeUnit.MILLISECONDS.sleep((long)100);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
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
                    UpdateAndSleep(map);
                }
            }

            else if(key.equals(Controls.CARD.get())){//vai usar uma carta ///// c2wd c3 c1a
                try{
                Card card = p.getCards().get(Integer.parseInt(actions.substring(0,1))-1);//pega a carta que sera usada
                if (card.getType() == "ATK"){//se for uma carta de ataque
                    String directions = actions.substring(1);
                    if(directions.length() > 0){
                        for(String d : directions.split("")){
                            Vector2 dir = Controls.CheckDir(d);
                            hitted = card.Use(p, dir, map);
                            UpdateAndSleep(map);
                        }
                    }
                }
                else if(card.getType() == "DEF"){//se for uma carta de defesa
                    card.Use(p,new Vector2(), map);
                    UpdateAndSleep(map);
                }
                else if(card instanceof MGKCard){
                    MGKCard mc = (MGKCard) card;
                    if(mc.getType() == "HEAL"){
                        mc.Use(p);
                    }
                }
                if(card.wasFullyUsed()){
                    p.getCardsRef().remove(card);
                }

                if(hitted) warning = "Acertou";

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
                            warning = ("Type:" + ((Item)i).getType()+"\n"+
                            "Description: "+ ((Item)i).getAttribute() +" "+ ((Item)i).getValue());
                        }
                    }
                }
                try{
                    int i = Integer.parseInt(actions);
                    try{warning = p.getCards().get(i-1).GetDesc();}
                    catch(IndexOutOfBoundsException e){}
                }catch(NumberFormatException e){

                }
            }
            else if(key.equals(Controls.HELP.get())){//ver tela de ajuda sobre comandos

            }

            Sleep();
        }

    }

    public void PrintHud(GameMap map, Player p){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        String helmet = (p.Equip.get("Helmet") != null) ? ( (p.Equip.get("Helmet").getValue()<10) ? p.Equip.get("Helmet").getValue()+" " : String.valueOf(p.Equip.get("Helmet").getValue()) ) : "  ";
        String chestPlate = (p.Equip.get("ChestPlate") != null) ? ( (p.Equip.get("ChestPlate").getValue()<10) ? p.Equip.get("ChestPlate").getValue()+" " : String.valueOf(p.Equip.get("ChestPlate").getValue()) ) : "  ";
        String shoe = (p.Equip.get("Shoe") != null) ? ( (p.Equip.get("Shoe").getValue()<10) ? p.Equip.get("Shoe").getValue()+" " : String.valueOf(p.Equip.get("Shoe").getValue()) ) : "  ";
        String glove = (p.Equip.get("Glove") != null) ? ( (p.Equip.get("Glove").getValue()<10) ? p.Equip.get("Glove").getValue()+" " : String.valueOf(p.Equip.get("Glove").getValue()) ) : "  ";

        String[] playerEquip = {
    "                    .-'-'-.        #########",
    "                    /     \\        #  "+((p.Equip.get("Helmet") != null)? p.Equip.get("Helmet").getAttribute() : "   " )+"  #",
    "                   | o   o | <-----#  "+helmet+"   #",
    "                   \\   ∆   /       #       #",
    "                    '-...-'        #########",
    "   #########           |           #########",
    "   #  "+((p.Equip.get("ChestPlate") != null)? p.Equip.get("ChestPlate").getAttribute() : "   " )+"  #          /|\\          #  "+((p.Equip.get("Glove") != null)? p.Equip.get("Glove").getAttribute() : "   " )+"  #",
    "   #  "+chestPlate+"   #-------| / | \\ <-------#  "+glove+"   #",
    "   #       #       v/  |  \\        #       #",
    "   #########       /   |   \\       #########",
    "   #########      /   / \\   \\",
    "   #  "+((p.Equip.get("Shoe") != null)? p.Equip.get("Shoe").getAttribute() : "   " )+"  #         /   \\",
    "   #  "+shoe+"   #------> /     \\",
    "   #       #       /       \\",
    "   #########      /         \\"
        };


        int maxY = Math.max(playerEquip.length, map.getMapSize().y);
        String[] tempMap = map.getRealMap();
        for(int i = 0; i <  maxY; i++){
            try{
                for(int j = 0; j < tempMap[i].length(); j++){
                    System.out.print((map.visibility[i][j]) ? tempMap[i].charAt(j) : " ");
                }
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.print(String.valueOf(' ').repeat(map.getMapSize().x));
            }
            try{
            System.out.println(playerEquip[i]);
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println();
            }
        }
        for(Enemy e : map.getEnemies()) {//print enemy info if hitted
            if(e.KnowsPlayer()) e.PrintInfo();
        }
        System.out.println("Inimigos Faltando: "+ map.getEnemies().size());
        System.out.print((warning == null) ? "" :warning+"\n");
        warning = null;
        p.PrintInfo();
        p.PrintCards();
    }
}
