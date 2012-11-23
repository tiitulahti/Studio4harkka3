import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Palikka vastaa tiettyä ruutukokonaisuutta, joka toimii yhdessä ja joilla
 * kaikilla on sama väri ja muoto. Palikoilla on omia muotoja ja kokojaan.
 * @author 290289
 */
public class Palikka
{
  // ATTRIBUUTIT
  
  private Muoto muoto;
  private int koko;
  // Palikan sisältämät neliöt
  private boolean[][] ruudut;
  private Point sijainti;
  private Peliruudukko ruudukko;
  long viimeksiKaannetty;
  long viimeksiSiirrettyHorisontaalisesti;
  
  
  // KONSTRUKTORI
  /**
   * Luo uuden palikan peliruudukon yläreunan keskelle.
   * @param uusimuoto: uuden palikan saama muoto Muoto-enumeraationa
   * @param uusiruudukko: ruudukko, johon palikka sijoitetaan.
   */
  public Palikka(Muoto uusimuoto, Peliruudukko uusiruudukko)
  {
    muoto = uusimuoto;
    
    try {maaritaMuoto();}
    catch (FileNotFoundException fnfe)
    {
      System.err.println("Muototiedostoa ei löydetty!");
    }
    
    this.ruudukko = uusiruudukko;
    this.viimeksiKaannetty = System.currentTimeMillis();
    
    // Sijainti on likimain keskellä (pyöristetään vasemmalle)
    this.sijainti = new Point((int) (this.ruudukko.annaLeveys()/2.0 - this.koko/2.0), 0);
  }
  
