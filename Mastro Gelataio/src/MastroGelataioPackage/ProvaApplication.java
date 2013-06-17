package MastroGelataioPackage;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.border.EtchedBorder;

import javax.swing.JLabel;

public class ProvaApplication {

	private JFrame frmMain;
	private JPanel pnlMain;
	private DBMgrWrap DBMgr;
	private boolean bResizePressed;
	
	private int curX, curY;
	private int winX, winY;
	private int prevLocX, prevLocY;
	private int prevLocWidth, prevLocHeight;

	/**
	 * ToolBar
	 */
	private JToolBar toolBar;
	private JLabel lblVersione;
	private static final String sVersione = "1.00";
	private JButton btnNuovaRicetta;
	private JButton btnRicettario;
	private JButton btnIngredienti;
	private JButton btnImpostazioni;
	private JButton btnHelp;
	private JButton btnExit;
	private JButton btnMin;
	private JButton btnMax;
	
	/**
	 * Pannello Ingredienti
	 */
	private IngredientiPanel pnlIngredienti;
	
	/**
	 * Pannello Nuova Ricetta
	 */
	private RicettaPanel pnlNuovaRicetta;
	
	/**
	 * Pannelli Ricettario
	 */
	private Ricettario pnlRicettario;
	
	/**
	 * Pannello Impostazioni
	 */
	private ImpostazioniPanel pnlImpostazioni;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProvaApplication window = new ProvaApplication();
					window.frmMain.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ProvaApplication() {
		
		try
		{
			UIManager.setLookAndFeel("com.easynth.lookandfeel.EaSynthLookAndFeel");
		}
		catch (Exception e){}
		/**
		 * Inizializzazione variabili 
		 */
		InizializzaVariabili();
		
		/**
		 * Connessione al DB
		 */
		if (!ConnectDB())
		{
			return;
		}
		
		/** 
		 * Creo JFrame & JPanel principale
		 */
		CreateJFrame();
		
		/** 
		 * Creo JToolBar
		 */
		CreateJToolBar();
		
		/** 
		 * Creo Pannello Ingredienti
		 */
		CreatePnlIngredienti();
		
		/** 
		 * Creo Pannello Nuova Ricetta
		 */
		CreatePnlNuovaRicetta();
		
		/** 
		 * Creo Pannelli Ricette
		 */
		CreatePnlRicettario();
		
		/** 
		 * Creo Pannello Impostazioni
		 */
		CreatePnlImpostazioni();
		
		/** 
		 * Creo Pannello Help
		 */
		CreatePnlHelp();
	}
	
	/**
	 * Inizializza le variabili
	 */
	private void InizializzaVariabili()
	{
		DBMgr = new DBMgrWrap();
		
		Locale.setDefault(Locale.ITALIAN); 
	}
	
