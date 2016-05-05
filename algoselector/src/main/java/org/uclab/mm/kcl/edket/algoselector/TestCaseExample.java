package org.uclab.mm.kcl.edket.algoselector;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jcolibri.cbrcore.CBRCase;

import org.uclab.mm.kcl.edket.algoselector.mfe.InputCaseBuilder;
import org.uclab.mm.kcl.edket.algoselector.mfe.MetaFeature;
import org.uclab.mm.kcl.edket.algoselector.mfe.MetaFeatureExtractor;

public class TestCaseExample {
    
    /**
     * A simple use case example for the automatic-algorithm-selection
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            
            /**** build inputCase file if not exists ****/
            (new InputCaseBuilder()).buildCasesIfNotExists();
            String resolvedCasesFile = "AlgorithmsSelectionCaseBase.csv";
            Map<MetaFeature, Integer> simIntervals = InputCaseBuilder.detectSimIntervals(resolvedCasesFile);
            
            /****instantiate AutomaticAlgorithmSelector****/
            AutomaticAlgorithmSelector autoAlgoSelector = new AutomaticAlgorithmSelector(new QueryManager(), new SimilarityManager(simIntervals));
            autoAlgoSelector.configure();
            
            /**** set options and extract meta features ****/ 
            Map<String, String> options = new HashMap<String, String>();
            options.put("dataset_dir", "");
            options.put("dataset_file", "F:/weka_in_out/input/dataset_61_iris.arff");
            options.put("mode", "single");
            options.put("output_dir", "output/");
     
            MetaFeatureExtractor mfe = new MetaFeatureExtractor( options );
            List<Map<String, Double>> xFeaturesList = mfe.extractMetaFeatures();
            
            /**** build queryCase from the extracted features ****/
            Map<MetaFeature, Object> queryCase = buildQueryCase(xFeaturesList.get(0));
           
            /********* select cases ***********/
            Collection<CBRCase> selectedCases = autoAlgoSelector.evaluateSimilarity(queryCase)
                            .retrieveTopKResults(3);
            
            /******* reuse *********/
            Collection<CBRCase> reuseCases = autoAlgoSelector.combineQueryAndCaseMethod(selectedCases);
            
            /******** revise *******/ 
            CBRCase bestCase = autoAlgoSelector.getBestCase(reuseCases);
            
            /********* retain ********/
            //. autoAlgoSelector.retainCase(bestCase);
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * builds queryCase from the extracted meta features.
     * Note: extracted features contains extra features that we don't need for our application, so here we are filtering only required features.
     * 
     * @param xFeatures
     * @return Map<MetaFeature, Object> queryCase
     */
    private static Map<MetaFeature, Object> buildQueryCase(Map<String, Double> xFeatures){
        
        Map<MetaFeature, Object> queryCase = new HashMap<MetaFeature, Object>();
        for(String key : xFeatures.keySet()){
            
            //. filter out only required metaFeatures
            try{ 
                MetaFeature columnName = MetaFeature.valueOf(key);
                Object columnValue = xFeatures.get(key);
                queryCase.put(columnName, columnValue);
            } catch (IllegalArgumentException e){
                //. we do not need this feature
            }
        }
        return queryCase;
    }

}
