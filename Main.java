/*

Author: Luzenildo de Sousa Batista Junior
E-Mail: luzejunior94@gmail.com

License:

MIT License

Copyright (c) 2016 Luzenildo de Sousa Batista Junior

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/

//Imports.
import java.util.ArrayList;
import java.util.Scanner;

//Main class.
public class Main {
	
	public static void main(String[] args) {
		
		//Initial variables
		ArrayList<Integer> pagesos = new ArrayList<Integer>(); //Create pages arraylist to handle text input.
		int array_size; //Int variable to determine memory stack size.
		
		//Input Reader
		Scanner s = new Scanner(System.in); //Scanner will read input file.
		array_size = s.nextInt(); //The fist integer found will be the memory stack size.
		while (s.hasNextInt()) {
			pagesos.add(s.nextInt()); //Add pageses to pages queue.
		} //End of while.

		//Classes call
		FIFO fifo = new FIFO(array_size, pagesos); //Call First In First Out class constructor passing the memory stack size and pages queue.
		OPT opt = new OPT(array_size, pagesos); //Call Optimal class constructor passing the memory stack size and pages queue.
		LRU lru = new LRU(array_size, pagesos); //Call Last Recent Used class constructor passing the memory stack size and pages queue.

		//Final result printout
		System.out.println(fifo.toString()); //Print the number of pages fault from First In First Out algorithm
		System.out.println(opt.toString()); //Print the number of pages fault from Optimal algorithm
		System.out.println(lru.toString()); //Print the number of pages fault from Last Recent Used algorithm
	} //End of main.
} //End of class.

/*
// Fist in First out algorithm.
// In this algorithm, the first page to enter in memory stack will be replaced first than the last one.
// ""
// First in, first out (FIFO), also known as first come, first served (FCFS), is the simplest scheduling algorithm. 
// FIFO simply queues pageses in the order that they arrive in the ready queue.
// @Wikipedia
// ""
*/
class FIFO{

	private int[] memory; //Array to handle memory stack.
	ArrayList<Integer> pages_queue = new ArrayList<Integer>(); //List of queue pageses.
	private int pages_fault = 0; //Number of pages fault counter.

	//Class Constructor
	// Int memory_size : handle the size of memory stack.
	// ArrayList pagesos : pages list from user input.
	public FIFO(int memory_size, ArrayList<Integer> pagesos){
		memory = new int[memory_size]; //Create memory stack of memory_size lenght.
		boolean can_run = true; //Boolean to check if the page are'nt in memory stack. If this flag is true, the page will be taken off the queue and putted in memory.

		//For every pages in pagesos ArrayList, add to pages_queue.
		for(int i = 0; i < pagesos.size(); i++){
			pages_queue.add(pagesos.get(i));
		} //End of for.
		//If memory_size is empty, add new proccess from pages_queue,
		for(int k = 0; k < memory_size; k++){
			//Check if page on pages_queue are already on memory stack.
			for(int i = 0; i < memory.length; i++){
				//If page are on memory stack, remove from pages_queue and set flag to false.
				if(memory[i] == pages_queue.get(0)){
					pages_queue.remove(0);
					can_run = false;
				} //End of if.
			} //End of for.
			//If pages are not in memory stack:
			// Add the first page on pages_queue on memory stack.
			// Remove the first page from pages_queue.
			// Increase pages_fault counter.
			if(can_run){
				memory[k] = pages_queue.get(0);
				pages_queue.remove(0);
				pages_fault++;
			} //End of if.
			//Set can_run to true for next loop.
			can_run = true;
		} //Enf of for.
		//Call simulate method.
		Simulate();
	} // End of constructor.

	//Simulate Method
	// Simulate the memory stack for FIFO algorithm.
	public void Simulate(){
		int i = 0; //Variable to handle stack position.
		boolean can_run = true; //Boolean to check if the page aren't in memory stack. If this flag is true, the page will be taken off the queue and putted in memory.

		//While pages_queue isn't empty:
		while(!pages_queue.isEmpty()){
			//System.out.println("Queue: " + Integer.toString(pages_queue.get(0)));
			for(int k = 0; k < memory.length; k++){
				if(!pages_queue.isEmpty()){
					// If page is in memory,
					if(pages_queue.get(0) == memory[k]){
						pages_queue.remove(0); //remove from pages_queue,
						can_run = false; //and continue to next loop.
					} //End of if.
				} //End of if.
			} //End of for.
			// If page isn't in memory, can_run equals to true,
			if(can_run){
				memory[i] = pages_queue.get(0); //memory index i will be replaced by the first page on pages_queue,
				pages_queue.remove(0); //Remove page from pages_queue,
				pages_fault++; //increment pages_fault,
				i++; //increment index i.
			} //End of if.

			i = i % memory.length; //get next array index.
			can_run = true; //Set can run to true for next loop.5
			//System.out.println("Pages Fault: " + Integer.toString(pages_fault));
		} //End of while.
	} //End of Simulate Method.

	//ToString method
	public String toString(){
		return "FIFO: " + Integer.toString(pages_fault);
	} //End of toString method.
} //End of class.

