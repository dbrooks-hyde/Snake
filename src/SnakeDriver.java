
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class SnakeDriver 
{

	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				createAndShowGUI();
			}
		});
	}
	
	private static void createAndShowGUI()
	{
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SnakeJPanel p = new SnakeJPanel();
        f.add(p);
        f.setResizable(false);
        f.pack();
        f.setVisible(true);	
	}
}