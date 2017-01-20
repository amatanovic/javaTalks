package orka.view.core;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import orka.model.core.OrkaClassUtil;

public class Util {

	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	public static String getUserName() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		return session.getAttribute("username").toString();
	}

	public static String getUserId() {
		HttpSession session = getSession();
		if (session != null)
			return (String) session.getAttribute("userid");
		else
			return null;
	}

	public static void sendMessage(String id, Severity level, String poruka,
			Object[] parametri) {
	 
		String s = "";
		 

		FacesContext fc = FacesContext.getCurrentInstance();

		if (fc == null) {
			return;
		}

		poruka = OrkaClassUtil.getPorukaFromBundle(poruka, parametri,
				FacesContext.getCurrentInstance().getViewRoot().getLocale());

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(level, poruka, s));
	}
}
