package salary;

import model.Contrato;

public abstract class AumentoDecorator implements CalculadoraSalario {
    protected final CalculadoraSalario calculadora;

    public AumentoDecorator(CalculadoraSalario calculadora) {
        this.calculadora = calculadora;
    }

    @Override
    public abstract double calcular(Contrato contrato);
}
