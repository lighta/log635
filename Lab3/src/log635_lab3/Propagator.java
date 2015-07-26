package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class Propagator extends Thread {
	private PipedWriter[] dup;
	private PipedReader pr;
	private final int GUI;

	
	public Propagator(final int GUI,PipedWriter pw,PipedWriter[] dup) throws IOException {
		super();
		
		System.out.println("Propagator["+GUI+"] init");
		this.GUI = GUI;
		this.dup = dup;
		pr = new PipedReader();
		pr.connect(pw);
	}
	
	private void disconnect(){
		try {
			pr.close();
			for(int i=0; i<dup.length; i++)
				dup[i].close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		super.run();
		int c=0;
		
		System.out.println("Propagator["+this.GUI+"] started");
		try {
			while(true){ //till close pipe
				//System.out.println("Propagator["+this.GUI+"] waiting");
				c = pr.read();
				if(c==-1)
					break;
				//System.out.println("Propagating["+GUI+"] c="+c+" ch"+((char)c) );
				for(int i=0; i<dup.length; i++){
					dup[i].write(c);
					dup[i].flush();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
	}
}
