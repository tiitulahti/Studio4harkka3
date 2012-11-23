/**
 * Suunta-enumeraatio kuvastaa erilaisia liikkumissuuntia. Näitä ovat
 * kaksiulotteissen avaruuden suunnat vasen, oikea, ylös ja alas.
 * @author 290289
 */
public enum Suunta
{
  VASEN, OIKEA, YLOS, ALAS;

  // Antaa suunnan X-muutoksen integerinä
  /**
   * Metodi kertoo suunnan vastaavuuden x-koordinaatin muutoksena.
   * Siten, että vasen ja oikea vaikuttavat x:ään ja toiset eivät.
   * @return suunnan arvo x:n muutoksena
   */
  public int annaX()
  {
    switch(this)
    {
    case VASEN: return -1;
    case OIKEA: return 1;
    
    default: return 0;
    }
  }
  
  // Sama Y:lle
  /**
   * Metodi kertoo suunnan vastaavuuden y-koordinaatin muutoksena Siten,
   * että ylös ja alas muuttavat y:n arvoa ja muut eivät.
   * @return suunnan arvo y:n muutoksena
   */
  public int annaY()
  {
    switch(this)
    {
    case YLOS: return -1;
    case ALAS: return 1;
    
    default: return 0;
    }
  }
}

