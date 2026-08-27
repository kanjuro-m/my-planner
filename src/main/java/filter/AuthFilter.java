package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();

        // 未ログインでもアクセスを許可するファイル・機能
        boolean isPublicPage = uri.endsWith("login.jsp") || 
                               uri.endsWith("register.jsp") || 
                               uri.endsWith("LoginServlet") || 
                               uri.endsWith("RegisterServlet") ||
                               uri.contains(".css") || 
                               uri.contains(".js") ||
                               uri.contains(".png");

        // ログイン状態の判定
        boolean isLoggedIn = (session != null && session.getAttribute("loginUser") != null);

        if (isLoggedIn || isPublicPage) {
            // ログイン済み、または除外ページの場合はそのまま通過
            chain.doFilter(request, response);
        } else {
            // 未ログインの場合はログイン画面へリダイレクト
            res.sendRedirect(req.getContextPath() + "/login.jsp");
        }
    }
}