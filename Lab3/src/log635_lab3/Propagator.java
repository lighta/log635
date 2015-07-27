package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class Propagator extends Thread {
	private PipedWriter[] dup;
	private PipedReader pr;
	private final int GUI;
	private boolean running;

	/**
	 * Constructor
	 * @param GUI : Unique identifier
	 * @param pw : input pipe
	 * @param dup : pipes to duplicate into
	 * @throws IOException
	 */
	public Propagator(final int GUI,final PipedWriter pw,final PipedWriter[] dup) throws IOException {
		super();
		
		System.out.println("Propagator["+GUI+"] init");
		this.GUI = GUI;
		this.dup = dup;
		pr = new PipedReader();
		pr.connect(pw);
	}
	
	/**
	 * Disconnect our pipe
	 * (ensure they all close properly)
	 */
	private void disconnect(){
		try {
			if(pr != null)
				pr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		try {
			for(int i=0; i<dup.length; i++){
				if(dup[i] != null)
					dup[i].close();
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	@Override
	public void interrupt() {
		super.interrupt();
		running = false;
		disconnect(); //hard disconection
	}
	
	/**
	 * Handler, get data then push them into duplicate pipe
	 * @TODO use parallel loop instead
	 */
	@Override
	public void run() {
		super.run();
		int c=0;
		
		System.out.println("Propagator["+this.GUI+"] started");
		try {
			running = true;
			while(running == true){ //till close pipe
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
