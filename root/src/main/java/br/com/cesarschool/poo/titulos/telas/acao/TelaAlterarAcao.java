package br.com.cesarschool.poo.titulos.telas.acao;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.mediators.MediatorAcao;

public class TelaAlterarAcao {
private JFrame frame;
private JTextField txtId;
private JTextField txtNome;
private JTextField txtValor;
private JTextField txtDataValidade;
private JButton btnIncluir;   
private MediatorAcao mediatorAcao = MediatorAcao.getInstancia();

/*
 * Launch the application.
 * @param args
 */
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            TelaAlterarAcao window = new TelaAlterarAcao();
            window.frame.setVisible(true);
        } catch (Exception e) {
            Logger.getLogger(TelaAlterarAcao.class.getName()).log(Level.SEVERE, null, e);//loga a exceção
        }
    });
}

public TelaAlterarAcao() {
    initialize();
}


private void createFrame() {
    frame = new JFrame();
    frame.setBounds(100, 100, 556, 370);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);
}
private void initialize() {
    createFrame();
    // Método para limpar os campos de texto

    int yPos = 40; // Posição inicial y
    int xLabel = 41; // Posição x para Labels
    int xTextField = 183; // Posição x para TextFields

    // COMPONENTE 1
    JLabel labelId = new JLabel("ID atual");
    labelId.setBounds(xLabel, yPos, 121, 20);
    frame.getContentPane().add(labelId);
    txtId = new JTextField();
    txtId.setBounds(xTextField, yPos, 78, 26);
    frame.getContentPane().add(txtId);
    yPos += 36; // Atualiza a posição y

    // COMPONENTE 2
    JLabel labelNome = new JLabel("Novo Nome");
    labelNome.setBounds(xLabel, yPos, 121, 20);
    frame.getContentPane().add(labelNome);
    txtNome = new JTextField();
    txtNome.setBounds(xTextField, yPos, 78, 26);
    frame.getContentPane().add(txtNome);
    yPos += 36;

    // COMPONENTE 3
    JLabel labelValor = new JLabel("Novo Valor");
    labelValor.setBounds(xLabel, yPos, 121, 20);
    frame.getContentPane().add(labelValor);
    txtValor = new JTextField();
    txtValor.setBounds(xTextField, yPos, 78, 26);
    frame.getContentPane().add(txtValor);
    yPos += 36;

    // COMPONENTE 4
    JLabel labelDataValidade = new JLabel("Data de Validade");
    labelDataValidade.setBounds(xLabel, yPos, 121, 20);
    frame.getContentPane().add(labelDataValidade);
    txtDataValidade = new JTextField();
    txtDataValidade.setBounds(xTextField, yPos, 78, 26);
    frame.getContentPane().add(txtDataValidade);
    yPos += 36;

    // COMPONENTE 5
    btnIncluir = new JButton("Alterar");
    btnIncluir.setBounds(xLabel, yPos, 90, 30);
    frame.getContentPane().add(btnIncluir);
    
    // Adicionando a ação do botão
    btnIncluir.addActionListener(e -> {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nome = txtNome.getText();
            LocalDate dataValidade = LocalDate.parse(txtDataValidade.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            double valor = Double.parseDouble(txtValor.getText());
            
            // Cria o objeto Acao aqui
            Acao acao = new Acao(id, nome, dataValidade, valor);

            // Tenta incluir a ação usando o mediador
            String msg = mediatorAcao.alterar(acao);
            if (msg == null) {
                JOptionPane.showMessageDialog(null, "Ação alterada com sucesso");
            } else {
                JOptionPane.showMessageDialog(null, msg);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar: " + ex.getMessage());
        }
    });

    
}
}