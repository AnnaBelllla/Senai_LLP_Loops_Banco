import java.util.ArrayList;

public class Banco {
    private ArrayList<ContaBancaria> contas;

    public Banco() {
        contas = new ArrayList<>();
    }

    public void abrirConta(ContaBancaria novaConta) {
        contas.add(novaConta);
    }

    public ContaBancaria buscarConta(String numero) {
        for (ContaBancaria conta : contas) {
            if (conta.getNumeroConta().equals(numero)) {
                return conta;
            }
        }
        return null;
    }

    public void realizarOperacao(String numeroConta, String tipoOperacao, double valor) {
        ContaBancaria conta = buscarConta(numeroConta);
        if (conta != null) {
            switch (tipoOperacao.toLowerCase()) {
                case "deposito":
                    conta.depositar(valor);
                    break;
                case "saque":
                    boolean sucesso = conta.sacar(valor);
                    if (!sucesso) {
                        System.out.println("Saldo insuficiente na conta " + numeroConta);
                    }
                    break;
                default:
                    System.out.println("Operação inválida.");
            }
        } else {
            System.out.println("Conta não encontrada.");
        }
    }

    public void listarContas() {
        for (ContaBancaria conta : contas) {
            System.out.println(conta);
        }
    }

    // Método main
    public static void main(String[] args) {
        Banco banco = new Banco();

        // Criando contas
        ContaCorrente cc = new ContaCorrente("001", "João", 2.5);
        ContaPoupanca cp = new ContaPoupanca("002", "Maria", 0.05);

        banco.abrirConta(cc);
        banco.abrirConta(cp);

        // Operações
        banco.realizarOperacao("001", "deposito", 1000);
        banco.realizarOperacao("002", "deposito", 500);

        banco.realizarOperacao("001", "saque", 100);
        banco.realizarOperacao("002", "saque", 50);

        banco.listarContas();

        // Aplicar rendimento específico para ContaPoupanca
        ContaBancaria conta = banco.buscarConta("002");
        if (conta instanceof ContaPoupanca) {
            ((ContaPoupanca) conta).aplicarRendimento();
        }

        System.out.println("\nApós aplicar rendimento na poupança:");
        banco.listarContas();
    }

    // Classes internas

    static class ContaBancaria {
        private String numeroConta;
        private double saldo;
        private String titular;

        public ContaBancaria(String numeroConta, String titular) {
            this.numeroConta = numeroConta;
            this.titular = titular;
            this.saldo = 0.0;
        }

        public String getNumeroConta() {
            return numeroConta;
        }

        public double getSaldo() {
            return saldo;
        }

        public String getTitular() {
            return titular;
        }

        public void depositar(double valor) {
            if (valor > 0) {
                saldo += valor;
            }
        }

        public boolean sacar(double valor) {
            if (valor > 0 && saldo >= valor) {
                saldo -= valor;
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Conta: " + numeroConta + " | Titular: " + titular + " | Saldo: R$ " + saldo;
        }
    }

    static class ContaCorrente extends ContaBancaria {
        private double taxaManutencao;

        public ContaCorrente(String numeroConta, String titular, double taxaManutencao) {
            super(numeroConta, titular);
            this.taxaManutencao = taxaManutencao;
        }

        @Override
        public boolean sacar(double valor) {
            double valorComTaxa = valor + taxaManutencao;
            return super.sacar(valorComTaxa);
        }
    }

    static class ContaPoupanca extends ContaBancaria {
        private double taxaRendimento;

        public ContaPoupanca(String numeroConta, String titular, double taxaRendimento) {
            super(numeroConta, titular);
            this.taxaRendimento = taxaRendimento;
        }

        public void aplicarRendimento() {
            double rendimento = getSaldo() * taxaRendimento;
            depositar(rendimento);
        }
    }
}
