
import java.util.HashMap;
import java.util.Random;

public class Agent implements Runnable {

	private int id;
	private Mailer mailer;
	private HashMap<VarTuple, ConsTable> constraints;
	private int domain;
	
	/*
	 * constructor parameters -
	 * agent's id
	 * a reference to mailer
	 * a reference to csp
	 */
	public Agent(int id, Mailer mailer, HashMap<VarTuple, ConsTable> constraints, int domain) {
		this.id = id;
		this.mailer = mailer;
		this.constraints = constraints;
		this.domain = domain;
	}

	private int getRandomAssinment() {
        Random rnd = new Random();
        int assignment = rnd.nextInt(this.domain);
		return assignment;
	}

	private void sendAssignmentToNeighbors() {
		int assignment = getRandomAssinment();
		constraints.forEach((key, value) -> {
			this.mailer.send(key.getJ(), new Message(key.getI(), assignment));
		});
	}

	@Override
	public void run() {
		sendAssignmentToNeighbors();
		
	}

}
