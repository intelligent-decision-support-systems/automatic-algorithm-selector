package org.uclab.mm.kcl.edket.algoselector.representation;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class CaseDescription implements CaseComponent {

    /* Generated Class. Please Do Not Modify... */

    private java.lang.Integer CaseID;

    public java.lang.Integer getCaseID() {
        return CaseID;
    }

    public void setCaseID(java.lang.Integer CaseID0) {
        this.CaseID = CaseID0;
    }

    private java.lang.Integer Attributes;

    public java.lang.Integer getAttributes() {
        return Attributes;
    }

    public void setAttributes(java.lang.Integer Attributes1) {
        this.Attributes = Attributes1;
    }

    private java.lang.Integer NominalAttributes;

    public java.lang.Integer getNominalAttributes() {
        return NominalAttributes;
    }

    public void setNominalAttributes(java.lang.Integer NominalAttributes2) {
        this.NominalAttributes = NominalAttributes2;
    }

    private java.lang.Integer NumericAttributes;

    public java.lang.Integer getNumericAttributes() {
        return NumericAttributes;
    }

    public void setNumericAttributes(java.lang.Integer NumericAttributes3) {
        this.NumericAttributes = NumericAttributes3;
    }

    private java.lang.Integer BinaryAttributes;

    public java.lang.Integer getBinaryAttributes() {
        return BinaryAttributes;
    }

    public void setBinaryAttributes(java.lang.Integer BinaryAttributes4) {
        this.BinaryAttributes = BinaryAttributes4;
    }

    private java.lang.Integer Classes;

    public java.lang.Integer getClasses() {
        return Classes;
    }

    public void setClasses(java.lang.Integer Classes5) {
        this.Classes = Classes5;
    }

    private java.lang.Integer InstanceCount;

    public java.lang.Integer getInstanceCount() {
        return InstanceCount;
    }

    public void setInstanceCount(java.lang.Integer InstanceCount6) {
        this.InstanceCount = InstanceCount6;
    }

    private java.lang.Integer Missing;

    public java.lang.Integer getMissing() {
        return Missing;
    }

    public void setMissing(java.lang.Integer Missing7) {
        this.Missing = Missing7;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("CaseID", this.getClass());
    }

    public String toString() {
        return "[" + Attributes + ", " + NominalAttributes + ", " + NumericAttributes + ", " + BinaryAttributes + ", "
                + Classes + ", " + InstanceCount + ", " + Missing + "]";

    }

}
