package orka.model.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class Entitet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String EVENT_SPREMI = "SPREMI";
	public static final String EVENT_OBRIS = "OBRIS";



	private Date datumPromjene;

	@Column(name = "DATUM_PROMJENE")
	public Date getDatumPromjene() {
		return datumPromjene;
	}

	public void setDatumPromjene(Date datumPromjene) {
		this.datumPromjene = datumPromjene;
	}

	private boolean aktivan = true;

	@Column(name = "AKTIVAN")
	public boolean isAktivan() {
		return aktivan;
	}

	public void setAktivan(boolean aktivan) {
		this.aktivan = aktivan;
	}

	private boolean oznacen;

	@Transient
	public boolean isOznacen() {
		return oznacen;
	}

	public void setOznacen(boolean oznacen) {
		this.oznacen = oznacen;
	}

	private Date datumKreiranja;

	@Column(name = "DATUM_KREIRANJA", updatable = false)
	public Date getDatumKreiranja() {
		return datumKreiranja;
	}

	public void setDatumKreiranja(Date datumKreiranja) {
		this.datumKreiranja = datumKreiranja;
	}

	private String operaterPromjeneString;

	public String getOperaterPromjeneString() {
		return operaterPromjeneString;
	}

	public void setOperaterPromjeneString(String operaterPromjeneString) {
		this.operaterPromjeneString = operaterPromjeneString;
	}

}