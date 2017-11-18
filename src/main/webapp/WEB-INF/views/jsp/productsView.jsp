<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Maven + Spring MVC</title>
 
</head>
 
<nav class="navbar navbar-inverse navbar-fixed-top">
  <div class="container">
	<div class="navbar-header">
		<a class="navbar-brand" href="http://localhost:8080/spring4/pages/index.html">Edit Products</a>
	</div>
  </div>
</nav>
 
<div class="jumbotron">
  <div class="container">
	</div>
	<table border="1">
		<tr>
			<th>Product Id</th>
			<th>Name</th>
			<th>Category</th>
			<th>Price</th>
		</tr>
		<c:forEach items="${products}" var="product">
			<tr>
				<td>${product.id}</td>
				<td>${product.name}</td>
				<td>${product.catId}</td>
				<td>${product.price}</td>
			</tr>
		</c:forEach>
	</table>
</div>



</div>
 
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
 
</body>
</html>