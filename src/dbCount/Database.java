package dbCount;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Database {
	
	private final Pattern format = Pattern.compile("(.*): (.*)");
	
	/*
	private HashMap<String, Integer> map;
	private String[] names;
	private int[] counts;
	*/
	private TreeSet<String> order;
	private HashMap<String, Integer> data;
	private String name;
	
	/*
	 * Load database with name or if does not exist create an empty database
	 * All lines in database should be of form "Name: count\n"
	 */
	public Database(String name) {
		order = new TreeSet<>(new TreeComp());
		data = new HashMap<>();
		this.name = name;
		try {
            FileReader fileReader = new FileReader(name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length != 2 || !parts[1].matches("[-+]?\\d*\\.?\\d+")) {
                	System.out.println("Database formatting error");// :Line " + count);
                    bufferedReader.close();  
                	return;
                }
                order.add(parts[0]);
                data.put(parts[0], Integer.parseInt(parts[1]));
            }
            bufferedReader.close();         
        }
        catch(FileNotFoundException e) {
    		order = new TreeSet<>();
    		data = new HashMap<>();
        }
        catch(IOException e) {
            e.printStackTrace();
		}
	}
	
	/*
	 * Adds a new item to database
	 */
	public void put(String n, int c) {
		order.add(n);
		data.put(n, c);
	}
	
	/*
	 * Returns the count of an item
	 * Throws a NullPointerException if item not in database
	 */
	public int getCount(String n) {
		if (data.keySet().contains(n)) {
			System.out.println("Item is not in Database");
			throw new NullPointerException();
		}
		return data.get(n);
	}
	
	public TreeSet<String> getData() {
		return order;
	}
	
	public HashMap<String, Integer> getMap(){ 
		return data;
	}
	/*
	 * Saves data to a text file named name
	 */
	public void save() {
		try {
			PrintWriter writer = new PrintWriter(name, "UTF-8");
            for (String s : order) {
    			writer.println(s + ": " + data.get(s));
            }
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	private class TreeComp implements Comparator<String> {
		@Override
		public int compare(String a, String b) {
			if (a.toLowerCase().equals(b.toLowerCase())) {
				return a.compareTo(b);
			}
			return a.toLowerCase().compareTo(b.toLowerCase());
		}
	}

}
