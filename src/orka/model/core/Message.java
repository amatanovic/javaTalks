package orka.model.core;

import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Message(String id, int level, String poruka, Object[] params) {
		this.level = level;
		this.id = id;
		this.parametars = params;
		this.poruka = poruka;
	}

	private int level;

	private String id;

	private String poruka;

	private Object[] parametars;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPoruka() {
		return poruka;
	}

	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}

	public Object[] getParametars() {
		return parametars;
	}

	public void setParametars(Object[] parametars) {
		this.parametars = parametars;
	}

}
