package demo.web;

import demo.impl.MessageWall_and_RemoteLogin_Impl;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ControllerServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
        String view = perform_action(request);
        forwardRequest(request, response, view);
    }

    protected String perform_action(HttpServletRequest request)
        throws IOException, ServletException {
        
        String serv_path = request.getServletPath();
        HttpSession session = request.getSession();

        if (serv_path.equals("/login.do")) {
            
            //TODO CONTROL USER AND PASSWORD
            
            MessageWall_and_RemoteLogin_Impl msgrl = (MessageWall_and_RemoteLogin_Impl) request.getServletContext().getAttribute("remoteLogin");
            UserAccess access = msgrl.connect(request.getParameter("user"), request.getParameter("password"));
            if (access==null){
                request.setAttribute("error", "User or password incorrect!");
                return "/error-not-loggedin.html";
            } else {
                session.setAttribute("useraccess", access);
                return "/view/wallview.jsp";
            }
            
        } 
        
        else if (serv_path.equals("/put.do")) {
            MessageWall_and_RemoteLogin_Impl msgrl = (MessageWall_and_RemoteLogin_Impl) request.getServletContext().getAttribute("remoteLogin");
            UserAccess access = (UserAccess) request.getSession().getAttribute("useraccess");
            String message = request.getParameter("msg");
            access.put(message);
            return "/view/wallview.jsp";
        
        } 
        
        else if (serv_path.equals("/delete.do")) {
            MessageWall_and_RemoteLogin_Impl msgrl = (MessageWall_and_RemoteLogin_Impl) request.getServletContext().getAttribute("remoteLogin");
            UserAccess access = (UserAccess) request.getSession().getAttribute("useraccess");
            String index = request.getParameter("index");
            access.delete(Integer.parseInt(index));
            return "/view/wallview.jsp";        
        } 
        
        else if (serv_path.equals("/refresh.do")) {
            return "/view/wallview.jsp";
            
        } 
        
        else if (serv_path.equals("/logout.do")) {
            //...
            return "/goodbye.html";
        } 
        
        else {
            return "/error-bad-action.html";
        }
    }

    public RemoteLogin getRemoteLogin() {
        return (RemoteLogin) getServletContext().getAttribute("remoteLogin");
    }
    public void forwardRequest(HttpServletRequest request, HttpServletResponse response, String view) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        if (dispatcher == null) {
            throw new ServletException("No dispatcher for view path '"+view+"'");
        }
        dispatcher.forward(request,response);
    }
}


