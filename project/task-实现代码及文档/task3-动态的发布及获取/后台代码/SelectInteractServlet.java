package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import bean.InteractBean;
import service.Service;

/**
 * Servlet implementation class SelectInteractServlet
 */
@WebServlet("/SelectInteractServlet")
public class SelectInteractServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectInteractServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Service service = new Service();
		List<InteractBean> list = service.interactSelect();
		
		JSONArray array = new JSONArray();
		for(InteractBean interacts : list) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			System.out.println(interacts.getInteractContent());
			
			JSONObject obj1 = new JSONObject();
			obj1.put("interactId", interacts.getInteactId());
			obj1.put("userName", interacts.getUserName());
			obj1.put("userTouxiang", interacts.getUserTouxiang());
			obj1.put("interactTime", interacts.getInteractTime());
			obj1.put("interactContent",interacts.getInteractContent());
			obj1.put("interactPhoto", interacts.getInteractPhoto());
			obj1.put("interactPraise", interacts.getInteractPraise());
			System.out.println(obj1.length());
			array.put(obj1);
			
		}
		response.getWriter().append(array.toString());
		System.out.println(array.toString());
		System.out.println("hahahahha");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
