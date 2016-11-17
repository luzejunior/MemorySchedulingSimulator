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
		OPT opt = new OPT(array_size, processos);
		LRU lru = new LRU(array_size, processos);

		System.out.println(fifo.toString());
		System.out.println(opt.toString());
		System.out.println(lru.toString());
	}
}

class FIFO{

	private int[] memory;
	ArrayList<Integer> process_queue = new ArrayList<Integer>();
	private int pages_fault = 0;


	public FIFO(int memory_size, ArrayList<Integer> processos){
		memory = new int[memory_size];

		for(int i = 0; i < processos.size(); i++){
			process_queue.add(processos.get(i));
		}
		for(int i = 0; i < memory.length; i++){
			memory[i] = process_queue.get(0);
			process_queue.remove(0);
			pages_fault++;
		}

		Simulate();
	}

	public void Simulate(){
		int i = 0;
		boolean can_run = true;

		while(!process_queue.isEmpty()){
			//System.out.println("Queue: " + Integer.toString(process_queue.get(0)));
			for(int k = 0; k < memory.length; k++){
				if(!process_queue.isEmpty()){
					if(process_queue.get(0) == memory[k]){
						process_queue.remove(0);
						can_run = false;
					}
				}
			}
			if(can_run){
				memory[i] = process_queue.get(0);
				process_queue.remove(0);
				pages_fault++;
				i++;
			}

			i = i % memory.length;
			can_run = true;
			//System.out.println("Pages Fault: " + Integer.toString(pages_fault));
		}
	}

	public String toString(){
		return "FIFO: " + Integer.toString(pages_fault);
	}
}

class OPT{

	private int[] memory;
	ArrayList<Integer> process_queue = new ArrayList<Integer>();
	private int pages_fault = 0;

	public OPT(int memory_size, ArrayList<Integer> processos){	
		memory = new int[memory_size];
		
		for(int i = 0; i < processos.size(); i++){
			process_queue.add(processos.get(i));
		}
		for(int i = 0; i < memory.length; i++){
			memory[i] = process_queue.get(0);
			process_queue.remove(0);
			pages_fault++;
		}
		
		Simulate();
	}

	public void Simulate(){
		int less_number_index = 0;
		boolean can_run = true;

		while(!process_queue.isEmpty()){
			//System.out.println("Queue: " + Integer.toString(process_queue.get(0)));
			//System.out.println("Current Queue: " + Integer.toString(process_queue.get(0)));
			for(int k = 0; k < memory.length; k++){
				if(!process_queue.isEmpty()){
					if(process_queue.get(0) == memory[k]){
						process_queue.remove(0);
						can_run = false;
						break;
					}
				}
			}
			if(can_run){
				less_number_index = searchLessUsed();
				//System.out.println("Less Number Index: " + Integer.toString(less_number_index));
				memory[less_number_index] = process_queue.get(0);
				process_queue.remove(0);
				pages_fault++;
			}
			can_run = true;
			//for(int j = 0; j<memory.length; j++){
			//	System.out.print("[" + Integer.toString(memory[j]) + "]");
			// 	}
			//System.out.println("");
			//System.out.println("Pages Fault: " + Integer.toString(pages_fault));
		}
	}	

	public int searchLessUsed(){
		int less_number_index = 0;
		int less_number_counter = 999;
		int current_number_counter = 0;
		int i = 0;
		
		for(i = 0; i<memory.length; i++){
			for(int j = 0; j<process_queue.size(); j++){
				if(process_queue.get(j) == memory[i]){
					current_number_counter++;
				}
			}
			if(less_number_counter > current_number_counter){
				less_number_counter = current_number_counter;
				less_number_index = i;
			}
			current_number_counter = 0;
		}

		return less_number_index;
	}

	public String toString(){
		return "OTM: " + Integer.toString(pages_fault);
	}

}

class LRU{

	private int[] memory;
	ArrayList<Integer> process_queue = new ArrayList<Integer>();
	ArrayList<Integer> stack = new ArrayList<Integer>();
	private int pages_fault = 0;

	public LRU(int memory_size, ArrayList<Integer> processos){	
		memory = new int[memory_size];
		
		for(int i = 0; i < processos.size(); i++){
			process_queue.add(processos.get(i));
		}
		for(int i = 0; i < memory.length; i++){
			memory[i] = process_queue.get(0);
			process_queue.remove(0);
			stack.add(memory[i]);
			pages_fault++;
		}
		
		Simulate();
	}

	public void Simulate(){
		boolean can_run = true;

		while(!process_queue.isEmpty()){
			for(int i = 0; i<stack.size(); i++){
				//System.out.println("Found the number: " + Integer.toString(stack.get(i)) + " in stack");
				//System.out.println("Im looking for number: " + Integer.toString(process_queue.get(0)) + " in stack");
				if(stack.get(i) == process_queue.get(0)){
					int aux = stack.get(i);
					//System.out.println("Found the number: " + Integer.toString(aux) + " in stack");
					stack.remove(i);
					stack.add(aux);
					process_queue.remove(0);
					//System.out.println("Next in Queue: " + Integer.toString(process_queue.get(0)));
					//for(int j = 0; j<stack.size(); j++){
					//	System.out.print("[" + Integer.toString(stack.get(j)) + "]");
					//}
					//System.out.println("");
					can_run = false;
					break;
				}
			}
			if(can_run){
				stack.add(process_queue.get(0));
				for(int k = 0; k<memory.length; k++){
					if(stack.get(0) == memory[k]){
						//System.out.println("The Number: " + Integer.toString(process_queue.get(0)) + " Will enter in the place index: " + Integer.toString(stack.get(0)));
						stack.remove(0);
						memory[k] = process_queue.get(0);
						process_queue.remove(0);
						pages_fault++;

						//if(!process_queue.isEmpty())
						//	System.out.println("Next in Queue: " + Integer.toString(process_queue.get(0)));
						//for(int j = 0; j<stack.size(); j++){
						//	System.out.print("[" + Integer.toString(stack.get(j)) + "]");
						//}
						//System.out.println("");
						break;
					}
				}
			}
			can_run = true;
		}
	}

	public String toString(){
		return "LRU: " + Integer.toString(pages_fault);
	}
}