<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add/Edit Property | Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; font-family: 'Inter', sans-serif; }
        .sidebar { background: #13252c; min-height: 100vh; color: #fff; padding-top: 2rem; border-right:1px solid #1c3945;}
        .sidebar a { color: #8cadb8; text-decoration: none; padding: 12px 20px; display: block; }
        .sidebar a:hover { color: #fff; background: rgba(255,255,255,0.1); }
        .form-control, .form-select { border-radius: 8px; padding: 12px; border: 1px solid #dee2e6; }
        .form-control:focus, .form-select:focus { border-color: #0f9d8a; box-shadow: 0 0 0 3px rgba(15,157,138,0.15); }
        .btn-brand { background: #0f9d8a; color: #fff; padding: 12px; border-radius: 8px; font-weight: 600; border:none;}
        .btn-brand:hover { background: #0c8272; color:#fff;}
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-2 sidebar">
            <h4 class="px-3 mb-4 text-white" style="font-weight:800;">Admin Platform</h4>
            <a href="/admin/dashboard">📚 Dashboard</a>
            <a href="/admin/property/new" style="color:#fff; background:rgba(255,255,255,0.05);">➕ Add Property</a>
            <a href="/">🔙 Back to App</a>
        </div>
        
        <!-- Main Form Content -->
        <div class="col-md-7 p-5">
            <h2 class="mb-4" style="font-weight:800; color:#163744;">Add Property Listing</h2>
            <div class="card shadow-sm border-0" style="border-radius:16px;">
                <div class="card-body p-4">
                    <form:form action="/admin/property/save" method="post" modelAttribute="property">
                        <form:hidden path="id" />
                        
                        <div class="mb-4">
                            <label class="form-label text-muted fw-bold" style="font-size:12px; text-transform:uppercase;">Property Title</label>
                            <form:input path="title" class="form-control" placeholder="e.g. 3 BHK Luxury Apartment" required="true"/>
                        </div>

                        <div class="row mb-4">
                            <div class="col-md-6">
                                <label class="form-label text-muted fw-bold" style="font-size:12px; text-transform:uppercase;">Asking Price (Cr)</label>
                                <form:input path="price" type="number" step="0.01" class="form-control" placeholder="1.25" required="true"/>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label text-muted fw-bold" style="font-size:12px; text-transform:uppercase;">Locality Assignment</label>
                                <select name="localityId" class="form-select" required>
                                    <option value="" disabled selected>Select boundary...</option>
                                    <c:forEach var="loc" items="${localities}">
                                        <option value="${loc.id}"><c:out value="${loc.name}"/> - <c:out value="${loc.city}"/></option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="mb-4">
                            <label class="form-label text-muted fw-bold" style="font-size:12px; text-transform:uppercase;">Street Address / Building</label>
                            <form:input path="address" class="form-control" placeholder="Tower B, Plot 42..."/>
                        </div>

                        <div class="mb-5">
                            <label class="form-label text-muted fw-bold" style="font-size:12px; text-transform:uppercase;">Marketing Description</label>
                            <form:textarea path="description" class="form-control" rows="4" placeholder="Briefly describe the property's unique selling points..."></form:textarea>
                        </div>

                        <button type="submit" class="btn btn-brand w-100">Add Property to Database</button>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
