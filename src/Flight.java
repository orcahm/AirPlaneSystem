import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;;

/**
 * class which is hold flights
 * @author Ahmet Oruc
 *
 */
public class Flight {

	private String flightId;	
	private Date startDate;
	private Date finishDate;
	private int price;
	private int flightMinutes;		// time by minute 
	private Airport startAirport;	//departure
	private Airport finishAirport;	//arrival
	
	/**
	 * objects constructor
	 * @param line which is read from file
	 * @param cityList all cities
	 * @throws ParseException parse exception
	 */
	public Flight(String line,ArrayList<City> cityList) throws ParseException{
		
		String [] splitLine = line.split("\t");
		this.flightId = splitLine[0];
		this.price = Integer.parseInt(splitLine[4]);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm EEE");
		this.startDate = dateFormat.parse(splitLine[2]);
		
		//set flight time to minute
		String [] splitHour = splitLine[3].split(":");	
		flightMinutes = Integer.parseInt(splitHour[0])*60;
		flightMinutes += Integer.parseInt(splitHour[1]);
		Calendar cal = Calendar.getInstance();
		cal.setTime(getStartDate());
		
		//calculate final time of arrival
		this.finishDate = new Date();
		this.finishDate.setTime(startDate.getTime());
		cal.set(Calendar.MINUTE, this.flightMinutes+cal.get(Calendar.MINUTE));
		this.finishDate = cal.getTime();
		
		//find start and finish airports
		String [] splitRoute = splitLine[1].split("->");
		for(int i=0;i<cityList.size();i++){
			for(int j=0;j<cityList.get(i).getAirportList().size();j++){
				if(cityList.get(i).getAirportList(j).getName().equals(splitRoute[0])){
					this.startAirport = cityList.get(i).getAirportList(j);
				}
				if(cityList.get(i).getAirportList(j).getName().equals(splitRoute[1])){
					this.finishAirport = cityList.get(i).getAirportList(j);
				}
			}
		}
	}

	//variable's getter and setter functions
	public String getFlightId() {
		return flightId;
	}
	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Airport getStartAirport() {
		return startAirport;
	}
	public void setStartAirport(Airport startAirport) {
		this.startAirport = startAirport;
	}
	public Airport getFinishAirport() {
		return finishAirport;
	}
	public void setFinishAirport(Airport finishAirport) {
		this.finishAirport = finishAirport;
	}
	public int getFlightMinutes() {
		return flightMinutes;
	}
	public void setFlightMinutes(int flightMinutes) {
		this.flightMinutes = flightMinutes;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	
}