  /**
   * Palikka muodostaa tällä metodilla itselleen totuusarvotaulukon saamansa
   * muodon perusteella. Näin se tietää, missä osassa palikkaa on ruutuja
   * ja missä ei. Palikoiden muototaulukot luetaan erillisestä
   * tekstitiedostosta Muodot.txt
   * 
   * @throws FileNotFoundException: Heittää exceptionin jos tiedostoa
   * muodot.txt ei löydy
   */
  private void maaritaMuoto() throws FileNotFoundException
  {
    File muotofile = new File("Sketchbook/TestScreen/data/muodot.txt");
    System.out.println(muotofile.getAbsolutePath());
    
    // Luodaan skanneri
    Scanner skanneri = new Scanner(muotofile);
    
    while (skanneri.hasNextLine())
    {
      // Lukee rivin kerrallaan
      String rivi = skanneri.nextLine();
      
      // Kun joltain riviltä löydetään oma muoto
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
          
          // Ruudut-taulukko luodaan vasta tässä vaiheessa, sillä
          // ennen ei ollut tietoa koosta
          ruudut = new boolean[koko][koko];
          
          // Käy kaikki rivit läpi
          for (int i = 0; i < koko; i++)
          {
            // Laskee rivin merkit stringinä ja taulukkona
            // Ja siirtyy samalla uudelle riville
            String merkkiryhma = skanneri.nextLine();
            
            char[] merkit = merkkiryhma.toCharArray();
            
            // Käy läpi merkit ja täyttää niiden perusteella
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
              "epäonnistui!");
        }
      }
    }  
  }
  
  // Kertoo, onko kyseisessä sijainnissa ruutu vai ei... duh
  /**
   * Metodi kertoo, onko kyseisessä palikan sisäisessä sijainnissa ruutu vai
   * ei.
   * @param x: tarkasteltavan sektorin x-koordinaatti
   * @param y: tarkasteltavan sektorin y-koordinaatti
   * @return onko sijainnissa ruutua vai ei
   */
  public boolean onkoRuutu(int x, int y)
  {
    // Palauttaa false jos etsitään ruutujen ulkopuolelta
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
   * Kertoo palikan tämänhetkisen sijainnin ruudukossa Point-tyyppisenä
   * arvona
   * @return nykyinen sijainti
   */
  public Point annaSijainti()
  {
    return sijainti;
  }
  
  // Sijoittaa itsensä ruudukkoon (kaikki toiminnallisuudet suoritetaan
  // tayta() -metodissa). Vaihdoin tämän boolean-tyyppiseksi metodiksi,
  // sillä sitten ei tarvitse tehdä tarkistuksia täytön onnistumisesta useassa
  // paikassa (ja se oli niin helppo ja kiva tehdä :))
  /**
   * Sijoittaa palikan ruudukkoon omaan sijaintiinsa, 
   * mikäli se vain on mahdollista
   * @return: onnistuiko sijoittaminen
   */
  public boolean sijoitaRuudukkoon()
  {
    return ruudukko.tayta(sijainti.x, sijainti.y, this);
  }
  
  // Poistaa kaikki käsittämänsä pisteet ruudukosta
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
        // Poistaa tosin vain siinä tapauksessa, että on kyse juuri
        // tämän palikan osista
        if (onkoRuutu(ix, iy))
          ruudukko.tyhjaa(x + ix, y + iy, this);
      }
    }
  }
  
  // Muuttaa palikan sijaintia yhden askeleen pyydettyyn suuntaan,
  // Mikäli se vain on mahdollista
  // PS: Tein tästäkin boolean tyyppisen metodin, jotta ei tarvita
  // erillisiä tarkistuksia
  /**
   * Yrittää liikuttaa palikkaa pyydettyyn suuntaan ja palauttaa, 
   * mikäli tämä oli tai ei ollut mahdollista.
   * @param s: suunta, johon palikkaa liikutetaan
   * @return onnistuttiinko palikkaa liikuttamaan
   */
  public boolean muutaSijaintia(Suunta s)
  {
    if ((s == Suunta.VASEN || s == Suunta.OIKEA) && System.currentTimeMillis()
    - this.viimeksiSiirrettyHorisontaalisesti <= 250) return false;
    this.viimeksiSiirrettyHorisontaalisesti = System.currentTimeMillis();
    // Laskee uuden sijainnin
    Point uusisijainti = new Point(sijainti.x + s.annaX(), 
        sijainti.y + s.annaY());
    
    // Poistetaan palikka ruudukosta
    poistaRuudukosta();
    
    // Jos uuteen sijaintiin voidaan siirtyä, siirtyy, muuten palaa takaisin
    if (!ruudukko.palikkaMahtuuSijaintiin(uusisijainti.x, 
        uusisijainti.y, this))
    {
      sijoitaRuudukkoon();
      return false;
    }
    
    // Siirretään palikka uuteen sijaintiin
    sijainti = uusisijainti;
    // Luodaan palikka uuteen sijaintiin
    sijoitaRuudukkoon();
    
    return true;
  }
  
  /**
   * Kääntää palikkaa 90 astetta positiiviseen suuntaan, mikäli se vain on
   * mahdollista.
   */
  public void kaanna()
  {
    if (System.currentTimeMillis() - this.viimeksiKaannetty <= 500) return;
    this.viimeksiKaannetty = System.currentTimeMillis();
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
      // Vaihtaa kaikki ruudut kivalla algoritmillä
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
      // Kun kyseessä on pystyviivan kääntäminen
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
      // Kun kyseessä on vaakaviivan kääntäminen
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
    
    // Poistaa itsensä ja kääntää itseään
    poistaRuudukosta();
    ruudut = uudet_ruudut;
    
    //Yrittää sijoittaa itseään
    if (!sijoitaRuudukkoon())
    {
      // Jos se ei onnistunut, joutuu kääntämään itsensä takaisin ja
      // sijoittamaan uudestaan
      ruudut = vanhat_ruudut;
      sijoitaRuudukkoon();
    }
  }
  
  // Tiputtaa palikan alas asti
  /**
   * Tiputtaa palikan niin pitkälle kuin sitä voi tiputtaa
   */
  public void tiputa()
  {
    while(muutaSijaintia(Suunta.ALAS))
    {}
  }
  /*
  // Päivittää sijainniksi uuden sijainnin, joka lasketaan kuvastamaan
  // sitä kohtaa, johon tippuva on putoamassa.
  public void paivitaHaamuna(Palikka seurattava)
  {
    // Poistaa aluksi varmuuden vuoksi itsensä ruudukosta ja päivittää
    // Aloitussijainnin
    poistaRuudukosta();
    sijainti = seurattava.annaSijainti();
    
    // Tiputtaa palikkaa alas, mutta pitää sen poissa ruudukosta
    while(muutaSijaintia(Suunta.ALAS))
    {
      poistaRuudukosta();
    }
  }
  */
}

