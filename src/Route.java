import java.util.ArrayList;
/**
 * for hold routes which is from command file
 * @author Ahmet Oruc
 *
 */
public class Route {
	
	private ArrayList<Flight> flightList;    //flight list which makes route
 	private int price;						//routes total money
	private long time;						// routes total time in millisecond type
	
	public Route(){
		flightList = new ArrayList<Flight>();
		price = 0;
		time=0;
		
	}

	public ArrayList<Flight> getFlightList() {
		return flightList;
	}
	/**
	 * takes wanted flight from route list
	 * @param index wanted flight index 
	 * @return wanted flight
	 */
	public Flight getFlightList(int index){
		return flightList.get(index);
	}
	
	/**
	 * add route to list
	 * calculate route time and price
	 * @param flightList route's flight list
	 */
	public void setFlightList(ArrayList<Flight> flightList) {
		this.flightList = flightList;
		this.setTime(flightList.get(flightList.size()-1).getFinishDate().getTime()-flightList.get(0).getStartDate().getTime());
		for (int i = 0; i < flightList.size(); i++) {
			this.price += flightList.get(i).getPrice();
		}
		
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
