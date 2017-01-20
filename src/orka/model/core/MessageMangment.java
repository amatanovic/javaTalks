package orka.model.core;

import java.util.List;

import javax.faces.application.FacesMessage.Severity;

public interface MessageMangment {

	public static final int INFO = 0;

	public static final int WARN = 1;

	public static final int ERROR = 2;

	public static final int FATAL = 3;

	public void sendMessage(String id, Severity level, String poruka,
			Object[] parametri);

	 
	
	
	public List<String> getBlokiranePoruke();

	public void setBlokiranePoruke(List<String> blokiranePoruke);

}
