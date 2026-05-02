<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${book.id != null ? 'Edit Book' : 'Add New Book'}</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div class="container form-container">
        <h2>${book.id != null ? 'Edit Book' : 'Add New Book'}</h2>
        
        <c:if test="${not empty error}">
            <div class="error-msg">${error}</div>
        </c:if>
        
        <form action="<c:url value='${book.id != null ? \"/books/edit/\".concat(book.id) : \"/books/add\"}' />" method="post">
            
            <div class="form-group">
                <label for="title">Book Title:</label>
                <input type="text" id="title" name="title" value="${book.title}" required />
            </div>
            
            <div class="form-group">
                <label for="isbn">ISBN:</label>
                <input type="text" id="isbn" name="isbn" value="${book.isbn}" required />
            </div>
            
            <div class="form-group">
                <label for="author">Author:</label>
                <select id="author" name="author" required>
                    <option value="">Select an Author</option>
                    <c:forEach var="author" items="${authors}">
                        <option value="${author.id}" ${book.author != null && book.author.id == author.id ? 'selected' : ''}>
                            ${author.name}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save</button>
                <a href="<c:url value='/books'/>" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>
