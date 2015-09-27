 <html>
 	<head>
 		<meta http-equiv="Content-Type"
 			content="text/html; charset=ISO-8859-1">
 		<title>Embedding Jetty</title>
 		<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
 		<script>
			console.log("trying to get json");
			$.getJSON('http://localhost:10000/services/trainers/1', function(data) {
				console.log(data);
			})
			.fail( function(d, textStatus, error) {
				  console.error("getJSON failed, status: " + textStatus + ", error: "+error)
			 });
 		</script>
 	</head>
 	<body>
 		<h2>Running Jetty web server from our application!!</h2>
 	</body>
 </html>