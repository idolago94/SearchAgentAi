
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Agent implements Runnable {

	private int id;
	private int domainSize, agents;
	private int assignment;
	private Mailer mailer;
	private HashMap<Integer, ConsTable> constraints;

	private ArrayList<Integer> domain;
	private ArrayList<Integer> current_domain;

	/*
	 * constructor parameters -
	 * agent's id
	 * a reference to mailer
	 * a reference to csp
	 */
	public Agent(int id, Mailer mailer, HashMap<Integer, ConsTable> constraints, int n, int d) {
		this.id = id;
		this.mailer = mailer;
		this.constraints = constraints;
		this.domainSize = d;
		this.agents = n;
		this.resetCurrentDomain();
	}

	private void resetCurrentDomain() {
		this.current_domain = new ArrayList<Integer>();
		for (int i = 0; i < domainSize; i++) {
			this.current_domain.add(i);
		}
	}

	private int getRandomAssignment() {
		Random rnd = new Random();
		if (this.current_domain.size() == 1) {
			return this.current_domain.get(0);
		}
		int index = rnd.nextInt(this.current_domain.size() - 1);
		int assignment = this.current_domain.get(index);
		return assignment;
	}

	private void checkConstraint(Message msg) {
		boolean flag = true;
		int myAssignment = this.getRandomAssignment();

		for (Entry<Integer, ConsTable> entry : this.constraints.entrySet()) {
			if (entry.getKey() < this.id) {
				this.mailer.incConstraintChecks();
				Integer sender_assignment = msg.getAgentAssignment(entry.getKey());
				if (!entry.getValue().check(sender_assignment, myAssignment)) { // the constraint is NOT OK
					flag = false;
					break;
				}
			}
		}

		if (flag) { // all the constraints are OK
			this.mailer.incAssignmentSuccess();
			if (this.id == this.agents - 1) {
				msg.setAgentAssignment(this.id, myAssignment);
				this.sendSolution(msg);
			} else {
				this.sendAssignment(msg, myAssignment);
			}
		} else {
			ArrayList<Integer> arr = new ArrayList<Integer>();
			this.current_domain.forEach(n -> {
				if (n != myAssignment)
					arr.add(n);
			});
			this.current_domain = arr;
			if (this.current_domain.size() > 0) { // check other assignment
				this.checkConstraint(msg);
			} else { // send backtrack message to prev agent
				if (this.id == 0) {
					this.sendNoSolution(msg);
				} else {
					this.mailer.incBacktrack();
					this.sendBacktrack(msg);
				}
			}
		}
	}

	private void sendBacktrack(Message msg) {
		this.resetCurrentDomain();
		msg.setTitle(MsgTitle.BACKTRACK);
		this.mailer.send(this.id - 1, msg);
	}

	private void sendAssignment(Message msg, int assignment) {
		this.assignment = assignment;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		this.current_domain.forEach(n -> {
			if (n != assignment)
				arr.add(n);
		});
		this.current_domain = arr;
		msg.setTitle(MsgTitle.CPA);
		msg.setAgentAssignment(this.id, assignment);
		this.mailer.send(this.id + 1, msg);
	}

	private void sendSolution(Message msg) {
		msg.setTitle(MsgTitle.SOLUTION);
		for (int i = 0; i < this.agents; i++) {
			this.mailer.send(i, msg);
		}
	}

	private void sendNoSolution(Message msg) {
		msg.setTitle(MsgTitle.NO_SOLUTION);
		for (int i = 0; i < this.agents; i++) {
			this.mailer.send(i, msg);
		}
	}

	@Override
	public void run() {
		if (this.id == 0) { // first agent chose assignment and send to second agent
			Message msg = new Message();
			this.sendAssignment(msg, getRandomAssignment());
		}

		boolean flag = true;
		while (flag) {
			Message msg = this.mailer.readOne(this.id);
			if (msg != null) {
				switch (msg.getTitle()) {
					case CPA:
						this.checkConstraint(msg);
						break;
					case BACKTRACK:
						msg.deleteAgentAssignment(this.id);
						if (this.current_domain.size() == 0) {
							if (this.id == 0) {
								this.sendNoSolution(msg);
								flag = false;
							} else {
								this.sendBacktrack(msg);
							}
						} else {
							this.checkConstraint(msg);
						}
						break;
					case SOLUTION:
					case NO_SOLUTION:
						flag = false;
						break;
					default:
						break;
				}
			}
		}
	}
}
