import java.util.Random;

/**
 * Peliruudukko pit‰‰ kirjaa kaikista tetrismaailman yksitt‰isist‰ palasista
 * ja tekee t‰rkeimm‰t tarkistukset palikoiden liikuttamista ja tiputtamista
 * varten.
 * @author 290289
 */
public class Peliruudukko {
	// ATTRIBUUTIT
	
	// Mitat
	private int korkeus;
	private int leveys;
	private int pisteet;
	private int pistelisays;
	private int viime_pistesaalis;
	private int poistetut_rivit;
	
	// Taulukko, joka kuvaa peliruudukkoa
	private Muoto[][] ruudukko;
	
	// Pelimaailmassa liikuskelevat palikat
	private Palikka seuraava;
	private Palikka tippuva;
	// haamu n‰ytt‰‰, mihin palikka on tippumassa
	//private Palikka haamu;
	
	// Satunnaisluvuissa k‰ytett‰v‰ random
	Random randomer;
	
	boolean game_over;
	
	
	// KOSNTRUKTORI
	/**
	 * Luo uuden peliruudukon palikoiden sijoittamista varten.
	 * @param uusileveys: peliruudukon saama leveys palasina
	 * @param uusikorkeus: peliruudukon saama korkeus palasina
	 */
	public Peliruudukko(int uusileveys, int uusikorkeus)
	{
		// P‰ivitt‰‰ attribuutit
		this.korkeus = uusikorkeus;
		this.leveys = uusileveys;
		
		this.ruudukko = new Muoto[leveys][korkeus];
		
		// T‰ytet‰‰n taulukko tyhjill‰ indekseill‰
		for (int x = 0; x < this.leveys; x++)
		{
			for (int y = 0; y < this.korkeus; y++)
			{
				this.ruudukko[x][y] = Muoto.TYHJA;
			}
		}
		
		this.randomer = new Random();
		
		this.game_over = false;
	
		this.seuraava = new Palikka(arvoUusiMuoto(), this);
		this.tippuva =  new Palikka(arvoUusiMuoto(), this);
		
		this.pisteet = 0;
		this.pistelisays = 0;
		this.viime_pistesaalis = 0;
		this.poistetut_rivit = 0;
	}
	
	
	// METODIT
	/**
	 * Kertoo, kuinka korkea peliruudukko on paloina
	 * @return: peliruudukon korkeus
	 */
	public int annaKorkeus()
	{
		return this.korkeus;
	}
	
	/**
	 * Kertoo, kuinka leve‰ peliruudukko on paloina
	 * @return: peliruudukon leveys
	 */
	public int annaLeveys()
	{
		return this.leveys;
	}
	
	/**
	 * Palauttaa peliruudukossa sijaitsevan palan muodon kysytyst‰ kohdasta.
	 * Jos kohdassa ei ole palaa, palauttaa TYHJA
	 * @param x: tarkasteltava x-koordinaatti
	 * @param y: tarkasteltava y-koordinaatti
	 * @return sijainnissa olevan palan muoto
	 */
	public Muoto annaMuoto(int x, int y)
	{
		return this.ruudukko[x][y];
	}
	
	/**
	 * Kertoo, mik‰ pala on t‰ll‰ hetkell‰ putoamassa
	 * @return t‰ll‰ hetkell‰ tippuva pala
	 */
	public Palikka annaTippuva()
	{
		return this.tippuva;
	}
	
	/**
	 * Palauttaa seuraavaksi tipuuvan palikan
	 * @return seuraavaksi tippuva palikka
	 */
	public Palikka annaSeuraava()
	{
		return this.seuraava;
	}
	
	/**
	 * Kertoo, kuinka monta pistett‰ t‰h‰nmenness‰ on ker‰tty
	 * @return ker‰tyt pisteet
	 */
	public int annaPisteet()
	{
		return this.pisteet;
	}
	
	// Palauttaa viimeisen onnistuneen poiston tuottamat pisteet
	/**
	 * Kertoo viimeisimm‰n rivinpoiston antaman pistem‰‰r‰n
	 * @return: viimeisimm‰n rivinpoiston tuottamat pisteet.
	 */
	public int annaPistesaalis()
	{
		return this.viime_pistesaalis;
	}
	
	/**
	 * Palauttaa t‰ll‰ pelikerralla poistettujen  pisteiden m‰‰r‰n
	 * @return poistetut rivit
	 */
	public int annaPoistetutRivit()
	{
		return this.poistetut_rivit;
	}
	
	/**
	 * Tyhjent‰‰ muistin poistettujen rivien osalta
	 */
	public void nollaaPoistetutRivit()
	{
		this.poistetut_rivit = 0;
	}
	
