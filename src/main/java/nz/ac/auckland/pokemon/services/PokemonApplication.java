package nz.ac.auckland.pokemon.services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import nz.ac.auckland.audit.Auditor;

import java.util.HashSet;
import java.util.Set;

/**
 * Application class for the Pokemon Web service. This class is required by the
 * JAX-RS implementation to deploy the Web service.
 *  
 * The relative path of the Web service beings with "services". If the Web 
 * server's address is "http://localhost:10000", the URI for hosted Web services
 * thus begins "http://localhost:10000/services".
 * The ajax client is accessed at "http://localhost:10000"
 * 
 * The TrainerResource specifies a URI path of "trainers", so this is appended,
 * making the Trainer Web service URI "http://localhost:8080/services/trainers".
 *
 *  
 * @author Wesley Yep
 *
 */
@ApplicationPath("/services")
public class PokemonApplication extends Application
{
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> components =new HashSet<Class<?>>();
   
   public PokemonApplication()
   {
	  singletons.add(new TrainerResource());
	  singletons.add(new PokemonResource());
	  singletons.add(new BattleResource());
	  singletons.add(new UserResource());
	  singletons.add(new TestResource());
	  singletons.add(new TeamResource());
	  components.add(Auditor.class);
   }

   @Override
   public Set<Object> getSingletons()
   {
	  return singletons;
   }
   
   @Override
   public Set<Class<?>> getClasses()
   {
	  return components;
   }
}
