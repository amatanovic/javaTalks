package orka.model.maticni;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import orka.model.core.Entitet;

@Entity
@SequenceGenerator(name = "jedinica_mjere_SEQ", sequenceName = "jedinica_mjere_SEQUENCE", allocationSize = 1)
public class JedinicaMjere extends Entitet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jedinica_mjere_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String naziv = "";

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	private String kratkiNaziv = "";

	public String getKratkiNaziv() {
		return kratkiNaziv;
	}

	public void setKratkiNaziv(String kratkiNaziv) {
		this.kratkiNaziv = kratkiNaziv;
	}

}
