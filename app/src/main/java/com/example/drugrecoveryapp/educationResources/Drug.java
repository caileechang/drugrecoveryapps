package com.example.drugrecoveryapp.educationResources;

public class Drug {
    private String drugName;
    private String drugDescription;
    private boolean readMore;
    public Drug(String name, String drugDescription, boolean testing){
        drugName = name;
        this.drugDescription = drugDescription;
        this.readMore = testing;
    }

    public String getName(){
        return drugName;
    }
    public String getDrugDescription(){
        return drugDescription;
    }

    public boolean isReadMore(){
        return readMore;
    }

}
