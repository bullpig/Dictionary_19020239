package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DictionaryApplication extends DictionaryAppAction implements ActionListener {

    private JFrame mainFrame;
    private final Dictionary dictionaryMain = new Dictionary();
    private final Dictionary dictionaryFavourite = new Dictionary();
    private final Dictionary dictionaryHistory = new Dictionary();
    private Dictionary dictionaryToShow;
    protected JMenuBar mainMenuBar;
    protected JMenu mainMenu, fileMenu;
    protected JMenuItem addWordOption, removeWordOption, speakWordOption, editWordOption,
            exitOption;


    final int width = 800;
    final int height = 600;
    final int wordListWidth = 150;
    private JButton speakButton, addButton, removeButton, editButton;

    private TextAreaWithImage textAreaDefinition;
    private final String imageFolderPath = System.getProperty("user.dir") + "\\DATA\\Image\\";

    private JList<String> wordList = new JList<>((String[]) null);
    private JScrollPane wordListScrollBar = new JScrollPane(null);

    private JTextField textSearchBar;
    private final textToSpeech speaker = new textToSpeech();

    String[] nullString = {};
    public DictionaryApplication() {

        sqlInit(dictionaryMain);
        prepareMenu();
        prepareGUI();
    }



    public void UI(DictionaryApplication dictionaryApplication) {

        mainFrame.setLocationRelativeTo(null);

        mainFrame.setResizable(false);

        dictionaryToShow = dictionaryMain;

        prepareWordList();

        UpdateList(dictionaryMain.wordTargetArray(), 1);

        prepareTextSearchBar();
        dictionaryApplication.prepareTextAreaDefinition();
        mainFrame.setVisible(true);
    }

    Image iconFromFile(String fileName) {
        return Toolkit.getDefaultToolkit().getImage(imageFolderPath + fileName);
    }

    public void UpdateList(String[] s, int flag) {

        if (flag == 1) {
            wordList = new JList<>(s);
        } else {
            wordList = new JList<>(dictionaryMain.wordTargetArray());
        }


        wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wordList.setVisibleRowCount(20);
        wordListScrollBar.remove(wordList);
        mainFrame.remove(wordListScrollBar);

        Font font = new Font("Times New Roman", Font.BOLD, 20);
        wordList.setFont(font);

        wordList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String wordNewAfterFilter = wordList.getSelectedValue();
                int i = DictionaryManagement.dictionaryLookup(
                        dictionaryToShow.getWordArrayList(), wordNewAfterFilter);

                textAreaDefinition.setText(dictionaryToShow.getWordArrayList().get(i).getWordTarget() +
                        "  " + dictionaryToShow.getWordArrayList().get(i).getWordExplainLine());
            }
        });

        wordListScrollBar = new JScrollPane(wordList);
        wordListScrollBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainFrame.add(wordListScrollBar);
        wordListScrollBar.setBounds(27, 50, wordListWidth, (int) ((double) height * 0.8));
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        DictionaryApplication dictionaryApplication = new DictionaryApplication();
        dictionaryApplication.UI(dictionaryApplication);
    }

    public void prepareWordList() {

    }
    public void prepareTextAreaDefinition() {
        textAreaDefinition = new TextAreaWithImage(1, 10, imageFolderPath + "textBackground.png");
        textAreaDefinition.setEditable(false);
        textAreaDefinition.setCaretPosition(0);

        Font font = new Font("Times New Roman", Font.BOLD, 16);
        textAreaDefinition.setFont(font);

        JScrollPane outputScrollPane = new JScrollPane(textAreaDefinition,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainFrame.add(outputScrollPane);
        outputScrollPane.setBounds(180, 50, 550, height - 120);

    }

    public void prepareMenu() {
        fileMenu = new JMenu("File");
        mainMenu = new JMenu("Edit");

        mainMenuBar = new JMenuBar();

        addWordOption = new JMenuItem("Thêm từ");
        addWordOption.addActionListener(this);


        removeWordOption = new JMenuItem("Xóa từ");
        removeWordOption.addActionListener(this);

        editWordOption = new JMenuItem("Chỉnh sửa từ này");
        editWordOption.addActionListener(this);

        speakWordOption = new JMenuItem("Phát âm");
        speakWordOption.addActionListener(this);

        exitOption = new JMenuItem("Thoát chương trình");
        exitOption.addActionListener(this);
        fileMenu.add(exitOption);

        mainMenu.add(addWordOption);
        mainMenu.add(removeWordOption);
        mainMenu.add(editWordOption);

        mainMenu.add(speakWordOption);
        mainMenuBar.add(fileMenu);

        mainMenuBar.add(mainMenu);
    }

    public void appAddWord() {
        if(addWordWindow(dictionaryMain)) {
            UpdateList(dictionaryMain.wordTargetArray(), 0);
            wordList.setSelectedIndex(newlyAddedWordIndex);
        }
    }
    public void appRemoveWord() {
        if(wordList.getSelectedIndex() != -1) {
            removeWord(dictionaryMain,
                    DictionaryManagement.dictionaryLookup(dictionaryMain.getWordArrayList(),
                            wordList.getSelectedValue())
            );
            UpdateList(nullString, 0);
            textAreaDefinition.setText("");
            textSearchBar.setText("");
            wordList.setSelectedIndex(0);
        }
    }
    public void appEditWord() {
        if(wordList.getSelectedIndex() != -1) {
            if(editWord(dictionaryMain,
                    DictionaryManagement.dictionaryLookup(dictionaryMain.getWordArrayList(),
                            wordList.getSelectedValue())
            )) {
                UpdateList(nullString, 0);
                textAreaDefinition.setText("");
            }
        }
    }
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == addWordOption) {
            appAddWord();

        } else if (source == removeWordOption) {
            appRemoveWord();

        } else if(source == exitOption) {
            System.exit(0);
        } else if(source == speakWordOption) {
            if(wordList.getSelectedIndex() != -1)
                speaker.speakWord(wordList.getSelectedValue());
        } else if(source == editWordOption) {
            appEditWord();
        }

    }



    public void prepareGUI() {
        mainFrame = new JFrame("DICTIONARY");

        mainFrame.setSize(width, height);
        mainFrame.getContentPane().setBackground(Color.RED);

        Image image = loadImageFromFile(imageFolderPath + "background.png", width, height);

        mainFrame.setContentPane(new ImagePanel(image));

        mainFrame.setLayout(null);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(null);

        mainFrame.add(wordListScrollBar);


        prepareButton();

        mainFrame.setJMenuBar(mainMenuBar);
        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
    }

    public void prepareButton() {
        int offset= 180;
        speakButton = new JButton(new ImageIcon(
                imageFolderPath + "speakerIcon.png"));
        speakButton.setBounds(offset,10,40,40);

        speakButton.addActionListener(e -> {
            if(wordList.getSelectedIndex() != -1)
                speaker.speakWord(wordList.getSelectedValue());
        });

        addButton = new JButton(new ImageIcon(
                imageFolderPath + "addIcon.png"));
        addButton.setBounds(offset + 45,10,40,40);

        addButton.addActionListener(e -> {
            appAddWord();
        });

        removeButton = new JButton(new ImageIcon(
                imageFolderPath + "removeIcon.png"));
        removeButton.setBounds(offset + 90,10,40,40);

        removeButton.addActionListener(e -> {
            appRemoveWord();
        });

        editButton = new JButton(new ImageIcon(
                imageFolderPath + "editIcon.png"));
        editButton.setBounds(offset + 90 + 45,10,40,40);

        editButton.addActionListener(e -> {
            appEditWord();
        });

        mainFrame.add(speakButton);
        mainFrame.add(addButton);
        mainFrame.add(removeButton);
        mainFrame.add(editButton);

    }




    public void prepareTextSearchBar() {
        textSearchBar = new HintTextField("Search...");
        textSearchBar.setOpaque(true);
        textSearchBar.setForeground(Color.LIGHT_GRAY);


        Font f = new Font("Times New Roman", Font.BOLD, 20);
        textSearchBar.setFont(f);
        mainFrame.add(textSearchBar);


        textSearchBar.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                Search(dictionaryMain);
                mainFrame.setVisible(true);
            }
        });

        textSearchBar.addActionListener(e-> {
            wordList.setSelectedIndex(0);
        });

        textSearchBar.setBounds(27, 9, wordListWidth, 40);
    }

    public void Search(Dictionary dictionary) {
        String s = textSearchBar.getText();
        if (!s.equals("")) {
            textSearchBar.setForeground(Color.BLACK);

            UpdateList(dictionary.searcherFilter(s), 1);
        } else {

            textSearchBar.setForeground(Color.LIGHT_GRAY);

            UpdateList(dictionary.wordTargetArray(), 0);
        }
    }

}
