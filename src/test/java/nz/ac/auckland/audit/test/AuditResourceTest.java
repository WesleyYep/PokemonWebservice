//package nz.ac.auckland.audit.test;
//
//import static org.junit.Assert.fail;
//
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.Cookie;
//import javax.ws.rs.core.Response;
//
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Simple JUnit test case to test for Audit
// *
// */
//public class AuditResourceTest
//{
//	private Logger _logger = LoggerFactory.getLogger(AuditResourceTest.class);
//
//   @Test
//   public void testsPass() {}
//
//   @Test
//   public void testAuditWithoutCookies()
//   {
//	   Client client = ClientBuilder.newClient();
//	      try {
//	         _logger.info("Invoking webservice ...");
//
//	         // Create a XML representation for a new Trainer.
//	         String xml = "<trainer>"
//	                 + "<first-name>Bob</first-name>"
//	                 + "<last-name>Jones</last-name>"
//	                 + "<gender>Male</gender>"
//	                 + "<date-of-birth>01/01/1950</date-of-birth>"
//	                 + "</trainer>";
//
//	         // Send a HTTP POST message, with a message body containing the XML,
//	         // to the Web service.
//	         Response response = client.target("http://localhost:10000/services/trainers")
//	                 .request().post(Entity.xml(xml));
//
//	         // Expect a HTTP 201 "Created" response from the Web service.
//	         int status = response.getStatus();
//	         if (status != 201) {
//	        	 _logger.error("It Failed to create Trainer; Web service responded with: " + status);
//	        	 fail();
//	         }
//	         response.close();
//	      } finally {
//	    	  // Release any connection resources.
//
//	         client.close();
//	      }
//   }
//
//   @Test
//   public void testAuditCookie()
//   {
//	// Use ClientBuilder to create a new client that can be used to create
//	   // connections to the Web service.
//      Client client = ClientBuilder.newClient();
//      try {
//         _logger.info("Creating a new Trainer ...");
//
//         // Create a XML representation for a new Trainer.
//         String xml = "<trainer>"
//                 + "<first-name>Bob</first-name>"
//                 + "<last-name>Jones</last-name>"
//                 + "<gender>Male</gender>"
//                 + "<date-of-birth>01/01/1950</date-of-birth>"
//                 + "</trainer>";
//
//         // Send a HTTP POST message, with a message body containing the XML,
//         // to the Web service.
//         Cookie cookie =new Cookie("username","test");
//         Response response = client.target("http://localhost:10000/services/trainers")
//                 .request().cookie(cookie).post(Entity.xml(xml));
//
//         // Expect a HTTP 201 "Created" response from the Web service.
//         int status = response.getStatus();
//         if (status != 201) {
//        	 _logger.error("Failed to create Trainer; Web service responded with: " + status);
//        	 fail();
//         }
//
//         // Close the connection.
//         response.close();
//
//
//      } finally {
//    	  // Release any connection resources.
//         client.close();
//      }
//   }
//}
//
