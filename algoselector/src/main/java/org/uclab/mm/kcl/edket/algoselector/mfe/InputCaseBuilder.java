package org.uclab.mm.kcl.edket.algoselector.mfe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputCaseBuilder {
    private static Logger LOG = LogManager.getLogger(InputCaseBuilder.class);
    
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
     * Detects and returns Interval value from the resolvedCasesFile.
     * 
     * @param resolvedCasesFile
     * @return Map intervals
     * @throws IOException
     */
    public static Map<MetaFeature, Integer> detectSimIntervals(String resolvedCasesFile) throws IOException{
        
        Map<MetaFeature, Integer> intervals = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(resolvedCasesFile));
            intervals = new HashMap<MetaFeature, Integer>();
            //. initialize intervals with minimum values
            for(MetaFeature mf : MetaFeature.values()){
                intervals.put(mf, Integer.MIN_VALUE);
            }
            
            String line = null;
            boolean heading = true;
            String[] columnNames = null;
            while((line = br.readLine()) != null){
                if(line.isEmpty()){continue;}
                line = line.substring(line.indexOf(",") + 1);
                line = line.replaceAll(",,+.*", "");
                line = line.substring(0, line.lastIndexOf(","));
                if(heading){
                    heading = false;
                    columnNames = line.split(",");
                    continue;
                }
                
                String[] columnValues = line.split(",");
                for(int i=0; i<columnNames.length; i++){
                    String column = columnNames[i].trim();
                    double v = Double.parseDouble(columnValues[i]);
                    int value = (int) v;
                    
                    MetaFeature mf = MetaFeature.valueOf(column);
                    if(intervals.get(mf) < value ){
                        intervals.put(mf, value);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            LOG.error("Error: {}", e.getMessage());
            throw e;
        }
        
        return intervals;
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
