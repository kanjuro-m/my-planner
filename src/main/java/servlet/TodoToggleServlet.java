package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.TodoDAO;
import model.User;

@WebServlet("/TodoToggleServlet")
public class TodoToggleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        // セッションからログインユーザー情報を取得
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        String idStr = request.getParameter("id");
        
        if (loginUser != null && idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                TodoDAO dao = new TodoDAO();
                // TODO ID と ユーザー ID の両方を渡して状態切替
                dao.toggleTodo(id, loginUser.getId());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("index.jsp");
    }
}