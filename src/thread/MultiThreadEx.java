package thread;

public class MultiThreadEx {

	public static void main(String[] args) {
		
		Thread thread1 = new AlphabetThread();
		Thread thread2 = new Thread(new DigitThread());
		new Thread(new Runnable() {

			@Override
			public void run() {
				for(int i = 0; i <= 9; i++) {
					System.out.print( i );
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();
		
		thread1.start();
		thread2.start();
		/*for(int i = 0; i <= 9; i++) {
			System.out.print( i );
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		
		
	}

}
