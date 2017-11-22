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

@Path("/ProductService")
public class ProductService {
	
   ProductDao productDao = new ProductDao();
   private static final String SUCCESS_RESULT="<result>success</result>";
   private static final String FAILURE_RESULT="<result>failure</result>";


   @GET
   @Path("/products")
   @Produces(MediaType.APPLICATION_XML)
   public List<Product> getProducts(){
      return productDao.getAllProducts();
   }

   @GET
   @Path("/products/{prodid}")
   @Produces(MediaType.APPLICATION_XML)
   public Product getProduct(@PathParam("prodid") int prodid){
      return productDao.getProduct(prodid);
   }

   @POST
   @Path("/products")
   @Produces(MediaType.APPLICATION_XML)
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public String createProduct(@FormParam("id") int id,
      @FormParam("name") String name,
      @FormParam("price") float price,
      @Context HttpServletResponse servletResponse) throws IOException{
      Product product = new Product(id, name, price);
      int result = productDao.addProduct(product);
      if(result == 1){
         return SUCCESS_RESULT;
      }
      return FAILURE_RESULT;
   }

   @PUT
   @Path("/products")
   @Produces(MediaType.APPLICATION_XML)
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public String updateProduct(@FormParam("id") int id,
      @FormParam("name") String name,
      @FormParam("price") float price,
      @Context HttpServletResponse servletResponse) throws IOException{
      Product product = new Product(id, name, price);
      int result = productDao.updateProduct(product);
      if(result == 1){
         return SUCCESS_RESULT;
      }
      return FAILURE_RESULT;
   }

   @DELETE
   @Path("/products/{prodid}")
   @Produces(MediaType.APPLICATION_XML)
   public String deleteProduct(@PathParam("prodid") int prodid){
      int result = productDao.deleteProduct(prodid);
      if(result == 1){
         return SUCCESS_RESULT;
      }
      return FAILURE_RESULT;
   }

   @OPTIONS
   @Path("/products")
   @Produces(MediaType.APPLICATION_XML)
   public String getSupportedOperations(){
      return "<operations>GET, PUT, POST, DELETE</operations>";
   }
}