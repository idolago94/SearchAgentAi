
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * used for communication among agents
 */
public class Mailer {
	Integer success_ass = 0;
	Integer constraints_checks = 0;
	Integer backracks = 0;

	// maps between agents and their mailboxes
	private HashMap<Integer, List<Message>> map = new HashMap<>();

	// send message @m to agent @receiver
	public void send(int receiver, Message m) {

		List<Message> l = map.get(receiver);

		synchronized (l) {
			l.add(m);
		}
	}

	// agent @receiver reads the first message from its mail box
	public Message readOne(int receiver) {

		List<Message> l = map.get(receiver);
		if (l.isEmpty()) {
			return null;
		}

		synchronized (l) {
			Message m = l.get(0);
			l.remove(0);
			return m;
		}
	}

	// only used for initialization
	public void put(int i) {
		List<Message> l = new ArrayList<Message>();
		this.map.put(i, l);
	}

	public void incAssignmentSuccess() {
		this.success_ass = this.success_ass + 1;
	}

	public void incConstraintChecks() {
		this.constraints_checks = this.constraints_checks + 1;
	}

	public void incBacktrack() {
		this.backracks = this.backracks + 1;
	}

	public Integer getAssignmentSuccess() {
		return this.success_ass;
	}

	public Integer getConstraintChecks() {
		return this.constraints_checks;
	}

	public Integer getBacktrack() {
		return this.backracks;
	}
}
