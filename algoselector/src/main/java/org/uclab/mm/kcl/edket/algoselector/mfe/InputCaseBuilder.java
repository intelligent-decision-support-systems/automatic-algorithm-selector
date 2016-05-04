package org.uclab.mm.kcl.edket.algoselector.mfe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InputCaseBuilder {

    private String inputCaseFile = "inputCBRCases.txt";
    private String metaFeatureFile = "AlgorithmsSelectionCaseBase.csv";

    /**
     * builds input case file from extracted Meta Features File, if and only if
     * the input case file does not exists.
     * 
     */
    public void buildCasesIfNotExists() throws IOException{
        
        File caseFile = new File(inputCaseFile);
        if (caseFile.exists()) {
            return;
        }

        BufferedReader metaReader = null;
        BufferedWriter caseWriter = null;
        try {
            metaReader = new BufferedReader(new FileReader(metaFeatureFile));
            caseWriter = new BufferedWriter(new FileWriter(caseFile));

            String metaRow = null;
            boolean firstRow = true;
            int caseID = 0;
            while ((metaRow = metaReader.readLine()) != null) {
                if (firstRow) {
                    firstRow = false;
                    metaRow = metaRow.substring(metaRow.indexOf(",") + 1);
                    caseWriter.append("# This file is generated from ["+ metaFeatureFile + "].\n");
                    caseWriter.append("# Column headings are given below, the order of columns matter. So please do not modify this file.\n");
                    caseWriter.append("# " + metaRow + "\n\n");
                    continue;
                }
                caseID++;
                metaRow = metaRow.substring(metaRow.indexOf(",") + 1);
                caseWriter.append(caseID + "," + metaRow + "\n");
            }
            
            metaReader.close();
            caseWriter.close();
            metaReader = null;
            caseWriter = null;
        } catch (IOException e) {
            if(metaReader != null || caseWriter != null){
                try {
                    metaReader.close();
                    caseWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
            throw e;
        }
    }

    /**
     * builds input case file from extracted Meta Features File if append ==
     * TRUE then appends the new cases to the end of the existing input case
     * file else if append == FALSE then overrides the existing input case file
     * 
     * @param override
     */
    public void buildCases(boolean append) {
        // . TODO implement buildCases(append)
    }

    public String getInputCaseFile() {
        return inputCaseFile;
    }

    public void setInputCaseFile(String inputCaseFile) {
        this.inputCaseFile = inputCaseFile;
    }

    public String getMetaFeatureFile() {
        return metaFeatureFile;
    }

    public void setMetaFeatureFile(String metaFeatureFile) {
        this.metaFeatureFile = metaFeatureFile;
    }
}
