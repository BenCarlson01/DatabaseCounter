package dbCount;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Database {
	
	private final Pattern format = Pattern.compile("(.*): (.*)");
	
	/*
	 * Add sublist, Shows all other counts previously counted
	 * 
	 * 
	 * 
	 */
	
	/*
	private HashMap<String, Integer> map;
	private String[] names;
	private int[] counts;
	*/
	private TreeSet<String> order;
	private HashMap<String, Integer> data;
	private Trie trie;
	private String name;
	
	/*
	 * Load database with name or if does not exist create an empty database
	 * All lines in database should be of form "Name: count\n"
	 */
	public Database(String name) {
		order = new TreeSet<>(new TreeComp());
		data = new HashMap<>();
		trie = new Trie();
		this.name = name;
		try {
            FileReader fileReader = new FileReader("Saved Databases/" + name);
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
                trie.insert(parts[0]);
            }
            bufferedReader.close();         
        } catch(FileNotFoundException e) {
    		//Don't need to do anything
        } catch(IOException e) {
            e.printStackTrace();
		}
	}
	
	/*
	 * Adds a new item to database
	 */
	public void put(String n, int c) {
		order.add(n);
		data.put(n, c);
		trie.insert(n);
	}
	
	/*
	 * Removes an item from the database, if it is in the database
	 */
	public void remove(String n) {
		if (data.keySet().contains(n)) {
			data.remove(n);
			order.remove(n);
			trie.remove(n);
		}
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
	
	public TreeSet<String> getPrefix(String s) {
		return trie.getPrefix(s);
	}
	/*
	 * Saves data to a text file named name
	 */
	public void save() {
		try {
			PrintWriter writer = new PrintWriter("Saved Databases/" + name, "UTF-8");
            for (String s : order) {
    			writer.println(s + ": " + data.get(s));
            }
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	/*
	 * Helper compare method for ordering
	 */
	public int comp(String a, String b) {
		int c = a.toLowerCase().compareTo(b.toLowerCase());
		if (c == 0) {
			if (a.length() == 0) {
				return 0;
			}
			char c1 = a.charAt(0);
			char c2 = b.charAt(0);
			if (c1 == c2) {
				return 0;
			} else if (c1 > c2) {
				return -1;
			} else {
				return 1;
			}
		}
		return c;
	}
	
	private class TreeComp implements Comparator<String> {
		@Override
		public int compare(String a, String b) {
			return comp(a, b);
		}
	}

}
