package com.jaredscarito.acmgnyrtester.api;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by TheWolfBadger on 11/22/18.
 */
public class Tester {
    private Process process;
    private String javaFile;
    private String inputFileWeb;
    private String outputFileWeb;
    private InputStream stdout;
    private OutputStream stdin;
    private BufferedWriter writer;

    /**
     *
     * @param javaFile
     */
    public Tester(String javaFile) {
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", javaFile);
        try {
            this.process = builder.start();
            this.stdin = this.process.getOutputStream();
            this.stdout = this.process.getInputStream();
            this.writer = new BufferedWriter(new OutputStreamWriter(this.stdin));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.javaFile = javaFile;
    }
    public Tester(String javaFile, String inputFileWeb, String outputFileWeb) {
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", javaFile);
        try {
            this.process = builder.start();
            this.stdin = this.process.getOutputStream();
            this.stdout = this.process.getInputStream();
            this.writer = new BufferedWriter(new OutputStreamWriter(this.stdin));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.javaFile = javaFile;
        this.inputFileWeb = inputFileWeb;
        this.outputFileWeb = outputFileWeb;
    }
    public void setJudgeInput(String url) {
        this.inputFileWeb = url;
    }
    public void setJudgeOutput(String url) {
        this.outputFileWeb = url;
    }
    /* Getters */

    /**
     *
     * @return
     */
    public String[] getJudgeInputs() {
        URL inputFileURL = null;
        ArrayList<String> inputs = new ArrayList<>();
        try {
            inputFileURL = new URL(this.inputFileWeb);
            InputStream inputFileStream = inputFileURL.openStream();
            BufferedReader webReader = new BufferedReader(new InputStreamReader(inputFileStream));
            while(webReader.ready()) {
                inputs.add(webReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] inputArr = new String[inputs.size()];
        int cout = 0;
        for(String input : inputs) {
            inputArr[cout] = input;
            cout++;
        }
        return inputArr;
    }

    /**
     *
     * @return
     */
    public String[] getJudgeOutputs() {
        ArrayList<String> outputs = new ArrayList<>();
        try {
            URL outputFileURL = new URL(this.outputFileWeb);
            InputStream outputFileStream = outputFileURL.openStream();
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(outputFileStream));
            while(outputReader.ready()) {
                outputs.add(outputReader.readLine());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String[] outputArr = new String[outputs.size()];
        int cout = 0;
        for(String output : outputs) {
            outputArr[cout] = output;
            cout++;
        }
        return outputArr;
    }

    /**
     *
     * @return
     */
    private String[] outputs = null;
    public String[] getOutputs() {
        if(this.outputs == null) {
            Scanner scanner = new Scanner(this.stdout);
            ArrayList<String> outputs = new ArrayList<>();
            while (scanner.hasNext()) {
                outputs.add(scanner.nextLine());
            }
            int cout = 0;
            String[] outputArr = new String[outputs.size()];
            for (String output : outputs) {
                outputArr[cout] = output;
                cout++;
            }
            this.outputs = outputArr;
            return outputArr;
        }
        return this.outputs;
    }

    /* Voids */

    /**
     *
     */
    public void writeJudgeInputs() {
        String[] judgeInputs = getJudgeInputs();
        try {
            for (String input : judgeInputs) {
                this.writer.write(input);
                this.writer.write("\n");
                this.writer.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void end() {
        this.process.destroy();
    }


    /**
     *
     * @return
     */
    public int[] getComparedWrongLocations() {
        String[] judgeOutputs = getJudgeOutputs();
        String[] outputs = getOutputs();
        int[] comparedWrongLocations = new int[outputs.length];
        int cout = 0;
        for(String output : outputs) {
            if(!output.equalsIgnoreCase(judgeOutputs[cout])) {
                // It is not the same
                comparedWrongLocations[cout] = cout;
            }
            cout++;
        }
        boolean returnNull = true;
        for(int loc : comparedWrongLocations) {
            if(loc != 0) {
                returnNull = false;
            }
        }
        if(returnNull) return null;
        return comparedWrongLocations;
    }
}
