import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * created for file writing operations
 * @author Ahmet Oruc
 *
 */
public class FileWrite {

	BufferedWriter bw;
	
	public FileWrite(String arg) throws IOException{
		bw = new BufferedWriter(new FileWriter(new File(arg)));
	}
	/**
	 * close output file which is job is done 
	 * @throws IOException file not found exception
	 */
	public void close() throws IOException{
		this.bw.close();
	}
	/**
	 * according to given command find right routes and print them out to output file 
	 * @param routeList arraylist which hold right routes
	 * @param commandLine command line which is executing that time
	 * @throws IOException file not found exception
	 */
	public void write(ArrayList<Route> routeList,String commandLine) throws IOException{
		
		StringBuilder strBuil = new StringBuilder(); // create string format 
		
		strBuil.append("Command :"+ commandLine+ "\n"); // write command line
		
		if(routeList.isEmpty()){
			strBuil.append("No suitable flight plan is found\n");
			bw.append(strBuil.toString());	
		}else{
			for (int i = 0; i < routeList.size(); i++) {
				for (int j = 0; j < routeList.get(i).getFlightList().size(); j++) {
					if(j!=0) strBuil.append("||");
					strBuil.append(routeList.get(i).getFlightList(j).getFlightId() + "\t"); 								//write flight id
					strBuil.append(routeList.get(i).getFlightList(j).getStartAirport().getName() + "->");					//flight's start airport
					strBuil.append(routeList.get(i).getFlightList(j).getFinishAirport().getName());							//flight's finish airport
				}
				int hour = (int) ((routeList.get(i).getTime()/ (1000*60*60))) ;
				strBuil.append("\t" + String.format("%02d",hour) + ":");		//tptal time of route
				int minute = (int) ((routeList.get(i).getTime()-hour*(1000*60*60))/ (1000*60));
				strBuil.append( String.format("%02d", minute)+ "/" +routeList.get(i).getPrice() + "\n");		
				bw.append(strBuil.toString());													//created string print out to output file
				strBuil.delete(0, strBuil.length());											//clear stringbuilder to create new string
			}
		}
		bw.append("\n\n");
	}
}
