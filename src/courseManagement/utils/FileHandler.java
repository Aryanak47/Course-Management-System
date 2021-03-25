package courseManagement.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
	
	
	public static List<String> readData(String file) {
		String currentDirectory = System.getProperty("user.dir");
		List<String> data = new ArrayList<>();
		try ( BufferedReader reader =  new BufferedReader( new InputStreamReader( new FileInputStream(currentDirectory+"\\src\\"+file), "UTF-8" ))){
			String line = new String();
			while ( ( line = reader.readLine() ) != null ) {
				data.add(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	public static void writeData(String file,String data) {
		String currentDirectory = System.getProperty("user.dir");
		try ( BufferedWriter writer =  new BufferedWriter(new FileWriter(currentDirectory+"\\src\\"+file, true))){
			PrintWriter pw = new PrintWriter(writer); 
			pw.println(data);
			pw.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
