package model;

import java.util.Date;

public class Contrato {

    private int id;
    private String matricula;
    private Date dataAdmissao;
    private Date dataDemissao;
    private String motivoDesligamento;
    private double salarioBase;
    private NivelSenioridade nivelSenioridade;
    private Funcionario funcionario;
    private Setor setor;

    public Contrato() {
        super();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public Date getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(Date dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public Date getDataDemissao() { return dataDemissao; }
    public void setDataDemissao(Date dataDemissao) { this.dataDemissao = dataDemissao; }

    public String getMotivoDesligamento() { return motivoDesligamento; }
    public void setMotivoDesligamento(String motivoDesligamento) { this.motivoDesligamento = motivoDesligamento; }

    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) { this.salarioBase = salarioBase; }

    public NivelSenioridade getNivelSenioridade() { return nivelSenioridade; }
    public void setNivelSenioridade(NivelSenioridade nivelSenioridade) { this.nivelSenioridade = nivelSenioridade; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public Setor getSetor() { return setor; }
    public void setSetor(Setor setor) { this.setor = setor; }

    public boolean isAtivo() {
        return dataDemissao == null;
    }

    public boolean estaEncerrado() {
        return dataDemissao != null;
    }

    public boolean foiAdmitidoAntesDe(Date data) {
        return dataAdmissao != null && dataAdmissao.before(data);
    }

    public static ContratoBuilder getBuilder() {
        return new ContratoBuilder();
    }

    public static class ContratoBuilder {
        private final Contrato c = new Contrato();

        public ContratoBuilder comId(int id) { c.id = id; return this; }
        public ContratoBuilder comMatricula(String matricula) { c.matricula = matricula; return this; }
        public ContratoBuilder comDataAdmissao(Date dataAdmissao) { c.dataAdmissao = dataAdmissao; return this; }
        public ContratoBuilder comDataDemissao(Date dataDemissao) { c.dataDemissao = dataDemissao; return this; }
        public ContratoBuilder comMotivoDesligamento(String motivo) { c.motivoDesligamento = motivo; return this; }
        public ContratoBuilder comSalarioBase(double salarioBase) { c.salarioBase = salarioBase; return this; }
        public ContratoBuilder comNivelSenioridade(NivelSenioridade nivel) { c.nivelSenioridade = nivel; return this; }
        public ContratoBuilder comFuncionario(Funcionario funcionario) { c.funcionario = funcionario; return this; }
        public ContratoBuilder comSetor(Setor setor) { c.setor = setor; return this; }

        public Contrato constroi() { return c; }
    }
}
