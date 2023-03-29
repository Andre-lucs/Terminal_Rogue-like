import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        final String DEFAULT_SAVE_NAME = "SaveFile.dat";
        
        Scanner scanner = new Scanner(System.in);

        String name;

        System.out.print("Digite o nome para o arquivo de save: ");
        name = scanner.nextLine();

        Save s = new Save(name);
        HandleSave.save(DEFAULT_SAVE_NAME, s);

        Save save = HandleSave.getSave(DEFAULT_SAVE_NAME);

        System.out.println("Bem vindo ao Jogo " + save.getName());
    }
}