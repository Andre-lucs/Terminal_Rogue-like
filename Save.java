import java.io.Serializable;

public class Save implements Serializable {

	private String SaveName;
	//future objects that need to be saved

	public Save(){
	}
	public Save(String saveName){
		this.setName(saveName);
	}

	public void setName(String saveName){
		this.SaveName = saveName;
	}
	public String getName(){
		return this.SaveName;
	}
}
