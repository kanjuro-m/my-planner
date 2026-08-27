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

@WebServlet("/DiaryServlet")
public class DiaryServlet extends HttpServlet {
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
        String content = request.getParameter("content");

        // ログインユーザーのIDを含めてDiaryインスタンスを作成 (userId, title, content, date)
        Diary diary = new Diary(loginUser.getId(), title, content, date);

        // データベースに保存する
        DiaryDAO dao = new DiaryDAO();
        dao.create(diary);

        // 一覧画面（index.jsp）に戻る
        response.sendRedirect("index.jsp");
    }
}