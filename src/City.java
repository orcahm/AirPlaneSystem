import java.util.ArrayList;
import java.util.Date;

/**
 * class which is hold cities
 * @author Ahmet Oruc
 *
 */
public class City {

	private String name; //city name
	private ArrayList<Airport> airportList; //airports which is in this city
	private boolean isArrived; //when searching for route this is for to know if this city is visited or not
	
	/**
	 * constructor
	 * split line and create city
	 * @param line from file
	 */
	public City(String line){
		String [] splitLine = line.split("\t");
		this.name = splitLine[0];
		this.airportList = new ArrayList<Airport>();
		for(int i=1;i<splitLine.length;i++){
			airportList.add(new Airport(splitLine[i],splitLine[0]));
			airportList.get(i-1).setCity(this);
		}
		this.isArrived = false;
	}
	/**
	 * assign flights to airports which is representing edges
	 * @param temp a flight
	 */
	public void creatFlight(Flight temp){
		for(int i=0;i<this.airportList.size();i++){
			if(this.airportList.get(i).getName().equals(temp.getStartAirport())){
				this.airportList.get(i).addFlight(temp);
			}
		}
	}
	/**
	 *it travel all vertex and search for right route 
	 * @param route hold that route which is holding at that time
	 * @param routeList hold all routes
	 * @param finisfCity arrival city
	 */
	public void routeFind(ArrayList<Flight> route,ArrayList<Route> routeList,String finisfCity,Date startDate){
		this.isArrived = true;		// if i visited that city then true
		if(finisfCity.equals(this.name)){ // if we re in right place or not
			routeList.add(new Route()); 
			ArrayList<Flight> tempRoute = new ArrayList<Flight>();
			tempRoute.addAll(route); 
			routeList.get(routeList.size()-1).setFlightList(tempRoute); //for hold the right routes add them to list
			route.remove(route.size()-1); 			// when backward delete that route from list
			this.isArrived = false; 			//because of we re deleting that route which means we still haven't visited there
			return;
		}else{
			for(int i=0;i<this.airportList.size();i++){   //check all airports in that city
				for(int j=0;j<this.getAirportList(i).getFlightsList().size();j++){  //check every flight from that airport		
					if(!route.isEmpty()){	 //for transfer flight time checking is done here
						if(route.get(route.size()-1).getFinishDate().before(this.getAirportList(i).getFlightsList(j).getStartDate())){ //if before flights arrival time is before next flights departure time checking is here 
							if(!this.getAirportList(i).getFlightsList(j).getFinishAirport().getCity().getIsArrived()){
								route.add(this.getAirportList(i).getFlightsList(j));
								this.getAirportList(i).getFlightsList(j).getFinishAirport().getCity().routeFind(route, routeList, finisfCity,startDate);
							}
						}
					}else{ 
						if(!this.getAirportList(i).getFlightsList(j).getFinishAirport().getCity().getIsArrived()){ //for not transferred flights or for first flight
							if(startDate.before(this.getAirportList(i).getFlightsList(j).getStartDate())){
								route.add(this.getAirportList(i).getFlightsList(j));
								this.getAirportList(i).getFlightsList(j).getFinishAirport().getCity().routeFind(route, routeList, finisfCity,startDate);
							}
						}
					}
				}
			}
		}
		if(!route.isEmpty())
		route.remove(route.size()-1); 	// for backward delete last route
		this.isArrived = false;			//because of that change this city visited to unvisited
		
	}
	/**
	 * for find airport
	 * @param index wanted airport index of list
	 * @return a airport object
	 */
	public Airport getAirportList(int index) {
		return airportList.get(index);
	}
	
	//variable's getter and setter functions
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Airport> getAirportList() {
		return airportList;
	}
	public void setAirportList(ArrayList<Airport> airportList) {
		this.airportList = airportList;
	}
	public boolean getIsArrived() {
		return isArrived;
	}
	public void setIsArrived(boolean isArrived) {
		this.isArrived = isArrived;
	}


}
