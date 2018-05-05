import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class SnakeJPanel extends JPanel implements ActionListener, KeyListener, MouseListener, ComponentListener 
{
	private Timer animationTimer;
	private Timer randomGemTimer;
	
	private Color snakeColor = Color.white;
	private Color backgroundColor = Color.black;
	private Color gemColor = Color.pink;
	
	protected static SnakeJPanel panel;

	protected int x = 5;	// these are grid coordinates, not pixels.
	protected int y = 5;	//	these grid coords are translated to pixels in paint

	protected int iterationCount = 0;
	protected final int iterationLimit = 20;
	
	protected boolean isRunning = false;
	
	protected ArrayList<Point> snake;
	protected ArrayList<Point> gems;
	
	protected int upDown = KeyEvent.VK_UNDEFINED;
	
	protected int leftRight = KeyEvent.VK_RIGHT;
	
	protected int delay = 100;
	protected int delayCount = 0;
	protected int delayLimit = 50;
	
	protected int gemDelay = 2000;
	protected int gemSpeedIncrease = 1;
	protected int gemsToAdd = 0;
	protected int segmentsPerGem = 15;
	
	protected void init()
	{
		this.setLayout(null);
		this.setBackground(backgroundColor);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addComponentListener(this);
		
		panel = this;

        snake = new ArrayList<Point>();
        snake.add(new Point(x, y));
        
        gems = new ArrayList<Point>();
		
		animationTimer = new Timer(delay, this);
        animationTimer.setRepeats(true);
        animationTimer.setCoalesce(true);
        animationTimer.setInitialDelay(0);
        animationTimer.start();
        
        randomGemTimer = new Timer(gemDelay, this);
        randomGemTimer.setRepeats(true);
        randomGemTimer.setCoalesce(true);
        randomGemTimer.setInitialDelay(0);
        randomGemTimer.start();
        
        isRunning = true;
        
		requestFocusInWindow();
	}
	
	public SnakeJPanel() {
		init();
	}

	public SnakeJPanel(LayoutManager arg0) {
		super(arg0);
		init();
	}

	public SnakeJPanel(boolean arg0) {
		super(arg0);
		init();
	}

	public SnakeJPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		init();
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(800, 800);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_RIGHT) ||
			(e.getKeyCode() == KeyEvent.VK_LEFT))
		{
			leftRight = e.getKeyCode();
			upDown = KeyEvent.VK_UNDEFINED;
			System.out.println("Set leftRight to: " + leftRight);
		}
		if ((e.getKeyCode() == KeyEvent.VK_UP) ||
			(e.getKeyCode() == KeyEvent.VK_DOWN))
		{
			upDown = e.getKeyCode();
			leftRight = KeyEvent.VK_UNDEFINED;
			System.out.println("Set upDown to: " + upDown);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		requestFocusInWindow();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		requestFocusInWindow();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	protected boolean isCollision(Point point)
	{
		// Check for self-collision
		for (Point p : snake)
		{
			if (p.x == point.x && p.y == point.y)
			{
				System.out.println("Collision!!!");
				return true;
			}
		}
		// Check for border collision
		if (point.x < 0 || point.y < 0 || point.x*10 >= getWidth() || point.y*10 >= getHeight())
			return true;

		return false;
	}
	
	public void generateNewGem()
	{
		if (gems.size() == 10)
			gems.remove(0);
		
		int x = (int)(Math.random() * getWidth()/10);
		int y = (int)(Math.random() * getHeight()/10);
		gems.add(new Point(x,y));
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (isRunning)
		{
			// Figure out where the new head location is
			if (upDown == KeyEvent.VK_UP)
				y -= 1;
			if (upDown == KeyEvent.VK_DOWN)
				y += 1;
			if (leftRight == KeyEvent.VK_LEFT)
				x -= 1;
			if (leftRight == KeyEvent.VK_RIGHT)
				x += 1;
	
			iterationCount++;
			Point p = new Point(x, y);
			if (!isCollision(p))
			{
				// First, if this new point is on a gem then need to 
				//	add points and make snake longer...
				for (int i = 0; i < gems.size(); i++)
				{
					if (gems.get(i).x == p.x && gems.get(i).y == p.y)
					{
						// Make the snake longer on the next five iterations
						gemsToAdd += segmentsPerGem;
						
						// remove the gem from this index...
						gems.remove(i);
						
						// bump the timer speed up by five
						if (delay > 28)
						{
							delay -= gemSpeedIncrease;
							animationTimer.setDelay(delay);
							System.out.println("Set delay to : " + delay);
					        animationTimer.start();		
						} 
				        break;
					}
				}
			
				
				if (iterationCount < iterationLimit)
				{
					// need to add one to head and remove one from tail
					snake.add(0, p);
					if (gemsToAdd == 0)
						snake.remove(snake.size()-1);
					else
						gemsToAdd--;
				}
				else
				{
					// need to grow the snake - just add one to head
					snake.add(0, p);
					iterationCount = 0;
				}
				
				if (delayCount >= delayLimit  && delay > 25)
				{
					delayCount = 0;
		
					delay = delay - 5;
//					animationTimer.setDelay(delay);
//					System.out.println("Set delay to : " + delay);
//			        animationTimer.start();		
			     }
				else
					delayCount++;
			}
			else
			{
				animationTimer.stop();
				isRunning = false;
				
				// Need to do something to make the end dramatic...
				g.setColor(Color.MAGENTA);
				g.fillOval(x*10-15, y*10-15, 40, 40);
			}
			for (Point point : snake)
			{
				g.setColor(snakeColor);
				g.fillOval(point.x*10, point.y*10, 10, 10);
			}
			for (Point point : gems)
			{
				g.setColor(gemColor);
				g.fillOval(point.x*10, point.y*10, 10, 10);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == animationTimer)
		{
			SwingUtilities.invokeLater(new Runnable() 
			{
				public void run() 
				{
					panel.repaint();
				}
			});
		}
		else
		{
			SwingUtilities.invokeLater(new Runnable() 
			{
				public void run() 
				{
					panel.generateNewGem();
				}
			});
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) 
	{
		System.out.println("Component shown...");
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				panel.requestFocusInWindow();
			}
		});
		
	}

//	@Override
//	public void windowStateChanged(WindowEvent e) 
//	{
//		if (e.getNewState() == WindowEvent.WINDOW_OPENED)
//		{
//			System.out.println("Window opened...");
//			this.requestFocusInWindow();
//		}
//		
//	}
}
