import java.util.Scanner;
import structures.*;
import main.*;

public class Main {

    public static void main(String[] args){
        final String DEFAULT_SAVE_NAME = "SaveFile.dat";
        Scanner scanner = new Scanner(System.in);
        Save save;
        boolean continuar;
        Game game;

        do{
            System.out.print("Digite o nome para o arquivo de save: ");
            String filename = scanner.nextLine();
            System.out.println("Deseja criar o save de novo? Sim = 1");

            Save s = new Save(filename);
            HandleSave.save(DEFAULT_SAVE_NAME, s);
            save = HandleSave.getSave(DEFAULT_SAVE_NAME);


            System.out.println("O que você deseja fazer?\n"+
        "1 - Criar um save\n"+
        "2 - Carregar um save\n"+
        "3 - Começar o jogo");

        int option = scanner.nextInt();
        scanner.nextLine();
        switch(option){
            case 1: //criar um save
                String name;
                System.out.print("Digite o seu nome para o save: ");
                name = scanner.nextLine();
                Save ns = new Save(name);
                HandleSave.save(DEFAULT_SAVE_NAME, ns);
            break;
            case 2:
                Save nsave = HandleSave.getSave(DEFAULT_SAVE_NAME);
                System.out.println("Bem vindo ao Jogo " + nsave.getName());

            break;
            case 3:
                game = new Game();
                game.Start();
            break;
        }


            continuar = (scanner.nextInt() == 1) ? true: false;
            scanner.nextLine();
        }while(continuar == true);

    }
}
