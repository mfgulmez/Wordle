package CMP2004;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class Wordle extends Thread implements ActionListener{ 
	
	class WordPanel extends JPanel{
		
		JLabel[] wordColumns = new JLabel[5];
		public WordPanel() {
			this.setLayout(new GridLayout(1,5));
			Border blackborder = BorderFactory.createLineBorder(new Color(128, 52, 235), 5);
			for(int i=0; i<5; i++) {
				wordColumns[i] = new JLabel();
				wordColumns[i].setOpaque(true);
				wordColumns[i].setBorder(blackborder);
				this.add(wordColumns[i]);
			}
		}
		
		public void setPanelText(String charValue, int position, Color color) {
			this.wordColumns[position].setText(charValue);
			this.wordColumns[position].setBackground(color);
			
		}	
	}
	
	class Keyboard extends JPanel {

		private JTextField keyTyped;
		private JButton okbutton;
		
		public Keyboard() {
			this.setLayout(new GridLayout(1,7));
			keyTyped = new JTextField();
			okbutton = new JButton("OK");
			this.add(okbutton);
			this.add(keyTyped);
		}
		
		public JTextField getUserinput() {
			return keyTyped;
		}
		public JButton getOkbutton() {
			return okbutton;
		}
		
	}
	class DragandDrop extends JPanel implements MouseListener,MouseMotionListener{
		private JButton buttonList[];
		private String keys="QWERTYUIOPASDFGHJKLZXCVBNM";
		Point previousPoint;
		
		public DragandDrop() {
			buttonList=new JButton[keys.length()];
			for(int i=0;i<keys.length();i++) {
				buttonList[i]=new JButton(""+keys.charAt(i));
				if(i<10) {
				add(buttonList[i],BorderLayout.NORTH);}
				else if(i>17){
					add(buttonList[i],BorderLayout.SOUTH);
				}
				else {
					add(buttonList[i],BorderLayout.CENTER);
				}
				buttonList[i].addMouseMotionListener(this);
				buttonList[i].addMouseListener(this);
			}
		} 
		
		@Override
		public void mouseClicked(MouseEvent e) {
		}
		@Override
		public void mousePressed(MouseEvent e) {
			previousPoint=e.getPoint();
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			Point currentPoint=e.getPoint();
			setLocation(currentPoint.x-previousPoint.x,currentPoint.y-previousPoint.y);
			
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			
		}
	}
	private JFrame screen;
	private Boolean gameOver = false;
	private int guess = 0;
	private int fullCorrect = 0;
	private int correct=0;
	private int wrong=0;
	private int score=0;
	private WordPanel[] lines = new WordPanel[6];
	private Keyboard keyboard;
	private DragandDrop DaD;
	private String wordlestring;
    private List<Integer> scores;
    File file = new File("C:\\Users\\Fatih\\eclipse-workspace\\Wordle\\src\\CMP2004\\scores.txt");
    PrintWriter writer;
	public Wordle() {
		try {
			writer=new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String[] options = new String[] {"Drag and Drop", "Keyboard"};
	    int response = JOptionPane.showOptionDialog(null,"Do you want to play on keyboard or drag and drop?", "Wordle",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
		screen = new JFrame("Wordle");
		screen.setSize(700,700);
		screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		screen.setLayout(new GridLayout(7,1));
		screen.setVisible(true);
		screen.setLocationRelativeTo(null);
		for(int i=0; i<6; i++) {
			lines[i] = new WordPanel();
			screen.add(lines[i]);
		}
		 if(response==0) {
			 DaD=new DragandDrop();
			 screen.add(DaD);
			 screen.revalidate();
		 }
		 if(response==1) {
		keyboard=new Keyboard();
		keyboard.getOkbutton().addActionListener(this);
		screen.add(keyboard);
		screen.revalidate();
		 }
		wordlestring = listString();
		System.out.println(wordlestring);
	}
	public String listString() {
		
		List<String> wordList = new ArrayList<>();
        BufferedReader read;
		try {
			read = new BufferedReader(new InputStreamReader(getClass().getResource("list.txt").openStream()));
			String i;
	        while ((i = read.readLine()) != null)
	            wordList.add(i);
	        read.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Random random = new Random();
		int position = random.nextInt(wordList.size());
		return wordList.get(position).trim();
	} 
	
	
	public WordPanel wordLine() {
		return this.lines[guess];
	}
	public void actionPerformed(ActionEvent e) {
		
		if(gameOver == false) {
		String userword = this.keyboard.getUserinput().getText();
		this.keyboard.getUserinput().setText("");
		this.keyboard.getUserinput().requestFocus();
		if(userword.length()>4) {
			sameWord(userword.trim());
			if(fullCorrect==5) {
				switch(guess) {
				case 0: score+=5000;break;
				case 1:score=4500-wrong*50+fullCorrect*100+correct*10;break;
				case 2:score=3000-wrong*50+fullCorrect*100+correct*10;break;
				case 3:score=2500-wrong*50+fullCorrect*100+correct*10;break;
				case 4:score=2000-wrong*50+fullCorrect*100+correct*10;break;
				case 5:score=2000-wrong*50+fullCorrect*100+correct*10;break;
				}
				gameOver = true;
				String[] options = new String[] {"Yes","No"};
				if(score<0) {
					score=0;
				}
				writer.print(score+"\n");
				int response=JOptionPane.showOptionDialog(null, "YOU WON!"+"\nYour score is "+score+"\nDo you want to play again","Wordle", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
				switch(response) {
			    case 0: screen.dispose();new Wordle();
			    case 1: screen.dispose();writer.close();
			    case -1:screen.dispose();writer.close();
			    }
			}
			fullCorrect=0;
			guess++;
		}
		
		if(fullCorrect !=5&&guess == 6 ) {
			 String[] options = new String[] {"Yes", "No"};
			    int response = JOptionPane.showOptionDialog(null,"You Lost! The word was: "+wordlestring+"\nTry Again", "Wordle",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
			    switch(response) {
			    case 0: screen.dispose();new Wordle();
			    case 1: screen.dispose();
			    case -1:screen.dispose();
			    }
			    System.out.println(response);
			gameOver = true;
		}}
	}
	
	
	@SuppressWarnings("unlikely-arg-type")
	private boolean sameWord(String userword) { //haktan
		List<String> wordlist = Arrays.asList(wordlestring.split(""));
		String[] userWordsArray ;
		List<Boolean> fittingStrings = new ArrayList<>();
		userWordsArray = userword.split("");
		for(int i=0;i<5;i++) {
			if(wordlist.contains(userWordsArray[i])) {
				if(wordlist.get(i).equals(userWordsArray[i])) {
					wordLine().setPanelText(userWordsArray[i], i, Color.GREEN);
					fullCorrect++;
					fittingStrings.add(true);
				}else {
					wordLine().setPanelText(userWordsArray[i], i, Color.YELLOW);
					correct++;
					fittingStrings.add(false);
				}
			}else {
				wordLine().setPanelText(userWordsArray[i], i, Color.GRAY);
				wrong++;
				fittingStrings.add(false);
			}
		}
		return !wordlist.contains(false);
	}
public static void main(String[] args) {
	new Wordle();
}


}