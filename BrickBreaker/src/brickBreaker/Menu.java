package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu {

	public Rectangle playButton = new Rectangle(40, 150, 125, 60);
	public Rectangle helpButton = new Rectangle(40, 500, 125, 60);
	public Rectangle exitButton = new Rectangle(530, 500, 125, 60);
	
	public Menu() {

	}

	public void initialGameMenu(Graphics g) throws IOException {
		Graphics2D g2d = (Graphics2D) g;
		Font font = null;
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("minecraft_font.ttf");

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		genv.registerFont(font);
		font = font.deriveFont(40f);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Minecraft Brick Breaker ", 60, 100);
		g.drawString("Play", playButton.x + 13, playButton.y + 47);
		g.drawString("Help", helpButton.x + 13, helpButton.y + 47);
		g.drawString("Exit", exitButton.x + 17, exitButton.y + 47);
		g2d.draw(playButton);
		g2d.draw(helpButton);
		g2d.draw(exitButton);

		BufferedImage image = ImageIO
				.read(new File("F:\\Users\\ilei0\\eclipse-workspace\\BrickBreaker\\images\\PlayButton.png"));
		g.drawImage(image, 40, 150, 126, 61, null);

		BufferedImage image2 = ImageIO
				.read(new File("F:\\Users\\ilei0\\eclipse-workspace\\BrickBreaker\\images\\QuitButton.png"));
		g.drawImage(image2, 530, 500, 126, 61, null);

	}
	

}
