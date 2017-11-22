package com.worldpay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class OfferDao {
   @SuppressWarnings("unchecked")
   public List<Offer> getAllOffers(){
      List<Offer> offerList = null;
      
      try {
         File file = new File(Database.offer);
         if (!file.exists()) {
            offerList = new ArrayList<Offer>();
            saveOfferList(offerList);		
         }else{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            offerList = (List<Offer>) ois.readObject();
            ois.close();
         }
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }	
      
      return offerList;
   }
   
   public List<Offer> getfiltedOffers(boolean cancelled, boolean expired){
	   List<Offer> offerList = getAllOffers();
	   List<Offer> offerList2 = new ArrayList<Offer>();
	   long curTime = System.currentTimeMillis();
       
	   for(Offer offer: offerList) {
    	   if(!cancelled) {
    		   if(offer.getIsCancelled())
    			   continue;
    	   }
    	   
    	   if(!expired) {
    		   long tmp = offer.getStartDate() + offer.getValidPeriod() * 24 * 3600 * 1000; 
    		   if(tmp < curTime)
    			   continue;
    	   }
    	   
    	   offerList2.add(offer);
       }
 
	   return offerList2;	   
   }

   public Offer getOffer(int id){
      List<Offer> offerList = getAllOffers();

      for(Offer offer: offerList){
         if(offer.getId() == id){
            return offer;
         }
      }
      
      return null;
   }

   public int addOffer(Offer pOffer){
      List<Offer> offerList = getAllOffers();
      boolean offerExists = false;
      
      for(Offer offer: offerList){
         if(offer.getId() == pOffer.getId()){
            offerExists = true;
            break;
         }
      }
      
      if(!offerExists){
         offerList.add(pOffer);
         saveOfferList(offerList);
         return 1;
      }
      
      return 0;
   }

   public int updateOffer(Offer pOffer){
      List<Offer> offerList = getAllOffers();

      for(Offer offer: offerList){
         if(offer.getId() == pOffer.getId()){
            int index = offerList.indexOf(offer);			
            offerList.set(index, pOffer);
            saveOfferList(offerList);
            return 1;
         }
      }
      
      return 0;
   }

   public int deleteOffer(int id){
      List<Offer> offerList = getAllOffers();

      for(Offer offer: offerList){
         if(offer.getId() == id){
            int index = offerList.indexOf(offer);			
            offerList.remove(index);
            saveOfferList(offerList);
            return 1;   
         }
      }
      
      return 0;
   }

   private void saveOfferList(List<Offer> offerList){
      try {
         File file = new File(Database.offer);
         FileOutputStream fos = new FileOutputStream(file);;
         ObjectOutputStream oos = new ObjectOutputStream(fos);		
         oos.writeObject(offerList);
         oos.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
