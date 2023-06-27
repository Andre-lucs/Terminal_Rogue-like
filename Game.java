import java.util.*;

import structures.*;
import main.*;

import java.lang.IndexOutOfBoundsException;
import java.util.concurrent.TimeUnit;

public class Game
{
    private Player p;
    private ArrayList<GameMap> maps;
    public String warning = null;
    private MapGenerator gen;
    private int mapIndex = -1;
    private float levelDiff;
    private boolean levelEnded;

    public Game(){
        levelDiff = 1;
        maps = new ArrayList<>();
        Vector2 startPos = new Vector2(8);
        gen = new MapGenerator(new Vector2(30,20), startPos);
    }

    public void Start(){
        p = new Player(5, 5, new Vector2(8));
        p.increaseMaxLife(90);//acrescimo de vida para testes
        NextLevel();
        Scanner scanner = new Scanner(System.in);

        while(true){
            Update(maps.get(mapIndex), p, scanner);
        }
    }

    private void Update(GameMap map, Player p, Scanner scanner){
        if(map.Boss == null){
            levelEnded = true;
        }
        p.Update();
        map.Update(true);
        PlayerControls(maps.get(mapIndex), scanner);
        PrintHud(maps.get(mapIndex), p);
        if(p.getLife() <= 0){
            p.setStatus(Status.DEAD);
            p.onDeath(maps.get(mapIndex));
        }
    }

    private void NextLevel(){
        mapIndex++;
        levelDiff += 0.5;
        levelEnded = false;
        gen.setPos(new Vector2(p.getPosition()));
        GameMap labirinto = gen.getNewMap();
        maps.add(labirinto);
        PopulateMap(maps.get(mapIndex));
        PrintHud(maps.get(mapIndex), p);
    }

