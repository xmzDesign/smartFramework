package org.smart4j.framework.threadLocal;

public class SequeneB implements Sequence{
	private static ThreadLocal<Integer> numberContain=new ThreadLocal<Integer>(){

		@Override
		protected Integer initialValue() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	};
	public int getNum() {
		numberContain.set(numberContain.get()+1);
		return numberContain.get();
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
