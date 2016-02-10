/**************************************************************************************
 * Copyright (C) 2007 Esper Team. All rights reserved.                                *
 * http://esper.codehaus.org                                                          *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.solusi247.benchmark.trx;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.logging.Logger; // < --- fire error NoClassDefFoundError: org/jboss/logging/Logger

public enum CountTrx {
	INSTANCE;
	
    private static Logger logger = Logger.getLogger(CountTrx.class);
    
    private volatile long _in_telnet_ticker=0;
  
    private volatile long _in_main_ticker=0;
    private volatile long _out_main_ticker=0;
    
    private volatile long _in_tweede_ticker=0;
    private volatile long _out_tweede_ticker=0;
      
    private volatile long _logger_in_ticker=0;
    private volatile long _logger_out_ticker=0;
    
    private volatile long _unifier_in_ticker=0;
    private volatile long _unifier_out_ticker=0;
    
    private volatile long _krome_in_ticker=0;
    private volatile long _krome_out_ticker=0;
    private volatile long _krome_done_ticker=0;
    
    
    //per API
    private volatile long _trumb_hit_ticker=0; //com.solusi247.poin.smsq.wsredeem.PoinRedeemServlet
    private volatile long _poinmo_hit_ticker=0; //com.solusi247.poin.smsq.wsmo.SmsMOServlet
    private volatile long _contactcenter_cekpoin_hit_ticker=0; //com.solusi247.poin.myapps.GetPoinContactCenterServlet
    private volatile long _geteligible_prepaid_hit_ticker=0;//com.solusi247.poin.corprod.wsprepaid.CpPrepaidGetEligibleServlet
//    private volatile long _geteligible_prepaid_hit_ticker=0;//com.solusi247.poin.corprod.wsprepaid.CpPrepaidGetEligibleServlet
    
    //umbapi.war
    private volatile long _umpapi_check_data_hitticker=0; //com.solusi247.umbapi.main.CheckData.class
    private volatile long _umpapi_create_data_hitticker=0; //com.solusi247.umbapi.main.CreateData.class
    private volatile long _umpapi_bonus_conf_hitticker=0; //com.solusi247.smauloop.wsbonus.BonusConfServlet.class
    private volatile long _umpapi_getdetaillinkid_hitticker=0; //com.solusi247.smauloop.wsbonus.GetDetailLinkIdServlet.class
    
    //TselVerify.war
    
    private  Map<String, Long> _trx_traffic_in = new HashMap();
    private  Map<String, String> _trx_traffic_out = new HashMap();
    
    //count time process
    private  Map<String, Long> _trx_traffic_diftime = new HashMap();
    private  Map<String, Long> _trx_traffic_mintime = new HashMap();
    private  Map<String, Long> _trx_traffic_maxtime = new HashMap();
    private  Map<String, Long> _trx_traffic_pertime = new HashMap();
    private  Map<String, Long> _trx_traffic_issuccess = new HashMap();
    
    
 
    
    // static private AtomicInteger _ticker = new AtomicInteger(0);
    private volatile long start_time;//ms
    private volatile long end_time;
    
    private OBTimer _ti_reporter = new OBTimer();
    private int _timer = 10000;
    private String id = "PoinSms";
    
    private class OBTimer extends Thread {
    	boolean _keep_running = true;
    	@Override
    	public void run() {
    		//System.out.println("Benchmark Poin SMS started");
    		logger.info("Benchmark Poin SMS started");
    		 
    		while(_keep_running) {
    			
    			end_time = System.currentTimeMillis();

    			float elapse = (end_time - start_time) / 1000f;
     			
    			// System.out.println("BenchmarkBrokerOut TPS = "+  _ticker/elapse +" messages per second");
    			//System.out.println(
//    			logger.info(String.format("TPS telnet in %.2f (sms queue in: %.2f out: %.2f) (xyz queue in: %.2f out: %.2f) " +
//    					"(krome in: %.2f out: %.2f sent: %.2f ) (logger in: %.2f out: %.2f) (unifier in: %.2f out: %.2f)"
//    					+ "",
//    				_in_telnet_ticker/elapse, _in_main_ticker/elapse, _out_main_ticker/elapse,
//    	            _in_tweede_ticker/elapse, _out_tweede_ticker/elapse, 
//    	            _krome_in_ticker/elapse, _krome_out_ticker/elapse, _krome_done_ticker/elapse, 
//    	            _logger_in_ticker/elapse,  _logger_out_ticker/elapse,
//    	            _unifier_in_ticker/elapse,  _unifier_out_ticker/elapse) 
//    	            );
    			
    			//per API
//    			logger.info(String.format("TpsPerAPI TRUMB:%,3f|MO:%,3f",
//    					_trumb_hit_ticker/elapse,_poinmo_hit_ticker/elapse));

    			//untuk hitung traffic
//    			INSTANCE._trx_traffic_diftime.put(key, INSTANCE._trx_traffic_diftime.get(key) + dif_time);
//    	    	INSTANCE._trx_traffic_maxtime.put(key, INSTANCE._trx_traffic_maxtime.get(key) > dif_time ? INSTANCE._trx_traffic_maxtime.get(key):dif_time);
//    	    	INSTANCE._trx_traffic_maxtime.put(key, INSTANCE._trx_traffic_mintime.get(key) < dif_time ? INSTANCE._trx_traffic_mintime.get(key):dif_time);
//    	    	INSTANCE._trx_traffic_pertime.put(key, INSTANCE._trx_traffic_pertime.get(key) + 1);
//    	    	INSTANCE._trx_traffic_issuccess.put(key, is_success ? INSTANCE._trx_traffic_issuccess.get(key) + 1 : INSTANCE._trx_traffic_issuccess.get(key));
    			
    			
    			try {
    				String trxTraffic = "trxreport begin[";
        			int i = 1;
        			int total = _trx_traffic_in.size();
        			for(Entry<String, Long> entry : _trx_traffic_in.entrySet()) {
        				String key = entry.getKey();
        			    long val_in = (entry.getValue() != null)? entry.getValue() : 0l;
        				long tot_in = val_in;
        				
        				Long tot = INSTANCE._trx_traffic_pertime.get(key);
        				tot = (tot != null)? tot : 0l;
        				Long dif = INSTANCE._trx_traffic_diftime.get(key);
        		    	dif = (dif != null)? dif : 0l;
        		    	Long max = INSTANCE._trx_traffic_maxtime.get(key);
        		    	max = (max != null)? max : 0l;
        		    	Long min = INSTANCE._trx_traffic_mintime.get(key);
        		    	min = (min != null)? min : 0l;
        		    	Long totsuccess = INSTANCE._trx_traffic_issuccess.get(key);
        		    	totsuccess = (totsuccess != null)? totsuccess : 0l;
        		    	
        		    	if(tot > 0) dif = dif/tot;
        				 
        		    	
        			    if(i==total) trxTraffic +=  "{\"k\":\""+key+"\",\"v\":\""+String.format("%.3f|%.3f",tot_in/elapse,tot/elapse)+"|"+dif+"|"+max+"|"+min+"|"+tot_in+"|"+tot+"|"+end_time+"|"+totsuccess+"\"}";
        			    else  trxTraffic +=  trxTraffic +=  "{\"k\":\""+key+"\",\"v\":\""+String.format("%.3f|%.3f",tot_in/elapse,tot/elapse)+"|"+dif+"|"+max+"|"+min+"|"+tot_in+"|"+tot+"|"+end_time+"|"+totsuccess+"\"},";
        			    i++;
        			}
        			trxTraffic += "]end";
        			logger.info(trxTraffic);
				} catch (Exception e) {
					logger.error("ada error:"+ e.getMessage());
					for (StackTraceElement se: e.getStackTrace()) {
						logger.error(" at "+ se);
					}
				}
    			
    			reset() ;
    			
    			try {
    				Thread.sleep(_timer);
    			} catch (InterruptedException e) {
    				_keep_running = false;
    			}
    		}
    		
     		logger.info("Benchmark Poin SMS stopped");
     		
    	}
    }
     
    
 
    public static CountTrx getInstance() {
		return INSTANCE;
	}

    private CountTrx() {
    	start_time = System.currentTimeMillis();
    	end_time = System.currentTimeMillis();
    	_ti_reporter.start();
    }
     
    private synchronized void reset() {
    	_in_telnet_ticker=0;

    	_in_main_ticker=0;
    	_out_main_ticker=0;

    	_in_tweede_ticker=0;
    	_out_tweede_ticker=0;

    	_logger_in_ticker=0;
    	_logger_out_ticker=0;

    	_krome_in_ticker=0;
    	_krome_out_ticker=0;
    	_krome_done_ticker=0;
    	
    	_unifier_in_ticker=0;
    	_unifier_out_ticker=0;
    	
    	//per api
    	_trumb_hit_ticker=0;
    	_poinmo_hit_ticker=0;

    	start_time = System.currentTimeMillis();
    	end_time = System.currentTimeMillis();
    	
    	_trx_traffic_out = new HashMap();
    	_trx_traffic_in = new HashMap();
    	
    	 _trx_traffic_diftime = new HashMap();
    	 _trx_traffic_mintime = new HashMap();
    	    _trx_traffic_maxtime = new HashMap();
    	    _trx_traffic_pertime = new HashMap();
    	    _trx_traffic_issuccess = new HashMap();
    }

    public void ophouden() {
    	_ti_reporter._keep_running = false;
    	_ti_reporter.interrupt();
    	
    	logger.info("Benchmark Poin SMS ophouden");
    }
    
    public synchronized void _telnet_in_tick_it() {
        //_ticker.incrementAndGet();
    	INSTANCE._in_telnet_ticker++;
    }
    
    public synchronized void _sms_queue_in_tick_it() {
        //_ticker.incrementAndGet();
    	INSTANCE._in_main_ticker++;
    }
 
    public synchronized void _sms_queue_out_tick_it() {
        //_ticker.incrementAndGet();
    	INSTANCE._out_main_ticker++;
    }
    
    
    public static synchronized void _secondary_queue_in_tick_it() {
        //_ticker.incrementAndGet();
    	INSTANCE._in_tweede_ticker++;
    }
 
    public static synchronized void _secondary_queue_out_tick_it() {
        //_ticker.incrementAndGet();
    	INSTANCE._out_tweede_ticker++;
    }
 
    public static synchronized void _logger_queue_in_tick_it() {
        //_ticker.incrementAndGet();
    	INSTANCE._logger_in_ticker++;
    }
    public static synchronized void _logger_queue_out_tick_it() {
        //_ticker.incrementAndGet();
    	INSTANCE._logger_out_ticker++;
    }
 
    public static synchronized void _krome_queue_in_counting() {
        //_ticker.incrementAndGet();
    	INSTANCE._krome_in_ticker++;
    }

    public static synchronized void _krome_queue_out_counting() {
        //_ticker.incrementAndGet();
    	INSTANCE._krome_out_ticker++;
    }

    public static synchronized void _krome_queue_done_counting() {
        //_ticker.incrementAndGet();
    	INSTANCE._krome_done_ticker++;
    }
    
    public static synchronized void _unifier_queue_in_counting() {
        //_ticker.incrementAndGet();
    	INSTANCE._unifier_in_ticker++;
    }

    public static synchronized void _unifier_queue_out_counting() {
        //_ticker.incrementAndGet();
    	INSTANCE._unifier_out_ticker++;
    }
    
    public synchronized void _trumb_hit_tick_it() {
        //_ticker.incrementAndGet();
    	INSTANCE._trumb_hit_ticker++;
    }
    public synchronized void _poinmo_hit_tick_it() {
    	INSTANCE._poinmo_hit_ticker++;
    }
    public static synchronized void _trx_traffic_begin_tick(String key) {
    	Long val = INSTANCE._trx_traffic_in.get(key);
    	val = (val != null)? val+1 : 1l;
    	INSTANCE._trx_traffic_in.put(key, val);
    }
    public static synchronized void _trx_traffic_end_tick(String key, long dif_time, boolean is_success) {
    	Long tot = INSTANCE._trx_traffic_pertime.get(key);
		tot = (tot != null)? tot : 0l;
		Long dif = INSTANCE._trx_traffic_diftime.get(key);
    	dif = (dif != null)? dif : 0l;
    	Long max = INSTANCE._trx_traffic_maxtime.get(key);
    	max = (max != null)? max : 0l;
    	Long min = INSTANCE._trx_traffic_mintime.get(key);
    	min = (min != null)? min : 0l;
    	Long issuccess = INSTANCE._trx_traffic_issuccess.get(key);
    	issuccess = (issuccess != null)? issuccess : 0l;
    	//update var
    	tot = tot+1;
    	dif = dif+dif_time; //in ms
    	max = (max > dif_time)? max : dif_time; //in ms
    	min = (min != 0 && min < dif_time)? min : dif_time; //in ms
    	issuccess = issuccess+1;
    	
    	
	    	INSTANCE._trx_traffic_diftime.put(key, dif);
	    	INSTANCE._trx_traffic_maxtime.put(key, max);
	    	INSTANCE._trx_traffic_mintime.put(key, min);
	    	INSTANCE._trx_traffic_pertime.put(key, tot);
	    	INSTANCE._trx_traffic_issuccess.put(key,issuccess);
    }
    public static synchronized void _trx_traffic_end_tick(String key, long dif_time) {
    	String val = INSTANCE._trx_traffic_out.get(key);
    	if(val == null) val = "0|0|0|0";
    	String[] parse = val.split("[|]");
    	
    	//format tot|dif|min|max
    	long tot = (parse.length > 0 && parse[0] != null)? parseLong(parse[0]):0;
    	long dif = (parse.length > 1 && parse[1] != null)? parseLong(parse[1]):0;
    	long max = (parse.length > 2 && parse[2] != null)? parseLong(parse[2]):0;
    	long min = (parse.length > 3 && parse[3] != null)? parseLong(parse[3]):0;
    	
    	//update var
    	tot = tot+1;
    	dif = dif+dif_time; //in ms
    	max = (max > dif_time)? max : dif_time; //in ms
    	min = (min != 0 && min < dif_time)? min : dif_time; //in ms
    	
    	
//    	val = (val != null)? val+1 : 1l;
    	INSTANCE._trx_traffic_out.put(key, tot+"|"+dif+"|"+max+"|"+min);
    }
    public static int parseInt(String str){
		try {
			return Integer.parseInt(str != null && !"".equalsIgnoreCase(str) ? str:"0");
		} catch (Exception e) {
//			System.out.println(e.toString());
		}
		return 0;
	}
	public static long parseLong(String str){
		try {
			return Long.parseLong(str != null && !"".equalsIgnoreCase(str) ? str:"0");
		} catch (Exception e) {
//			System.out.println(e.toString());
		}
		return 0;
	}
    
    

    
    public static void set_timer(int timer) {
    	INSTANCE._timer = timer;
    }

    public static void set_id(String id) {
    	INSTANCE.id = id;
    }
    
//    public static Map<String, String> get_transaction_traffic() {
//		  return INSTANCE._transaction_traffic;
//		}
}


