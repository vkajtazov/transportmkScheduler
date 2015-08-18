package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import models.Line;
import models.Station;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import serverImporters.LineImporter;
import crawlers.InterncityBusScheduler;
import crawlers.TrainScheduler;

public class TrainTest {

	public static void printLineList(List<Line> list, String fileName)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");

		for (Line line : list) {
			writer.print(line.toString());
			writer.println("------------------------------------------");
		}
		writer.close();
	}

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		String url = "https://transport-mk.herokuapp.com/data/rest/lines";
		
		//List<Line> trainList = TrainScheduler.parseAllTrainPdfs();
	//	LineImporter.printJSON(trainList);
		//printLineList(trainList,"trains.txt");
		List<Line> busList = InterncityBusScheduler.get();
//		LineImporter.sendPostToServer(busList);
		//printLineList(busList, "buses.txt");
		System.out.println(busList.size());
//		for(int i=0;i<busList.size();i++)
//			try{
//				LineImporter.sendPost(busList.get(i), url);
//			}
//			catch (RuntimeException e){
//				if(!e.getMessage().equals("200")){
//					i--;
//					System.out.println("Retry item "+ i);
//				}else{
//					System.out.println("200 OK item "+ i);
//				}
//			}
		}

}
