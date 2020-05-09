package brickBreaker;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MapGenerator{
	public int map[][];
	public int brickWidth;
	public int brickHeight; 
	
	public MapGenerator(int row, int col)
	{
		map = new int[row][col];
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[0].length; j++)
			{
				map[i][j] = 1;
			}
		}
		brickWidth = 695/col;
		brickHeight = 200/row;
	}
	
	public void draw(Graphics2D g) throws IOException
	{
		//Draw background
		BufferedImage image = ImageIO.read(new File("F:\\Users\\ilei0\\eclipse-workspace\\BrickBreaker\\images\\Diamond_Ore_JE3_BE3.png"));
		BufferedImage image2 = ImageIO.read(new File("F:\\Users\\ilei0\\eclipse-workspace\\BrickBreaker\\images\\minecraft-papercraft-grass-background-minecraft-images-minecraft-backgrounds-png-900_506.png"));
		g.drawImage(image2, 1, 1, 700, 600, null);
		
		//Draw bricks
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[0].length; j++)
			{
				if(map[i][j] > 0)
				{
					g.drawImage(image, j*brickWidth, i * brickHeight, brickWidth, brickHeight, null);

				}
			}
		}

	}
	
	public void setBrickValue(int value, int row, int col)
	{
		map[row][col] = value;
		
	}
}
