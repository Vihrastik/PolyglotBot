package com.telebot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Vocabulary {

    String filename;
    JSONObject vocabularyAll;
    List<String> vocabularyThemes;
    public List<String> vocabularyLanguages;
    DbApplication dbApp;

    public Vocabulary() {
        filename = ".//data/Wordschatz.json";
        vocabularyLanguages = Arrays.asList(
                "\uD83C\uDDE9\uD83C\uDDEA german",
                "\uD83C\uDDF8\uD83C\uDDEE russian",
                "\uD83C\uDDEC\uD83C\uDDE7 english");
    }

    public static JSONObject parseJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(content);
    }

    public List<String> getThemesfromJSONFile() throws JSONException, IOException {
        vocabularyAll = parseJSONFile(filename);
        vocabularyThemes = new ArrayList<>(vocabularyAll.keySet());
        return vocabularyThemes;
    }

    public Map<String, List> getWordsByThemeAndLanguageJSONFile(String topic, String fromLand, String toLang) throws IOException {
        Map<String, List> wordsMap = new HashMap<>();
        JSONArray allWords = parseJSONFile(filename).getJSONArray(topic);
        for (int i = 0; i < allWords.length(); i++) {
            Map<String, Object> currentWord = allWords.getJSONObject(i).toMap();
            if (currentWord.containsKey(fromLand) && currentWord.containsKey(toLang)) {
                wordsMap.put(currentWord.get(fromLand).toString(), Arrays.asList(currentWord.get(toLang).toString().strip().split(",")));
            }
        }
        return wordsMap;
    }

    public String getWordExample(String topic, String currentLanguage, String word) {
        JSONArray allWords = vocabularyAll.getJSONArray(topic);
        for (int i = 0; i < allWords.length(); i++) {
            Map<String, Object> currentWord = allWords.getJSONObject(i).toMap();
            if (word == currentWord.get(currentLanguage) && currentWord.containsKey("example")) {
                return currentWord.get("example").toString();
            }
        }
        return "";
    }

    public List<String> getThemesFromDB() throws Exception {
        dbApp = new DbApplication();
        vocabularyThemes = dbApp.getAllTopics();
        dbApp.closeConnection();
        return vocabularyThemes;
    }

    public Map<String, List> getWordsByThemeAndLanguageDB(String topic, String firstLang, String secondLang) throws Exception {
        DbApplication dbApp = new DbApplication();
        Integer topicId = dbApp.getTopicId(topic);
        var firstLangWords = this.getWordsByTopic(dbApp, firstLang, topicId);
        var secondLangWords = this.getWordsByTopic(dbApp, secondLang, topicId);
        var translation = this.getTranslation(dbApp, firstLang, secondLang);

        Map<String, List> wordsMap = new HashMap<>();
        for (Integer wordId : firstLangWords.keySet()) {
            var firstKey = firstLangWords.get(wordId);
            var secondKeyIds = translation.get(wordId);
            ArrayList<String> secondKeys = new ArrayList<>();
            for (Integer secondKeyId : secondKeyIds) {
                secondKeys.add(secondLangWords.get(secondKeyId));
            }
            wordsMap.put(firstKey, secondKeys);
        }
        dbApp.closeConnection();
        return wordsMap;
    }

    public Map<Integer, String> getWordsByTopic(DbApplication dbApp, String language, Integer topicId) throws Exception {
        if (Objects.equals(language, "german")) {
            return dbApp.getAllGermanWords(topicId);
        } else if (Objects.equals(language, "russian")) {
            return dbApp.getAllRussianWords(topicId);
        } else {
            return dbApp.getAllEnglishWords(topicId);
        }
    }

    public HashMap<Integer, List<Integer>> getTranslation(DbApplication dbApp, String firstLang, String secondLang) throws Exception {
        boolean firstCond = Objects.equals(firstLang, "german") | Objects.equals(secondLang, "german");
        boolean secondCond = Objects.equals(firstLang, "russian") | Objects.equals(secondLang, "russian");
        boolean thirdCond = Objects.equals(firstLang, "english") | Objects.equals(secondLang, "english");
        if (firstCond & secondCond) {
            return dbApp.getAllTranslationsRusDe();
        } else {
            if (firstCond & thirdCond) {
                return dbApp.getAllTranslationsEngDe();
            } else if (thirdCond & secondCond) {
                return dbApp.getAllTranslationsRusEng();
            } else {
                throw new Exception("No such combination");
            }
        }
    }

}



