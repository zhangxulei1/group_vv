package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.Service;

/**
 * Servlet implementation class LogLet
 */
@WebServlet("/LogLet")
public class LogLet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogLet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 接受客户端信息
				String userphone = request.getParameter("userphone");
				userphone = new String(userphone.getBytes("ISO-8859-1"), "UTF-8");
				String password = request.getParameter("password");
				password = new String(password.getBytes("ISO-8859-1"), "UTF-8");
				System.out.println(userphone + ":" + password);

				// 新建服务对象
				Service service = new Service();

				// 验证处理
				boolean log = service.login(userphone, password);
				if (log) {
					System.out.println("log success");
					// request.getSession().setAttribute("username", username);
				} else {
					System.out.println("log fail");
				}

				// 返回信息到客户端
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				if (log) {
//					out.print("手机号：" + userphone);
//					out.print("密码： " + password);
					out.print("true");

				} else {
					out.print("false");
				}
				out.flush();
				out.close();
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
