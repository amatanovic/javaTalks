package orka.model.core;

import java.util.List;
import java.util.Vector;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class KontrolaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Message> messages = new Vector<Message>();

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public KontrolaException(String message, Throwable cause) {
		super(message, cause);
	}

	public KontrolaException(String message) {
		super(message);
	}

	public KontrolaException() {
		super();
	}

	public KontrolaException(List<Message> messages) {
		super();
		this.messages = messages;
	}

	public KontrolaException(Message message) {
		super();
		this.messages.add(message);
	}

	@Override
	public String getMessage() {
		if (messages == null || messages.size() == 0) {
			return super.getMessage();
		}
		return messages.get(0).getPoruka();
	}

}