    private void PopulateMap(GameMap map){
        p.setPosition(map.getRandomFreePosition());
        map.Insert(p);
        Random rdn = new Random(System.nanoTime());
        //cria e insere os inimigos (incluindo o chefe)
        Enemy boss = new Enemy((int) (40 * levelDiff),(int) (8*levelDiff), map.getRandomFreePosition());
        boss.setStyle("B");
        map.Boss = boss;
        map.Insert(boss);
        for(int i = 0; i <rdn.nextInt(2,7); i++) {
            map.Insert(new Enemy(rdn.nextInt((int)(levelDiff*25)), rdn.nextInt((int)(levelDiff* 5)), map.getRandomFreePosition()){
                @Override
                public boolean takeHit(int dmg){
                    if(super.gen.nextDouble() > this.attributes.get("DEF")/100){
                        life -= (int) (dmg*(1.5/levelDiff));
                        if (life <= 0){
                            status = Status.DEAD;
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
        //cria e insere as cartas e items do mapa
        for(int i = 0; i <rdn.nextInt(4,9); i++){
            Vector2 pos = map.getRandomFreePosition();
            Item item = (rdn.nextInt(2) == 0) ? Card.CreateRandom(levelDiff, pos) : Item.CreateRandom(levelDiff, pos);
            map.Insert(item);
        }//items em lugares normais
        for(int i = 0;i < 2; i++){
            Vector2[] pos = {map.getPositionOnWall(),map.getPositionOnWall()};
            Item[] item = {Card.CreateRandom(levelDiff*2, pos[0]), Item.CreateRandom(levelDiff*2, pos[1])};
            map.Insert(item[0], item[1]);//items em paredes
        }
        map.Update(false);
    }

    private void Sleep(){
        try {
            TimeUnit.MILLISECONDS.sleep((long)100);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private void UpdateAndSleep(GameMap map){
        map.Update(true);
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
        if(command.contains("next") && levelEnded){
            NextLevel();
            return;
        }
        if(maps.get(mapIndex).getPlayer().getStatus() == Status.DEAD){
            if(command.contains("yes")) {
                Start();
                System.exit(0);
            }else if (command.contains("no")){
                System.exit(0);
            }
        }
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
                if (card.getType().equals("ATK")){//se for uma carta de ataque
                    String directions = actions.substring(1);
                    if(directions.length() > 0){
                        for(String d : directions.split("")){
                            Vector2 dir = Controls.CheckDir(d);
                            hitted = card.Use(p, dir, map);
                            UpdateAndSleep(map);
                            warning = (hitted) ?"Acertou":"Errou";
                        }
                    }
                }
                else if(card.getType().equals("DEF")){//se for uma carta de defesa
                    card.Use(p);
                    UpdateAndSleep(map);
                }
                else if(card instanceof MGKCard mc){
                    if(mc.getType().equals("HEAL")){
                        mc.Use(p);
                    }
                }
                if(card.wasFullyUsed()){
                    p.getCardsRef().remove(card);
                }
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
                            warning = ("-".repeat(50)+"\nPreview Item:\n"+"Type:" + ((Item)i).getType()+"\n"+
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
                warning = "m = Mover. (mwasd) Se move para as direcoes especificadas com as teclas wasd\n"+
                "c = Carta. (c1w)(c2) Usa a carta na posicao especificada podendo ser omitido a direcao em cartas como heal e def\n"+
                "i = Inspecionar. Ao ficar na mesma pocisao que um item mostre uma preview do que ele e\n"+
                "e = Pegar Item. pega o item que se esta sobre\n" +
                "h = Ajuda. Mostra o texto de ajuda" +
                "next = Proxima Fase. Vai para o proximo nivel ao matar o chefe no nivel atual"+
                "yes = Ao morrer digite \"yes\" para jogar de novo.";
            }

            Sleep();
        }

    }

    private String SetColorAtb(String str, String atb){
        return switch(atb){
            case "ATK"-> TextColor.TEXT_RED;
            case "DEF"-> TextColor.TEXT_BLUE;
            case "MHP"-> TextColor.TEXT_GREEN;
            default -> TextColor.TEXT_WHITE;
        }+ str + TextColor.TEXT_RESET;
    }

    public void PrintHud(GameMap map, Player p){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        String helmet = (p.Equip.get("Helmet") != null) ? ( (p.Equip.get("Helmet").getValue()<10) ? p.Equip.get("Helmet").getValue()+" " : String.valueOf(p.Equip.get("Helmet").getValue()) ) : "  ";
        String chestPlate = (p.Equip.get("ChestPlate") != null) ? ( (p.Equip.get("ChestPlate").getValue()<10) ? p.Equip.get("ChestPlate").getValue()+" " : String.valueOf(p.Equip.get("ChestPlate").getValue()) ) : "  ";
        String shoe = (p.Equip.get("Shoe") != null) ? ( (p.Equip.get("Shoe").getValue()<10) ? p.Equip.get("Shoe").getValue()+" " : String.valueOf(p.Equip.get("Shoe").getValue()) ) : "  ";
        String glove = (p.Equip.get("Glove") != null) ? ( (p.Equip.get("Glove").getValue()<10) ? p.Equip.get("Glove").getValue()+" " : String.valueOf(p.Equip.get("Glove").getValue()) ) : "  ";

        String helmetAtb = ((p.Equip.get("Helmet") != null)? p.Equip.get("Helmet").getAttribute() : "   " );
        String chestAtb = ((p.Equip.get("ChestPlate") != null)? p.Equip.get("ChestPlate").getAttribute() : "   " );
        String shoeAtb = ((p.Equip.get("Shoe") != null)? p.Equip.get("Shoe").getAttribute() : "   " );
        String gloveAtb = ((p.Equip.get("Glove") != null)? p.Equip.get("Glove").getAttribute() : "   " );

        helmet = SetColorAtb(helmet, helmetAtb);
        chestPlate = SetColorAtb(chestPlate, chestAtb);
        shoe = SetColorAtb(shoe, shoeAtb);
        glove = SetColorAtb(glove, gloveAtb);
        helmetAtb = SetColorAtb(helmetAtb, helmetAtb);
        chestAtb = SetColorAtb(chestAtb,chestAtb);
        shoeAtb = SetColorAtb(shoeAtb,shoeAtb);
        gloveAtb = SetColorAtb(gloveAtb,gloveAtb);
        String[] playerEquip = {
    "                    .-'-'-.        #########",
    "                    /     \\        #  "+helmetAtb+"  #",
    "                   | o   o | <-----#  "+helmet+"   #",
    "                   \\   ∆   /       #       #",
    "                    '-...-'        #########",
    "   #########           |           #########",
    "   #  "+gloveAtb+"  #          /|\\          #  "+chestAtb+"  #",
    "   #  "+glove+"   #-------| / | \\ <-------#  "+chestPlate+"   #",
    "   #       #       v/  |  \\        #       #",
    "   #########       /   |   \\       #########",
    "   #########      /   / \\   \\",
    "   #  "+shoeAtb+"  #         /   \\",
    "   #  "+shoe+"   #------> /     \\",
    "   #       #       /       \\",
    "   #########      /         \\"
        };

        int maxY = Math.max(playerEquip.length, map.getMapSize().y);
        String[] tempMap = map.getRealMap();
        for(int i = 0; i <  maxY; i++){//imprime mapa e equipamento
            try{
                for(int j = 0; j < tempMap[i].length(); j++){
                    String cell = String.valueOf(tempMap[i].charAt(j));
                    cell = switch(cell){
                        case "E" , "B" -> TextColor.TEXT_RED+cell+TextColor.TEXT_RESET;
                        case "#" -> TextColor.TEXT_YELLOW+cell+TextColor.TEXT_RESET;
                        case "P" -> TextColor.TEXT_WHITE+cell+TextColor.TEXT_RESET;
                        case "I" -> TextColor.TEXT_BLUE+cell+TextColor.TEXT_RESET;
                        default -> cell;
                    };
                    System.out.print(TextColor.ANSI_BLACK_BACKGROUND);
                    System.out.print((map.visibility[i][j]) ? cell : " ");
                }
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.print(" ".repeat(map.getMapSize().x));
            }
            try{
                String pEquip = playerEquip[i];
                System.out.println(pEquip);
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println();
            }
        }
        System.out.println(TextColor.TEXT_RESET);
        if(levelEnded)System.out.println("O chefe foi morto. digite \"next\" para ir para o proximo nivel.");
        else System.out.println("Inimigos Faltando: "+ map.getEnemies().size());
        int i = 1;
        boolean printOnce = true;
        for(Enemy e : map.getEnemies()) {//imprime informacoes do inimigo ao entrar em combate
            if(e.KnowsPlayer()) {
                if(printOnce) {
                    System.out.println((maps.get(mapIndex).enemyTimer == 1) ? "Os inimigos vao agir na sua proxima acao." : "Os inimigos nao vao agir.");
                    printOnce = false;
                }
                System.out.println(i + ": \n" + e);
            }
            i++;
        }
        System.out.print((warning == null) ? "" :warning+"\n");
        warning = null;
        System.out.println("-".repeat(100));
        p.PrintInfo();
        p.PrintCards();
    }
    public static void main(String[] args){
        Game game;
        game = new Game();
        game.Start();
    }
}
