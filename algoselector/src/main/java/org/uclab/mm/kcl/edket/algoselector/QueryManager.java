package org.uclab.mm.kcl.edket.algoselector;

import java.util.Map;

import jcolibri.cbrcore.CBRQuery;

import org.uclab.mm.kcl.edket.algoselector.mfe.MetaFeature;
import org.uclab.mm.kcl.edket.algoselector.representation.CaseDescription;

public class QueryManager {
    
     public CBRQuery getQuery(Map<MetaFeature, Object> queryCase){
         CaseDescription caseDescription = new CaseDescription();
         for(MetaFeature key : queryCase.keySet()){
             double value = ((Number)queryCase.get(key)).doubleValue();
             setMetaDescription(key, value, caseDescription);
         }         
        
         CBRQuery cbrQuery = new CBRQuery();
         cbrQuery.setDescription(caseDescription);
         return cbrQuery;
    }
     
    private void setMetaDescription(MetaFeature columnName, double columnValue, CaseDescription caseDescription){
          
        switch(columnName){
          case ClassCount:
              caseDescription.setClassCount((int) columnValue);
              break;
          case ClassEntropy:
              caseDescription.setClassEntropy(columnValue);
              break;
          case DefaultAccuracy:
              caseDescription.setDefaultAccuracy(columnValue);
              break;
          case Dimensionality:
              caseDescription.setDimensionality(columnValue);
              break;
          case EquivalentNumberOfAtts:
              caseDescription.setEquivalentNumberOfAtts(columnValue);
              break;
          case IncompleteInstanceCount:
              caseDescription.setIncompleteInstanceCount((int) columnValue);
              break;
          case InstanceCount:
              caseDescription.setInstanceCount((int) columnValue);
              break;
          case MaxNominalAttDistinctValues:
              caseDescription.setMaxNominalAttDistinctValues((int) columnValue);
              break;
          case MeanAttributeEntropy:
              caseDescription.setMeanAttributeEntropy(columnValue);
              break;
          case MeanKurtosisOfNumericAtts:
              caseDescription.setMeanKurtosisOfNumericAtts(columnValue);
              break;
          case MeanMeansOfNumericAtts:
              caseDescription.setMeanMeansOfNumericAtts(columnValue);
              break;
          case MeanMutualInformation:
              caseDescription.setMeanMutualInformation(columnValue);
              break;
          case MeanNominalAttDistinctValues:
              caseDescription.setMeanNominalAttDistinctValues(columnValue);
              break;
          case MeanSkewnessOfNumericAtts:
              caseDescription.setMeanSkewnessOfNumericAtts(columnValue);
              break;
          case MeanStdDevOfNumericAtts:
              caseDescription.setMeanStdDevOfNumericAtts(columnValue);
              break;
          case MinNominalAttDistinctValues:
              caseDescription.setMinNominalAttDistinctValues((int) columnValue);
              break;
          case NegativePercentage:
              caseDescription.setNegativePercentage(columnValue);
              break;
          case NoiseToSignalRatio:
              caseDescription.setNoiseToSignalRatio(columnValue);
              break;
          case NumAttributes:
              caseDescription.setNumAttributes((int) columnValue);
              break;
          case NumBinaryAtts:
              caseDescription.setNumBinaryAtts((int) columnValue);
              break;
          case NumMissingValues:
              caseDescription.setNumMissingValues((int) columnValue);
              break;
          case NumNominalAtts:
              caseDescription.setNumNominalAtts((int) columnValue);
              break;
          case NumNumericAtts:
              caseDescription.setNumNumericAtts((int) columnValue);
              break;
          case PercentageOfBinaryAtts:
              caseDescription.setPercentageOfBinaryAtts(columnValue);
              break;
          case PercentageOfMissingValues:
              caseDescription.setPercentageOfMissingValues(columnValue);
              break;
          case PercentageOfNominalAtts:
              caseDescription.setPercentageOfNominalAtts(columnValue);
              break;
          case PercentageOfNumericAtts:
              caseDescription.setPercentageOfNumericAtts(columnValue);
              break;
          case PositivePercentage:
              caseDescription.setPositivePercentage(columnValue);
              break;
          case StdvNominalAttDistinctValues:
              caseDescription.setStdvNominalAttDistinctValues(columnValue);
              break;
          default:
              break;       
        }
    }
}
