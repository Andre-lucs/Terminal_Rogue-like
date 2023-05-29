package structures;

public enum Status
{
    ALIVE(1), DEAD(0);
    
    private final int value;
    
    Status(int n){
        this.value = n;
    }
    
    public int getValue(){
        return this.value;
    }
}
