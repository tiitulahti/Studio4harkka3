import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import processing.core.*;
import processing.video.*;
import java.awt.Point;

import ddf.minim.*;


  private Pelicanvas pelicanvas;
  private Peliruudukko peliruudukko;
  private Ajoittaja ajastin;
  
  Minim minim;
  AudioInput in;
  long aika1;
  long aika;
  


    Capture cam;
    Blobfinder2 blobfinder;
    Blobfinder2 blobfinder2;
    Blobfinder2 changedLast;
    public static color myMovieColors[];

    @Override
    public void setup(){
        size(560,480);
        
        this.peliruudukko = new Peliruudukko(15, 30);
        this.pelicanvas = new Pelicanvas(this.peliruudukko, this, 320, 0);
    
        this.ajastin = new Ajoittaja(750);
        
        
        this.minim = new Minim(this);
        this.minim.debugOn();
        this.aika1 = 0;  
        this.in = minim.getLineIn(Minim.STEREO, 512);
        
        color c1 = color(235, 39, 39);
        this.blobfinder = new Blobfinder2(320, 240, this, c1, 1);
        
        color c2 = color(231, 102, 241);
        this.blobfinder2 = new Blobfinder2(320, 240, this, c2, 2);

        String[] cameras = Capture.list();

        if (cameras.length == 0) {
            println("There are no cameras available for capture.");
            exit();
        } else {
            println("Available cameras:");
            for (int i = 0; i < cameras.length; i++) {
                println(cameras[i]);
            }

            // The camera can be initialized directly using an 
            // element from the array returned by list():
            cam = new Capture(this, 320, 240);
            cam.start();     
        }     
    }
    
    void mousePressed(){
      int pixels = this.blobfinder.getNumPixelsX();
      System.out.println(pixels*mouseY + mouseX);
      color c = this.blobfinder.getMyMovieColors(pixels*mouseY + mouseX);
      System.out.println("painettu väri:" + c);
        
      if (this.changedLast == null || this.changedLast == this.blobfinder2){
        this.blobfinder.setSampleColor(c);
      } else {
        this.blobfinder2.setSampleColor(c);
      }  
    }

    public void draw() {
      pushMatrix();
      scale(-1,1);
      translate(-320,0);
      
        if (cam.available() == true) {
            cam.read();
            image(cam, 0, 0);
        }
        
        
        //this.blobfinder.draw();
        //this.blobfinder.getCurrentBlobCenter();
        
        
        //this.blobfinder2.getCurrentBlobCenter();
        
        Point p1 = this.blobfinder.getCurrentBlobCenter();
        this.blobfinder.draw();
        Point p2 = this.blobfinder2.getCurrentBlobCenter();
        this.blobfinder2.draw();
        //System.out.println(calculateAngle(p1, p2));
        //System.out.println("blobfinder1: " + p1);
        //System.out.println("blobfinder2: " + p2);
        
        if (this.monitorSwing(p1, p2)){
          if (this.peliruudukko.annaTippuva() != null){
            this.peliruudukko.annaTippuva().kaanna();
          }
        }  
        
        this.monitorMovement(p1, p2);
        this.monitorDropping();
        
        popMatrix();
        
        this.pelicanvas.piirra();
       
        //System.out.println(this.blobfinder.getCurrentPointCenter());
        //The following does the same, and is faster when just drawing the image
        //without any additional resizing, transformations, or tint.
        //set(0, 0, cam);
    }
    
    public Capture getCam(){
        return this.cam;
    }
    
  public double calculateAngle(Point point1, Point point2){
    int x1 = (int) point1.getX();
    int x2 = (int) point2.getX();
    int y1 = (int) point1.getY();
    int y2 = (int) point2.getY();
    
    if (y1 > y2){
      
    }  
    
    PVector a = new PVector(0,1);
    PVector b = new PVector((x2-x1),(y2-y1));
    
    float num = a.dot(b);
    float denum = a.mag()*b.mag();
    
    return Math.toDegrees(acos(num/denum));
    
  }
  
  
  public Point[] organizePointsTopToBottom(Point point1, Point point2){
    Point[] organizedPoints = new Point[2];
    if (point1.getY() < point2.getY()) {
      organizedPoints[0] = point1;
      organizedPoints[1] = point2;
    } else {
      organizedPoints[0] = point2;
      organizedPoints[1] = point1;
    }
    return organizedPoints;
  }
    
  public boolean monitorSwing(Point point1, Point point2){
    Point[] organizedPoints = this.organizePointsTopToBottom(point1, point2);
    
    Point upperPoint = organizedPoints[0];
    Point lowerPoint = organizedPoints[1];
    
    if (upperPoint.getX() - lowerPoint.getX() < -70){
      //pyöräytäMyötäpäivään
      System.out.println("Pyötäytä myötäpäivään");
      return true;
    }
    
    else if (upperPoint.getX() - lowerPoint.getX() > 70){
      //pyöräytä vastapäivään 
      System.out.println("Pyötäytä vastapäivään");
      return true;
    }  
    
    return false;
    
  }
  
  public boolean monitorMovement(Point point1, Point point2){
    if (this.peliruudukko.annaTippuva() == null) return false;
    if (point1.getX() > this.blobfinder.getNumPixelsX()*0.7 && point2.getX() > this.blobfinder.getNumPixelsX()*0.7) {
      this.peliruudukko.annaTippuva().muutaSijaintia(Suunta.VASEN);
      System.out.println("Siirry vasemmalle");
      return true;
    } else if (point1.getX() < this.blobfinder.getNumPixelsX()*0.3 && point2.getX() < this.blobfinder.getNumPixelsX()*0.3) {
      System.out.println("Siirry oikealle");
      this.peliruudukko.annaTippuva().muutaSijaintia(Suunta.OIKEA);
      return true;
    }
    return false;
  }
  
   
  public void monitorDropping(){
    System.out.println("monitorDropping()");
    for(int i = 0; i < in.bufferSize() - 1; i++){
      if(in.left.get(i) > 0.8 && in.right.get(i) > 0.8){
        System.out.println("monitorDropping(): 2. vaihe");
        aika = millis();      
        if((aika1 == 0 || aika-aika1 > 500) && this.peliruudukko.annaTippuva() != null){
          System.out.println("monitorDropping(): 3. vaihe");
          this.peliruudukko.annaTippuva().tiputa();
          aika1 = aika;
        }
      }
    }
  }
  
  void stop(){
    in.close();
    minim.stop();  
    super.stop();
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
      
      //System.out.println("action performed");
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

