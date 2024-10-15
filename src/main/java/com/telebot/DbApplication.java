package com.telebot;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DbApplication {

    private final Connection connection;
    private final Statement statement;

    public DbApplication() throws Exception {
        Dotenv dotenv = Dotenv.load();
        connection = DriverManager.getConnection(
                Objects.requireNonNull(dotenv.get("DB_HOST")),
                "root", dotenv.get("DB_PASSWORD"));

        statement = connection.createStatement();
    }

    public void closeConnection() throws Exception {
        connection.close();
    }

    public List<String> getAllTopics() throws Exception {
        ResultSet results = statement.executeQuery("SELECT * FROM topics");
        var vocabularyThemes = new ArrayList<String>();
        while (results.next()) {
            vocabularyThemes.add(results.getString(2));
        }

        return vocabularyThemes;
    }


    public Integer getTopicId(String topicName) throws SQLException {
        ResultSet results = statement.executeQuery(String.format("SELECT idtopics FROM topics where value='%s';", topicName));
        results.next();
        return results.getInt(1);
    }

    public HashMap<Integer, String> getAllRussianWords(Integer topicId) throws Exception {
        ResultSet results = statement.executeQuery(String.format("SELECT * FROM russian_words where topic_id=%s;", topicId.toString()));
        var russianWords = new HashMap<Integer, String>();
        while (results.next()) {
            russianWords.put(results.getInt(1), results.getString(2));
        }

        return russianWords;
    }

    public HashMap<Integer, String> getAllGermanWords(Integer topicId) throws Exception {
        ResultSet results = statement.executeQuery(String.format("SELECT * FROM german_words where topic_id=%s;", topicId.toString()));
        var germanWords = new HashMap<Integer, String>();
        while (results.next()) {
            germanWords.put(results.getInt(1), results.getString(2));
        }

        return germanWords;
    }

    public HashMap<Integer, String> getAllEnglishWords(Integer topicId) throws Exception {
        ResultSet results = statement.executeQuery(String.format("SELECT * FROM english_words where topic_id=%s;", topicId.toString()));
        var englishWords = new HashMap<Integer, String>();
        while (results.next()) {
            englishWords.put(results.getInt(1), results.getString(2));
        }

        return englishWords;
    }

    public HashMap<Integer, List<Integer>> getAllTranslationsRusDe() throws Exception {
        ResultSet results = statement.executeQuery("SELECT * FROM ru_de;");
        var translationsRusDe = new HashMap<Integer, List<Integer>>();
        while (results.next()) {
            translationsRusDe.computeIfAbsent(results.getInt(1), k -> new ArrayList<>()).add(results.getInt(2));
        }

        return translationsRusDe;
    }

    public HashMap<Integer, List<Integer>> getAllTranslationsEngDe() throws Exception {
        ResultSet results = statement.executeQuery("SELECT * FROM eng_de;");
        var translationsEngDe = new HashMap<Integer, List<Integer>>();
        while (results.next()) {
            translationsEngDe.computeIfAbsent(results.getInt(1), k -> new ArrayList<>()).add(results.getInt(2));
        }

        return translationsEngDe;
    }

    public HashMap<Integer, List<Integer>> getAllTranslationsRusEng() throws Exception {
        ResultSet results = statement.executeQuery("SELECT * FROM ru_eng;");
        var translationsRusEng = new HashMap<Integer, List<Integer>>();
        while (results.next()) {
            translationsRusEng.computeIfAbsent(results.getInt(1), k -> new ArrayList<>()).add(results.getInt(2));
        }

        return translationsRusEng;
    }

}
