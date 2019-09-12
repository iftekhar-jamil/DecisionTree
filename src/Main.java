import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.naming.spi.DirStateFactory.Result;

public class Main {

	static ArrayList<Data> data;
	static ArrayList<String> weather,humidity, temperature, windy, res;
	static double mainEntropy; 
	static Node root;
	public static void main(String[] args) throws URISyntaxException {
		URL path = ClassLoader.getSystemResource("data.txt");
		
		data = new ArrayList<Data>();
		weather = new ArrayList<String>();
		humidity = new ArrayList<String>();
		temperature = new ArrayList<String>();
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
				temperature.add(arr[2]);
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
		
		mainEntropy = entropy(res);
		makeDecisionTree();
		printDecisionTree();
	}

	public static void printDecisionTree() {
		// TODO Auto-generated method stub
		System.out.println(root.value+"root");
		for(int i=0; i<root.children.size(); i++) {
			System.out.println();
			System.out.print("   "+root.children.get(i).value);
			boolean flag = false;
			for(int j=0; j<root.children.get(i).children.size(); j++) {
				if(j==0) System.out.println();
				String decision = findDecision(root.children.get(i).value,root.children.get(i).children.get(j).value);
				System.out.println("\n      "+root.children.get(i).children.get(j).value+"-->"+decision);
				flag = true;
			}
			
			if(!flag)
				System.out.println("-->"+findDecision(root.children.get(i).value, "")+"\n");
		}
	}

	private static String findDecision(String value, String value2) {
		// TODO Auto-generated method stub
		int yes=0,no=0;
		for(int i=0; i<data.size(); i++)
			if(data.get(i).toString().contains(value) && data.get(i).toString().contains(value2))
				if(res.get(i).equals("Yes"))
					yes++;
				else no++;
		
		return yes>no? "Yes":"No";
	}

	public static void makeDecisionTree() {
		// TODO Auto-generated method stub
		ArrayList<String>[] test = new ArrayList[4];
		test[0] = weather;
		test[1] = humidity;
		test[2] = temperature;
		test[3] = windy;
		
		ArrayList<String> first =  getBestAttribute(test,"");
		root = new Node("");
		
		for(int i=0; i<4;i++)
			if(test[i].equals(first)) {
				test[i] = new ArrayList<>();
				break;
			}	
			
			
		
		HashSet<String> hs = new HashSet<String>();
		for (int i = 0; i < first.size(); i++)
			hs.add(first.get(i));
		
		Iterator<String> i=hs.iterator();
		
		ArrayList<String>[] second = new ArrayList[hs.size()];
		
		int kk = 0;
		//Node [] child = new Node[root.arr.size()];
		
		while(i.hasNext()) {
			 String tmp  = i.next();
			 root.children.add(new Node(tmp));
			 if(entropy(tmp)==0.0) {
				 second[kk] = new ArrayList();
				 kk++;
				 continue;
			 }
			 second[kk]= getBestAttribute(test, tmp);
			 
			 
			 kk++;
		}
		
	
		for(int it=0; it<root.children.size(); it++) {
			hs = new HashSet<>();
			for(int j=0; j<second[it].size(); j++)
				hs.add(second[it].get(j));
			i = hs.iterator();
			while(i.hasNext())
				root.children.get(it).children.add(new Node(i.next()));
		}
		
		

	}
	

	public static ArrayList<String> getBestAttribute(ArrayList<String> arr[], String str) {
		// TODO Auto-generated method stub
		double min = 1000000.0;
		
		ArrayList<String> minFeature = null;
		for(int i=0; i<arr.length; i++) {
			//if(data.get(i).toString().contains(str))
			if(arr[i].size()==0) continue;
			if(featureBasedSplit(arr[i],str)<min) {
				min = featureBasedSplit(arr[i],str);
				minFeature = arr[i];
			}
		}
		//if(min==0) minFeature = new ArrayList<>();
		return minFeature;
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

		if(yes==0 || no==0)  return 0;
		entropy = 0 - (double) no / (double) (yes + no)*log2((double) no / (double) (yes + no)) - (double) yes / (double) (yes + no)*log2((double) yes / (double) (yes + no));
		return entropy;
	}

	public static double log2(double x) {
		return (double) (Math.log(x) / Math.log(2));
	}

	
	public static double featureBasedSplit(ArrayList<String> arr, String str) {
		// TODO Auto-generated method stub
		int yes = 0, no = 0;
		HashSet<String> hs = new HashSet<String>();
		for (int i = 0; i < arr.size(); i++)
			hs.add(arr.get(i));
		
		 Iterator<String> i=hs.iterator();  
		 
		 double finalEntropyForAttribute  = 0.0;
		 //i.next();
         while(i.hasNext())  
         {  
        	 double temptropy = 0.0;
        	 ArrayList<String> temp =  new ArrayList<>();
        	 String s = i.next();
        	 for(int j=0; j<res.size(); j++) {
 				
        		 if(data.get(j).toString().contains(s) && data.get(j).toString().contains(str)) {
        			temp.add(res.get(j));
 				}
 			}
        	 temptropy = entropy(temp);
        	 finalEntropyForAttribute+=(double)((double)temp.size()/(double)res.size())*temptropy;
         }  
		
		return finalEntropyForAttribute;
	}
	
	public static double entropy(String s) {
		int yes=0, no=0;
		for(int i=0; i<res.size(); i++) {
			if(data.get(i).toString().contains(s)) {
				if(res.get(i).equals("Yes"))
					yes++;
				else no++;
			}
		}
		if(yes==0 || no==0) return 0.0;
		double entropy = 0 - (double) no / (double) (yes + no)*log2((double) no / (double) (yes + no)) - (double) yes / (double) (yes + no)*log2((double) yes / (double) (yes + no));
		return entropy;
	}

	//ArrayList<String> second

}
