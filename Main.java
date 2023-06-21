import java.util.Scanner;
import structures.*;
import main.*;

public class Main {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Game game;

        game = new Game();
        game.Start();

        scanner.close();
    }
}
