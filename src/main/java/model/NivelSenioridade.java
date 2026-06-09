package model;

public enum NivelSenioridade {
    JOVEM_APRENDIZ("Jovem Aprendiz", 1),
    ESTAGIARIO("Estagiário", 2),
    JUNIOR("Junior", 3),
    PLENO("Pleno", 4),
    SENIOR("Senior", 5);

    private final String rotulo;
    private final int peso;

    NivelSenioridade(String rotulo, int peso) {
        this.rotulo = rotulo;
        this.peso = peso;
    }

    public String getRotulo() {
        return rotulo;
    }

    public int getPeso() {
        return peso;
    }

    public boolean ehInferiorA(NivelSenioridade outro) {
        return this.peso < outro.peso;
    }

    public boolean podeSerGerente() {
        return this == PLENO || this == SENIOR;
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
