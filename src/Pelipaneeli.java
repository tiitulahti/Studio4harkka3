
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;



/**
 * Pelipaneeli sis‰lt‰‰ pelin loogiseen toimintaan vaadittavat komponentit, 
 * eli peliruudukon. Lis‰ksi pelipaneeli hoitaa n‰pp‰intoimintojen
 * toteuttamisen.
 * @author 290289
 */
public class Pelipaneeli extends JPanel implements KeyListener
{
	// ATTRIBUUTIT
	int leveys;
	int korkeus;
	
	GridBagConstraints c;

	JButton[][] nappitaulu;
	
	Peliruudukko ruudukko;
	
	Tetrisorig luojatetris;
	
	
	
	// KONSTURUKTORI
	/**
	 * Luo uuden pelipaneelin ja asettaa sille graafisen ulkoasun.
	 * @param uusiruudukko: ruudukko, joka sijoitetaan pelipaneeliin
	 * @param luoja: Tetris-luokan olio, joka loi t‰m‰n pelipaneeli-olion
	 */
	public Pelipaneeli(Peliruudukko uusiruudukko, Tetrisorig luoja)
	{
		ruudukko = uusiruudukko;
		luojatetris = luoja;
		
		// Asetetaan uuden gridbagconstraintsille leveyden ja korkeuden
		leveys = ruudukko.annaLeveys();
		korkeus = ruudukko.annaKorkeus();
		
		// Vaihetaan Layout
		setLayout(new GridBagLayout());
		
		// Luodaan uusi taulukko
		nappitaulu = new JButton[leveys][korkeus];
		
		// Luodaan constraints
		GridBagConstraints c = new GridBagConstraints();
		
		// Ja asetetaan napit sinne
		for (int x = 0; x < leveys; x ++)
		{
			for (int y = 0; y < korkeus; y++)
			{
				
				c.gridx = x;
				c.gridy = y;
				
				JButton uusinappi = new JButton();
				
				uusinappi.setSize(25, 25);
				uusinappi.setPreferredSize(uusinappi.getSize());
				uusinappi.setMinimumSize(uusinappi.getSize());
				
				uusinappi.setBackground(Color.BLACK);
				uusinappi.setBorder(
						BorderFactory.createLineBorder(Color.BLACK));
				
				uusinappi.setVisible(true);
				
				uusinappi.setFocusable(false);
				uusinappi.setFocusTraversalKeysEnabled(false);
				
				nappitaulu[x][y] = uusinappi;
				
				add(uusinappi, c);
			}
		}
		
		setBackground(Color.BLACK);
		setVisible(true);
		
		setFocusable(true);
		setFocusTraversalKeysEnabled(true);
		
		addKeyListener(this);
	}
	
	// Vaihtaa kaikkien nappien v‰rin joksikin muuksi
	// (K‰ytettiin l‰hinn‰ n‰pp‰inten testaamiseen)
	private void vaihdaVari(Color vari)
	{
		for (int x = 0; x < leveys; x++)
		{
			for (int y = 0; y < korkeus; y++)
			{
				nappitaulu[x][y].setBackground(vari);
			}
		}
	}

	@Override
	/**
	 * Liikuttaa tippuvaa palikkaa nuolin‰pp‰imi‰ painettaessa ja
	 * k‰‰nt‰‰ sit‰ ylˆsp‰in-nuolesta sek‰ v‰lilyˆnnist‰.
	 */
	public void keyPressed(KeyEvent e)
	{
		// Jos peli on keskeytetty, ei reagoi n‰pp‰imiin (paitsi enteriin)
		if (luojatetris.onKeskeytetty() && e.getKeyCode() != KeyEvent.VK_ENTER)
			return;
		
		// Jos tippuvaa palikkaa ei ole, ei tee mit‰‰n
		if (ruudukko.annaTippuva() == null)
			return;
		
		// paikallisviittaus ruudukon tippujaan
		Palikka tippuva = ruudukko.annaTippuva();
			
		
		int painettu = e.getKeyCode();
		
		switch (painettu)
		{
		 case KeyEvent.VK_LEFT : tippuva.muutaSijaintia(Suunta.VASEN); break;
         case KeyEvent.VK_RIGHT: tippuva.muutaSijaintia(Suunta.OIKEA); break;
         case KeyEvent.VK_UP   : tippuva.kaanna(); 					   break;
         case KeyEvent.VK_DOWN : tippuva.muutaSijaintia(Suunta.ALAS);  break;
         case KeyEvent.VK_SPACE : tippuva.tiputa(); 				   break;
         case KeyEvent.VK_ENTER : luojatetris.pause();				   break;
		}
		
		// P‰ivitt‰‰ tapahtuman ruudulla
		nayta();
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{	
	}

	@Override
	public void keyTyped(KeyEvent e)
	{	
	}
	
	/* P‰ivitt‰‰ kaikkien ruudun nappuloiden v‰rin vastaamaan ruudukon muotoja
	 * Ja vaikka teht‰v‰nanto ei lupaa antanutkaan, joudun tekem‰‰n t‰st‰
	 * publicin. Katso selitys Tetriksen Ajoittaja -alaluokasta
	 */
	/**
	 * P‰ivitt‰‰ peliruudun siten, ett‰ kaikki palat piirret‰‰n oikeille
	 * paikoilleen
	 */
	public void nayta()
	{
		for (int x = 0; x < leveys; x++)
		{
			for (int y = 0; y < korkeus; y++)
			{
				nappitaulu[x][y].setBackground(
						ruudukko.annaMuoto(x, y).annaVari());
			}
		}
	}
	
	// Aloittaa koko pelin alusta.
	/**
	 *Aloittaa pelin alusta, tyhjent‰en taulukon ja nollaten pisteet
	 *peliruudukon tyhjaaKaikki()-metodin avulla
	 */
	public void aloitaAlusta()
	{
		// Pys‰ytt‰‰ ja k‰ynnist‰‰ ajan
		luojatetris.pysaytaAika();
		
		// Tyhjent‰‰ ruudukon ja poistaa tippuvan palan
		ruudukko.tyhjaaKaikki();
		nayta();
		
		luojatetris.resetAika(1000);
		
		// Resettaa poistetut rivit, ja k‰ytetyn ajan
		ruudukko.nollaaPoistetutRivit();
		luojatetris.ajanlaskuAlusta();
	}
}
