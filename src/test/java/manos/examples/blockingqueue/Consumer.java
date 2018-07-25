package manos.examples.blockingqueue;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{
	private final BlockingQueue<String> queue;
	private final String consumer_id;
	private final String poison_pill_message;
	public Consumer(BlockingQueue<String> queue, String consumer_id, String poison_pill_message) {
		super();
		this.queue = queue;
		this.consumer_id = consumer_id;
		this.poison_pill_message = poison_pill_message;
	}
	
	@Override
	public void run(){
		try {
			while(true){
				String consumedString = queue.take();
				if (consumedString.equals(poison_pill_message)){
					return;
				}
				System.out.println("Consumer with id " +consumer_id + " consumed : " + consumedString);
			}
		} catch (InterruptedException e){
			Thread.currentThread().interrupt();
		}
	}
}
