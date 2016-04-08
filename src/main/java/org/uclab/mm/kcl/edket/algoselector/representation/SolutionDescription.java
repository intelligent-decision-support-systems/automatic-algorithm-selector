package org.uclab.mm.kcl.edket.algoselector.representation;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class SolutionDescription implements CaseComponent {

    /* Generated Class. Please Do Not Modify... */

    private java.lang.Integer CaseId;

    public java.lang.Integer getCaseId() {
        return CaseId;
    }

    public void setCaseId(java.lang.Integer CaseId8) {
        this.CaseId = CaseId8;
    }

    private java.lang.String Algorithm;

    public java.lang.String getAlgorithm() {
        return Algorithm;
    }

    public void setAlgorithm(java.lang.String Algorithm9) {
        this.Algorithm = Algorithm9;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("CaseId", this.getClass());
    }

    public String toString() {
        return "[" + CaseId + " , " + Algorithm + "]";
    }

}
