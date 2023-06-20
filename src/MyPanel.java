import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import java.io.FileWriter;
import java.util.Locale;

import static java.lang.Thread.sleep;
import static java.time.temporal.ChronoUnit.SECONDS;

public class MyPanel extends JPanel {
    private static JFrame frame;
    Thread threadsObserver;
    String pathToFile; boolean fileExistStop = false;
    private JLabel jcomp1,jcomp4,jcomp10,pathToTest, jcomp15, infoFileStarter;
    private JCheckBox multipleThreadsEnable, fileSizeEnable,accept, repeatsEnable;
    private JTextField numberOfThreads, numberOfRepeat;
    private JSlider fileSizeSelect;
    private JButton startTestButton, selectTestingButton, selectFolderForResult, selectPathToFile;
    private JTextField numberOfThreadInformation, lopgInfoArea, selectedPathToTestingFolder, selectedPathForResult,
                        selectedPathToCreateFile;
    private int threadsNumber = 1, accurancy = 10;
    private int activeThreads = 0;
    private int closed = 1;
    float resultPercent = (float) 0.0;
    boolean testWasFinished = false, generateFileFlag = false, startedCreating = false, threadWasCreated = false;
    ArrayList<ArrayList<String>> dataToAVG = new ArrayList<>();
    String startTestDate, endTestDate;

