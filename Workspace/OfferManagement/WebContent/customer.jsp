<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
  <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css">
  <link href = "https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" rel = "stylesheet">
  <script src = "https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script type="text/javascript">
$(document).ready(function() {
	getProductInfo();    
} );

function getProductInfo() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
          products = processXMLResponse(this);
          console.log("Test " + products[0][0] + "  " + products[0][1] + "  " + products[0][2]);
          getOfferInfo();
        } else
            console.log("Product Response - ReadyState: " + this.readyState + "   " + 
                                                                    "Status: " + this.status);
    };
    
    xhttp.open("GET", "http://localhost:8080/OfferManagement/rest/ProductService/products", true);
    xhttp.send();  	
}

function getOfferInfo() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            offers = processXMLResponse(this);
            console.log("Test " + offers[0][0] + "  " + offers[0][1] + "  " + offers[0][2] +
            		                offers[0][3] + "  " + offers[0][4] + "  " + offers[0][5]);
            
         	// updateOfferTable(data);
         	var list = calOfferPrice(products, offers);
         	console.log("Test " + list[0][0] + "  " + list[0][1] + "  " + list[0][2] + "  " + 
         			list[0][3]);
         	updateOfferTable(list);
        } else
            console.log("Offer Response - ReadyState: " + this.readyState + "   " + 
            		                                               "Status: " + this.status);
    };
    
    var url = "http://localhost:8080/OfferManagement/rest/OfferService/offers";
    var cancelled = false;
    var expired = false;
    
   	url = url + '/' + cancelled + '/' + expired;
   	console.log('Test' + url);
    xhttp.open("GET", url, true);
    xhttp.send();  	
}

function calOfferPrice(products, offers) {
	var list = [];
	var newPrice, validTill;
	
	for(var i = 0; i < products.length; i++) {
		var row = products[i];
		[newPrice, validTill] = getBestOffer(offers, products[i][0]);
		newPrice = newPrice * products[i][2];
		row.push(newPrice.toFixed(2));
		row.push(validTill);
		list.push(row);
	}
	
	return list;
}

function getBestOffer(offers, productID) {
	var min = 1.0;
	var d = new Date();
	var n = d.getTime();
	var validTill = '-';
	
	for(var i = 0; i < offers.length; i++) {
		var t = new Date(offers[i][4].substring(6), parseInt(offers[i][4].substring(3,5))-1, 
				                                                 offers[i][4].substring(0,2));
		var lt = t.getTime();
		console.log("Test 0 " + lt);
		console.log(n);
		console.log(lt + parseInt(offers[i][5])*24*3600*1000);
		
		if(n < lt|| (lt + parseInt(offers[i][5])*24*3600*1000) < n) // not in valid period
			continue;
		
		console.log(">>>>>>");
		
		if(productID == offers[i][3] && offers[i][2] < min) {
			min = offers[i][2];
			var x = new Date(lt + parseInt(offers[i][5])*24*3600*1000);
			validTill = x.getDate() + '-' + (x.getMonth() + 1) + '-' + x.getFullYear();			
		}
	} 
	
	return [min, validTill];
}

function processXMLResponse(xhttp) {
    var xml1 = (new DOMParser()).parseFromString(xhttp.responseText, "text/xml");

    if(!xml1.hasChildNodes()) 
    	return null;
	
    var xml2 = xml1.childNodes.item(0);
    
    if (xml2.hasChildNodes()) {
		var col = (xml2.childNodes.item(0)).childNodes.length;
		var data = [];
		
		for(var i = 0; i < xml2.childNodes.length; i++){
			var item = xml2.childNodes.item(i);
			var row = [];
			
			for(var j = 0; j < item.childNodes.length; j++) {
			   var attr = item.childNodes.item(j);
			   
			   if(attr.nodeName == "startDate") {
				   var t = new Date(parseInt(attr.textContent));
				   var d = t.getDate() + '-' + (t.getMonth() + 1) + '-' + t.getFullYear();
				   row.push(d);
			   } else	   
			       row.push(attr.textContent);
			}
			
			data.push(row);
        }
		
		return data;
    } else
    	return null;
}

function updateOfferTable(dataSet) {
	if(typeof(offTable) == 'undefined') {
        offTable = $('#merchandiseTbl').DataTable( {
            data: dataSet,
            columns: [
                { title: "ID" },
                { title: "Name" },
                { title: "Price" },
                { title: "After Discount" },
                { title: "Valid Till" },
            ]
        } );
	} else {
       offTable.clear().draw();
       offTable.rows.add(dataSet); // Add new data
       offTable.columns.adjust().draw(); // Redraw the DataTable		
	}
  }

  </script>
</head>
<body>
  <div class="row">
    <div class="col-sm-2">
    </div>
    <div class="col-sm-8">
        <table id="merchandiseTbl" class="display">
         <thead></thead>
         <tbody></tbody>     
       </table>
    </div>
    <div class="col-sm-2">
    </div>    
  </div>
</body>
</html>