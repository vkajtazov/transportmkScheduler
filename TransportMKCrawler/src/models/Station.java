package models;

public class Station implements BasicEntity{

	private String stationName;

	private double stationLatitude;

	private double stationLongitude;

	private City stationCity;

	public City getStationCity() {
		return stationCity;
	}

	public void setStationCity(City stationCity) {
		this.stationCity = stationCity;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public double getStationLatitude() {
		return stationLatitude;
	}

	public void setStationLatitude(double stationLatitude) {
		this.stationLatitude = stationLatitude;
	}

	public double getStationLongitude() {
		return stationLongitude;
	}

	public void setStationLongitude(double stationLongitude) {
		this.stationLongitude = stationLongitude;
	}

	@Override
	public String toString() {
		return "Station [stationName=" + stationName + ", stationLatitude="
				+ stationLatitude + ", stationLongitude=" + stationLongitude
				+ ", stationCity=" + stationCity + "]";
	}
	
	

}
