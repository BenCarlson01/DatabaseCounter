package dbCount;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class TrieTest {

	@Test
	public void testInsert() {
		Trie t1 = new Trie();
		HashSet<String> words = new HashSet<>(Arrays.asList(
				"Hello", "World", "Karel", "12345", "Karel1", "Hello_World", "Hello World",
				"a b_c=d+e)f(g!h≈i√jklmnopqrstuvwxyz1234567890"));
		for (String w : words) {
			t1.insert(w);
		}
		HashSet<String> wordSet = t1.getPrefix("");
		assertEquals(words, wordSet);
		wordSet = t1.getPrefix("hello");
		assertEquals(new HashSet<>(Arrays.asList("Hello", "Hello_World", "Hello World")),
				wordSet);
	}
	
	@Test
	public void testRemove() {
		Trie t1 = new Trie();
		HashSet<String> words = new HashSet<>(Arrays.asList(
				"Hello", "World", "Karel", "12345", "Karel1", "Hello_World", "Hello World",
				"a b_c=d+e)f(g!h≈i√jklmnopqrstuvwxyz1234567890"));
		for (String w : words) {
			t1.insert(w);
		}
		HashSet<String> wordSet = t1.getPrefix("");
		assertEquals(words, wordSet);
		t1.remove("a b_c=d+e)f(g!h≈i√jklmnopqrstuvwxyz1234567890");
		t1.remove("Hello");
		wordSet = t1.getPrefix("");
		assertEquals(new HashSet<>(Arrays.asList(
				"World", "Karel", "12345", "Karel1", "Hello_World", "Hello World")), 
				wordSet);
		
	}
}
