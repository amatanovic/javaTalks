package orka.model.maticni;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import orka.model.core.KontrolaException;
import orka.model.core.Obrada;
import orka.model.core.ObradaEntitet;

@Obrada(entitet = JedinicaMjere.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ObradaJedinicaMjere extends ObradaEntitet<JedinicaMjere> implements ObradaJedinicaMjereLocal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public JedinicaMjere kontroliraj(JedinicaMjere entitet)
			throws KontrolaException {
		return entitet;
	}

	@Override
	public JedinicaMjere unutarKontrola(JedinicaMjere entitet) {
		return entitet;
	}

	@Override
	protected JedinicaMjere obradaPrijeKontrole(JedinicaMjere entitet) {
		return entitet;
	}

	@Override
	protected JedinicaMjere unutarPersist(JedinicaMjere entitet) {
		return entitet;
	}

	@Override
	protected JedinicaMjere afterPersist(JedinicaMjere entitet) {
		return entitet;
	}

	@Override
	protected boolean kontPromjene(JedinicaMjere entitet, JedinicaMjere izBaze) {
		return false;
	}

	@Override
	public void resetFields() {
		
	}

	@Override
	protected JedinicaMjere obradaPrijeKontroleBrisi(JedinicaMjere entitet) {
		return null;
	}

	@Override
	public JedinicaMjere createNew(JedinicaMjere entitet) {
		return entitet;
	}

	@Override
	public JedinicaMjere unutarBrisi(JedinicaMjere entitet)
			throws KontrolaException {
		return entitet;
	}
	
}
