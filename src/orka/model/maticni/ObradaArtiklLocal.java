package orka.model.maticni;

import javax.ejb.Local;

import orka.model.core.ObradaEntitetLocal;

@Local
public interface ObradaArtiklLocal extends ObradaEntitetLocal<Artikl> {

	public Artikl ucitaj(long id);

}