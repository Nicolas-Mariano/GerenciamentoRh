package salary;

public enum TipoAumento {
    PERCENTUAL("Percentual (%)"),
    BONUS("Bônus fixo (R$)");

    private final String rotulo;

    TipoAumento(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}
