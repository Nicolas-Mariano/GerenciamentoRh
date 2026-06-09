/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Endereco {

    private int id;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String numEndereco;
    private String complemento;
    private Funcionario funcionario;

    public Endereco() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumEndereco() {
        return numEndereco;
    }

    public void setNumEndereco(String numEndereco) {
        this.numEndereco = numEndereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    
    public String getCepFormatado() {
        if (cep != null && cep.length() == 8) {
            return cep.substring(0,5) + "-" + cep.substring(5,8);
        }
        return cep;
    }
    
    public String getEnderecoPadronizado() {
        String end = logradouro + ", " + numEndereco;
        if (complemento != null && !complemento.trim().isEmpty()) {
            end += ", " + complemento;
        }
        end += " - " + bairro + ", " + cidade + " - " + estado + ", " + getCepFormatado();
        return end;
    }

    public static EnderecoBuilder getBuilder() {
        return new EnderecoBuilder();
    }

    public static class EnderecoBuilder {

        Endereco endereco = new Endereco();

        public EnderecoBuilder comId(int id) {
            endereco.id = id;
            return this;
        }

        public EnderecoBuilder comLogradouro(String logradouro) {
            endereco.logradouro = logradouro;
            return this;
        }

        public EnderecoBuilder comBairro(String bairro) {
            endereco.bairro = bairro;
            return this;
        }

        public EnderecoBuilder comCidade(String cidade) {
            endereco.cidade = cidade;
            return this;
        }

        public EnderecoBuilder comEstado(String estado) {
            endereco.estado = estado;
            return this;
        }

        public EnderecoBuilder comCep(String cep) {
            endereco.cep = cep;
            return this;
        }

        public EnderecoBuilder comNumEndereco(String numEndereco) {
            endereco.numEndereco = numEndereco;
            return this;
        }

        public EnderecoBuilder comComplemento(String complemento) {
            endereco.complemento = complemento;
            return this;
        }

        public Endereco constroi() {
            return endereco;
        }
    } 
}