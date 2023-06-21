package structures;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;

public class MapGenerator{
    private Vector2 size;
    private Vector2 startPosition;
    private boolean[][] visited;
    private char[][] map;

    public MapGenerator(Vector2 size, Vector2 startPos){
        this.size = size;
        this.startPosition = startPos;
    }

    public String[] getNewMap(){
        map = new char[size.y][size.x];
        visited = new boolean[size.y][size.x];
        Arrays.fill(map[0],'#');
        Arrays.fill(map[size.y-1],'#');
        Arrays.fill(visited[0], true);
        Arrays.fill(visited[size.y-1], true);
        for(int i = 1; i < size.y; i++){
            map[i][0] = '#';
            map[i][size.x-1] = '#';
            visited[i][0] = true;
            visited[i][size.x-1] = true;
        }

        step(startPosition);
        String[] newMap = new String[size.y];
        for(int i = 0; i < size.y; i++){
            for(int j = 0; j < size.x; j++){
                if(map[i][j] == Character.MIN_VALUE) map[i][j] = ' ';
            }
            newMap[i] = String.valueOf(map[i]);
        }
        return newMap;
    }

    private void step(Vector2 pos){
        if(map[pos.y][pos.x] == '#') return;

        pos = new Vector2(pos);

        while(visited[pos.y][pos.x+1] == false || visited[pos.y][pos.x-1] == false || visited[pos.y+1][pos.x] == false || visited[pos.y-1][pos.x] == false){
            Directions dir =Directions.getDir(ThreadLocalRandom.current().nextInt(1,5));
            if(visited[pos.y+dir.getY()][pos.x+dir.getX()] == true) continue;

            int distance = ThreadLocalRandom.current().nextInt(1,6);

            if(dir == Directions.UP || dir == Directions.DOWN){
                int y;
                for(y = pos.y+dir.getY(); y != pos.y+distance*dir.getY(); y+= dir.getY()){
                    if(visited[y][pos.x] == true) return;
					if(visited[y][pos.x+1] == false){
		                map[y][pos.x+1] = '#';
		                visited[y][pos.x+1] = true;
					}
					if(visited[y][pos.x-1] == false){
		                map[y][pos.x-1] = '#';
		                visited[y][pos.x-1] = true;
                    }
                    visited[y][pos.x] = true;
                }
                visited[y][pos.x] = true;
                pos = new Vector2(pos);
                pos.y = y;
            }else if(dir == Directions.LEFT || dir == Directions.RIGHT){
                int x;
                for(x = pos.x+dir.getX(); x != pos.x+distance*dir.getX(); x+= dir.getX()){
                    if(visited[pos.y][x] == true) return;
                    if(visited[pos.y+1][x] == false){
                        map[pos.y+1][x] = '#';
                        visited[pos.y+1][x] = true;
                    }
                    if(visited[pos.y-1][x] == false){
                        map[pos.y-1][x] = '#';
                        visited[pos.y-1][x] = true;
                    }
                    visited[pos.y][x] = true;
                }
                visited[pos.y][x] = true;
                pos = new Vector2(pos);
                pos.x = x;
            }
            step(pos);
            if(map[pos.y][pos.x] == '#') return;
        }
    }

    public void setPos(Vector2 position){
        this.startPosition = position;
    }

    public void setSize(Vector2 size){
        this.size = size;
    }
}
