package sample;

import java.io.IOException;
import java.util.ArrayList;

enum wordFindType {
    matchFirst, matchAll
}

public class DictionaryCommandline {

    static void showAllWords(Dictionary dictionary) {
        System.out.printf("%-10s%-25s%-25s\n", "No", "|English", "|Vietnamese");
        for (int i = 0; i < dictionary.wordArrayList.size(); i++) {
            System.out.printf("%-10d%-25s%-25s", i + 1,
                    "|" + dictionary.wordArrayList.get(i).getWordTarget(),
                    "|" + dictionary.wordArrayList.get(i).getWordExplain());
            System.out.print("\n");
        }
    }

    static void dictionaryBasic(Dictionary dictionary) {
        DictionaryManagement.insertFromCommandline(dictionary);
        showAllWords(dictionary);
    }

    static void dictionaryAdvanced(Dictionary dictionary) throws IOException {
        DictionaryManagement.insertFromFile(dictionary);
        showAllWords(dictionary);
    }

    public ArrayList<Word> dictionaryFinder(Dictionary d, String wordToFind, wordFindType typeOfWordFind) {
        ArrayList<Word> wordsFound = new ArrayList<>();
        switch (typeOfWordFind) {
            case matchFirst:
                int n = wordToFind.length();

                for (Word word : d.wordArrayList) {
                    if (n > word.getWordTarget().length()) {

                        continue;
                    }
                    String temp = word.getWordTarget().substring(0, n);
                    if (wordToFind.charAt(0) == word.getWordTarget().charAt(0) && temp.equals(wordToFind)) {
                        wordsFound.add(word);
                    }
                }
                break;
            case matchAll:
                for (Word word : d.wordArrayList) {
                    if(word.getWordTarget().contains(wordToFind)) {
                        wordsFound.add(word);

                    }
                }

                break;
            default:


        }
        return  wordsFound;
    }
}
