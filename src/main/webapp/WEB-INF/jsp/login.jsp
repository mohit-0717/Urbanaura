<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UrbanAura | Sign In</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg: #eef2f5;
            --card: rgba(255, 255, 255, 0.84);
            --text: #1d1d1f;
            --muted: #6e6e73;
            --accent: #0f9d8a;
            --border: rgba(255, 255, 255, 0.35);
            --success: #0f9f6e;
            --danger: #c93b32;
        }
        * { box-sizing: border-box; font-family: 'Inter', sans-serif; }
        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background:
                radial-gradient(circle at top left, rgba(15,157,138,0.12), transparent 30%),
                radial-gradient(circle at bottom right, rgba(28,120,192,0.12), transparent 28%),
                var(--bg);
            color: var(--text);
        }
        .login-shell {
            width: min(420px, calc(100vw - 32px));
            background: var(--card);
            backdrop-filter: blur(22px) saturate(180%);
            border: 1px solid var(--border);
            border-radius: 28px;
            padding: 32px;
            box-shadow: 0 20px 60px rgba(15, 23, 42, 0.12);
        }
        h1 { margin: 0 0 8px; font-size: 28px; letter-spacing: -0.03em; }
        p { margin: 0 0 24px; color: var(--muted); font-size: 14px; }
        label { display: block; margin-bottom: 8px; font-size: 13px; font-weight: 600; }
        input {
            width: 100%;
            margin-bottom: 16px;
            padding: 14px 16px;
            border-radius: 14px;
            border: 1px solid rgba(15, 23, 42, 0.08);
            background: rgba(255,255,255,0.78);
            outline: none;
        }
        input:focus { border-color: var(--accent); box-shadow: 0 0 0 4px rgba(0,113,227,0.12); }
        button {
            width: 100%;
            border: none;
            border-radius: 999px;
            padding: 14px 18px;
            background: linear-gradient(135deg, #0f9d8a, #126f97);
            color: white;
            font-weight: 600;
            cursor: pointer;
        }
        .alert {
            margin-bottom: 16px;
            padding: 12px 14px;
            border-radius: 14px;
            font-size: 13px;
        }
        .alert.error { background: rgba(201,59,50,0.12); color: var(--danger); }
        .alert.ok { background: rgba(15,159,110,0.12); color: var(--success); }
        .back-link {
            display: inline-block;
            margin-top: 18px;
            color: var(--accent);
            text-decoration: none;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="login-shell">
        <h1>UrbanAura</h1>
        <p>Authenticate to access the live Aura Pulse, AI consultation, and admin controls.</p>
        <div style="margin-bottom:24px; font-weight: 600; color: var(--accent); padding: 12px; background: rgba(15,157,138,0.06); border-radius: 12px; border: 1px solid rgba(15,157,138,0.15);">
            Access AI insights & live AQI
        </div>

        <c:if test="${showError}">
            <div class="alert error">Sign-in failed. Check your username and password.</div>
        </c:if>
        <c:if test="${loggedOut}">
            <div class="alert ok">You have been signed out.</div>
        </c:if>

        <form method="post" action="/login">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <label for="username">Username</label>
            <input id="username" name="username" type="text" autocomplete="username" required>

            <label for="password">Password</label>
            <input id="password" name="password" type="password" autocomplete="current-password" required>

            <button type="submit">Sign In</button>
        </form>

        <a href="/" class="back-link">Back to dashboard</a>
    </div>
</body>
</html>
