package org.uclab.mm.kcl.edket.algoselector.representation;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class SolutionDescription implements CaseComponent {

    private java.lang.String Algorithm;

    public java.lang.String getAlgorithm() {
        return Algorithm;
    }

    public void setAlgorithm(java.lang.String Algorithm9) {
        this.Algorithm = Algorithm9;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("Algorithm", this.getClass());
    }

    public String toString() {
        return "[" + Algorithm + "]";
    }

}
