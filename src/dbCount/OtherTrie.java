package dbCount;

public class OtherTrie {
	/*
	 * Python program for delete operation in a Trie:
	 * Obtained from https://www.geeksforgeeks.org/trie-delete/
	 * Code contributed by Atul Kumar (www.facebook.com/atul.kumar.007)
	 * 
	 * Reconstructed in Java by myself
	 */
	 
	private class TrieNode {
	    /*
	     * Trie node class
	     */
		private TrieNode[] children;
		private int value;
		
	    private TrieNode() {
	        children = new TrieNode[26];
	        // non zero if leaf
	        value = 0;
	    }
	 
	    private boolean leafNode() {
	    	/*
	         * Check if node is leaf node or not
	         */
	        return value != 0;
	    }
	 
	    private boolean isItFreeNode() {
	    	/*
	         * If node have no children then it is free
	         * If node have children return False else True
	         */
	        for (TrieNode c : children) {
	            if (c != null) {
	            	return false;
	            }
	        }
	        return true;
	    }
	    
	    private TrieNode[] getChildren() {
	    	return children;
	    }
	    
	    private int getValue() {
	    	return value;
	    }
	    
	    private void setValue(int value) {
	    	this.value = value;
	    }
	}
	private TrieNode root;
	private int count;
	
    public OtherTrie() {
        root = getNode();
        // keep count on number of keys inserted in trie
        count = 0;
    }
 
    private int index(char c) {
    	/* 
    	 * private helper function
         * Converts key current character into index
         * use only 'a' through 'z' and lower case
         */
        return c - 97;
    }
 
    public TrieNode getNode() {
        /*
         * Returns new trie node (initialized to NULLs)
         */
        return new TrieNode();
    }
 
    public void insert(String key) {
        /*
         * If not present, inserts key into trie
         * If the key is prefix of trie node, mark it as leaf (non zero)
         */
        int length = key.length();
        TrieNode pCrawl = root;
        count += 1;
        char[] cKey = new char[length];
        key.getChars(0, length, cKey, 0);
 
        for (char c : cKey) {
            int in = index(c);
 
            if (pCrawl.getChildren()[in] != null) {
                // skip current node
                pCrawl = pCrawl.children[in];
            }
            else {
                // add new node
                pCrawl.children[in] = getNode();
                pCrawl = pCrawl.children[in];
            }
        }
 
        // mark last node as leaf (non zero)
        pCrawl.setValue(count);
    }
 
    public boolean search(String key) {
        /*
         * Search key in the trie
         * Returns true if key presents in trie, else false
         */
        int length = key.length();
        TrieNode pCrawl = root;
        char[] cKey = new char[length];
        key.getChars(0, length, cKey, 0);
        
        for (char c : cKey) {
            int in = index(c);
            if (pCrawl.getChildren()[in] == null) {
                return false;
            }
            pCrawl = pCrawl.getChildren()[in];
        }
 
        return pCrawl != null && pCrawl.getValue() != 0;
    }
 
 
    private boolean deleteHelper(TrieNode pNode, String key, int level, int length) {
        /*
         * Helper function for deleting key from trie
         */
        if (pNode != null) {
            // Base case
            if (level == length) {
                if (pNode.getValue() != 0) {
                    // unmark leaf node
                    pNode.setValue(0);
                }
                // if empty, node to be deleted
                return pNode.isItFreeNode();
            }
            // recursive case
            else {
                int in = index(key.charAt(level));
                if (deleteHelper(pNode.getChildren()[in], key, level + 1, length)) {
                	// last node marked,delete it
                    pNode.getChildren()[in] = null;
 
                    // recursively climb up and delete eligible nodes
                    return (!pNode.leafNode() && pNode.isItFreeNode());
                }
            }
        }
        return false;
    }
 
    public void deleteKey(String key) {
        /*
         * Delete key from trie
         */
        int length = key.length();
        if (length > 0) {
            deleteHelper(root, key, 0, length); 
        }
    }
	 
	/*
	public void main(String[] args) {
	    keys = ["she","sells","sea","shore","the","by","sheer"]
	    trie = Trie()
	    for key in keys:
	        trie.insert(key)
	 
	    trie.deleteKey(keys[0])
	 
	    print("{} {}".format(keys[0],\
	        "Present in trie" if trie.search(keys[0]) \
	                        else "Not present in trie"))
	 
	    print("{} {}".format(keys[6],\
	        "Present in trie" if trie.search(keys[6]) \
	                        else "Not present in trie"))    
	}
	*/
}
