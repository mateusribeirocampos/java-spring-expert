package entities.factories;

import org.example.entities.Financing;

public class FinancingFactory {

    // Valores padrão válidos
    private static final Double VALID_TOTAL_AMOUNT = 100000.0;
    private static final Double VALID_INCOME = 2000.0;
    private static final Integer VALID_MONTHS = 80;
    private static final Integer INVALID_MONTHS = 20;

    // Cenário 1: Financiamento válido padrão
    // Exemplo: R$ 100.000, renda R$ 2.000, 80 meses
    // Parcela: 1.000 (50% da renda) - no limite
    public static Financing createDefaultValidFinancing() {
        return new Financing(VALID_TOTAL_AMOUNT, VALID_INCOME, VALID_MONTHS);
    }

    public static double[] getInvalidFinancingData() {
        return new double[] { VALID_TOTAL_AMOUNT, VALID_INCOME, INVALID_MONTHS };  // totalAmount, income, months
    }


}