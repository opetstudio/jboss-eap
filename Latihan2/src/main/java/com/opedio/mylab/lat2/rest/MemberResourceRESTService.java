package com.opedio.mylab.lat2.rest;



import java.util.Date;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.solusi247.poin.util.TselpoinIDGenerator;

import com.solusi247.benchmark.PoinSms;
import org.infinispan.*;
import org.infinispan.manager.CacheContainer;
import org.jboss.msc.service.StartException;
import org.jboss.logging.Logger;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/members")
@RequestScoped
public class MemberResourceRESTService {
	
	static private Logger logger = Logger.getLogger(MemberResourceRESTService.class);
//    private org.infinispan.manager.CacheContainer container; 
//	@Resource(name = "java:jboss/infinispan/container/WorldCities") private org.infinispan.manager.CacheContainer container;  
	
	CacheContainer container;
	private Cache<String, Long> tspoinParamCache = null;
	
	 @GET
	 @Path("/{id:[0-9][0-9]*}")
	 @Produces(MediaType.APPLICATION_JSON)
	public String lookupMemberById(@PathParam("id") long id) {
		 PoinSms._trx_traffic_begin_tick("API4");
		 try {
			   InitialContext ic = new InitialContext();
			   container = (CacheContainer) ic.lookup("java:jboss/infinispan/container/tspoinparam");
			   tspoinParamCache = getCityCache();
		 } catch (NamingException e) {}
		 long val = 0;
		 if(id != 0){
			 logger.info("parameter id ada value. set cache tes");
			 if(tspoinParamCache != null) tspoinParamCache.put("tes", id);
			 val = id; 
		 }else{
			 logger.info("parameter id tidak ada value. get cache tes");
			 if(tspoinParamCache != null){
			 try {
				 val = tspoinParamCache.get("tes");
				} catch (Exception e) {
					tspoinParamCache.put("tes", 0l);
				}
			 }
			 
		 }
		 
		 logger.info("nilai id = "+id+ " val="+val);
		 
		/* try {
			   InitialContext ic = new InitialContext();
			   container = (CacheContainer) ic.lookup("java:jboss/infinispan/container/tspoinparam");
//			   Cache<String, Object> cache = cc.getCache();
			  } catch (NamingException e) {
				  logger.error("[] destroy, "+ e);
					for (StackTraceElement s: e.getStackTrace()) {
						logger.error("[] at "+ s);
					}
			  }
		 
		 
//		 org.infinispan.Cache cache  = container.getCache("WorldCitiesCache");
		 
		 Cache<String, Integer> cityCache = getCityCache();
		 int i = 0;
		 int val = 0;
	      if (cityCache != null){
	    	try {
	    		  val = cityCache.get("trxid");
	    		  val++;
	    		  logger.info("tidak ada error "+val);
			} catch (Exception e) {
				logger.error("ada error: "+e);
			}
	      }
		 
		 if(i==0) i=0;
		 else i++;
		 
		    if (cityCache != null){
		    	logger.info("citycache tidak null");
		        cityCache.put("trxid", val);
		    }
		    else{
		    	logger.info("citycache null");
		    }
		 */
		return ""+TselpoinIDGenerator.getNextTselpoinID();
	}
	 public <K, V> Cache<K, V> getCityCache() {
		    try {
		        if (container != null) {
		          Cache<K, V> cache = container.getCache("tspoinparamCache");
		            return cache;
		        }
		        else{
		        	logger.error("container null");
		        }
		    } catch (Exception ex) {
		      logger.error("Failed to get cache: " + ex.getLocalizedMessage());
		      ex.printStackTrace();
		    }
		    return null;
		}

}
