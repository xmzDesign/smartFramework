package org.smart4j.framework.threadLocal;

public class SequenceC {
	private static MyThreadLocal<Integer> numberContainer=new MyThreadLocal<Integer>(){

		@Override
		protected Integer initailValue() {
			return 0;
		}
		
	};
	public int getNUmber(){
		numberContainer.set(numberContainer.get()+1);
		return numberContainer.get();
	}
	
	public static void main(String[] args) {
		Sequence sequence=new SequeneB();
		ClientThread t1=new ClientThread(sequence);
		ClientThread t2=new ClientThread(sequence);
		ClientThread t3=new ClientThread(sequence);
		t1.start();
		t2.start();
		t3.start();
	}

}
