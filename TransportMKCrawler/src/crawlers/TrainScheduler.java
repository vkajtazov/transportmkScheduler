package crawlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import models.Line;

public class TrainScheduler {

	private static String path = "train schedule.txt";

	public static ArrayList<Line> parseAllTrainPdfs() {
		ArrayList<String> list = null;
		ArrayList<Line> result = new ArrayList<Line>();
		try {
			list = readFromDirectoriesNames(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String string : list) {
			result.addAll(getTrainSchedule(string));
		}
		return result;
	}

	private static ArrayList<Line> getTrainSchedule(String string) {
		String[] parseStrings = string.split(";");

		String pathToFile = parseStrings[0];
		int startRow = Integer.parseInt(parseStrings[1]);
		int endRow = Integer.parseInt(parseStrings[2]);
		int numCols = Integer.parseInt(parseStrings[3]);

		String[] parseSkipRows = parseStrings[4].split(",");
		HashSet<Integer> skipRows = new HashSet<Integer>();
		for (String skipRow : parseSkipRows) {
			skipRows.add(Integer.parseInt(skipRow));
		}
		TrainScheduleParserFromPdf schedule = new TrainScheduleParserFromPdf(
				pathToFile, skipRows, startRow, endRow, numCols);

		return schedule.getAll();
	}

	public static ArrayList<String> readFromDirectoriesNames(String path)
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		ArrayList<String> resultList = new ArrayList<String>();

		try {

			String line = br.readLine();
			while (line != null) {
				resultList.add(line);
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return resultList;
	}

}
