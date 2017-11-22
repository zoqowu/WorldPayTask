package com.worldpay;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "offer")
public class Offer implements Serializable {

   private static final long serialVersionUID = 2L;
   private int id;
   private int productID;
   private long startDate;
   private int validPeriod; //days
   private boolean isCancelled;
   private float offerRate;

   public Offer(){}

   public Offer(int id, int productID, long startDate, int validPeriod, 
		                              boolean isCancelled, float offerRate){
      this.id = id;
      this.productID = productID;
      this.startDate = startDate;
      this.validPeriod = validPeriod;
      this.isCancelled = isCancelled;
      this.offerRate = offerRate;
   }

   @XmlElement
   public void setId(int id) {
      this.id = id;
   }
   
   public int getId() {
	      return id;
   }
 
   @XmlElement
   public void setProductID(int productID) {
      this.productID = productID;
   }
   
   public int getProductID() {
	      return productID;
   }   
   
   @XmlElement
   public void setIsCancelled(boolean isCancelled) {
      this.isCancelled = isCancelled;
   }
   
   public boolean getIsCancelled() {
	  return isCancelled;
   }
   
   @XmlElement
   public void setOfferRate(float offerRate) {
      this.offerRate = offerRate;
   }
   
   public float getOfferRate() {
	  return offerRate;
   }    
 
   @XmlElement
   public void setStartDate(long startDate) {
       this.startDate = startDate;
   }
   
   public long getStartDate() {
      return startDate;
   }

   @XmlElement
   public void setValidPeriod(int validPeriod) {
      this.validPeriod = validPeriod;
   }  
   
   public int getValidPeriod() {
      return validPeriod;
   }	

   @Override
   public boolean equals(Object object){
      if(object == null){
         return false;
      }else if(!(object instanceof Offer)){
         return false;
      }else {
         Offer offer = (Offer)object;
         if(id == offer.getId()
            && productID == offer.productID
            && startDate == offer.startDate
            && validPeriod == offer.validPeriod
            && isCancelled == offer.isCancelled
            && offerRate == offer.offerRate){
            return true;
         }			
      }
      
      return false;
   }	
}