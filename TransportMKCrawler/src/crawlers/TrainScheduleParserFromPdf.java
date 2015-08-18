package crawlers;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import models.Line;
import models.RegularityType;
import models.Schedule;
import models.Station;
import models.VehicleType;
import pdfReaders.PdfReader;

public class TrainScheduleParserFromPdf {

	private String pathToFile;
	private HashSet<Integer> skipRows;
	private int startRow;
	private int endRow;
	private int numCols;

	public TrainScheduleParserFromPdf(String pathToFile,
			HashSet<Integer> skipRows, int startRow, int endRow, int numCols) {
		super();
		this.pathToFile = pathToFile;
		this.skipRows = skipRows;
		this.startRow = startRow;
		this.endRow = endRow;
		this.numCols = numCols;
	}

	public ArrayList<Line> getAll() {
		ArrayList<Line> result = new ArrayList<Line>();

		ArrayList<String[]> list = parseSchedule(startRow, endRow);
		ArrayList<Line> trainList = getListOfTrainRelation(list,
				list.get(0).length);
		result.addAll(trainList);

		return result;
	}

	private ArrayList<String[]> parseSchedule(int start, int end) {
		// System.out.println(PdfReader.getTextFromPdf(pathToFile));
		String[] array = PdfReader.getRowsStringsFromPdf(pathToFile);
		ArrayList<String[]> result = new ArrayList<String[]>();

		for (int i = start; i < end; i++) {
			if (skipRows.contains(i))
				continue;
			String[] tokens = array[i].split("     ");
			result.add(tokens);
		}
		return result;
	}

	private ArrayList<Line> getListOfTrainRelation(ArrayList<String[]> list,
			int size) {
		// od matrica so raspored kreira site mozni termini za trgnuvanje i
		// pristignuvanje
		Line train = null;
		HashMap<Integer, Line> trainMap = new HashMap<Integer, Line>();

		ArrayList<Line> result = new ArrayList<Line>();

		for (int start = 0; start < list.size() - 1; start++) {
			for (int position = 1; position < numCols; position++) {
				for (int end = start + 1; end < list.size(); end++) {
					String[] startSchedule = null;
					String[] endSchedule = null;

					if (list.get(start).length < numCols) {
						startSchedule = tranformRowWithNumCols(
								list.get(start - 1)[0], list.get(start));
					} else {
						startSchedule = list.get(start);
					}
					if (list.get(end).length < numCols) {
						endSchedule = tranformRowWithNumCols(
								list.get(end - 1)[0], list.get(end));
					} else {
						endSchedule = list.get(end);
					}
					if (startSchedule[0].equals(endSchedule[0]))
						continue;
					train = getTrainBySchedule(startSchedule, endSchedule,
							position);

					if (train != null) {
						if (trainMap.containsKey(train.hashCode())) {
							System.out.println(train);
						} else {
							result.add(train);
							trainMap.put(train.hashCode(), train);
						}
					}
					train = null;
				}
			}
		}

		return result;
	}

	private Line getTrainBySchedule(String[] start, String[] end, int position) {
		Line train = setStartingParameters(start, position);
		if (train == null)
			return null;
		train = setArrivingParameters(train, end, position);
		return train;
	}

	private Line setStartingParameters(String[] schedule, int position) {
		// postavuva parametri od stanica kade sto trgnuva
		String start = schedule[0];
		String startTime = schedule[position];

		if (startTime.trim().equals("-"))
			return null;

		String[] splitTime = startTime.trim().split("\\.");

		Time time = Time.valueOf(splitTime[0] + ":" + splitTime[1] + ":00");

		Line train = new Line();
		Station startingStation = new Station();
		Schedule startSchedule = new Schedule();
		// System.out.println(start);
		startingStation.setStationName(Translate.translateWord(start));

		train.setStartingStation(startingStation);

		startSchedule.setDepartureTime(time);
		startSchedule.setVehicleType(VehicleType.TRAIN);
		startSchedule.setRegularityType(RegularityType.EVERYDAY);
		startSchedule.setTransporter("MZ");
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		scheduleList.add(startSchedule);
		train.setScheduleList(scheduleList);

		return train;
	}

	private String[] tranformRowWithNumCols(String cityName,
			String[] scheduleTimes) {
		String[] array = new String[numCols];

		array[0] = cityName;
		for (int i = 0; i < numCols - 1; i++) {
			array[i + 1] = scheduleTimes[i];
		}
		return array;

	}

	private Line setArrivingParameters(Line train, String[] schedule,
			int position) {
		// postavuva parametri od stanica kade sto trgnuva
		String end = schedule[0];

		String endTime = schedule[position];

		if (endTime.trim().equals("-"))
			return null;

		String[] splitTime = endTime.trim().split("\\.");
		// System.out.println(endTime.trim() + " " + splitTime.length+"   da");

		Time time = Time.valueOf(splitTime[0] + ":" + splitTime[1] + ":00");
		Station arrivingStation = new Station();
		arrivingStation.setStationName(Translate.translateWord(end));
		train.setArrivingStation(arrivingStation);
		train.getScheduleList().get(0).setArrivalTime(time);

		return train;
	}

}
