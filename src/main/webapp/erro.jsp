<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Erro</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 30px;
                background-color: #f5f5f5;
            }
            h1 {
                color: #333;
                border-bottom: 2px solid #555;
                padding-bottom: 8px;
            }
            .msg-erro {
                background-color: #fff;
                border: 1px solid #ccc;
                padding: 16px 20px;
                width: 380px;
                font-size: 14px;
                color: #cc0000;
            }
            .voltar {
                display: inline-block;
                margin-top: 14px;
                font-size: 13px;
                color: #0066cc;
                text-decoration: none;
            }
            .voltar:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <h1>Ocorreu um Erro</h1>

        <div class="msg-erro">
            <%
                Exception erro = (Exception) request.getAttribute("erro");
                if (erro != null) {
            %>
                <%= erro.getMessage() %>
            <%
                } else {
            %>
                Erro desconhecido. Tente novamente.
            <%
                }
            %>
        </div>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>
