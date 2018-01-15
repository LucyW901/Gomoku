//Lucy Wu
//end of April 2016
//The "FiveInARow" class.
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.Color;

public class FiveInARow extends Applet implements ActionListener, MouseListener, MouseMotionListener, ItemListener
{
	private static final long serialVersionUID = 1L;
	
	//initialize all the varibales
    public int user_Color = 0; //color of user: 0 for white, 1 for black
    public int Game_Start = 0; //the flag to show game has already started
    public int Game_Body[] [] = new int [16] [16]; //array for the board: 0 for none, 1 for white and 2 for black
    Button start = new Button ("Start"); //start button, dont need to be clicked when first time lauching the applet
    Button restart = new Button ("Reset"); //reset button for resetting
    TextField winner = new TextField (" "); //text field to display the winning player
    TextField winLabel = new TextField (" "); //text field for "Win!"
    Label result = new Label ("Result"); //name of the text fields
    Checkbox ckb[] = new Checkbox [2]; //two check box for each player
    CheckboxGroup ckg = new CheckboxGroup ();
    Font f;

    public void init ()  //method for initializing
    {
    		setSize(600,400);
		setLayout (null);
		addMouseListener (this);
		
		//Buttons
		add (start);
		start.setBounds (330, 50, 80, 30);
		start.addActionListener (this);
		add (restart);
		restart.setBounds (330, 90, 80, 30);
		restart.addActionListener (this);
		
		//CheckBoxes
		ckb [0] = new Checkbox ("white first", ckg, false);
		ckb [0].setBounds (320, 20, 100, 30);
		ckb [1] = new Checkbox ("black first", ckg, false);
		ckb [1].setBounds (420, 20, 100, 30);
		add (ckb [0]);
		add (ckb [1]);
		ckb [0].addItemListener (this);
		ckb [1].addItemListener (this);
		
		//TextFields
		add (result);
		add (winner);
		add (winLabel);
		result.setBounds (330, 130, 80, 30);
		winner.setBounds (330, 160, 170, 50);
		winLabel.setBounds (330, 210, 170, 50);
		f = new Font ("Comic Sans MS", Font.BOLD, 40);
		winner.setFont (f);
		winLabel.setFont (f);
		
		//start the game
		Game_start ();
    }
    
    
    public void paint (Graphics g) //draw the game board
    {
	    	g.setColor (Color.lightGray);
	    	g.fill3DRect (10, 10, 300, 300, true);
	    	g.setColor (Color.black);
	    	for (int i = 1 ; i < 16 ; i++)
	    	{
	    	    g.drawLine (20, 20 * i, 300, 20 * i);
	    	    g.drawLine (20 * i, 20, 20 * i, 300);
	    	}
    }

    
    public void actionPerformed (ActionEvent e) //when start is pressed, start the game
    {
		Graphics g = getGraphics ();
		if (e.getSource () == start)
		{
		    Game_start ();
		}
		else
		{
		    Game_Restart ();
		}
    }
    
    
    public void Game_start ()  //start the game
    {
		Game_Start = 1;
		Game_enable (false); //enable the buttons and check boxes
		restart.setEnabled (true);
    }


    public void Game_enable (boolean e) //enable the corresponding buttons and check boxes according to current state
    {
    		start.setEnabled (e);
    		restart.setEnabled (e);
    		ckb [0].setEnabled (e);
    		ckb [1].setEnabled (e);
    }


