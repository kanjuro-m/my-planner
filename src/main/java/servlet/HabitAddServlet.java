package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.HabitDAO;
import model.Habit;

@WebServlet("/HabitAddServlet")
public class HabitAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");

        if (name != null && !name.trim().isEmpty()) {
            Habit habit = new Habit(name, false);
            HabitDAO dao = new HabitDAO();
            dao.create(habit);
        }

        response.sendRedirect("index.jsp");
    }
}