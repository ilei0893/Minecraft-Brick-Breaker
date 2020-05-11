package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class DeathScreen {
	
	public Rectangle respawnButton = new Rectangle(175, 325, 350, 50);
	public Rectangle titleScreenButton = new Rectangle(40, 150, 125, 60);
	
	public void showDeathScreen(Graphics g, Font font) {
		if (Gameplay.getLostStatus() == true) {
			Gameplay.setBallXdir(0);
			Gameplay.setBallYdir(0);
			Color c = new Color(.5f, 0f, 0f, .5f); // Draw transparent red death screen at 50% opacity
			g.setColor(c);
			g.drawRect(0, 0, 700, 600);
			g.fillRect(0, 0, 700, 600);
			g.drawRect(175, 325, 335, 40);

			printText(g, Color.WHITE, font, 40f, "You died! ", 240, 250);
			printText(g, Color.WHITE, font, 20f, "You fell out of the world", 200, 300);
//			printText(g, Color.WHITE, font, 20f, "Press Space to restart.", 230, 350);
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
