package log635_lab3;


public class Main {
	public static void main(String[] args) {
		Network neuroneNetwork = new Network();
		Lab3FileReader lb3fr = new Lab3FileReader("data/Donnees_sources.csv", neuroneNetwork.getValidationLineRate());
		//lb3fr.printAll();
		boolean res = neuroneNetwork.learn(lb3fr,1000);

		System.out.println("Finish learning, res="+res);
		neuroneNetwork.report();
	}
}

