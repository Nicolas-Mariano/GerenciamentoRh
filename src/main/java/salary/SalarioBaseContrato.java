package salary;

import model.Contrato;

public class SalarioBaseContrato implements CalculadoraSalario {
    @Override
    public double calcular(Contrato contrato) {
        return contrato.getSalarioBase();
    }
}
