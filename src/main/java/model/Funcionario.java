/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public class Funcionario {

    private int id;
    private String nome;
    private String cpf;
    private String matricula;
    private String funcao;
    private double salarioBase;
    private Date dataAdmissao;
    private Date dataDemissao;
    private String telefone;
    private String nivel;
    private int idSetor;
    private int idEndereco;

    public Funcionario() {
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Date getDataDemissao() {
        return dataDemissao;
    }

    public void setDataDemissao(Date dataDemissao) {
        this.dataDemissao = dataDemissao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public int getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(int idSetor) {
        this.idSetor = idSetor;
    }

    public int getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getCpfFormatado() {
        if (cpf != null && cpf.length() == 11) {
            return cpf.substring(0,3) + "." + cpf.substring(3,6) + "." + cpf.substring(6,9) + "-" + cpf.substring(9,11);
        }
        return cpf;
    }
    
    public String getTelefoneFormatado() {
        if (telefone != null && telefone.length() == 11) {
            return "(" + telefone.substring(0,2) + ") " + telefone.substring(2,7) + "-" + telefone.substring(7,11);
        }
        if (telefone != null && telefone.length() == 10) {
            return "(" + telefone.substring(0,2) + ") " + telefone.substring(2,6) + "-" + telefone.substring(6,10);
        }
        return telefone;
    }
    
    public static FuncionarioBuilder getBuilder() {
        return new FuncionarioBuilder();
    }

    public static class FuncionarioBuilder {

        Funcionario f = new Funcionario();

        public FuncionarioBuilder comId(int id) {
            f.id = id;
            return this;
        }

        public FuncionarioBuilder comNome(String nome) {
            f.nome = nome;
            return this;
        }

        public FuncionarioBuilder comCpf(String cpf) {
            f.cpf = cpf;
            return this;
        }

        public FuncionarioBuilder comMatricula(String matricula) {
            f.matricula = matricula;
            return this;
        }

        public FuncionarioBuilder comFuncao(String funcao) {
            f.funcao = funcao;
            return this;
        }

        public FuncionarioBuilder comSalarioBase(double salarioBase) {
            f.salarioBase = salarioBase;
            return this;
        }

        public FuncionarioBuilder comDataAdmissao(Date dataAdmissao) {
            f.dataAdmissao = dataAdmissao;
            return this;
        }

        public FuncionarioBuilder comDataDemissao(Date dataDemissao) {
            f.dataDemissao = dataDemissao;
            return this;
        }

        public FuncionarioBuilder comTelefone(String telefone) {
            f.telefone = telefone;
            return this;
        }

        public FuncionarioBuilder comNivel(String nivel) {
            f.nivel = nivel;
            return this;
        }

        public FuncionarioBuilder comIdSetor(int idSetor) {
            f.idSetor = idSetor;
            return this;
        }

        public FuncionarioBuilder comIdEndereco(int idEndereco) {
            f.idEndereco = idEndereco;
            return this;
        }

        public Funcionario constroi() {
            return f;
        }
    }
}