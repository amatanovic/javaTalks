package orka.model.core;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public abstract class ObradaEntitet<T extends Entitet> extends Action implements
		ObradaEntitetLocal<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<T> rezultati = new Vector<T>();

	private boolean porukaUspjesnoSpremi = true;

	public List<T> getRezultati() {
		return rezultati;
	}

	public void setRezultati(List<T> rezultati) {
		this.rezultati = rezultati;
	}

	/**
	 * Klasicni logger
	 */
	private static final Logger log = Logger.getLogger(ObradaEntitet.class);
	/**
	 * Ident posla preko kojeg se izvlaci posao, sluzi tome da se moze prijaviti
	 * vise poslova koji koriste isti proces
	 * 
	 */
	protected String posaoIdent = "";

	/**
	 * Kao i posaoIdent sluzi tome da se moze prijaviti vise poslova koji
	 * koriste isti proces i da se napravi distinkcija izmedju tih poslova
	 * nazivom zadatka u zadacima operatera/grupe
	 * 
	 */
	protected String posaoZadatak = "";

	@PersistenceContext(unitName = "kupnjaDS")
	protected EntityManager manager;

	
	/**
	 * Glavna kontorola nekog entiteta prije spremanja u bazu, za dodavanje
	 * kontrola na entitetu potrebno je pregaziti ovu metodu i odmah kao prvu
	 * liniju pozvati entitet=super.kontrola(entitet)
	 * 
	 * @param entitet
	 * @return
	 * @throws KontrolaException
	 */
	public T kontrola(T entitet) throws KontrolaException {
		clearGreske();
		Obrada anno = this.getClass().getAnnotation(Obrada.class);
		if (anno == null) {
			throw new RuntimeException("NEMA ANOTACIJU OBRADA");
		}
		// kontrolaLicence(entitet.getOperaterPromjene());

		entitet = obradaPrijeKontrole(entitet);
		entitet = kontroliraj(entitet);
		entitet = unutarKontrola(entitet);
		if (isRollBack()) {
			log.debug("Greska u kontrolama " + " baci KontrolaException");
			throw new KontrolaException();
		}
		return entitet;
	}

	private String radno = "";

	public String getRadno() {
		return radno;
	}

	public void setRadno(String radno) {
		this.radno = radno;
	}

	/**
	 * Kontrolila licencu za rad
	 * 
	 * @throws KontrolaException
	 */

	/**
	 * Metoda u kojoj bi se trebalo dodavati kontrole za entitet ili entitete
	 * koji se spremaju
	 * 
	 * @param entitet
	 * @return
	 * @throws KontrolaException
	 */
	public abstract T kontroliraj(T entitet) throws KontrolaException;

	/**
	 * Metoda se koristi kada se vise obrada vezuje u jednu transakciju da bi se
	 * sve kontrole izvrsile prije otvaranja transakcije, znaci u ovoj metodi
	 * povezati dvije obrade i pozvati kontrolu na drugoj obradi
	 * 
	 * @param entitet
	 * @return
	 */
	public abstract T unutarKontrola(T entitet);

	/**
	 * Metoda koja ce se pozvati prije kontrole kada spremamo neki entitet u
	 * bazu u njoj je moguce izvristi razne huncuntarije :) prije ulasak u
	 * kontrolu pottrebno ju je samo pregaziti i postivati pravilo da se u
	 * metodi koja je gazi opet ona prvo pozove pogotovo ako imamo visestruko
	 * nasljedivanje
	 * 
	 * @param entitet
	 * @return
	 */
	protected abstract T obradaPrijeKontrole(T entitet);

	/**
	 * Metoda je vrlo bitna odvija se unutra transakcije, u slucaju da se veze
	 * vise obrada u jednu transakciju, u toj metodi ce se pozvati persist druge
	 * obrade
	 * 
	 * @param entitet
	 * @return
	 */
	protected abstract T unutarPersist(T entitet);

	/**
	 * Merge-a entitet u persistence context
	 * 
	 * @param entitet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T merge(T entitet) {
		return (T) mergeEntitet(entitet);
	}

	/**
	 * Metoda koja poziva kontrole i nareduje manageru da persistitra entitet,
	 * baca RollBackException ako je bilo gresaka<br>
	 * poziva ove metode:<br>
	 * obradaPrijeKontrole<br>
	 * kontrola<br>
	 * unutarSpremi<br>
	 * tim navednim redosljedom
	 * 
	 * @throws TransactionException
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public T spremi(T entitet) throws KontrolaException {
		log.debug("ENTITET SPREMI " + entitet);
		entitet = manager.merge(entitet);
		entitet = kontrola(entitet);

		try {
			entitet = persist(entitet);
		} catch (Exception e) {
			log.error("GRESKA ROLLBACK TRANSACTION ", e);
			// rollBackTransaction();
			sendMessage("", FacesMessage.SEVERITY_ERROR,
					"ObradaEntitet.transactionRolledBack", null);
			sendMessage("", FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
			throw new RuntimeException("ObradaEntitet.transactionRolledBack", e);
		}

		if (porukaUspjesnoSpremi) {
			sendMessage("", FacesMessage.SEVERITY_INFO,
					"ObradaEntitet.entitetUspjesnoSpremljen", null);
		}

		return entitet;
	}

	public T persist(T entitet) {

		manager.joinTransaction();
		entitet = unutarPersist(entitet);

		log.debug("ENTITET KOJI SE PERSISTA " + entitet);
		manager.persist(entitet);

		entitet = afterPersist(entitet);

		return entitet;

	}

	public T reBuildContext(T entitet) {

		manager.clear();
		entitet = manager.merge(entitet);
		manager.refresh(entitet);

		return entitet;

	}

	/**
	 * Poziva se nakon presista u metodi spremi
	 * 
	 * @param entitet
	 * @return
	 */
	protected abstract T afterPersist(T entitet);

	/**
	 * u ovoj metodi se usporeduje promjene na entitetu, metoda ne podrzava
	 * transkaciju
	 * 
	 * @param entitet
	 * @param izBaze
	 * @return
	 */
	protected abstract boolean kontPromjene(T entitet, T izBaze);

	/**
	 * Metoda koja se koristi da bi se ucitao entitet koji ce se obradivat,
	 * jednom rijecju pocetak promjene
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public T entIzBaze(String id) {
		resetFields();
		Obrada anno = this.getClass().getAnnotation(Obrada.class);
		log.debug("Dohvacam entitet iz baze: " + anno);
		if (anno == null) {
			throw new RuntimeException("NEMA ANOTACIJU OBRADA");
		}

		Class<T> klasaEntiteta = (Class<T>) anno.entitet();

		T entitet = manager.find(klasaEntiteta, id);
		entitet = manager.merge(entitet);
		manager.refresh(entitet);
		log.debug("Dovacen entitet iz baze: " + entitet);

		return entitet;
	}

	/**
	 * Sluzi za resetiranje polja koja su definirana na SFSB-u znaci u metodi
	 * navedete polja sa null ili praznim vrijednostima
	 */
	public abstract void resetFields();

	/**
	 * Glavana kontrola prije brisanja isti prinicip kao i kod metode kontrola
	 * 
	 * @param entitet
	 * @return
	 */
	protected T kontrolaBrisi(T entitet) {
		Obrada anno = this.getClass().getAnnotation(Obrada.class);
		if (anno == null) {
			throw new RuntimeException("NEMA ANOTACIJU OBRADA");
		}

		return entitet;
	}

	/**
	 * obrada prije kontole brisanja isti prinicip kao i kod obradaPrijeKontrole
	 * 
	 * @param entitet
	 * @return
	 */
	protected abstract T obradaPrijeKontroleBrisi(T entitet);

	/**
	 * Sluzi za kreiranje novog entiteta anotiranog preko anotiacije Obrada, ako
	 * nema greska vraca string OK i outjectira entitet u njemu pripadajuci
	 * kontext
	 * 
	 * @returns
	 */
	@SuppressWarnings("unchecked")
	public T noviEntitet() throws KontrolaException {
		resetFields();
		Obrada anno = this.getClass().getAnnotation(Obrada.class);
		if (anno == null) {
			throw new RuntimeException("NEMA ANOTACIJU OBRADA");
		}
		T o = null;
		try {
			o = (T) anno.entitet().newInstance();

		} catch (InstantiationException e) {
			throw new RuntimeException("GRESKA", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("GRESKA", e);
		}

		o = createNew(o);

		if (o == null) {
			sendMessage("", FacesMessage.SEVERITY_ERROR,
					"ObradaEntitet.nemoguceKreiratNoviEntitet", null);
			throw new KontrolaException();
		}

		return o;

	}

	/**
	 * Kontrola prije kreiranja novog entiteta
	 * 
	 * @param o
	 * @return
	 */
	public T kontrolaPrijeCreateNew(T o) {

		return o;
	}

	/**
	 * metoda koja ce biti pozvana ako se zeli kreirati novi entitet u nju
	 * mozemo postaviti zeljene atribute u novi entitet
	 * 
	 * @param entitet
	 * @return
	 */
	public abstract T createNew(T entitet);

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public T obrisi(T entitet) throws KontrolaException {
		log.debug("REMOVE ENTITET " + entitet);

		clearGreske();
		entitet = manager.merge(entitet);
		entitet = obradaPrijeKontroleBrisi(entitet);
		entitet = kontrolaBrisi(entitet);
		if (isRollBack()) {
			log.debug("NOTCOMMIT EXCEPTION");
			throw new KontrolaException();
		}

		try {

			entitet = unutarBrisi(entitet);
			manager.joinTransaction();

			manager.remove(entitet);

			manager.flush();
			sendMessage("", FacesMessage.SEVERITY_INFO,
					"ObradaEntitet.obrisano", null);
		} catch (Exception e) {

			sendMessage("", FacesMessage.SEVERITY_ERROR, "greskaKodBrisanja",
					null);
			// throw new RuntimeException("ObradaEntitet.transactionRolledBack",
			// e);
		}

		return entitet;

	}

	/**
	 * Poziva se iz metode brisi, sluzi za dodatne operacije koje se zele
	 * izvesti unutar brisanja.
	 * 
	 * @param entitet
	 * @return
	 */
	public abstract T unutarBrisi(T entitet) throws KontrolaException;

	/**
	 * Ubacije entitet u persitence kontekst SFSB-a
	 * 
	 * @param entitet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Entitet mergeEntitet(Entitet entitet) {
		if (!manager.contains(entitet)) {
			return manager.merge(entitet);
		}
		return entitet;
	}

	/**
	 * Sluzi za ucitavanje entiteta u presitence context ovog SFSB-a
	 * 
	 * @param klasa
	 * @param id
	 * @return
	 */
	public <M> M ucitajEntitet(Class<M> klasa, long id) {

		M o = manager.find(klasa, id);

		return o;
	}

	@PostConstruct
	public void postConstruct() {

	}

	@PreDestroy
	public void preDestroy() {

	}

	public EntityManager getManager() {
		return manager;
	}

	public void setManager(EntityManager manager) {

		this.manager = manager;
	}

	public BigDecimal getAsObject(String value) {

		if (value == null || value.trim().equals(""))
			return BigDecimal.ZERO;
		DecimalFormat fb;

		fb = new DecimalFormat("###,###.######");
		fb.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.getDefault()));
		BigDecimal broj = null;
		try {
			broj = new BigDecimal(fb.parse(value).toString());
			return broj;

		} catch (ParseException e) {
			return BigDecimal.ZERO;
		}

	}

	public String getPosaoIdent() {
		return posaoIdent;
	}

	public void setPosaoIdent(String posaoIdent) {
		this.posaoIdent = posaoIdent;
	}

	public String getPosaoZadatak() {
		return posaoZadatak;
	}

	public void setPosaoZadatak(String posaoZadatak) {
		this.posaoZadatak = posaoZadatak;
	}

	public boolean isPorukaUspjesnoSpremi() {
		return porukaUspjesnoSpremi;
	}

	public void setPorukaUspjesnoSpremi(boolean porukaUspjesnoSpremi) {
		this.porukaUspjesnoSpremi = porukaUspjesnoSpremi;
	}
}