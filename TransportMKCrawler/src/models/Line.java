package models;

import java.sql.Time;
import java.util.List;


public class Line implements BasicEntity{

	private String lineName;

	private List<Subline> sublines;

	private Station startingStation;

	private Station arrivingStation;

	private List<Schedule> scheduleList;

	private Time traveledTime;

	private float kmTraveled;

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public List<Schedule> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(List<Schedule> scheduleList) {
		this.scheduleList = scheduleList;
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

	public List<Subline> getSublines() {
		return sublines;
	}

	public void setSublines(List<Subline> sublines) {
		this.sublines = sublines;
	}

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

}