    public MyPanel() {
        createPanel();

        startTestButton.setEnabled(false);
        fileSizeSelect.setValue(1);
        createListener();


    }
    void createPanel(){
        //construct components
        jcomp1 = new JLabel ("File Transfer Tester - inż. K. Kisielewski - 2023, Lublin");
        multipleThreadsEnable = new JCheckBox ("Multiple Threads");
        repeatsEnable = new JCheckBox ("n - repeate");

        numberOfThreads = new JTextField (5);
        numberOfRepeat = new JTextField(5);
        infoFileStarter = new JLabel("Specify path for temp files");
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
        selectPathToFile = new JButton("Select");
        selectedPathToCreateFile = new JTextField(5);

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
        selectedPathToCreateFile.setToolTipText("PATH for temporary file for testing");
        numberOfThreadInformation.setEnabled (false);
        numberOfThreadInformation.setToolTipText ("This field show number of threads.");
        lopgInfoArea.setEnabled (false);
        lopgInfoArea.setToolTipText ("Information");
        accept.setToolTipText ("Accept the terms and risks");
        selectedPathToTestingFolder.setToolTipText ("Here is path to selected folder.");
        selectedPathForResult.setToolTipText ("Here is PATH to your result.");
        numberOfRepeat.setToolTipText("Type here int - n. It's important to improve accurancy of results.");


        //adjust size and set layout
        setPreferredSize (new Dimension (320, 420));
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
        add(infoFileStarter);
        add(selectPathToFile);
        add(selectedPathToCreateFile);

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (5, 5, 320, 20);
        repeatsEnable.setBounds(185,35,135,20);
        multipleThreadsEnable.setBounds (0, 35, 135, 20);
        numberOfThreads.setBounds (85, 60, 100, 25);
        numberOfRepeat.setBounds(185,60,100,25);
        jcomp4.setBounds (5, 60, 100, 25);

        fileSizeEnable.setBounds (0, 90, 150, 20);
        fileSizeSelect.setBounds (5, 110, 315, 60);
        pathToTest.setBounds (5, 170, 130, 25);

        jcomp15.setBounds (5, 225, 185, 20);
        selectFolderForResult.setBounds (5, 245, 100, 25);
        selectedPathForResult.setBounds (110, 245, 210, 25);

        infoFileStarter.setBounds (5, 280, 185, 20);
        selectPathToFile.setBounds (5, 300, 100, 25);
        selectedPathToCreateFile.setBounds (110, 300, 210, 25);

        jcomp10.setBounds (10, 370, 45, 25);
        lopgInfoArea.setBounds (55, 370, 230, 25);
                accept.setBounds (295, 340, 20, 25);
        startTestButton.setBounds (5, 340, 100, 25);
        numberOfThreadInformation.setBounds (120, 340, 165, 25);

        selectTestingButton.setBounds (5, 195, 100, 25);
        selectedPathToTestingFolder.setBounds (110, 195, 210, 25);


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
                if(selectedPathToCreateFile.getText().equals("") || selectedPathForResult.getText().equals("") || selectedPathToTestingFolder.getText().equals("")) accept.setSelected(false);
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
        numberOfRepeat.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' && Integer.valueOf(numberOfRepeat.getText()) <= 1001
                        && Integer.valueOf(numberOfRepeat.getText()) > 0){
                    lopgInfoArea.setText("New n - number: " + numberOfRepeat.getText());
                    accurancy =Integer.valueOf(numberOfRepeat.getText());
                }
                else {
                    numberOfRepeat.setText("10");
                    accurancy = 10;
                }
            }
        });
        numberOfRepeat.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                int value = Integer.valueOf(numberOfRepeat.getText());
                if(value > 0 && value < 1001){
                    accurancy = value;
                } else {
                    numberOfRepeat.setText("10");
                    accurancy = 10;
                }

            }
        });
        selectPathToFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION){
                selectedPathToCreateFile.setText(fileChooser.getSelectedFile().getPath());
            }else{
                fileChooser.setSelectedFile(new File("C:\\Users"));
                selectedPathToCreateFile.setText("C:\\Users");
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
                    clearAllFiles();
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
        Path pathCreate = Path.of(selectedPathToCreateFile.getText());
        if(Files.exists(pathTesting) && Files.exists(pathResult) && Files.exists(pathCreate)
                && !selectedPathToCreateFile.getText().equals("") && !selectedPathToTestingFolder.getText().equals("") && !selectedPathForResult.getText().equals("")) {

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
        generateFileFlag = true;
        for(int sufix = 1; sufix <= threadsNumber; sufix++) {
            String fileName = "\\test" + sufix + ".txt";
            pathToFile = selectedPathToCreateFile.getText() + fileName;
            boolean dialogShow = false;

            if (Files.exists(Path.of(pathToFile))) {
                dialogShow = true;
                int result = JOptionPane.showConfirmDialog(frame, "You want to overide file? Sure?", "Warning! File exists!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    File file = new File(pathToFile);
                    file.delete();

                    while (Files.exists(Path.of(pathToFile))) {
                    } //wait for remove file
                    fileExistStop = false;

                } else if (result == JOptionPane.NO_OPTION) {
                    fileExistStop = true;
                } else {
                    fileExistStop = true;
                }
                dialogShow = false;
            }

            while (dialogShow) {
            }

            if (!fileExistStop) {
                File file = new File(pathToFile);
                RandomAccessFile randomAccessFile;
                try {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.setLength(ConertMbToBytes(fileSizeSelect.getValue()));
                    randomAccessFile.close();
                } catch (Exception e) {
                }

//                Instant dat1 = Instant.now();
//                String nano = String.valueOf(dat1.getNano());
                String timeBase = String.valueOf( new Timestamp(System.currentTimeMillis()));

//                String fullDate = timeBase + "." + nano;

                System.out.println("File " + sufix + " was created at: " + timeBase);

            }
            while (!Files.exists(Path.of(pathToFile)) && !fileExistStop) {
            }
        }
        generateFileFlag = false;
    }
    void clearAllFiles(){
        int thNum = Integer.parseInt(numberOfThreads.getText());

        Thread th = new Thread("Files cleaner") {
            public void run(){
                while(!testWasFinished && !threadWasCreated){
                    System.out.println("Waiting loop - " + testWasFinished + " - " + threadWasCreated);
                    try{sleep(1000);}catch(Exception e){}
                }
                for(int sufix = 1; sufix <= thNum; sufix++) {
                    String fileName = "\\test" + sufix + ".txt";
                    pathToFile = selectedPathToCreateFile.getText() + fileName;
                    File file = new File(pathToFile);
                    file.delete();
                    while(Files.exists(Path.of(pathToFile))){};
                }exportToCSV();
            }
        };
        th.start();
    }

    int ConertMbToBytes(int numberMb){
        return numberMb * 1048576;
    }

    void runThreads(){
        startedCreating = true;
        ArrayList<Runnable> listOfTh = new ArrayList<>();

        dataToAVG = new ArrayList<>();

        while(generateFileFlag){}

        if(listOfTh.size()==0) { startTestDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()); }

        for(int i = 1; i <= threadsNumber; i++) {
            int finalI = i;
            Thread thread = new Thread("New Thread no. " + finalI) {
                public void run() {

                    ArrayList<String> rowData = new ArrayList<>();

                    float[] timesUP = new float[Integer.parseInt(numberOfRepeat.getText())] ;
                    float[] timesDW = new float[Integer.parseInt(numberOfRepeat.getText())] ;

                    int incrementing = activeThreads;
                    threadWasCreated = false;
                    activeThreads++;
                    numberOfThreadInformation.setText("Active " + activeThreads + "/" + threadsNumber);

                    String fileName = "\\test" + finalI + ".txt";

                    Path sourcePath = Path.of(selectedPathToCreateFile.getText() + fileName);
                    Path destinationPath = Path.of(selectedPathToTestingFolder.getText() + fileName);

                    long timeElapsedUP, timeElapsedDW;
                    double elapsedTimeInSecondUP=0F,elapsedTimeInSecondDW=0F;

                    try {
                        rowData.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));

                        for (int resoInt = 1; resoInt <= Integer.parseInt(numberOfRepeat.getText()); resoInt++) {

                            long startUP = System.nanoTime();
                            Path temp = Files.move(sourcePath, destinationPath);
                            long megabytes = ((Files.size(destinationPath) / 1024) / 1024);
                            while(megabytes != fileSizeSelect.getValue()) {}
                            long finishUP = System.nanoTime();

                            timeElapsedUP = finishUP - startUP;
                            elapsedTimeInSecondUP = (double) timeElapsedUP / 1_000_000_000;

                            long startDW = System.nanoTime();
                            Path temp1 = Files.move(destinationPath, sourcePath);
                            megabytes = ((Files.size(sourcePath) / 1024) / 1024);
                            while(megabytes != fileSizeSelect.getValue()) {}
                            long finishDW = System.nanoTime();

                            timeElapsedDW = finishDW - startDW;
                            elapsedTimeInSecondDW = (double) timeElapsedDW / 1_000_000_000;

                            if(Integer.valueOf(numberOfThreads.getText()) != 1 && Integer.parseInt(numberOfRepeat.getText()) != 1) {
                                System.out.println("Control 1");

                                timesUP[resoInt - 1] = (float) elapsedTimeInSecondUP;
                                timesDW[resoInt - 1] = (float) elapsedTimeInSecondDW;
                                System.out.println("Control 2");

                            }
                        }


                    rowData.add(String.valueOf(threadsNumber));
                    rowData.add(String.valueOf(startTestDate));
                    rowData.add(String.valueOf(numberOfRepeat.getText()));
                        System.out.println("Control 3");


                    if(Integer.parseInt(numberOfRepeat.getText()) == 1){
                        rowData.add(String.valueOf(elapsedTimeInSecondUP));
                        rowData.add(String.valueOf(elapsedTimeInSecondDW));
                        System.out.println("Added single row");

                    }else{
                        rowData.add(String.valueOf(calcaulateAvg(timesUP)));
                        rowData.add(String.valueOf(calcaulateAvg(timesDW)));
                        System.out.println("Added avg row");
                    }

                    dataToAVG.add(rowData);

                    System.out.println("Control 4");

                    if(activeThreads==1 && incrementing != 0) {setResultPercent(100F);testWasFinished=true;
                            endTestDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                    }
                    activeThreads--;
                        System.out.println("Control 5");

                    numberOfThreadInformation.setText("Active " + activeThreads + "/" + threadsNumber);
                        System.out.println("Control 6");

                    } catch (Exception e) {System.out.println("HERE1 : " + e);}

                    if(Integer.valueOf(numberOfThreads.getText()) == 1 ){
                        endTestDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                        testWasFinished = true; threadWasCreated = true;}
                }
            };
            listOfTh.add(thread);
            thread.start();
            threadWasCreated = true;
        }
        startedCreating = false;
    }


    void exportToCSV(){
        Thread th = new Thread("CSV Generator") {
            public void run() {
                String dateStartToFileName = startTestDate;
                String dateEndToFileName = endTestDate;
                dateStartToFileName = dateStartToFileName.replace("-", "");
                dateStartToFileName = dateStartToFileName.replace(" ", "_");
                dateStartToFileName = dateStartToFileName.replace(":", "");

                dateEndToFileName = dateEndToFileName.replace("-", "");
                dateEndToFileName = dateEndToFileName.replace(" ", "_");
                dateEndToFileName = dateEndToFileName.replace(":", "");


                String fileName = "\\AllResults_Th" + numberOfThreads.getText() + "_" + fileSizeSelect.getValue() + "MB_" + dateStartToFileName + ".csv";
                String fileNameAvg = "\\AvgResults_Th" + numberOfThreads.getText() + "_"  + fileSizeSelect.getValue() + "MB_" +dateEndToFileName + ".csv";

                String path = selectedPathForResult.getText();

                Path pathToAvg = Path.of(path + fileNameAvg);
                Path pathToAll = Path.of(path + fileName);

                String[] headersAvg = {"DateMeasurement", "ThreadsNumber", "StartDateTest", "Resolution", "AverageTimeUP", "AverageTimeDW"};
                String[] headersAll = {"DateMeasurement", "OperationsNumber", "StartDate", "EndDate",};

                long diff;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                try {
                    diff = Math.abs(sdf.parse(endTestDate).getTime() - sdf.parse(startTestDate).getTime());
                } catch (Exception e) {
                    System.out.println("Error with date");
                }

                try {
                    File csvFileAvg = new File(pathToAvg.toUri());
                    FileWriter fileWriterAvg = new FileWriter(csvFileAvg);

                    File csvFileAll = new File(pathToAll.toUri());
                    FileWriter fileWriterAll = new FileWriter(csvFileAll);

                    StringBuilder lineAll = new StringBuilder();
                    for (String t : headersAll) {
                        lineAll.append(t);
                        if (headersAll[headersAll.length - 1] != t) lineAll.append(',');
                        else lineAll.append("\n");
                    }
                    fileWriterAll.write(lineAll.toString());

                    lineAll = new StringBuilder();
                    lineAll.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                    lineAll.append(',');
                    lineAll.append(threadsNumber * 2 * Integer.parseInt(numberOfRepeat.getText()));
                    lineAll.append(',');
                    lineAll.append(startTestDate);
                    lineAll.append(',');
                    lineAll.append(endTestDate);
                    lineAll.append("\n");
                    fileWriterAll.write(lineAll.toString());
                    fileWriterAll.close();


                    StringBuilder lineAvg = new StringBuilder();
                    for (String t : headersAvg) {
                        lineAvg.append(t);
                        if (headersAvg[headersAvg.length - 1] != t) lineAvg.append(',');
                        else lineAvg.append("\n");
                    }

                    fileWriterAvg.write(lineAvg.toString());
                    for (ArrayList<String> row : dataToAVG) {
                        lineAvg = new StringBuilder();
                        for (String val : row) {
                            lineAvg.append(val);
                            if (row.get(row.size() - 1) != val) lineAvg.append(',');
                            else lineAvg.append("\n");
                        }
                        fileWriterAvg.append(lineAvg);
                    }
                    fileWriterAvg.close();

                } catch (Exception e) { System.out.println("Problem with export csv");
                }
            }

            ;
        };
        th.start();
    }

    float calcaulateAvg(float[] table){
        float sum = 0F;

        System.out.println("Len of table : " + table.length);

        for(float tmp : table){
            System.out.println("Seconds in table : " + tmp);
            sum+=tmp;
        }
        System.out.println("Srednia sec in table : " + sum/(float) table.length);

        return sum/(float) table.length;
    }


    public static void main (String[] args) {
        frame = new JFrame ("File Transfer Tester v.1.0");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new MyPanel());
        frame.pack();
        frame.setVisible (true);
    }
}
