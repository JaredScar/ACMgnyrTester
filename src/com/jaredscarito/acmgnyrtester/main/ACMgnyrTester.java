package com.jaredscarito.acmgnyrtester.main;

import com.jaredscarito.acmgnyrtester.api.Tester;
import com.jaredscarito.acmgnyrtester.display.PanelBuilder;
import com.jaredscarito.acmgnyrtester.display.WindowBuilder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ACMgnyrTester {
    public static WindowBuilder window = new WindowBuilder(null, 600, 600);
    private static PanelBuilder panel = new PanelBuilder("Base", 0, 0, 600, 600);

    private static JFileChooser javaFileChoice = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    private static JLabel fileChooserLabel = new JLabel();
    private static JTextField javaFile = new JTextField();
    private static JButton selectFile = new JButton();
    private static JLabel inputFileLabel = new JLabel();
    private static JTextField inputFileLink = new JTextField();
    private static JLabel outputFileLabel = new JLabel();
    private static JTextField outputFileLink = new JTextField();
    private static JButton submitButton = new JButton();

    public static WindowBuilder resultWindow = new WindowBuilder(null, 600, 600);
    public static JTable table = new JTable();

    private static File selectedFile;

    public static void main(String[] args) {
        window = new WindowBuilder(null, 600, 600);
        PanelBuilder displayResults = new PanelBuilder("Table Results", 0, 0, 600, 600);
        displayResults.setLayout(null);
        panel.setLayout(null);
        /* Add components to JPanel 'PanelBuilder'  */

        // fileChooserLabel
        fileChooserLabel.setText("Choose .jar file to test: ");
        fileChooserLabel.setBackground(Color.LIGHT_GRAY);
        fileChooserLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        fileChooserLabel.setBounds(145, 10, 400, 40);
        fileChooserLabel.setVisible(true);
        panel.add(fileChooserLabel);
        // fileChooser (JFileChooser)
        javaFileChoice.setFileSelectionMode(JFileChooser.FILES_ONLY);
        javaFileChoice.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only Java .jar files", "jar");
        javaFileChoice.setFileFilter(filter);
        // javaFile
        javaFile.setBackground(Color.WHITE);
        javaFile.setBounds(140, 60, 100, 20);
        javaFile.setSize(300, 20);
        javaFile.setToolTipText("Jar file you are testing");
        javaFile.setEditable(false);
        javaFile.setVisible(true);
        panel.add(javaFile);
        // selectFile (JButton)
        selectFile.setText("...");
        selectFile.setBounds(440, 60, 40, 20);
        selectFile.setVisible(true);
        selectFile.addActionListener(new FileSelection());
        panel.add(selectFile);
        // inputFileLabel
        inputFileLabel.setText("Judge's Inputs URL: ");
        inputFileLabel.setBackground(Color.LIGHT_GRAY);
        inputFileLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        inputFileLabel.setBounds(145, 90, 400, 40);
        inputFileLabel.setVisible(true);
        panel.add(inputFileLabel);
        // inputFileLink
        inputFileLink.setBackground(Color.WHITE);
        inputFileLink.setBounds(140, 140, 100, 20);
        inputFileLink.setSize(300, 20);
        inputFileLink.setToolTipText("Link of Judge's input file");
        inputFileLink.setEditable(true);
        inputFileLink.setVisible(true);
        panel.add(inputFileLink);
        // outputFileLabel
        outputFileLabel.setText("Judge's Outputs URL: ");
        outputFileLabel.setBackground(Color.LIGHT_GRAY);
        outputFileLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        outputFileLabel.setBounds(145, 170, 400, 40);
        outputFileLabel.setVisible(true);
        panel.add(outputFileLabel);
        // outputFileLink
        outputFileLink.setBackground(Color.WHITE);
        outputFileLink.setBounds(140, 220, 100, 20);
        outputFileLink.setSize(300, 20);
        outputFileLink.setToolTipText("Link of Judge's output file");
        outputFileLink.setEditable(true);
        outputFileLink.setVisible(true);
        panel.add(outputFileLink);
        // submitButton
        submitButton.setText("Submit");
        submitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        submitButton.setBounds(140, 300, 300, 40);
        submitButton.setVisible(true);
        submitButton.addActionListener(new Submittal());
        panel.add(submitButton);

        // displayResults (JPanel)
        //table.setBounds(140, 60, 400, 2000);
        resultWindow = new WindowBuilder(null, 600, 600);


        panel.setBackground(Color.LIGHT_GRAY);
        window.add(panel);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setVisible(true);
        window.setVisible(true);
    }

    private static class FileSelection implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int r = javaFileChoice.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                javaFile.setText(javaFileChoice.getSelectedFile().getAbsolutePath());
                selectedFile = javaFileChoice.getSelectedFile();
            } else {
                // Pressed cancel option
            }
        }
    }
    private static class Submittal implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Check to see if all are set
            if(selectedFile != null) {
                if(inputFileLink.getText().endsWith(".in")) {
                    if(outputFileLink.getText().endsWith(".out")) {
                        Tester tester = new Tester(selectedFile.getAbsolutePath(), inputFileLink.getText(), outputFileLink.getText());
                        tester.writeJudgeInputs();
                        int[] wrongLocations = tester.getComparedWrongLocations();
                        String[] judgeInputs = tester.getJudgeInputs();
                        String[] judgeOutputs = tester.getJudgeOutputs();
                        String[] outputs = tester.getOutputs();

                        // Show other Panel
                        Object[] columns = new Object[] {"Your Output", "Correct Output", "Input"};
                        Object[][] rows = new Object[][] {};
                        if(wrongLocations != null) {
                            rows = new Object[wrongLocations.length][3];
                            int cout = 0;
                            for (int loc : wrongLocations) {
                                if (loc != 0 && cout != 0) {
                                    String judInput = judgeInputs[loc + 1];
                                    String judOutput = judgeOutputs[loc];
                                    String output = outputs[loc];
                                    rows[loc][0] = output;
                                    rows[loc][1] = judOutput;
                                    rows[loc][2] = judInput;
                                } else {
                                    String judInput = judgeInputs[loc + 1];
                                    String judOutput = judgeOutputs[loc];
                                    String output = outputs[loc];
                                    if (!judOutput.equalsIgnoreCase(output)) {
                                        rows[loc][0] = output;
                                        rows[loc][1] = judOutput;
                                        rows[loc][2] = judInput;
                                    }
                                }
                                cout++;
                            }
                            JPanel panel = new JPanel();
                            panel.setLayout(null);
                            panel.setBounds(0, 0, 600, 600);
                            table = new JTable(rows, columns);
                            table.setPreferredSize(new Dimension(600, 2000));
                            table.setBounds(0, 0, 600, 2000);
                            table.setCellSelectionEnabled(false);
                            JScrollPane scrollPaneTable = new JScrollPane(table);
                            scrollPaneTable.setSize(600, 2000);
                            panel.add(scrollPaneTable);
                            resultWindow.add(panel);
                            resultWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            resultWindow.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "ALL CORRECT OUTPUTS");
                        }
                    } else {
                        // Output File Link wrong
                        JOptionPane.showMessageDialog(null, "The Output File Link should end with '.out'");
                    }
                } else {
                    // Input File Link wrong
                    JOptionPane.showMessageDialog(null, "The Input File Link should end with '.in'");
                }
            } else {
                // No file is selected
                JOptionPane.showMessageDialog(null, "No .jar file is selected to be tested");
            }
        }
    }
}
