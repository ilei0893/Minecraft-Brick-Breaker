package brickBreaker;

import java.io.IOException;
import javax.swing.JFrame;


public class Main {
	
	public static JFrame obj;
	public static Gameplay gamePlay;
	
	public static void runGame()
	{
		obj = new JFrame();
		gamePlay = new Gameplay();
		obj.setBounds(10, 10, 700, 600); // Create Swing GUI
		obj.setTitle("Brick Breaker");
		obj.setResizable(false);
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(gamePlay);
	}
	

	public static void main(String[] args) throws IOException {
		runGame();
	}
}
