/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */

ALTER TABLE Setor
ADD COLUMN id_func_responsavel INTEGER;

ALTER TABLE Setor
ADD CONSTRAINT fk_func_responsavel
FOREIGN KEY (id_func_responsavel) REFERENCES Funcionario(id) ON DELETE SET NULL; -- Regra: Gerente deletado -> Campo fica NULL;
