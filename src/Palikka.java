import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Palikka vastaa tietty� ruutukokonaisuutta, joka toimii yhdess� ja joilla
 * kaikilla on sama v�ri ja muoto. Palikoilla on omia muotoja ja kokojaan.
 * @author 290289
 */
public class Palikka
{
	// ATTRIBUUTIT
	
	private Muoto muoto;
	private int koko;
	// Palikan sis�lt�m�t neli�t
	private boolean[][] ruudut;
	private Point sijainti;
	private  Peliruudukko ruudukko;
	
	
	// KONSTRUKTORI
	/**
	 * Luo uuden palikan peliruudukon yl�reunan keskelle.
	 * @param uusimuoto: uuden palikan saama muoto Muoto-enumeraationa
	 * @param uusiruudukko: ruudukko, johon palikka sijoitetaan.
	 */
	public Palikka(Muoto uusimuoto, Peliruudukko uusiruudukko)
	{
		muoto = uusimuoto;
		
		try {maaritaMuoto();}
		catch (FileNotFoundException fnfe)
		{
			System.err.println("Muototiedostoa ei l�ydetty!");
		}
		
		this.ruudukko = uusiruudukko;
		
		// Sijainti on likimain keskell� (py�ristet��n vasemmalle)
		this.sijainti = new Point((int) (this.ruudukko.annaLeveys()/2.0 - this.koko/2.0), 0);
	}
	
	/**
	 * Palikka muodostaa t�ll� metodilla itselleen totuusarvotaulukon saamansa
	 * muodon perusteella. N�in se tiet��, miss� osassa palikkaa on ruutuja
	 * ja miss� ei. Palikoiden muototaulukot luetaan erillisest�
	 * tekstitiedostosta Muodot.txt
	 * 
	 * @throws FileNotFoundException: Heitt�� exceptionin jos tiedostoa
	 * muodot.txt ei l�ydy
	 */
	private void maaritaMuoto() throws FileNotFoundException
	{
		File muotofile = new File("muodot.txt");
		
		// Luodaan skanneri
		Scanner skanneri = new Scanner(muotofile);
		
		while (skanneri.hasNextLine())
		{
			// Lukee rivin kerrallaan
			String rivi = skanneri.nextLine();
			
			// Kun joltain rivilt� l�ydet��n oma muoto
			if (rivi.equals(muoto.toString()))
			{
				try
				{
					// Laskee uuden koon ja tekee virhetarkistukset
					int uusikoko = Integer.parseInt(skanneri.nextLine());
					
					if (uusikoko < 0)
						System.err.println("Tiedostossa annettu koko on negatiivinen");
					else if (uusikoko > 4)
						System.err.println("Tiedostossa annettu koko on yli maksimin");
					
					koko = uusikoko;
					
					// Ruudut-taulukko luodaan vasta t�ss� vaiheessa, sill�
					// ennen ei ollut tietoa koosta
					ruudut = new boolean[koko][koko];
					
					// K�y kaikki rivit l�pi
					for (int i = 0; i < koko; i++)
					{
						// Laskee rivin merkit stringin� ja taulukkona
						// Ja siirtyy samalla uudelle riville
						String merkkiryhma = skanneri.nextLine();
						
						char[] merkit = merkkiryhma.toCharArray();
						
						// K�y l�pi merkit ja t�ytt�� niiden perusteella
						// ruudut
						for (int ci = 0; ci < koko; ci++)
						{
							if (merkit[ci] == '*')
								ruudut[i][ci] = true;
							else if (merkit[ci] == '_')
								ruudut[i][ci] = false;
							else
								System.err.println("Virheellinen taulukko " +
										"tekstitedostossa!");
						}
					}
				}
				catch (NumberFormatException nfe)
				{
					System.err.println("Koon lukeminen tiedostosta " +
							"ep�onnistui!");
				}
			}
		}	
	}
	
	// Kertoo, onko kyseisess� sijainnissa ruutu vai ei... duh
	/**
	 * Metodi kertoo, onko kyseisess� palikan sis�isess� sijainnissa ruutu vai
	 * ei.
	 * @param x: tarkasteltavan sektorin x-koordinaatti
	 * @param y: tarkasteltavan sektorin y-koordinaatti
	 * @return onko sijainnissa ruutua vai ei
	 */
	public boolean onkoRuutu(int x, int y)
	{
		// Palauttaa false jos etsit��n ruutujen ulkopuolelta
		if (x < 0 || y < 0 || x >= annaKoko() || y >= annaKoko())
			return false;
		
		return ruudut[x][y];
	}
	
	/**
	 * Kertoo palikan muodon
	 * @return palikan muoto-enumeraatio
	 */
	public Muoto annaMuoto()
	{
		return muoto;
	}
	
	/**
	 * Palauttaa palikan maksimileveyden/-korkeuden.
	 * @return palikan leveys / korkeus
	 */
	public int annaKoko()
	{
		return koko;
	}
	
	/**
	 * Kertoo palikan t�m�nhetkisen sijainnin ruudukossa Point-tyyppisen�
	 * arvona
	 * @return nykyinen sijainti
	 */
	public Point annaSijainti()
	{
		return sijainti;
	}
	
	// Sijoittaa itsens� ruudukkoon (kaikki toiminnallisuudet suoritetaan
	// tayta() -metodissa). Vaihdoin t�m�n boolean-tyyppiseksi metodiksi,
	// sill� sitten ei tarvitse tehd� tarkistuksia t�yt�n onnistumisesta useassa
	// paikassa (ja se oli niin helppo ja kiva tehd� :))
	/**
	 * Sijoittaa palikan ruudukkoon omaan sijaintiinsa, 
	 * mik�li se vain on mahdollista
	 * @return: onnistuiko sijoittaminen
	 */
	public boolean sijoitaRuudukkoon()
	{
		return ruudukko.tayta(sijainti.x, sijainti.y, this);
	}
	