/*
// Optimal algorithm.
// ""
// Optimal is an algorithm that works as follows: when a page needs to be swapped in, 
// the operating system swaps out the page whose next use will occur farthest in the future.
// @Wikipedia.
// ""
*/
class OPT{

	private int[] memory; //Array to handle memory stack
	ArrayList<Integer> pages_queue = new ArrayList<Integer>(); //List of queued pageses.
	private int pages_fault = 0; //Number of pages fault counter.

	//Class Constructor
	// Int memory_size : handle the size of memory stack.
	// ArrayList pagesos : pages list from user input.
	public OPT(int memory_size, ArrayList<Integer> pagesos){	
		memory = new int[memory_size]; //Create memory stack of memory_size lenght.
		boolean can_run = true; //Boolean to check if the page are'nt in memory stack. If this flag is true, the page will be taken off the queue and putted in memory.
		
		//For every pages in pagesos ArrayList.
		for(int i = 0; i < pagesos.size(); i++){
			pages_queue.add(pagesos.get(i)); //Add to pages_queue.
		} //End of for.
		for(int k = 0; k < memory_size; k++){
			//If memory_size is empty, add new page from pages_queue,
			for(int i = 0; i < memory.length; i++){
				//Check if page on pages_queue are already on memory stack.
				if(memory[i] == pages_queue.get(0)){
					pages_queue.remove(0); //Remove page from pages_queue.
					can_run = false; //Set flag to false and continue to next loop.
				} //End of if.
			} //End of for.
			//If pages aren't on memory stack,
			if(can_run){
				memory[k] = pages_queue.get(0); //memory index k equals to first page on pages queue,
				pages_queue.remove(0); //Remove the first page on pages queue,
				pages_fault++; //Increment pages_faut counter.
			} //End of if.
			can_run = true; //Set can_run flag to true for next loop.
		} //End of for.
		//Call Simulate method.
		Simulate();
	} //End of Constructor.

	//Simulate Method
	// Simulate the memory stack for Optimal algorithm.
	public void Simulate(){
		int less_number_index = 0; //Int to handle the last number index.
		boolean can_run = true; //Boolean to check if the page are'nt in memory stack. If this flag is true, the page will be taken off the queue and putted in memory.

		//while pages_queue isn't empty
		while(!pages_queue.isEmpty()){

			// For Debug Uncomment this block:
			/*
			System.out.println("Queue: " + Integer.toString(pages_queue.get(0)));
			System.out.println("Current Queue: " + Integer.toString(pages_queue.get(0)));
			*/

			//For every page on memory.
			for(int k = 0; k < memory.length; k++){
				if(!pages_queue.isEmpty()){
					//If the first page on pages_queue are on memory,
					if(pages_queue.get(0) == memory[k]){
						pages_queue.remove(0); //Remove the page from queue,
						can_run = false; //set can_run flag to false,
						break; //Go to next loop.
					} //End of if.
				} //End of if.
			} //End of for.
			//If the page isn't in memory,
			if(can_run){
				less_number_index = searchLessUsed(); //Call searchLessUsed method to get less used number index.
				//System.out.println("Less Number Index: " + Integer.toString(less_number_index));
				memory[less_number_index] = pages_queue.get(0); //Memory index less used number will be switched by first page on pages_queue.
				pages_queue.remove(0); //Remove the first page from pages_queue.
				pages_fault++; //Increment pages_fault counter.
			} //End of if.
			can_run = true; //Set can_run flag to true for next loop.

			// For Debug Uncomment this block:
			/*
			for(int j = 0; j<memory.length; j++){
				System.out.print("[" + Integer.toString(memory[j]) + "]");
			}
			System.out.println("");
			System.out.println("Pages Fault: " + Integer.toString(pages_fault));
			*/
		} //End of while.
	} //End of Simulate method.

	//searchLessUsed method.
	// Method to search in pages_queue the less used page to be switched in memory.
	public int searchLessUsed(){
		int less_number_index = 0; //Variable to handle less number index.
		int less_number_counter = 999; //Variable to handle the less used page number counter.
		int current_number_counter = 0; //Variable to handle the current page counter.
		int i = 0; //Variable to handle index.
		
		//For every page on memory,
		for(i = 0; i<memory.length; i++){
			//For every page on queue,
			for(int j = 0; j<pages_queue.size(); j++){
				//If the page on queue are in memory,
				if(pages_queue.get(j) == memory[i]){
					//Increment the current page counter.
					current_number_counter++;
				} //End of if.
			} //End of for.
			//If the counter of less page is larger than current page counter,
			if(less_number_counter > current_number_counter){
				less_number_counter = current_number_counter; //Less page counter equals to current page counter,
				less_number_index = i; //Less page index equals to i.
			} //End of if.
			//Or if counter of less page is equal the current page counter,
			else if(less_number_counter == current_number_counter)
				less_number_index = i; //Less page index equals to i.
			current_number_counter = 0; //Set current number to zero before next loop.
		} //End of for.

		return less_number_index; //Return less page index.
	} //End of searchLessUsed method.

	//ToString method.
	public String toString(){
		return "OTM: " + Integer.toString(pages_fault);
	} //End of toString method.
} //End of class. 

