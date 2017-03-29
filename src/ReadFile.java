import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 * class which is reading information from files
 * @author Ahmet Oruc
 *
 */
public class ReadFile {

	private ArrayList<String> line; //arraylist which is holding every line from file
	
	public ReadFile(String filename) throws IOException{
		this.line = new ArrayList<String>();
		String str = null;
		
		try{
			// the file which will be read is open
			BufferedReader bf = new BufferedReader(new FileReader(filename));  
			//every line will be added to arraylist until line will be null
			while((str=bf.readLine()) != null ){
				this.line.add(str);
			}
		}catch(Exception e){
			System.err.println("ERROR : " + e.getMessage()); // file not found exception
		}
	}

	public ArrayList<String> getLine() {
		return line;
	}
	
	public String getLine(int index) {
		return line.get(index);
	}
}