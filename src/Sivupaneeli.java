import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Sivupaneeli n‰ytt‰‰ k‰ytt‰j‰lle seuraavan palikan mustalla taustalla.
 * @author 290289
 */
public class Sivupaneeli extends JPanel
{
	// ATTRIBUUTIT
	
	private Peliruudukko ruudukko;
	private JButton[][] seuraava;
	
	// KONSTRUKTORI
	/**
	 * Luo uuden sivupaneelin ja piirt‰‰ siihen seuraavaksi tippuvan palikan
	 * @param uusiruudukko: Peliruudukko, josta seuraava palikka m‰‰ritet‰‰n
	 */
	public Sivupaneeli(Peliruudukko uusiruudukko)
	{
		// Alustaa attribuutit
		ruudukko = uusiruudukko;
		seuraava = new JButton[6][5];
		
		// S‰‰t‰‰ itse paneelin asetuksia
		setLayout(new GridBagLayout());
		
		// Luodaan constraints
		GridBagConstraints c = new GridBagConstraints();
				
		// Ja seuraavan ruudut sinne
		for (int x = 0; x < 6; x ++)
		{
			for (int y = 0; y < 5; y++)
			{
				c.gridx = x + 1;
				c.gridy = y + 2;
				
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
				
				seuraava[x][y] = uusinappi;
						
				add(uusinappi, c);
			}
		}
				
		setBackground(Color.WHITE);
		setVisible(true);
		
		setFocusable(false);
		setFocusTraversalKeysEnabled(false);
		
		setSize(200, 175);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		
		// P‰ivittelee n‰kym‰n kivasti
		naytaSeuraava();
	}
	
	
	// METODIT
	/**
	 * P‰ivitt‰‰ sivupaneelin n‰ytˆn niin, ett‰ seuraavaksi n‰ytett‰v‰ palikka
	 * on ajan tasalla.
	 */
	public void naytaSeuraava()
	{
		// Tarkistetaan, ett‰ seuraava on olemassa
		if (ruudukko.annaSeuraava() == null)
			return;
		
		// K‰yd‰‰n l‰pi kaikki ruudukon seuraavan arvot ja muutetaan oman
		// n‰kym‰n v‰ri vastaamaan niit‰
		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				if (ruudukko.annaSeuraava().onkoRuutu(x, y))
					seuraava[x+1][y+1].setBackground(ruudukko.annaSeuraava().annaMuoto().annaVari());
				else
					seuraava[x+1][y+1].setBackground(Color.BLACK);
			}
		}
	}
}
