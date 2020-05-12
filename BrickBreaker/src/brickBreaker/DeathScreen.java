package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DeathScreen {

	public void showDeathScreen(Graphics g, Font font) throws IOException {
		if (Gameplay.getLostStatus() == true) {
			Gameplay.setBallXdir(0);
			Gameplay.setBallYdir(0);
			Color c = new Color(.5f, 0f, 0f, .5f); // Draw transparent red death screen at 50% opacity
			g.setColor(c);
			g.drawRect(0, 0, 700, 600);
			g.fillRect(0, 0, 700, 600);

			// Respawn and TitleScreen buttons
			BufferedImage image = ImageIO
					.read(new File("F:\\Users\\ilei0\\eclipse-workspace\\BrickBreaker\\images\\RespawnButton.png"));
			BufferedImage image2 = ImageIO
					.read(new File("F:\\Users\\ilei0\\eclipse-workspace\\BrickBreaker\\images\\TitleScreenButton.png"));
			g.drawImage(image, 175, 335, 336, 41, null);
			g.drawImage(image2, 175, 390, 336, 41, null);

			printText(g, Color.WHITE, font, 40f, "You died! ", 240, 260);
			printText(g, Color.WHITE, font, 20f, "You fell out of the world", 200, 300);
			printText(g, Color.WHITE, font, 15f, "Score: " , 300, 325);
			printText(g, Color.YELLOW,font, 15f, "" + Gameplay.getScore(), 370, 325);
			int numPlays = Gameplay.getNumPlays();
			numPlays++;
			Gameplay.setNumPlays(numPlays);
		}
	}

	private void printText(Graphics g, Color c, Font f, float n, String s, int x, int y) {
		g.setColor(c);
		f = f.deriveFont(n);
		g.setFont(f);
		g.drawString(s, x, y);
	}
}
