# egocci

アーティスト「egocci」の公式Webサイトです。
楽曲・公演スケジュール・映像・ギャラリーの紹介と、ビジネス問い合わせの受付・管理ができます。
自宅サーバー上で実際に稼働しています。
設計から開発・デプロイ・運用まで、すべて一人で担当しました。

[日本語](#日本語) | [한국어](#한국어)

---

## 日本語

### 概要

JSP/Servlet（Jakarta EE）で構築したMVC構成のWebアプリケーションです。
ファンが楽曲や公演情報を閲覧できる公開ページと、管理者がコンテンツを登録・編集できる管理機能で構成されています。

### 主な機能

**公開ページ**
- 楽曲一覧・詳細（カバー画像、各音楽配信サービスへのリンク）
- 公演スケジュール（ポスター画像、終了した公演の表示）
- 映像一覧（メイン映像の設定）
- ギャラリー
- プロジェクト紹介
- ビジネス問い合わせフォーム

**管理者機能**
- 楽曲・公演・映像・ギャラリーの登録・編集・削除
- 問い合わせメールの一覧・詳細確認、送信結果（成功・失敗）の管理
- 会員管理

### 技術スタック

| 区分 | 技術 |
|------|------|
| Backend | Java, JSP, Servlet（Jakarta EE）, JSTL, JDBC |
| Frontend | HTML, CSS, JavaScript, jQuery |
| Database | MySQL 8 |
| ライブラリ | Apache Commons DBCP2, Commons FileUpload, Jakarta Mail, JSch |
| Server | Apache Tomcat 10, Ubuntu Server, Cloudflare Tunnel, Tailscale |

### システム構成

    ブラウザ
       │ HTTPS
       ▼
    Cloudflare Tunnel
       │
       ▼
    Webサーバー（Ubuntu Server / Tomcat 10）
       │ Tailscale（プライベートネットワーク）
       ▼
    DBサーバー（Ubuntu Server）
       ├─ MySQL 8
       └─ 画像ファイル保存（SFTP）

### 設計のポイント

- **コネクションプール**：Apache Commons DBCP2でDB接続を再利用し、プールが使えない場合は通常の接続に切り替えるようにしました。
- **画像のSFTP保存とキャッシュ**：アップロードした画像はDBサーバー側にSFTPで保存し、配信時はWebサーバー側でキャッシュして、毎回SFTPに接続しないようにしました。
- **問い合わせメールの送信管理**：問い合わせ内容をDBに保存し、メール送信の成功・失敗を記録して管理画面で確認できるようにしました。
- **パスワードのハッシュ化**：会員パスワードはSHA-256でハッシュ化して保存しています。

### 運用・セキュリティ改善

- 使われていなかったファイルダウンロード用Servletを、パストラバーサル（経路操作）脆弱性の対策として削除しました。
- DB・SFTP・メールの機密情報をソースコードから分離し、サーバー上の設定ファイル（`/etc/egocci.properties`）から読み込む `AppConfig` クラスを追加しました。
- WindowsからUbuntu Serverへ移行し、Tomcatをsystemdで常時稼働させています。

### データベース設計

テーブル設計は [egoccidatabase.pdf](egoccidatabase.pdf) を参照してください。

### ローカルでの実行方法

1. Eclipseで「Dynamic Web Project」としてインポートし、Tomcat 10を設定します。
2. 以下の形式で設定ファイルを作成します。

        db.base-url=jdbc:mysql://localhost:3306/egocci_music?
        db.user=your_db_user
        db.password=your_db_password
        sftp.host=localhost
        sftp.user=your_sftp_user
        sftp.password=your_sftp_password
        mail.sender=your_address@gmail.com
        mail.password=your_gmail_app_password

3. 設定ファイルの場所は、既定では `/etc/egocci.properties` です。別の場所に置く場合は、環境変数 `EGOCCI_CONFIG` にパスを指定してください。

---

## 한국어

### 개요

아티스트 「egocci」의 공식 웹사이트입니다. JSP/Servlet(Jakarta EE) 기반 MVC 구조로, 팬이 곡·공연·영상을 볼 수 있는 공개 페이지와 관리자가 콘텐츠를 등록·수정하는 관리 기능으로 구성되어 있습니다. 현재 자택 서버에서 실제로 운영 중입니다. 설계부터 개발·배포·운영까지 혼자 담당했습니다.

### 주요 기능

- 공개 페이지: 곡 목록·상세(커버 이미지, 음원 사이트 링크), 공연 일정(포스터, 지난 공연 표시), 영상(메인 영상 설정), 갤러리, 프로젝트 소개, 비즈니스 문의 폼
- 관리자: 곡·공연·영상·갤러리 등록·수정·삭제, 문의 메일 목록·상세 및 발송 결과 관리, 회원 관리

### 기술 스택

Java / JSP / Servlet(Jakarta EE) / JSTL / JDBC / MySQL 8 / jQuery / Commons DBCP2 / Commons FileUpload / Jakarta Mail / JSch(SFTP) / Tomcat 10 / Ubuntu Server / Cloudflare Tunnel / Tailscale

### 포인트

- Commons DBCP2 커넥션 풀 적용, 풀 사용 불가 시 일반 연결로 대체
- 이미지는 SFTP로 DB 서버에 저장하고, 웹 서버에서 캐시해 매번 SFTP 접속하지 않도록 개선
- 문의 내용을 DB에 저장하고 메일 발송 성공·실패를 기록해 관리 화면에서 확인
- 회원 비밀번호 SHA-256 해시 저장
- 사용하지 않는 파일 다운로드 서블릿을 경로 조작 취약점 대책으로 삭제
- DB·SFTP·메일 비밀값을 소스에서 분리해 서버 설정 파일에서 읽도록 개선
- DB 설계서: [egoccidatabase.pdf](egoccidatabase.pdf)
