/**
 * 
 */
package org.smart4j.framework.threadLocal;

/**
 * @author Minzhe Xu	2017年7月22日下午2:44:44
 *	
 */
public class ClientThread extends Thread{
	private Sequence sequence;
	public ClientThread(Sequence sequence){
		this.sequence=sequence;
	}
	@Override
	public void run() {
		for(int i=0;i<3;i++){
			System.out.println(Thread.currentThread().getName()+"----->"+sequence.getNum());
		
		}
	}
	

}
