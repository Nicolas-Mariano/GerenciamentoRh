package model;

public class Setor {

    private int id;
    private String nome;
    private Contrato contratoResponsavel;
    private String nomeResponsavel;

    public Setor() {
        super();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Contrato getContratoResponsavel() { return contratoResponsavel; }
    public void setContratoResponsavel(Contrato contratoResponsavel) { this.contratoResponsavel = contratoResponsavel; }

    public String getNomeResponsavel() { return nomeResponsavel; }
    public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = nomeResponsavel; }

    public static SetorBuilder getBuilder() {
        return new SetorBuilder();
    }

    public static class SetorBuilder {
        private final Setor setor = new Setor();

        public SetorBuilder comId(int id) { setor.id = id; return this; }
        public SetorBuilder comNome(String nome) { setor.nome = nome; return this; }
        public SetorBuilder comContratoResponsavel(Contrato contrato) { setor.contratoResponsavel = contrato; return this; }
        public SetorBuilder comNomeResponsavel(String nomeResponsavel) { setor.nomeResponsavel = nomeResponsavel; return this; }

        public Setor constroi() { return setor; }
    }
}
