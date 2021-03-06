package com.crunchify.restjersey;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.json.JSONException;

import com.tutorialspoint.stateless.LibrarySessionBeanRemote;

/**
 * http://rafael-pc:8080/SimpleBeanUseInsideServer/crunchify/getmovies
 * 
 * @author Rafael
 * 
 * It is better to do a JNDI local lookup.
 * 
 * Promoting this class to a EJB via @Stateless & @EJB annotations would work
 * but carry lots of additional weight behind it.
 * 
 */
//@Stateless
@Path("/getmovies")
public class GetMovies {
	
	//@EJB(name="LibrarySessionBean") // Also works
	public LibrarySessionBeanRemote bean;
	
	
	public GetMovies() {
		try {
			String lookupName = "java:global/SimpleBean/LibrarySessionBean";
			bean = (LibrarySessionBeanRemote) InitialContext.doLookup(lookupName);
	    } catch (NamingException e) {
	        e.printStackTrace();
	    }
	 }
	
	/*@EJB(name="LibrarySessionBean")
	public void setBean(LibrarySessionBeanRemote bean) {
		this.bean = bean;
	}*/
	
	@GET
	public Response listBooks() throws JSONException {
		String result = "";
		if(bean!=null){
			result = ""+bean.getBooks().size();
		}else{
			result = "Bean is null";
		}
		return Response.status(200).entity(result).build();
		
	}
	
}