	/**
	 * Crea il JFrame principale
	 */
	private void CreateJFrame()
	{
		bResizePressed = false;
		
		frmMain = new JFrame();
		frmMain.setBounds(100, 100, 610, 300);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMain.setUndecorated(true);
		frmMain.getContentPane().setLayout(null);
		
		pnlMain = new JPanel();
		pnlMain.setBounds(0, 0, frmMain.getWidth(), frmMain.getHeight());
		pnlMain.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"pippo"));
		pnlMain.setLayout(null);
		frmMain.getContentPane().add(pnlMain);
		
		pnlMain.addMouseListener(new MouseAdapter(){
			
			public void mousePressed(MouseEvent m) {
				if(pnlMain.getCursor() != Cursor.getDefaultCursor())
				{
					bResizePressed = true;
				}
			}
			
			public void mouseReleased(MouseEvent m) {
				bResizePressed = false;
			}
		});
		
		pnlMain.addMouseMotionListener(new MouseAdapter(){
			
			public void mouseMoved(MouseEvent m) {
				if ((m.getX() > pnlMain.getWidth() - 8) && (m.getY() > pnlMain.getHeight() - 8))
				{
					pnlMain.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
				}
				else if ((m.getX() > pnlMain.getWidth() - 8) && (m.getY() < pnlMain.getHeight() - 8))
				{
					pnlMain.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
				}
				else if((m.getX() < pnlMain.getWidth() - 8) && (m.getY() > pnlMain.getHeight() - 8))
				{
					pnlMain.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
				}
				else
				{
					pnlMain.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
			
			public void mouseDragged(MouseEvent m) {
				if(bResizePressed == true)
				{
					if(pnlMain.getCursor() == Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR))
					{
						if (m.getX() > toolBar.getMinimumSize().width)
						{
							frmMain.setBounds(frmMain.getX(), frmMain.getY(), m.getX(), m.getY());
						}
						else
						{
							frmMain.setBounds(frmMain.getX(), frmMain.getY(), frmMain.getWidth(), m.getY());
						}
					}
					else if (pnlMain.getCursor() == Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR))
					{
						if (m.getX() > toolBar.getMinimumSize().width)
						{
							frmMain.setBounds(frmMain.getX(), frmMain.getY(), m.getX(), frmMain.getHeight());
						}
						else
						{
							frmMain.setBounds(frmMain.getX(), frmMain.getY(), frmMain.getWidth(), frmMain.getHeight());
						};
					}
					else if (pnlMain.getCursor() == Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR))
					{
						frmMain.setBounds(frmMain.getX(), frmMain.getY(), frmMain.getWidth(), m.getY());
					}
					CambiaSizeTutto(null);
				}
			}
		});
		
		/**
		 * Listener per ridimensionare la finestra
		 */
		frmMain.addComponentListener(new ComponentListener() {
			
			public void componentResized (ComponentEvent evt) {
				CambiaSizeTutto(evt);
			}
			public void componentHidden (ComponentEvent evt) {}
			public void componentMoved (ComponentEvent evt) {}
			public void componentShown (ComponentEvent evt) {}
		});
	}
	
	/**
	 * Crea la JToolBar con i bottoni
	 */
	private void CreateJToolBar()
	{
		/**
		 * Creo la il contenitore JToolBar
		 */
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBounds(0, 0, frmMain.getWidth(), 60);
		toolBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		pnlMain.add(toolBar);
		
		
		toolBar.addMouseListener(new MouseAdapter(){
			
			public void mousePressed(MouseEvent m) {
				curX = m.getXOnScreen();
				curY = m.getYOnScreen();
				winX = frmMain.getX();
				winY = frmMain.getY();
			}
		});
		
		toolBar.addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent m) {
				frmMain.setLocation(winX + m.getXOnScreen() - curX, winY + m.getYOnScreen() - curY);
			}
		});
		
		
		/**
		 * Creo il bottone Ricettario
		 */
		btnRicettario = new JButton("");
		btnRicettario.setIcon(new ImageIcon(".\\icons\\Ricettario2.png"));
		btnRicettario.setToolTipText("Ricettario");
		toolBar.add(btnRicettario);
		
		btnRicettario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnRicettarioSelected();
			}
		});
		
		/**
		 * Creo il bottone Nuova Ricetta
		 */
		btnNuovaRicetta = new JButton("");
		btnNuovaRicetta.setIcon(new ImageIcon(".\\icons\\NewDocument2.png"));
		btnNuovaRicetta.setToolTipText("Crea/Modifica Ricetta");
		toolBar.add(btnNuovaRicetta);
		
		btnNuovaRicetta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnNuovaRicettaSelected();
			}
		});
		
		/**
		 * Creo il bottone Ingredienti
		 */
		btnIngredienti = new JButton("");
		btnIngredienti.setIcon(new ImageIcon(".\\icons\\Ingredienti2.png"));
		btnIngredienti.setToolTipText("Ingredienti");
		toolBar.add(btnIngredienti);
		
		btnIngredienti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnIngredientiSelected();
			}
		});
		
		/**
		 * Creo il bottone Impostazioni
		 */
		btnImpostazioni = new JButton("");
		btnImpostazioni.setIcon(new ImageIcon(".\\icons\\Settings2.png"));
		btnImpostazioni.setToolTipText("Impostazioni");
		toolBar.add(btnImpostazioni);
		
		btnImpostazioni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnImpostazioniSelected();
			}
		});
		
		/**
		 * Creo il bottone Help
		 */
		btnHelp = new JButton("");
		btnHelp.setIcon(new ImageIcon(".\\icons\\Settings2.png"));
		btnHelp.setToolTipText("Help");
		toolBar.add(btnHelp);
		
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnHelpSelected();
			}
		});
		
		/**
		 * Creo la JLabel per la versione del programma
		 */
		toolBar.add(Box.createHorizontalGlue());
		lblVersione = new JLabel("Mastro Gelataio " + sVersione);
		lblVersione.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		toolBar.add(lblVersione);
		
		/**
		 * Creo lo spazio per allineare sulla destra i JButton che seguono
		 */
		toolBar.add(Box.createHorizontalGlue());
		
		/**
		 * Creo il bottone per Minimizzare
		 */
		btnMin = new JButton("");
		btnMin.setIcon(new ImageIcon(".\\icons\\Minimize2.png"));
		btnMin.setToolTipText("Minimizza");
		toolBar.add(btnMin);
		
		btnMin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnMinimizeSelected();
			}
		});
		
		/**
		 * Creo il bottone per Massimizzare
		 */
		btnMax = new JButton("");
		btnMax.setIcon(new ImageIcon(".\\icons\\Maximize2.png"));
		btnMax.setToolTipText("Massimizza");
		toolBar.add(btnMax);
		
		btnMax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnMaximizeSelected();
			}
		});
		
		/**
		 * Creo il bottone Exit
		 */
		btnExit = new JButton("");
		btnExit.setIcon(new ImageIcon(".\\icons\\Close2.png"));
		btnExit.setToolTipText("Esci");
		toolBar.add(btnExit);
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnExitSelected();
			}
		});
		
		/**
		 * Imposto la minima dimensione della toolbar
		 */
		Dimension d = new Dimension();
		d.setSize((40 * 4) + 300 + (50 * 3), 60);
		toolBar.setMinimumSize(d);
	}
	
	/**
	 * Crea il JPanel degli Ingredienti
	 */
	private void CreatePnlIngredienti()
	{
		pnlIngredienti = new IngredientiPanel(DBMgr);
		pnlMain.add(pnlIngredienti);
		pnlIngredienti.setVisible(false);
	}
	
	/**
	 * Crea il JPanel della Nuova Ricetta
	 */
	private void CreatePnlNuovaRicetta()
	{
		pnlNuovaRicetta = new RicettaPanel(DBMgr);
		pnlNuovaRicetta.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		pnlMain.add(pnlNuovaRicetta);
		pnlNuovaRicetta.setVisible(false);
	}
	
	/**
	 * Crea i JPanel delle Ricette
	 */
	private void CreatePnlRicettario()
	{	
		pnlRicettario = new Ricettario(DBMgr);
		pnlMain.add(pnlRicettario);
		pnlRicettario.setVisible(false);
	}
	
	/**
	 * Crea il JPanel delle Impostazioni 
	 */
	private void CreatePnlImpostazioni()
	{
		pnlImpostazioni = new ImpostazioniPanel(DBMgr);
		pnlMain.add(pnlImpostazioni);
		pnlImpostazioni.setVisible(false);
	}
	
	/**
	 * Crea il JPanel dell'Help 
	 */
	private void CreatePnlHelp()
	{
		
	}
	
	/**
	 * Funzione associata al Listener del JFrame per ridimensionare tutti i componenti
	 * @param evt
	 */
	private void CambiaSizeTutto(ComponentEvent evt)
	{
		/**
		 * Cambia dimensioni del JPanel principale
		 */
		pnlMain.setBounds(0, 0, frmMain.getWidth(), frmMain.getHeight());
		
		/**
		 * Riaggiusta le dimensioni della JToolBar.
		 */
		toolBar.setBounds(0, 0, frmMain.getWidth(), 60);
		
		/**
		 * Cambia dimensioni del JPanel del Ricettario
		 */
		pnlRicettario.CambiaSize(0, toolBar.getHeight() + 1, frmMain.getWidth(), frmMain.getHeight() - toolBar.getHeight());
		
		/**
		 * Cambia dimensioni del JPanel degli Ingredienti
		 */
		pnlIngredienti.CambiaSize(20, toolBar.getHeight() + 30, frmMain.getWidth() - 40, frmMain.getHeight() - toolBar.getHeight() - 50);
		
		/**
		 * Cambia dimensioni del JPanel della Nuova Ricetta
		 */
		pnlNuovaRicetta.CambiaSize(20, toolBar.getHeight() + 30, frmMain.getWidth() - 40, frmMain.getHeight() - toolBar.getHeight() - 50);
		
		/**
		 * Cambia dimensioni del JPanel delle Impostazioni
		 */
		pnlImpostazioni.CambiaSize(0, toolBar.getHeight() + 1, frmMain.getWidth(), frmMain.getHeight() - toolBar.getHeight());
	}
	
	/**
	 * Funzione associata al Listener del bottone Ricettario della JToolBar
	 */
	private void btnRicettarioSelected()
	{
		/**
		 * Se il pannello correntemente aperto è quello degli ingredienti controllo se sono state
		 * fatte delle modifiche e chiedo conferma.
		 */
		if (pnlIngredienti.IsModified() == true)
		{
			int option = JOptionPane.showConfirmDialog(null, "Le modifiche alla lista ingredienti verrano perse. Continuare?", "Attenzione", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.NO_OPTION)
			{
				return;	
			}
		}
		
		pnlRicettario.CaricaRicette();
		
		pnlRicettario.setVisible(true);
		pnlIngredienti.setVisible(false);
		pnlNuovaRicetta.setVisible(false);
		pnlImpostazioni.setVisible(false);
	}
	
	/**
	 * Funzione associata al Listener del bottone Ingredienti della JToolBar
	 */
	private void btnIngredientiSelected()
	{
		pnlIngredienti.CaricaIngredienti();
		
		pnlNuovaRicetta.setVisible(false);
		pnlIngredienti.setVisible(true);
		pnlRicettario.setVisible(false);
		pnlImpostazioni.setVisible(false);
	}
	
	/**
	 * Funzione associata al Listener del bottone Nuova Ricetta della JToolBar
	 */
	private void btnNuovaRicettaSelected()
	{
		/**
		 * Se il pannello correntemente aperto è quello degli ingredienti controllo se sono state
		 * fatte delle modifiche e chiedo conferma.
		 */
		if (pnlIngredienti.IsModified() == true)
		{
			int option = JOptionPane.showConfirmDialog(null, "Le modifiche alla lista ingredienti verrano perse. Continuare?", "Attenzione", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.NO_OPTION)
			{
				return;	
			}
		}
		
		pnlNuovaRicetta.CaricaPannello();
		
		pnlRicettario.setVisible(false);
		pnlIngredienti.setVisible(false);
		pnlNuovaRicetta.setVisible(true);
		pnlImpostazioni.setVisible(false);
	}
	
	/**
	 * Funzione associata al Listener del bottone Impostazioni della JToolBar
	 */
	private void btnImpostazioniSelected()
	{		
		/**
		 * Se il pannello correntemente aperto è quello degli ingredienti controllo se sono state
		 * fatte delle modifiche e chiedo conferma.
		 */
		if (pnlIngredienti.IsModified() == true)
		{
			int option = JOptionPane.showConfirmDialog(null, "Le modifiche alla lista ingredienti verrano perse. Continuare?", "Attenzione", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.NO_OPTION)
			{
				return;	
			}
		}
		
		pnlRicettario.setVisible(false);
		pnlIngredienti.setVisible(false);
		pnlNuovaRicetta.setVisible(false);
		pnlImpostazioni.setVisible(true);
	}
	
	/**
	 * Funzione associata al Listener del bottone Help della JToolBar
	 */
	private void btnHelpSelected()
	{
		
	}
	
	private void btnMinimizeSelected()
	{
		frmMain.setExtendedState(JFrame.ICONIFIED);
		CambiaSizeTutto(null);
	}
	
	private void btnMaximizeSelected()
	{
		Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		
		if(frmMain.getSize().equals(r.getSize()))
		{
			frmMain.setLocation(prevLocX, prevLocY);
			frmMain.setSize(prevLocWidth, prevLocHeight);
		}
		else
		{
			prevLocX = frmMain.getX();
			prevLocY = frmMain.getY();
			prevLocWidth = frmMain.getWidth();
			prevLocHeight = frmMain.getHeight();
			
			frmMain.setLocation(0, 0);
			frmMain.setSize(r.getSize());
		}
		CambiaSizeTutto(null);
	}
	
	private void btnExitSelected()
	{
		System.exit(0);
	}
	
	private boolean ConnectDB()
	{
		try
		{
			DBMgr.ConnectDB();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}  
