package manos.examples.blockingqueue;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable{
	private final BlockingQueue<String> queue;
	private final String producer_id;
	private final String poison_pill_message;
	private final int poison_pill_per_producer;
	public Producer(BlockingQueue<String> s_queue, String producerId, String poisonMessage, int poisonPillPerProducer){
		this.queue = s_queue;
		this.producer_id = producerId;
		this.poison_pill_message = poisonMessage;
		this.poison_pill_per_producer = poisonPillPerProducer;
	}
	
	@Override
	public void run(){
		try {
			produce100Messages();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void produce100Messages() throws InterruptedException{
		for (int i=0; i<100; i++){
			String queue_message = "Producer with id " + this.producer_id + " produced: " + producer_id + "_" + Integer.toString(i) + " message ";
			this.queue.put(queue_message);
			System.out.println(queue_message);
		}
		//depends on the number of consumers
		for (int i=0; i<poison_pill_per_producer; i++){
			this.queue.put(poison_pill_message);
		}
	}
	
}
