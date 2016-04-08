package org.uclab.mm.kcl.edket.algoselector;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.uclab.mm.kcl.edket.algoselector.representation.SolutionDescription;
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
    
    public static final int TOP_K_RESULTS = 3;
    
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
        System.err.println("\nTop [" + TOP_K_RESULTS + "] Selected Cases\n");
        for(CBRCase c : selected){
            System.err.println(c);
        }
        
        System.out.println("\nAll retrieved Cases\n");
        for(RetrievalResult rr: eval) {
                SolutionDescription matchsimpleSolution = (SolutionDescription) rr.get_case().getSolution();
                double similarity = rr.getEval();
                String algo = matchsimpleSolution.getAlgorithm();
                System.out.println("algorithm: " + algo + ", similarity: " + similarity);
        }
        System.out.println("\n\nDetail view of Retrieved cases:\n");
        for(RetrievalResult retrievedCase: eval) {
           System.out.println(retrievedCase + "\n");
        }       
    }
}
