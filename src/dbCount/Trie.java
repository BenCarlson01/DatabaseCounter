package dbCount;

import java.util.TreeSet;

public class Trie {
	
	private Node root;
	
	private class Node {
		private Node[] children;
		// 0-25 : a-z, 26-35 : 0-9, 36: any other character
		private TreeSet<String> names;
		
		private Node(String name) {
			children = new Node[37];
			names = new TreeSet<>();
			names.add(name);
		}
		
		private Node() {
			children = new Node[37];
			names = new TreeSet<>();
		}
	}
	
	public Trie() {
		root = new Node();
	}
	
	private int charToInt(char c) {
		if (Character.isDigit(c)) {
			return c - 47;
		}
		c = Character.toLowerCase(c);
		int num = c - 86;
		if (num < 11 || num > 36) {
			return 0;
		}
		return num;
	}
	
	public void insert(String word) {
		Node cur = root;
		int i = 0;
		for (; i < word.length(); i++) {
			char c = word.charAt(i);
			int in = charToInt(c);
			if (in == -1) {
				
			}
			if (cur.children[in] != null) {
				cur = cur.children[in];
			} else {
				cur.children[in] = new Node();
				cur = cur.children[in];
			}
		}
		cur.names.add(word);
	}
	
	public boolean contains(String word) {
		Node cur = root;
		int i = 0;
		for (; i < word.length(); i++) {
			char c = word.charAt(i);
			int in = charToInt(c);
			if (cur.children[in] == null) {
				return false;
			}
			cur = cur.children[in];
		}
		return cur.names.contains(word);
	}
	
	public TreeSet<String> getPrefix(String prefix) {
		Node cur = root;
		int i = 0;
		for (; i < prefix.length(); i++) {
			char c = prefix.charAt(i);
			int in = charToInt(c);
			if (cur.children[in] == null) {
				return new TreeSet<>();
			}
			cur = cur.children[in];
		}
		return getPrefixHelper(cur);
	}
	
	private TreeSet<String> getPrefixHelper(Node cur) {
		TreeSet<String> ret = new TreeSet<>();
		if (cur == null) {
			return ret;
		}
		ret.addAll(cur.names);
		for (Node n : cur.children) {
			ret.addAll(getPrefixHelper(n));
		}
		return ret;
	}
	
	public void remove(String word) {
		Node cur = root;
		int i = 0;
		for (; i < word.length(); i++) {
			char c = word.charAt(i);
			int in = charToInt(c);
			if (cur.children[in] == null) {
				return;
			}
			cur = cur.children[in];
		}
		if (cur.names.contains(word)) {
			cur.names.remove(word);
		}
	}
	
	public void print() {
		printHelper(root);
	}
	
