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
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener, MouseListener {
	// Set up values for bricks and score
	private boolean play = false;
	private static boolean lost = false;
	private static int score = 0;
	private static int totalBricks = 27;
	private static int level = 0;
	static Random random = new Random();

	private static Timer timer;
	private int delay = 1;

	// Set up initial ball values;
	private static int playerX = 310;
	private static int ballposX = random.nextInt(600);
	private static int ballposY = 350;
	private static int a = -1;
	private static int b = 1;
	private static int ballXdir = random.nextBoolean() ? a : b;
	private static int ballYdir = -7;
	private static int numPlays = 0;

	private static int width = 700;
	private static int height = 600;

	private MapGenerator gameStage;
	private Music player;
	private Menu menu;
	private DeathScreen deathScreen;

	// Set up timer, map Generator, and music object
	public Gameplay() {
		gameStage = new MapGenerator(3, 9);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
		player = new Music();
		player.playMusic(level);
		menu = new Menu();
		deathScreen = new DeathScreen();
		addMouseListener(this);
	}

	public static enum STATE {
		MENU, GAME, PAUSE, DEATH
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
				menu.initialGameMenu(g);
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

			if (getPlay())
				printText(g, Color.WHITE, font, 20f, "Press control to restart", 100, 560);

			// Initial press arrow key instruction
			if (!getPlay() && State == STATE.GAME)
				printText(g, Color.BLACK, font, 30f, "Hit an arrow key to start.", 120, 300);

			// Lose condition
			if (ballposY > 570)
				lostCon(g, font);

			// Show death screen
			try {
				deathScreen.showDeathScreen(g, font);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Win condition
			if (totalBricks <= 0)
				winCon(g, font);

			g.dispose();
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		if (State == STATE.GAME) {
			System.out.println("1");
			timer.start();
			if (getPlay()) {
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

	public void keyReleased(KeyEvent e) {
	}
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
				if (!getPlay()) {
					restartGame();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				restartGame();
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				setPlay(false);
				timer.stop();
				State = STATE.PAUSE;
			}
		}

	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent e) {

		int mx = e.getX();
		int my = e.getY();

		if (Gameplay.State == Gameplay.STATE.MENU) {
			// Play button
			if (mx >= 40 && mx <= 165) {
				if (my >= 150 && my <= 210) {
					Gameplay.State = Gameplay.STATE.GAME;
					timer.restart();
					player.pressButtonSound();
					
				}
			}
			// Help button
			if (mx >= 40 && mx <= 165) {
				if (my >= 500 && my <= 560) {
					player.pressButtonSound();
				}
			}
			// Exit button
			if (mx >= 530 && mx <= 655) {
				if (my >= 500 && my <= 560) {
					System.exit(1);
				}
			}
		}
		if (Gameplay.State == Gameplay.STATE.DEATH) {
			// Respawn button
			if (mx >= 175 && mx <= 510) {
				if (my >= 335 && my <= 365) {
					player.pressButtonSound();
					Gameplay.State = Gameplay.STATE.GAME;
					restartGame();
					timer.restart();
				}
			}
			// Quit to title button
			if (mx >= 175 && mx <= 510) {
				if (my >= 390 && my <= 420) {
					player.pressButtonSound();
					Gameplay.setLostStatus(false);
					Gameplay.State = Gameplay.STATE.MENU;
					restartGame();
					timer.restart();
				}
			}
		}

	}

	public void moveRight() {
		setPlay(true);
		playerX += 50;
	}

	public void moveLeft() {
		setPlay(true);
		playerX -= 50;
	}
	
	//Generate new map
	public void restartGame() {
		setPlay(true);
		lost = false;
		ballposX = random.nextInt(600);
		ballposY = 350;
		ballXdir = (random.nextBoolean() ? a : b);
		ballYdir = -10;
		playerX = 310;
		score = 0;
		totalBricks = 27;
		gameStage = new MapGenerator(3, 9);
		repaint();
	}
	
	// Win Condition
		public void winCon(Graphics g, Font font) {
			setPlay(false);
			timer.stop();
			ballXdir = 0;
			ballYdir = 0;
			printText(g, Color.GREEN, font, 30f, "You Win!, Your Score: " + score, 150, 320);
			printText(g, Color.GREEN, font, 30f, "Press Space to restart.", 140, 365);
			numPlays++;
//			level++;
			player.winSound();
//			player.playMusic(level);
		}

		// Lost Condition
		public void lostCon(Graphics g, Font font) {
			setPlay(false);
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
			State = STATE.DEATH;
			timer.stop();
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

	
	public static void setBallXdir(int num) {
		ballXdir = num;
	}

	public static void setBallYdir(int num) {
		ballYdir = num;
	}

	public static boolean getLostStatus() {
		return lost;
	}

	public static void setLostStatus(boolean val) {
		lost = val;
	}

	public static void setNumPlays(int num) {
		numPlays = num;
	}

	public static int getNumPlays() {
		return numPlays;
	}

	public static int getScore() {
		return score;
	}

	public boolean getPlay() {
		return play;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}

}
