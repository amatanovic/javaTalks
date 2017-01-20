package orka.model.core;

import java.util.List;

import javax.persistence.EntityManager;

public interface ObradaEntitetLocal<T extends Entitet> extends MessageMangment,
		ActionLocal {

	/**
	 * Klijent SFSB-a poziva ovu metodu kada zeli spremiti promjene u bazu ona
	 * ce pozvati kontrolu i nakon toga ako nema gresaka otvoriti transkaciju te
	 * pozvati persist, ako je greska nastala u persistu to je kraj presistence
	 * conteksta i baca se TransactionException
	 * 
	 * @param entitet
	 * @return
	 * @throws KontrolaException
	 */
	@SuppressWarnings("unchecked")
	public T spremi(T entitet) throws KontrolaException;

	/**
	 * Metoda koja se koristi da bi se ucitao entitet koji ce se obradivat,
	 * jednom rijecju pocetak promjene
	 */
	@SuppressWarnings("unchecked")
	public T entIzBaze(String UID);

	public void resetFields();

	/**
	 * Sluzi za kreiranje novog entiteta anotiranog preko anotiacije Obrada, ako
	 * nema greska vraca string OK i outjectira entitet u njemu pripadajuci
	 * kontext
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T noviEntitet() throws KontrolaException;

	/**
	 * Sluzi za brisanje entiteta
	 * 
	 * @param entitet
	 * @return
	 * @throws KontrolaException
	 * @throws TransactionException
	 */
	public T obrisi(T entitet) throws KontrolaException;

	/**
	 * Ubacije entitet u persitence kontekst SFSB-a
	 * 
	 * @param entitet
	 * @return
	 */
	public Entitet mergeEntitet(Entitet entitet);

	/**
	 * Sluzi za ucitavanje entiteta u presitence context ovog SFSB-a
	 * 
	 * @param klasa
	 * @param id
	 * @return
	 */
	public <M> M ucitajEntitet(Class<M> klasa, long id);

	public T kontrola(T entitet) throws KontrolaException;

	/**
	 * poziva se nakon neuspjele transkacije da ponovo uspostavi persistence
	 * context, po potrebi izvrsisti override metode
	 * 
	 * @param entitet
	 * @return
	 */
	public T reBuildContext(T entitet);

	/**
	 * metoda koja se mora pozvati unutar transakcije, def. ona se i poziva iz
	 * spremi nakon kontrola i otvaranja transkacije
	 * 
	 * @param entitet
	 * @return
	 */
	public T persist(T entitet);

	public EntityManager getManager();

	public void setManager(EntityManager manager);

	public String getRadno();

	public void setRadno(String radno);

	public boolean isPorukaUspjesnoSpremi();

	public void setPorukaUspjesnoSpremi(boolean porukaUspjesnoSpremi);
}