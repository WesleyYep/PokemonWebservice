 <html>
 	<head>
 		<meta http-equiv="Content-Type"
 			content="text/html; charset=ISO-8859-1">
 		<title>Trainer Card</title>
 		<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
 	</head>
	<body onload="loadJSONData()">
		<script>

		function loadJSONData() {
			console.log("trying to get json");
			$.getJSON('http://localhost:10000/services/trainers/1', function(data) {
				console.log(data);
				document.write("<h2>Trainer Card </h2>");
				document.write("<p>");
				document.write("<h3>" + data.firstName + " " + data.lastName + "</h3>")
				document.write("Trainer id: " + data.id + "<br/>")
				document.write(data.gender + "<br/>")
				var dob = data.dateOfBirth;
				document.write(dob[0] + "-" + dob[1] + "-" + dob[2] + "<br/>");
				var record = data.record;
				document.write("Badges won: " + record.badgesWon + "<br/>")
				document.write("Battles won: " + record.battlesWon + "<br/>")
				document.write("Battles played: " + record.battlesPlayed + "<br/>")
				document.write("Competition wins: " + record.competitionWins + "<br/>")
				document.write("</p>");
			})
			.fail( function(d, textStatus, error) {
				  console.error("getJSON failed, status: " + textStatus + ", error: " + error)
			 });
		 }

		</script>
 	</body>
 </html>