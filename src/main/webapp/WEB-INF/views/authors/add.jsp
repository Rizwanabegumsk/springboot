<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Author</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div class="container form-container">
        <h2>Add New Author</h2>
        <form action="<c:url value='/authors/add' />" method="post">
            <div class="form-group">
                <label>Name</label>
                <input type="text" name="name" required />
            </div>
            <div class="form-group">
                <label>Nationality</label>
                <input type="text" name="nationality" required />
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save Author</button>
                <a href="<c:url value='/authors'/>" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>
