<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Book</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div class="container form-container">
        <h2>Add New Book</h2>
        
        <form action="<c:url value='/books/add' />" method="post">
            <div class="form-group">
                <label>Title</label>
                <input type="text" name="title" required />
            </div>
            <div class="form-group">
                <label>Genre</label>
                <input type="text" name="genre" required />
            </div>
            <div class="form-group">
                <label>Published Year</label>
                <input type="number" name="publishedYear" required />
            </div>
            <div class="form-group">
                <label>ISBN</label>
                <input type="text" name="isbn" required />
            </div>
            <div class="form-group">
                <label>Author</label>
                <select name="authorId" required>
                    <option value="">Select Author</option>
                    <c:forEach var="author" items="${authors}">
                        <option value="${author.id}">${author.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save Book</button>
                <a href="<c:url value='/books'/>" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>
