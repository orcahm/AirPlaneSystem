import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * All operations running in this class
 * @author Ahmet oruc
 *
 */
public class Operation {
	
	private ReadFile file;					//created for reading file 
	private FileWrite output;				//results print it out to this file
	private ArrayList<City> cityList;		//all city object hold by this
	private ArrayList<Route> routeList;	//for hold all routes for one command this arraylist  is created
	
	public Operation(String [] args) throws IOException, ParseException{
		
		cityList = new ArrayList<City>();
		routeList = new ArrayList<Route>();
		output = new FileWrite(args[3]);
		
		// cities and airports which is read from city.txt file is a graph corner which is created here
		file = new ReadFile(args[0]);
		for(int i=0;i<file.getLine().size();i++){
			if(file.getLine().get(i).isEmpty()){
				file.getLine().remove(i--);
				continue;
			}
			cityList.add(new City(file.getLine(i)));
			for(int j=0;j<cityList.get(i).getAirportList().size();j++){
				cityList.get(i).getAirportList().get(j).setCity(cityList.get(i));
			}
		}
		
		//information from flight.txt is used for connect corners of graph
		file = new  ReadFile(args[1]);
		for(int i=0;i<file.getLine().size();i++){
			if(file.getLine().get(i).isEmpty()) continue;
			Flight temp = new Flight(file.getLine(i),cityList);
			temp.getStartAirport().addFlight(temp);
		}
		
		//for take commands from commands.txt file and print them out , runCommand function is called.
		file = new ReadFile(args[2]);
		for(int i=0;i<file.getLine().size();i++){
			if(file.getLine().get(i).isEmpty()) continue;
			runCommand(file.getLine(i));
			routeList.clear();			// for second command it clear the list
		}
		
		output.close();
			
	}
	/**
	 * split command line
	 * and that commands tells which flight is wanted and search it and print it out
	 * @param line ş,me from command file
	 * @throws ParseException date format parsing exception
	 * @throws IOException file not found exception
	 */
	public void runCommand(String line) throws ParseException, IOException{
		Date startDate = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
		ArrayList<Flight> route = new ArrayList<Flight>();  //arraylist for creating routes.
		String [] splitCommand = line.split("\t"); 			//lines from command.txt splitted here
		String [] splitRoute = splitCommand[1].split("->"); //split for take which cities wanted
		startDate = df.parse(splitCommand[2]);
		
		for(int i=0;i<cityList.size();i++){					//find starting city
			if(cityList.get(i).getName().equals(splitRoute[0])){
				cityList.get(i).routeFind(route, routeList, splitRoute[1],startDate);  //to find flights from start city routeFind function is called
				break;
			}
		}
		
		if(splitCommand[0].equals("listall")){		// if command is listAll then write all routes which is just found
			output.write(routeList, line);
		}else{										//for other command it send them to properFlight function
			properFlight(line,splitCommand[0]);
		}
		
	}
	/**
	 * cheapest routes
	 * quickest routes
	 * and proper routes are found here according to need
	 * @param line from command file every line is hold line by line in here
	 * @param comment command which will be run
	 * @throws ParseException for conditionalFlight function exception control
	 * @throws IOException exception for not find the file
	 */
	public void properFlight(String line,String comment) throws ParseException, IOException{
		
		ArrayList<Route> tempProperRoute ;	
		ArrayList<Route> tempCheapestRoute ;
		ArrayList<Route> tempQuickestRoute;
		
		if(comment.equals("listcheapest")){					// find cheapest routes
			tempCheapestRoute = new ArrayList<Route>();
			cheapestRoute(tempCheapestRoute);
			output.write(tempCheapestRoute, line);
		}else if(comment.equals("listquickest")){			// find quickest routes
			tempQuickestRoute = new ArrayList<Route>();
			quickestRoute(tempQuickestRoute);
			output.write(tempQuickestRoute, line);
		}else{												//find proper routes
			tempCheapestRoute = new ArrayList<Route>();
			cheapestRoute(tempCheapestRoute);
			tempQuickestRoute = new ArrayList<Route>();
			quickestRoute(tempQuickestRoute);
			tempProperRoute = new ArrayList<Route>();
			combineCheapestAndQuickest(tempCheapestRoute, tempQuickestRoute, tempProperRoute);
			if(comment.equals("listproper")){				//print proper routes
				output.write(tempProperRoute, line);
			}else{											
				conditionalFlight(tempProperRoute, line);	//find conditional routes from proper routes
				output.write(tempProperRoute, line);		//print them to the file
			}
		}
		
	}
	/**
	 * cheapest route finder function
	 * @param tempCheapestRoute
	 */
	public void cheapestRoute(ArrayList<Route> tempCheapestRoute){
		
		int minPrice = 0;
		
		//find minimum money
		for (int i = 0; i < routeList.size(); i++) {   
			if(minPrice==0 || minPrice>routeList.get(i).getPrice()){
				minPrice = routeList.get(i).getPrice();
			}
		}
		//if it is ok for minimum add it to routelist
		for (int i = 0; i < routeList.size(); i++) {
			if(minPrice==routeList.get(i).getPrice()){
				tempCheapestRoute.add(routeList.get(i));
			}
		}
		
		
	}
	/**
	 * find quickest route
	 * @param tempQuickestRoute
	 */
	public void quickestRoute(ArrayList<Route> tempQuickestRoute){
		
		long minTime = 0;
		//find minimum time route
		for (int i = 0; i < routeList.size(); i++) {
			if(minTime==0 || minTime>routeList.get(i).getTime()){
				minTime = routeList.get(i).getTime();
			}
		}
		//according to minimum create a quickest routes list
		for (int i = 0; i < routeList.size(); i++) {
			if(minTime==routeList.get(i).getTime()){
				tempQuickestRoute.add(routeList.get(i));
			}
		}
	
	}
	/**
	 * for proper flights this function merge cheapest and qucikest lists
	 * @param tempCheapestRoute	cheapest routes
	 * @param tempQuickestRoute	quickest routes
	 * @param tempProperRoute	proper routes
	 */
	public void combineCheapestAndQuickest(ArrayList<Route> tempCheapestRoute,ArrayList<Route> tempQuickestRoute,ArrayList<Route> tempProperRoute){
		//exclude dublicate objects from list
		for (int i = 0; i < tempCheapestRoute.size(); i++) {
			if(!tempQuickestRoute.contains(tempCheapestRoute.get(i))){
				tempProperRoute.add(tempCheapestRoute.get(i));
			}
		}
		tempProperRoute.addAll(tempQuickestRoute); //tempQuickestRoute list copies to   tempProperRoute list
	}
	/**
	 * function which is specify which condition will be run
	 * @param tempProper proper routes list
	 * @param line	command lines
	 * @throws ParseException for quickerRoute parsing exception
	 */
	public void conditionalFlight(ArrayList<Route> tempProper,String line) throws ParseException{
		
		String [] temp = line.split("\t");
		
		if(temp[0].equals("listcheaper")){
			cheaperRoute(tempProper, temp[3]);
		}else if(temp[0].equals("listquicker")){
			quickerRoute(tempProper, temp[3]);
		}else if(temp[0].equals("listexcluding")){
			excludingRoute(tempProper, temp[3]);
		}else if(temp[0].equals("listonlyfrom")){
			onlyFromRoute(tempProper, temp[3]);
		}
	}
	/**
	 * find routes which is cheaper than wanted money and those are from proper list
	 * @param tempProper proper routes list
	 * @param conditional wanted money limit
	 */
	
