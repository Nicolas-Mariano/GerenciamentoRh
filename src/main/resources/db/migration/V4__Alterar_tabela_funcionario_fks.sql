/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  55119
 * Created: 9 de abr. de 2026
 */
ALTER TABLE Funcionario
ADD COLUMN id_setor INTEGER NOT NULL;

ALTER TABLE Funcionario
ADD CONSTRAINT fk_funcionario_setor
FOREIGN KEY (id_setor) REFERENCES Setor(id);

ALTER TABLE Funcionario
ADD COLUMN id_endereco INTEGER UNIQUE NOT NULL;

ALTER TABLE Funcionario
ADD CONSTRAINT fk_funcionario_endereco
FOREIGN KEY (id_endereco) REFERENCES Endereco(id);