    public void Game_Restart ()  //restart the game
    {
		repaint ();
		Game_start_clear ();
    }
    
    
    public void Game_start_clear ()  //initialize the game and board to original settings
    {
		Game_Start = 0;
		Game_enable (true);
		restart.setEnabled (false);
		ckb [0].setState (true);
		for (int i = 0 ; i < 16 ; i++)
		{
		    for (int j = 0 ; j < 16 ; j++)
		    {
		    		Game_Body [i] [j] = 0;
		    }
		}
		winner.setText ("");
    }

    
    public void itemStateChanged (ItemEvent e) //a method determines which player goes first from the check box
    {
		if (ckb [0].getState ()) 
		{
		    user_Color = 0;
		}
		else
		{
		    user_Color = 1;
		}
    }

    
  //method from MouseListener 
    public void mouseClicked (MouseEvent e)
    {
		Graphics g = getGraphics ();
		int x1, y1;
		x1 = e.getX ();
		y1 = e.getY ();
		if (e.getX () < 20 || e.getX () > 300 || e.getY () < 20 || e.getY () > 300)
		{
		    return;
		}
		if (x1 % 20 > 10)
		{
		    x1 += 20;
		}
		if (y1 % 20 > 10)
		{
		    y1 += 20;
		}
		x1 = x1 / 20 * 20;
		y1 = y1 / 20 * 20;
		display_New (x1, y1); //when an action is detected, display the new move to the board if possible with the processed coordinates above
    }
    
    
    public void display_New (int x, int y)  //when a new movement is made
    {
		if (Game_Start == 0) //return if the game is not on
		{
		    return;
		}
		if (Game_Body [x / 20] [y / 20] != 0) //return if movement is invalid
		{
		    return;
		}
		Graphics g = getGraphics ();
		if (user_Color == 1) //determines the player who made this movement
		{
		    g.setColor (Color.black);
		    user_Color = 0;
		}
		else
		{
		    g.setColor (Color.white);
		    user_Color = 1;
		}
		g.fillOval (x - 10, y - 10, 15, 15); //draw the chess piece on the board 
		Game_Body [x / 20] [y / 20] = user_Color + 1;
		//check all the possible four ways of winning
		if (Game_win_1 (x / 20, y / 20)) 
		{
			//display the winner and flash the text field for 10 times
			//just for it to look a bit fancier...
		    for (int i = 0 ; i <= 10 ; i++)
		    {
		    		winner.setText (Get_Player (user_Color));
		    		winLabel.setText ("Wins!");
				for (int time = 0 ; time <= 100000000 ; time++) {}
				winner.setText (" ");
				winLabel.setText (" ");
				for (int time = 0 ; time <= 100000000 ; time++) {}
				winner.setText (Get_Player (user_Color));
				winLabel.setText ("Wins!");
				i++;
		    }
		    //set the game invalid to continue
		    Game_Start = 0;
		}
		if (Game_win_2 (x / 20, y / 20))
		{
			//display the winner and flash the text field for 10 times
		    for (int i = 0 ; i <= 10 ; i++)
		    {
				winner.setText (Get_Player (user_Color));
				winLabel.setText ("Wins!");
				for (int time = 0 ; time <= 100000000 ; time++) {}
				winner.setText (" ");
				winLabel.setText (" ");
				for (int time = 0 ; time <= 100000000 ; time++) {}
				winner.setText (Get_Player (user_Color));
				winLabel.setText ("Wins!");
				i++;
		    }
		    //set the game invalid to continue
		    Game_Start = 0;
		}
		if (Game_win_3 (x / 20, y / 20)) 
		{
			//same as above
		    for (int i = 0 ; i <= 10 ; i++)
		    {
				winner.setText (Get_Player (user_Color));
				winLabel.setText ("Wins!");
				for (int time = 0 ; time <= 100000000 ; time++)
				{
				}
				winner.setText (" ");
				winLabel.setText (" ");
				for (int time = 0 ; time <= 100000000 ; time++)
				{
				}
				winner.setText (Get_Player (user_Color));
				winLabel.setText ("Wins!");
				i++;
		    }
		    Game_Start = 0;
		}
		if (Game_win_4 (x / 20, y / 20))
		{
			//same as above
		    for (int i = 0 ; i <= 10 ; i++)
		    {
				winner.setText (Get_Player (user_Color));
				winLabel.setText ("Wins!");
				for (int time = 0 ; time <= 100000000 ; time++)
				{
				}
				winner.setText (" ");
				winLabel.setText (" ");
				for (int time = 0 ; time <= 100000000 ; time++)
				{
				}
				winner.setText (Get_Player (user_Color));
				winLabel.setText ("Wins!");
				i++;
		    }
		    Game_Start = 0;
		}
    }
    
