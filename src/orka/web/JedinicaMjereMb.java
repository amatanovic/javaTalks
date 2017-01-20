package orka.web;

import java.util.List;
import java.util.Vector;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import orka.model.core.KontrolaException;
import orka.model.core.OrkaClassUtil;
import orka.model.maticni.JedinicaMjere;
import orka.model.maticni.ObradaJedinicaMjereLocal;

@ManagedBean(name = "jedinicaMjereMb")
@SessionScoped
public class JedinicaMjereMb {

	private ObradaJedinicaMjereLocal obrada = (ObradaJedinicaMjereLocal) OrkaClassUtil
			.getSessionBean("ObradaJedinicaMjere", true);

	public ObradaJedinicaMjereLocal getObrada() {
		return obrada;
	}

	public void setObrada(ObradaJedinicaMjereLocal obrada) {
		this.obrada = obrada;
	}

	private JedinicaMjere jedinicaMjere;

	public JedinicaMjere getJedinicaMjere() {
		return jedinicaMjere;
	}

	public void setJedinicaMjere(JedinicaMjere jedinicaMjere) {
		this.jedinicaMjere = jedinicaMjere;
	}

	public void novi() throws KontrolaException {
		jedinicaMjere = obrada.noviEntitet();
	}

	public void spremi() throws KontrolaException {
		obrada.spremi(jedinicaMjere);
	}

	private List<JedinicaMjere> jedinicaMjera = new Vector<JedinicaMjere>();

	public List<JedinicaMjere> getJedinicaMjera() {
		String q = "select ent from JedinicaMjere ent order by ent.naziv";
		List<JedinicaMjere> resultList = obrada.getManager().createQuery(q).getResultList();
		return resultList;
	}

	public void setJedinicaMjera(List<JedinicaMjere> jedinicaMjera) {
		this.jedinicaMjera = jedinicaMjera;
	}
	
	public void obrisi(JedinicaMjere entitet) throws KontrolaException {
		getObrada().obrisi(entitet);
	}
}
