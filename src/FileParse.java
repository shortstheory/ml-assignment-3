/**
 * Created by nic on 7/11/17.
 */
import org.apache.commons.io.FileUtils;
import javax.xml.transform.Result;
import java.io.*;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class FileParse {
    final private static String testFolderPath = "/home/nic/original-projects/ml3/aclImdb/";
    final private static String imdbLabel = "train/labeledBow.feat";
    final private static String imdbVocab = "imdb.vocab";

    class FileRating {
        HashMap<Integer, Integer> wordMap;
        int movieRating;
        FileRating(){
            wordMap = new HashMap<Integer, Integer>();
        }
    }

    ArrayList<FileRating> fileRatingArrayList = new ArrayList<FileRating>();

    FileParse() {
        HashMap<Integer, Integer> globalMap = new HashMap<Integer, Integer>();
        String vocabPath = testFolderPath + imdbVocab;
        String labelPath = testFolderPath + imdbLabel;
        try {
            FileInputStream fileInputStream = new FileInputStream(labelPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split("\\s+");
                FileRating fileRating = new FileRating();
                fileRating.movieRating = Integer.parseInt(words[0]);
                for (int i = 1; i < words.length; i++) {
                    String[] keyValue = words[i].split(":");
                    globalMap.put(Integer.parseInt(keyValue[0]), Integer.parseInt(keyValue[1]));
                    fileRating.wordMap.put(Integer.parseInt(keyValue[0]), Integer.parseInt(keyValue[1]));
                }
                fileRatingArrayList.add(fileRating);
            }
        } catch (Exception e) {
            System.out.println("Exception in: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        FileParse fileParse = new FileParse();
        System.out.println(fileParse.fileRatingArrayList.size());
    }
}
