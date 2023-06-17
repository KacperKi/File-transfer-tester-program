import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

import static java.lang.Thread.sleep;

public class MyPanel extends JPanel {
    private static JFrame frame;
    Thread threadsObserver;
    String pathToFile; boolean fileExistStop = false;
    private JLabel jcomp1,jcomp4,jcomp10,pathToTest, jcomp15;
    private JCheckBox multipleThreadsEnable, fileSizeEnable,accept, repeatsEnable;
    private JTextField numberOfThreads, numberOfRepeat;
    private JSlider fileSizeSelect;
    private JButton startTestButton, selectTestingButton, selectFolderForResult;
    private JTextField numberOfThreadInformation, lopgInfoArea, selectedPathToTestingFolder, selectedPathForResult;
    private int threadsNumber = 1;
    private int activeThreads = 0;
    private int closed = 1;
    float resultPercent = (float) 0.0;
    boolean testWasFinished = false;

    public MyPanel() {
        createPanel();

        startTestButton.setEnabled(false);
        fileSizeSelect.setValue(1);
        createListener();


    }
    void createPanel(){
        //construct components
        jcomp1 = new JLabel ("NAS/FTP/Disc Tester - inż. K. Kisielewski - 2023, Lublin");
        multipleThreadsEnable = new JCheckBox ("Multiple Threads");
        repeatsEnable = new JCheckBox ("n - repeate");

        numberOfThreads = new JTextField (5);
        numberOfRepeat = new JTextField(5);
        jcomp4 = new JLabel ("Nr Threads");
        fileSizeEnable = new JCheckBox ("Specify size of file");
        fileSizeSelect = new JSlider (0, 50);
        startTestButton = new JButton ("Start Test");
        numberOfThreadInformation = new JTextField (5);
        pathToTest = new JLabel ("Specify testing folder");
        jcomp10 = new JLabel ("logInfo");
        lopgInfoArea = new JTextField (5);
        accept = new JCheckBox ("");
        selectTestingButton = new JButton ("Select");
        selectedPathToTestingFolder = new JTextField (5);
        jcomp15 = new JLabel ("Specify folder to store data");
        selectFolderForResult = new JButton ("Select");
        selectedPathForResult = new JTextField (5);

        //set components properties
        numberOfThreads.setEnabled (false);
        numberOfThreads.setText("1");
        numberOfRepeat.setEnabled(false);
        numberOfRepeat.setText("10");
        fileSizeSelect.setOrientation (JSlider.HORIZONTAL);
        fileSizeSelect.setMinorTickSpacing (10);
        fileSizeSelect.setMajorTickSpacing (3);
        fileSizeSelect.setPaintTicks (true);
        fileSizeSelect.setPaintLabels (true);
        fileSizeSelect.setEnabled (false);
        fileSizeSelect.setToolTipText ("1 MB");
        startTestButton.setToolTipText ("Press Button to Start Test!");
        numberOfThreadInformation.setEnabled (false);
        numberOfThreadInformation.setToolTipText ("This field show number of threads.");
        lopgInfoArea.setEnabled (false);
        lopgInfoArea.setToolTipText ("Information");
        accept.setToolTipText ("Accept the terms and risks");
        selectedPathToTestingFolder.setToolTipText ("Here is path to selected folder.");
        selectedPathForResult.setToolTipText ("Here is PATH to your result.");
        numberOfRepeat.setToolTipText("Type here int - n. It's important to improve accurancy of results.");


        //adjust size and set layout
        setPreferredSize (new Dimension (320, 370));
        setLayout (null);

        //add components
        add (jcomp1);
        add (multipleThreadsEnable);
        add (numberOfThreads);
        add (jcomp4);
        add (fileSizeEnable);
        add (fileSizeSelect);
        add (startTestButton);
        add (numberOfThreadInformation);
        add (pathToTest);
        add (jcomp10);
        add (lopgInfoArea);
        add (accept);
        add (selectTestingButton);
        add (selectedPathToTestingFolder);
        add (jcomp15);
        add (selectFolderForResult);
        add (selectedPathForResult);
        add (repeatsEnable);
        add (numberOfRepeat);

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (5, 5, 320, 20);
        repeatsEnable.setBounds(185,35,135,20);
        multipleThreadsEnable.setBounds (0, 35, 135, 20);
        numberOfThreads.setBounds (85, 60, 100, 25);
        numberOfRepeat.setBounds(185,60,100,25);
        jcomp4.setBounds (5, 60, 100, 25);
        fileSizeEnable.setBounds (0, 90, 150, 20);
        fileSizeSelect.setBounds (5, 110, 315, 60);
        startTestButton.setBounds (5, 300, 100, 25);
        numberOfThreadInformation.setBounds (120, 300, 165, 25);
        pathToTest.setBounds (5, 165, 130, 25);
        jcomp10.setBounds (10, 330, 45, 25);
        lopgInfoArea.setBounds (55, 330, 230, 25);
        accept.setBounds (295, 300, 20, 25);
        selectTestingButton.setBounds (5, 195, 100, 25);
        selectedPathToTestingFolder.setBounds (110, 195, 210, 25);
        jcomp15.setBounds (5, 225, 185, 20);
        selectFolderForResult.setBounds (5, 245, 100, 25);
        selectedPathForResult.setBounds (110, 245, 210, 25);
    }
    void createListener(){
        multipleThreadsEnable.addItemListener(e -> {
            numberOfThreads.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            if(e.getStateChange() == ItemEvent.DESELECTED) threadsNumber = 1;
        });
        fileSizeEnable.addItemListener(e -> {
            fileSizeSelect.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
        });
        accept.addItemListener(e -> {
            startTestButton.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(selectedPathForResult.getText().equals("") || selectedPathToTestingFolder.getText().equals("")) accept.setSelected(false);
            }
        });
        numberOfThreads.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' && Float.valueOf(numberOfThreads.getText()) <= 1000){
                    lopgInfoArea.setText("New number threads: " + numberOfThreads.getText());
                    threadsNumber = Integer.valueOf(numberOfThreads.getText());
                }
                else {
                    numberOfThreads.setText("");
                    threadsNumber = 1;
                }
                numberOfThreadInformation.setText("Active " + activeThreads + "/" + threadsNumber);

            }
        });
        numberOfThreads.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                if(numberOfThreads.getText().equals(""))numberOfThreads.setText("1");
            }
        });
        fileSizeSelect.addChangeListener(e -> {
            if(fileSizeSelect.getValue() ==0 ) fileSizeSelect.setValue(1);
            lopgInfoArea.setText("New size of file: " + fileSizeSelect.getValue() + " MB");
            fileSizeSelect.setToolTipText (fileSizeSelect.getValue() + " MB");
        });
        selectTestingButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION){
                selectedPathToTestingFolder.setText(fileChooser.getSelectedFile().getPath());
            }
        });
        selectFolderForResult.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION){
                selectedPathForResult.setText(fileChooser.getSelectedFile().getPath());
            }else{
                fileChooser.setSelectedFile(new File("C:\\Users"));
                selectedPathForResult.setText("C:\\Users");
            }
        });
        repeatsEnable.addItemListener(e -> {
            numberOfRepeat.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            if(e.getStateChange() == ItemEvent.DESELECTED) numberOfRepeat.setText("10");
        });
        numberOfRepeat.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                if(Integer.parseInt(numberOfRepeat.getText() )> 0 && Integer.parseInt(numberOfRepeat.getText()) < 10001 )numberOfRepeat.setText("10");
            }
        });

        startTestButton.addActionListener(e -> {
            testAllElements();
            resultPercent = 0;
            activeThreads = 0;
            testWasFinished = false;

            while(closed == 1){
                //DO NTH
                try{
                    sleep(500);
                }catch(Exception we){

                }
            }
            if(closed == 0){
                resultPercent = (float) 0;
                activeThreads = 0;

                generateRandomFile();

                if(!fileExistStop) {
                    observeThreadsNumber();
                    runInfoStatusMonitor();
                    runThreads();
                }
            }
        });

    }

    void runInfoStatusMonitor(){
        JFrame f = new JFrame("Status Test Monitor");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container content = f.getContentPane();
        JProgressBar progressBar = new JProgressBar();

        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Testing Server...");

        progressBar.setBorder(border);

        content.add(progressBar, BorderLayout.NORTH);
        f.setSize(300, 100);
        f.setVisible(true);

        Thread refreshingStat = new Thread("Watcher") {
            public void run(){
                while(!testWasFinished){
                    progressBar.setValue((int) getResultPercent());
                }
                progressBar.setValue(100);
                Border border = BorderFactory.createTitledBorder("Testing finished!");
                progressBar.setBorder(border);

            }
        };
        refreshingStat.start();

    }
    boolean testAllElements(){
        Path pathTesting = Path.of(selectedPathToTestingFolder.getText());
        Path pathResult = Path.of(selectedPathForResult.getText());
        if(Files.exists(pathTesting) && Files.exists(pathResult) && !selectedPathToTestingFolder.getText().equals("") && !selectedPathForResult.getText().equals("")) {

            closed = JOptionPane.showOptionDialog(frame,
                    "All selected path are correct!\n" +
                            "Program will start when this icon will be closed",
                    "Test information",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    null
            );
            return true;
        }
        else {
            accept.setSelected(false);
            closed = JOptionPane.showOptionDialog(frame,
                    "We have problem with PATHs. Select new!",
                    "Error 404r",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    null,
                    null
            );
            return false;
        }
    }
    void observeThreadsNumber(){
        threadsObserver = new Thread("Thread Number Watcher") {
            public void run(){
                while(!testWasFinished){
                    int threadDone = threadsNumber - activeThreads;
                    setResultPercent((float) (( (float) threadDone / (float) threadsNumber ) * 100.0));
                }
            }
        };
        try{
            sleep(1000);
        } catch(Exception e){}
        threadsObserver.start();
    }

    public float getResultPercent() {
        return resultPercent;
    }
    public void setResultPercent(float resultPercent) {
        this.resultPercent = resultPercent;
    }


    void generateRandomFile(){
        String fileName = "\\test.txt";
        pathToFile = selectedPathToTestingFolder.getText() + fileName;
        boolean dialogShow = false;

        if(Files.exists(Path.of(pathToFile))) {
            dialogShow = true;
            int result = JOptionPane.showConfirmDialog(frame,"Sure? You want to overide file?", "Warning! File exists!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if(result == JOptionPane.YES_OPTION){
                File file = new File(pathToFile);
                file.delete();

                while(Files.exists(Path.of(pathToFile))){} //wait for remove file
                fileExistStop = false;

            }else if (result == JOptionPane.NO_OPTION){
                fileExistStop = true;
            }else {
                fileExistStop = true;
            }
            dialogShow = false;
        }

        while(dialogShow){}

        if(!fileExistStop) {
            File file = new File(pathToFile);
            RandomAccessFile randomAccessFile;
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.setLength(ConertMbToBytes(fileSizeSelect.getValue()));
                randomAccessFile.close();
            } catch (Exception e) {}
        }
        while(!Files.exists(Path.of(pathToFile)) && !fileExistStop){}

    }
    int ConertMbToBytes(int numberMb){
        return numberMb * 1048576;

    }

    void runThreads(){
        ArrayList<Runnable> listOfTh = new ArrayList<>();
        for(int i = 0; i < threadsNumber; i++) {

            int finalI = i;
            Thread thread = new Thread("New Thread no. " + finalI) {
                public void run(){
                    activeThreads++;
                    numberOfThreadInformation.setText("Active " + activeThreads + "/" + threadsNumber);

                    try{sleep(1000 + 100 * finalI);}catch (Exception e){}

                    activeThreads--;
                    numberOfThreadInformation.setText("Active " + activeThreads + "/" + threadsNumber);



                    if(activeThreads == 1) {
                        testWasFinished = true;
                    }
                }
            };
            listOfTh.add(thread);
            thread.start();
        }
    }

    public static void main (String[] args) {
        frame = new JFrame ("Fail Transfer Tester v1.0");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new MyPanel());
        frame.pack();
        frame.setVisible (true);
    }
}
