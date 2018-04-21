package dbCount;

import java.util.HashSet;
import java.util.TreeSet;

public class MyTrie {
	
	private Node top;
	
	private class Node {
		private Node left;
		private Node center;
		private Node right;
		private char letter;
		private String name;
		
		private Node(char let, String name) {
			left = null;
			center = null;
			right = null;
			letter = let;
			this.name = name;
		}
		
		private Node(char let) {
			left = null;
			center = null;
			right = null;
			letter = let;
			this.name = null;
		}
	}
	
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
}
