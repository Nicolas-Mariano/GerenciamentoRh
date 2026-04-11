/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  55119
 * Created: 9 de abr. de 2026
 */

CREATE TABLE Setor (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);