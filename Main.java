import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        final String DEFAULT_SAVE_NAME = "SaveFile.dat";
        
        Scanner scanner = new Scanner(System.in);

        String name;
        Save save;
        boolean continuar;

        do{
            System.out.print("Digite o nome para o arquivo de save: ");
            name = scanner.nextLine();
            System.out.println("Deseja criar o save de novo? Sim = 1");
            
            Save s = new Save(name);
            HandleSave.save(DEFAULT_SAVE_NAME, s);
            save = HandleSave.getSave(DEFAULT_SAVE_NAME);
    
            continuar = (scanner.nextInt() == 1) ? true: false;
            scanner.nextLine();
        }while(continuar == true);

        System.out.println("Bem vindo ao Jogo " + save.getName());
    }
}