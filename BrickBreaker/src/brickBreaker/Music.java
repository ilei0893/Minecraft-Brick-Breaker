package brickBreaker;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {

	private void musicPlayer(int level) {
		playMusic(level);

	}

	public void playMusic(int level) {
		System.out.println(level);
		if (level == 0) {
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem
						.getAudioInputStream(Main.class.getResourceAsStream("Sweden.wav"));
				clip.open(inputStream);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			try {
				Clip clip2 = AudioSystem.getClip();
				AudioInputStream inputStream2 = AudioSystem
						.getAudioInputStream(Main.class.getResourceAsStream("Ballad of the Cats.wav"));
				clip2.open(inputStream2);
				clip2.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void pressButtonSound() {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(Main.class.getResourceAsStream("minecraft_click.wav"));
			clip.open(inputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
