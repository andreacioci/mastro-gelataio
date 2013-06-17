package MastroGelataioPackage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SelezionaCreaModificaRicetta extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SelezionaCreaModificaRicetta dialog = new SelezionaCreaModificaRicetta();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SelezionaCreaModificaRicetta() {
		setBounds(200, 200, 450, 300);
		//getContentPane().setLayout(null);
		//contentPanel.setLayout(new FlowLayout());
		//contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setUndecorated(true);
		//getContentPane().add(contentPanel);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(null);
		getContentPane().add(buttonPane);
			
		JButton btnNuova = new JButton("Nuova Ricetta");
		btnNuova.setBounds(40, 120, 60, 60);
		btnNuova.setIcon(new ImageIcon(".\\icons\\Minimize2.png"));
		//btnNuova.setActionCommand("Nuova Ricetta");
		buttonPane.add(btnNuova);
		getRootPane().setDefaultButton(btnNuova);
			
			
		JButton btnModifica = new JButton("Modifica Ricetta");
		//btnModifica.setActionCommand("Modifica Ricetta");
		buttonPane.add(btnModifica);
			
			
		JButton btnCancella = new JButton("Cancella");
		//btnCancella.setActionCommand("Cancella");
		buttonPane.add(btnCancella);
	}

}
