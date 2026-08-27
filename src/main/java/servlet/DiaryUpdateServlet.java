package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.DiaryDAO;
import model.Diary;
import model.User;

@WebServlet("/DiaryUpdateServlet")
public class DiaryUpdateServlet extends HttpServlet {
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

        int id = Integer.parseInt(request.getParameter("id"));
        String date = request.getParameter("date");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // ログインユーザーのIDを含めて Diary インスタンスを作成 (id, userId, title, content, date)
        Diary diary = new Diary(id, loginUser.getId(), title, content, date);

        // DBのデータを更新する
        DiaryDAO dao = new DiaryDAO();
        dao.update(diary);

        response.sendRedirect("index.jsp");
    }
}