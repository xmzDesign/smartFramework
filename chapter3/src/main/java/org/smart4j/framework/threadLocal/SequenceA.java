package org.smart4j.framework.threadLocal;

public class SequenceA implements Sequence{
	private static int number=0;

	public int getNum() {
		number=number+1;
		return number;
	}
	public static void main(String[] args) {
		Sequence sequence=new SequenceA();
		ClientThread t1=new ClientThread(sequence);
		ClientThread t2=new ClientThread(sequence);
		ClientThread t3=new ClientThread(sequence);
		t1.start();
		t2.start();
		t3.start();
	}

}
