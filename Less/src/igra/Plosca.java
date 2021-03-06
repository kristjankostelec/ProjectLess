package igra;

import java.util.ArrayList;

//Vedela bo, kje so katere bele figurice in kje �rne. 
//Vedela bo, ce je igra ze napol koncana(ce beli postavil vse na koncno mesto), ce je koncana (crni postavil na koncne mesto).


public class Plosca {
	protected Polje[][] vsaPolja;	
	protected int[][] ograjiceVod;
	protected int[][] ograjiceNavp;
	
	public Plosca(int dim) {
		vsaPolja = new Polje[dim][dim];
		ograjiceNavp = new int[dim][dim+1]; //mozne napake: ravno obratno
		ograjiceVod = new int[dim+1][dim];
				
		//poklicemo zacetno postavitev figuric na plosco - beli v zgornji levi kot, crni v spodnji desni
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (i < 2 && j < 2) {
					vsaPolja[i][j] = Polje.BELO;
				} else if (i > dim-3 && j > dim-3) {
					vsaPolja[i][j] = Polje.CRNO;
				} else {
					vsaPolja[i][j] = Polje.PRAZNO;
				}
			}
		}

	//s pomocjo malih ploscic sestavimo veliko plosco - predvsem nakljucno postavimo ograjice
			
	Ploscica[] seznamPloscic = {												
								new Ploscica(new int[] {2,3,6}),				
	                            new Ploscica(new int[] {2,3,4}),
	                            new Ploscica(new int[] {2,3,6}),
	                            new Ploscica(new int[] {2,3}),
	                            new Ploscica(new int[] {0,1}),
	                            new Ploscica(new int[] {0,1}),
	                            new Ploscica(new int[] {1}),
	                            new Ploscica(new int[] {2,4}),
	                            new Ploscica(new int[] {0,1}),
	                            new Ploscica(new int[] {2,3}),
	                            new Ploscica(new int[] {0,3}),
	                            new Ploscica(new int[] {0,3})
								};
	
		int i = 0;
		int j = 0;
		int ploscic = 0;
		ArrayList<Integer> izbrane = new ArrayList<Integer>(10);
		
		while (ploscic < 9) {
			int st = (int) Math.floor(Math.random()*12);
			while (izbrane.contains(st)) {
				st = (int) Math.floor(Math.random()*12); 		//iz seznama ploscic izberemo eno
			}
			izbrane.add(st);
			
			Ploscica pl = seznamPloscic[st];
			pl.nakljucno_rotiraj();	
			
			for (int k : pl.ograjice) { 						//pogledamo vsako ograjico posebej
				
				if (k%2==1) { 									//dodajamo navpicne ograjice
					if (k < 6) {								//prva vrstica na ploscici
						if	(k==1) {
							ograjiceNavp[i][j + 2]++;
						} else if (k==3) {
							ograjiceNavp[i][j + 1]++;
						} else {
							ograjiceNavp[i][j]++;
						}
					} else {									//druga vrstica
						if (k ==7) {
							ograjiceNavp[i+1][j]++;
						} if (k ==7) {
							ograjiceNavp[i+1][j + 1]++;
						} else {
							ograjiceNavp[i+1][j + 2]++;
						}
					}
				
				} else {										 //dodajamo vodoravne ograjice
					if (3 < k && k < 10) {
						if(k==4) {
							ograjiceVod[i][j]++;
						} else if (k==6) {
							ograjiceVod[i+1][j]++;
						} else {
							ograjiceVod[i+2][j]++;
						}
					} else {
						if (k==10) {
						ograjiceVod[i + 2][j+1]++;
						}
						if (k==2) {
							ograjiceVod[i][j+1]++;
							}
						if (k==0) {
							ograjiceVod[i+1][j+1]++;
							}
					}
						
				}
			}
			ploscic++;
			i = (ploscic / 3) * 2;
			j = (ploscic % 3) * 2;
		}
	}
	
	public Plosca(Plosca p) {
		int dim = p.getDim();
		this.vsaPolja = new Polje[dim][dim];
		this.ograjiceNavp = new int[dim][dim];
		this.ograjiceVod = new int[dim][dim];
		
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				this.vsaPolja[i][j] = p.vsaPolja[i][j];
				this.ograjiceNavp[i][j] = p.ograjiceNavp[i][j];
				this.ograjiceVod[i][j] = p.ograjiceVod[i][j];
			}
		}
	}
		
	
	
	public Lokacija[] crnaPolja(){
		Lokacija[] l = new Lokacija[4];
		int p = 0;
		for (int i=0; i < getDim(); i++){
			for (int j=0; j < getDim(); j++){
				if (this.vsaPolja[j][i] == Polje.CRNO){
					l[p] = new Lokacija(i,j);
					p++;
				}
			}
		}
		return l;
	}
	

	public Lokacija[] belaPolja(){
		Lokacija[] l = new Lokacija[4];
		int p = 0;
		for (int i=0; i < getDim(); i++){
			for (int j=0; j < getDim(); j++){
				if (this.vsaPolja[j][i] == Polje.BELO){
					l[p] = new Lokacija(i,j);
					p++;
				}
			}
		}
		return l;
	}
	
	/**
	 * 
	 * @return ali je crni postavil vse figure na ciljna polja in je igra koncana 
	 */
	public boolean konecCrni() {       											//crni je vse svoje figurice pripeljal do konca --> igra se takoj zakljuci
		return (vsaPolja[0][0] == Polje.CRNO && vsaPolja[1][0] == Polje.CRNO && 
				vsaPolja[0][1] == Polje.CRNO && vsaPolja[1][1] == Polje.CRNO);
	}

	/**
	 * 
	 * @return ali je beli postavil vse figure na ciljna polja in ce crni v treh potezah ne konca, je zmagal beli
	 */
	
	public boolean konecBeli() {       //beli je vse svoje figurice pripeljal do konca
		int dim = getDim();
		return (vsaPolja[dim-1][dim-1] == Polje.BELO && vsaPolja[dim-2][dim-1] == Polje.BELO && 
				vsaPolja[dim-1][dim-2] == Polje.BELO && vsaPolja[dim-2][dim-2] == Polje.BELO);
	}
	
	

	public Polje[][] getVsa_polja() {
		return vsaPolja;
	}

	public int[][] getOgrajiceVod() {
		return ograjiceVod;
	}

	public Polje[][] getVsaPolja() {
		return vsaPolja;
	}

	public void setVsaPolja(Polje[][] vsaPolja) {
		this.vsaPolja = vsaPolja;
	}

	public void setOgrajiceVod(int[][] ograjiceVod) {
		this.ograjiceVod = ograjiceVod;
	}

	public void setOgrajiceNavp(int[][] ograjiceNavp) {
		this.ograjiceNavp = ograjiceNavp;
	}

	public int[][] getOgrajiceNavp() {
		return ograjiceNavp;
	}

	public int getDim() {
		return this.vsaPolja.length;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plosca other = (Plosca) obj;
		for (int i = 0; i < Igra.DIM ; i++ ){
			for (int j = 0; j < Igra.DIM; j++) {
				if (this.getVsa_polja()[i][j] != other.getVsa_polja()[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
}
	

