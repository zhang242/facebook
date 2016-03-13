/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookwebcrawler;

import java.util.Date;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hwei
 */
//package com.mkyong.core;
public class Mongo {

    private DBCollection myTable;
    
    public Mongo() {
        //Constructor
        myTable = this.queryDb(null);
    }

    private DBCollection queryDb(String[] args) {
        try {

            /**** Connect to MongoDB ****/
            // Since 2.10.0, uses MongoClient
            MongoClient mongo = new MongoClient("localhost", 27017);

            /**** Get database ****/
            // if database doesn't exists, MongoDB will create it for you
            DB db = mongo.getDB("test");

            /**** Get collection / table from 'testdb' ****/
            // if collection doesn't exists, MongoDB will create it for you
            DBCollection table = db.getCollection("recommendations");
            return table;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void addProducts(Set<Map<String,Object>> prodList) {
        for (Map<String,Object> prod : prodList) {               
            this.inputData(prod);
            this.searchData((String)prod.get("name"));
        }
    }

    public void inputData(Map<String, Object> product) {
        try{
            /**** Insert ****/
            // create a document to store key and value
            BasicDBObject document = new BasicDBObject();
            document.put("name", (String)product.get("name"));
            document.put("rating", (double)product.get("rating"));
            document.put("url", (String)product.get("url"));
            document.put("price", (String)product.get("price"));
            document.put("ID", (String)product.get("ID"));
            document.put("tag", (List<String>)product.get("tag"));
            document.put("review", (List<String>)product.get("review"));
            document.put("createdDate", new Date());
            removeData((String)product.get("name"));
            myTable.insert(document);
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
    
    public void removeData(String name) {
        try{
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("name", name);

            DBCursor cursor = myTable.find(searchQuery);

            while (cursor.hasNext()) {
                myTable.remove(cursor.next());
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
    
    public void searchData(String name) {
    try{
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("name", name);

        DBCursor cursor = myTable.find(searchQuery);

        while (cursor.hasNext()) {
                System.out.println(cursor.next());
        }

    } catch (MongoException e) {
        e.printStackTrace();
    }
  }
   
   public void addTagByID(int ID, Map<String, Object> fb) {
    try{
        //BasicDBObject searchQuery = new BasicDBObject();
        //searchQuery.append("_id", ID);
        
        //BasicDBObject updateQuery = new BasicDBObject();
	//updateQuery.append("$set", 
	//	new BasicDBObject().append("receiverTags", (List<String>)fb.get("tag")));

        //DBCursor cursor = myTable.find(searchQuery);

        //myTable.updateMulti(searchQuery, searchQuery);
        myTable.update(new BasicDBObject("_id", ID),
            new BasicDBObject("$set", new BasicDBObject("receiverTags", (List<String>)fb.get("tag"))));
        //myTable.insert(myObj);
    } catch (MongoException e) {
        e.printStackTrace();
    }      
   }
    
   public String getUrlFromID(int ID) {
    String url = "";
    try{
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", ID);

        DBCursor cursor = myTable.find(searchQuery);

        while (cursor.hasNext()) {
                DBObject myObj = cursor.next();
                url = (String) myObj.get("receiver");
        }
    } catch (MongoException e) {
        e.printStackTrace();
    }
    return url;
  }

  public void searchAndReplaceData(DBCollection table, String[]args){
        try {
            BasicDBObject query = new BasicDBObject();
            query.put("name", "mkyong");

            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("name", "mkyong-updated");

            BasicDBObject updateObj = new BasicDBObject();
            updateObj.put("$set", newDocument);

            table.update(query, updateObj);
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

}  
