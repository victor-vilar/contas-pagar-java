package br.com.thveiculos.erp.views.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.thveiculos.erp.views.despesas.CategoriaDespesaView;
import br.com.thveiculos.erp.views.despesas.DespesaAvulsaViewImpl;
import br.com.thveiculos.erp.views.despesas.DespesaRecorrenteViewImpl;
import br.com.thveiculos.erp.views.despesas.FormaPagamentoView;
import br.com.thveiculos.erp.views.despesas.MovimentoPagamentoView;

@Component
public class MainView extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private ApplicationContext context;
	private JPanel contentPane;
	private JButton btnFormaPagamento;
	private JButton btnCategoriaDespesa;
        private JButton btnMovimentoPagamento;
        private JButton btnDespesaAvulsa;
        private JButton btnDespesaRecorrente;

	@Autowired
	public void MainView(ApplicationContext context) {
		this.context = context;
		this.setUp();
	}
	
	/**
	 * Create the frame.
	 */
	public void setUp() {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(100, 100, 476, 345);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

            setContentPane(contentPane);

            btnFormaPagamento = new JButton("Forma Pagamento");
            btnFormaPagamento.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    var view = context.getBean(FormaPagamentoView.class);
                    view.setVisible(true);
                }
            });

            btnCategoriaDespesa = new JButton("Categorias Despesa");
            btnCategoriaDespesa.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    var view = context.getBean(CategoriaDespesaView.class);
                    view.setVisible(true);
                }
            });

            btnMovimentoPagamento = new JButton("MovimentoPagamento");
            btnMovimentoPagamento.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    var view = context.getBean(MovimentoPagamentoView.class);
                    view.setVisible(true);
                }
            });
 
            btnDespesaAvulsa = new JButton("Despesa Avulsa");
            btnDespesaAvulsa.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    var view = context.getBean(DespesaAvulsaViewImpl.class);
                    view.setVisible(true);
                }
            });
            
            btnDespesaRecorrente = new JButton("Despesa Recorrente");
            btnDespesaRecorrente.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    var view = context.getBean(DespesaRecorrenteViewImpl.class);
                    view.setVisible(true);
                }
            });
		
		contentPane.add(btnFormaPagamento);
		contentPane.add(btnCategoriaDespesa);
                contentPane.add(btnMovimentoPagamento);
                contentPane.add(btnDespesaAvulsa);
                contentPane.add(btnDespesaRecorrente);
                
		
		
		

	}
}
