import java.util.*;
/* *
 * This is a class that models a LevenshteinFinder
 * keeping the start and end words, and finding the
 * distance and path between the two words.
 *
 * @author Thomas H. Le
 * @version 10-20-2022
*/
public class LevenshteinFinder {
   private String start, end;
   private int distance = -1;
   private List<String> path;
   private TreeMap<String, Set<String>> neighbors;
   
   /**
   * This is a constructor that creates a
   * a Levenshtein Finder with a given start
   * and end word and a dictionary.
   *
   * @param start The given start word
   * @param end the given end word.
   * @param A set of words in a dictionary.
   * @exceptions throw IllegalArgumentException if start and end length aren't equal.
   */
   public LevenshteinFinder(String start, String end, Set<String> words) {
      this.start = start;
      this.end = end;
      if (this.start.length() != this.end.length()) { //Checks if start and end words are same length
         throw new IllegalArgumentException();
      }
      neighbors = new TreeMap<String, Set<String>>();
      List<String> equalLength = new ArrayList<String>();
      for (String item : words) { //Add every word of same length as start to list
         if (item.length() == start.length()) {
            equalLength.add(item);
         }
      }
      for(String x : equalLength) { //put every word in and an empty set into a map
         Set<String> neighborSet = new TreeSet<String>();
         neighbors.put(x, neighborSet);
      }
      for (String word : neighbors.keySet()) { //add neighboring word into sets for each word in list.
         Set<String> add = neighbors.get(word);
         for (String neighborWord : neighbors.keySet()) {
            if (!word.equals(neighborWord)) {
               int differ = differentLetters(word, neighborWord);
               if (differ == 1) {
                  add.add(neighborWord);
               }
            }
         }
         neighbors.put(word, add);
      }
      distance = findDistance(start, end);
      findPath(start, end);
   }
   
   /**
    * This is the getDistance method that returns
    * the distance between the start and end word.
    *
    * @return distance between start and end word.
    */
   public int getDistance() {
      
      return distance;
   }
   
   /**
    * This is the getPath method that returns
    * the string representation of the path between
    * the start and end word.
    *
    * @return The path between the start and end word.
    */
   public String getPath() {
      if (distance == -1) {
         return "There is no path";
      }
      String paths = "";
      for (String i : path) {
         if (i.equals(start)) {
            paths = paths + i;
         } else {
            paths = paths + "-->" + i;
         }
      }
      return paths;
   }

   private int findDistance(String A, String B) {
      Set<String> set1 = new TreeSet<String>();
      Set<String> set2 = new TreeSet<String>();
      set2.add(A);
      int counter = 0;
      while (!set2.contains(B) && set1.size() != set2.size()) {
         set1.addAll(set2);
         set2.clear();
         for(String word1 : set1) {
           set2.add(word1);
           set2.addAll(neighbors.get(word1));
         }
         counter += 1;
      }
      if (set2.contains(B)) {
         return counter;
      } else {
         return -1;
      }
   }
   
   private void findPath(String A, String B) {
      path = new ArrayList<String>();
      String word = A;
      if (distance == -1) {
         return;
      } else {
         path.add(word);
         for (int i = distance - 1; i >= 1; i--) {
            Set<String> x = neighbors.get(word);
            int add = 0;
            for (String j : x) {
               int z = findDistance(j, B);
               if (z == i && add == 0) {
                  path.add(j);
                  add = add + 1;
                  word = j;
               }
            }
         }
         path.add(B);
      }
   }
   
   private int differentLetters(String A, String B) {
      int differ = 0;
      for (int i = 0; i < A.length(); i++) {
         if (A.charAt(i) != B.charAt(i)) {
            differ += 1;
         }
      }
      return differ;
   }
}