package com.worldpay;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

public class ProductServiceTester  {

   private Client client;
   private String REST_SERVICE_URL = "http://localhost:8080/OfferManagement/rest/ProductService/products";
   private static final String SUCCESS_RESULT="<result>success</result>";
   private static final String PASS = "pass";
   private static final String FAIL = "fail";

   private void init(){
      this.client = ClientBuilder.newClient();
   }

   public static void main(String[] args){
	   ProductServiceTester tester = new ProductServiceTester();
      //initialize the tester
      tester.init();
      //test get all products Web Service Method
      tester.testGetAllProducts();
      //test get product Web Service Method 
      tester.testGetProduct();
      //test update product Web Service Method
      tester.testUpdateProduct();
      //test add product Web Service Method
      tester.testAddProduct();
      //test delete product Web Service Method
      tester.testDeleteProduct();
   }
   //Test: Get list of all products
   //Test: Check if list is not empty
   private void testGetAllProducts(){
      GenericType<List<Product>> list = new GenericType<List<Product>>() {};
      List<Product> products = client.target(REST_SERVICE_URL).request(MediaType.APPLICATION_XML).get(list);
      String result = PASS;
      if(products.isEmpty()){
         result = FAIL;
      }
      System.out.println("Test case name: testGetAllProducts, Result: " + result );
   }
   //Test: Get Product of id 1
   //Test: Check if product is same as sample product
   private void testGetProduct(){
      Product sampleProduct = new Product();
      sampleProduct.setId(1);

      Product product = client
         .target(REST_SERVICE_URL)
         .path("/{productid}")
         .resolveTemplate("productid", 1)
         .request(MediaType.APPLICATION_XML)
         .get(Product.class);
      String result = FAIL;
      if(sampleProduct != null && sampleProduct.getId() == product.getId()){
         result = PASS;
      }
      System.out.println("Test case name: testGetProduct, Result: " + result );
   }
   //Test: Update Product of id 1
   //Test: Check if result is success XML.
   private void testUpdateProduct(){
      Form form = new Form();
      form.param("id", "1");
      form.param("name", "pen");
      form.param("price", "0.99");

      String callResult = client
         .target(REST_SERVICE_URL)
         .request(MediaType.APPLICATION_XML)
         .put(Entity.entity(form,
            MediaType.APPLICATION_FORM_URLENCODED_TYPE),
            String.class);
      String result = PASS;
      if(!SUCCESS_RESULT.equals(callResult)){
         result = FAIL;
      }

      System.out.println("Test case name: testUpdateProduct, Result: " + result );
   }
   //Test: Add Product of id 10
   //Test: Check if result is success XML.
   private void testAddProduct(){
      Form form = new Form();
      form.param("id", "10");
      form.param("name", "keyboard");
      form.param("price", "19.99");

      String callResult = client
         .target(REST_SERVICE_URL)
         .request(MediaType.APPLICATION_XML)
         .post(Entity.entity(form,
            MediaType.APPLICATION_FORM_URLENCODED_TYPE),
            String.class);
   
      String result = PASS;
      if(!SUCCESS_RESULT.equals(callResult)){
         result = FAIL;
      }

      System.out.println("Test case name: testAddProduct, Result: " + result );
   }
   //Test: Delete Product of id 10
   //Test: Check if result is success XML.
   private void testDeleteProduct(){
      String callResult = client
         .target(REST_SERVICE_URL)
         .path("/{productid}")
         .resolveTemplate("productid", 10)
         .request(MediaType.APPLICATION_XML)
         .delete(String.class);

      String result = PASS;
      if(!SUCCESS_RESULT.equals(callResult)){
         result = FAIL;
      }

      System.out.println("Test case name: testDeleteProduct, Result: " + result );
   }
}