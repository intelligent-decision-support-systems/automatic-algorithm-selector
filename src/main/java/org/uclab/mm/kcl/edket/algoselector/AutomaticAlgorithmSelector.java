package org.uclab.mm.kcl.edket.algoselector;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.uclab.mm.kcl.edket.algoselector.ui.AlgoSelectorUI;

import jcolibri.cbraplications.StandardCBRApplication;
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

public class AutomaticAlgorithmSelector implements StandardCBRApplication {
    private static Logger LOG = LogManager.getLogger(AutomaticAlgorithmSelector.class);

    Connector connector;
    CBRCaseBase casebase;
    
    QueryManager queryManager;
    SimilarityManager similarityManager;
    
    public static final int TOP_K_RESULTS = 2;
    
    public AutomaticAlgorithmSelector(){
        this(null, null);
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
        
        for(CBRCase c: casebase.getCases()){
            System.out.println(c);
        }
        System.err.println("Ready!");
    }
    
    private void configureConnector() throws InitializingException{
        connector = new jcolibri.connector.PlainTextConnector();
        connector.initFromXMLfile(jcolibri.util.FileIO.findFile(getClass().getClassLoader().getResource("plainTextConnectorConfig.xml").getPath()));
    }
    
    private void configureCaseBase() throws InitializingException{
        casebase = new jcolibri.casebase.LinealCaseBase();
    }
    
    @Override
    public void cycle(CBRQuery cbrQuery) throws ExecutionException {
        NNConfig simConfig = similarityManager.getSimilarityConfig(AlgoSelectorUI.getInstance());
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(casebase.getCases(), cbrQuery, simConfig);
        Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, TOP_K_RESULTS);
        
        showSelectedCases(eval, selectedcases);
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
    
    public void setQueryManager(QueryManager queryManager){
        this.queryManager = queryManager;
    }
    public void setSimilarityManager(SimilarityManager similarityManager){
        this.similarityManager = similarityManager;   
    }
    
    public void buildRecommendation() throws ExecutionException {
        CBRQuery query = queryManager.getQuery(AlgoSelectorUI.getInstance());
        cycle(query);
    }
    private void showSelectedCases(Collection<RetrievalResult> eval, Collection<CBRCase> selected) {
        // TODO print selected case
        LOG.info("Un Implemented Method[showSelectedCases]");
    }
}
