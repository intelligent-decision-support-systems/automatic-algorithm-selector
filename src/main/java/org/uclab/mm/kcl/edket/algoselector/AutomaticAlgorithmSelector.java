package org.uclab.mm.kcl.edket.algoselector;

import java.util.Collection;
import java.util.HashMap;

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
import org.uclab.mm.kcl.edket.algoselector.representation.CaseDescription;
import org.uclab.mm.kcl.edket.algoselector.representation.SolutionDescription;
import org.uclab.mm.kcl.edket.algoselector.ui.AlgoSelectorUI;

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
        doPerformCBRSteps(cbrQuery, simConfig);
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
    
    private void doPerformCBRSteps(CBRQuery query, NNConfig simConfig) throws ExecutionException{
        
        /********* Execute NN ************/
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(casebase.getCases(), query, simConfig);
        
        /********* Select cases **********/
        Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, TOP_K_RESULTS);
        System.err.println("\nRETRIEVE:  Similar cases\n");
        for(RetrievalResult rr : eval){
            if(selectedcases.contains(rr.get_case())){
                CaseDescription retrievedCase = (CaseDescription) rr.get_case().getDescription();
                
                String description = retrievedCase.toString();
                System.out.print(rr.get_case());
                System.out.println(" sim: " + rr.getEval());
            }
        }
        System.out.println();
        /********* Reuse **********/
        
        //Combine query description with cases solutions, obtaining a list of new cases.
        Collection<CBRCase> newcases = CombineQueryAndCasesMethod.combine(query, selectedcases);
        System.err.println("REUSE: Map the solution from the previous case to the target problem, Combined cases\n");
        for(jcolibri.cbrcore.CBRCase c: newcases){
            System.out.println(c);
            CaseDescription cd = (CaseDescription) c.getDescription();
            System.out.println(cd);
        }
        /********* Revise **********/
        // Lets store only the best case
        CBRCase bestCase = newcases.iterator().next();
        Integer newCaseId = casebase.getCases().size() + 1;
        
        // Define new ids for the compound attributes
        HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute, Object>();
        componentsKeys.put(new Attribute("CaseID", CaseDescription.class), newCaseId);  
        jcolibri.method.revise.DefineNewIdsMethod.defineNewIdsMethod(bestCase, componentsKeys);
               
        System.err.println("\nREVISE: Cases with new Id");
        System.err.println("do you want to RETAIN this solution?");
        System.out.println(bestCase);
        
        /********* Retain **********/
        System.err.println("RETAIN: Save Case for future use?");
        // Uncomment next line to store cases into persistence
        //jcolibri.method.retain.StoreCasesMethod.storeCase(casebase, bestCase);
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
