package vmesnik;

import javax.swing.SwingWorker;

import igra.Igralec;
import igra.Poteza;
import inteligenca.MinimaxAlfaBeta;

public class Racunalnik extends Strateg {
	private GlavnoOkno master;
	private Igralec rac;
	private SwingWorker<Poteza,Object> mislec;
	
	public Racunalnik(GlavnoOkno master, Igralec igralec) {
		this.master = master;
		rac = igralec;
	}
	
	@Override
	public void na_potezi() {
		mislec = new MinimaxAlfaBeta(master, 3, rac);
		mislec.execute();
	}

	@Override
	public void prekini() {
		if (mislec != null) {
			mislec.cancel(true);
		}
	}

	@Override
	public void klik (Poteza poteza) {
	}
	
	@Override
	public boolean uporabljaGUI() {
		return false;
	}

}
