import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

import static java.lang.Thread.sleep;

public class MyPanel extends JPanel {
    private JLabel jcomp1,jcomp4,jcomp10,pathToTest, jcomp15;
    private JCheckBox multipleThreadsEnable, fileSizeEnable,accept;
    private JTextField numberOfThreads;
    private JSlider fileSizeSelect;
    private JButton startTestButton, selectTestingButton, selectFolderForResult;
    private JTextField numberOfThreadInformation, lopgInfoArea, selectedPathToTestingFolder, selectedPathForResult;
    private int threadsNumber = 1;
    private int activeThreads = 0;

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
        numberOfThreads = new JTextField (5);
        jcomp4 = new JLabel ("Number of Threads");
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

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (5, 5, 320, 20);
        multipleThreadsEnable.setBounds (0, 35, 135, 20);
        numberOfThreads.setBounds (180, 60, 100, 25);
        jcomp4.setBounds (60, 60, 120, 25);
        fileSizeEnable.setBounds (0, 90, 150, 20);
        fileSizeSelect.setBounds (5, 110, 315, 60);
        startTestButton.setBounds (5, 300, 100, 25);
        numberOfThreadInformation.setBounds (120, 300, 165, 25);
        pathToTest.setBounds (5, 165, 130, 25);
        jcomp10.setBounds (10, 330, 45, 25);
        lopgInfoArea.setBounds (55, 330, 230, 25);
        accept.setBounds (295, 315, 20, 25);
        selectTestingButton.setBounds (5, 195, 100, 25);
        selectedPathToTestingFolder.setBounds (110, 195, 210, 25);
        jcomp15.setBounds (5, 225, 185, 20);
        selectFolderForResult.setBounds (5, 245, 100, 25);
        selectedPathForResult.setBounds (110, 245, 210, 25);
    }
    void createListener(){
        multipleThreadsEnable.addItemListener(e -> {
            numberOfThreads.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
        });
        fileSizeEnable.addItemListener(e -> {
            fileSizeSelect.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
        });
        accept.addItemListener(e -> {
            startTestButton.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
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
            }
        });
        fileSizeSelect.addChangeListener(e -> {
            lopgInfoArea.setText("New size of file: " + fileSizeSelect.getValue() + " MB");
            fileSizeSelect.setToolTipText (fileSizeSelect.getValue() + " MB");
        });

        startTestButton.addActionListener(e -> {
            runThreads();
        });

    }


    void runThreads(){
        ArrayList<Runnable> listOfTh = new ArrayList<>();
        for(int i = 0; i < threadsNumber; i++) {

            int finalI = i;
            Thread thread = new Thread("New Thread no. " + finalI) {
                public void run(){
                    System.out.println("Runnable running");
                    activeThreads++;
                    numberOfThreadInformation.setText("Active " + activeThreads + "/" + threadsNumber);

                    try{sleep(1000 + 100 * finalI);}catch (Exception e){}
                    System.out.println("Runnable stoped");

                    activeThreads--;
                    numberOfThreadInformation.setText("Active " + activeThreads + "/" + threadsNumber);
                    System.out.println("Size of table: " + listOfTh.size());
                }
            };
            listOfTh.add(thread);
            thread.start();
        }
    }


    public static void main (String[] args) {
        JFrame frame = new JFrame ("Fail Transfer Tester v1.0");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new MyPanel());
        frame.pack();
        frame.setVisible (true);
    }
}
