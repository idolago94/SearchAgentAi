public class Reporter {
    private int agentsAmount;
    private int reportsSum = 0;
    private int checksSum = 0;

    public Reporter(int agentsAmount) {
        this.agentsAmount = agentsAmount;
    }

    public void reportCorrectCheck(int checks) {
        this.reportsSum++;
        this.checksSum += checks;
        if (this.reportsSum > this.agentsAmount) {
            System.out.println("not good");
        }
        if (this.reportsSum == this.agentsAmount) {
            this.printSummary();
        }
    }

    private void printSummary() {
        System.out.println("total number of constraint checks: " + this.checksSum);
    }
}
