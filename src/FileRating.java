import java.util.HashMap;

// Helper class used for storing a HashMap of each word and the number of time it occurs in a file. The rating of the movie
//is also stored here.

public class FileRating {
    HashMap<Integer, Integer> wordMap;
    int movieRating;
    FileRating(){
        wordMap = new HashMap<Integer, Integer>();
    }
}
