package org.uclab.mm.kcl.edket.algoselector.mfe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openml.apiconnector.xml.DataQuality;
import org.openml.apiconnector.xml.DataQuality.Quality;
import org.openml.webapplication.fantail.dc.Characterizer;
import org.openml.webapplication.fantail.dc.StreamCharacterizer;
import org.openml.webapplication.fantail.dc.landmarking.GenericLandmarker;
import org.openml.webapplication.fantail.dc.statistical.AttributeCount;
import org.openml.webapplication.fantail.dc.statistical.AttributeEntropy;
import org.openml.webapplication.fantail.dc.statistical.AttributeType;
import org.openml.webapplication.fantail.dc.statistical.ClassAtt;
import org.openml.webapplication.fantail.dc.statistical.DefaultAccuracy;
import org.openml.webapplication.fantail.dc.statistical.IncompleteInstanceCount;
import org.openml.webapplication.fantail.dc.statistical.InstanceCount;
import org.openml.webapplication.fantail.dc.statistical.MissingValues;
import org.openml.webapplication.fantail.dc.statistical.NominalAttDistinctValues;
import org.openml.webapplication.fantail.dc.statistical.Statistical;
import org.openml.webapplication.fantail.dc.stream.ChangeDetectors;
//. import org.uclab.mm.kcl.edket.algoselector.ui.AlgorithmSelectionUI;

import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;

public class MetaFeatureExtractor implements Runnable {
    private static Logger LOG = LogManager.getLogger(MetaFeatureExtractor.class);
    private final Integer window_size;
    private List<Map<String, Double>> metaFeatures;
    
    private Characterizer[] batchCharacterizers = {
        new Statistical(), new AttributeCount(), new AttributeType(),
        new ClassAtt(), new DefaultAccuracy(),
        new IncompleteInstanceCount(), new InstanceCount(),
        new MissingValues(), new NominalAttDistinctValues(),
        new AttributeEntropy(), 

        new GenericLandmarker( "NaiveBayes", "weka.classifiers.bayes.NaiveBayes", 2, null ),
        new GenericLandmarker( "NBTree", "weka.classifiers.trees.NBTree", 2, null ),
        new GenericLandmarker( "DecisionStump", "weka.classifiers.trees.DecisionStump", 2, null ),
        new GenericLandmarker( "SimpleLogistic", "weka.classifiers.functions.SimpleLogistic", 2, null ),
        new GenericLandmarker( "JRip", "weka.classifiers.rules.JRip", 2, null )
    };
    
    private static StreamCharacterizer[] streamCharacterizers;
    private String exportCsvPath;
    private Map<String, String> options;
    
    public MetaFeatureExtractor( Map<String, String> appOptions ){
        this.options = appOptions;
        window_size = null;
        metaFeatures = new ArrayList<Map<String, Double>>();
    }
    
    private List<Quality> extractFeatures(String datasetPath, Integer interval_size) throws Exception {
        
        streamCharacterizers = new StreamCharacterizer[1]; 
        streamCharacterizers[0] = new ChangeDetectors( interval_size );
        System.out.println(datasetPath);
        DataSource source = new DataSource(datasetPath);
        Instances dataset = source.getDataSet();
         if (dataset.classIndex() == -1) {
            dataset.setClassIndex(dataset.numAttributes() - 1);
        }
        
        
        for( StreamCharacterizer sc : streamCharacterizers ) {
            sc.characterize( dataset );
        }
        
        List<Quality> qualities = new ArrayList<DataQuality.Quality>();
        
        qualities.addAll( datasetCharacteristics( dataset, null, null ) );
        for( StreamCharacterizer sc : streamCharacterizers ) {
            Map<String, Double> streamqualities = sc.global();
            qualities.addAll( hashMaptoList( streamqualities, null, null ) );
        }
        return qualities;
    }

    private List<Quality> datasetCharacteristics( Instances fulldata, Integer start, Integer interval_size ) throws Exception {
        List<Quality> result = new ArrayList<DataQuality.Quality>();
        Instances intervalData;
        
        // Be careful changing this!
        if( interval_size != null ) {
            intervalData = new Instances( fulldata, start, Math.min( interval_size, fulldata.numInstances() - start ) );
            intervalData = applyFilter( intervalData, new StringToNominal(), "-R first-last" );
            intervalData.setClassIndex( fulldata.classIndex() );
        } else {
            intervalData = fulldata;
            // todo: use StringToNominal filter? might be to expensive
        }
        
        for( Characterizer dc : batchCharacterizers ) {
            Map<String,Double> qualities = dc.characterize(intervalData);
            result.addAll( hashMaptoList( qualities, start, interval_size ) );
        }
        
        return result;
    }
    
    public static List<Quality> hashMaptoList( Map<String, Double> map, Integer start, Integer size ) {
        List<Quality> result = new ArrayList<DataQuality.Quality>();
        for( String quality : map.keySet() ) {
            Integer end = start != null ? start + size : null;
            result.add( new Quality( quality, map.get( quality ) + "", start, end ) );
        }
        return result;
    }
    