/*
// Last Recent Used Algorithm
// ""
// The Last recently used (LRU) page replacement algorithm is an algorithm that keeps track of page usage over a short period of time. 
// This algorithm works on the idea that pages that have been most heavily used in the past few instructions are most likely to be used heavily in the next few instructions too.
// @Wikipedia.
// ""
*/
class LRU{

	private int[] memory; //Array to handle memory stack
	ArrayList<Integer> pages_queue = new ArrayList<Integer>(); //List of queued pages.
	ArrayList<Integer> stack = new ArrayList<Integer>(); //List of pages tracked by algorithm
	private int pages_fault = 0; //Number of pages fault.

	//Class Constructor
	// Int memory_size : handle the size of memory stack.
	// ArrayList pagesos : pages list from user input.
	public LRU(int memory_size, ArrayList<Integer> pagesos){	
		memory = new int[memory_size]; //Create memory stack of memory_size lenght.
		boolean can_run = true; //Boolean to check if the page are'nt in memory stack. If this flag is true, the page will be taken off the queue and added in memory.
		
		//For every pages in pagesos ArrayList.
		for(int i = 0; i < pagesos.size(); i++){
			pages_queue.add(pagesos.get(i)); //Add to pages_queue.
		} //End of for.

		//For every page in memory
		for(int k = 0; k < memory_size; k++){
			for(int i = 0; i < memory.length; i++){
				//If page_queue first page can be found in memory,
				if(memory[i] == pages_queue.get(0)){
					pages_queue.remove(0); //remove page from queue,
					can_run = false; //set can_run flag to false.
				} //End of if.
			} //End of for.
			//If can_run is true,
			if(can_run){
				memory[k] = pages_queue.get(0); //Put the first queued page on memory,
				pages_queue.remove(0); //Remove page from queue,
				stack.add(memory[k]); //Add page on the secondary list,
				pages_fault++; //increment number of pages fault.
			} //End of if.
			can_run = true; //Set variable can run to next loop.
		} //End of for.
		//Call Simulate method.
		Simulate();
	} //End of Constructor.

	//Simulate Method
	// Simulate the memory stack for Last Recent Used Algorithm.
	public void Simulate(){
		boolean can_run = true; //Boolean to check if the page are'nt in memory stack. If this flag is true, the page will be taken off the queue and added in memory.

		//While pages_queue isn't empty,
		while(!pages_queue.isEmpty()){
			//for every page in stack,
			for(int i = 0; i<stack.size(); i++){

				// For Debug Uncomment this block:
				/*
				System.out.println("Found the number: " + Integer.toString(stack.get(i)) + " in stack");
				System.out.println("Im looking for number: " + Integer.toString(pages_queue.get(0)) + " in stack");
				*/

				//if the page in stack equals to page in queue,
				if(stack.get(i) == pages_queue.get(0)){
					int aux = stack.get(i); //auxiliary variable to handle page position change on stack.

					// For Debug Uncomment this block:
					/*
					System.out.println("Found the number: " + Integer.toString(aux) + " in stack");
					*/

					stack.remove(i); //remove the page from stack,
					stack.add(aux); //and add it to the top of stack,
					pages_queue.remove(0); //remove page from pages_queue.
				
					// For Debug Uncomment this block:
					/*
					System.out.println("Next in Queue: " + Integer.toString(pages_queue.get(0)));
					for(int j = 0; j<stack.size(); j++){
						System.out.print("[" + Integer.toString(stack.get(j)) + "]");
					}
					System.out.println("");
					*/

					can_run = false; //Can_run equals to false
					break; //move to next loop.
				} //Enf of if.
			} //Enf of for.
			//If can_run equals to true or the algorithm can run,
			if(can_run){
				stack.add(pages_queue.get(0)); //Add the first queued page on stack.
				//For every page on memory,
				for(int k = 0; k<memory.length; k++){
					//If the first page on stack equals to any page in memory,
					if(stack.get(0) == memory[k]){

						// For Debug Uncomment this block:
						/*
						System.out.println("The Number: " + Integer.toString(pages_queue.get(0)) + " Will enter in the place index: " + Integer.toString(stack.get(0)));
						*/

						stack.remove(0); //Remove the first page from stack,
						memory[k] = pages_queue.get(0); //memory index k will be replaced by first queued page,
						pages_queue.remove(0); //Remove the first page queued,
						pages_fault++; //increment number of pages fault.

						// For Debug Uncomment this block:
						/*
						if(!pages_queue.isEmpty())
							System.out.println("Next in Queue: " + Integer.toString(pages_queue.get(0)));
						for(int j = 0; j<stack.size(); j++){
							System.out.print("[" + Integer.toString(stack.get(j)) + "]");
						}
						System.out.println("");
						*/

						break; //move to next loop.
					} //End of if.
				} //Enf of for.
			} //End of if.
			can_run = true; //set can_run flag to true.
		} //End of While
	} //End of Simulate method.

	//ToString method.
	public String toString(){
		return "LRU: " + Integer.toString(pages_fault);
	} //End of toString method.
} //End of class.