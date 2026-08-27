<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<body>
    <h1>日記の投稿完了</h1>

    <p>以下の内容で受け付けました。</p>

    <div>
        <strong>タイトル：</strong> ${diary.title}
    </div>
    <div>
        <strong>本文：</strong> ${diary.content}
    </div>

    <br>
    <a href="index.jsp">入力画面に戻る</a>
</body>
</body>
</html>