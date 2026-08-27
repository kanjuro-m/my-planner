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

@WebServlet("/TodoAddServlet")
public class TodoAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        // セッションからログインユーザー情報を取得
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        // 未ログインの場合はログイン画面にリダイレクト
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String task = request.getParameter("task");

        // タスク名が入力されているか判定してDBに保存
        if (task != null && !task.trim().isEmpty()) {
            TodoDAO dao = new TodoDAO();
            // ユーザーIDとタスク名を渡してDBに保存
            dao.addTodo(loginUser.getId(), task.trim());
        }

        response.sendRedirect("index.jsp");
    }
}