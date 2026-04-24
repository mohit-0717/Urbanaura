<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UrbanAura | Admin Operations</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --bg: #eef5f6; --text: #13252c; --muted: #6f8b95; --line: rgba(19,37,44,.15);
            --accent: #0f9d8a; --accent-deep: #0c8272; --surface: #ffffff;
        }
        body { background-color: var(--bg); font-family: 'Inter', sans-serif; color: var(--text); }
        .sidebar { background: var(--text); min-height: 100vh; color: #fff; padding-top: 2rem; box-shadow: 4px 0 24px rgba(0,0,0,0.1); z-index: 10;}
        .sidebar a { color: #8cadb8; text-decoration: none; padding: 14px 24px; display: block; font-weight: 500; border-radius: 12px; margin: 4px 12px; transition: all 0.2s;}
        .sidebar a:hover { color: #fff; background: rgba(255,255,255,0.1); transform: translateX(4px); }
        .card { border: none; border-radius: 20px; box-shadow: 0 12px 36px rgba(19,37,44,0.06); overflow: hidden; background: var(--surface); }
        .card-header { background: #f8fbfb; font-weight: 700; color: #163744; padding: 20px 24px; border-bottom: 1px solid var(--line); font-size: 18px; letter-spacing: -0.01em; }
        .table { margin-bottom: 0; }
        .table th { font-weight: 600; color: var(--muted); text-transform: uppercase; font-size: 12px; padding: 16px 24px; border-bottom: 1px solid var(--line); }
        .table td { padding: 16px 24px; vertical-align: middle; border-bottom: 1px solid rgba(19,37,44,0.05); }
        .table-hover tbody tr:hover { background-color: #f7fafa; }
        .btn-brand { background: linear-gradient(135deg, var(--accent), var(--accent-deep)); color: #fff; border: none; font-weight: 600; padding: 10px 20px; border-radius: 999px; box-shadow: 0 4px 12px rgba(15,157,138,0.25); transition: transform 0.2s, box-shadow 0.2s;}
        .btn-brand:hover { transform: translateY(-2px); box-shadow: 0 8px 24px rgba(15,157,138,0.35); color: #fff; }
        .btn-outline-danger { border-radius: 999px; padding: 6px 16px; font-weight: 600; font-size: 13px; }
        .badge { font-weight: 600; padding: 6px 10px; border-radius: 6px; }
    </style>
</head>
<body>
<div class="container-fluid p-0">
    <div class="row g-0">
        <!-- Sidebar -->
        <div class="col-md-2 sidebar">
            <div class="px-4 mb-5">
                <span style="display:inline-block; width:36px; height:36px; background:var(--accent); border-radius:10px; color:#fff; text-align:center; line-height:36px; font-weight:800; font-size:18px; margin-right:10px; vertical-align:middle;">UA</span>
                <span style="font-weight:800; font-size:20px; vertical-align:middle; letter-spacing:-0.03em;">Central</span>
            </div>
            <a href="/admin/dashboard" style="color:#fff; background:rgba(255,255,255,0.08);">Dashboard Registry</a>
            <a href="/admin/property/new">Add Property</a>
            <hr style="border-color:rgba(255,255,255,0.1); margin:24px;">
            <a href="/">Exit to Dashboard</a>
        </div>
        
        <!-- Main Content -->
        <div class="col-md-10" style="padding: 40px 60px; height: 100vh; overflow-y: auto;">
            <div class="d-flex justify-content-between align-items-center mb-5">
                <div>
                    <h2 style="font-weight:800; color:var(--text); letter-spacing:-0.03em; margin-bottom:4px;">Property Asset Management</h2>
                    <p style="color:var(--muted); font-size:15px; font-weight:500;">Oversee live database assets and territorial boundaries.</p>
                </div>
                <div class="d-flex gap-3">
                    <input type="text" id="adminSearch" class="form-control" placeholder="Search properties..." style="border-radius:999px; padding:10px 20px; border:1px solid var(--line); min-width:260px;" onkeyup="filterProperties()">
                    <a href="/admin/property/new" class="btn btn-brand" style="white-space:nowrap;">+ Add Property</a>
                </div>
            </div>

            <!-- Properties Table -->
            <div class="card mb-5">
                <div class="card-header">Active Listed Properties (${properties.size()})</div>
                <div class="card-body p-0">
                    <table class="table table-hover align-middle">
                        <thead>
                            <tr>
                                <th style="width: 80px;">ID</th>
                                <th>Asset Title</th>
                                <th>Pricing</th>
                                <th>Neighborhood Zone</th>
                                <th class="text-end">Controls</th>
                            </tr>
                        </thead>
                        <tbody id="propertiesTable">
                            <c:forEach var="prop" items="${properties}">
                                <tr>
                                    <td class="text-muted fw-bold">#<c:out value="${prop.id}"/></td>
                                    <td><strong style="color:var(--text); font-size:15px;"><c:out value="${prop.title}"/></strong></td>
                                    <td><span style="color:var(--accent-deep); font-weight:800;">Rs. <c:out value="${prop.price}"/> Cr</span></td>
                                    <td><span class="badge" style="background:rgba(15,157,138,0.1); color:var(--accent-deep);"><c:out value="${prop.locality.name}"/></span></td>
                                    <td class="text-end">
                                        <form action="/admin/property/delete/${prop.id}" method="post" style="display:inline;">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger" onclick="return confirm('WARNING: This will permanently delete this property asset. Proceed?');">Delete</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <!-- Localities Reference Table -->
            <h4 style="font-weight:800; color:var(--text); letter-spacing:-0.02em;" class="mb-4">Geographic Zones</h4>
            <div class="card">
                <div class="card-body p-0">
                    <table class="table table-hover align-middle">
                        <thead>
                            <tr>
                                <th style="width: 80px;">ID</th>
                                <th>Locality Name</th>
                                <th>Governing City</th>
                                <th>Mapping Coords</th>
                                <th>Trust / Safety Base</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="loc" items="${localities}">
                                <tr>
                                    <td class="text-muted fw-bold">#<c:out value="${loc.id}"/></td>
                                    <td><strong style="color:var(--text);"><c:out value="${loc.name}"/></strong></td>
                                    <td><c:out value="${loc.city}"/></td>
                                    <td><code style="color:var(--muted); background:rgba(0,0,0,0.04); padding:4px 8px; border-radius:4px;"><c:out value="${loc.latitude}"/>, <c:out value="${loc.longitude}"/></code></td>
                                    <td><strong style="color:var(--accent);"><c:out value="${loc.safetyScore}"/> / 10</strong></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </div>
</div>

<script>
    function filterProperties() {
        const query = document.getElementById('adminSearch').value.toLowerCase();
        const rows = document.querySelectorAll('#propertiesTable tbody tr');
        rows.forEach(row => {
            const text = row.innerText.toLowerCase();
            row.style.display = text.includes(query) ? '' : 'none';
        });
    }
</script>
</body>
</html>
