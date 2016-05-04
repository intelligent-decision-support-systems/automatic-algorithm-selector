package org.openml.webapplication.fantail.dc;

import java.util.Map;

import org.openml.webapplication.fantail.dc.landmarking.SimpleLandmarkers;
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

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class TestDC {
	
	public static void datasetCharacteristics(Instances data) {
		
        Characterizer dc = new Statistical();
        Map<String, Double> dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new AttributeCount();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");
        
        
        dc = new AttributeType();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new ClassAtt();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new DefaultAccuracy();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new IncompleteInstanceCount();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new InstanceCount();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new MissingValues();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new NominalAttDistinctValues();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new AttributeEntropy();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new SimpleLandmarkers();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        /*dc = new J48BasedLandmarker();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new REPTreeBasedLandmarker();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");

        dc = new RandomTreeBasedLandmarker();
        dcValues = dc.characterize(data);
        for(String key : dcValues.keySet()){
        	System.out.println(key + " : " + dcValues.get(key));
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");
    	*/
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String datasetPath = "dataset_61_iris.arff";
		try{
	        DataSource source = new DataSource(datasetPath);
	        Instances data = source.getDataSet();
	        if (data.classIndex() == -1) {
	            data.setClassIndex(data.numAttributes() - 1);
	        }
	        System.err.println(datasetPath);
	        datasetCharacteristics(data);
		}catch(Exception ex){
			System.out.println("Excepiotn...");
			ex.printStackTrace();
		}
	}

}
