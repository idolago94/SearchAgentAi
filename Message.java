import java.util.ArrayList;

/*
 * messages communicate sending messages to each other
 */
public class Message {
    MsgTitle title = MsgTitle.CPA;
    ArrayList<Integer> assignments = new ArrayList<Integer>();

    public void setTitle(MsgTitle str) {
        this.title = str;
    }

    public MsgTitle getTitle() {
        return this.title;
    }

    public void setAgentAssignment(int id, int ass) {
        this.assignments.add(ass);
    }

    public Integer getAgentAssignment(int id) {
        return this.assignments.get(id);
    }

    public void deleteAgentAssignment(int id) {
        this.assignments.remove(this.assignments.size() - 1);
    }

    public void print() {
        System.out.println(this.getTitle() + " assignments=" + this.assignments.toString());
    }
}
