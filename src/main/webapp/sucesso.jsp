<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Operação Realizada</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Operação Realizada</h1>

        <div class="msg">
            <c:choose>
                <c:when test="${not empty mensagem}">
                    ${mensagem}
                </c:when>
                <c:otherwise>
                    Operação realizada com sucesso.
                </c:otherwise>
            </c:choose>
        </div>

        <br>
        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>