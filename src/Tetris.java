import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import processing.core.*;


public class Tetris extends PApplet{
	private Pelicanvas pelicanvas;
	private Peliruudukko peliruudukko;
	private Ajoittaja ajastin;
	
	public void setup(){
		size(500, 475);
	
		this.peliruudukko = new Peliruudukko(15, 30);
		this.pelicanvas = new Pelicanvas(this.peliruudukko, this, 0, 0);
		
		this.ajastin = new Ajoittaja(750);
	}
	
	public void draw(){
		this.pelicanvas.piirra();
	}

	class Ajoittaja implements ActionListener
	{	
		/*
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
			time.addActionListener(this);
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
			
			System.out.println("action performed");
			/* Tarkistetaan, onko peli p‰‰ttynyt
			 * Huom: k‰yt‰n t‰ss‰ loppuikoPeli()-metodia, sill‰ se p‰ivittyy
			 * Peliruudukko-luokassa hieman tehokkaammin kuin tippuva
			 * (Lis‰ksi sit‰ ei viel‰ olla k‰ytetty miss‰‰n muuallakaan)
			 */
			if (peliruudukko.loppuikoPeli())
			{
				// Pelin p‰‰ttyess‰ vaihetaan teksti ja pys‰ytet‰‰n ajastin
				//asetaAlaTeksti("H‰vsit pelin :D");
				time.stop();

			}
			
			// Muulloin liikutetaan palikoita
			Palikka tippuva = peliruudukko.annaTippuva();
			
			// Jos tippuva = null, tiputtaa uuden palikan ja jatkaa vasta
			// ensi stepill‰
			if (tippuva == null)
			{
				peliruudukko.tiputaUusiPalikka();
				return;
			}
			
			// Mik‰li palikka tippui alas asti, luo uuden tippuvan palikan
			// Ja p‰ivitt‰‰ seuraavan kuvan
			if (!tippuva.muutaSijaintia(Suunta.ALAS))
			{
				peliruudukko.tiputaUusiPalikka();
		/*		// Asettaa nopeuden pistelis‰yksen mukaan
				stepaika *= 0.995;
				reset(stepaika);*/
				
			}
			
			// P‰ivitt‰‰ screenin (mink‰ muun kautta t‰n olis voinu teh‰?)
			pelicanvas.piirra();
		}
	}
}
