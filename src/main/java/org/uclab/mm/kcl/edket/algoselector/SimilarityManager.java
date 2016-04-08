package org.uclab.mm.kcl.edket.algoselector;

import org.uclab.mm.kcl.edket.algoselector.representation.CaseDescription;
import org.uclab.mm.kcl.edket.algoselector.ui.AlgoSelectorUI;

import jcolibri.cbrcore.Attribute;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class SimilarityManager {
    
    Double weight = 1.00;
    
    /**
     * gets the selected similarity function of all attributes from the application and then builds and returns SimilarityConfiguration
     * 
     * @param app
     * @return NNConfig
     */
    public NNConfig getSimilarityConfig(AlgoSelectorUI app) {
        
        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new jcolibri.method.retrieve.NNretrieval.similarity.global.Average());
        
        Attribute attributes = new Attribute("Attributes", CaseDescription.class);
        simConfig.addMapping(attributes, getLocalSimilarityFunction(app.getAttrSim().getSelectedItem()));
        simConfig.setWeight(attributes, weight);
        
        Attribute nominalAttributes = new Attribute("NominalAttributes", CaseDescription.class);
        simConfig.addMapping(nominalAttributes, getLocalSimilarityFunction(app.getNominalSim().getSelectedItem()));
        simConfig.setWeight(nominalAttributes, weight);
        
        Attribute numericAttributes = new Attribute("NumericAttributes", CaseDescription.class);
        simConfig.addMapping(numericAttributes, getLocalSimilarityFunction(app.getNumericSim().getSelectedItem()));
        simConfig.setWeight(numericAttributes, weight);
        
        Attribute binaryAttributes = new Attribute("BinaryAttributes", CaseDescription.class);
        simConfig.addMapping(binaryAttributes, getLocalSimilarityFunction(app.getBinarySim().getSelectedItem()));
        simConfig.setWeight(binaryAttributes, weight);
        
        Attribute classAttributes = new Attribute("Classes", CaseDescription.class);
        simConfig.addMapping(classAttributes, getLocalSimilarityFunction(app.getClassesSim().getSelectedItem()));
        simConfig.setWeight(classAttributes, weight);
        
        Attribute instanceCountAttributes = new Attribute("InstanceCount", CaseDescription.class);
        simConfig.addMapping(instanceCountAttributes, getLocalSimilarityFunction(app.getInstanceCountSim().getSelectedItem()));
        simConfig.setWeight(instanceCountAttributes, weight);
        
        Attribute missingAttributes = new Attribute("Missing", CaseDescription.class);
        simConfig.addMapping(missingAttributes, getLocalSimilarityFunction(app.getMissingSim().getSelectedItem()));
        simConfig.setWeight(missingAttributes, weight);
        
        return simConfig;
    }
    
    private LocalSimilarityFunction getLocalSimilarityFunction(Object sim){
        return getLocalSimilarityFunction((Class<?>) sim);
    }
    private LocalSimilarityFunction getLocalSimilarityFunction(Class<?> sim){
        LocalSimilarityFunction similarityFunction = null;
        try {
            similarityFunction = (LocalSimilarityFunction) sim.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return similarityFunction;
    }
}