	/**
	 * Arpoo uuden muodon valmiiden muotovaihtoehtojen joukosta (k‰sitt‰‰ kaikki
	 * muoto-enumeraation arvot tyhj‰‰ lukuun ottamatta)
	 * @return: satunnaisesti valittu muoto
	 */
	private Muoto arvoUusiMuoto()
	{
		// Muodostetaan arvottavien muotojen joukko ja valitaan niist‰ jokin
		Muoto[] muodot = {Muoto.I, Muoto.J, Muoto.L, Muoto.O, Muoto.S, 
				Muoto.T, Muoto.Z};
		int muoto_nro = randomer.nextInt(muodot.length);
		
		return muodot[muoto_nro];
	}
	
	/*
	 * Sorry, musta olis ihan kiva saada tieto pelin loppumisesta myˆs
	 * tetris-luokalle ja sen alaluokille, joten laitan t‰n publiciks
	 */
	/**
	 * Kertoo, onko peli viel‰ p‰‰ttynyt sen johdosta, ett‰ uusia paloja ei
	 * voida en‰‰n lis‰t‰
	 * @return onko peli p‰‰ttynyt
	 */
	public boolean loppuikoPeli()
	{
		return this.game_over;
	}
	
	// Tarkistaa, onko pyydetty rivi kokonaan t‰ytetty
	private boolean onkoRiviValmis(int rivi)
	{
		// K‰y l‰pi kaikki rivin indeksit ja jos jokin
		// Niist‰ on tyhj‰, rivi ei ole valmis
		for (int x = 0; x < this.leveys; x++)
		{
			if (this.ruudukko[x][rivi] == Muoto.TYHJA)
				return false;
		}
		
		return true;
	}
	
	
	private void poistaRivit()
	{
		// Kuinka paljon k‰sitelt‰vi‰ rivej‰ lasketaan
		// (kasvaa kun rivej‰ poistetaan)
		int alaspain = 0;
		
		// K‰y l‰pi kaikki rivit alhaalta ylˆs
		for (int y = this.korkeus -1; y >= 0; y--)
		{
			// Jos rivi on t‰ynn‰, poistaa sen ja laskee kaikkia ylempi‰ rivej‰
			if (onkoRiviValmis(y))
			{
				for (int x = 0; x < this.leveys; x++)
				{
					this.ruudukko[x][y] = Muoto.TYHJA;
				}
				alaspain++;
			}
			// Muutoin vain laskee rivi‰
			else if (alaspain > 0)
			{
				// Siirt‰‰ alemmalle riville ja poistaa nykyiselt‰
				for (int x = 0; x < this.leveys; x++)
				{
					this.ruudukko[x][y + alaspain] = this.ruudukko[x][y];
					this.ruudukko[x][y] = Muoto.TYHJA;
				}
			}
		}
		
		// Pisteit‰ saa vain, jos rivej‰ poistettiin
		if (alaspain == 0)
			return;
		
		// Mist‰kˆh‰n johtuu alun pisteenlaskuongelmat?
		
		int viime_pisteet = pisteet;
		
		// Pisteet kasvavat hienosti eksponentiaalisesti
		pistelisays += alaspain;
		// Kun tuhoaa monta palaa kerralla, saa enemm‰n pisteit‰
		pisteet += pistelisays*Math.pow(alaspain, 2);
		// Kun poistaa 4 rivi‰ kerralla, pisteet viel‰ kaksinkertaistuvat!
		if (alaspain == 4)
			pisteet *= 2;
		
		// P‰ivitt‰‰ siirron aiheuttaman pistesaaliin
		viime_pistesaalis = pisteet - viime_pisteet;
		
		// Lis‰t‰‰n viel‰ poistetut rivit
		poistetut_rivit += alaspain;
		
		//return alaspain;
	}
	
	// Muutan tˆm‰n teht‰v‰nannosta huolimatta public booleaniksi,
	// T‰m‰ johtuu siit‰, ett‰ en halua tehd‰ erillisi‰ tarkistuksia
	// Palikka-luokassa
	/**
	 * Sijoittaa palikan annettuun sijaintiin, mik‰li se vain on mahdollista
	 * @param x: palikan uuden sijainnin x-koordinaatti
	 * @param y: palikan uuden sijainnin y-koordinaatti
	 * @param p: sijoitettava palikka
	 * @return onnistuttiinko palikka sijoittamaan ruudukkoon
	 */
	public boolean tayta(int x, int y, Palikka p)
	{
		if (!palikkaMahtuuSijaintiin(x, y, p))
			return false;
		
		for (int ix = 0; ix < p.annaKoko(); ix++)
		{
			for (int iy = 0; iy < p.annaKoko(); iy++)
			{
				// Jos tarkasteltavassa kohdassa on ruutu,
				// lis‰‰ muodon ruudukkoon
				if (p.onkoRuutu(ix, iy))
					ruudukko[x + ix][y + iy] = p.annaMuoto();
			}
		}
		
		return true;
		// Palauttaa true, jos palikka pystyttiin lis‰‰m‰‰n onnistuneesti
	}
	
