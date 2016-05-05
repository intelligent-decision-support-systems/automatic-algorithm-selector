package org.uclab.mm.kcl.edket.algoselector;

import java.util.Map;

import jcolibri.cbrcore.Attribute;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

import org.uclab.mm.kcl.edket.algoselector.mfe.MetaFeature;
import org.uclab.mm.kcl.edket.algoselector.representation.CaseDescription;

public class SimilarityManager {
    
    Double weight = 1.00;
    private Map<MetaFeature, Integer> intervals;
    
    public SimilarityManager(Map<MetaFeature, Integer> intervals){
        this.intervals = intervals;
    }
    /**
     * builds and returns similarity configuration for attributes.
     * 
     * @return SimilarityConfig : NNConfig
     */
    public NNConfig getSimilarityConfig() {
        
        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new jcolibri.method.retrieve.NNretrieval.similarity.global.Average());

        for(MetaFeature mf : MetaFeature.values()){
            Attribute metaAttribute = new Attribute(mf.name(), CaseDescription.class);
            int interval = intervals.get(mf);
            if(interval < 1){interval = 1;}
            simConfig.addMapping(metaAttribute, new jcolibri.method.retrieve.NNretrieval.similarity.local.Interval(interval));
            simConfig.setWeight(metaAttribute, weight);
        }
             
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
    
    public void setIntervals(Map<MetaFeature, Integer> intervals){
        this.intervals = intervals;
    }
    
    public Map<MetaFeature, Integer> getIntervals(){
        return this.intervals;
    }
}
