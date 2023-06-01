package structures;

import java.util.ArrayList;
import main.*;

public class GameMap
{
    private String[] initialMap ={"##########",
                                  "#        #",
                                  "#        #",
                                  "#        #",
                                  "#        #",
                                  "#        #",
                                  "#        #",
                                  "#        #",
                                  "#        #",
                                  "##########"};//mapa que serve de base para os a construçao dos elemetos do mapa

    private String[] realMap;//mapa armazenado em tempo real com as instancias dos objetos representados
    private ArrayList<Entity> instances;//Lista de objetos instanciados no mapa
    private Vector2 mapSize = new Vector2(0);//dimençoes do mapa
    private boolean showed = false;//se deve imprimir ele ao modificar
    private Player player;
    private ArrayList<Enemy> enemies;

    public GameMap()
    {
        mapSize.x = initialMap[0].length();
        mapSize.y = initialMap.length;
        instances = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        this.realMap = this.initialMap.clone();
    }
    public GameMap(String[] newMap)
    {
        this.initialMap = newMap.clone();
        mapSize.x = initialMap[0].length();
        mapSize.y = initialMap.length;
        instances = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        this.realMap = this.initialMap.clone();
    }
    public GameMap(GameMap pastMap)
    {
        this(pastMap.initialMap);
        instances = new ArrayList<Entity>(pastMap.instances);
    }

    public Vector2 getMapSize(){
        return new Vector2(mapSize);
    }

    public ArrayList<Entity> getInstances(){
        return new ArrayList<>(instances);
    }

    public String[] getRealMap(){
        return realMap;
    }

    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }

    public Entity getCell(int x, int y){
        for(Entity i : instances){
            Vector2 pos = i.getPosition();
            if(pos.x == x && pos.y == y){
                return i;
            }
        }
        Entity fallback = new Entity();
        fallback.setPosition(new Vector2(x,y));
        fallback.setStyle(realMap[y].charAt(x));
        return fallback;
    }
    public Entity getCell(Vector2 pos){
        return getCell(pos.x, pos.y);
    }

    public Vector2 searchFirstElement(char c){
        for(int x = 0; x < mapSize.x; x++){
            for(int y = 0; y < mapSize.y; y++){
                if(getCell(x,y).getStyle() == c) {
                    return new Vector2(x,y);
                }
            }
        }
        return new Vector2();
    }

    //atualiza o mapa atual
    public void Update(){
        GameMap newMap = new GameMap(this);

        this.UpdateEnemies();

        for(Entity i : instances){
            if(i instanceof Actor){
                Actor a = (Actor) i;
                if(a.getStatus() == Status.ALIVE){
                    newMap.updateCell(i.getPosition(), i.getStyle());
                }

            } else {
                newMap.updateCell(i.getPosition(), i.getStyle());
            }
        };
        this.realMap = newMap.realMap;
        if(player != null){
            if(player.getLife() <= 0){
                System.out.println("Game Over");
                System.exit(0);//////////Futuramente apenas sai da partida e volta para o main
            }
            updateCell(player.getPosition(), player.getStyle());//jogador pode ficar em cima de items e cartas
        }

    }

    //atualiza o caractere de uma posiçao do mapa
    public void updateCell(int x, int y, char cell){
        realMap[y] = realMap[y].substring(0, x) + cell + realMap[y].substring(x+1);
    }
    public void updateCell(Vector2 pos, char cell){
        updateCell(pos.x, pos.y, cell);
    }

    //insere um objeto no mapa
    public void Insert(Entity... objects){
        for(Entity i : objects){
            if(i instanceof Player) player = (Player) i;
            if(i instanceof Enemy) enemies.add((Enemy) i);
            instances.add(i);
        }
        Update();
    }
    //remove o objeto do mapa (deve ser chamado dentro do objeto por exemplo "map.Remove(this);")
    public void Remove(Entity object){
        instances.remove(object);
        Update();
    }

    public boolean haveEntity(Entity obj){
        return instances.contains(obj);
    }

    private Entity getEntity(Entity e){
        for(Entity i : instances){
            if(i == e) return i;
        }
        return null;
    }

    //checa se um objeto pode se mover para uma posiçao
    public boolean canMove(Vector2 newPos){
        Entity pos = getCell(newPos);
        if(pos.getStyle() == '#' || pos instanceof Actor){
            return false;
        }
        return true;
    }

    private void UpdateEnemies(){
        for(Enemy e : enemies){
            e.Update(this);
        }
    }
}
