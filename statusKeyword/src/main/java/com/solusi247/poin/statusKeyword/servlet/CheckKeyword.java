package com.solusi247.poin.statusKeyword.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import com.solusi247.poin.iprestrict.InjectApiIpRestrictSingleton;
import com.solusi247.poin.util.TselpoinIDGenerator;

import org.jboss.logging.Logger;

/**
 * Servlet implementation class checkKeyword
 */
@WebServlet("/checkKeyword")
public class CheckKeyword extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private DataSource ds = null;
	private static Logger logger = Logger.getLogger(CheckKeyword.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckKeyword() 
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
		// _queue = new LinkedBlockingQueue(100);
		ds = (DataSource) ictx.lookup("java:/JNDITselpoinDS");

		// _access_list = InjectApiSingleton.get_access_list();
		// _access_list = InjectApiIpRestrictSingleton.get_access_list_ip();


		// benchmark = SmauloopBM.getInstance();
		// benchmark = PoinSms.getInstance();
		//openJmsConnection();
		} catch (NamingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (Exception e) 
		  {
			// TODO Auto-generated catch block
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
		long msgid= TselpoinIDGenerator.getNextTselpoinID();
		CallableStatement st = null;
		
		java.sql.Connection conn = null;
		String ip_addr = request.getRemoteAddr();
		String ip_f5 = ip_addr;
		logger.info(ip_addr+" ip_f5="+ip_f5+" msgid="+msgid+" requesting "+request.getQueryString() );
		
		if(!"".equals(request.getHeader("X-Forwarded-For")) && request.getHeader("X-Forwarded-For") != null)
		{
		  ip_addr = request.getHeader("X-Forwarded-For");
		}
		String keyword = request.getParameter("keyword");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String statusResult = "";
		String q_sql = "{CALL proc_val_takeout(?,?)}";
		try {
			conn = ds.getConnection();
			st = conn.prepareCall(q_sql);
			st.setString(1, keyword);
			st.registerOutParameter(2, Types.VARCHAR);
			boolean run = st.execute();
			statusResult = st.getString(2);
			logger.info(keyword+" "+" msgid= "+msgid+" "+statusResult);  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (conn != null) {
				conn.close();
				}
				if (st != null) {
				st.close();
				}
				} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
				}
		//execute procedure
		out.println(statusResult);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
