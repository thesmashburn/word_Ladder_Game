import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Doublets.java
 * Provides an implementation of the WordLadderGame interface. The lexicon
 * is stored as a TreeSet of Strings.
 *
 * @author Your Name (you@auburn.edu)
 * @author Dean Hendrix (dh@auburn.edu)
 * @version 2016-04-07
 */
public class Doublets implements WordLadderGame {

    ////////////////////////////////////////////
    // DON'T CHANGE THE FOLLOWING TWO FIELDS. //
    ////////////////////////////////////////////

    // A word ladder with no words. Used as the return value for the ladder methods
    // below when no ladder exists.
   List<String> EMPTY_LADDER = new ArrayList<>();

    // The word list used to validate words.
    // Must be instantiated and populated in the constructor.
   TreeSet<String> lexicon;


    /**
     * Instantiates a new instance of Doublets with the lexicon populated with
     * the strings in the provided InputStream. The InputStream can be formatted
     * in different ways as long as the first string on each line is a word to be
     * stored in the lexicon.
     */
   public Doublets(InputStream in) {
      try {
         lexicon = new TreeSet<String>();
         Scanner s =
                new Scanner(new BufferedReader(new InputStreamReader(in)));
         while (s.hasNext()) {
            String str = s.next();
            lexicon.add(str.toLowerCase());
                ////////////////////////////////////////////////
                // Add code here to store str in the lexicon. //
                ////////////////////////////////////////////////
            s.nextLine();
         }
         in.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }

    ///////////////////////////////////////////////////////////////////////////////
    // Fill in implementations of all the WordLadderGame interface methods here. //
    ///////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Returns the Hamming distance between two strings, str1 and str2. The
     * Hamming distance between two strings of equal length is defined as the
     * number of positions at which the corresponding symbols are different. The
     * Hamming distance is undefined if the strings have different length, and
     * this method returns -1 in that case. See the following link for
     * reference: https://en.wikipedia.org/wiki/Hamming_distance
     *
     * @param  str1 the first string
     * @param  str2 the second string
     * @return      the Hamming distance between str1 and str2 if they are the
     *                  same length, -1 otherwise
     */
   public int getHammingDistance(String str1, String str2) {
   
      if (str1.length() != str2.length()) {
         return -1; 
      }
    
      int hammingDistance = 0;
      for (int i = 0; i < str1.length(); i++) {
         if (str1.charAt(i) != str2.charAt(i)) {
            hammingDistance++;
         }
      }
    
      return hammingDistance;
   }
    
    
     /**
     * Returns all the words that have a Hamming distance of one relative to the
     * given word.
     *
     * @param  word the given word
     * @return      the neighbors of the given word
     */
   public List<String> getNeighbors(String word) {
      int length = word.length();
      String base = word.toLowerCase();
      List<String> neighbors = new ArrayList<>();
      
    
      for (int i = 0; i < length; i++) {
         char first = 'a';
         StringBuilder check = new StringBuilder(base);
         for (int j = 0; j < 26; j++) {
            check.setCharAt(i,first);
            if (this.isWord(check.toString()) && !check.toString().equals(base)) {
               neighbors.add(check.toString());
            }
            first++;
         }
      }
    
      return neighbors;
   }
    
       
    
   


    /**
     * Returns the total number of words in the current lexicon.
     *
     * @return number of words in the lexicon
     */
   public int getWordCount() {
      return lexicon.size();
   }


    /**
     * Checks to see if the given string is a word.
     *
     * @param  str the string to check
     * @return     true if str is a word, false otherwise
     */
   public boolean isWord(String str) {
      if (lexicon.contains(str.toLowerCase())) {
         return true;
      }
      else {
         return false;
      }
   }


    /**
     * Checks to see if the given sequence of strings is a valid word ladder.
     *
     * @param  sequence the given sequence of strings
     * @return          true if the given sequence is a valid word ladder,
     *                       false otherwise
     */
   public boolean isWordLadder(List<String> sequence){
      if (sequence.isEmpty()) {
         return false;
      }
      int size = sequence.get(0).length();
      for (String word : sequence) {
         if (!this.isWord(word)) {
            return false;
         }
         else if (word.length() != size) {
            return false;
         }
      }
      for (int i = 1; i < sequence.size(); i++) {
         if (this.getHammingDistance(sequence.get(i), sequence.get(i-1)) != 1) {
            return false;
         }
      }
    
      return true;
   }
   
    /**
    * Returns a word ladder from start to end. If multiple word ladders exist,
    * no guarantee is made regarding which one is returned. If no word ladder exists,
    * this method returns an empty list.
    *
    * Depth-first search with backtracking must be used in all implementing classes.
    *
    * @param  start  the starting word
    * @param  end    the ending word
    * @return        a word ladder from start to end
    */
   public List<String> getLadder(String start, String end){
      ArrayList<String> result = new ArrayList<>();
      result.add(start.toLowerCase());
   
      if (start.toLowerCase().equals(end.toLowerCase())) {
         return result;
      }
      
      Deque<String> stack = new ArrayDeque<>();
      TreeSet<String> visited = new TreeSet<>();
      stack.addLast(start.toLowerCase());
      visited.add(start.toLowerCase());
      while (!stack.isEmpty()) {
         String current = stack.peekLast();
         String neighbor = null;
         ArrayList<String> neighbors = (ArrayList<String>) this.getNeighbors(current);
         for (String p : neighbors) {
            if (!visited.contains(p)) {
               neighbor = p;
               break;
            }
         }
            
      
       
       // generate neighbors for the top current word
       // remove all words from neighbors such that triedWords.contains(word)
         if (neighbor != null) {
            stack.addLast(neighbor);
            visited.add(neighbor);
            result.add(neighbor);
            if (neighbor.equals(end.toLowerCase())) {
               return result;
            }
         
         }
         else { // backtrack
            stack.removeLast();
            result.remove(result.size() - 1);
         }
      
      }
      return result;
   }


   /**
    * Returns a minimum-length word ladder from start to end. If multiple
    * minimum-length word ladders exist, no guarantee is made regarding which
    * one is returned. If no word ladder exists, this method returns an empty
    * list.
    *
    * Breadth-first search must be used in all implementing classes.
    *
    * @param  start  the starting word
    * @param  end    the ending word
    * @return        a minimum length word ladder from start to end
    */
   public List<String> getMinLadder(String start, String end){
      ArrayList<String> result = new ArrayList<>();
      ArrayList<String> empty = new ArrayList<>();
      
      if (start.toLowerCase().equals(end.toLowerCase())) {
         result.add(start.toLowerCase());
         return result;
      }
      TreeSet<String> visited = new TreeSet<>();
      Deque<Node> queue = new ArrayDeque<>();
      visited.add(start.toLowerCase());
      result.add(start.toLowerCase());
      
      queue.addLast(new Node(start.toLowerCase(), null));
      while (!queue.isEmpty()) {
         Node n = queue.removeFirst();
         String value = n.value;
         for (String neighbor : this.getNeighbors(value)) {
            if (!visited.contains(neighbor)) {
               visited.add(neighbor);
               Node thing = new Node(neighbor, n);
               queue.addLast(thing);
               if (neighbor.equals(end.toLowerCase())) {
                 ArrayList<String> temp = new ArrayList<>();
                  while (thing.predecessor != null) {
                     temp.add(thing.value);
                     thing = thing.predecessor;
                  }
                  for (int i = temp.size(); i > 0; i--) {
                  result.add(temp.get(i-1));
                  }
                  return result;
               }
            
               
            }
         }
      }
   
      return empty;
   
   }
      
      
   private class Node {
      String value;
      Node predecessor;
   
      public Node(String v, Node pred) {
         value = v;
         predecessor = pred;
      }
   }

    
    
}
