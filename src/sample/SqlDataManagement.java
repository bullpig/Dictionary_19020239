package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class SqlDataManagement {
    private Connection connection = null;
    private Statement statement = null;
    //word_text and word_def

    String fileName = "dictionary.db";
    String url = "jdbc:sqlite:" +
            System.getProperty("user.dir").replace("\\","/") +
            "/DATA/" + fileName;

    public void connect() {

        System.out.println(url);
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
            System.out.println("ConnectionSuccess");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try {
            Statement createStatements = connection.createStatement();
            String sqlCreate = "CREATE TABLE" +
                    " IF NOT EXISTS" +
                    " words " +
                    "(word_text TEXT," +
                    " word_def TEXT);";
            createStatements.executeUpdate(sqlCreate);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createTableHistory() {
        try {
            Statement createStatements = connection.createStatement();
            String sqlCreate = "CREATE TABLE" +
                    " IF NOT EXISTS" +
                    " history " +
                    "(word_text TEXT," +
                    " word_def TEXT);";
            createStatements.executeUpdate(sqlCreate);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertData(String word, String def, String pronunciation, String toTable) {

        word = word.replaceAll("'", "\\\''");
        pronunciation = pronunciation.replaceAll("'", "`");
        executeCommand( String.format("INSERT INTO %s VALUES('%s','%s','%s')",
                toTable, word, def, pronunciation));
    }


    public void executeCommand(String command) {
        try  (PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertFromDictionary(Dictionary dictionary) {
        for(Word word : dictionary.getWordArrayList()) {
            insertData(word.getWordTarget(), word.getWordExplain());
            System.out.println(word.getWordTarget() + " has been added to database");

        }
    }
    public void insertToDictionary(Dictionary dictionary) {
        try(Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT word_text, word_def from words") ){
            while(resultSet.next()) {
                dictionary.addWord(resultSet.getString("word_text"),
                        resultSet.getString("word_def"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertFromFileAdvanced(Dictionary d, String path)   {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));

            String bufferString;

            String wordText = "";
            String wordDef  = "";
            String wordPronunciation = "";

            while((bufferString = in.readLine()) != null) {

                if (!bufferString.isEmpty()) {

                    if (bufferString.charAt(0) == '@') {

                        if (wordDef != "") {
                            wordPronunciation += wordDef;
                            d.addWord(wordText, wordPronunciation);

                            wordDef = "";
                            wordPronunciation = "";
                        }

                        int endPoint = bufferString.indexOf("/");
                        if (endPoint == -1) {
                            endPoint = bufferString.length();
                        } else {
                            wordPronunciation = bufferString.substring(endPoint, bufferString.length());
                            System.out.println(wordPronunciation);
                        }
                        wordText = bufferString.substring(1,endPoint);


                    } else if (bufferString.charAt(0) == '-' && wordText != "") {

                        wordDef += bufferString;
                    }

                }
            }

            d.addWord(wordText, wordDef);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void insertData(String word, String def) {

        String query = "INSERT INTO words VALUES(?,?)";

        try (
                PreparedStatement towa = connection.prepareStatement(query)) {
            towa.setString(1, word);
            towa.setString(2, def);
            towa.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void cleanUpSpace(Dictionary dictionary) {
        for(Word word : dictionary.wordArrayList) {
            // if(word.getText().charAt(word.getText().length()-1) == ' ')
            {
                // System.out.println("cleaned");
                String query = "UPDATE words " +
                        "SET word_text = ? " +
                        "WHERE word_text = ?" ;
                try (
                        PreparedStatement towa = connection.prepareStatement(query)) {
                    towa.setString(1,
                            word.getWordTarget());
                    towa.setString(2, word.getWordTarget() + " ");
                    towa.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteData(String wordText) {
        String query = "DELETE FROM words WHERE word_text=?;";
        try (
                PreparedStatement towa = connection.prepareStatement(query)) {

            towa.setString(1, wordText);

            towa.executeUpdate();
            System.out.println(wordText + " has been deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWordData(String wordText, String wordDef) {
        String query = "UPDATE words " +
                "SET word_def = ? " +
                "WHERE word_text = ?" ;
        try (
                PreparedStatement towa = connection.prepareStatement(query)) {

            towa.setString(1, wordDef);
            towa.setString(2, wordText);
            towa.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void printDictionary() {
        try(Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT word_text, word_def from words") ){
            while(resultSet.next()) {
                System.out.println(resultSet.getString("word_text")+ "\t" +
                        resultSet.getString("word_def"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DictionaryManagement dm = new DictionaryManagement();

        SqlDataManagement sdm = new SqlDataManagement();
        Dictionary dictionary = new Dictionary();

        sdm.insertFromFileAdvanced(dictionary,
                System.getProperty("user.dir") + "\\DATA\\dictionaries.txt");
        DictionaryCommandline dc = new DictionaryCommandline();
        dc.showAllWords(dictionary);
        sdm.connect();
        sdm.createTable();
        sdm.insertFromDictionary(dictionary);

        sdm.printDictionary();

    }

}