    private static Instances applyFilter( Instances dataset, Filter filter, String options ) throws Exception {
        ((OptionHandler) filter).setOptions( Utils.splitOptions( options ) );
        filter.setInputFormat(dataset);
        return Filter.useFilter(dataset, filter);
    }
    public void exportToCsv(List<Quality> qualities, String filePath, String fileName, boolean SINGLE_MODE, boolean ADD_HEADER){
        
        final String COMMA_DELIMITER = ",";
        final String NEW_LINE_SEPARATOR = "\n";
        
        StringBuilder FILE_HEADER = null;
        StringBuilder ROW = new StringBuilder(fileName+COMMA_DELIMITER);
        
        if(ADD_HEADER){
            FILE_HEADER = new StringBuilder("Dataset Name"+COMMA_DELIMITER);
        }
        
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(filePath, !ADD_HEADER);
            Map<String, Double> xFeatures = new HashMap<String, Double>(); 
            for(DataQuality.Quality quality : qualities){
                String qualityKey = quality.getName();
                String qualityValue = quality.getValue();
                //. if(SINGLE_MODE){
                    xFeatures.put(qualityKey, toDouble(qualityValue));
                    //AppUI.addTableRow(qualityKey, qualityValue);
                //. }
                ROW.append(qualityValue);
                ROW.append(COMMA_DELIMITER);
                if(ADD_HEADER){
                    FILE_HEADER.append(qualityKey);
                    FILE_HEADER.append(COMMA_DELIMITER);
                }
                
            }
            
            //. add row
            //. AlgorithmSelectionUI.addTableRow(xFeatures);
            metaFeatures.add(xFeatures);
            
                
            if(ADD_HEADER){
                String fileHeader = FILE_HEADER.toString();
                fileHeader = fileHeader.replaceAll(",$", "");
                fileWriter.append(fileHeader);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            String row = ROW.toString();
            row = row.replaceAll(",$", "");
            fileWriter.append(row);
            fileWriter.append(NEW_LINE_SEPARATOR);
            
        }catch(IOException ioex){
            
        }finally{
            try{
                if(fileWriter != null){
                    fileWriter.flush();
                    fileWriter.close();
                }
            }catch(IOException ioex){
                
            }
        }
    }
    @Override
    public void run() {
        
        try{
            boolean singleMode = options.get("mode").equals("single") ? true : false;
            
            this.exportCsvPath = options.get("outputDirectory")+"_"+System.currentTimeMillis()+".csv";
            File folder = null;
            File[] fileList = null;
            
            if(!singleMode){
                folder = new File(options.get("datasetPath"));
                fileList = folder.listFiles();
            }
            
            // additional batch landmarkers
            TreeMap<String, String[]> REPOptions = new TreeMap<String, String[]>();
            TreeMap<String, String[]> J48Options = new TreeMap<String, String[]>();
            TreeMap<String, String[]> RandomTreeOptions = new TreeMap<String, String[]>();
            TreeMap<String, String[]> kNNOptions = new TreeMap<String, String[]>();
            TreeMap<String, String[]> smoPolyOptions = new TreeMap<String, String[]>();
            String zeros = "0";
            for( int i = 1; i <= 3; ++i ) {
                zeros += "0";
                String[] repOption = { "-L", "" + i };
                REPOptions.put( "Depth" + i, repOption );
    
                String[] j48Option = { "-C", "." + zeros + "1" };
                J48Options.put( "." + zeros + "1.", j48Option );
                
                String[] randomtreeOption = { "-depth", "" + i };
                RandomTreeOptions.put( "Depth" + i, randomtreeOption );
                        
                String[] kNNOption = { "-K", "" + i };
                kNNOptions.put( "_" + i + "N", kNNOption );
                
                String[] smoPolyOption = { "-M", "-K", "weka.classifiers.functions.supportVector.PolyKernel -E "+i+".0" };
                smoPolyOptions.put( "e" + i, smoPolyOption );
            }
            
            batchCharacterizers = ArrayUtils.add( batchCharacterizers, new GenericLandmarker( "REPTree", "weka.classifiers.trees.REPTree", 2, REPOptions ) );
            batchCharacterizers = ArrayUtils.add( batchCharacterizers, new GenericLandmarker( "J48", "weka.classifiers.trees.J48", 2, J48Options ) );
            batchCharacterizers = ArrayUtils.add( batchCharacterizers, new GenericLandmarker( "RandomTree", "weka.classifiers.trees.RandomTree", 2, RandomTreeOptions ) );
            batchCharacterizers = ArrayUtils.add( batchCharacterizers, new GenericLandmarker( "kNN", "weka.classifiers.lazy.IBk", 2, kNNOptions ) );
            batchCharacterizers = ArrayUtils.add( batchCharacterizers, new GenericLandmarker( "SVM", "weka.classifiers.functions.SMO", 2, smoPolyOptions ) );
            
            if(singleMode){
                LOG.debug("Extracting qualities from: "+options.get("datasetFile"));
                List<Quality> exquality = extractFeatures( options.get("datasetFile"), window_size );
                LOG.debug("Writing qualities to table");
                File temp = new File(options.get("datasetFile"));
                exportToCsv(exquality, exportCsvPath,temp.getName(), true, true);
                LOG.debug("Done. Result is stored in: "+exportCsvPath);
            }else{
                boolean IS_FIRST_FILE = true;
                LOG.debug("Looping through directory");
                for( File dataset : fileList  ) {
                    if (dataset.isFile() && dataset.getName().endsWith(".arff")){
                        LOG.debug("Extracting Qualities From: " +dataset.getName());
                        List<Quality> exquality = extractFeatures( dataset.getAbsolutePath(), window_size );
                        LOG.debug("Writing qualities to csv file");
                        exportToCsv(exquality, exportCsvPath, dataset.getName(), false, IS_FIRST_FILE);
                        IS_FIRST_FILE = false;
                        LOG.debug("Done.");
                    }
                }
                
                LOG.debug("Done. Result is stored in: "+exportCsvPath);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
    
    public List<Map<String, Double>> getMetaFeatures(){
        return this.metaFeatures;
    }
    
    public double toDouble(String v) {
        if(v == null || v.isEmpty()){ return -1; }
        return Double.parseDouble(v);
    }
    public int toInteger(String v){
        if(v == null || v.isEmpty()){ return -1; }
        double d = Double.parseDouble(v);
        return (int) d;
    }

}
