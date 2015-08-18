package models;

import java.sql.Time;

public class Subline implements BasicEntity{

	private Station startingStation;

	private Station arrivingStation;

	private Time traveledTime;

	private float kmTraveled;

	public Station getStartingStation() {
		return startingStation;
	}

	public void setStartingStation(Station startingStation) {
		this.startingStation = startingStation;
	}

	public Station getArrivingStation() {
		return arrivingStation;
	}

	public void setArrivingStation(Station arrivingStation) {
		this.arrivingStation = arrivingStation;
	}

	public Time getTraveledTime() {
		return traveledTime;
	}

	public void setTraveledTime(Time traveledTime) {
		this.traveledTime = traveledTime;
	}

	public float getKmTraveled() {
		return kmTraveled;
	}

	public void setKmTraveled(float kmTraveled) {
		this.kmTraveled = kmTraveled;
	}

}
