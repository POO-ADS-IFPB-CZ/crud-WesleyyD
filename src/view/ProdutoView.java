package view;

import dao.GenericDao;
import model.Produto;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Set;

public class ProdutoView extends JFrame {

    private JTextField txtCodigo, txtDescricao, txtPreco;
    private JButton btnSalvar, btnListar, btnExcluir, btnAtualizar;
    private JTextArea textArea;
    private GenericDao<Produto> dao;

    public ProdutoView() {
        super("Cadastro de Produtos");

        try {
            dao = new GenericDao<>("produtos.dat");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao acessar arquivo: " + e.getMessage());
            System.exit(1);
        }


        setLayout(new BorderLayout());

        JPanel painelCampos = new JPanel(new GridLayout(4, 2));
        painelCampos.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        painelCampos.add(txtCodigo);

        painelCampos.add(new JLabel("Descrição:"));
        txtDescricao = new JTextField();
        painelCampos.add(txtDescricao);

        painelCampos.add(new JLabel("Preço:"));
        txtPreco = new JTextField();
        painelCampos.add(txtPreco);

        btnSalvar = new JButton("Salvar");
        btnListar = new JButton("Listar");
        btnExcluir = new JButton("Excluir");
        btnAtualizar = new JButton("Atualizar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnAtualizar);

        textArea = new JTextArea(10, 40);
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);

        add(painelCampos, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);


        btnSalvar.addActionListener(e -> salvarProduto());
        btnListar.addActionListener(e -> listarProdutos());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnAtualizar.addActionListener(e -> atualizarProduto());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Produto obterProdutoDosCampos() {
        try {
            int codigo = Integer.parseInt(txtCodigo.getText().trim());
            String descricao = txtDescricao.getText().trim();
            double preco = Double.parseDouble(txtPreco.getText().trim());
            return new Produto(codigo, descricao, preco);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dados inválidos");
            return null;
        }
    }

    private void salvarProduto() {
        Produto p = obterProdutoDosCampos();
        if (p != null) {
            try {
                if (dao.salvar(p)) {
                    JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Produto já existe!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            }
        }
    }

    private void listarProdutos() {
        try {
            Set<Produto> produtos = dao.getAll();
            textArea.setText("");

            if (produtos.isEmpty()) {
                textArea.setText("Nenhum produto cadastrado.");
                return;
            }

            for (Produto p : produtos) {
                textArea.append("CÓDIGO: " + p.getCodigo() + "\n");
                textArea.append("DESCRIÇÃO: " + p.getDescricao() + "\n");
                textArea.append("PREÇO: R$ " + String.format("%.2f", p.getPreco()) + "\n");
                textArea.append("\n");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar: " + ex.getMessage());
        }
    }


    private void excluirProduto() {
        try {
            int codigo = Integer.parseInt(txtCodigo.getText().trim());

            Produto p = new Produto(codigo, "", 0);

            if (dao.remover(p)) {
                JOptionPane.showMessageDialog(this, "Produto removido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código inválido!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
        }
    }


    private void atualizarProduto() {
        Produto p = obterProdutoDosCampos();
        if (p != null) {
            try {
                if (dao.atualizar(p)) {
                    JOptionPane.showMessageDialog(this, "Produto atualizado!");
                } else {
                    JOptionPane.showMessageDialog(this, "Produto não encontrado!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new ProdutoView();
    }
}
