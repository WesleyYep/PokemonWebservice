package nz.ac.auckland.pokemon.services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import nz.ac.auckland.audit.Auditor;

import java.util.HashSet;
import java.util.Set;

/**
 * Application class for the Parolee Web service. This class is required by the 
 * JAX-RS implementation to deploy the Web service.
 *  
 * The relative path of the Web service beings with "services". If the Web 
 * server's address is "http://localhost:8080", the URI for hosted Web services
 * thus begins "http://localhost:8080/services". 
 * 
 * The TrainerResource specifies a URI path of "trainers", so this is appended,
 * making the Parolees Web service URI "http://localhost:8080/services/trainers".
 *
 *  
 * @author Ian Warren
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
