import java.io.Serializable;

public class Save implements Serializable {

	private String SaveName;
	private String[] Items;
	//future objects that need to be saved

	public Save(){
	}
	public Save(String saveName){
		this.setName(saveName);
	}
	public Save(String[] newItems){
		this.setItems(newItems);
	}
	public Save(String saveName, String[] newItems){
		this.setName(saveName);
		this.setItems(newItems);
	}

	public void setName(String saveName){
		this.SaveName = saveName;
	}
	public String getName(){
		return this.SaveName;
	}
	public void setItems(String[] newItems){
		this.Items = newItems;
	}
	public String[] getItems(){
		return this.Items;
	}
}
