import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;



/**
 *  Tetris-luokka huolehtii koko ohjelman graafisesta toteutuksesta sek‰
 *  alaluokkiensa toiminnasta.
 * @author 290289
 */
public class Tetrisorig extends JFrame
{
	// ATTRIBUUTIT
	private Pelipaneeli pelipaneeli;
	private JPanel alapaneeli;
	private JMenuBar valikkorivi;
	private JMenu pelimenu;
	private JMenuItem lopeta_peli;
	private JMenuItem aloita_alusta;
	private JMenuItem pause;
	private JMenuItem nayta_ennatykset;
	private Kuuntelija menukuuntelija;
	private JLabel alapaneeliteksti;
	private Peliruudukko matrix;
	private Ajoittaja ajoittaja;
	private JPanel pistepaneeli;
	private JLabel pistelabel;
	private int stepaika;
	private JPanel reunapaneeli;
	private Sivupaneeli sivupaneeli;
	private JButton seisnappi;
	private JPanel tietopaneeli;
	private JLabel aikalabel;
	private JLabel rivilabel;
	
	
	private boolean keskeytetty;
	// Ajanlaskuun liittyv‰t muuttujat
	private long aloitusaika;
	private long viimeaika;
	private long peliaika;
	
	/**
	 * Konstruktori luo uuden Tetris-olennon ja kaikki sen sis‰lt‰m‰t
	 * komponentit kuten peliruudukon ja pelipaneelin
	 * 
	 * Metodi myˆs asettaa kaikille komponenteille koon ja n‰kyvyyden
	 */
	// KONSTRUKTORI
	public Tetrisorig()
	{
		
		// Asettaa vaikeuden l‰htˆtason
		stepaika = 1000;
		keskeytetty = false;
		
		// Asettaa myˆs ajanlaskun kohdalleen
		aloitusaika = System.currentTimeMillis();
		viimeaika = 0;
		peliaika = 0;
		
		// Luodaan peliruudukko
		matrix = new Peliruudukko(10, 20);
		// Luodaan uusi paneeli peliruudukon pohjalta
		pelipaneeli = new Pelipaneeli(matrix, this);
		
		// Luodaan ajoittaja pelin etenemist‰ varten
		ajoittaja = new Ajoittaja(stepaika);
		

		// Luodaan menu
		pelimenu = new JMenu("Peli");

		// Luodaan kuuntelijat
		menukuuntelija = new Kuuntelija();

		// Luodaan "Lopeta-peli, Aloita-alusta, pause
		// ja n‰yt‰ enn‰tykset -toiminnot"
		lopeta_peli = new JMenuItem("Lopeta peli");
		lopeta_peli.addActionListener(menukuuntelija);
		lopeta_peli.setActionCommand("lopeta");

		aloita_alusta = new JMenuItem("Aloita alusta");
		aloita_alusta.addActionListener(menukuuntelija);
		aloita_alusta.setActionCommand("alusta");

		pause = new JMenuItem("Pys‰yt‰ peli");
		pause.addActionListener(menukuuntelija);
		pause.setActionCommand("pause");
		
		nayta_ennatykset = new JMenuItem("N‰yt‰ enn‰tykset");
		nayta_ennatykset.addActionListener(menukuuntelija);
		nayta_ennatykset.setActionCommand("nayta");


		// Lis‰t‰‰n menuitemit menuun
		pelimenu.add(aloita_alusta);
		pelimenu.addSeparator();
		pelimenu.add(lopeta_peli);
		pelimenu.addSeparator();
		pelimenu.add(pause);
		pelimenu.addSeparator();
		pelimenu.add(nayta_ennatykset);

		// Lis‰t‰‰n menu valikkoriviin
		valikkorivi = new JMenuBar();
		valikkorivi.add(pelimenu);
		valikkorivi.setVisible(true);
		
		
		// Luodaan hauska alapaneeli
		alapaneeli = new JPanel(new BorderLayout());
		alapaneeli.setSize(250, 32);
		alapaneeli.setPreferredSize(alapaneeli.getSize());
		alapaneeli.setMinimumSize(alapaneeli.getSize());
		alapaneeli.setBackground(java.awt.Color.YELLOW);
		
		alapaneeliteksti = new JLabel("Peli on k‰ynniss‰", JLabel.CENTER);
		alapaneeli.add(alapaneeliteksti);
		
		// Luodaan kiva reunapaneeli ja lis‰t‰‰n se tetrikseen
		reunapaneeli = new JPanel(new BorderLayout());
		reunapaneeli.setSize(175, 600);
		reunapaneeli.setPreferredSize(reunapaneeli.getSize());
		reunapaneeli.setMinimumSize(reunapaneeli.getSize());
		reunapaneeli.setBackground(Color.WHITE);
		reunapaneeli.setVisible(true);
		reunapaneeli.setFocusable(false);
		reunapaneeli.setFocusTraversalKeysEnabled(false);
		
		// Luodaan kaikista kivoin sivupaneeli ja laitetaan se reunapaneeliin
		sivupaneeli = new Sivupaneeli(matrix);
		reunapaneeli.add(sivupaneeli, BorderLayout.NORTH);
		// Lis‰t‰‰n myˆs reunapaneeli tetrikseen
		add(reunapaneeli, BorderLayout.EAST);
		
		// Luodaan paussinappula ja sijoitetaan se reunapaneeliin
		seisnappi = new JButton("SEIS!");
		seisnappi.addActionListener(menukuuntelija);
		seisnappi.setVisible(true);	
		seisnappi.setFocusable(false);
		seisnappi.setFocusTraversalKeysEnabled(false);	
		seisnappi.setSize(96, 32);
		seisnappi.setPreferredSize(seisnappi.getSize());
		seisnappi.setMinimumSize(seisnappi.getSize());
		seisnappi.setBackground(Color.YELLOW);
		seisnappi.setActionCommand("seis");
		reunapaneeli.add(seisnappi, BorderLayout.SOUTH);
		
		// Luodaan samalla tavalla myˆs pistepaneeli
		pistepaneeli = new JPanel(new BorderLayout());
		pistepaneeli.setSize(250, 16);
		pistepaneeli.setPreferredSize(pistepaneeli.getSize());
		pistepaneeli.setMinimumSize(pistepaneeli.getSize());
		pistepaneeli.setBackground(java.awt.Color.YELLOW);

		pistelabel = new JLabel("Pisteet: " + 
				matrix.annaPisteet(), JLabel.CENTER);
		pistepaneeli.add(pistelabel);
		
		// Luodaan hauska aikalabel ja rivilabel, jotka lisataan  tietopaneeliin
		aikalabel = new JLabel("Aika: " + peliaika, JLabel.CENTER);
		rivilabel = new JLabel("Poistetut rivit: " + matrix.annaPoistetutRivit());
		
		// Kolmanneksi luodaan paneeli, johon sijoitetaan ker‰tyt pisteet,
		// kulunut aika (ja poistetut rivit)
		tietopaneeli = new JPanel();
		BoxLayout boxi = new BoxLayout(tietopaneeli, BoxLayout.Y_AXIS);
		tietopaneeli.setLayout(boxi);
		tietopaneeli.setSize(96, 96);
		tietopaneeli.setPreferredSize(pistepaneeli.getSize());
		tietopaneeli.setMinimumSize(pistepaneeli.getSize());
		tietopaneeli.setBackground(java.awt.Color.WHITE);

		//tietopaneeli.add(pistelabel);
		tietopaneeli.add(aikalabel);
		tietopaneeli.add(rivilabel);
		// Lis‰t‰‰n paneeli reunapaneeliin
		reunapaneeli.add(tietopaneeli, BorderLayout.CENTER);
		
		
		// Lis‰t‰‰n pelipaneeli ja valikkorivi tetrikseen
		add(pelipaneeli, BorderLayout.CENTER);
		setJMenuBar(valikkorivi);
		// Ja alapaneeli sek‰ pistepaneeli
		add(alapaneeli, BorderLayout.SOUTH);
		add(pistepaneeli, BorderLayout.NORTH);
		
		
		
		// S‰‰det‰‰n ite tetrist‰
		setTitle("Meitsin p‰hee tetris");
		
		setSize(475, 664);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	
	// METODIT
	
	/**
	 * Pys‰ytt‰‰ pelin kulun, kunnes k‰ytet‰‰n resetAika()-metodia
	 */
	// Pyt‰ytt‰‰ pelin
	public void pysaytaAika()
	{
		ajoittaja.pysayta();
		asetaAlaTeksti("PAUSE");
		pause.setText("Jatka peli‰");
		keskeytetty = true;
		
		// P‰ivitt‰‰ ajanlaskun
		viimeaika = peliaika;
	}
	
	// Jatkaa peli‰ (Huom. jos aikav‰liksi annetaan 0, ei muuta aikav‰li‰)
	/**
	 * K‰ynnist‰‰ pelin kulun uudestaan, niin ett‰ peli kulkee taas
	 * eteenp‰in.
	 * 
	 * @param aikavali asettaa uuden etenemisnopeuden. 0:n laittaminen
	 * parametriksi saa pelin jatkumaan samalla nopeudella.
	 */
	public void resetAika(int aikavali)
	{
		ajoittaja.reset(aikavali);
		
		// Jos ei haluta jatkaa samaan tahtiin niin p‰ivitt‰‰ myˆs tahdin
		if (aikavali != 0)
			stepaika = aikavali;
		
		asetaAlaTeksti("Peli on k‰ynniss‰");
		pause.setText("Pys‰yt‰ peli");
		
		keskeytetty = false;
		
		// P‰ivitt‰‰ ajanlaskun
		aloitusaika = System.currentTimeMillis();
	}
	
	// Pys‰ytt‰‰ ajan tai aloittaa jatkaa sit‰ riippuen kummassa menn‰‰n
	/**
	 * Pause joko pys‰ytt‰‰ ajan tai jatkaa ajan kulkua siit‰, mihin ennen
	 * pys‰ytyst‰ j‰‰tiin, riippuen siit‰, mik‰ tilanne oli ennen metodin
	 * kutsumista
	 */
	public void pause()
	{
		if (ajoittaja.onPaalla())
			pysaytaAika();
		else
			resetAika(0);
	}
	
	/**
	 * Kertoo kysyj‰lle nykyisen pelinetenemisnopeuden
	 * @return nykyinen pelinetenemisnopeus
	 */
	public int annaStepaika()
	{
		return stepaika;
	}
	
	/**
	 * Kertoo, onko peli keskeytetty vai ei...
	 * @return onko peli kesken
	 */
	public boolean onKeskeytetty()
	{
		return keskeytetty;
	}
	
	// Muuttaa ajanlaskua niin, ett‰ l‰hdet‰‰n laskemaan j‰lleen nollasta
	// T‰t‰ voidaan k‰ytt‰‰ esimerkiksi pelin uudelleenaloituksessa.
	/**
	 * Nollaa t‰h‰nasti kulutetun ajan.
	 */
	public void ajanlaskuAlusta()
	{
		viimeaika = 0;
		aloitusaika = System.currentTimeMillis();
	}
	
	// Asettaa alapaneelille uuden tekstin
	private void asetaAlaTeksti(String uusiteksti)
	{
		alapaneeliteksti.setText(uusiteksti);
	}
	
	
	// SUBCLASSIT
	
	/**
	 * Kuuntelija kuunttelee tetriksen sis‰lt‰mien menuitemien klikkauksia
	 * ja toimii n‰iden signaalien mukaan.
	 * 
	 * @author 290289
	 */
	class Kuuntelija implements ActionListener
	{
		
		// Mit‰ tekee kun joku klikkaa menua
		/**
		 * Toteuttaa menunklikkauksen aiheuttaman toiminnon riippuen siit‰,
		 * mit‰ menua klikattiin.
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String komento = e.getActionCommand();
			
			// Riippuen klikatusta joko lopettaa
			if (komento.equals("lopeta"))
				System.exit(0);
			// Tai alottaa alusta
			else if (komento.equals("alusta"))
				pelipaneeli.aloitaAlusta();
			// Tai pys‰ytt‰‰ pelin
			else
				pause();
		}
	}

	
	// Timerin kuuntelija ja toteuttaja
	/**
	 * Ajoittaja hoitaa pelin eteenp‰in viemisen. Se p‰ivit‰‰ n‰yttˆ‰
	 * tietyin v‰liajoin ja hoitaa palikoiden tiputuksen ja uusien palojen
	 * tuomisen ruudulle. Lis‰ksi Ajoittaja hoitaa pelin lopettamisen ja
	 * enn‰tysikkunan n‰ytt‰misen.
	 * @author 290289
	 */
	class Ajoittaja implements ActionListener
	{	
		/*
		 * T‰‰ teht‰v‰nanto on aika ep‰selv‰.
		 * Jos kerran n‰yttˆ‰ ei saa p‰ivitt‰‰
		 * muualta kuin pelipaneelista ja timerin toteuttajaan on p‰‰sy ainoas-
		 * taan tetris luokasta niin mill‰ hitsill‰ tota pystyy p‰ivitt‰‰ teke-
		 * m‰tt‰ kahta timerin toteuttajaa (mik‰ on typer‰‰ haaskausta!)
		 * Miksei t‰st‰ a) voi tehd‰ omaa luokkaa, joka k‰ytt‰‰ listenerin‰
		 * pelipaneelia, tai b) saa oikeuksia nayta()-metodin k‰yttˆˆn???
		 * 
		 * Mun t‰ytyy ratkaista t‰‰ ylim‰‰r‰inen hauskuus kuitenkin jollain
		 * tavalla, joten k‰yt‰n nayta()-metodia nyt tehht‰v‰nannonvastaisesti
		 * publiccina.
		 * 
		 * Toisekseen, mik‰ ajatus "Timerin pys‰ytt‰misell‰" on? Pelih‰n
		 * jatkuu (ainakin normaalissa tetriksess‰) ihan tavalliseen tapaan,
		 * vaikka palikka osuisikin maahan...
		 */
		
		private Timer time;
		private boolean on_paalla;
		
		/**
		 * Luo uuden ajoittajan ja laittaa sen p‰‰lle
		 * @param aikavali m‰‰ritt‰‰, kuinka usein ajoittaja tiputtaa palikoita
		 */
		private Ajoittaja(int aikavali)
		{
			// Luo uuden timerin, jota sitten kuunnellaan
			time = new Timer(aikavali, this);
			//time.setInitialDelay(500);
			time.start();
			
			on_paalla = true;
		}
		
		// Pys‰ytt‰‰ pelin etenemisen hetkeksi
		private void pysayta()
		{
			time.stop();
			on_paalla = false;
		}
		
		// K‰ynnist‰‰ ajastimen uudestaan tietyll‰ aikav‰lill‰
		private void reset(int aikavali)
		{
			// Jos aikav‰li on nolla, pit‰‰ saman aikav‰lin
			if (aikavali != 0)
				time.setDelay(aikavali);
			time.start();
			on_paalla = true;
		}
		
		// Kertoo, onko ajastin k‰ynniss‰
		private boolean onPaalla()
		{
			return on_paalla;
		}

		// Siirt‰‰ tippuvaa palikka alas(, mik‰li mahdollista)
		// Ja p‰ivitt‰‰ n‰ytˆn. Ilmaisee myˆs pelin p‰‰ttymisen
		@Override
		/**
		 * Ajoittaja ajaa t‰m‰n metodin tietyin v‰liajoin. T‰‰ll‰ ajoittaja
		 * tiputtaa aktiivista palikkaa ja hoitaa uusien palikoiden tiputuksen.
		 * Lis‰ksi ajoittaja tarkistaa pelin loppumisen ja huolehtii
		 * siihen liittyvist‰ teht‰vist‰
		 */
		public void actionPerformed(ActionEvent e)
		{	
			// P‰ivitet‰‰n pistelaskija ja rivilaskija
			pistelabel.setText("Pisteet: " + matrix.annaPisteet() + " ( +" + 
					matrix.annaPistesaalis() + ")");
			rivilabel.setText("Poistetut rivit: " + matrix.annaPoistetutRivit());
			
			// P‰ivitet‰‰n nykyinen peliaika, mik‰li peli‰ ei ole keskeytetty
			if (!keskeytetty)
			{
				peliaika = System.currentTimeMillis() - aloitusaika + viimeaika;
				
				// P‰ivitet‰‰n aikalaskija n‰ytˆll‰
				aikalabel.setText("Aika: " + peliaika/1000);
			}
			
			/* Tarkistetaan, onko peli p‰‰ttynyt
			 * Huom: k‰yt‰n t‰ss‰ loppuikoPeli()-metodia, sill‰ se p‰ivittyy
			 * Peliruudukko-luokassa hieman tehokkaammin kuin tippuva
			 * (Lis‰ksi sit‰ ei viel‰ olla k‰ytetty miss‰‰n muuallakaan)
			 */
			if (matrix.loppuikoPeli())
			{
				// Pelin p‰‰ttyess‰ vaihetaan teksti ja pys‰ytet‰‰n ajastin
				asetaAlaTeksti("H‰vsit pelin :D");
				time.stop();
				
				// Lis‰ksi pyydet‰‰n k‰ytt‰j‰lt‰ nime‰ ja tallennetaan enn‰tys
				String ennatysnimi = JOptionPane.showInputDialog(null, "Onnittelut h‰vi‰misen johdosta! Mik‰s oli muuten nimesi?");
				// Tarkistetaan samalla, ett‰ enn‰tys ei sis‰ll‰ &-merkki‰
				
				if (ennatysnimi == null)
					return;
				
				while(ennatysnimi.contains("&"))
				{
				ennatysnimi = JOptionPane.showInputDialog(null, "&-merkkej‰ ei hyv‰ksyt‰!");
				}
				
	
				
				return;
			}
			
			// Muulloin liikutetaan palikoita
			Palikka tippuva = matrix.annaTippuva();
			
			// Jos tippuva = null, tiputtaa uuden palikan ja jatkaa vasta
			// ensi stepill‰
			if (tippuva == null)
			{
				matrix.tiputaUusiPalikka();
				return;
			}
			
			// Mik‰li palikka tippui alas asti, luo uuden tippuvan palikan
			// Ja p‰ivitt‰‰ seuraavan kuvan
			if (!tippuva.muutaSijaintia(Suunta.ALAS))
			{
				matrix.tiputaUusiPalikka();
				// Asettaa nopeuden pistelis‰yksen mukaan
				stepaika *= 0.995;
				reset(stepaika);
				
				sivupaneeli.naytaSeuraava();
			}
			
			// P‰ivitt‰‰ screenin (mink‰ muun kautta t‰n olis voinu teh‰?)
			pelipaneeli.nayta();
		}
	}
	
	// MAIN METODIT
	/**
	 * Main metodi luo uuden tetrisolennon ja aloittaa pelin.
	 * @param args ei m‰‰rit‰ mit‰‰n
	 */
	public static void main( String[] args)
	{
		Tetrisorig tetris = new Tetrisorig();
		
		// TESTI: pyyt‰‰ k‰ytt‰j‰lt‰ stringii ja kirjoittaa sen
		//String teksti = JOptionPane.showInputDialog(tetris, "Kirijoita jotain - pliis");
		//System.out.println(teksti);
	}
	
}
