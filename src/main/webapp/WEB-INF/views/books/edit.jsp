<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Book</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div class="container form-container">
        <h2>Edit Book</h2>
        
        <form action="<c:url value='/books/edit/${book.id}' />" method="post">
            <div class="form-group">
                <label>Title</label>
                <input type="text" name="title" value="${book.title}" required />
            </div>
            <div class="form-group">
                <label>Genre</label>
                <input type="text" name="genre" value="${book.genre}" required />
            </div>
            <div class="form-group">
                <label>Published Year</label>
                <input type="number" name="publishedYear" value="${book.publishedYear}" required />
            </div>
            <div class="form-group">
                <label>ISBN</label>
                <input type="text" name="isbn" value="${book.isbn}" required />
            </div>
            <div class="form-group">
                <label>Author</label>
                <select name="authorId" required>
                    <c:forEach var="author" items="${authors}">
                        <option value="${author.id}" ${book.author.id == author.id ? 'selected' : ''}>${author.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Update Book</button>
                <a href="<c:url value='/books'/>" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>
