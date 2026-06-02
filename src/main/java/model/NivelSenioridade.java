package model;

public enum NivelSenioridade {
    JOVEM_APRENDIZ("Jovem Aprendiz"),
    ESTAGIARIO("Estagiário"),
    JUNIOR("Junior"),
    PLENO("Pleno"),
    SENIOR("Senior");

    private final String rotulo;

    NivelSenioridade(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
