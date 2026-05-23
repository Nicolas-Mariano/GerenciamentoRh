package salary;

import model.Contrato;

public class AumentoPercentual extends AumentoDecorator {
    private final double percentual;

    public AumentoPercentual(CalculadoraSalario calculadora, double percentual) {
        super(calculadora);
        if (percentual <= 0) {
            throw new IllegalArgumentException("O percentual deve ser maior que zero.");
        }
        this.percentual = percentual;
    }

    @Override
    public double calcular(Contrato contrato) {
        return calculadora.calcular(contrato) * (1 + percentual / 100);
    }
}
