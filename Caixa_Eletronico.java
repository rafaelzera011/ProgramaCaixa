package caixaeletronico;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridLayout;

public class Caixa_Eletronico extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Caixa_Eletronico frame = new Caixa_Eletronico();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Caixa_Eletronico() {
		setTitle("Caixa Eletronico");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 254, 304);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Modulo do Cliente :");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JButton btnNewButton = new JButton("Efetuar Saque");
		
		JLabel lblNewLabel_1 = new JLabel("Modulo do Administrador :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JButton btnNewButton_1 = new JButton("Relatorio De Cedulas");
		
		JButton btnNewButton_2 = new JButton("Valor Total Disponivel");
		
		JButton btnNewButton_3 = new JButton("Reposicao De Cedulas");
		
		JButton btnNewButton_4 = new JButton("Cota Minima");
		
		JLabel lblNewLabel_2 = new JLabel("Modulo De Ambos :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JButton btnNewButton_5 = new JButton("Sair");
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		contentPane.add(lblNewLabel);
		contentPane.add(btnNewButton);
		contentPane.add(lblNewLabel_1);
		contentPane.add(btnNewButton_1);
		contentPane.add(btnNewButton_2);
		contentPane.add(btnNewButton_3);
		contentPane.add(btnNewButton_4);
		contentPane.add(lblNewLabel_2);
		contentPane.add(btnNewButton_5);

	}
}
