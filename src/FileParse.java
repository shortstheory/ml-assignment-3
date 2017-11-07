/**
 * Created by nic on 7/11/17.
 */
import org.apache.commons.io.FileUtils;
import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class FileParse {
    final private static String testFolderPath = "/home/nic/original-projects/ml3/aclImdb/";
    final private static String labelPath = "train/labeledBow.feat";
    final private static String imdbVocab = "imdb.vocab";

    class FileRating {
        HashMap<Integer, Integer> wordMap;
        int movieRating;
        FileRating(){}
    }

    FileParse() {
        HashMap<Integer, Integer> globalMap = new HashMap<Integer, Integer>();
        
    }
    public static void main(String[] args) {
        FileParse fileParse = new FileParse();
    }
}
