<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gerenciamento de RH</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Sistema de Gerenciamento de RH</h1>

        <div class="menu-box">
            <h2>Setores</h2>
            <ul>
                <li><a href="cadastro_setor.jsp">Cadastrar Setor</a></li>
                <li><a href="controller.do?acao=ListarSetores">Listar Setores</a></li>
            </ul>

            <hr/>

            <h2>Funcionários</h2>
            <ul>
                <li><a href="controller.do?acao=AbrirFormFuncionario">Cadastrar Funcionário</a></li>
                <li><a href="controller.do?acao=ListarFuncionarios">Listar Funcionários</a></li>
                <li><a href="controller.do?acao=ListarEnderecos">Listar Endereços</a></li>
            </ul>

            <hr/>

            <h2>Ações</h2>
            <ul>
                <li><a href="controller.do?acao=AbrirVincularGerente">Vincular Gerente ao Setor</a></li>
                <li><a href="controller.do?acao=AbrirAplicarAumento">Aplicar Aumento Salarial</a></li>
            </ul>
        </div>
    </body>
</html>