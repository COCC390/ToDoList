package com.bc.secretnoteandtodo.database.model;

public class Note
{
   private int id;
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
}
