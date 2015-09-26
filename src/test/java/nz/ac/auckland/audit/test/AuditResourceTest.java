package nz.ac.auckland.audit.test;

import static org.junit.Assert.fail;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import nz.ac.auckland.pokemon.domain.Gender;
import nz.ac.auckland.pokemon.domain.Record;
import nz.ac.auckland.pokemon.dto.TrainerDTO;
import nz.ac.auckland.setup.test.InitialiseTest;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple JUnit test case to test for Audit
 *
 */
public class AuditResourceTest
{
	private Logger _logger = LoggerFactory.getLogger(AuditResourceTest.class);

    @BeforeClass
    public static void initializeIfNeeded() {
        InitialiseTest.init();
    }

   @Test
   public void testsPass() {}

   @Test
   public void testAuditWithoutCookies()
   {
	   Client client = ClientBuilder.newClient();
	      try {
	         _logger.info("Invoking webservice ...");

              TrainerDTO misty = new TrainerDTO("mistylastname", "misty", Gender.FEMALE, new LocalDate(1978, 2, 3), new Record());
              // Send a HTTP POST message, with a message body containing the XML,
              // to the Web service.
              Response response = client.target("http://localhost:10000/services/trainers")
                      .request().post(Entity.xml(misty));

	         // Expect a HTTP 201 "Created" response from the Web service.
	         int status = response.getStatus();
	         if (status != 201) {
	        	 _logger.error("It Failed to create Trainer; Web service responded with: " + status);
	        	 fail();
	         }
	         response.close();
	      } finally {
	    	  // Release any connection resources.

	         client.close();
	      }
   }

   @Test
   public void testAuditCookie()
   {
	// Use ClientBuilder to create a new client that can be used to create
	   // connections to the Web service.
      Client client = ClientBuilder.newClient();
      try {
         _logger.info("Creating a new Trainer ...");

          TrainerDTO dawn = new TrainerDTO("piplup", "dawn", Gender.FEMALE, new LocalDate(1968, 12, 5), new Record());

          Cookie cookie =new Cookie("username","test");
          Response response = client.target("http://localhost:10000/services/trainers")
                 .request().cookie(cookie).post(Entity.xml(dawn));

         // Expect a HTTP 201 "Created" response from the Web service.
         int status = response.getStatus();
         if (status != 201) {
        	 _logger.error("Failed to create Trainer; Web service responded with: " + status);
        	 fail();
         }

         // Close the connection.
         response.close();


      } finally {
    	  // Release any connection resources.
         client.close();
      }
   }
}

