/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Setor {

    private int id;
    private String nome;
    private Funcionario funcResponsavel;
    private String nomeResponsavel;

    public Setor() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    } 

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Funcionario getFuncResponsavel() {
        return funcResponsavel;
    }

    public void setFuncResponsavel(Funcionario funcResponsavel) {
        this.funcResponsavel = funcResponsavel;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public static SetorBuilder getBuilder() {
        return new SetorBuilder();
    }

    public static class SetorBuilder {

        Setor setor = new Setor();

        public SetorBuilder comId(int id) {
            setor.id = id;
            return this;
        }

        public SetorBuilder comNome(String nome) {
            setor.nome = nome;
            return this;
        }

        public SetorBuilder comFuncResponsavel(Funcionario funcResponsavel) {
            setor.funcResponsavel = funcResponsavel;
            return this;
        }
        
        public SetorBuilder comNomeResponsavel(String nomeResponsavel) {
            setor.nomeResponsavel = nomeResponsavel;
            return this;
        }

        public Setor constroi() {
            return setor;
        }
    } 
}
