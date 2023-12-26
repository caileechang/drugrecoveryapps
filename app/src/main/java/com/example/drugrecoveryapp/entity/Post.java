package com.example.drugrecoveryapp.entity;

public class Post {

    private String postImage; //store uri.toString(), use this string to retrieve the img, via glide | ref:https://bumptech.github.io/glide/
    private String postCategory;
    private String postBy; //store userId
    private String postUserName; //store String UserID/ UserName
    private String postTitle; // store post title
    private String postDescription; //store description
    private long postedAt; //store post time : System.currentTimeMillis()
    private String postId;
    private int postLike;
    private int commentCount;


    public Post(String postID, String postImage, String postCategory, String postBy, String postUserName, String postTitle, String postDescription, long postedAt) {
        this.postId = postID;
        this.postImage = postImage;
        this.postCategory = postCategory;
        this.postBy = postBy;
        this.postUserName = postUserName;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postedAt = postedAt;
    }

    public Post() {
    }

    public String getPostUserName() {
        return postUserName;
    }

    public void setPostUserName(String postUserName) {
        this.postUserName = postUserName;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getPostBy() {
        return postBy;
    }

    public void setPostBy(String postBy) {
        this.postBy = postBy;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

}
