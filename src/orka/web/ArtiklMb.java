package orka.web;

import java.util.List;
import java.util.Vector;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import orka.model.core.KontrolaException;
import orka.model.core.OrkaClassUtil;
import orka.model.maticni.Artikl;
import orka.model.maticni.JedinicaMjere;
import orka.model.maticni.ObradaArtiklLocal;

@ManagedBean(name = "artiklMb")
@SessionScoped
public class ArtiklMb {

	private ObradaArtiklLocal obrada = (ObradaArtiklLocal) OrkaClassUtil
			.getSessionBean("ObradaArtikl", true);

	public ObradaArtiklLocal getObrada() {
		return obrada;
	}

	public void setObrada(ObradaArtiklLocal obrada) {
		this.obrada = obrada;
	}

	public void obrisi(Artikl artikl) throws KontrolaException {
		getObrada().obrisi(artikl);
	}

	public void spremi() throws KontrolaException {
		if (artikl != null) {
			obrada.spremi(artikl);
		} else {
			artikl = obrada.noviEntitet();
			obrada.spremi(artikl);
		}
		artikl = null;

	}

	private Artikl artikl;

	public Artikl getArtikl() {
		return artikl;
	}

	public void setArtikl(Artikl artikl) {
		this.artikl = artikl;
	}

	public void spremi(Artikl a) throws KontrolaException {
		getObrada().spremi(a);
	}

	public void novi() throws KontrolaException {
		artikl = obrada.noviEntitet();
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

	private Long odabranaJedinicaMjere;

	public Long getOdabranaJedinicaMjere() {
		if (artikl != null && odabranaJedinicaMjere != null) {
			odabranaJedinicaMjere = getArtikl().getJedinicaMjere().getId();
			return odabranaJedinicaMjere;
		}
		return null;
	}

	public void setOdabranaJedinicaMjere(Long odabranaJedinicaMjere) {
		if (odabranaJedinicaMjere != null) {
			artikl.setJedinicaMjere(obrada.getManager().find(
					JedinicaMjere.class, odabranaJedinicaMjere));
		}
		this.odabranaJedinicaMjere = odabranaJedinicaMjere;
	};

	private List<Artikl> artikli = new Vector<Artikl>();

	public List<Artikl> getArtikli() {

		String q = "select ent from Artikl ent order by ent.naziv";
		List<Artikl> resultList = obrada.getManager().createQuery(q)
				.getResultList();

		list = new Vector<SelectItem>();
		q = "select ent from JedinicaMjere ent order by ent.naziv";
		List<JedinicaMjere> listJedMj = obrada.getManager().createQuery(q)
				.getResultList();
		SelectItem si = new SelectItem();
		si.setValue(null);
		si.setLabel("Nije odabrano");
		list.add(si);
		for (JedinicaMjere jedinicaMjere : listJedMj) {
			si = new SelectItem();
			si.setValue(jedinicaMjere.getId());
			si.setLabel(jedinicaMjere.getNaziv());
			list.add(si);
		}

		return resultList;
	}

	public void setArtikli(List<Artikl> artikli) {
		this.artikli = artikli;
	}

	List<SelectItem> list = new Vector<SelectItem>();

	public List<SelectItem> getList() {
		return list;
	}

	public void setList(List<SelectItem> list) {
		this.list = list;
	}

}
