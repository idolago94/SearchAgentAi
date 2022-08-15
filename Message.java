import java.util.ArrayList;

/*
 * messages communicate sending messages to each other
 */
public class Message {
	int senderId;
	int assignment;
	
	// a message should include information.
	// you are required to add corresponding fields and constructor parameters
	// in order to pass on that information
	public Message(int senderId, int assignment) {
		this.senderId = senderId;
		this.assignment = assignment;
	}

	public int getSenderId() {
		return this.senderId;
	}

	public int getAssinment() {
		return this.assignment;
	}
}
