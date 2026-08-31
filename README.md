# MY PLANNER（スケジュール・TODO・日記管理Webアプリ）

ログインユーザーごとに「カレンダー予定」「TODO」「日記」を統合管理できるWebアプリケーションです。

---

## 💡 概要・目的
日常のタスクや予定、日記を1つの画面でストレスなく管理することを目的に作成しました。
モーダル表示による予定の追加や、レスポンシブなタブ切り替え機能により、直感的な操作感を実現しています。

---

## ✨ 主な機能

- **ユーザー認証機能**
  - ログイン / ログアウト（セッション管理）
  - ログインユーザーごとのデータ表示切替

- **カレンダー・予定管理機能**
  - 月間カレンダー表示（前月/次月切替）
  - マス目クリックによる予定追加（モーダル表示）
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

## 画面イメージ
<img width="1919" height="983" alt="スクリーンショット 2026-08-31 223158" src="https://github.com/user-attachments/assets/61fc5967-750a-44f7-88dc-3f9aa1912f96" />
<img width="1918" height="986" alt="スクリーンショット 2026-08-31 223132" src="https://github.com/user-attachments/assets/482b440c-2d5a-495f-86f2-566c464e824a" />
<img width="1919" height="985" alt="スクリーンショット 2026-08-31 223106" src="https://github.com/user-attachments/assets/b5369f65-0eb0-4af7-b848-818ac8576e91" />
<img width="1919" height="987" alt="スクリーンショット 2026-08-31 223025" src="https://github.com/user-attachments/assets/facd7a43-7e17-4a35-96b0-1ba8096487c3" />
<img width="1919" height="986" alt="スクリーンショット 2026-08-31 222928" src="https://github.com/user-attachments/assets/3550b876-393e-4324-88ff-0f03acad0297" />
<img width="1919" height="987" alt="スクリーンショット 2026-08-31 221143" src="https://github.com/user-attachments/assets/0e9c2f8f-7b49-4a16-852d-8f6cbb0ae814" />
<img width="1919" height="986" alt="スクリーンショット 2026-08-31 221026" src="https://github.com/user-attachments/assets/53382e85-a63b-4808-9a19-073d0c987eb3" />
