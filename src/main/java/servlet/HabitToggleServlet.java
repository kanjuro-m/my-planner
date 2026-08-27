package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.HabitDAO;

@WebServlet("/HabitToggleServlet")
public class HabitToggleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String date = request.getParameter("date");
        String completedStr = request.getParameter("completed");
        
        if (idStr != null && date != null && completedStr != null) {
            int id = Integer.parseInt(idStr);
            int completed = Integer.parseInt(completedStr);
            HabitDAO dao = new HabitDAO(); // インポートを追加したことでここでエラーが出なくなります
            dao.saveCheck(id, date, completed);
        }
    }
}