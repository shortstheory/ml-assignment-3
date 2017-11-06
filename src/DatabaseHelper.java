import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;

public class DatabaseHelper {
    final private static String testFolderPath = "/home/nic/original-projects/ml3/aclImdb/test/";

    private static void listAllFiles(String classification) {
        File folder = new File(testFolderPath + classification);
        File[] listOfFiles = folder.listFiles();
        System.out.println(listOfFiles.length);

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        listAllFiles("neg/");
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::memory:");
            String sql;
            statement = connection.createStatement();
            sql = "create table WordsTable (word text, classification text)";
            statement.executeUpdate(sql);

            sql = "insert into WordsTable values ('oh well', '+')";
            statement.executeUpdate(sql);
            sql = "insert into WordsTable values ('oh dear', '-')";
            statement.executeUpdate(sql);

            ResultSet rs = statement.executeQuery("select * from WordsTable");

            while (rs.next()) {
                String word = rs.getString("word");
                String classification = rs.getString("classification");

                System.out.println(word);
                System.out.println(classification);
            }

            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Ohdear" + e.getClass().getName() + e.getMessage());
        }
        System.out.println("Good");
    }
}