import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import processing.core.*;
import processing.video.*;
import java.awt.Point;

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
     * Jos kerran näyttöä ei saa päivittää
     * muualta kuin pelipaneelista ja timerin toteuttajaan on pääsy ainoas-
     * taan tetris luokasta niin millä hitsillä tota pystyy päivittää teke-
     * mättä kahta timerin toteuttajaa (mikä on typerää haaskausta!)
     * Miksei tästä a) voi tehdä omaa luokkaa, joka käyttää listenerinä
     * pelipaneelia, tai b) saa oikeuksia nayta()-metodin käyttöön???
     * 
     * Mun täytyy ratkaista tää ylimääräinen hauskuus kuitenkin jollain
     * tavalla, joten käytän nayta()-metodia nyt tehhtävänannonvastaisesti
     * publiccina.
     * 
     * Toisekseen, mikä ajatus "Timerin pysäyttämisellä" on? Pelihän
     * jatkuu (ainakin normaalissa tetriksessä) ihan tavalliseen tapaan,
     * vaikka palikka osuisikin maahan...
     */
    
    private Timer time;
    private boolean on_paalla;
    
    /**
     * Luo uuden ajoittajan ja laittaa sen päälle
     * @param aikavali määrittää, kuinka usein ajoittaja tiputtaa palikoita
     */
    private Ajoittaja(int aikavali)
    {
      // Luo uuden timerin, jota sitten kuunnellaan
      time = new Timer(aikavali, this);
      time.addActionListener(this);
      time.start();
      
      on_paalla = true;
    }
    
    // Pysäyttää pelin etenemisen hetkeksi
    private void pysayta()
    {
      time.stop();
      on_paalla = false;
    }
    
    // Käynnistää ajastimen uudestaan tietyllä aikavälillä
    private void reset(int aikavali)
    {
      // Jos aikaväli on nolla, pitää saman aikavälin
      if (aikavali != 0)
        time.setDelay(aikavali);
      time.start();
      on_paalla = true;
    }
    
    // Kertoo, onko ajastin käynnissä
    private boolean onPaalla()
    {
      return on_paalla;
    }

    // Siirtää tippuvaa palikka alas(, mikäli mahdollista)
    // Ja päivittää näytön. Ilmaisee myös pelin päättymisen
    @Override
    /**
     * Ajoittaja ajaa tämän metodin tietyin väliajoin. Täällä ajoittaja
     * tiputtaa aktiivista palikkaa ja hoitaa uusien palikoiden tiputuksen.
     * Lisäksi ajoittaja tarkistaa pelin loppumisen ja huolehtii
     * siihen liittyvistä tehtävistä
     */
    public void actionPerformed(ActionEvent e)
    {  
      
      System.out.println("action performed");
      /* Tarkistetaan, onko peli päättynyt
       * Huom: käytän tässä loppuikoPeli()-metodia, sillä se päivittyy
       * Peliruudukko-luokassa hieman tehokkaammin kuin tippuva
       * (Lisäksi sitä ei vielä olla käytetty missään muuallakaan)
       */
      if (peliruudukko.loppuikoPeli())
      {
        // Pelin päättyessä vaihetaan teksti ja pysäytetään ajastin
        //asetaAlaTeksti("Hävsit pelin :D");
        time.stop();

      }
      
      // Muulloin liikutetaan palikoita
      Palikka tippuva = peliruudukko.annaTippuva();
      
      // Jos tippuva = null, tiputtaa uuden palikan ja jatkaa vasta
      // ensi stepillä
      if (tippuva == null)
      {
        peliruudukko.tiputaUusiPalikka();
        return;
      }
      
      // Mikäli palikka tippui alas asti, luo uuden tippuvan palikan
      // Ja päivittää seuraavan kuvan
      if (!tippuva.muutaSijaintia(Suunta.ALAS))
      {
        peliruudukko.tiputaUusiPalikka();
    /*    // Asettaa nopeuden pistelisäyksen mukaan
        stepaika *= 0.995;
        reset(stepaika);*/
        
      }
      
      // Päivittää screenin (minkä muun kautta tän olis voinu tehä?)
      pelicanvas.piirra();
    }
  }
}

