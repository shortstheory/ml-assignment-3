import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nic on 7/11/17.
 */

public class NaiveBayesClassifier {
    final private static String testFolderPath = "/home/nic/original-projects/ml3/aclImdb/";
    final private static String testLabel = "test/labeledBow.feat";
    final private static String imdbVocab = "imdb.vocab";
    ArrayList<FileRating> fileRatingArrayList;


    float positive = 0;
    float negative = 0;
    float true_positive = 0;
    float true_negative = 0;
    float false_positive = 0;
    float false_negative = 0;

    private FileParse fileParse;
    NaiveBayesClassifier() {
        fileParse = new FileParse();
        String testPath = testFolderPath + testLabel;

        try {
            FileInputStream fileInputStream = new FileInputStream(testPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            fileRatingArrayList = new ArrayList<FileRating>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split("\\s+");
                FileRating fileRating = new FileRating();
                fileRating.movieRating = Integer.parseInt(words[0]);
                for (int i = 1; i < words.length; i++) {
                    String[] keyValue = words[i].split(":");

                    int key = Integer.parseInt(keyValue[0]); //wordID
                    int value = Integer.parseInt(keyValue[1]); //value
                    fileRating.wordMap.put(key, value);
                }
                fileRatingArrayList.add(fileRating);
            }
        } catch (Exception e) {
            System.out.println("Exception in: " + e.getMessage());
        }
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Integer val = (Integer) pair.getValue();
            System.out.println(pair.getKey() + " = " + val);
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public String classifyInstance(int i) { //gets file i
        float posProbability = 0;
        float negProbability = 0;
        int scoredRating = fileRatingArrayList.get(i).movieRating;

        boolean actuallyGoodFilm;

        if (scoredRating >= 7) {
            actuallyGoodFilm = true;
        } else {
            actuallyGoodFilm = false;
        }

        HashMap<Integer, Integer> map = fileRatingArrayList.get(i).wordMap; //first arg is the index of the word, second is # of times it occurs
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) { //iterate through all the words in the wordmap
            HashMap.Entry<Integer, Integer> pair = (HashMap.Entry<Integer, Integer>) it.next();

            Integer key = (Integer) pair.getKey();
            Integer val = (Integer) pair.getValue(); //number of occ of word in files
            //search for key in the global map
            FileParse.PosNegPair posNegPair = fileParse.globalMap.get(key);

            int posOccurences = posNegPair.posOccurences;
            int negOccurences = posNegPair.negOccurences;

            posProbability +=  Math.log((double) (posOccurences + 1) / (fileParse.posWords + fileParse.totalWords));
            negProbability += Math.log((double) (negOccurences + 1) / (fileParse.negWords + fileParse.totalWords));
            it.remove();
        }
        posProbability += fileParse.priorPosLog;
        negProbability += fileParse.priorNegLog;
        boolean goodFilm;
        if (posProbability > negProbability) {
            goodFilm = true;
        } else {
            goodFilm = false;
        }

        if (actuallyGoodFilm && goodFilm) {
            positive++;
            true_positive++;
        } else if (actuallyGoodFilm && !goodFilm) {
            positive++;
            false_negative++;
        } else if (!actuallyGoodFilm && !goodFilm) {
            negative++;
            true_negative++;
        } else if (!actuallyGoodFilm && goodFilm) {
            negative++;
            false_positive++;
        }

        return null;
    }

    public void printResults() {
        float accuracy = (true_positive+true_negative) / (true_positive + false_negative + false_positive + true_negative);
        float precision = true_positive / (true_positive + false_positive);
        float recall = true_positive / positive;
        float f1_score = 2 * (precision*recall) / (precision+recall);

        System.out.println(accuracy);
        System.out.println(precision);
        System.out.println(recall);
        System.out.println(f1_score);
    }

    public static void main(String[] args) {
        NaiveBayesClassifier naiveBayesClassifier = new NaiveBayesClassifier();

        for (int i = 0; i < naiveBayesClassifier.fileRatingArrayList.size(); i++) {
            naiveBayesClassifier.classifyInstance(i);
        }
        naiveBayesClassifier.printResults();


    }
}
