
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HandleSave{
	public static void save(String fileName, Save saveObject){
		try{
			FileOutputStream f = new FileOutputStream(new File(fileName));
			ObjectOutputStream o = new ObjectOutputStream(f);
	
			o.writeObject(saveObject);
	
			o.close();
			f.close();

		} catch(FileNotFoundException e){
			System.out.println(e.toString());
		} catch(IOException e){
			System.out.println(e.toString());
		}
	}

	public static Save getSave(String fileName){
		try {
			FileInputStream fi = new FileInputStream(new File(fileName));
			ObjectInputStream oi = new ObjectInputStream(fi);

			Save saveObject = (Save) oi.readObject();

			oi.close();
			fi.close();
			return saveObject;
		} catch(FileNotFoundException e){
			System.out.println(e.toString());
		} catch(IOException e){
			System.out.println(e.toString());
		} catch(ClassNotFoundException e){
			e.getStackTrace();
		}
		return null;
	}
}
