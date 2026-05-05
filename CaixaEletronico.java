package caixaeletronico;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe CaixaEletronico implementando o contrato com ICaixaEletronico.
 * Trabalha com uma matriz 6x2 para guardar a quantidade de cedulas disponivel.
 *
 * Coluna 0 = valor das cedulas: 100, 50, 20, 10, 5, 2
 * Coluna 1 = quantidade de cedulas
 *
 * Valores iniciais (conforme tabela do Programa 3):
 *   100 -> 100
 *    50 -> 200
 *    20 -> 300
 *    10 -> 350
 *     5 -> 450
 *     2 -> 500
 */
public class CaixaEletronico implements ICaixaEletronico {

    // Matriz 6x2: [linha][0] = valor da cedula, [linha][1] = quantidade
    private int[][] cedulas = {
        {100, 100},
        { 50, 200},
        { 20, 300},
        { 10, 350},
        {  5, 450},
        {  2, 500}
    };

    // Cota minima de atendimento (valor em reais)
    private int cotaMinima = 0;

    // Historico de saques para o extrato final (botao Sair)
    private List<String> historicoSaques = new ArrayList<>();

    /**
     * Retorna o valor total disponivel no caixa eletronico.
     */
    @Override
    public String pegaValorTotalDisponivel() {
        int total = 0;
        for (int i = 0; i < cedulas.length; i++) {
            total += cedulas[i][0] * cedulas[i][1];
        }
        String resposta = "Valor total disponivel no caixa: R$ " + total;
        return resposta;
    }

    /**
     * Efetua o saque, dando prioridade as cedulas de maior valor.
     * Se nao for possivel realizar o saque, retorna mensagem de erro.
     * Se o caixa ficar abaixo da cota minima apos o saque, emite aviso.
     */
    @Override
    public String sacar(Integer valor) {
        String resposta = "";

        // Verifica se o caixa esta acima da cota minima antes de atender
        int totalAntes = calcularTotal();
        if (totalAntes <= cotaMinima) {
            resposta = "Caixa Vazio: Chame o Operador";
            return resposta;
        }

        // Tenta realizar o saque usando copias temporarias para nao alterar o estado real
        int[] temporario = new int[cedulas.length];
        for (int i = 0; i < cedulas.length; i++) {
            temporario[i] = cedulas[i][1];
        }

        int[] cedulasUsadas = new int[cedulas.length];
        int restante = valor;
        int totalCedulasUsadas = 0;

        // Percorre da cedula de maior valor para a menor
        for (int i = 0; i < cedulas.length; i++) {
            if (restante <= 0) break;

            int valorCedula = cedulas[i][0];
            int qtdDisponivel = temporario[i];

            if (valorCedula <= restante && qtdDisponivel > 0) {
                int qtdNecessaria = restante / valorCedula;
                int qtdUtilizada = Math.min(qtdNecessaria, qtdDisponivel);

                cedulasUsadas[i] = qtdUtilizada;
                totalCedulasUsadas += qtdUtilizada;

                restante -= qtdUtilizada * valorCedula;
                temporario[i] -= qtdUtilizada;
            }
        }

        // Verifica se o saque foi possivel com as cedulas disponiveis
        if (restante != 0) {
            resposta = "Nao Temos Notas Para Este Saque";
            return resposta;
        }

        // Verifica se ultrapassou o limite maximo de 30 cedulas
        if (totalCedulasUsadas > 30) {
            resposta = "Saque nao realizado. Limite maximo de 30 cedulas excedido.";
            return resposta;
        }

        // Saque possivel: atualiza a matriz real
        StringBuilder sb = new StringBuilder();
        sb.append("Saque de R$ ").append(valor).append(" realizado com sucesso!\n");
        sb.append("Cedulas dispensadas:\n");

        for (int i = 0; i < cedulas.length; i++) {
            if (cedulasUsadas[i] > 0) {
                cedulas[i][1] -= cedulasUsadas[i];

                sb.append("  Nota de R$ ").append(cedulas[i][0])
                  .append(": ").append(cedulasUsadas[i]).append(" cedula(s)\n");
            }
        }

        resposta = sb.toString().trim();

        // Registra no historico para o extrato
        historicoSaques.add("Saque: R$ " + valor + " | Saldo restante: R$ " + calcularTotal());

        // Verifica cota minima apos o saque
        int totalDepois = calcularTotal();
        if (totalDepois <= cotaMinima) {
            resposta += "\n\nCaixa Vazio: Chame o Operador";
        }

        return resposta;
    }

    /**
     * Retorna relatorio com a quantidade de cedulas disponiveis para cada valor.
     */
    @Override
    public String pegaRelatorioCedulas() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Relatorio de Cedulas ===\n");
        for (int i = 0; i < cedulas.length; i++) {
            sb.append("Nota de R$ ").append(cedulas[i][0])
              .append(": ").append(cedulas[i][1]).append(" cedula(s)\n");
        }
        String resposta = sb.toString().trim();
        return resposta;
    }

    /**
     * Efetua a reposicao de cedulas de um determinado valor.
     * @param cedula  valor da cedula a repor (deve ser 2, 5, 10, 20, 50 ou 100)
     * @param quantidade  quantidade a adicionar
     */
    @Override
    public String reposicaoCedulas(Integer cedula, Integer quantidade) {
        String resposta = "";
        boolean encontrou = false;

        for (int i = 0; i < cedulas.length; i++) {
            if (cedulas[i][0] == cedula) {
                cedulas[i][1] += quantidade;
                resposta = "Reposicao realizada!\n"
                         + "Nota de R$ " + cedula + ": agora com "
                         + cedulas[i][1] + " cedula(s).";
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            resposta = "Valor de cedula invalido: R$ " + cedula
                     + ".\nValores aceitos: 2, 5, 10, 20, 50, 100.";
        }

        return resposta;
    }

    /**
     * Armazena a cota minima de atendimento.
     * Se o total disponivel cair abaixo deste valor, o caixa para de atender.
     */
    @Override
    public String armazenaCotaMinima(Integer minimo) {
        cotaMinima = minimo;
        String resposta = "Cota minima definida: R$ " + minimo;
        return resposta;
    }

    /**
     * Retorna o extrato completo de saques (usado pelo botao Sair).
     */
    public String gerarExtrato() {
        if (historicoSaques.isEmpty()) {
            return "Nenhum saque realizado nesta sessao.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=== Extrato de Saques ===\n");
        for (String linha : historicoSaques) {
            sb.append(linha).append("\n");
        }
        sb.append("========================\n");
        sb.append("Saldo atual: R$ ").append(calcularTotal());
        return sb.toString();
    }

    /**
     * Calcula o valor total disponivel no caixa.
     */
    private int calcularTotal() {
        int total = 0;
        for (int i = 0; i < cedulas.length; i++) {
            total += cedulas[i][0] * cedulas[i][1];
        }
        return total;
    }

    // ---------------------------------------------------------------
    // main: conecta com a interface grafica (GUI) fornecida
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        GUI janela = new GUI(CaixaEletronico.class);
        janela.show();
    }
}