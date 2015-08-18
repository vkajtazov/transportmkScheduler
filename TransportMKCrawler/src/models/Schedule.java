package models;

import java.sql.Time;

public class Schedule implements BasicEntity{

	private VehicleType vehicleType;

	private String transporter;

	private RegularityType regularityType;

	private Time departureTime;

	private Time arrivalTime;

	private int price;

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public RegularityType getRegularityType() {
		return regularityType;
	}

	public void setRegularityType(RegularityType regularityType) {
		this.regularityType = regularityType;
	}

	public Time getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Time departureTime) {
		this.departureTime = departureTime;
	}

	public Time getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Time arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Schedule [vehicleType=" + vehicleType + ", transporter="
				+ transporter + ", regularityType=" + regularityType
				+ ", departureTime=" + departureTime + ", arrivalTime="
				+ arrivalTime + ", price=" + price + "]";
	}
	
	

}
