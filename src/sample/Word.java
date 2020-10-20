package sample;

public class Word {
    private String wordTarget;//từ tiếng anh
    private String wordExplain;//từ tiếng việt

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

    public void setWordTarget(String wordTarget) {
        this.wordTarget = wordTarget;
    }

    public void setWordExplain(String wordExplain) {
        this.wordExplain = wordExplain;
    }

    public void addWordExplain(String wordExplain) {
        if (this.wordExplain == null) {
            this.wordExplain = wordExplain;
        } else {
            this.wordExplain += "- " + wordExplain;
        }
    }

    public String getWordExplainLine() {
        String tempExp = wordExplain;
        return tempExp.replaceAll("-","\n");
    }

}
