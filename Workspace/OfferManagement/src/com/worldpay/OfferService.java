package com.worldpay;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/OfferService")
public class OfferService {
	
   OfferDao offerDao = new OfferDao();
   private static final String SUCCESS_RESULT="<result>success</result>";
   private static final String FAILURE_RESULT="<result>failure</result>";


   @GET
   @Path("/offers")
   @Produces(MediaType.APPLICATION_XML)
   public List<Offer> getOffers(){
      return offerDao.getAllOffers();
   }   

   @GET
   @Path("/offers/{cancelled}/{expired}")
   @Produces(MediaType.APPLICATION_XML)
   public List<Offer> getfiltedOffer(@PathParam("cancelled") boolean cancelled,
		                             @PathParam("expired") boolean expired){
	  System.out.println("Test: " + cancelled + "  " + expired); 
      return offerDao.getfiltedOffers(cancelled, expired);
   }
   
   @GET
   @Path("/offers/{offerid}")
   @Produces(MediaType.APPLICATION_XML)
   public Offer getOffer(@PathParam("offerid") int offerid){
      return offerDao.getOffer(offerid);
   }   

   @POST
   @Path("/offers")
   @Produces(MediaType.APPLICATION_XML)
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public String createOffer(@FormParam("id") int id,
      @FormParam("productID") int productID,
      @FormParam("startDate") long startDate,
      @FormParam("validPeriod") int validPeriod,
      @FormParam("isCancelled") boolean isCancelled,
      @FormParam("offerRate") float offerRate,
      @Context HttpServletResponse servletResponse) throws IOException{
	  System.out.println(id + " " + productID + " " +  validPeriod+ " " +  startDate+ " " +  isCancelled+ " " +  offerRate); 
      Offer offer = new Offer(id, productID, startDate, 
    		                         validPeriod, isCancelled, offerRate);
      int result = offerDao.addOffer(offer);
      
      if(result == 1){
         return SUCCESS_RESULT;
      }
      
      return FAILURE_RESULT;
   }

   @PUT
   @Path("/offers")
   @Produces(MediaType.APPLICATION_XML)
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public String updateOffer(@FormParam("id") int id,
      @FormParam("productID") int productID,
      @FormParam("startDate") long startDate,
      @FormParam("validPeriod") int validPeriod,
      @FormParam("isCancelled") boolean isCancelled,
      @FormParam("offerRate") float offerRate,      
      @Context HttpServletResponse servletResponse) throws IOException{
      Offer offer = new Offer(id, productID, startDate, 
    		                         validPeriod, isCancelled, offerRate);
      int result = offerDao.updateOffer(offer);
      
      if(result == 1){
         return SUCCESS_RESULT;
      }
      
      return FAILURE_RESULT;
   }
   
   @DELETE
   @Path("/offers/{offerid}")
   @Produces(MediaType.APPLICATION_XML)
   public String deleteOffer(@PathParam("offerid") int offerid){
      int result = offerDao.deleteOffer(offerid);
      
      if(result == 1){
         return SUCCESS_RESULT;
      }
      
      return FAILURE_RESULT;
   }

   @OPTIONS
   @Path("/offers")
   @Produces(MediaType.APPLICATION_XML)
   public String getSupportedOperations(){
      return "<operations>GET, PUT, POST, DELETE</operations>";
   }
}
