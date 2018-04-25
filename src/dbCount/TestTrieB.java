package dbCount;

import java.util.TreeSet;

public class TestTrieB {
	
	public static void testInsert() {
		Trie t1 = new Trie();
		t1.insert("hey");
		t1.insert("heyoy");
		t1.insert("heyyo");
		t1.insert("heyaidnajonasoinf");
		t1.insert("hiyou");
		//t1.print();
		System.out.println();
		TreeSet<String> words = t1.getPrefix("hey");
		for (String s : words) {
			//System.out.println(s);
		}
		t1.remove("hey");
		t1.print();
		t1.remove("heyoy");
		t1.print();
		
//		Trie t1 = new Trie();
//		t1.insert("first");
//		t1.insert("second");
//		t1.insert("third");
//		//assertTrue(true);
//		//assertTrue(t1.contains("second"));
//		//assertTrue(t1.contains("third"));
//		System.out.println(t1.contains("first"));
//		System.out.println(t1.contains("second"));
//		System.out.println(t1.contains("third"));
//		System.out.println(t1.contains("fourth"));
//		
//		t1.insert("temp");
//		t1.insert("temp1");
//		t1.insert("temper");
//		t1.insert("temporary");
//		t1.insert("temple");
//		t1.insert("tempmultuous");;
//		t1.insert("temperary");
//		t1.insert("tepmle");
//		t1.insert("mfkr");
//		//t1.printTrie();
//		TreeSet<String> test1 = t1.getPrefix("temp");
//		for (String s : test1) {
//			System.out.println(s);
//		}
//		//System.out.println("break:::\n\n");
//		TreeSet<String> test2 = t1.getPrefix("mf");
//		for (String s : test2) {
//			System.out.println(s);
//		}
	}
}
