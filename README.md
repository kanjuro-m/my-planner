# MY PLANNER（スケジュール・TODOリスト・日記管理Webアプリ）

ログインユーザーごとに「カレンダー予定」「TODOリスト」「日記」を統合管理できるWebアプリケーションです。

---

## 💡 概要・目的

日常のタスクや予定、日記を1つの画面でストレスなく管理することを目的に作成しました。
画面を切り替えずに操作できるポップアップ画面（モーダル）や、サイドバーでのスムーズな画面表示切り替えにより、直感的な操作感を実現しています。

---

## ✨ 主な機能

- **ユーザー認証機能**
  - ログイン / ログアウト（セッション管理）
  - ログインユーザーごとのデータ表示切替

- **カレンダー・予定管理機能**
  - 月間カレンダー表示（前月/次月切替）
  - マス目クリックによる予定追加（ポップアップ表示）
  - 予定の削除機能（文字溢れ防止・スクロール表示対応）
  - 日記が存在する日のインジケーター（ドット）表示

- **TODOリスト機能**
  - タスクの追加 / 完了切り替え（打ち消し線表示）/ 削除
  - 非同期通信（Fetch API）による完了状態の即時更新

- **日記機能**
  - 日記の新規投稿（アコーディオン形式でのフォーム開閉）
  - 過去の日記一覧表示（改行保持表示対応）
  - 日記の編集 / 削除機能

- **UI / UX**
  - サイドバーによる画面表示切替（ホーム / カレンダー / TODO / 日記）
  - ダークモード / ライトモード切替（LocalStorageによる設定保存）

---

## 🛠 使用技術（技術スタック）

| カテゴリ | 技術・ライブラリ |
| :--- | :--- |
| **フロントエンド** | HTML5, CSS3, JavaScript (Vanilla JS) |
| **バックエンド** | Java 17 / JSP & Servlet |
| **データベース** | MySQL (または使用しているDB名) |
| **Webサーバー** | Apache Tomcat 10 |
| **ビルド/環境** | Eclipse / Git / GitHub |

---

## 🗄 データベース設計（ER構造）

### 1. `users` テーブル（ユーザー情報）
- `id` (INT, PK, AUTO_INCREMENT)
- `username` (VARCHAR)
- `password` (VARCHAR)

### 2. `schedules` テーブル（予定）
- `id` (INT, PK, AUTO_INCREMENT)
- `user_id` (INT, FK)
- `date` (DATE)
- `title` (VARCHAR)

### 3. `todos` テーブル（TODO）
- `id` (INT, PK, AUTO_INCREMENT)
- `user_id` (INT, FK)
- `title` (VARCHAR)
- `is_completed` (BOOLEAN)

### 4. `diaries` テーブル（日記）
- `id` (INT, PK, AUTO_INCREMENT)
- `user_id` (INT, FK)
- `date` (DATE)
- `title` (VARCHAR)
- `content` (TEXT)

---

## 📷 画面イメージ

### 新規登録画面
<img width="1919" height="986" alt="スクリーンショット 2026-08-31 221026" src="https://github.com/user-attachments/assets/f1ca0a4b-a077-4cd1-8e31-3328a8f9f859" />

### ログイン画面
<img width="1919" height="987" alt="スクリーンショット 2026-08-31 221143" src="https://github.com/user-attachments/assets/323cea74-163e-4811-ba6c-8347c46a1a1e" />

### メイン画面
<img width="1919" height="986" alt="スクリーンショット 2026-08-31 222928" src="https://github.com/user-attachments/assets/e46c5828-4791-4445-8c41-9f93fa2bf9a9" />

### カレンダー
<img width="1919" height="987" alt="スクリーンショット 2026-08-31 223025" src="https://github.com/user-attachments/assets/d85a6c4e-1c69-4f08-94cc-e501ef83c2df" />

### TODOリスト
<img width="1919" height="985" alt="スクリーンショット 2026-08-31 223106" src="https://github.com/user-attachments/assets/07f92106-9340-4cd0-9204-5109cd0b4ba9" />

### 日記
<img width="1918" height="986" alt="スクリーンショット 2026-08-31 223132" src="https://github.com/user-attachments/assets/8aa5e786-97cc-40f6-8c68-dc4ae1ee3029" />

### ダークモード使用例
<img width="1919" height="983" alt="スクリーンショット 2026-08-31 223158" src="https://github.com/user-attachments/assets/eaf14393-fa4b-438c-8385-9d2137a2cd3f" />

---

## 📊 ER図

```mermaid
erDiagram
    users ||--o{ schedules : "1対多"
    users ||--o{ todos     : "1対多"
    users ||--o{ diaries   : "1対多"

    users {
        int id PK
        string username
        string password
    }
    schedules {
        int id PK
        int user_id FK
        date date
        string title
    }
    todos {
        int id PK
        int user_id FK
        string title
        boolean is_completed
    }
    diaries {
        int id PK
        int user_id FK
        date date
        string title
        text content
    }
```

---

## 🔄 画面遷移図

```mermaid
graph TD
    A[新規登録画面] -->|登録成功| B[ログイン画面]
    B -->|ログイン成功| C[メイン画面]
    
    C -->|タブ切替| D[カレンダー表示]
    C -->|タブ切替| E[TODOリスト表示]
    C -->|タブ切替| F[日記表示]
    C -->|ボタン押下| G[新規予定/TODO/日記追加モーダル]
    C -->|トグル切り替え| H[ダークモード表示]
    
    C -->|ログアウト| B
```

---

## 🏗 システム構成図

```mermaid
graph LR
    Client[ブラウザ<br/>HTML / CSS / JS] -->|HTTPリクエスト| Server[Web・Appサーバー<br/>Apache Tomcat]
    Server -->|レスポンス| Client
    Server --- Logic[アプリロジック<br/>Java / JSP / Servlet]
    Logic <-->|JDBC| DB[(データベース<br/>MySQL)]
```
