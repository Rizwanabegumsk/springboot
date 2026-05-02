<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Books List</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div class="container">
        <h2>Books Directory</h2>
        <div class="actions">
            <a href="<c:url value='/books/add'/>" class="btn btn-primary">Add New Book</a>
        </div>
        
        <table class="styled-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>ISBN</th>
                    <th>Author</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="book" items="${books}">
                    <tr>
                        <td>${book.id}</td>
                        <td>${book.title}</td>
                        <td>${book.isbn}</td>
                        <td>${book.author.name}</td>
                        <td>
                            <a href="<c:url value='/books/edit/${book.id}'/>" class="btn btn-secondary">Edit</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
