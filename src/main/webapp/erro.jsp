<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Erro</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Ocorreu um Erro</h1>

        <div class="msg-erro">
            <c:choose>
                <c:when test="${not empty erro}">
                    ${erro.message != null ? erro.message : erro}
                </c:when>
                <c:otherwise>
                    Erro desconhecido. Tente novamente.
                </c:otherwise>
            </c:choose>
        </div>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>