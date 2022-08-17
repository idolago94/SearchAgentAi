
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public class Agent implements Runnable {

	private int id;
	private Mailer mailer;
	private HashMap<VarTuple, ConsTable> constraints;
	private int domain;
	private int assignment;
	private int checkSum = 0;
	
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
		this.assignment = getRandomAssinment();
		constraints.forEach((key, value) -> {
			this.mailer.send(key.getJ(), new Message(key.getI(), assignment));
		});
	}

	private void checkConstraint(Message msg) {
		int fromAgent = msg.getSenderId();
		int msgAssignment = msg.getAssinment();
		for (Entry<VarTuple, ConsTable> entry : this.constraints.entrySet()) {
			if (entry.getKey().getI() == fromAgent && entry.getKey().getJ() == this.id) {
				if (entry.getValue().getPosition(msgAssignment, this.assignment)){
					this.checkSum++;
				}
			}
		}
	}

	@Override
	public void run() {
		int msgRead = 0;
		sendAssignmentToNeighbors();
		while (true) {
			Message msg = this.mailer.readOne(this.id);
			if (msg != null) {
				checkConstraint(msg);
				msgRead++;
			}
			if (constraints.size() == msgRead) break;
		}

		System.out.println("ID: "+this.id+", assignment: "+this.assignment+", successful constraints checks: "+this.checkSum);
	}

}
