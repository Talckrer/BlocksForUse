/**
 * 
 */
package Core.client.threads;

import Core.client.sounds.SoundLoader;
import Core.modBFU.BlocksForUse;


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
				if (second >= 2){
					second = 0;
					SoundLoader.loadListForGui(false);
				}
			} catch (InterruptedException e) {
			}
		}
	}
}
