package com.pagonxt.onetradefinance.integrations.model;

public class OfficeMiddleOffice {
    private String office;
    private String middleOffice;
    public OfficeMiddleOffice(){}
    public OfficeMiddleOffice(String office, String middleOffice){
        this.middleOffice = middleOffice;
        this.office = office;
    }
    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getMiddleOffice() {
        return middleOffice;
    }

    public void setMiddleOffice(String middleOffice) {
        this.middleOffice = middleOffice;
    }

}