	private void printHelper(Node cur) {
		if (cur == null) {
			return;
		}
		for (Node n : cur.children) {
			printHelper(n);
		}
		if (!cur.names.isEmpty()) {
			for (String name : cur.names) {
				System.out.println(name);
			}
		}
	}
	
/*
	public Trie() {
		top = new Node('m');
	}
	
	public void insert(String word) {
		insertHelper(top, word, word);
	}
	
	private void insertHelper(Node n, String s, String word) {
		char c = s.charAt(0);
		if (s.length() == 1) {
			if (n.letter == c) {
				n.name = word;
			} else if (n.letter > c) {
				if (n.left == null) {
					n.left = new Node(c, word);
					n.left.name = word;
				} else {
					insertHelper(n.left, s, word);
				}
			} else {
				if (n.right == null) {
					n.right = new Node(c, word);
				} else {
					insertHelper(n.right, s, word);
				}
			}
		} else if (n.letter == c) {
			if (n.center == null) {
				n.center = new Node(s.charAt(1));
				insertHelper(n.center, s.substring(1), word);
			} else {
				insertHelper(n.center, s.substring(1), word);
			}
		} else if (n.letter > c) {
			if (n.left == null) {
				n.left = new Node(c);
				insertHelper(n.left, s, word);
			} else {
				insertHelper(n.left, s, word);
			}
		} else {
			if (n.right == null) {
				n.right = new Node(c);
				insertHelper(n.right, s, word);
			} else {
				insertHelper(n.right, s, word);
			}
		}
	}
	
	public boolean contains(String s) {
		return containsHelper(top, s, s);
	}
	
	private boolean containsHelper(Node n, String s, String word) {
		char c = s.charAt(0);
		if (s.length() == 1) {
			if (n == null) {
				return false;
			} else if (n.letter == c) {
				return n.name.equals(word);
			} else if (n.letter > c) {
				return containsHelper(n.left, s, word);
			} else {
				return containsHelper(n.right, s, word);
			}
		} else {
			if (n == null) {
				return false;
			} else if (n.letter == c) {
				return containsHelper(n.center, s.substring(1), word);
			} else if (n.letter > c) {
				return containsHelper(n.left, s, word);
			} else {
				return containsHelper(n.right, s, word);
			}
		}
	}
	
	public TreeSet<String> getPrefix(String s) {
		if (s.length() < 1) {
			return getAllWords(top);
		}
		if (s.length() == 1 && s.charAt(0) == 'm') {
			return getAllWords(top.center);
		}
		return getPrefixHelper(top, s);
	}
	
	private TreeSet<String> getPrefixHelper(Node n, String s) {
		if (n == null) {
			return new TreeSet<>();
		}
		char c = s.charAt(0);
		if (s.length() == 1) {
			if (n.letter == c) {
				return getAllWords(n);
			}
			if (n.letter > c) {
				return getPrefixHelper(n.left, s);
			}
			return getPrefixHelper(n.right, s);
		}
		if (n.letter == c) {
			return getPrefixHelper(n.center, s.substring(1));
		}
		if (n.letter > c) {
			return getPrefixHelper(n.left, s);
		}
		return getPrefixHelper(n.right, s);
	}
	
	private TreeSet<String> getAllWords(Node n) {
		if (n == null) {
			return new TreeSet<>();
		}
		TreeSet<String> words = new TreeSet<>();
		if (n.name != null) {
			words.add(n.name);
		}
		words.addAll(getAllWords(n.left));
		words.addAll(getAllWords(n.center));
		words.addAll(getAllWords(n.right));
		return words;
	}
	
	public void remove(String s) {
		removeHelper(top, s);
	}
	
	private void removeHelper(Node cur, String s) {
		
	}
	
	public void printTrie() {
		printTrieHelper(top, 1);
	}
	
	private void printTrieHelper(Node cur, int spaces) {
		if (cur == null) {
			return;
		}
		System.out.print(cur.letter + ": ");
		if (cur.left != null) {
			System.out.print(cur.left.letter);
		} else {
			System.out.print("XX");
		}
		System.out.print(", ");
		if (cur.center != null) {
			System.out.print(cur.center.letter);
		} else {
			System.out.print("XX");
		}
		System.out.print(", ");
		if (cur.right != null) {
			System.out.print(cur.right.letter);
		} else {
			System.out.print("XX");
		}
		if (cur.name != null) {
			System.out.print(", " + cur.name);
		}
		
		System.out.println();
		
		if (cur.left != null) {
			System.out.print(genSpaces(spaces) + cur.letter + "-l: ");
			printTrieHelper(cur.left, spaces + 1);
		}
		if (cur.center != null) {
			System.out.print(genSpaces(spaces) + cur.letter + "-c: ");
			printTrieHelper(cur.center, spaces + 1);
		}
		if (cur.right != null) {
			System.out.print(genSpaces(spaces) + cur.letter + "-r: ");
			printTrieHelper(cur.right, spaces + 1);
		}
	}
	
	private String genSpaces(int spaces) {
		String ans = "";
		for (int i = 0; i < spaces; i++) {
			ans += " ";
		}
		return ans;
	}
*/
}
