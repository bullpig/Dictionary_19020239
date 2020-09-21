public class Word {
    private String wordTarget;
    private String wordExplain;

    Word(String wordTarget, String wordExplain) {
        this.wordTarget = wordTarget;
        this.wordExplain = wordExplain;
    }


    String getWordTarget() {
        return wordTarget;
    }

    String getWordExplain() {
        return wordExplain;
    }

    void setWordTarget(String _wordTarget) {
        wordTarget = _wordTarget;
    }

    void setWordExplain(String _wordExplain) {
        wordExplain = _wordExplain;
    }
}
