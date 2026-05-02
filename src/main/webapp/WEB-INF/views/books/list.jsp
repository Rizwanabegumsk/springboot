<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Books Catalogue</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div class="container">
        <h2>Books Catalogue</h2>
        
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        
        <div class="actions">
            <a href="<c:url value='/books/add'/>" class="btn btn-primary">Add New Book</a>
            <a href="<c:url value='/authors'/>" class="btn btn-secondary">View Authors</a>
        </div>
        
        <table class="styled-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Genre</th>
                    <th>Published</th>
                    <th>ISBN</th>
                    <th>Author</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="book" items="${catalogue}">
                    <tr>
                        <td>${book.id}</td>
                        <td>${book.title}</td>
                        <td>${book.genre}</td>
                        <td>${book.publishedYear}</td>
                        <td>${book.isbn}</td>
                        <td>${book.authorName} (${book.authorNationality})</td>
                        <td>
                            <a href="<c:url value='/books/edit/${book.id}'/>" class="btn btn-sm btn-info">Edit</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
