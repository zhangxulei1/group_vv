package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.InteractBean;
import service.Service;

/**
 * Servlet implementation class InteractServlet
 */
@WebServlet("/InteractServlet")
public class InteractServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InteractServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 得到从客户端传来的动态信息
		InteractBean interact = new InteractBean();
		System.out.println("daolezhele ");
		String userName = request.getParameter("userName");
		userName = new String(userName.getBytes("UTF-8"),"UTF-8");
		String userTouxiang = request.getParameter("userTouxiang");
		String interactTime = request.getParameter("interactTime");
		String interactContent = request.getParameter("interactContent");
		String interactPhoto = request.getParameter("interactPhoto");
		String interactPraise = request.getParameter("interactPraise");
		interact.setUserName(userName);
		interact.setUserTouxiang(userTouxiang);
		interact.setInteractTime(interactTime);
		interact.setInteractContent(interactContent);
		interact.setInteractPhoto(interactPhoto);
		interact.setInteractPraise(interactPraise);
		//新建服务器对象
		Service service = new Service();
		//把数据放入数据库
		boolean interactRet = service.putInteract(interact);
		if(interactRet) {
			System.out.println("interacctRet success");
		}else {
			System.out.println("interactRet failure");
		}
		//返回信息到客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		if(interactRet) {
			out.print("true");
		}else {
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
