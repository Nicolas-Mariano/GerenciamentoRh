package model;

public class Funcionario {

    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private Endereco endereco;
    private boolean desligado;

    public Funcionario() {
        super();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

    public boolean isDesligado() { return desligado; }
    public void setDesligado(boolean desligado) { this.desligado = desligado; }

    public boolean temEnderecoVinculado() {
        return endereco != null && endereco.getId() > 0;
    }

    public String getCpfFormatado() {
        if (cpf != null && cpf.length() == 11) {
            return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
        }
        return cpf;
    }

    public String getTelefoneFormatado() {
        if (telefone != null && telefone.length() == 11) {
            return "(" + telefone.substring(0, 2) + ") " + telefone.substring(2, 7) + "-" + telefone.substring(7, 11);
        }
        if (telefone != null && telefone.length() == 10) {
            return "(" + telefone.substring(0, 2) + ") " + telefone.substring(2, 6) + "-" + telefone.substring(6, 10);
        }
        return telefone;
    }

    public static FuncionarioBuilder getBuilder() {
        return new FuncionarioBuilder();
    }

    public static class FuncionarioBuilder {
        private final Funcionario f = new Funcionario();

        public FuncionarioBuilder comId(int id) { f.id = id; return this; }
        public FuncionarioBuilder comNome(String nome) { f.nome = nome; return this; }
        public FuncionarioBuilder comCpf(String cpf) { f.cpf = cpf; return this; }
        public FuncionarioBuilder comTelefone(String telefone) { f.telefone = telefone; return this; }
        public FuncionarioBuilder comEmail(String email) { f.email = email; return this; }
        public FuncionarioBuilder comEndereco(Endereco endereco) { f.endereco = endereco; return this; }
        public FuncionarioBuilder comDesligado(boolean desligado) { f.desligado = desligado; return this; }

        public Funcionario constroi() { return f; }
    }
}
