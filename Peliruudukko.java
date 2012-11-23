import java.util.Random;

/**
 * Peliruudukko pitää kirjaa kaikista tetrismaailman yksittäisistä palasista
 * ja tekee tärkeimmät tarkistukset palikoiden liikuttamista ja tiputtamista
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
  // haamu näyttää, mihin palikka on tippumassa
  //private Palikka haamu;
  
  // Satunnaisluvuissa käytettävä random
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
    // Päivittää attribuutit
    this.korkeus = uusikorkeus;
    this.leveys = uusileveys;
    
    this.ruudukko = new Muoto[leveys][korkeus];
    
    // Täytetään taulukko tyhjillä indekseillä
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
   * Kertoo, kuinka leveä peliruudukko on paloina
   * @return: peliruudukon leveys
   */
  public int annaLeveys()
  {
    return this.leveys;
  }
  
  /**
   * Palauttaa peliruudukossa sijaitsevan palan muodon kysytystä kohdasta.
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
   * Kertoo, mikä pala on tällä hetkellä putoamassa
   * @return tällä hetkellä tippuva pala
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
   * Kertoo, kuinka monta pistettä tähänmennessä on kerätty
   * @return kerätyt pisteet
   */
  public int annaPisteet()
  {
    return this.pisteet;
  }
  
  // Palauttaa viimeisen onnistuneen poiston tuottamat pisteet
  /**
   * Kertoo viimeisimmän rivinpoiston antaman pistemäärän
   * @return: viimeisimmän rivinpoiston tuottamat pisteet.
   */
  public int annaPistesaalis()
  {
    return this.viime_pistesaalis;
  }
  
  /**
   * Palauttaa tällä pelikerralla poistettujen  pisteiden määrän
   * @return poistetut rivit
   */
  public int annaPoistetutRivit()
  {
    return this.poistetut_rivit;
  }
  
  /**
   * Tyhjentää muistin poistettujen rivien osalta
   */
  public void nollaaPoistetutRivit()
  {
    this.poistetut_rivit = 0;
  }
  
  /**
   * Arpoo uuden muodon valmiiden muotovaihtoehtojen joukosta (käsittää kaikki
   * muoto-enumeraation arvot tyhjää lukuun ottamatta)
   * @return: satunnaisesti valittu muoto
   */
  private Muoto arvoUusiMuoto()
  {
    // Muodostetaan arvottavien muotojen joukko ja valitaan niistä jokin
    Muoto[] muodot = {Muoto.I, Muoto.J, Muoto.L, Muoto.O, Muoto.S, 
        Muoto.T, Muoto.Z};
    int muoto_nro = randomer.nextInt(muodot.length);
    
    return muodot[muoto_nro];
  }
  
  /*
   * Sorry, musta olis ihan kiva saada tieto pelin loppumisesta myös
   * tetris-luokalle ja sen alaluokille, joten laitan tän publiciks
   */
  /**
   * Kertoo, onko peli vielä päättynyt sen johdosta, että uusia paloja ei
   * voida enään lisätä
   * @return onko peli päättynyt
   */
  public boolean loppuikoPeli()
  {
    return this.game_over;
  }
  
  // Tarkistaa, onko pyydetty rivi kokonaan täytetty
  private boolean onkoRiviValmis(int rivi)
  {
    // Käy läpi kaikki rivin indeksit ja jos jokin
    // Niistä on tyhjä, rivi ei ole valmis
    for (int x = 0; x < this.leveys; x++)
    {
      if (this.ruudukko[x][rivi] == Muoto.TYHJA)
        return false;
    }
    
    return true;
  }
  
  
  private void poistaRivit()
  {
    // Kuinka paljon käsiteltäviä rivejä lasketaan
    // (kasvaa kun rivejä poistetaan)
    int alaspain = 0;
    
    // Käy läpi kaikki rivit alhaalta ylös
    for (int y = this.korkeus -1; y >= 0; y--)
    {
      // Jos rivi on täynnä, poistaa sen ja laskee kaikkia ylempiä rivejä
      if (onkoRiviValmis(y))
      {
        for (int x = 0; x < this.leveys; x++)
        {
          this.ruudukko[x][y] = Muoto.TYHJA;
        }
        alaspain++;
      }
      // Muutoin vain laskee riviä
      else if (alaspain > 0)
      {
        // Siirtää alemmalle riville ja poistaa nykyiseltä
        for (int x = 0; x < this.leveys; x++)
        {
          this.ruudukko[x][y + alaspain] = this.ruudukko[x][y];
          this.ruudukko[x][y] = Muoto.TYHJA;
        }
      }
    }
    
    // Pisteitä saa vain, jos rivejä poistettiin
    if (alaspain == 0)
      return;
    
    // Mistäköhän johtuu alun pisteenlaskuongelmat?
    
    int viime_pisteet = pisteet;
    
    // Pisteet kasvavat hienosti eksponentiaalisesti
    pistelisays += alaspain;
    // Kun tuhoaa monta palaa kerralla, saa enemmän pisteitä
    pisteet += pistelisays*Math.pow(alaspain, 2);
    // Kun poistaa 4 riviä kerralla, pisteet vielä kaksinkertaistuvat!
    if (alaspain == 4)
      pisteet *= 2;
    
    // Päivittää siirron aiheuttaman pistesaaliin
    viime_pistesaalis = pisteet - viime_pisteet;
    
    // Lisätään vielä poistetut rivit
    poistetut_rivit += alaspain;
    
    //return alaspain;
  }
  
  // Muutan tömän tehtävänannosta huolimatta public booleaniksi,
  // Tämä johtuu siitä, että en halua tehdä erillisiä tarkistuksia
  // Palikka-luokassa
  /**
   * Sijoittaa palikan annettuun sijaintiin, mikäli se vain on mahdollista
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
        // lisää muodon ruudukkoon
        if (p.onkoRuutu(ix, iy))
          ruudukko[x + ix][y + iy] = p.annaMuoto();
      }
    }
    
    return true;
    // Palauttaa true, jos palikka pystyttiin lisäämään onnistuneesti
  }
  
  /*
   * tiputaUusiPalikka() tekee seuraavaa stuffii
   * 1) Poistaa rivit
   * 2) Tiputtaa uuden palikan ruudukkoon (ellei game_over)
   * 3) Generoi uuden tiputettavan
   * 
   * Tarkistaa myös, ollaanko ajauduttu tilanteeseen, jossa voidaan sanoa
   * pelin päättyneen (ei voida tiputtaa palaa)
   */
  /**
   * Metodi lisää peliin uuden palikan, poistaa kertyneet rivit ja generoi
   * uuden tiputettavan palikan, ellei peli ole vielä päättynyt.
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
      
      // Lisää uuden palikan, ja mikäli se ei onnistunut, tuottaa game-
      // overin
      if (!tayta(this.tippuva.annaSijainti().x, 0, this.tippuva))
        this.game_over = true;
    }
    // 3)
    this.seuraava = new Palikka(arvoUusiMuoto(), this);
    
  }
  
  // Poistaa sijainnista samanmuotoiset palat kuin palikka p:ssä
  /**
   * Poistaa palikan yksittäisen ruudun ruudukosta.
   * @param x: poistettavan ruudun x-koordinaatti
   * @param y: poistettavan ruudun y-koordinaatti
   * @param p: palikka, josta ruutua poistetaan
   */
  public void tyhjaa(int x, int y, Palikka p)
  {
    // Jos yritetään tyhjätä taulukon ulkopuolelta, jättää huomiotta
    if (!sijaintiOnRuudukossa(x, y))
      return;
    
    if (this.ruudukko[x][y].equals(p.annaMuoto()))
      this.ruudukko[x][y] = Muoto.TYHJA;
  }
  
  // Poistaa kaikki muodot koko mestasta. Poistaa lisäksi tippuvan palan
  // ja asettaa game_overin falseksi
  /**
   * Poistaa kaikki palat koko ruudukosta, mukaan lukien tippuvan palan.
   * Palauttaa myös pelin päättymisen arvon niin, että peli ei enään
   * puhdistamisen jälkeen ole päättynyt.
   * Lisäksi metodi nollaa kaikki saavutetut pisteet
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
    
    // Lisäksi nollaa pisteet
    pisteet = 0;
    pistelisays = 0;
    viime_pistesaalis = 0;
  }
  
  /**
   * Tarkistaa, pystyykö annettu palikka mahtumaan annettuun sijaintiin
   * ilman, että se menisi päällekkäin yhdenkään jo ruudukossa sijaitsevan
   * palikan kanssa tai joutuisi ulos ruudukosta.
   * @param x: tarkasteltavan sijainnin x-koordinaatti
   * @param y: tarkasteltavan sijainnin y-koordinaatti
   * @param p: Palikka, jota yritetään sijoittaa annettuun sijaintiin
   * @return: Pystyykö palikan sijoittamaan pyydettyyn sijaintiin.
   */
  public boolean palikkaMahtuuSijaintiin(int x, int y, Palikka p)
  {
    // Käy läpi palikan laajuuden edestä ruutuja ja jos
    // Yksikin niistä aiheuttaa sijoitusongelman, palauttaa false
    for (int ix = 0; ix < p.annaKoko(); ix++)
    {
      for (int iy = 0; iy < p.annaKoko(); iy++)
      {  
        // Jos palikka yrittää täyttää jo täytettyä sijaintia, palauttaa
        // false. Samoin jos yritetään täyttää ruudukon ulkopuolelta
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

