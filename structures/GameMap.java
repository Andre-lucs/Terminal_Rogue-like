package structures;

import java.util.ArrayList;
import main.*;
import java.util.Arrays;
import java.util.Random;

public class GameMap
{
    private String[] initialMap;

    private String[] realMap;//mapa armazenado em tempo real com as instancias dos objetos representados
    private ArrayList<Entity> instances;//Lista de objetos instanciados no mapa
    private Vector2 mapSize = new Vector2(0);//dimençoes do mapa
    private boolean showed = false;//se deve imprimir ele ao modificar
    private Player player;
    private ArrayList<Enemy> enemies;
    public boolean[][] visibility;
    protected boolean[][] canWalkInto;
    public Enemy Boss;
    private ArrayList<Vector2> freePosition;
    private ArrayList<Vector2> positionsNotVisitedByGenerator;

    protected GameMap()
    {
        mapSize.x = initialMap[0].length();
        mapSize.y = initialMap.length;
        instances = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        this.realMap = this.initialMap.clone();
        this.visibility = new boolean[mapSize.y][mapSize.x];

        Arrays.fill(visibility[0], true);
        Arrays.fill(visibility[mapSize.y-1], true);
        for(int i = 1; i < mapSize.y; i++){
            visibility[i][0] = true;
            visibility[i][mapSize.x-1] = true;
        }
    }
    protected GameMap(String[] newMap)
    {
        this.initialMap = newMap.clone();
        mapSize.x = initialMap[0].length();
        mapSize.y = initialMap.length;
        instances = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        this.realMap = this.initialMap.clone();
        this.visibility = new boolean[mapSize.y][mapSize.x];

        Arrays.fill(visibility[0], true);
        Arrays.fill(visibility[mapSize.y-1], true);
        for(int i = 1; i < mapSize.y; i++){
            visibility[i][0] = true;
            visibility[i][mapSize.x-1] = true;
        }
    }
    protected GameMap(GameMap pastMap)
    {
        this(pastMap.initialMap);
        instances = new ArrayList<Entity>(pastMap.instances);
    }
    protected GameMap(GameMap pastMap, boolean[][] visited)
    {
        this(pastMap);
        canWalkInto = visited;
        instances = new ArrayList<Entity>(pastMap.instances);
        freePosition = new ArrayList<>();
        positionsNotVisitedByGenerator = new ArrayList<>();
        for(int i = 0;i <mapSize.y;i++){
            for(int j = 0;j<mapSize.x;j++){
                Vector2 pos = new Vector2(j,i);
                if(getCell(pos).getStyle() == ' ' && canWalkInto[i][j]){ freePosition.add(pos);}
                if(getCell(pos).getStyle() == ' ' && !canWalkInto[i][j]) {positionsNotVisitedByGenerator.add(pos);}
            }
        }
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
            newMap.updateCell(i.getPosition(), i.getStyle());
        };
        this.realMap = newMap.realMap;
        if(player != null){
            updateCell(player.getPosition(), player.getStyle());//jogador pode ficar em cima de items e cartas
            setVisible(player.getPosition(), 5);
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
            if(getCell(i.getPosition()).getStyle() == '#') {
                if(i instanceof Enemy && i.getStyle() == 'B'){
                    i.setPosition(getRandomFreePosition());
                    Boss =(Enemy) i;
                    enemies.add((Enemy) i);
                    instances.add(i);
                    freePosition.remove(i.getPosition());
                }
                if(i instanceof Player){
                    i.setPosition(getRandomFreePosition());
                    player = (Player)i;
                    instances.add(i);
                    freePosition.remove(i.getPosition());
                }
                continue;
            }
            if(i instanceof Player) player = (Player) i;
            if(i instanceof Enemy) {
                if(i.getStyle() == 'B'){
                    Boss = (Enemy)i;
                }
                enemies.add((Enemy) i);
            }
            instances.add(i);
            freePosition.remove(i.getPosition());
        }
        Update();
    }

    public void Remove(Entity object){
        instances.remove(object);
        if(object instanceof Enemy){
            enemies.remove(object);
            if(object == Boss) Boss = null;
        }
        if( object instanceof Player){
            player = null;
        }
        freePosition.add(object.getPosition());
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
            if(e.getStatus() == Status.DEAD) {
                Enemy dead = e;
                Remove(e);
                dead.onDeath(this);
                break;
            }
        }
    }

    public void setVisible(Vector2 pos, int range){
        if(range <= 0) return;
        try{
            visibility[pos.y][pos.x] = true;
        }catch(ArrayIndexOutOfBoundsException e){
            return;
        }
        if(this.realMap[pos.y].charAt(pos.x) == '#') return;

        Vector2 pUp = new Vector2(pos),pDw = new Vector2(pos),pR = new Vector2(pos),pL = new Vector2(pos);

        pUp.y += 1;
        pDw.y -= 1;
        pR.x += 1;
        pL.x -= 1;
        setVisible(pUp, range-1);
        setVisible(pDw, range-1);
        setVisible(pR, range-1);
        setVisible(pL, range-1);
    }
    public Vector2 getRandomFreePosition(){
        Random rdn = new Random(System.nanoTime());
        Vector2 pos = freePosition.get(rdn.nextInt(freePosition.size()));
        return pos;
    }
    public Vector2 getPositionOnWall(){
        Random rdn = new Random(System.nanoTime());
        Vector2 pos = positionsNotVisitedByGenerator.get(rdn.nextInt(positionsNotVisitedByGenerator.size()));
        return pos;
    }

}
