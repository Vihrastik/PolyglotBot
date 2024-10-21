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
    DbApplication databaseApp;
    public enum Language {
        GERMAN, RUSSIAN, ENGLISH;
    }

    public Vocabulary() {
        filename = ".//data/Wordschatz.json";
        vocabularyLanguages = Arrays.asList(
                "\uD83C\uDDE9\uD83C\uDDEA german",
                "\uD83C\uDDF8\uD83C\uDDEE russian",
                "\uD83C\uDDEC\uD83C\uDDE7 english");
    }

    // Method to parse a JSON file and return it as a JSONObject
    public static JSONObject parseJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(content);
    }

    // Method to get the list of themes from the JSON file
    public List<String> getThemesfromJSONFile() throws JSONException, IOException {
        vocabularyAll = parseJSONFile(filename);
        vocabularyThemes = new ArrayList<>(vocabularyAll.keySet());
        return vocabularyThemes;
    }

    // Method to get words by theme and language from the JSON file
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
        databaseApp = new DbApplication();
        vocabularyThemes = databaseApp.getAllTopics();
        databaseApp.closeConnection();
        return vocabularyThemes;
    }

    public Map<String, List> getWordsByThemeAndLanguageDB(String topic, String firstLang, String secondLang) throws Exception {
        DbApplication databaseApp = new DbApplication();
        Integer topicId = databaseApp.getTopicId(topic);
        var firstLangWords = this.getWordsByTopic(databaseApp, firstLang, topicId);
        var secondLangWords = this.getWordsByTopic(databaseApp, secondLang, topicId);
        var translation = this.getTranslation(databaseApp, firstLang, secondLang);

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
        databaseApp.closeConnection();
        return wordsMap;
    }

    // Method to get words by topic from the database depending on the language
    public Map<Integer, String> getWordsByTopic(DbApplication databaseApp, String language, Integer topicId) throws Exception {
        Language langEnum = Language.valueOf(language.toUpperCase());
        if (langEnum == Language.GERMAN) {
            return databaseApp.getAllGermanWords(topicId);
        } else if (langEnum == Language.RUSSIAN) {
            return databaseApp.getAllRussianWords(topicId);
        } else if (langEnum == Language.ENGLISH) {
            return databaseApp.getAllEnglishWords(topicId);
        } else {
            throw new Exception("Unsupported language");
        }
    }

    // Method to get translations between two languages
    public HashMap<Integer, List<Integer>> getTranslation(DbApplication databaseApp, String firstLang, String secondLang) throws Exception {
        Language firstLangEnum = Language.valueOf(firstLang.toUpperCase());
        Language secondLangEnum = Language.valueOf(secondLang.toUpperCase());

        boolean firstCond = firstLangEnum == Language.GERMAN | secondLangEnum == Language.GERMAN;
        boolean secondCond = firstLangEnum == Language.RUSSIAN | secondLangEnum == Language.RUSSIAN;
        boolean thirdCond = firstLangEnum == Language.ENGLISH | secondLangEnum == Language.ENGLISH;
        if (firstCond & secondCond) {
            return databaseApp.getAllTranslationsRusDe();
        } else {
            if (firstCond & thirdCond) {
                return databaseApp.getAllTranslationsEngDe();
            } else if (thirdCond & secondCond) {
                return databaseApp.getAllTranslationsRusEng();
            } else {
                throw new Exception("No such combination");
            }
        }
    }

}



