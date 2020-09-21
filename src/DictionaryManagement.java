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
            dictionary.words.add(word);
        }


    }
}
