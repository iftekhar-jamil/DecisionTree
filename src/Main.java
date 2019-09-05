import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class Main {

	static ArrayList<Data> data;
	static ArrayList<String> weather,humidity, temp, windy, res;
	
	public static void main(String[] args) throws URISyntaxException {
		URL path = ClassLoader.getSystemResource("data.txt");
		
		
		
		
		
		
		data = new ArrayList<Data>();
		weather = new ArrayList<String>();
		humidity = new ArrayList<String>();
		temp = new ArrayList<String>();
		windy = new ArrayList<String>();
		res = new ArrayList<String>();
		
		String fileName = "data.txt";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(new File(path.toURI()));

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String arr[] = line.split(",");
				weather.add(arr[0]);
				humidity.add(arr[1]);
				temp.add(arr[2]);
				windy.add(arr[3]);
				res.add(arr[4]);
				
				data.add(new Data(arr));  
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}

	}

	public static double entropy(ArrayList<String> arr1) {

		int cnt = (int) arr1.stream().distinct().count();
		double entropy = 0.0;
		int yes = 0, no = 0;
		for (int i = 0; i < arr1.size(); i++) {
			if (arr1.get(i).equals("No"))
				no++;
			else
				yes++;
		}

		entropy = 0 - log2((double) no / (double) (yes + no)) - log2((double) yes / (double) (yes + no));
		return entropy;
	}

	public static double log2(double x) {
		return (double) (Math.log(x) / Math.log(2));
	}

	public void featureBasedSplit(ArrayList<String> arr) {
		// TODO Auto-generated method stub
		int yes = 0, no = 0;
		HashSet<String> hs = new HashSet<String>();
		for (int i = 0; i < arr.size(); i++)
			hs.add(arr.get(i));
		
		 Iterator<String> i=hs.iterator();  
         while(i.hasNext())  
         {  
        	 double temptropy = 0.0;
        	 for(int j=0; j<res.size(); j++) {
 				
        		 if(i.next().equals(arr.get(j))) {
        			 if(res.get(j).equals("Yes"))
        				 yes++;
        			 else no++;
 				}
 			}
         }  
		
		
	

	}

}
