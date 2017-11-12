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

//Parses the input data labeledBow.feat to build a HashMap of the words, with key as the id of the word as according to
//imdb.vocab and the value being the number of occurences of the word.

public class FileParse {
//    Define file paths for the input data

    final private String trainingPath = getClass().getResource("labeledBowTrain.feat").getPath();
    final private String imdbVocab = getClass().getResource("imdb.vocab").getPath();
    final private String stopWordsPath = getClass().getResource("stop_words.txt").getPath();

    public int posWords = 0;
    public int negWords = 0;
    public int totalWords = 0;

    public double priorPosProb;
    public double priorNegProb;

    public double priorPosLog;
    public double priorNegLog;

    private int posFileCount = 0;
    private int negFileCount = 0;

//    Inner class used for storing how many times a word occurs in a positve and negative context. This is used as the
//    value in the member globalMap hashmap.

    class PosNegPair {
        int posOccurences;
        int negOccurences;
        PosNegPair() {
            posOccurences = 0;
            negOccurences = 0;
        }
    }

//    Used for storing the number of occurences of each word and the rating for a given file.

    ArrayList<FileRating> fileRatingArrayList = new ArrayList<FileRating>();
    HashMap<Integer, PosNegPair> globalMap;

//    We parse the file in the constructor of the FileParse class. This populates the HashMap

    FileParse() {
        globalMap = new HashMap<Integer, PosNegPair>();

        try {
            FileInputStream fileInputStream = new FileInputStream(trainingPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split("\\s+");
                  int movieRating = Integer.parseInt(words[0]);

                if (movieRating >=7) {
                    posWords += words.length -1;
                    posFileCount++;
                } else if (movieRating <= 4) {
                    negWords += words.length -1;
                    negFileCount++;
                }

                for (int i = 1; i < words.length; i++) {
                    String[] keyValue = words[i].split(":");

                    int key = Integer.parseInt(keyValue[0]);
                    int value = Integer.parseInt(keyValue[1]);

                    if (!globalMap.containsKey(key)) {
                        PosNegPair pair = new PosNegPair();
                        globalMap.put(key, pair);
                    }

                    PosNegPair occPair = globalMap.get(key);

                    if (movieRating >= 7) {
                        occPair.posOccurences += value;
                    } else if (movieRating <= 4) {
                        occPair.negOccurences += value;
                    }

                    globalMap.put(key, occPair);
                }
            }
            totalWords = posWords + negWords;

            priorPosProb = (double) posWords / (totalWords);
            priorNegProb = (double) negWords / (totalWords);

//            We compute log as this is needed for getting the probability of positive or negative occurences.

            priorPosLog = Math.log(priorPosProb);
            priorNegLog = Math.log(priorNegProb);
        } catch (Exception e) {
            System.out.println("Exception in: " + e.getMessage());
        }
    }

//    Remove the stop words (commonly used words) from the NBC by deleting the key from the HashMap

    public void removeStopWords() {
        try {
            BufferedReader stopWordReader = new BufferedReader(new InputStreamReader(new FileInputStream(stopWordsPath)));
            BufferedReader vocabReader = new BufferedReader(new InputStreamReader(new FileInputStream(imdbVocab)));
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
}
