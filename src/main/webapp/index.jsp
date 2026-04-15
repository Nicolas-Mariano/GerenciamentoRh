<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gerenciamento de RH</title>
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
            .menu-box {
                background-color: #fff;
                border: 1px solid #ccc;
                padding: 20px;
                width: 400px;
            }
            .menu-box h2 {
                margin-top: 0;
                color: #444;
                font-size: 16px;
            }
            .menu-box ul {
                list-style: none;
                padding: 0;
                margin: 0;
            }
            .menu-box ul li {
                margin-bottom: 8px;
            }
            .menu-box ul li a {
                color: #0066cc;
                text-decoration: none;
                font-size: 14px;
            }
            .menu-box ul li a:hover {
                text-decoration: underline;
            }
            hr {
                border: none;
                border-top: 1px solid #ccc;
                margin: 8px 0;
            }
        </style>
    </head>
    <body>
        <h1>Sistema de Gerenciamento de RH</h1>

        <div class="menu-box">
            <h2>Setores</h2>
            <ul>
                <li><a href="controller.do?acao=CadastrarSetor">Cadastrar Setor</a></li>
                <li><a href="controller.do?acao=ListarSetores">Listar Setores</a></li>
            </ul>

            <hr/>

            <h2>Funcionários</h2>
            <ul>
                <li><a href="controller.do?acao=CadastrarFuncionario">Cadastrar Funcionário</a></li>
                <li><a href="controller.do?acao=ListarFuncionarios">Listar Funcionários</a></li>
                <li><a href="controller.do?acao=ListarEnderecos">Listar Endereços</a></li>
            </ul>

            <hr/>

            <h2>Ações</h2>
            <ul>
                <li><a href="controller.do?acao=AtualizarGerenteSetor">Vincular Gerente ao Setor</a></li>
                <li><a href="controller.do?acao=AplicarAumento">Aplicar Aumento Salarial</a></li>
            </ul>
        </div>
    </body>
</html>
