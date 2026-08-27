package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UserDAO;
import model.User;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 入力値チェック
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "ユーザー名とパスワードを入力してください。");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // --- デバッグ用ログ出力 ---
        System.out.println("=== ログイン試行 ===");
        System.out.println("入力されたユーザー名: [" + username + "]");
        System.out.println("入力されたパスワード: [" + password + "]");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByUsername(username.trim()); // trim()で余計な空白を削除

        if (user == null) {
            System.out.println("結果: DBに指定されたユーザーが存在しません。");
        } else {
            System.out.println("結果: DBから取得成功 -> DBのパスワード: [" + user.getPassword() + "]");
        }
        System.out.println("==================");

        // ★ 判定処理（ここを修正しました）
        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            
            // ユーザー情報（Userオブジェクト）をセッションに保持
            session.setAttribute("loginUser", user);
            
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("error", "ユーザー名またはパスワードが正しくありません。");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}