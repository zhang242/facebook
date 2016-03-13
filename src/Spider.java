/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookwebcrawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hwei
 */
public class Spider
{
  private static final int MAX_PAGES_TO_SEARCH = 10;
  private Set<String> pagesVisited = new HashSet<String>();
  private List<String> pagesToVisit = new LinkedList<String>();
  
  private boolean debugFlag = false;
    
  //Amazon search URL
  private static final String AMAZON_SEARCH = 
    "http://www.amazon.ca/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=";
  
  public Map<String,Object> updateFacebookProfile(String url) {
      //Set<Map<String,Object>> fbProfile = new HashSet<>();
      SpiderLeg leg = new SpiderLeg();
      
      Map<String, Object> fbMap = leg.getFBInfo(url);
      
      return fbMap;
  }
  
  public Set<Map<String,Object>> updateProductListing(String url) {
      SpiderLeg leg = new SpiderLeg();   
      Set<Map<String,Object>> productListing = new HashSet<>();
      List<String> productList = leg.getProductLinks(url);
      List<String> extraLinks = new LinkedList<String>();
      
      if (productList.isEmpty()) {
          System.out.println("What???");
      }
      
      //int m = 0;
      for (String link : productList) {
          //if (m==1) break;
          //System.out.println(link);
          Map<String, Object> prodMap = leg.getProductInfo(link, extraLinks);
          if (prodMap != null) {
            productListing.add(prodMap);
          }
          //System.out.println(prodMap);
          //m++;
      }
      // /*
      for (String link : extraLinks) {
          //System.out.println(link);
          Map<String, Object> prodMap = leg.getProductInfo(link, null);
          if (prodMap != null) {
            productListing.add(prodMap);
          }
          //System.out.println(prodMap);
      }
      // */
      return productListing;
  }
  
  /**
   * Our main launching point for the Spider's functionality. Internally it creates spider legs
   * that make an HTTP request and parse the response (the web page).
   * 
   * @param url
   *            - The starting point of the spider
   * @param searchWord
   *            - The word or string that you are searching for
   */
  public void search(String url, String searchWord)
  {
      while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
      {
          String currentUrl;
          SpiderLeg leg = new SpiderLeg();
          if(this.pagesToVisit.isEmpty())
          {
              currentUrl = url;
              this.pagesVisited.add(url);
          }
          else
          {
              currentUrl = this.nextUrl();
          }
          leg.crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in
                                 // SpiderLeg
          boolean success = leg.searchForWord(searchWord);
          if(success)
          {
              System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
              break;
          }
          this.pagesToVisit.addAll(leg.getLinks());
      }
      System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
  }


  /**
   * Returns the next URL to visit (in the order that they were found). We also do a check to make
   * sure this method doesn't return a URL that has already been visited.
   * 
   * @return
   */
  private String nextUrl()
  {
      String nextUrl;
      do
      {
          nextUrl = this.pagesToVisit.remove(0);
      } while(this.pagesVisited.contains(nextUrl));
      this.pagesVisited.add(nextUrl);
      return nextUrl;
  }
  
    public String searchTag(String tag) {
        
        System.out.println("Searching for "+tag+" in Amazon");
        
        return AMAZON_SEARCH+tag;
    }
}