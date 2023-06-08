package structures;

public class Vector2
{
    public int x, y;

    public Vector2(){
        this.x = this.y = -1;
    }

    public Vector2(Vector2 vector){
        this(vector.x, vector.y);
    }
    public Vector2(int xy){
        this.x = this.y = xy;
    }
    public Vector2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static Vector2 Null(){
        return new Vector2(-1);
    }

    public boolean isNull(){
        return (x == -1 || y == -1) ? true : false;
    }

    public String toString(){
        return String.format("%d , %d", x, y);
    }

    public static Vector2 getDistance(Vector2 v1, Vector2 v2){
        int x1 = v1.x, x2 = v2.x, y1 = v1.y, y2 = v2.y;
        return new Vector2(x2-x1, y2-y1);
    }

    public static boolean compareVectors(Vector2 v1, Vector2 v2){
        if(v1 == null || v2 == null) return false;
        return (v1.x == v2.x) && (v1.y == v2.y);
    }
}
