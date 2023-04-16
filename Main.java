import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        final String DEFAULT_SAVE_NAME = "SaveFile.dat";
        
        Scanner scanner = new Scanner(System.in);
        Save save;
        boolean continuar;

        do{
            System.out.print("Digite o nome para o arquivo de save: ");
            name = scanner.nextLine();
            System.out.println("Deseja criar o save de novo? Sim = 1");
            
            Save s = new Save(name);
            HandleSave.save(DEFAULT_SAVE_NAME, s);
            save = HandleSave.getSave(DEFAULT_SAVE_NAME);
    

            System.out.println("O que você deseja fazer?\n"+
        "1 - Criar um save\n"+
        "2 - Carregar um save");

        int option = scanner.nextInt();
        scanner.nextLine();
        switch(option){
            case 1: //criar um save            
                String name;
                System.out.print("Digite o seu nome para o save: ");
                name = scanner.nextLine();
                Save s = new Save(name);
                HandleSave.save(DEFAULT_SAVE_NAME, s);
            break;
            case 2:
                Save save = HandleSave.getSave(DEFAULT_SAVE_NAME);
                System.out.println("Bem vindo ao Jogo " + save.getName());

            break;
        }


            continuar = (scanner.nextInt() == 1) ? true: false;
            scanner.nextLine();
        }while(continuar == true);
        




    }
}