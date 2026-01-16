package entities.factories;

import org.example.entities.Account;

/**
 * Factory para criação de instâncias de Account em testes.
 * Padrão Factory:
 * - Centraliza a lógica de instanciação de objetos
 * - Elimina duplicação de código nos testes
 * - Facilita manutenção (mudanças no construtor afetam apenas o Factory)
 * - Melhora legibilidade com nomes descritivos
 */
public class AccountFactory {

    private static final Long DEFAULT_ID = 1L;

    /**
     * Cria uma conta com saldo zero.
     * Uso: Testes que precisam começar com saldo vazio para validar depósitos.
     * @return Account com id=1L e balance=0.0
     */
    public static Account createEmptyAccount() {
        return new Account(DEFAULT_ID, 0.0);
    }

    /**
     * Cria uma conta com saldo específico.
     * Uso: Testes que precisam de um saldo inicial customizado.
     * @param balance saldo inicial da conta
     * @return Account com id=1L e balance fornecido
     */
    public static Account createAccountWithBalance(Double balance) {
        return new Account(DEFAULT_ID, balance);
    }

    /**
     * Cria uma conta com ID e saldo personalizados.
     * Uso: Testes que precisam de controle total sobre id e balance.
     * @param id identificador da conta
     * @param balance saldo inicial da conta
     * @return Account com id e balance fornecidos
     */
    public static Account createAccount(Long id, Double balance) {
        return new Account(id, balance);
    }

    /**
     * Cria uma conta com saldo padrão de 100.0.
     * Uso: Testes que precisam de um valor médio para validações.
     * @return Account com id=1L e balance=100.0
     */
    public static Account createDefaultAccount() {
        return new Account(DEFAULT_ID, 100.0);
    }
}
