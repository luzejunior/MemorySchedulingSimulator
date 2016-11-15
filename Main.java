import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		
		ArrayList<Integer> processos = new ArrayList<Integer>();
		int array_size;
		
		Scanner s = new Scanner(System.in);
		array_size = s.nextInt();
		while (s.hasNextInt()) {
			processos.add(s.nextInt());
		}

		FIFO fifo = new FIFO(array_size, processos);

		System.out.println(fifo.toString());
	}
}

class FIFO{

	private int[] stack;
	ArrayList<Integer> process_queue = new ArrayList<Integer>();
	private int pages_fault = 0;


	public FIFO(int stack_size, ArrayList<Integer> processos){
		stack = new int[stack_size];
		for(int i = 0; i < processos.size(); i++){
			process_queue.add(processos.get(i));
		}
		for(int i = 0; i < stack.length; i++){
			stack[i] = process_queue.get(0);
			process_queue.remove(0);
			pages_fault++;
		}
		Simulate();
	}

	public void Simulate(){
		int i = 0;
		boolean can_run = true;
		while(!process_queue.isEmpty()){
			System.out.println("Queue: " + Integer.toString(process_queue.get(0)));
			for(int k = 0; k < stack.length; k++){
				if(!process_queue.isEmpty()){
					if(process_queue.get(0) == stack[k]){
						process_queue.remove(0);
						can_run = false;
					}
				}
			}
			if(can_run){
				stack[i] = process_queue.get(0);
				process_queue.remove(0);
				pages_fault++;
				i++;
			}
			i = i % stack.length;
			can_run = true;
			System.out.println("Pages Fault: " + Integer.toString(pages_fault));
		}
	}

	public String toString(){
		return "FIFO: " + Integer.toString(pages_fault);
	}
}

class OTM{

}

class LRU{

}