package com.solusi247.poin.statusKeyword.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.solusi247.benchmark.trx.CountTrx;
import com.solusi247.poin.data.PoinApiAccess;
import com.solusi247.poin.iprestrict.InjectApiIpRestrictSingleton;
import com.solusi247.poin.util.TselpoinIDGenerator;
/**
 * Servlet implementation class checkKeyword
 */
@WebServlet("/checkKeywordv2")
public class CheckKeywordV2Servlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private DataSource ds = null;
	private static Logger logger = Logger.getLogger(CheckKeywordV2Servlet.class);
	private String _default_channel_name = "UMBREQ";
	
	private Map<String,PoinApiAccess> _access_list  = null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckKeywordV2Servlet() 
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
		InitialContext ictx = null;
		try {
			ictx = new InitialContext();
			ds = (DataSource) ictx.lookup("java:/JNDITselpoinDS");
			_access_list = InjectApiIpRestrictSingleton.get_access_list_ip();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() 
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//declare
		long msgid= TselpoinIDGenerator.getNextTselpoinID();
		CallableStatement st = null;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		long start_time, end_time, dif_time;
		boolean is_success = false;
		String statusResult, stockResult, responseOut, ip_addr, ip_f5, TAG, _p_channel, password, username, msisdn, keyword;
		
		//initial
		start_time = System.currentTimeMillis();
		java.sql.Connection conn = null;
		ip_addr = request.getRemoteAddr();
		ip_f5 = ip_addr;
		TAG = "";
		responseOut = "NOK";
		
		//GET PARAMETER
		keyword = request.getParameter("keyword");
		msisdn = request.getParameter("msisdn");
		username = getRequestParameter(request,"username");
		password = getRequestParameter(request,"password");
		_p_channel = request.getParameter("channel");
		if (_p_channel==null || "".equalsIgnoreCase(_p_channel)) 
			_p_channel = this._default_channel_name;
		
		try {
			if(!"".equals(request.getHeader("X-Forwarded-For")) && request.getHeader("X-Forwarded-For") != null)
			{
			  ip_addr = request.getHeader("X-Forwarded-For");
			}
			logger.info(ip_addr+"|"+ip_f5+"|"+msgid+" requesting "+request.getQueryString() );
		
			boolean isAuth = false;
			TAG = ip_addr+"|"+msgid+"|"+msisdn+"|"+_p_channel;
			
			//BENCHMARK BEGIN
			CountTrx._trx_traffic_begin_tick("API2|"+_p_channel+"|"+ip_addr);
		
			PoinApiAccess acc = (PoinApiAccess)this._access_list.get(ip_addr);
			if (acc!=null 
					&& acc.get_ip_addr().equals(ip_addr)
					&& acc.get_pPass().equals(password)
					&& acc.get_pUid().equals(username)
					){
				isAuth = true;
			}
			isAuth = true;
			
			//validasi user/password
			if (!isAuth){
				statusResult = "8";
				responseOut = statusResult+"|Authentication failed";
			}
			else{
				//create or replace procedure proc_val_quota_by_location (p_msisdnx in varchar2, p_keywordx in varchar2, p_status out number, p_stock out varchar2)
				String q_sql = "{CALL proc_val_quota_by_location(?,? ,?,?)}";
					//HIT DB
					try {
						conn = ds.getConnection();
						st = conn.prepareCall(q_sql);
						st.setString(1, msisdn);
						st.setString(2, keyword);
						st.registerOutParameter(3, Types.VARCHAR);
						st.registerOutParameter(4, Types.VARCHAR);
						boolean run = st.execute();
						statusResult = st.getString(3);
						stockResult = st.getString(4);
						responseOut = statusResult+"|"+stockResult;
						is_success = true;
					} catch (SQLException e) {
						logger.error(TAG+" "+ e.getMessage());
						for (StackTraceElement se: e.getStackTrace()) {
							logger.error(TAG+" at "+ se);
						}
					}finally {
						try {if (conn != null) conn.close(); } catch (SQLException e) {}
						try {if (st != null) st.close();} catch (Exception e2) {}
					}
			}
		} catch (Exception e) {
			logger.error(TAG+" "+ e.getMessage());
			for (StackTraceElement se: e.getStackTrace()) {
				logger.error(TAG+" at "+ se);
			}
		} finally{
			end_time = System.currentTimeMillis();
			dif_time = end_time - start_time;
			CountTrx._trx_traffic_end_tick("API2|"+_p_channel+"|"+ip_addr,dif_time);
			logger.info(TAG+"|"+dif_time+" resp "+responseOut);
			out.println(responseOut);
			try {out.flush();} catch (Exception e) {}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public String getRequestParameter(HttpServletRequest request, String parameter){
	    for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
	        if(entry.getKey().equalsIgnoreCase(parameter)){
	            return entry.getValue()[0];
	        }
	    }
	    return null;
	}

}
