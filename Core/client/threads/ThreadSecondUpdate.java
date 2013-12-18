/**
 * 
 */
package Core.client.threads;

import Core.ModBFU.BlocksForUse;
import Core.client.sounds.SoundLoader;


public class ThreadSecondUpdate extends Thread implements Runnable {

	@Override
	public void run() {
		
		int second = 0;
		while (true){
			try {
				this.sleep(1000);
				if (BlocksForUse.sounds.fileRunning){
					BlocksForUse.info.SecondsPlayed++;
				}
				second++;
				if (second >= 3){
					second = 0;
					SoundLoader.loadListForGui(false);
				}
			} catch (InterruptedException e) {
			}
		}
	}
}