	// Poistaa kaikki k�sitt�m�ns� pisteet ruudukosta
	/**
	 * Poistaa omat ruutunsa ruudukosta.
	 */
	public void poistaRuudukosta()
	{
		int x = sijainti.x;
		int y = sijainti.y;
		
		for (int ix = 0; ix < koko; ix++)
		{
			for (int iy = 0; iy < koko; iy++)
			{
				// Poistaa tosin vain siin� tapauksessa, ett� on kyse juuri
				// t�m�n palikan osista
				if (onkoRuutu(ix, iy))
					ruudukko.tyhjaa(x + ix, y + iy, this);
			}
		}
	}
	
	// Muuttaa palikan sijaintia yhden askeleen pyydettyyn suuntaan,
	// Mik�li se vain on mahdollista
	// PS: Tein t�st�kin boolean tyyppisen metodin, jotta ei tarvita
	// erillisi� tarkistuksia
	/**
	 * Yritt�� liikuttaa palikkaa pyydettyyn suuntaan ja palauttaa, 
	 * mik�li t�m� oli tai ei ollut mahdollista.
	 * @param s: suunta, johon palikkaa liikutetaan
	 * @return onnistuttiinko palikkaa liikuttamaan
	 */
	public boolean muutaSijaintia(Suunta s)
	{	
		// Laskee uuden sijainnin
		Point uusisijainti = new Point(sijainti.x + s.annaX(), 
				sijainti.y + s.annaY());
		
		// Poistetaan palikka ruudukosta
		poistaRuudukosta();
		
		// Jos uuteen sijaintiin voidaan siirty�, siirtyy, muuten palaa takaisin
		if (!ruudukko.palikkaMahtuuSijaintiin(uusisijainti.x, 
				uusisijainti.y, this))
		{
			sijoitaRuudukkoon();
			return false;
		}
		
		// Siirret��n palikka uuteen sijaintiin
		sijainti = uusisijainti;
		// Luodaan palikka uuteen sijaintiin
		sijoitaRuudukkoon();
		
		return true;
	}
	
	/**
	 * K��nt�� palikkaa 90 astetta positiiviseen suuntaan, mik�li se vain on
	 * mahdollista.
	 */
	public void kaanna()
	{
		// Muistaa vanhat ruutunsa palautusta varten
		boolean[][] vanhat_ruudut = ruudut;
		// Laskee uudet ruudut itselleen
		boolean[][] uudet_ruudut = new boolean[annaKoko()][annaKoko()];
		
		/*
		 * [00][10][20]
		 * [01][11][21]
		 * [02][12][22]
		 */
		
		switch (annaKoko())
		{
		case 2: uudet_ruudut = ruudut; break;
		case 3:
		{
			// Vaihtaa kaikki ruudut kivalla algoritmill�
			for (int x = 0; x < annaKoko(); x++)
			{
				for (int y = 0; y < annaKoko(); y++)
				{
					uudet_ruudut[x][y] = ruudut[annaKoko() - 1 - y][x];
				}
			}
			
			break;
		}
		case 4:
		{
			// Kun kyseess� on pystyviivan k��nt�minen
			if (onkoRuutu(1, 0))
			{
				for (int ix = 0; ix < 4; ix++)
				{
					for (int iy = 0; iy < 4; iy++)
					{
						if (iy == 2)
							uudet_ruudut[ix][iy] = true;
						else
							uudet_ruudut[ix][iy] = false;
					}
				}
			}
			// Kun kyseess� on vaakaviivan k��nt�minen
			else
			{
				for (int ix = 0; ix < 4; ix++)
				{
					for (int iy = 0; iy < 4; iy++)
					{
						if (ix == 1)
							uudet_ruudut[ix][iy] = true;
						else
							uudet_ruudut[ix][iy] = false;
					}
				}
			}
			
			break;
		}
		}
		
		// Poistaa itsens� ja k��nt�� itse��n
		poistaRuudukosta();
		ruudut = uudet_ruudut;
		
		//Yritt�� sijoittaa itse��n
		if (!sijoitaRuudukkoon())
		{
			// Jos se ei onnistunut, joutuu k��nt�m��n itsens� takaisin ja
			// sijoittamaan uudestaan
			ruudut = vanhat_ruudut;
			sijoitaRuudukkoon();
		}
	}
	
	// Tiputtaa palikan alas asti
	/**
	 * Tiputtaa palikan niin pitk�lle kuin sit� voi tiputtaa
	 */
	public void tiputa()
	{
		while(muutaSijaintia(Suunta.ALAS))
		{}
	}
	/*
	// P�ivitt�� sijainniksi uuden sijainnin, joka lasketaan kuvastamaan
	// sit� kohtaa, johon tippuva on putoamassa.
	public void paivitaHaamuna(Palikka seurattava)
	{
		// Poistaa aluksi varmuuden vuoksi itsens� ruudukosta ja p�ivitt��
		// Aloitussijainnin
		poistaRuudukosta();
		sijainti = seurattava.annaSijainti();
		
		// Tiputtaa palikkaa alas, mutta pit�� sen poissa ruudukosta
		while(muutaSijaintia(Suunta.ALAS))
		{
			poistaRuudukosta();
		}
	}
	*/
}
