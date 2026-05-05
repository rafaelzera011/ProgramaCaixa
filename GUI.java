package caixaeletronico;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import javax.swing.*;

/**
 * GUI - Interface grafica do Caixa Eletronico.
 * Replica a interface mostrada no enunciado do professor.
 *
 * Uso: GUI janela = new GUI(CaixaEletronico.class);
 *      janela.show();
 */
public class GUI {

    private JFrame frame;
    private ICaixaEletronico caixa;

    /**
     * Construtor que recebe a classe que implementa ICaixaEletronico.
     * Instancia automaticamente a classe passada.
     */
    public GUI(Class<?> classeImplementacao) {
        try {
            Constructor<?> construtor = classeImplementacao.getDeclaredConstructor();
            construtor.setAccessible(true);
            this.caixa = (ICaixaEletronico) construtor.newInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao instanciar a classe: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        construirInterface();
    }

    private void construirInterface() {
        frame = new JFrame("Caixa Eletronico");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(280, 320);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Ao fechar a janela pelo X, chama o extrato (mesmo comportamento do botao Sair)
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                acaoSair();
            }
        });

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // --- Modulo do Cliente ---
        JLabel lblCliente = new JLabel("Modulo do Cliente:");
        JPanel painelLblCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelLblCliente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        painelLblCliente.add(lblCliente);
        painel.add(painelLblCliente);
        painel.add(Box.createVerticalStrut(4));

        JButton btnSaque = criarBotao("Efetuar Saque");
        btnSaque.addActionListener(e -> acaoEfetuarSaque());
        painel.add(btnSaque);

        painel.add(Box.createVerticalStrut(10));

        // --- Modulo do Administrador ---
        JLabel lblAdmin = new JLabel("Modulo do Administrador:");
        JPanel painelLblAdmin = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelLblAdmin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        painelLblAdmin.add(lblAdmin);
        painel.add(painelLblAdmin);
        painel.add(Box.createVerticalStrut(4));

        JButton btnRelatorio = criarBotao("Relatorio de Cedulas");
        btnRelatorio.addActionListener(e -> acaoRelatorioCedulas());
        painel.add(btnRelatorio);
        painel.add(Box.createVerticalStrut(4));

        JButton btnTotal = criarBotao("Valor total disponivel");
        btnTotal.addActionListener(e -> acaoValorTotal());
        painel.add(btnTotal);
        painel.add(Box.createVerticalStrut(4));

        JButton btnReposicao = criarBotao("Reposicao de Cedulas");
        btnReposicao.addActionListener(e -> acaoReposicaoCedulas());
        painel.add(btnReposicao);
        painel.add(Box.createVerticalStrut(4));

        JButton btnCota = criarBotao("Cota Minima");
        btnCota.addActionListener(e -> acaoCotaMinima());
        painel.add(btnCota);

        painel.add(Box.createVerticalStrut(10));

        // --- Modulo de Ambos ---
        JLabel lblAmbos = new JLabel("Modulo de Ambos:");
        JPanel painelLblAmbos = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelLblAmbos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        painelLblAmbos.add(lblAmbos);
        painel.add(painelLblAmbos);
        painel.add(Box.createVerticalStrut(4));

        JButton btnSair = criarBotao("Sair");
        btnSair.addActionListener(e -> acaoSair());
        painel.add(btnSair);

        frame.add(painel);
    }

    /**
     * Cria um botao padrao com largura maxima.
     */
    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return btn;
    }

    // ---------------------------------------------------------------
    // Acoes dos botoes
    // ---------------------------------------------------------------

    private void acaoEfetuarSaque() {
        String entrada = JOptionPane.showInputDialog(frame,
            "Digite o valor do saque (R$):",
            "Efetuar Saque", JOptionPane.QUESTION_MESSAGE);

        if (entrada == null || entrada.trim().isEmpty()) return;

        try {
            int valor = Integer.parseInt(entrada.trim());
            String resultado = caixa.sacar(valor);
            JOptionPane.showMessageDialog(frame, resultado,
                "Resultado do Saque", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Valor invalido. Digite um numero inteiro.",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoRelatorioCedulas() {
        String resultado = caixa.pegaRelatorioCedulas();
        JOptionPane.showMessageDialog(frame, resultado,
            "Relatorio de Cedulas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void acaoValorTotal() {
        String resultado = caixa.pegaValorTotalDisponivel();
        JOptionPane.showMessageDialog(frame, resultado,
            "Valor Total Disponivel", JOptionPane.INFORMATION_MESSAGE);
    }

    private void acaoReposicaoCedulas() {
        String[] opcoes = {"100", "50", "20", "10", "5", "2"};
        String cedula = (String) JOptionPane.showInputDialog(frame,
            "Selecione o valor da cedula:",
            "Reposicao de Cedulas", JOptionPane.QUESTION_MESSAGE,
            null, opcoes, opcoes[0]);

        if (cedula == null) return;

        String qtdStr = JOptionPane.showInputDialog(frame,
            "Digite a quantidade de cedulas de R$ " + cedula + ":",
            "Reposicao de Cedulas", JOptionPane.QUESTION_MESSAGE);

        if (qtdStr == null || qtdStr.trim().isEmpty()) return;

        try {
            int quantidade = Integer.parseInt(qtdStr.trim());
            String resultado = caixa.reposicaoCedulas(
                Integer.parseInt(cedula), quantidade);
            JOptionPane.showMessageDialog(frame, resultado,
                "Reposicao de Cedulas", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Quantidade invalida.",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoCotaMinima() {
        String entrada = JOptionPane.showInputDialog(frame,
            "Digite o valor da cota minima (R$):",
            "Cota Minima", JOptionPane.QUESTION_MESSAGE);

        if (entrada == null || entrada.trim().isEmpty()) return;

        try {
            int minimo = Integer.parseInt(entrada.trim());
            String resultado = caixa.armazenaCotaMinima(minimo);
            JOptionPane.showMessageDialog(frame, resultado,
                "Cota Minima", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Valor invalido. Digite um numero inteiro.",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoSair() {
        // Exibe extrato se a classe tiver o metodo gerarExtrato
        try {
            java.lang.reflect.Method metodo = caixa.getClass().getMethod("gerarExtrato");
            String extrato = (String) metodo.invoke(caixa);
            JOptionPane.showMessageDialog(frame, extrato,
                "Extrato da Sessao", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            // Se nao tiver o metodo, apenas fecha
        }
        frame.dispose();
        System.exit(0);
    }

    /**
     * Exibe a janela na tela.
     */
    public void show() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}