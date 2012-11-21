import java.awt.Color;

/**
 * Muoto-enumeraatio vastaa tetrispalikoiden erilaisia muotoja. Nämä voivat
 * olla joko I:n, J:n, L:n, S:n, O:n, Z:n, tai T:n muotoisia tai ilman muotoa,
 * eli palikoiden ulkopuolella. 
 * @author 290289
 */
public enum Muoto
{
	I, J, L, S, O, Z, T, TYHJA;
	
	/**
	 * Palauttaa tiettyä muotoa vastaavan värin. Jokaisella muodolla on oma
	 * värinsä.
	 * @return muodon väri
	 */
	public Color  annaVari()
	{
		switch (this)
		{
		case I: return new Color(255, 255, 255);
		case J: return new Color(55, 216, 1);
		case L: return new Color(255, 36, 173);
		case S: return new Color(26, 66, 255);
		case O: return Color.YELLOW;
		case Z: return new Color(230, 60, 0);
		case T: return new Color(47, 174, 242);
		case TYHJA: return Color.BLACK;
		
		default: return Color.BLACK;
		}
	}
}
