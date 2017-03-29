import java.util.ArrayList;

/**
 * airports are hold in this class
 * @author Ahmet Oruc
 *
 */
public class Airport {

	private String name; //name of airport
	private City city;		//city which is airport is
	private ArrayList<Flight> flightsList;	//all flights from that airport
	
	/**
	 *constructor
	 * @param name airport name
	 * @param cityName which airport is in
	 */
	public Airport(String name,String cityName){
	this.name = name;
		this.city = null;
		flightsList = new ArrayList<Flight>();
	}
	/**
	 * add flight to flight list
	 * @param temp created flight
	 */
	public void addFlight(Flight temp){
		this.flightsList.add(temp);
	}
	/**
	 * for reach flight from flightlist
	 * @param index index which is wanted object index
	 * @return a flight object
	 */
	public Flight getFlightsList(int index){
		return flightsList.get(index);
	}
	
	//variable's getter and setter functions
	public ArrayList<Flight> getFlightsList() {
		return flightsList;
	}
	public void setFlightsList(ArrayList<Flight> flightsList) {
		this.flightsList = flightsList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}

}
