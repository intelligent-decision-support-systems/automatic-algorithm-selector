package org.uclab.mm.kcl.edket.algoselector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;
import jcolibri.exception.InitializingException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.method.reuse.CombineQueryAndCasesMethod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.uclab.mm.kcl.edket.algoselector.mfe.MetaFeature;
import org.uclab.mm.kcl.edket.algoselector.representation.CaseDescription;

public class AutomaticAlgorithmSelector implements StandardCBRApplication {
    private static Logger LOG = LogManager.getLogger(AutomaticAlgorithmSelector.class);

    private Connector connector;
    private CBRCaseBase casebase;
    
    private QueryManager queryManager;
    private SimilarityManager similarityManager;
    
    public static int TOP_K_RESULTS = 3;
    
    private Collection<RetrievalResult> evaluation;
    private CBRQuery query;
    
    public AutomaticAlgorithmSelector(){
    }
    public AutomaticAlgorithmSelector(QueryManager queryManager, SimilarityManager similarityManager){
        this.queryManager = queryManager;
        this.similarityManager = similarityManager;
    }
    
    @Override
    public void configure() throws ExecutionException {
        configureConnector();
        configureCaseBase();
        preCycle();
        LOG.debug("Loaded Cases");
        for(CBRCase c: casebase.getCases()){
            LOG.debug(c);
        }
        LOG.info("Ready!");
    }
    
    private void configureConnector() throws InitializingException{
        connector = new jcolibri.connector.PlainTextConnector();
        connector.initFromXMLfile(jcolibri.util.FileIO.findFile("/plainTextConnectorConfig.xml"));
    }
    
    private void configureCaseBase() throws InitializingException{
        casebase = new jcolibri.casebase.LinealCaseBase();
    }
    
    @Override
    public void cycle(CBRQuery cbrQuery) throws ExecutionException {
        NNConfig simConfig = similarityManager.getSimilarityConfig();
        evaluation = NNScoringMethod.evaluateSimilarity(casebase.getCases(), cbrQuery, simConfig);
    }

    @Override
    public void postCycle() throws ExecutionException {
        connector.close();
    }

    @Override
    public CBRCaseBase preCycle() throws ExecutionException {
        casebase.init(connector);
        return casebase;
    }
    
    /**
     * setQueryManager
     * 
     * @param queryManager
     */
    public void setQueryManager(QueryManager queryManager){
        this.queryManager = queryManager;
    }
    
    /**
     * setSimilarityManager
     * 
     * @param similarityManager
     */
    public void setSimilarityManager(SimilarityManager similarityManager){
        this.similarityManager = similarityManager;   
    }
    
    /**
     * evaluates similarity for the queryCase
     * 
     * @param queryCase
     * @return AutomaticAlgorithmSelector
     * @throws ExecutionException
     */
    public AutomaticAlgorithmSelector evaluateSimilarity(Map<MetaFeature, Object> queryCase) throws ExecutionException{
        query = queryManager.getQuery(queryCase);
        cycle(query);
        return this;
    }
    
    /**
     * returns top K most similar cases from resolved cases
     * 
     * @param k
     * @return Collection<CBRCase>
     */
    public Collection<CBRCase> retrieveTopKResults(int k){
        Collection<CBRCase> selectedcases = SelectCases.selectTopK(evaluation, k);
        LOG.debug("RETRIEVE:  Similar cases");
        for(RetrievalResult rr : evaluation){
            if(selectedcases.contains(rr.get_case())){
                String retriveRow = rr.get_case() + ", similarity ::  " + rr.getEval();
                LOG.debug(retriveRow);
            }
        }
        
        return selectedcases;
    }
    
    /**
     * Combine query description with cases solutions, obtaining a list of new cases.
     * 
     * @param query
     * @param selectedcases
     * @return Collection<CBRCase>
     */
    public Collection<CBRCase> combineQueryAndCaseMethod(Collection<CBRCase> selectedcases){
        Collection<CBRCase> newCases = CombineQueryAndCasesMethod.combine(query, selectedcases);
        
        LOG.debug("REUSE:  Similar cases");
        for(jcolibri.cbrcore.CBRCase c: newCases){
            LOG.debug(c);
            CaseDescription cd = (CaseDescription) c.getDescription();
            LOG.debug(cd);
        }
        return newCases;
    }
    
    /**
     * returns the best case from the retrieved cases
     * 
     * @param newcases
     * @return CBRCase
     * @throws ExecutionException
     */
    public CBRCase getBestCase(Collection<CBRCase> newcases) throws ExecutionException{
   
        CBRCase bestCase = newcases.iterator().next();
        Integer newCaseId = casebase.getCases().size() + 1;
        
        // Define new ids for the compound attributes
        HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute, Object>();
        componentsKeys.put(new Attribute("CaseID", CaseDescription.class), newCaseId);  
        jcolibri.method.revise.DefineNewIdsMethod.defineNewIdsMethod(bestCase, componentsKeys);
        
        LOG.debug("REVISE:  best case with new ID");
        LOG.debug(bestCase);
        
        return bestCase;
    }
    
    /**
     * Lets store only the best case
     * 
     * @param bestCase
     */
    public void retainCase(CBRCase bestCase){
        LOG.debug("RETAIN:  case for future use");
        jcolibri.method.retain.StoreCasesMethod.storeCase(casebase, bestCase);
    }
    
    /**
     * returns instance of CBRQuery
     * 
     * @return CBRQuery
     */
    public CBRQuery getQuery(){
        return this.query;
    }
    
    /**
     * returns collection of retrieved results.
     * 
     * @return Collection<RetrievalResult> retrievalResult
     */
    public Collection<RetrievalResult> getEvaluationList(){
        return evaluation;
    }
    /**
     * sets number of top similar results to be retrieve, during match 
     * @param int k
     */
    public static void setTopKResults(int k){
        TOP_K_RESULTS = k;
    }
}
