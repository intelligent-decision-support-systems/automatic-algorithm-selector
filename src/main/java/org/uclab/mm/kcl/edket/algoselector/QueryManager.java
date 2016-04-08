package org.uclab.mm.kcl.edket.algoselector;

import org.uclab.mm.kcl.edket.algoselector.representation.CaseDescription;
import org.uclab.mm.kcl.edket.algoselector.ui.AlgoSelectorUI;

import jcolibri.cbrcore.CBRQuery;

public class QueryManager {

    public CBRQuery getQuery(AlgoSelectorUI app){
        
        CaseDescription caseDescription = new CaseDescription();
        
        caseDescription.setAttributes(toInteger(app.getAttrV().getText()));
        caseDescription.setNominalAttributes(toInteger(app.getNominalAttrV().getText()));
        caseDescription.setNumericAttributes(toInteger(app.getNumericAttrV().getText()));
        caseDescription.setBinaryAttributes(toInteger(app.getBinaryAttrV().getText()));
        caseDescription.setClasses(toInteger(app.getClassesAttrV().getText()));
        caseDescription.setInstanceCount(toInteger(app.getInstanceCountAttrV().getText()));
        caseDescription.setMissing(toInteger(app.getMissingAttrV().getText()));
        
        CBRQuery cbrQuery = new CBRQuery();
        cbrQuery.setDescription(caseDescription);
        return cbrQuery;
    }
    
    private int toInteger(String v){
        return Integer.parseInt(v);
    }
}
