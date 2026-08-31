<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新規登録 - MY PLANNER</title>
    <script>
        if (localStorage.getItem('theme') === 'dark') {
            document.documentElement.setAttribute('data-theme', 'dark');
        }
    </script>
    <style>
        :root {
            --bg-app: #f8f9fa;
            --bg-card: #ffffff;
            --text-main: #18181b;
            --text-sub: #52525b;
            --border-color: #e4e4e7;
            --border-input: #d4d4d8;
            --accent-color: #18181b;
            --btn-text: #ffffff;
            --shadow-md: 0 4px 6px -1px rgb(0 0 0 / 0.1);
            --error-color: #ef4444;
        }

        [data-theme="dark"] {
            --bg-app: #09090b;
            --bg-card: #18181b;
            --text-main: #f4f4f5;
            --text-sub: #a1a1aa;
            --border-color: #27272a;
            --border-input: #3f3f46;
            --accent-color: #f4f4f5;
            --btn-text: #18181b;
        }

        * { box-sizing: border-box; }
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
            background-color: var(--bg-app);
            color: var(--text-main);
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .card {
            background-color: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 12px;
            padding: 32px 28px;
            width: 100%;
            max-width: 360px;
            box-shadow: var(--shadow-md);
        }

        .brand-logo {
            font-size: 1.25rem;
            font-weight: 800;
            letter-spacing: 0.05em;
            text-align: center;
            margin-bottom: 6px;
        }

        .page-subtitle {
            font-size: 0.85rem;
            color: var(--text-sub);
            text-align: center;
            margin-bottom: 24px;
            font-weight: 600;
        }

        .form-group {
            margin-bottom: 16px;
        }

        label {
            font-size: 0.8rem;
            font-weight: 600;
            color: var(--text-sub);
            display: block;
            margin-bottom: 6px;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            border: 1px solid var(--border-input);
            border-radius: 6px;
            padding: 8px 12px;
            background-color: var(--bg-card);
            color: var(--text-main);
            font-size: 0.85rem;
            outline: none;
            transition: border-color 0.15s;
        }

        input[type="text"]:focus, input[type="password"]:focus {
            border-color: var(--accent-color);
        }

        .btn-submit {
            width: 100%;
            background-color: var(--accent-color);
            color: var(--btn-text);
            border: none;
            padding: 10px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
            font-size: 0.85rem;
            margin-top: 8px;
            transition: opacity 0.15s;
        }

        .btn-submit:hover {
            opacity: 0.85;
        }

        .error-message {
            color: var(--error-color);
            font-size: 0.75rem;
            margin-bottom: 12px;
            text-align: center;
        }

        .link-wrapper {
            margin-top: 16px;
            text-align: center;
            font-size: 0.8rem;
        }

        .link-wrapper a {
            color: var(--text-main);
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <div class="card">
        <div class="brand-logo">MY PLANNER</div>
        <div class="page-subtitle">新規アカウント作成</div>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="error-message"><%= error %></div>
        <% } %>

        <form action="RegisterServlet" method="post">
            <div class="form-group">
                <label for="username">ユーザー名</label>
                <input type="text" id="username" name="username" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">パスワード</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">パスワード（確認用）</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <button type="submit" class="btn-submit">登録する</button>
        </form>

        <div class="link-wrapper">
            既にお持ちの方は <a href="login.jsp">ログインへ</a>
        </div>
    </div>

</body>
</html>