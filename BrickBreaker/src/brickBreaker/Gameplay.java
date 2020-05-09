package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
	//Set up values for bricks and score
	private boolean play = false;
	private int score = 0;
	private int totalBricks = 21;
	Random random = new Random();
	
	private Timer timer;
	private int delay = 1;
	
	//Set up initial ball values;
	private int playerX = 310;
	private int ballposX = random.nextInt(600);		
	private int ballposY = 350;
	private int a = -1;
	private int b = 1;
	private int ballXdir = random.nextBoolean() ? a : b;
	private int ballYdir = -10;
	private int numPlays = 0;
	
	private int width = 700;
	private int height = 600;
	
	private MapGenerator gameStage;
	
	//Set up timer and map Generator object
	public Gameplay()
	{
		gameStage = new MapGenerator (3,9);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g)
	{
		
		//draw the background and bricks
			try {
				gameStage.draw(((Graphics2D)g));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		//Setup custom minecraft themed font
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("minecraft_font.ttf");
		Font font = null;
		
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(48f);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		genv.registerFont(font);
		
		//borders
		g.setColor(Color.black);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//scores
		printText(g, Color.WHITE, font, 25f, "" + score, 590, 560);
		
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 500, 100, 8);
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("F:\\Users\\ilei0\\eclipse-workspace\\BrickBreaker\\images\\Slimeball_JE2_BE2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//the ball
		g.drawImage(image, ballposX, ballposY, 30, 30, null);
//		g.setColor(Color.yellow);
//		g.fillOval(ballposX, ballposY, 20, 20);
		
		if(play)
		{
			printText(g, Color.WHITE, font, 20f,"Press control to restart", 100, 560);
		}
		//Initial press arrow key instruction
		if(!play && numPlays == 0)
		{
			printText(g, Color.BLACK, font, 30f, "Hit an arrow key to start." , 120, 300);
		}
		
		//Lose condition
		if(ballposY > 570) 
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			
			Color c = new Color(.5f,0f,0f,.5f);	//Draw transparent red death screen at 50% opacity
			g.setColor(c);
			g.drawRect(0, 0, width, height);
			g.fillRect(0, 0, width, height);
			
			FontMetrics fm = g.getFontMetrics();
			int x = ((width - fm.stringWidth("You Died! ")) / 2);
			
			printText(g, Color.WHITE, font, 40f, "You Died! ", x, 250);
			printText(g, Color.WHITE, font, 20f, "You fell out of the world", 200, 300);
			printText(g, Color.WHITE, font, 20f, "Press Space to restart.", 230, 350);
			numPlays++;
		}
		//Win condition
		if(totalBricks <= 0)
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			printText(g, Color.GREEN, font, 20f, "You Win!, Your Score: " + score, 160, 300);
			printText(g, Color.GREEN, font, 20f, "Press Space to restart.", 230, 350);
			numPlays++;
		}
		g.dispose();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		timer.start();
		if(play)
		{
			//Ball collision physics between ball and paddle
			if(new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(playerX, 500, 100, 8)))
			{
				ballYdir = -ballYdir;
			}
			
			A: for(int i = 0; i < gameStage.map.length; i++)
				{
				for(int j = 0; j < gameStage.map[0].length; j++)
				{
					if(gameStage.map[i][j] > 0)
					{
						int brickX = j * gameStage.brickWidth;
						int brickY = i * gameStage.brickHeight;
						int brickWidth = gameStage.brickWidth;
						int brickHeight = gameStage.brickHeight;
						
						//rectangles for the balls and bricks used for detecting collision
						Rectangle rect = new Rectangle (brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 30, 30);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect))
						{
							gameStage.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)
							{
								ballXdir= -ballXdir;
							}
							else
							{
								ballYdir = -ballYdir;
							}
							break A;
						}
					}
				}
			}
			//Initialize movement of ball;
			ballposX += ballXdir;
			ballposY += ballYdir;
			
			//Change direction when hitting borders
			if(ballposX < 0)
			{
				ballXdir = -ballXdir;
			}
			if(ballposY < 0)
			{
				ballYdir = -ballYdir;
			}
			if(ballposX > 670)
			{
				ballXdir = -ballXdir;
			}
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) 
		{
			if(playerX >= 600)
			{
				playerX = 600;
			} else {
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) 
		{
			if(playerX < 10)
			{
				playerX = 10;
			}else {
				moveLeft();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) 
		{
			if(!play)
			{
				restartGame();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
		{
			restartGame();
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			
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
	
	public void restartGame()
	{
		play = true;
		ballposX = random.nextInt(600);
		ballposY = 350;
		ballXdir =  random.nextBoolean() ? a : b;
		ballYdir = -10;
		playerX = 310;
		score = 0;
		totalBricks = 21;
		gameStage = new MapGenerator(3,7);
		
		repaint();
	}
	//Sets and displays color, font, size, x, and y of given string
	public void printText(Graphics g, Color c, Font f, float n, String s, int x, int y)
	{
		g.setColor(c);
		f = f.deriveFont(n);
		g.setFont(f);
		g.drawString(s, x, y);
		
	}
}
