package com.bc.secretnoteandtodo.database.model;

public class Note {
    public static final String TABLE_NAME = "Note";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_CONTENT = "Content";

    private int id;
    private String Title;
    private String Content;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_CONTENT + " TEXT"
                    + ")";

    public Note() {
    }

    public Note(int id, String Content, String Title) {
        this.id = id;
        this.Title = Title;
        this.Content = Content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getContent() {
        return Content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }
}
