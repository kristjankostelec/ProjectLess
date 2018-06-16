package inteligenca;

import igra.Igra;
import igra.Igralec;
import igra.Lokacija;

public class Ocena1 {
	//ocenjevalna kot smo jo naredili na govorilni uri 
	
	public static final int ZMAGA = (1 << 20); // vrednost zmage, vec kot vsaka druga ocena pozicije
	public static final int ZGUBA = -ZMAGA;  // vrednost izgube, mora biti -ZMAGA
	public static final int NEODLOCENO = 0;  // vrednost neodlocene igre

	/**
	 * @param jaz - igralec, ki zeli oceno
	 * @param igra - trentno stanje igre (ne spreminjaj tega objekta!)
	 * @return ocena - vrednosti pozicije (ce je igre konec, je ocena zagotovo pravilna)
	 */
	public static int oceniPozicijo(Igralec jaz, Igra igra) {
		switch (igra.getTrenutnoStanje()) {
		case ZMAGA_BELI:
			return (jaz == Igralec.BELI ? ZMAGA : ZGUBA);
		case ZMAGA_CRNI:
			return (jaz == Igralec.CRNI ? ZMAGA : ZGUBA);
		case NEODLOCENO:
			return NEODLOCENO;
		default:
		
		// Ovrednotimo glede na to, koliko kvote potrebuje vsak igralec do koncne pozicije
		int vrednostBeli = 0;
		int vrednostCrni = 0;
		Lokacija[] figuriceBeli = igra.getIgralnaPlosca().belaPolja();
		Lokacija[] figuriceCrni = igra.getIgralnaPlosca().crnaPolja();
		
		for (Lokacija bFig : figuriceBeli){
			vrednostBeli += 12 - optimalnaBeli(bFig,igra,0);
			vrednostBeli += narazen(figuriceBeli);
		}
		for (Lokacija cFig : figuriceCrni){
			vrednostCrni += 12 - optimalnaCrni(cFig,igra,0);
			vrednostCrni += narazen(figuriceCrni);
		}
		
		
		//Vrednost iz stališča belega: 
		int vrednost = (igra.getNaPotezi() == Igralec.BELI) ? 
				(20*vrednostBeli - vrednostCrni) :
				(20*vrednostCrni - vrednostBeli) ;

	
		return ((jaz == Igralec.BELI) ? vrednost : (-vrednost));
		}
	}
	
	
	//racunamo optimalno pot za dano figurico dane barve
	//mozne izboljsave: zdruzitev obeh sledecih metod z dodatnim parametrom "igralec"
	public static int optimalnaBeli (Lokacija l, Igra igra, int ocena){			 
			int[][] ograjiceVod = igra.getIgralnaPlosca().getOgrajiceVod();
			int[][] ograjiceNavp = igra.getIgralnaPlosca().getOgrajiceNavp();
			int x = l.getX();
			int y = l.getY();
			int dim = igra.getIgralnaPlosca().getDim();
			int vrednostDol = 0;
			int vrednostDesno = 0;
			
			if((x+1)!=dim-1 || (y+1)!=dim-1){
				if(x+1 < dim){
					vrednostDesno =  optimalnaBeli(new Lokacija(x + 1, y), igra, ocena + 1 + ograjiceNavp[y][x+1]);
				}else {
					vrednostDesno = 1500;
				}
				if(y+1 < dim){
					vrednostDol = optimalnaBeli(new Lokacija(x, y + 1), igra, ocena + 1 + ograjiceVod[y+1][x]);			
				} else{
					vrednostDol = 1500;
				}
				return Math.min(vrednostDesno, vrednostDol);
			}
			
			
			return ocena;
	}
	
	public static int optimalnaCrni (Lokacija l, Igra igra, int ocena){ 
		int[][] ograjiceVod = igra.getIgralnaPlosca().getOgrajiceVod();
		int[][] ograjiceNavp = igra.getIgralnaPlosca().getOgrajiceNavp();
		int x = l.getX();
		int y = l.getY();
		int vrednostGor = 0;
		int vrednostLevo = 0;
		
		if((x-1)!=0 || (y-1)!=0){
			if(x-1 > 0){
				vrednostLevo = optimalnaCrni(new Lokacija(x - 1, y), igra, ocena + 1 + ograjiceNavp[y][x]);
			}else {
				vrednostLevo = 1500;
			}
			if(y-1 > 0){
				vrednostGor = optimalnaCrni(new Lokacija(x, y - 1), igra, ocena + 1 + ograjiceVod[y][x]);			
			} else{
				vrednostGor = 1500;
			}
			return Math.min(vrednostLevo, vrednostGor);
		}
		
		
		return ocena;
		
}
	
	
	/*premikanje v desno in dol je za belega dobro 
			public static int dobrePotezeBeli (Lokacija b, Igra igra){
		int rez = 0;
		int x = b.getX();
		int y = b.getY();
		LinkedList<Lokacija> mozne = igra.moznePoteze(b, igra.getKvotaPremikov());
		if (mozne.contains(new Lokacija(x+1,y))){
			rez += 1;
			}
		if (mozne.contains(new Lokacija(x,y+1))){
			rez += 1;
			}
		return rez;
		}
	*/
	
	public static int narazen (Lokacija[] figurice){
		int max = 0;
		int x1 = 0;
		int x2 = 0;
		int y1 = 0;
		int y2 = 0;
		for (Lokacija l1 : figurice){
			for (Lokacija l2 : figurice){
				x1 = l1.getX();
				x2 = l2.getX();
				y1 = l1.getY();
				y2 = l2.getY();
				int mmax = Math.max(Math.abs(x1-x2),Math.abs(y1-y2));
			if (mmax > max){
				max = mmax;
			}
			}
		}
		return max;
	}
	
}