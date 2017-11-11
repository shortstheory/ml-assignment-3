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
    final private static String stopWordsPath = "stop_words.txt";

    public int posWords = 0;
    public int negWords = 0;
    public int totalWords = 0;

    public double priorPosProb;
    public double priorNegProb;

    public double priorPosLog;
    public double priorNegLog;

    private int posFileCount = 0;
    private int negFileCount = 0;

    class PosNegPair {
        int posOccurences;
        int negOccurences;
        PosNegPair() {
            posOccurences = 0;
            negOccurences = 0;
        }
    }

    ArrayList<FileRating> fileRatingArrayList = new ArrayList<FileRating>();
    HashMap<Integer, PosNegPair> globalMap;
    FileParse() {
        globalMap = new HashMap<Integer, PosNegPair>();
        String labelPath = testFolderPath + imdbLabel;

        try {
            FileInputStream fileInputStream = new FileInputStream(labelPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split("\\s+");
                FileRating fileRating = new FileRating();
                fileRating.movieRating = Integer.parseInt(words[0]);

                if (fileRating.movieRating >=7) {
                    posWords += words.length -1;
                    posFileCount++;
                } else if (fileRating.movieRating <= 4) {
                    negWords += words.length -1;
                    negFileCount++;
                }

                for (int i = 1; i < words.length; i++) {
                    String[] keyValue = words[i].split(":");

                    int key = Integer.parseInt(keyValue[0]);
                    int value = Integer.parseInt(keyValue[1]);

                    fileRating.wordMap.put(key, value);

                    if (!globalMap.containsKey(key)) {
                        PosNegPair pair = new PosNegPair();
                        globalMap.put(key, pair);
                    }

                    PosNegPair occPair = globalMap.get(key);

                    if (fileRating.movieRating >= 7) {
                        occPair.posOccurences += value;
                    } else if (fileRating.movieRating <= 4) {
                        occPair.negOccurences += value;
                    }

                    globalMap.put(key, occPair);
                }
                fileRatingArrayList.add(fileRating);
            }
            totalWords = posWords + negWords;

            priorPosProb = (double) posWords / (totalWords);
            priorNegProb = (double) negWords / (totalWords);

            priorPosLog = Math.log(priorPosProb);
            priorNegLog = Math.log(priorNegProb);
        } catch (Exception e) {
            System.out.println("Exception in: " + e.getMessage());
        }
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            PosNegPair p = (PosNegPair) pair.getValue();
            System.out.println(pair.getKey() + " = " + "Pos: " + p.posOccurences + "Neg: " + p.negOccurences);
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void removeStopWords() {
        try {
            BufferedReader stopWordReader = new BufferedReader(new InputStreamReader(new FileInputStream(testFolderPath + stopWordsPath)));
            BufferedReader vocabReader = new BufferedReader(new InputStreamReader(new FileInputStream(testFolderPath + imdbVocab)));
            ArrayList<String> stopWordList = new ArrayList<String>();
            ArrayList<String> vocabList = new ArrayList<String>();
            ArrayList<Integer> deletedIndices = new ArrayList<Integer>();
            String line;

            while ((line = stopWordReader.readLine()) != null) {
                stopWordList.add(line);
            }
            while ((line = vocabReader.readLine()) != null) {
                vocabList.add(line);
            }

            for (int i = 0; i < stopWordList.size(); i++) {
                int index = vocabList.indexOf(stopWordList.get(i));
                if (index != -1) {
                    deletedIndices.add(index);
                }
            }

            for (int i = 0; i < deletedIndices.size(); i++) {
                globalMap.remove(i);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        FileParse fileParse = new FileParse();
        fileParse.removeStopWords();
//        System.out.println(fileParse.fileRatingArrayList.size());
//        printMap(fileParse.globalMap);
    }
}
