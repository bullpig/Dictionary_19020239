package sample;

import java.util.ArrayList;

public class Dictionary {
    ArrayList<Word> wordArrayList = new ArrayList<>();

    public ArrayList<Word> getWordArrayList() {
        return wordArrayList;
    }

    public Word wordByIndex(int i) {
        return wordArrayList.get(i);
    }

    public int wordCount() {
        return wordArrayList.size();
    }

    public void addWord(String wordTarget, String wordExplain) {
        if (wordTarget.charAt(wordTarget.length() - 1) == ' ') {
            wordTarget = wordTarget.substring(0, wordTarget.length() - 1);
        }
        wordArrayList.add(new Word(wordTarget.toLowerCase(), wordExplain));
        System.out.println("Word added: " + wordTarget);
    }


    public int compareWord(Word a, Word b) {
        return a.getWordTarget().compareTo(b.getWordTarget());
    }

    public void sort() {
        wordArrayList.sort(this::compareWord);
    }

    public String[] wordTargetArray() {
        String[] targetWordArray = new String[wordCount()];
        for (int i = 0; i < wordCount(); i++) {
            targetWordArray[i] = wordByIndex(i).getWordTarget();
        }
        return targetWordArray;
    }

    public void removeWord(int index){
        System.out.println("Word removed: " + wordArrayList.get(index).getWordTarget());
        wordArrayList.remove(index);
    }

    public void removeWord(String wordTarget){
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        int i = dictionaryManagement.dictionaryLookup(wordArrayList,wordTarget);
        if(i!=-1){
            wordArrayList.remove(wordArrayList.get(i));
        }
        else {
            System.out.println("Word Not Found!");
        }
    }

    public String[] searcherFilter(String wordToSearch){
        wordToSearch = wordToSearch.toLowerCase();
        ArrayList<String> filteredWord = new ArrayList<>();
        for(Word word : wordArrayList){
            if(wordToSearch.length()>word.getWordTarget().length())
                continue;
            Boolean same = true;
            for (int i = 0;i<wordToSearch.length();i++){
                if(wordToSearch.charAt(i)!= word.getWordTarget().charAt(i))
                    same = false;
            }
            if(same){
                filteredWord.add(word.getWordTarget());
            }
        }
        return filteredWord.toArray(new String[0]);
    }

    public int findWord(String wordToFind){
        for (int i=0;i<wordArrayList.size();i++){
            if(wordByIndex(i).getWordTarget().equals(wordToFind)){
                return i;
            }
        }
        return -1;
    }

    Dictionary() {
    }
}
