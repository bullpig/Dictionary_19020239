package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryManagement {

    public static void insertFromCommandline(Dictionary dictionary) {
        Scanner scanner = new Scanner(System.in);
        int wordsCount = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < wordsCount; i++) {
            String word_target = scanner.nextLine();
            String word_explain = scanner.nextLine();
            Word word = new Word(word_target, word_explain);
            dictionary.wordArrayList.add(word);
        }
    }

    public static void insertFromFile(Dictionary dictionary) throws IOException {
        File file = new File("\\DATA\\dictionaries.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String word_target = sc.next();
            String word_explain = sc.nextLine();
            Word newWord = new Word(word_target, word_explain);
            dictionary.wordArrayList.add(newWord);
        }
        sc.close();
    }

    public Dictionary WordFromBigFile(ArrayList<String> words){
        Dictionary dictionary = new Dictionary();
        for(String word : words){
            int h = words.indexOf('/');
            if(h==-1)
                continue;
            String s1 = word.substring(1,h);
            String s2 = word.substring(h);
            dictionary.addWord(s1,s2);
        }
        return dictionary;
    }

    public static int dictionaryLookup(ArrayList<Word> arrayList, String s){
        for (int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getWordTarget().equals(s)){
                return i;
            }
        }
        return -1;
    }

    public ArrayList<String> insertFromFileAdvanced() {

        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\DATA\\dictionaries.txt"));
            ArrayList<String> words = new ArrayList<>();
            String f;
            f = in.readLine();
            f = f.substring(1);
            StringBuilder word = new StringBuilder();
            word.append(f).append("\n");

            StringBuilder NextText = new StringBuilder();
            while (f != null) {

                while (true) {
                    if (NextText.length() > 0) {
                        word.append(NextText).append("\n");
                        NextText = new StringBuilder();
                    }

                    f = in.readLine();

                    if (f != null)
                        if (!f.isEmpty() && f.charAt(0) == '@') {
                            NextText.append(f);

                            break;
                        }

                    word.append(f).append("\n");

                    if (f == null) {
                        word = new StringBuilder(word.substring(0, word.length() - 5));
                        break;
                    }

                }

                words.add(word.toString());
                word = new StringBuilder();


            }
            return words;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void main(String[] args)   {

        DictionaryManagement d = new DictionaryManagement();
        ArrayList<String> words = d.insertFromFileAdvanced();
        d.WordFromBigFile(words);
    }


}
