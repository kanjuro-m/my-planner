package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ScheduleDAO;
import model.Schedule;
import model.User;

@WebServlet("/ScheduleAddServlet")
public class ScheduleAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        String date = request.getParameter("date");
        String title = request.getParameter("title");

        if (date != null && !date.trim().isEmpty() && title != null && !title.trim().isEmpty()) {
            // ログインユーザーIDを含めて Schedule インスタンスを作成 (userId, date, title)
            Schedule schedule = new Schedule(loginUser.getId(), date, title);
            ScheduleDAO dao = new ScheduleDAO();
            dao.insert(schedule);
        }

        response.sendRedirect("index.jsp");
    }
}