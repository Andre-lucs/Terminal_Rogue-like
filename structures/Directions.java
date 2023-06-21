package structures;

public enum Directions {
    UP(0, -1),
    DOWN(0, 1),
    RIGHT(1, 0),
    LEFT(-1, 0);

    private final int x;
    private final int y;

    Directions(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Directions getDir(int n){
        if(n == 1) return UP;
        if(n == 2) return RIGHT;
        if(n == 3) return DOWN;
        if(n == 4) return LEFT;
        return UP;
    }
}
