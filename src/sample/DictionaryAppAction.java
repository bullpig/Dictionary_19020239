package sample;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DictionaryAppAction {

    protected SqlDataManagement sdm = new SqlDataManagement();
    protected int newlyAddedWordIndex = -1;


    public Image loadImageFromFile(String path, int width, int height) {
        Image image = null;
        try {
            image = ImageIO.read(new File(path))
                    .getScaledInstance(width, height, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void sqlInit(Dictionary dictionary) {
        sdm.connect();

        sdm.insertToDictionary(dictionary);

        dictionary.sort();
    }

    public void removeWord(Dictionary dictionary, int index) {
        String wordToRemove = dictionary.wordByIndex(index).getWordTarget();
        JOptionPane.showMessageDialog(null,
                String.format("\"%s\" đã bị xóa khỏi từ điển!", wordToRemove));

        sdm.deleteData(wordToRemove);
        dictionary.removeWord(index);

    }

    public boolean editWord(Dictionary dictionary, int index) {

        JTextField definition = new JTextField(5);
        JTextField pronunciation = new JTextField(5);

        String def = dictionary.wordByIndex(index).getWordExplain();
        String splitter[] = def.split("/-");
        String pronoun = splitter[0] + "/";
        def = "- " + splitter[1];

        Object[] components = {
                new JLabel("Cách phát âm"), pronunciation,
                new JLabel("Nghĩa"), definition
        };

        pronunciation.setText(pronoun);
        definition.setText(def);

        Word word = dictionary.wordByIndex(index);

        int editWordResult = JOptionPane.showConfirmDialog(null, components,
                "Chỉnh sửa các thuộc tính của từ", JOptionPane.OK_CANCEL_OPTION);


        if (editWordResult == JOptionPane.OK_OPTION) {
            dictionary.wordByIndex(index).setWordExplain(
                    pronunciation.getText() + definition.getText());
            sdm.updateWordData(dictionary.wordByIndex(index).getWordTarget(), dictionary.wordByIndex(index).getWordExplain());
            return true;
        } else {
            return false;
        }

    }


    public boolean addWordWindow(Dictionary dictionary) {
        boolean inserted = false;
        JTextField text = new JTextField(5);
        JTextField definition = new JTextField(5);

        JTextField pronunciation = new JTextField(5);

        Object[] components = {
                new JLabel("Từ cần thêm"), text,
                new JLabel("Cách phát âm"), pronunciation,
                new JLabel("Nghĩa"), definition
        };


        while (true) {
            int addWordResult = JOptionPane.showConfirmDialog(null, components,
                    "Nhập các thuộc tính của từ", JOptionPane.OK_CANCEL_OPTION);


            if (addWordResult == JOptionPane.OK_OPTION) {
                String wordAddMessage;
                if (text.getText().isEmpty()) {
                    wordAddMessage = "Vui lòng nhập từ";
                } else if (pronunciation.getText().isEmpty()) {
                    wordAddMessage = "Vui lòng nhập cách phát âm của từ";
                } else if (definition.getText().isEmpty()) {
                    wordAddMessage = "Vui lòng nhập định nghĩa của từ";
                } else {
                    int i;
                    if ((i = dictionary.findWord(text.getText())) != -1) {

                        dictionary.wordByIndex(i).addWordExplain(definition.getText());
                        sdm.updateWordData(text.getText(), dictionary.wordByIndex(i).getWordExplain());

                        wordAddMessage = String.format(
                                "\"%s\" đã có một định nghĩa mới!", text.getText());

                    } else {
                        String def = "/" + pronunciation.getText() + "/" + "- " + definition.getText();
                        sdm.insertData(text.getText(), def);
                        dictionary.addWord(text.getText(), def);
                        wordAddMessage = String.format(
                                "\"%s\" đã thêm vào từ điển thành công!", text.getText());

                    }
                    JOptionPane.showMessageDialog(null, wordAddMessage);
                    dictionary.sort();
                    newlyAddedWordIndex = dictionary.findWord(text.getText());
                    inserted = true;
                    break;
                }
                JOptionPane.showMessageDialog(null, wordAddMessage);


            } else {
                break;
            }
        }
        return inserted;
    }

}

