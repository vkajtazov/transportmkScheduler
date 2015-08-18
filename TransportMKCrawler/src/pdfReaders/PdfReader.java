package pdfReaders;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.OutputTarget;

public class PdfReader {
	
	public static String getTextFromPdf (String path){
		Document pdf = PDF.open(path);
		
	    StringBuilder text = new StringBuilder(1024);
	    pdf.pipe(new OutputTarget(text));	
	    
	    try {
			pdf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return text.toString();
	}
	
	public static String [] getRowsStringsFromPdf (String path){
		String text = getTextFromPdf(path);
		return text.split("\n");
	}
	
}
