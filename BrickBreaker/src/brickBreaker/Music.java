package brickBreaker;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music{

	private int level = 1;

	private void musicPlayer() 
	{
		playMusic();
		
	}
	public void playMusic()
	{
		try {
			Clip clip2 = AudioSystem.getClip();
			AudioInputStream inputStream2 = AudioSystem.getAudioInputStream(
					Main.class.getResourceAsStream("Sweden.wav"));
			clip2.open(inputStream2);		
			clip2.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
	}
	public static void main(String[] args) {
		Music player = new Music();
	}

}
