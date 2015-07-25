package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class Propagator extends Thread {
	private PipedWriter[] dup;
	private PipedReader pr;

	
	public Propagator(PipedWriter pw,PipedWriter[] dup) throws IOException {
		super();
		this.dup = dup;
		pr = new PipedReader();
		pr.connect(pw);
	}
	
	@Override
	public void run() {
		super.run();
		int c=0;
		
		try {
			while(c!=-1){ //till close pipe
				c = pr.read();
				System.out.println("Propagating c="+c);
				for(PipedWriter pw : dup){
					pw.write(c);
					pw.notify();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