	/*
	 * tiputaUusiPalikka() tekee seuraavaa stuffii
	 * 1) Poistaa rivit
	 * 2) Tiputtaa uuden palikan ruudukkoon (ellei game_over)
	 * 3) Generoi uuden tiputettavan
	 * 
	 * Tarkistaa myˆs, ollaanko ajauduttu tilanteeseen, jossa voidaan sanoa
	 * pelin p‰‰ttyneen (ei voida tiputtaa palaa)
	 */
	/**
	 * Metodi lis‰‰ peliin uuden palikan, poistaa kertyneet rivit ja generoi
	 * uuden tiputettavan palikan, ellei peli ole viel‰ p‰‰ttynyt.
	 */
	public void tiputaUusiPalikka()
	{
		// 1)
			poistaRivit();
		// 2)
		if (this.game_over)
			this.tippuva = null;
		else
		{
			this.tippuva = this.seuraava;
			
			// Lis‰‰ uuden palikan, ja mik‰li se ei onnistunut, tuottaa game-
			// overin
			if (!tayta(this.tippuva.annaSijainti().x, 0, this.tippuva))
				this.game_over = true;
		}
		// 3)
		this.seuraava = new Palikka(arvoUusiMuoto(), this);
		
	}
	
	// Poistaa sijainnista samanmuotoiset palat kuin palikka p:ss‰
	/**
	 * Poistaa palikan yksitt‰isen ruudun ruudukosta.
	 * @param x: poistettavan ruudun x-koordinaatti
	 * @param y: poistettavan ruudun y-koordinaatti
	 * @param p: palikka, josta ruutua poistetaan
	 */
	public void tyhjaa(int x, int y, Palikka p)
	{
		// Jos yritet‰‰n tyhj‰t‰ taulukon ulkopuolelta, j‰tt‰‰ huomiotta
		if (!sijaintiOnRuudukossa(x, y))
			return;
		
		if (this.ruudukko[x][y].equals(p.annaMuoto()))
			this.ruudukko[x][y] = Muoto.TYHJA;
	}
	
	// Poistaa kaikki muodot koko mestasta. Poistaa lis‰ksi tippuvan palan
	// ja asettaa game_overin falseksi
	/**
	 * Poistaa kaikki palat koko ruudukosta, mukaan lukien tippuvan palan.
	 * Palauttaa myˆs pelin p‰‰ttymisen arvon niin, ett‰ peli ei en‰‰n
	 * puhdistamisen j‰lkeen ole p‰‰ttynyt.
	 * Lis‰ksi metodi nollaa kaikki saavutetut pisteet
	 */
	public void tyhjaaKaikki()
	{
		for (int x = 0; x < annaLeveys(); x++)
		{
			for (int y = 0; y < annaKorkeus(); y++)
			{
				ruudukko[x][y] = Muoto.TYHJA;
			}
		}
		
		tippuva = null;
		game_over = false;
		
		// Lis‰ksi nollaa pisteet
		pisteet = 0;
		pistelisays = 0;
		viime_pistesaalis = 0;
	}
	
	/**
	 * Tarkistaa, pystyykˆ annettu palikka mahtumaan annettuun sijaintiin
	 * ilman, ett‰ se menisi p‰‰llekk‰in yhdenk‰‰n jo ruudukossa sijaitsevan
	 * palikan kanssa tai joutuisi ulos ruudukosta.
	 * @param x: tarkasteltavan sijainnin x-koordinaatti
	 * @param y: tarkasteltavan sijainnin y-koordinaatti
	 * @param p: Palikka, jota yritet‰‰n sijoittaa annettuun sijaintiin
	 * @return: Pystyykˆ palikan sijoittamaan pyydettyyn sijaintiin.
	 */
	public boolean palikkaMahtuuSijaintiin(int x, int y, Palikka p)
	{
		// K‰y l‰pi palikan laajuuden edest‰ ruutuja ja jos
		// Yksikin niist‰ aiheuttaa sijoitusongelman, palauttaa false
		for (int ix = 0; ix < p.annaKoko(); ix++)
		{
			for (int iy = 0; iy < p.annaKoko(); iy++)
			{	
				// Jos palikka yritt‰‰ t‰ytt‰‰ jo t‰ytetty‰ sijaintia, palauttaa
				// false. Samoin jos yritet‰‰n t‰ytt‰‰ ruudukon ulkopuolelta
				if (p.onkoRuutu(ix, iy) && 
						(!sijaintiOnRuudukossa(x + ix, y + iy) ||
						!ruudukko[x + ix][y + iy].equals(Muoto.TYHJA)))
					return false;
			}
		}
		
		return true;
	}
	
	private boolean sijaintiOnRuudukossa(int x, int y)
	{
		if (x < 0 || y < 0 || x >= leveys || y >= korkeus)
			return false;
		else
			return true;
	}
	
}
