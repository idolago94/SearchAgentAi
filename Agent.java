
import java.util.HashMap;

public class Agent implements Runnable {

	private int id;
	private Mailer mailer;
	private HashMap<VarTuple, ConsTable> constraints;
	
	/*
	 * constructor parameters -
	 * agent's id
	 * a reference to mailer
	 * a reference to csp
	 */
	public Agent(int id, Mailer mailer, HashMap<VarTuple, ConsTable> constraints) {
		this.id = id;
		this.mailer = mailer;
		this.constraints = constraints;
	}

	@Override
	public void run() {
		// CODE...
		// CODE...
		// CODE...
	}

}
