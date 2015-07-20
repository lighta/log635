package log635_lab3;

import java.util.*;


public class main {

	public static void main(String[] args) {
		Lab3FileReader lb3fr = new Lab3FileReader("D:\\Download\\Firefox\\Donnees_sources2.csv");
		
		// Print headers
		for (int i = 0; i < lb3fr.GetHeaders().size(); i++)
		{
			System.out.println(lb3fr.GetHeaders().elementAt(i));
		}
		
		System.out.println("---");
		
		// Print first column
		for (int i = 0; i < lb3fr.GetDataColumn(0).size(); i++)
		{
			System.out.println(lb3fr.GetDataColumn(0).elementAt(i));
		}
		
		System.out.println("---");
		
		// Print second column, even numbers only
		for (int i = 0; i < lb3fr.GetDataColumnEvenRowsOnly(1).size(); i++)
		{
			System.out.println(lb3fr.GetDataColumnEvenRowsOnly(1).elementAt(i));
		}
		
		System.out.println("---");
		
		// Print third column, odd numbers only
		for (int i = 0; i < lb3fr.GetDataColumnOddRowsOnly(2).size(); i++)
		{
			System.out.println(lb3fr.GetDataColumnOddRowsOnly(2).elementAt(i));
		}
	}

}

