import org.apache.commons.io.FileUtils;

import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseHelper {
    final private static String testFolderPath = "/home/nic/original-projects/ml3/aclImdb/train/";

    private void listAllFiles(String classification) {
        File folder = new File(testFolderPath + classification);
        File[] listOfFiles = folder.listFiles();
        System.out.println(listOfFiles.length);

        for (int i = 0; i < listOfFiles.length; i++) {
            processFile(listOfFiles[i], classification);
        }
    }

    private void processFile(File file, String classification) {
        try {
            statement = connection.createStatement();
            String str = FileUtils.readFileToString(file);
            String processedString = str.replaceAll("[^A-Za-z0-9']", " ");
            String[] words = processedString.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                String lowerCaseWord = words[i].toLowerCase();
                String sql = "insert into WordsTable values ('" + lowerCaseWord + "', '" + classification + "')";
                statement.executeUpdate(sql);
//                System.out.println(words[i]);
            }
            statement.close();
        } catch (IOException e) {
            System.out.println("File not exists");
        } catch (SQLException se) {
            System.out.println("SQL problem!" + se.getMessage());
        }
    }


    Connection connection = null;
    Statement statement = null;

    DatabaseHelper() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::memory:");
            String sql;
            statement = connection.createStatement();
            sql = "create table WordsTable (word text, classification text)";
            statement.executeUpdate(sql);
            statement.close();
//            connection.close();
        } catch (Exception e) {
            System.out.println("Ohdear" + e.getClass().getName() + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.listAllFiles("neg/");
        System.out.println("Good");
    }
}