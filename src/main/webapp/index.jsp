<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.YearMonth" %>
<%@ page import="java.time.DayOfWeek" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="model.User" %>
<%@ page import="model.Diary" %>
<%@ page import="dao.DiaryDAO" %>
<%@ page import="model.Todo" %>
<%@ page import="dao.TodoDAO" %>
<%@ page import="model.Schedule" %>
<%@ page import="dao.ScheduleDAO" %>
<%
    // セッションチェック（未ログインならログイン画面へ）
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // DAO & データ取得
    DiaryDAO diaryDao = new DiaryDAO();
    List<Diary> diaryList = diaryDao.findByUserId(loginUser.getId());

    String editIdStr = request.getParameter("editId");
    Diary editDiary = null;
    if (editIdStr != null) {
        int editId = Integer.parseInt(editIdStr);
        editDiary = diaryDao.findById(editId);
    }

    TodoDAO todoDao = new TodoDAO();
    List<Todo> todoList = todoDao.getTodosByUserId(loginUser.getId());

    ScheduleDAO scheduleDao = new ScheduleDAO();

    LocalDate today = LocalDate.now();
    
    // 月間カレンダー計算
    String calYearStr = request.getParameter("calYear");
    String calMonthStr = request.getParameter("calMonth");
    
    int calYear = (calYearStr != null) ? Integer.parseInt(calYearStr) : today.getYear();
    int calMonth = (calMonthStr != null) ? Integer.parseInt(calMonthStr) : today.getMonthValue();
    
    YearMonth currentYearMonth = YearMonth.of(calYear, calMonth);
    YearMonth prevYearMonth = currentYearMonth.minusMonths(1);
    YearMonth nextYearMonth = currentYearMonth.plusMonths(1);

    LocalDate firstOfMonth = currentYearMonth.atDay(1);
    int lengthOfMonth = currentYearMonth.lengthOfMonth();
    int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue();

    List<String> diaryDatesInMonth = diaryDao.getDiaryDatesInMonth(calYear, calMonth, loginUser.getId());

    DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String defaultFormDate = (editDiary != null) ? editDiary.getDate() : today.format(dbFormatter);
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MY PLANNER</title>
    <script>
        if (localStorage.getItem('theme') === 'dark') {
            document.documentElement.setAttribute('data-theme', 'dark');
        }
    </script>
    <style>
        :root {
            --bg-app: #f8f9fa;
            --bg-sidebar: #ffffff;
            --bg-card: #ffffff;
            --text-main: #18181b;
            --text-sub: #52525b;
            --text-muted: #a1a1aa;
            --border-color: #e4e4e7;
            --border-input: #d4d4d8;
            --accent-color: #18181b;
            --accent-hover: #27272a;
            --accent-light: #f4f4f5;
            --schedule-bg: #e4e4e7;
            --schedule-text: #18181b;
            --today-bg: #f4f4f5;
            --today-border: #18181b;
            --dot-color: #18181b;
            --btn-text: #ffffff;
            --shadow-sm: 0 1px 2px 0 rgb(0 0 0 / 0.05);
            --shadow-md: 0 4px 6px -1px rgb(0 0 0 / 0.1);
        }

        [data-theme="dark"] {
            --bg-app: #09090b;
            --bg-sidebar: #18181b;
            --bg-card: #18181b;
            --text-main: #f4f4f5;
            --text-sub: #a1a1aa;
            --text-muted: #71717a;
            --border-color: #27272a;
            --border-input: #3f3f46;
            --accent-color: #f4f4f5;
            --accent-hover: #e4e4e7;
            --accent-light: #27272a;
            --schedule-bg: #27272a;
            --schedule-text: #f4f4f5;
            --today-bg: #27272a;
            --today-border: #f4f4f5;
            --dot-color: #f4f4f5;
            --btn-text: #18181b;
        }

        * { box-sizing: border-box; }
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            background-color: var(--bg-app);
            color: var(--text-main);
            margin: 0;
            padding: 0;
            display: flex;
            height: 100vh;
            overflow: hidden;
            font-size: 18px;
        }

        .sidebar {
            width: 260px;
            background-color: var(--bg-sidebar);
            border-right: 1px solid var(--border-color);
            padding: 28px 18px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            flex-shrink: 0;
        }

        .brand-logo {
            font-size: 1.6rem;
            font-weight: 800;
            letter-spacing: 0.05em;
            color: var(--text-main);
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 32px;
            padding-left: 6px;
        }

        .nav-menu {
            list-style: none;
            padding: 0;
            margin: 0;
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .nav-item {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 14px 16px;
            border-radius: 10px;
            color: var(--text-sub);
            text-decoration: none;
            font-size: 1.15rem;
            font-weight: 600;
            transition: all 0.15s ease;
            cursor: pointer;
        }

        .nav-item:hover, .nav-item.active {
            background-color: var(--accent-light);
            color: var(--text-main);
            font-weight: 800;
        }

        .theme-toggle-btn {
            background: none;
            border: 1px solid var(--border-input);
            color: var(--text-main);
            padding: 12px;
            border-radius: 10px;
            cursor: pointer;
            font-size: 1.05rem;
            font-weight: 600;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            width: 100%;
            transition: background-color 0.2s;
        }
        .theme-toggle-btn:hover {
            background-color: var(--accent-light);
        }

        .main-content {
            flex: 1;
            padding: 24px;
            display: grid;
            grid-template-columns: 1fr 450px;
            gap: 24px;
            height: 100vh;
            overflow: hidden;
        }

        .main-content.single-view {
            display: block;
        }

        .left-group, .summary-group {
            display: flex;
            flex-direction: column;
            gap: 24px;
            height: 100%;
            min-height: 0;
        }

        .card {
            background-color: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 14px;
            padding: 24px;
            box-shadow: var(--shadow-sm);
            display: flex;
            flex-direction: column;
            min-height: 0;
        }

        .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;
            border-bottom: 1px solid var(--border-color);
            padding-bottom: 12px;
            flex-shrink: 0;
        }

        .card-title {
            font-size: 1.45rem;
            font-weight: 700;
            letter-spacing: -0.01em;
            margin: 0;
        }

        .calendar-section {
            height: 100%;
        }

        .calendar-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;
            flex-shrink: 0;
        }

        .calendar-title {
            font-size: 1.6rem;
            font-weight: 800;
        }

        .calendar-nav {
            display: flex;
            gap: 10px;
        }

        .calendar-nav a {
            text-decoration: none;
            color: var(--text-main);
            padding: 8px 18px;
            border-radius: 8px;
            border: 1px solid var(--border-input);
            font-weight: bold;
            font-size: 1.1rem;
            transition: background-color 0.15s;
        }

        .calendar-nav a:hover {
            background-color: var(--accent-light);
        }

        .calendar-grid {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            grid-template-rows: auto repeat(6, 1fr);
            gap: 6px;
            flex: 1;
            min-height: 0;
        }

        .calendar-day-header {
            text-align: center;
            font-weight: 700;
            font-size: 1.05rem;
            padding: 4px 0;
            color: var(--text-sub);
        }

        .calendar-cell {
            background-color: var(--bg-app);
            border: 1px solid var(--border-color);
            border-radius: 8px;
            padding: 6px;
            display: flex;
            flex-direction: column;
            font-size: 1.1rem;
            position: relative;
            cursor: pointer;
            transition: border-color 0.15s;
            min-height: 0;
            overflow: hidden; /* はみ出しを防止 */
        }

        .calendar-cell:hover {
            border-color: var(--accent-color);
        }

        .calendar-cell.is-today {
            background-color: var(--today-bg);
            border: 2px solid var(--today-border);
        }

        .calendar-cell.empty {
            opacity: 0.25;
            background-color: transparent;
            border: none;
            cursor: default;
        }

        .cell-top {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-shrink: 0;
        }

        .cell-day-num {
            font-weight: 800;
            font-size: 1rem;
        }

        /* 予定リストのスクロール対応 */
        .cell-schedule-list {
            display: flex;
            flex-direction: column;
            gap: 3px;
            margin-top: 4px;
            overflow-y: auto; /* 2つ目以降もスクロールで表示 */
            flex: 1;
            min-height: 0;
        }

        .schedule-badge {
            background-color: var(--schedule-bg);
            color: var(--schedule-text);
            font-size: 0.8rem;
            padding: 2px 4px;
            border-radius: 4px;
            font-weight: 600;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 4px;
            width: 100%;
            box-sizing: border-box;
            flex-shrink: 0;
        }

        .schedule-badge .schedule-title {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            flex: 1;
            min-width: 0;
        }

        .schedule-badge .del-btn {
            color: inherit;
            text-decoration: none;
            font-size: 0.8rem;
            opacity: 0.75;
            flex-shrink: 0;
            padding: 0 2px;
        }
        .schedule-badge .del-btn:hover { opacity: 1; }

        .diary-dot-badge {
            width: 8px;
            height: 8px;
            background-color: var(--dot-color);
            border-radius: 50%;
        }

        .form-group { margin-bottom: 12px; }

        label {
            font-size: 1rem;
            font-weight: 700;
            color: var(--text-sub);
            display: block;
            margin-bottom: 4px;
        }

        input[type="text"], input[type="date"], textarea {
            width: 100%;
            border: 1px solid var(--border-input);
            border-radius: 8px;
            padding: 8px 12px;
            background-color: var(--bg-card);
            color: var(--text-main);
            font-family: inherit;
            font-size: 1rem;
            outline: none;
            transition: border-color 0.15s;
        }

        input[type="text"]:focus, input[type="date"]:focus, textarea:focus {
            border-color: var(--accent-color);
        }

        .btn {
            background-color: var(--accent-color);
            color: var(--btn-text);
            border: 1px solid var(--accent-color);
            padding: 8px 16px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 700;
            font-size: 1rem;
            transition: opacity 0.15s;
        }

        .btn:hover { opacity: 0.85; }

        .btn-sec {
            background-color: transparent;
            border: 1px solid var(--border-input);
            color: var(--text-main);
            padding: 6px 12px;
            border-radius: 8px;
            cursor: pointer;
            text-decoration: none;
            font-size: 0.9rem;
            font-weight: 600;
            transition: background-color 0.15s;
        }

        .btn-sec:hover { background-color: var(--accent-light); }

        #view-todo { flex: 4; }

        .task-list {
            list-style: none;
            padding: 0;
            margin: 0;
            display: flex;
            flex-direction: column;
            gap: 8px;
            flex: 1;
            overflow-y: auto;
            padding-right: 4px;
        }

        .task-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 10px 12px;
            background-color: var(--bg-app);
            border: 1px solid var(--border-color);
            border-radius: 8px;
        }

        .task-left {
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 1rem;
            font-weight: 600;
        }

        .task-left input[type="checkbox"] {
            width: 16px;
            height: 16px;
            cursor: pointer;
        }

        .completed-text {
            text-decoration: line-through;
            color: var(--text-muted);
        }

        #view-diary { flex: 6; }

        /* 日記フォーム初期非表示用スタイル */
        .diary-form-container {
            display: none;
            margin-bottom: 14px;
            border-bottom: 1px solid var(--border-color);
            padding-bottom: 14px;
        }

        .recent-box {
            flex: 1;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
            gap: 10px;
            padding-right: 4px;
        }

        .diary-card-item {
            padding: 10px 12px;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            font-size: 0.95rem;
            background-color: var(--bg-app);
        }

        .diary-card-content {
            color: var(--text-sub);
            margin-top: 4px;
            line-height: 1.4;
            white-space: pre-wrap;
            word-break: break-word;
        }

        ::-webkit-scrollbar { width: 6px; }
        ::-webkit-scrollbar-track { background: transparent; }
        ::-webkit-scrollbar-thumb {
            background: var(--border-input);
            border-radius: 4px;
        }
        ::-webkit-scrollbar-thumb:hover { background: var(--text-muted); }

        .modal-overlay {
            position: fixed;
            top: 0; left: 0; width: 100vw; height: 100vh;
            background: rgba(0, 0, 0, 0.5);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }

        .modal-card {
            background: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 14px;
            width: 420px;
            padding: 24px;
            box-shadow: var(--shadow-md);
        }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div>
            <div class="brand-logo">MY PLANNER</div>
            <ul class="nav-menu">
                <li><a class="nav-item active" data-tab="home">ホーム</a></li>
                <li><a class="nav-item" data-tab="calendar">カレンダー</a></li>
                <li><a class="nav-item" data-tab="todo">TODOリスト</a></li>
                <li><a class="nav-item" data-tab="diary">日記</a></li>
            </ul>
        </div>
        <div>
            <button type="button" class="theme-toggle-btn" onclick="toggleTheme()">
                <span id="themeIcon">🌙</span>
                <span id="themeLabel">ダークモード</span>
            </button>
            
            <div style="margin-top: 20px; padding-top: 16px; border-top: 1px solid var(--border-color);">
                <div style="font-size: 0.95rem; color: var(--text-sub); margin-bottom: 10px; text-align: center;">
                    ユーザー: <strong><%= loginUser.getUsername() %></strong>
                </div>
                <a href="LogoutServlet" class="btn-sec" style="display: block; text-align: center; text-decoration: none; padding: 10px;">
                    ログアウト
                </a>
            </div>
        </div>
    </aside>

    <main class="main-content" id="mainContent">
        
        <div class="left-group" id="leftGroup">
            <div class="card calendar-section tab-view" id="view-calendar">
                <div class="calendar-header">
                    <div class="calendar-title"><%= calYear %>年 <%= calMonth %>月</div>
                    <div class="calendar-nav">
                        <a href="index.jsp?calYear=<%= prevYearMonth.getYear() %>&calMonth=<%= prevYearMonth.getMonthValue() %>">&lt;</a>
                        <a href="index.jsp?calYear=<%= nextYearMonth.getYear() %>&calMonth=<%= nextYearMonth.getMonthValue() %>">&gt;</a>
                    </div>
                </div>

                <div class="calendar-grid">
                    <div class="calendar-day-header">月</div>
                    <div class="calendar-day-header">火</div>
                    <div class="calendar-day-header">水</div>
                    <div class="calendar-day-header">木</div>
                    <div class="calendar-day-header">金</div>
                    <div class="calendar-day-header">土</div>
                    <div class="calendar-day-header">日</div>

                    <% for (int i = 1; i < startDayOfWeek; i++) { %>
                        <div class="calendar-cell empty"></div>
                    <% } %>

                    <% for (int day = 1; day <= lengthOfMonth; day++) { 
                        String checkDate = String.format("%04d-%02d-%02d", calYear, calMonth, day);
                        boolean hasDiary = diaryDatesInMonth.contains(checkDate);
                        boolean isTodayCell = (calYear == today.getYear() && calMonth == today.getMonthValue() && day == today.getDayOfMonth());
                        
                        List<Schedule> daySchedules = scheduleDao.findByDateAndUserId(checkDate, loginUser.getId());
                    %>
                        <div class="calendar-cell <%= isTodayCell ? "is-today" : "" %>" onclick="openScheduleModal('<%= checkDate %>')">
                            <div class="cell-top">
                                <span class="cell-day-num"><%= day %></span>
                                <% if (hasDiary) { %>
                                    <div class="diary-dot-badge" title="日記あり"></div>
                                <% } %>
                            </div>
                            <div class="cell-schedule-list">
                                <% if (daySchedules != null) {
                                    for (Schedule s : daySchedules) { %>
                                        <div class="schedule-badge" title="<%= s.getTitle() %>">
                                            <span class="schedule-title"><%= s.getTitle() %></span>
                                            <a href="ScheduleDeleteServlet?id=<%= s.getId() %>" class="del-btn" onclick="event.stopPropagation(); return confirm('予定を削除しますか？');">✕</a>
                                        </div>
                                <%  } 
                                } %>
                            </div>
                        </div>
                    <% } %>
                </div>
            </div>
        </div>

        <div class="summary-group" id="summaryGroup">
            
            <div class="card tab-view" id="view-todo">
                <div class="card-header">
                    <h3 class="card-title">TODOリスト</h3>
                    <button type="button" class="btn-sec" onclick="showTodoForm()">＋ 追加</button>
                </div>

                <form action="TodoAddServlet" method="post" id="addTodoForm" style="display: none; flex-direction: column; gap: 10px; margin-bottom: 14px; flex-shrink: 0;">
                    <input type="text" name="task" placeholder="新しいTODOを入力" required>
                    <button type="submit" class="btn">保存</button>
                </form>

                <ul class="task-list">
                    <% if (todoList != null && !todoList.isEmpty()) {
                        for (Todo t : todoList) {
                            boolean checked = t.isCompleted();
                    %>
                        <li class="task-item">
                            <div class="task-left">
                                <input type="checkbox" <%= checked ? "checked" : "" %> onchange="toggleTodo(<%= t.getId() %>, this)">
                                <div>
                                    <span class="<%= checked ? "completed-text" : "" %>"><%= t.getTitle() %></span>
                                </div>
                            </div>
                            <a href="TodoDeleteServlet?id=<%= t.getId() %>" class="btn-sec" style="padding: 4px 10px;" onclick="return confirm('削除しますか？');">✕</a>
                        </li>
                    <% } } else { %>
                        <p style="color: var(--text-muted); font-size: 1rem; margin: 0;">登録されているTODOはありません。</p>
                    <% } %>
                </ul>
            </div>

            <!-- 日記カード -->
            <div class="card tab-view" id="view-diary">
                <div class="card-header">
                    <h3 class="card-title">日記</h3>
                    <button type="button" class="btn-sec" id="toggleDiaryBtn" onclick="toggleDiaryForm()">＋ 日記を書く</button>
                </div>

                <!-- 日記フォーム（初期状態は非表示） -->
                <div class="diary-form-container" id="diaryFormContainer" style="<%= (editDiary != null) ? "display: block;" : "" %>">
                    <form action="<%= (editDiary != null) ? "DiaryUpdateServlet" : "DiaryServlet" %>" method="post" style="flex-shrink: 0;">
                        <% if (editDiary != null) { %>
                            <input type="hidden" name="id" value="<%= editDiary.getId() %>">
                        <% } %>
                        <input type="hidden" name="date" value="<%= defaultFormDate %>">
                        
                        <div class="form-group">
                            <input type="text" name="title" value="<%= (editDiary != null) ? editDiary.getTitle() : "" %>" placeholder="タイトル" required>
                        </div>
                        <div class="form-group">
                            <textarea name="content" rows="3" placeholder="今日の出来事や感想..." required><%= (editDiary != null) ? editDiary.getContent() : "" %></textarea>
                        </div>
                        <div style="text-align: right;">
                            <% if (editDiary != null) { %>
                                <a href="index.jsp" class="btn-sec">キャンセル</a>
                            <% } %>
                            <button type="submit" class="btn"><%= (editDiary != null) ? "更新" : "保存" %></button>
                        </div>
                    </form>
                </div>

                <label style="flex-shrink: 0; margin-bottom: 8px;">最近の日記</label>
                <div class="recent-box">
                    <% if (diaryList != null && !diaryList.isEmpty()) {
                        for (Diary d : diaryList) { %>
                            <div class="diary-card-item">
                                <div style="font-size: 0.85rem; color: var(--text-muted);"><%= d.getDate() %></div>
                                <div style="font-weight: 700; margin-top: 2px; font-size: 1.05rem;"><%= d.getTitle() %></div>
                                <div class="diary-card-content"><%= d.getContent() %></div>
                                <div style="text-align: right; margin-top: 8px;">
                                    <a href="index.jsp?editId=<%= d.getId() %>" class="btn-sec" style="font-size: 0.85rem; padding: 3px 8px;">編集</a>
                                    <a href="DiaryDeleteServlet?id=<%= d.getId() %>" class="btn-sec" style="font-size: 0.85rem; padding: 3px 8px;" onclick="return confirm('削除しますか？');">削除</a>
                                </div>
                            </div>
                    <% } } else { %>
                        <p style="color: var(--text-muted); font-size: 1rem; margin: 0;">投稿された日記はありません。</p>
                    <% } %>
                </div>
            </div>

        </div>

    </main>

    <div class="modal-overlay" id="scheduleModal">
        <div class="modal-card">
            <h3 style="margin-top:0; font-size: 1.3rem; margin-bottom: 16px;">予定を追加</h3>
            <form action="ScheduleAddServlet" method="post">
                <div class="form-group">
                    <label>日付</label>
                    <input type="date" name="date" id="modalScheduleDate" required readonly style="background-color: var(--accent-light);">
                </div>
                <div class="form-group">
                    <label>予定のタイトル</label>
                    <input type="text" name="title" placeholder="例: 会議、旅行など" required autofocus>
                </div>
                <div style="display: flex; justify-content: flex-end; gap: 12px; margin-top: 18px;">
                    <button type="button" class="btn-sec" onclick="closeScheduleModal()">キャンセル</button>
                    <button type="submit" class="btn">追加</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function applyTheme(theme) {
            if (theme === 'dark') {
                document.documentElement.setAttribute('data-theme', 'dark');
                document.getElementById('themeIcon').textContent = '☀️';
                document.getElementById('themeLabel').textContent = 'ライトモード';
            } else {
                document.documentElement.removeAttribute('data-theme');
                document.getElementById('themeIcon').textContent = '🌙';
                document.getElementById('themeLabel').textContent = 'ダークモード';
            }
        }

        function toggleTheme() {
            const currentTheme = document.documentElement.getAttribute('data-theme');
            const newTheme = (currentTheme === 'dark') ? 'light' : 'dark';
            localStorage.setItem('theme', newTheme);
            applyTheme(newTheme);
        }

        const savedTheme = localStorage.getItem('theme') || 'light';
        applyTheme(savedTheme);

        function showTodoForm() {
            const form = document.getElementById('addTodoForm');
            form.style.display = (form.style.display === 'none') ? 'flex' : 'none';
        }

        /* 日記フォームの表示切替 */
        function toggleDiaryForm() {
            const formContainer = document.getElementById('diaryFormContainer');
            const btn = document.getElementById('toggleDiaryBtn');
            if (formContainer.style.display === 'none' || formContainer.style.display === '') {
                formContainer.style.display = 'block';
                btn.textContent = '閉じる';
            } else {
                formContainer.style.display = 'none';
                btn.textContent = '＋ 日記を書く';
            }
        }

        function openScheduleModal(dateStr) {
            document.getElementById('modalScheduleDate').value = dateStr;
            document.getElementById('scheduleModal').style.display = 'flex';
        }

        function closeScheduleModal() {
            document.getElementById('scheduleModal').style.display = 'none';
        }

        function toggleTodo(todoId, checkbox) {
            const textSpan = checkbox.closest('.task-left').querySelector('span');
            if (checkbox.checked) {
                textSpan.classList.add('completed-text');
            } else {
                textSpan.classList.remove('completed-text');
            }

            fetch('TodoToggleServlet', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'id=' + todoId
            });
        }

        function switchTab(tabName) {
            const mainContent = document.getElementById('mainContent');
            const leftGroup = document.getElementById('leftGroup');
            const summaryGroup = document.getElementById('summaryGroup');
            const viewCalendar = document.getElementById('view-calendar');
            const viewTodo = document.getElementById('view-todo');
            const viewDiary = document.getElementById('view-diary');

            document.querySelectorAll('.nav-item').forEach(item => {
                if (item.getAttribute('data-tab') === tabName) {
                    item.classList.add('active');
                } else {
                    item.classList.remove('active');
                }
            });

            if (tabName === 'home') {
                mainContent.classList.remove('single-view');
                leftGroup.style.display = 'flex';
                summaryGroup.style.display = 'flex';
                viewCalendar.style.display = 'flex';
                viewTodo.style.display = 'flex';
                viewDiary.style.display = 'flex';
            } else {
                mainContent.classList.add('single-view');
                
                viewCalendar.style.display = 'none';
                viewTodo.style.display = 'none';
                viewDiary.style.display = 'none';
                leftGroup.style.display = 'none';
                summaryGroup.style.display = 'none';

                if (tabName === 'calendar') {
                    leftGroup.style.display = 'flex';
                    viewCalendar.style.display = 'flex';
                } else if (tabName === 'todo') {
                    summaryGroup.style.display = 'flex';
                    viewTodo.style.display = 'flex';
                } else if (tabName === 'diary') {
                    summaryGroup.style.display = 'flex';
                    viewDiary.style.display = 'flex';
                }
            }
        }

        document.querySelectorAll('.nav-item').forEach(item => {
            item.addEventListener('click', function() {
                const tabName = this.getAttribute('data-tab');
                switchTab(tabName);
            });
        });

        <% if (editDiary != null) { %>
            switchTab('diary');
        <% } %>
    </script>
</body>
</html>