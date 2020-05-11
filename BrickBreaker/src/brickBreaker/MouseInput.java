package brickBreaker;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener {

	Music click = new Music();
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {

		int mx = e.getX();
		int my = e.getY();

//		public Rectangle playButton = new Rectangle(40, 150, 125, 60);
//		public Rectangle helpButton = new Rectangle(40, 500, 125, 60);
//		public Rectangle exitButton = new Rectangle(530, 500, 125, 60);

		// Play button
		if (mx >= 40 && mx <= 165) {
			if (my >= 150 && my <= 210) {
				Gameplay.State = Gameplay.STATE.GAME;
				click.pressButtonSound();
			}
		}
		// Help button
		if (mx >= 40 && mx <= 165) {
			if (my >= 500 && my <= 560) {
				click.pressButtonSound();
			}
		}
		// Exit button
		if (mx >= 530 && mx <= 655) {
			if (my >= 500 && my <= 560) {
				System.exit(1);
			}
		}
	}
}
