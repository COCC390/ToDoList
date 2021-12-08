package com.bc.secretnoteandtodo.database.model;

public class Note
{
   private int id, userId;
   private String content;

   public int getId()
   {
        return id;
   }

   public void setId(int id)
   {
        this.id = id;
   }

   public String getContent()
   {
        return content;
   }

   public void setContent(String content)
   {
        this.content = content;
   }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
