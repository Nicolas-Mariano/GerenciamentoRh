package salary;

import model.Contrato;

public class AumentoPorBonus extends AumentoDecorator {
    private final double valorFixo;

    public AumentoPorBonus(CalculadoraSalario calculadora, double valorFixo) {
        super(calculadora);
        if (valorFixo <= 0) {
            throw new IllegalArgumentException("O valor fixo deve ser maior que zero.");
        }
        this.valorFixo = valorFixo;
    }

    @Override
    public double calcular(Contrato contrato) {
        return calculadora.calcular(contrato) + valorFixo;
    }
}
