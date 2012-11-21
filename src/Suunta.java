/**
 * Suunta-enumeraatio kuvastaa erilaisia liikkumissuuntia. N�it� ovat
 * kaksiulotteissen avaruuden suunnat vasen, oikea, yl�s ja alas.
 * @author 290289
 */
public enum Suunta
{
	VASEN, OIKEA, YLOS, ALAS;

	// Antaa suunnan X-muutoksen integerin�
	/**
	 * Metodi kertoo suunnan vastaavuuden x-koordinaatin muutoksena.
	 * Siten, ett� vasen ja oikea vaikuttavat x:��n ja toiset eiv�t.
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
	 * ett� yl�s ja alas muuttavat y:n arvoa ja muut eiv�t.
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
