import javax.xml.transform.Result;
import java.sql.*;

public class DatabaseHelper {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

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