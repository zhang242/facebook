/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookwebcrawler;

import java.util.Map;

/**
 *
 * @author Xu
 */
public class FacebookWebCrawler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Mongo mg = new Mongo();
        String fbProfileUrl = "";
        int userID = 68;
        //Check if we have any arguments...
        if (args.length > 0) {
            userID = Integer.parseInt(args[0]);
        }
        
        fbProfileUrl = mg.getUrlFromID(userID);
        
        Spider spider = new Spider();               
        
        Map<String,Object> fb = spider.updateFacebookProfile(fbProfileUrl);
        //System.out.println(fb);
        mg.addTagByID(userID, fb);
    }
    
}
