package orka.model.core;

import javax.ejb.Local;

@Local
public interface ActionLocal {

	public boolean isRollBack();

	public void clearGreske();

}
