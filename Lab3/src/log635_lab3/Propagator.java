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
	
	@Override
	public void run() {
		super.run();
		int c=0;
		
		System.out.println("Propagator["+this.GUI+"] started");
		try {
			while(c!=-1){ //till close pipe
				//System.out.println("Propagator["+this.GUI+"] waiting");
				c = pr.read();
				System.out.println("Propagating["+GUI+"] c="+c);
				for(int i=0; i<dup.length; i++){
					dup[i].write(c);
					dup[i].flush();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			pr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
