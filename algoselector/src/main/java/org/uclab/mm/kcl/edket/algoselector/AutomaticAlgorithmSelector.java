package org.uclab.mm.kcl.edket.algoselector;

import java.awt.Font;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

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
import org.uclab.mm.kcl.edket.algoselector.ui.AlgoSelectionResultsModel;

public class AutomaticAlgorithmSelector implements StandardCBRApplication {
    private static Logger LOG = LogManager.getLogger(AutomaticAlgorithmSelector.class);

    Connector connector;
    CBRCaseBase casebase;
    
    QueryManager queryManager;
    SimilarityManager similarityManager;
    
    public static final int TOP_K_RESULTS = 3;
    
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
    
    public void buildRecommendation(Map<MetaFeature, Object> queryCase) throws ExecutionException {
        CBRQuery query = queryManager.getQuery(queryCase);
        cycle(query);
    }
    
    private void doPerformCBRSteps(CBRQuery query, NNConfig simConfig) throws ExecutionException{
        AlgoSelectionResultsModel resultModel = new AlgoSelectionResultsModel();
        Thread modelThread = new Thread(resultModel);
        modelThread.setDaemon(true);
        modelThread.start(); 
        
        /********* Execute NN ************/
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(casebase.getCases(), query, simConfig);
        
        /********* Select cases **********/
        Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, TOP_K_RESULTS);
        LOG.debug("RETRIEVE:  Similar cases");
        for(RetrievalResult rr : eval){
            if(selectedcases.contains(rr.get_case())){
                CaseDescription retrievedCase = (CaseDescription) rr.get_case().getDescription();
                
                String description = retrievedCase.toString();
                String retriveRow = rr.get_case()+"";
                retriveRow += " :: similarity:  " + rr.getEval(); 
                LOG.debug(retriveRow);
                
                JLabel label = new JLabel(retriveRow);
                label.setFont(new Font("Verdana", Font.PLAIN, 12));
                resultModel.getRetrievePanel().add(label);
            }
        }
        
        /********* Reuse **********/
        //Combine query description with cases solutions, obtaining a list of new cases.
        Collection<CBRCase> newcases = CombineQueryAndCasesMethod.combine(query, selectedcases);
        LOG.debug("REUSE: Map the solution from the previous case to the target problem, Combined cases");
        for(jcolibri.cbrcore.CBRCase c: newcases){
            LOG.debug(c);
            CaseDescription cd = (CaseDescription) c.getDescription();
            LOG.debug(cd);
            
            JLabel label = new JLabel(c+"");
            label.setFont(new Font("Verdana", Font.PLAIN, 12));
            resultModel.getReusePanel().add(label);
        }
        
        
        /********* Revise **********/
        // Lets store only the best case
        CBRCase bestCase = newcases.iterator().next();
        Integer newCaseId = casebase.getCases().size() + 1;
        
        // Define new ids for the compound attributes
        HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute, Object>();
        componentsKeys.put(new Attribute("CaseID", CaseDescription.class), newCaseId);  
        jcolibri.method.revise.DefineNewIdsMethod.defineNewIdsMethod(bestCase, componentsKeys);
               
        LOG.debug("REVISE: Cases with new Id");
        LOG.debug("do you want to RETAIN this solution?");
        LOG.debug(bestCase);
        
        JLabel label = new JLabel(bestCase+"");
        label.setFont(new Font("Verdana", Font.PLAIN, 12));
        resultModel.getRevisePanel().add(label);
        
        /********* Retain **********/
        LOG.debug("RETAIN: Save Case for future use?");
        // Uncomment next line to store cases into persistence
        //. jcolibri.method.retain.StoreCasesMethod.storeCase(casebase, bestCase);
        
        LOG.debug("Component size: " + resultModel.getRetrievePanel().getComponentCount());
    }
}