	public void cheaperRoute(ArrayList<Route> tempProper,String conditional){
		
		int tempPrice = Integer.parseInt(conditional);
		
		//check all routes money
		//if it is more expensive it deleted
		for (int i = 0; i < tempProper.size(); i++) {
			if(tempProper.get(i).getPrice()>=tempPrice){
				tempProper.remove(i--);
			}
		}
	}
	
	/**
	 * find route according to limit date
	 * @param tempProper proper routes list
	 * @param conditional limit date
	 * @throws ParseException parsing date exception
	 */
	public void quickerRoute(ArrayList<Route> tempProper,String conditional) throws ParseException{
		
		DateFormat finishDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm EEE");
		
		Date finish = finishDateFormat.parse(conditional);
		
		//route list's final arrive date
		//if it is after limit date than delete it
		for (int i = 0; i < tempProper.size(); i++) {																				
			if(tempProper.get(i).getFlightList(tempProper.get(i).getFlightList().size()-1).getFinishDate().after(finish)){
				tempProper.remove(i--);
			}
		}

	}
	/**
	 * routes which is not includes that airline
	 * @param tempProper proper route list
	 * @param conditional	airline which is not wanted
	 */
	
	public void excludingRoute(ArrayList<Route> tempProper,String conditional){
		
		for (int i = 0; i < tempProper.size(); i++) {
			for (int j = 0; j < tempProper.get(i).getFlightList().size(); j++) { 						//check all routes from list
				if(tempProper.get(i).getFlightList(j).getFlightId().startsWith(conditional)){			//if there is un wanted flight
					tempProper.remove(i--);																//then delete that route
					break;
				}
			}
		}
	}
	/**
	 * only specify airline is used in that flightlist
	 * @param tempProper proper routes list
	 * @param conditional wanted airline
	 */
	
	public void onlyFromRoute(ArrayList<Route> tempProper,String conditional){
		
		for (int i = 0; i < tempProper.size(); i++) {
			for (int j = 0; j < tempProper.get(i).getFlightList().size(); j++) {					//check all routes 
				if(!tempProper.get(i).getFlightList(j).getFlightId().startsWith(conditional)){		//if there is not wanted airline in that flight
					tempProper.remove(i--);															//delete that route
					break;
				}
			}
		}
	}
	
}
