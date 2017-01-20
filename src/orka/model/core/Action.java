package orka.model.core;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import javax.faces.application.FacesMessage.Severity;

import org.apache.log4j.Logger;

import orka.view.core.Util;

;

/**
 * Session Bean implementation class Action Osnova za svaki statefull bean
 */
public abstract class Action implements ActionLocal, MessageMangment,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> blokiranePoruke = new Vector<String>();

	public List<String> getBlokiranePoruke() {
		return blokiranePoruke;
	}

	public void setBlokiranePoruke(List<String> blokiranePoruke) {
		this.blokiranePoruke = blokiranePoruke;
	}

	private static final Logger log = Logger.getLogger(Action.class);

	protected int maxMessageLevel = 0;

	public void sendMessage(String id, Severity level, String poruka,
			Object[] parametri) {

		Util.sendMessage(id, level, poruka, parametri);

	}

	public boolean isRollBack() {

		if (maxMessageLevel >= 2) {
			return true;
		}

		return false;
	}

	public void clearGreske() {

		maxMessageLevel = 0;

	}

}
