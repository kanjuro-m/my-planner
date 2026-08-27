package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDAO;
import model.User;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // 空文字チェック
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "ユーザー名とパスワードを入力してください。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // パスワード再入力一致チェック
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "パスワードが一致しません。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        
        // ユーザー名重複チェック
        if (userDAO.findByUsername(username) != null) {
            request.setAttribute("error", "このユーザー名は既に登録されています。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // DBへ登録処理
        User newUser = new User(username, password);
        if (userDAO.registerUser(newUser)) {
            request.setAttribute("message", "アカウントが作成されました。ログインしてください。");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "登録処理に失敗しました。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}