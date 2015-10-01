package nz.ac.auckland.authentication.test;

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
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple JUnit test case to test that the server authentication is correct.
 * POST requests should not be able to pass unless they have the required cookie
 * This class simply tests cases which should fail due to unauthorised error 403
 * All other classes should test the use of correct authentication cookies for POST and PUT requests
 *
 * @author Wesley Yep
 *
 */
public class AuthenticationTest
{
	private Logger _logger = LoggerFactory.getLogger(AuthenticationTest.class);

    @BeforeClass
    public static void initializeIfNeeded() {
        InitialiseTest.init();
    }

   @Test
   public void testsPass() {}

    /**
     * Test with no cookie sent
     */
   @Test
   public void testAuthenticationWithoutCookies()
   {
	   Client client = ClientBuilder.newClient();
	      try {
	         _logger.info("Invoking webservice ...");

              TrainerDTO misty = new TrainerDTO("mistylastname", "misty", Gender.FEMALE, new LocalDate(1978, 2, 3), new Record());
              // Send a HTTP POST message, with a message body containing the XML,
              // to the Web service.
              Response response = client.target(InitialiseTest.webserviceURL + "/services/trainers")
                      .request().post(Entity.xml(misty));

	         // Expect a 403 "Unauthorised"response from the Web service.
	         int status = response.getStatus();
	         if (status != 403) {
	        	 _logger.error("Should have thrown a unauthorized error due to lack of authentication cookie.");
	        	 fail();
	         }
	         response.close();
	      } finally {
	    	  // Release any connection resources.

	         client.close();
	      }
   }

    /**
     * Tests using a cookie that does not contain a valid username/password pair
     * See other tests in other files for POST and PUT requests with correct authentication cookies
     */
   @Test
   public void testAuthenticationWithWrongCookie()
   {
	// Use ClientBuilder to create a new client that can be used to create
	   // connections to the Web service.
      Client client = ClientBuilder.newClient();
      try {
         _logger.info("Creating a new Trainer ...");

          TrainerDTO dawn = new TrainerDTO("piplup", "dawn", Gender.FEMALE, new LocalDate(1968, 12, 5), new Record());

          Cookie cookie = new Cookie("username","test");
          Response response = client.target(InitialiseTest.webserviceURL + "/services/trainers")
                 .request().cookie(cookie).post(Entity.xml(dawn));

         // Expect a HTTP 403 "Unauthorised" response from the Web service.
         int status = response.getStatus();
         if (status != 403) {
             _logger.error("Should have thrown a unauthorized error due to lack of authentication cookie.");
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

