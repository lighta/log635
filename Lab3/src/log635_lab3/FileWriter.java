package log635_lab3;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class FileWriter {
	
	public static boolean write(Schema sch)
	{
		ObjectOutputStream oos = null;
		try
		{
			FileOutputStream fichier = new FileOutputStream("data/Layer-Percept-Weight.txt");
			StringBuilder _sb = new StringBuilder();
			
			oos = new ObjectOutputStream(fichier);
			for(int i = 0; i < sch.getSchema().size() -1; i++)
			{
				for(int k = 0; k < sch.getSchema().get(i).size()-1; k++)
				{
					//Perceptron p = sch.getSchema().get(i).get(k);
					double[] inputsWeights = sch.getSchema().get(i).get(k).getInputsWeights();
					for(int w=0; w < inputsWeights.length-1; w++)
					{
						_sb.append("[" + i + "]" + "[" + k + "]" + "[" + inputsWeights[w] + "]"+ "\n");
				//		oos.writeBytes("[" + i + "]" + "[" + k + "]" + "[" + inputsWeights[w] + "]"+ "\n");
					}
					_sb.append("[" + i + "]" + "[" + k + "]" + "[" + sch.getSchema().get(i).get(k).getBias() + "]"+ "\n");
					_sb.append("[" + i + "]" + "[" + k + "]" + "[" + sch.getSchema().get(i).get(k).getBiasWeights() + "]"+ "\n");
				//	oos.writeBytes("[" + i + "]" + "[" + k + "]" + "[" + sch.getSchema().get(i).get(k).getBias() + "]"+ "\n");
				//	oos.writeBytes("[" + i + "]" + "[" + k + "]" + "[" + sch.getSchema().get(i).get(k).getBiasWeights() + "]"+ "\n");
				}
			}
			System.out.println(_sb.toString());
			oos.writeUTF(_sb.toString());
				
		} 
		catch (final java.io.IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				if (oos != null) 
				{
					oos.flush();
					oos.close();
				}
		} 
			catch (final IOException ex) 
			{
				ex.printStackTrace();
			}
		}
		return false;
	}
	
/*	public static double[][][] load()
	{
		ObjectOutputStream oos = null;
		try
		{
			FileOutputStream fichier = new FileOutputStream("data/Layer-Percept-Weight.txt");
			StringBuilder _sb = new StringBuilder();
			
			oos = new ObjectOutputStream(fichier);
			for(int i = 0; i < sch.getSchema().size() -1; i++)
			{
				for(int k = 0; k < sch.getSchema().get(i).size()-1; k++)
				{
					//Perceptron p = sch.getSchema().get(i).get(k);
					double[] inputsWeights = sch.getSchema().get(i).get(k).getInputsWeights();
					for(int w=0; w < inputsWeights.length-1; w++)
					{
						_sb.append("[" + i + "]" + "[" + k + "]" + "[" + inputsWeights[w] + "]"+ "\n");
				//		oos.writeBytes("[" + i + "]" + "[" + k + "]" + "[" + inputsWeights[w] + "]"+ "\n");
					}
					_sb.append("[" + i + "]" + "[" + k + "]" + "[" + sch.getSchema().get(i).get(k).getBias() + "]"+ "\n");
					_sb.append("[" + i + "]" + "[" + k + "]" + "[" + sch.getSchema().get(i).get(k).getBiasWeights() + "]"+ "\n");
				//	oos.writeBytes("[" + i + "]" + "[" + k + "]" + "[" + sch.getSchema().get(i).get(k).getBias() + "]"+ "\n");
				//	oos.writeBytes("[" + i + "]" + "[" + k + "]" + "[" + sch.getSchema().get(i).get(k).getBiasWeights() + "]"+ "\n");
				}
			}
			System.out.println(_sb.toString());
			oos.writeUTF(_sb.toString());
				
		} 
		catch (final java.io.IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				if (oos != null) 
				{
					oos.flush();
					oos.close();
				}
		} 
			catch (final IOException ex) 
			{
				ex.printStackTrace();
			}
		}
		return false;
	}*/
	
	
}
