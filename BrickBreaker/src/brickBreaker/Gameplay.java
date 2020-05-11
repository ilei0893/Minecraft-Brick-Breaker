package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
	// Set up values for bricks and score
	private boolean play = false;
	private static boolean lost = false;
	private int score = 0;
	private int totalBricks = 27;
	private int level = 0;
	static Random random = new Random();

	private Timer timer;
	private int delay = 1;

	// Set up initial ball values;
	private int playerX = 310;
	private int ballposX = random.nextInt(600);
	private int ballposY = 350;
	private static int a = -1;
	private static int b = 1;
	private static int ballXdir = random.nextBoolean() ? a : b;
	private static int ballYdir = -10;
	private static int numPlays = 0;

	private static int width = 700;
	private static int height = 600;

	private MapGenerator gameStage;
	private Music player;
	private Menu initMenu;
	private DeathScreen deathScreen;

	// Set up timer, map Generator, and music object
	public Gameplay() throws IOException {
		gameStage = new MapGenerator(3, 9);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
		player = new Music();
		player.playMusic(level);
		initMenu = new Menu();
		deathScreen = new DeathScreen();
		this.addMouseListener(new MouseInput());

	}

	public static enum STATE {
		MENU, GAME, PAUSE
	};

	public static STATE State = STATE.MENU;

	public void paint(Graphics g) {
		// draw the background and bricks
		if (State == STATE.MENU) {
			BufferedImage image = null;
			try {
				image = ImageIO
						.read(new File("F:\\Users\\ilei0\\eclipse-workspace\\BrickBreaker\\images\\startscreen.jpg"));
				g.drawImage(image, 0, 0, width, height, null);
				initMenu.initialGameMenu(g);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (State == STATE.GAME) {
			try {
				gameStage.draw(((Graphics2D) g));
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Setup custom minecraft themed font
			Font font = null;
			InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("minecraft_font.ttf");

			try {
				font = Font.createFont(Font.TRUETYPE_FONT, stream);
				GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
				genv.registerFont(font);
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
//			GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
//			genv.registerFont(font);

			// borders
			g.setColor(Color.black);
			g.fillRect(0, 0, 3, 592);
			g.fillRect(0, 0, 692, 3);
			g.fillRect(691, 0, 3, 592);

			// scores
			printText(g, Color.WHITE, font, 25f, "" + score, 590, 560);

			// paddle
			g.setColor(Color.green);
			g.fillRect(playerX, 500, 100, 8);

			// the ball
			drawBall(g);

			if (play)
				printText(g, Color.WHITE, font, 20f, "Press control to restart", 100, 560);

			// Initial press arrow key instruction
			if (!play && numPlays == 0)
				printText(g, Color.BLACK, font, 30f, "Hit an arrow key to start.", 120, 300);

			// Lose condition
			if (ballposY > 570)
				lostCon();

			// Minecraft death screen
			deathScreen.showDeathScreen(g, font);

			// Win condition
			if (totalBricks <= 0)
				winCon(g, font);

			g.dispose();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (State == STATE.GAME) {
			timer.start();
			if (play) {
				// Ball collision physics between ball and paddle
				if (new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(playerX, 500, 100, 8))) {
					ballYdir = -ballYdir;
				}

				A: for (int i = 0; i < gameStage.map.length; i++) {
					for (int j = 0; j < gameStage.map[0].length; j++) {
						if (gameStage.map[i][j] > 0) {
							int brickX = j * gameStage.brickWidth;
							int brickY = i * gameStage.brickHeight;
							int brickWidth = gameStage.brickWidth;
							int brickHeight = gameStage.brickHeight;

							// rectangles for the balls and bricks used for detecting collision
							Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
							Rectangle ballRect = new Rectangle(ballposX, ballposY, 30, 30);
							Rectangle brickRect = rect;

							if (ballRect.intersects(brickRect)) {
								gameStage.setBrickValue(0, i, j);
								totalBricks--;
								score += 5;

								if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
									ballXdir = -ballXdir;
								} else {
									ballYdir = -ballYdir;
								}
								break A;
							}
						}
					}
				}
				// Initialize movement of ball;
				ballposX += ballXdir;
				ballposY += ballYdir;

				// Change direction when hitting borders
				if (ballposX < 0) {
					ballXdir = -ballXdir;
				}
				if (ballposY < 0) {
					ballYdir = -ballYdir;
				}
				if (ballposX > 670) {
					ballXdir = -ballXdir;
				}
			}
		}

		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (State == STATE.GAME) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (playerX >= 600) {
					playerX = 600;
				} else {
					moveRight();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (playerX < 10) {
					playerX = 10;
				} else {
					moveLeft();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (!play) {
					restartGame();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				restartGame();
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				play = false;
				timer.stop();
				State = STATE.PAUSE;
			}
		}

	}

	public void moveRight() {
		play = true;
		playerX += 50;
	}

	public void moveLeft() {
		play = true;
		playerX -= 50;
	}

	public void restartGame() {
		play = true;
		lost = false;
		ballposX = random.nextInt(600);
		ballposY = 350;
		ballXdir = (random.nextBoolean() ? a : b);
		ballYdir = -10;
		playerX = 310;
		score = 0;
		totalBricks = 27;
		gameStage = new MapGenerator(3, 9);
		level++;
//		player.playMusic(level);
		repaint();
	}

	// Sets and displays color, font, size, x, and y of given string
	public void printText(Graphics g, Color c, Font f, float n, String s, int x, int y) {
		g.setColor(c);
		f = f.deriveFont(n);
		g.setFont(f);
		g.drawString(s, x, y);

	}

	// Center death screen text
	public void drawCenteredString(String s, int w, int h, Graphics g, Font f, float n) {
		FontMetrics fm = g.getFontMetrics();
		int x = (w - fm.stringWidth(s)) / 2;
		f = f.deriveFont(n);
		g.setFont(f);
		g.drawString(s, x, h);
	}

	// Create ball
	public void drawBall(Graphics g) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("F:\\Users\\ilei0\\eclipse-workspace\\BrickBreaker\\images\\SlimeBall.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// the ball
		g.drawImage(image, ballposX, ballposY, 30, 30, null);
	}

	// Win Condition
	public void winCon(Graphics g, Font font) {
		play = false;
		ballXdir = 0;
		ballYdir = 0;
		printText(g, Color.GREEN, font, 20f, "You Win!, Your Score: " + score, 160, 300);
		printText(g, Color.GREEN, font, 20f, "Press Space to restart.", 230, 350);
		numPlays++;
		level++;
		player.playMusic(level);
		System.out.println(level);
	}

	// Lost Condition
	public void lostCon() {
		play = false;
		lost = true;
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("OOF.wav"));
			clip.open(inputStream);
			clip.start();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		ballposY = 569;

	}
	
	public static void setBallXdir(int num)
	{
		ballXdir = num;
	}
	public static void setBallYdir(int num)
	{
		ballYdir = num;
	}
	public static boolean getLostStatus()
	{
		return lost;
	}
	public static void setNumPlays(int num)
	{
		numPlays = num;
	}
	public static int getNumPlays()
	{
		return numPlays;
	}

}
