package orka.model.maticni;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import orka.model.core.KontrolaException;
import orka.model.core.Obrada;
import orka.model.core.ObradaEntitet;

@Obrada(entitet = Artikl.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ObradaArtikl extends ObradaEntitet<Artikl> implements
		ObradaArtiklLocal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Artikl kontroliraj(Artikl entitet) throws KontrolaException {

		return entitet;
	}

	@Override
	public Artikl unutarKontrola(Artikl entitet) {
		return entitet;
	}

	@Override
	protected Artikl obradaPrijeKontrole(Artikl entitet) {
		return entitet;
	}

	@Override
	protected Artikl unutarPersist(Artikl entitet) {
		return entitet;
	}

	@Override
	protected Artikl afterPersist(Artikl entitet) {
		return entitet;
	}

	@Override
	protected boolean kontPromjene(Artikl entitet, Artikl izBaze) {

		return false;
	}

	@Override
	public void resetFields() {

	}

	@Override
	protected Artikl obradaPrijeKontroleBrisi(Artikl entitet) {
		return entitet;
	}

	@Override
	public Artikl createNew(Artikl entitet) {
		return entitet;
	}

	@Override
	public Artikl unutarBrisi(Artikl entitet) throws KontrolaException {
		return entitet;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Artikl ucitaj(long id) {
		Artikl artikl = super.ucitajEntitet(Artikl.class, id);

		return artikl;
	}

}