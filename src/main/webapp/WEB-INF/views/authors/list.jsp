<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Authors List</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div class="container">
        <h2>Authors List</h2>
        <div class="actions">
            <a href="<c:url value='/authors/add'/>" class="btn btn-primary">Add New Author</a>
            <a href="<c:url value='/books'/>" class="btn btn-secondary">View Books</a>
        </div>
        <table class="styled-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Nationality</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="author" items="${authors}">
                    <tr>
                        <td>${author.id}</td>
                        <td>${author.name}</td>
                        <td>${author.nationality}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
