package com.example.drugrecoveryapp.entity;

public class Category {

    private String category;
    private boolean hasImage;

    public Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }
    @Override
    public String toString() {
        return this.category;
    }

}
