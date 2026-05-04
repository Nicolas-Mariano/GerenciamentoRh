/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */

CREATE TABLE Funcionario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE CHECK (cpf ~ '^[0-9]{11}$'),
    matricula VARCHAR(20) NOT NULL UNIQUE,
    funcao TEXT NOT NULL,
    salario_base DECIMAL(10,2) NOT NULL,
    data_admissao DATE NOT NULL CHECK (data_admissao <= CURRENT_DATE),
    data_demissao DATE,
    telefone VARCHAR(11) NOT NULL CHECK (telefone ~ '^[0-9]{11}$'),
    nivel VARCHAR(50) CHECK (nivel IN ('Jovem Aprendiz', 'Estagiário', 'Junior','Pleno', 'Senior')),
    CHECK (data_demissao IS NULL OR data_demissao >= data_admissao)
);
