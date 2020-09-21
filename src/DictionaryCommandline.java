public class DictionaryCommandline {
    static void showAllWords(Dictionary dictionary) {
        System.out.println("No    |English                   |Vietnamese");
        for (int i = 0; i < dictionary.words.size(); i++) {
            System.out.printf("%d     | %-25s| %s", i + 1, dictionary.words.get(i).getWordTarget(), dictionary.words.get(i).getWordExplain());
            System.out.print("\n");
        }
    }

    static void dictionaryBasic(Dictionary dictionary) {
        DictionaryManagement.insertFromCommandline(dictionary);
        showAllWords(dictionary);
    }

}
