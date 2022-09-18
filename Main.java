
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		// extract parameters
		int n = 5;// Integer.valueOf(args[0]).intValue();
		int d = 3;// Integer.valueOf(args[1]).intValue();
		double p1 = 0.3;// Double.valueOf(args[2]).doubleValue();

		// for p2 in range [0.1,...,0.9]
		for (double p2 = 0.1; p2 <= 0.9; p2 += 0.1) {

			Integer assignmentsSuccess = 0;
			Integer constraintsChecks = 0;
			Integer backtracks = 0;

			// generate CSP
			Generator gen = new Generator(n, d, p1, p2);

			// generate and solve 100 problems
			for (int N = 0; N < 100; N++) {

				CSP csp = gen.generateDCSP();

				// initialize mailer
				Mailer mailer = new Mailer();
				for (int i = 0; i < n; i++) {
					mailer.put(i);
				}

				// create agents
				ArrayList<Thread> threads = new ArrayList<Thread>();
				for (int i = 0; i < n; i++) {
					// use the csp to extract the private information of each agent
					HashMap<Integer, ConsTable> private_information = csp.tablesOf(i);
					Thread t = new Thread(new Agent(i, mailer, private_information, n, d));
					threads.add(t);
				}

				// run agents as threads
				for (Thread t : threads) {
					t.start();
				}

				// wait for all agents to terminate
				for (Thread t : threads) {
					t.join();
				}

				assignmentsSuccess = assignmentsSuccess + mailer.getAssignmentSuccess();
				constraintsChecks = constraintsChecks + mailer.getConstraintChecks();
				backtracks = backtracks + mailer.getBacktrack();
			}

			System.out.println("for p2 = " + p2 + ":");
			// print necessary information
			System.out.println((assignmentsSuccess / 100) + " assignments on average");
			System.out.println((constraintsChecks / 100) + " CCs on average");
			System.out.println((backtracks / 100) + " backtracks on average");
			System.out.println();
		}
	}
}
