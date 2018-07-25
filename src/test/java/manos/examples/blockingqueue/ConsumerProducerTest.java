package manos.examples.blockingqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.junit.Assert;
import org.junit.Test;


public class ConsumerProducerTest {
	@Test
	public void consume_produce_test(){
		final int number_of_producers = 2;
		final int number_of_consumers = 6;
		final int number_of_poison_messages_per_producer = 3;//6/2+6%2 = 3
		final String poison_pill_message = "END";
		BlockingQueue<String> s_queue = new LinkedBlockingDeque<>(10);
		
		List<Thread> threads = new ArrayList<>();
		
		for (int i=0; i<number_of_producers; i++){
			Thread t = new Thread(new Producer(s_queue, Integer.toString(i), poison_pill_message, number_of_poison_messages_per_producer));
			threads.add(t);
			t.start();
		}
		for (int i=0; i<number_of_consumers; i++){
			Thread t = new Thread(new Consumer(s_queue, Integer.toString(i), poison_pill_message)); 
			threads.add(t);
			t.start();
		}
		
		for (int i=0; i<threads.size(); i++){
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Assert.assertTrue(s_queue.isEmpty());
		
	}
}
