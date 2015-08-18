package crawlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import models.Line;
import models.RegularityType;
import models.Schedule;
import models.Station;
import models.VehicleType;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InterncityBusScheduler {

	private static String urlRequest = "http://api.liniskiprevoz.gov.mk/vozniredovi?datefrom=2015-04-01&type=json";
	private static String name = "api.liniskiprevoz";
	private static String password = "asdh^$jhgFD334d$%";

	private static JSONArray getSchedule() {
		try {
			String authString = name + ":" + password;
			System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			System.out.println("Base64 encoded auth string: " + authStringEnc);

			URL url = new URL(urlRequest);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic "
					+ authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String result = sb.toString();
			result = result.trim();
			result = result.substring(1, result.length() - 1);
			result = result.replace("\\", "");
			JSONObject obj = new JSONObject(result);

			return obj.getJSONObject("VozniRedovi").getJSONArray("VozenRed");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Line> get() {
		JSONArray arr = getSchedule();
		List<Line> lineList = new ArrayList<Line>();
		for (int i = 0; i < arr.length(); i++) {
			lineList.addAll(getLines(arr.getJSONObject(i)));
		}

		return lineList;
	}

	private static int getOneWayLenght(JSONArray array) {
		String direction = null;
		int position = 0;
		while (true) {
			direction = array.getJSONObject(position).get("@Nasoka").toString();
			if (direction.equals("1") && position < array.length() - 1) {
				++position;
			} else {
				break;
			}
		}
		return position;
	}

	private static List<Line> getLines(JSONObject obj) {
		JSONArray array = obj.getJSONObject("DefinicijaNaVozniotRed")
				.getJSONArray("VozenRedDef");
		String direction;

		ArrayList<Line> lineList = new ArrayList<Line>();
		int position = getOneWayLenght(array);
		Line line = null;
		for (int i = 0; i < position - 1; i++) {
			for (int j = i + 1; j < position; j++) {
				try {
					line = getLine(i, j, array, obj);
				} catch (JSONException e) {
					if (e.getMessage().equals(
							"JSONObject[\"@VremeNaPoaganje\"] not found."))
						continue;
				}
				lineList.add(line);
			}
		}
		for (int i = array.length() - 1; i > position; i--) {
			for (int j = i - 1; j >= position; j--) {
				try {
					line = getLine(i, j, array, obj);
				} catch (JSONException e) {
					if (e.getMessage().equals(
							"JSONObject[\"@VremeNaPoaganje\"] not found."))
						continue;
				}
				lineList.add(line);
			}
		}

		return lineList;
	}

	private static Line getLine(int i, int j, JSONArray array, JSONObject obj) {

		Station start, end;
		String transporter;
		transporter = obj.get("@FirmaIme").toString();

		start = getStation(array.getJSONObject(i));
		end = getStation(array.getJSONObject(j));

		Line line = new Line();
		line.setStartingStation(start);
		line.setArrivingStation(end);

		Schedule schedule = new Schedule();
		schedule.setDepartureTime(getTimeFromIndex(obj, i));
		schedule.setArrivalTime(getTimeFromIndex(obj, j));
		schedule.setVehicleType(VehicleType.BUS);
		schedule.setTransporter(transporter);
		RegularityType regType = getRegularity(obj);
		schedule.setRegularityType(regType);
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		scheduleList.add(schedule);
		line.setScheduleList(scheduleList);
		return line;
	}

	private static Station getStationfromIndex(JSONObject obj, int index) {
		JSONArray array = obj.getJSONObject("DefinicijaNaVozniotRed")
				.getJSONArray("VozenRedDef");
		return getStation(array.getJSONObject(index));
	}

	private static Time getTimeFromIndex(JSONObject obj, int index) {
		// System.out.println(obj.getJSONObject("DefinicijaNaPoagjanja")
		// .toString());
		JSONArray array;
		try {
			array = obj.getJSONObject("DefinicijaNaPoagjanja")
					.getJSONObject("PoaganjeDef")
					.getJSONObject("VreminjaNaPoaganje")
					.getJSONArray("PoaganjeVreme");

		} catch (JSONException ex) {
			array = obj.getJSONObject("DefinicijaNaPoagjanja")
					.getJSONArray("PoaganjeDef").getJSONObject(0)
					.getJSONObject("VreminjaNaPoaganje")
					.getJSONArray("PoaganjeVreme");

		}

		String timeString;
		timeString = array.getJSONObject(index).get("@VremeNaPoaganje")
				.toString();

		return Time.valueOf(timeString.split("T")[1]);
	}

	private static Time parseTimeFromMinutes(int minutes) {
		int hour = minutes / 60;
		minutes = minutes % 60;
		return Time.valueOf(hour + ":" + minutes + ":00");

	}

	private static RegularityType getRegularity(JSONObject object) {
		JSONObject obj;
		try {
			obj = object.getJSONObject("DefinicijaNaPoagjanja").getJSONObject(
					"PoaganjeDef");

		} catch (JSONException ex) {
			obj = object.getJSONObject("DefinicijaNaPoagjanja")
					.getJSONArray("PoaganjeDef").getJSONObject(0);
		}

		String monday = obj.getJSONObject("Rezim").get("@Ponedelnik")
				.toString();
		String tuesday = obj.getJSONObject("Rezim").get("@Vtornik").toString();
		String wednesday = obj.getJSONObject("Rezim").get("@Sreda").toString();
		String thursday = obj.getJSONObject("Rezim").get("@Cetvrtok")
				.toString();
		String friday = obj.getJSONObject("Rezim").get("@Petok").toString();
		String saturday = obj.getJSONObject("Rezim").get("@Sabota").toString();
		String sunday = obj.getJSONObject("Rezim").get("@Nedela").toString();
		String praznici = obj.getJSONObject("Rezim").get("@DrzavniPraznici")
				.toString();
		String regularity = "";
		if (monday.equals("1"))
			regularity += ";Monday";
		if (tuesday.equals("1"))
			regularity += ";Tuesday";
		if (wednesday.equals("1"))
			regularity += ";Wednesday";
		if (thursday.equals("1"))
			regularity += ";Thursday";
		if (friday.equals("1"))
			regularity += ";Friday";
		if (saturday.equals("1"))
			regularity += ";Saturday";
		if (sunday.equals("1"))
			regularity += ";Sunday";
		if (praznici.equals("1"))
			regularity += ";Praznici";
		if (regularity.equals(";Monday;Tuesday;Wednesday;Thursday;Friday"))
			return RegularityType.MON_FRI;
		else if (regularity
				.equals(";Monday;Tuesday;Wednesday;Thursday;Friday;Saturday"))
			return RegularityType.MON_SAT;
		else if (regularity
				.equals(";Monday;Tuesday;Wednesday;Thursday;Friday;Saturday;Praznici"))
			return RegularityType.MON_SAT_HOLIDAYS;
		else if (regularity.equals(";Sunday;Praznici"))
			return RegularityType.SUN_HOLIDAYS;
		else if (regularity.equals(";Saturday;Sunday;Praznici"))
			return RegularityType.SAT_SUN_HOLIDAYS;
		else if (regularity
				.equals(";Monday;Tuesday;Wednesday;Thursday;Friday;Saturday;Sunday;Praznici"))
			return RegularityType.EVERYDAY;
		else if (regularity.equals(";Sunday"))
			return RegularityType.SUNDAY;
		// else if (
		// regularity
		// .equals(";Monday;Tuesday;Wednesday;Thursday;Friday;Praznici"))
		return RegularityType.MON_FRI_HOLIDAYS;
	}

	private static int parseTraveledMinutesFromString(String parseTime) {
		String[] timeString = parseTime.split("T")[1].split(":");
		int hour = Integer.valueOf(timeString[0]);
		int minutes = Integer.valueOf(timeString[1]);
		return hour * 60 + minutes;
	}

	private static Station getStation(JSONObject obj) {
		String stationName = obj.get("@Ime").toString();
		Station station = new Station();
		station.setStationName(stationName);
		return station;
	}

}
