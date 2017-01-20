package orka.model.maticni;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import orka.model.core.Entitet;

 

@Entity
@SequenceGenerator(name = "Artikl_SEQ", sequenceName = "Artikl_SEQUENCE", allocationSize = 1)
public class Artikl extends Entitet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

 
	private Long id;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Artikl_SEQ")
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

	private String barkod = "";

	public String getBarkod() {
		return barkod;
	}

	public void setBarkod(String barkod) {
		this.barkod = barkod;
	}
	
	private JedinicaMjere jedinicaMjere;
	
	@ManyToOne
	public JedinicaMjere getJedinicaMjere() {
		return jedinicaMjere;
	}
	
	public void setJedinicaMjere(JedinicaMjere jedinicaMjere) {
		this.jedinicaMjere = jedinicaMjere;
	}
	 
}