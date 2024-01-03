package com.example.drugrecoveryapp.educationResources;

public class Drug {
    private String drugName;
    private String drugDescription;
    private boolean bookmark;
    private boolean readMore;
    private String drugLongDescription;
    private String drugEffect;
    private String drugImmediateEffect;
    private String drugLongTermEffect;
    private String drugPersonalStory;
    private String documentID;

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

    public void setDrugLongDescription(String drugLongDescription) {
        this.drugLongDescription = drugLongDescription;
    }

    public String getDrugLongDescription() {
        return drugLongDescription;
    }

    public void setDrugEffect(String drugEffect) {
        this.drugEffect = drugEffect;
    }

    public String getDrugEffect() {
        return drugEffect;
    }

    public void setDrugLongTermEffect(String drugLongTermEffect) {
        this.drugLongTermEffect = drugLongTermEffect;
    }

    public String getDrugLongTermEffect() {
        return drugLongTermEffect;
    }

    public void setDrugImmediateEffect(String drugImmediateEffect) {
        this.drugImmediateEffect = drugImmediateEffect;
    }

    public String getDrugImmediateEffect() {
        return drugImmediateEffect;
    }

    public void setDrugPersonalStory(String drugPersonalStory) {
        this.drugPersonalStory = drugPersonalStory;
    }

    public String getDrugPersonalStory() {
        return drugPersonalStory;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public boolean getBookmark() {
        return bookmark;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDocumentID() {
        return documentID;
    }
}
