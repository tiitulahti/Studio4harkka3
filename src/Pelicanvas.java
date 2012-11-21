
import java.awt.Color;

import processing.core.*;

public class Pelicanvas {
	private int leveys;
	private int korkeus;
	private int sijaintix;
	private int sijaintiy;
	private Peliruudukko ruudukko;
	private Tetris tetris;
	
    public Pelicanvas(Peliruudukko ruudukko, Tetris tetris, int x, int y){
		this.ruudukko = ruudukko;
		this.tetris = tetris;
		
		this.sijaintix = x;
		this.sijaintiy = y;
		
		this.korkeus = this.ruudukko.annaKorkeus();
		this.leveys = this.ruudukko.annaLeveys();
		
		this.piirra();
		
	}
 
 	public void piirra(){
 		//System.out.println("piirtaa");
 		this.tetris.fill(0,0,0);
 		this.tetris.rect(0,0,this.leveys*16, 
 				this.korkeus*16);
 		
 		for(int x = 0; x < this.leveys; x++){
 			for(int y = 0; y < this.korkeus; y++){
 			//	System.out.println(this.ruudukko.annaMuoto(x, y));
 				if(this.ruudukko.annaMuoto(x, y) != Muoto.TYHJA){
 				//	System.out.println("muoto löytyy");
 					Color vari = this.ruudukko.annaMuoto(x, y).annaVari();
 					int r = vari.getRed();
 					int g = vari.getGreen();
 					int b = vari.getBlue();
 					this.tetris.fill(r, g, b);
 					//sijaintix ja -y marginaalit piirrolle
 					this.tetris.rect(this.sijaintix+x*16, 
 								this.sijaintiy+y*16, 16, 16);
 				}
 			}
 		}
 	}
	
	
}