    //The following four methods are to detect whether one of the player won, either horizontally or vertically or diagonally
    //returns a boolean variable
    public boolean Game_win_1 (int x, int y)  //detect horizontally
    {
		int x1, y1, t = 1;
		x1 = x;
		y1 = y;
		for (int i = 1 ; i < 5 ; i++)
		{
		    if (x1 > 15)
		    {
		    		break;
		    }
		    if (Game_Body [x1 + i] [y1] == Game_Body [x] [y])
		    {
		    		t += 1;
		    }
		    else
		    {
		    		break;
		    }
		}
		for (int i = 1 ; i < 5 ; i++)
		{
		    if (x1 < 1)
		    {
		    		break;
		    }
		    if (Game_Body [x1 - i] [y1] == Game_Body [x] [y])
		    {
		    		t += 1;
		    }
		    else
		    {
		    		break;
		    }
		}
		if (t > 4)
		{
		    return true;
		}
		else
		{
		    return false;
		}
    }

    public boolean Game_win_2 (int x, int y)  //detect vertically
    {
		int x1, y1, t = 1;
		x1 = x;
		y1 = y;
		for (int i = 1 ; i < 5 ; i++)
		{
		    if (x1 > 15)
		    {
		    		break;
		    }
		    if (Game_Body [x1] [y1 + i] == Game_Body [x] [y])
		    {
		    		t += 1;
		    }
		    else
		    {
		    		break;
		    }
		}
		for (int i = 1 ; i < 5 ; i++)
		{
		    if (x1 < 1)
		    {
		    		break;
		    }
		    if (Game_Body [x1] [y1 - i] == Game_Body [x] [y])
		    {
		    		t += 1;
		    }
		    else
		    {
		    		break;
		    }
		}
		if (t > 4)
		{
		    return true;
		}
		else
		{
		    return false;
		}
    }

    public boolean Game_win_3 (int x, int y)  //detect diagonally from the left top
    {
		int x1, y1, t = 1;
		x1 = x;
		y1 = y;
		for (int i = 1 ; i < 5 ; i++)
		{
		    if (x1 > 15)
		    {
			break;
		    }
		    if (Game_Body [x1 + i] [y1 - i] == Game_Body [x] [y])
		    {
			t += 1;
		    }
		    else
		    {
			break;
		    }
		}
		for (int i = 1 ; i < 5 ; i++)
		{
		    if (x1 < 1)
		    {
		    		break;
		    }
		    if (Game_Body [x1 - i] [y1 + i] == Game_Body [x] [y])
		    {
		    		t += 1;
		    }
		    else
		    {
		    		break;
		    }
		}
		if (t > 4)
		{
		    return true;
		}
		else
		{
		    return false;
		}
    }


    public boolean Game_win_4 (int x, int y)  //detect diagonally from the top right corner
    {
		int x1, y1, t = 1;
		x1 = x;
		y1 = y;
		for (int i = 1 ; i < 5 ; i++)
		{
		    if (x1 > 15)
		    {
		    		break;
		    }
		    if (Game_Body [x1 + i] [y1 + i] == Game_Body [x] [y])
		    {
		    		t += 1;
		    }
		    	else
		    {
		    		break;
		    }
		}
		for (int i = 1 ; i < 5 ; i++)
		{
		    if (x1 < 1)
		    {
		    		break;
		    }
		    if (Game_Body [x1 - i] [y1 - i] == Game_Body [x] [y])
		    {
		    		t += 1;
		    }
		    else
		    {
		    		break;
		    }
		}
		if (t > 4)
		{
		    return true;
		}
		else
		{
		    return false;
		}
    }


    public String Get_Player (int x) //get which player it is 
    {
		if (x == 0)
		{
		    return "Black";
		}
		else
		{
		    return "White";
		}
    }

    
    	//unused method from MouseListener
    public void mousePressed (MouseEvent e){}
    public void mouseEntered (MouseEvent e) {}
    public void mouseExited (MouseEvent e) {}
    public void mouseReleased (MouseEvent e) {}
    public void mouseDragged (MouseEvent e) {}
    public void mouseMoved (MouseEvent e) {}
 
}
