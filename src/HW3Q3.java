import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class HW3Q3 extends JPanel {
	
	public static boolean isGameOver = false;
	
   public static HW3Q3 plan;
   //box and edge and bricks properties

   static Clip PlaySound;
   static JFrame frame = new JFrame("Amazing Brick");
   public static boolean ANewKey = false;
   public static  int BOX_WIDTH = 500;
   public static  int BOX_HEIGHT = 800;
   public static  int DELTA_EDGE = 700;
   public static  int DELTA_BRICK = 200;
   public static  int EDGE_DIAMETER = 35;
   public static  int BRICK_LENGTH = 30;
   public static  int EDGE_DISTANCE_FROM_SIDES = 140;
   public static  int EDGE_FIRST_PUSH_UP = 500;
   //score
	public static int Score = 0;
	//random
   public static  ArrayList<Integer> RANDOM_NUMBERS = new ArrayList<>();
   public static  ArrayList<Integer> RANDOM_NUMBERS2 = new ArrayList<>();
   public static  ArrayList<Integer> RANDOM_NUMBERS3 = new ArrayList<>();
   //properties of EDGES & BRICKS
   public static  ArrayList<Integer> X_OF_EDGES = new ArrayList<>();
   public static ArrayList<Integer> Y_OF_EDGES = new ArrayList<>();
   public static  ArrayList<Integer> EDGE_LEFT = new ArrayList<>();
   public static  ArrayList<Integer> EDGE_RIGHT = new ArrayList<>();
   public static  ArrayList<Integer> X_OF_UPPER_BRICKS = new ArrayList<>();
   public static  ArrayList<Integer> X_OF_DOWNER_BRICKS = new ArrayList<>();
   public static ArrayList<Integer> Y_OF_UPPER_BRICKS = new ArrayList<>();
   public static ArrayList<Integer> Y_OF_DOWNER_BRICKS = new ArrayList<>();
   // Ball's properties
   public static float ballRadius = 18f;
   public static float ballX = BOX_WIDTH/2;
   public static float ballY = BOX_HEIGHT/5;
   public static float ballSpeedX = 0f;
   public static float ballSpeedY = 0f;
   public static float ballAccerelationY = 0.0f;
	public static float BallAccerelationOmega = 2.0f;
	public static float Omega = 0;
	public static float degree = 0;
	static public Polygon polygon;
	//picture of game:
	public static JavaImageIOTest CLASS_OF_PIC = new JavaImageIOTest();
	public static BufferedImage TitlePIC = CLASS_OF_PIC.image;
	public static int Y_OF_PIC = BOX_HEIGHT - TitlePIC.getHeight();
	public static int X_OF_PIC = 0;
	
   //refresh per second:
   public static final int UPDATE_RATE = 80;
   
   public boolean isMid(double number, int point1, int point2) {
       if((number>=point1 && number <= point2) || (number<=point1 && number>= point2)) {
           return true;
       }
       return false;
   }


	public boolean checkOverlapRecPoly(Rectangle rec, Polygon poly) {// p1: nearby rectangle  p2: 
		for(int i = 0 ; i < poly.xpoints.length ; i++)
			if(isMid(poly.xpoints[i], rec.x, rec.x + rec.width) && isMid(poly.ypoints[i], rec.y , rec.y + rec.height))
				return true;
		
		
		return false;
	}

	public static void playSound(String address) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(address).getAbsoluteFile());
            PlaySound = AudioSystem.getClip();
            PlaySound.open(audioInputStream);
            PlaySound.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }
	
	
	
   public ArrayList<Rectangle> nearbyRectangles(){
	   ArrayList<Rectangle> toReturn = new ArrayList<>();
	   int k = 0;
	   for(int i = 0 ; i < 1000 ; i++){
		   if( (((BOX_HEIGHT / 2) - 40) < Y_OF_EDGES.get(i)) && (Y_OF_EDGES.get(i) < BOX_HEIGHT) ){
			   toReturn.add(new Rectangle(0, Y_OF_EDGES.get(i), EDGE_LEFT.get(i), EDGE_DIAMETER));
			   toReturn.add(new Rectangle(X_OF_EDGES.get(i), Y_OF_EDGES.get(i), EDGE_RIGHT.get(i), EDGE_DIAMETER));
			   k = i;
		   }
	   }
	   
	   if(k != 0){
		   for(int i = k - 1 ; i <= k + 1 ; i++){
		   		if( (((BOX_HEIGHT / 2) - 40) < Y_OF_UPPER_BRICKS.get(i)) && (Y_OF_UPPER_BRICKS.get(i) < BOX_HEIGHT) ){
			   		toReturn.add(new Rectangle(X_OF_UPPER_BRICKS.get(i), Y_OF_UPPER_BRICKS.get(i), BRICK_LENGTH, BRICK_LENGTH));
		   		}
		   }
	   }
	   
	   else{
		   for(int i = 0 ; i < 1000 ; i++){
			   if( (((BOX_HEIGHT / 2) - 40) < Y_OF_UPPER_BRICKS.get(i)) && (Y_OF_UPPER_BRICKS.get(i) < BOX_HEIGHT) ){
				   toReturn.add(new Rectangle(X_OF_UPPER_BRICKS.get(i), Y_OF_UPPER_BRICKS.get(i), BRICK_LENGTH, BRICK_LENGTH));
				   k = i;
			   }
		   }
	   }
	   
	   if(k != 0){
		   for(int i = k - 1 ; i <= k + 1 ; i++){
		   		if( (((BOX_HEIGHT / 2) - 40) < Y_OF_DOWNER_BRICKS.get(i)) && (Y_OF_DOWNER_BRICKS.get(i) < BOX_HEIGHT) ){
			   		toReturn.add(new Rectangle(X_OF_DOWNER_BRICKS.get(i), Y_OF_DOWNER_BRICKS.get(i), BRICK_LENGTH, BRICK_LENGTH));
		   		}
		   }
	   }
	   
	   else{
		   for(int i = 0 ; i < 1000 ; i++){
			   if( (((BOX_HEIGHT / 2) - 40) < Y_OF_DOWNER_BRICKS.get(i)) && (Y_OF_DOWNER_BRICKS.get(i) < BOX_HEIGHT) ){
				   toReturn.add(new Rectangle(X_OF_DOWNER_BRICKS.get(i), Y_OF_DOWNER_BRICKS.get(i), BRICK_LENGTH, BRICK_LENGTH));
				   k = i;
			   }
		   }
	   }
	   return toReturn;
   }

   public void removeKeyListenerFromFrame(JFrame frame, KeyListener keylistener){
	   frame.removeKeyListener(keylistener);
   }

   public static int counter = 0;
   public static boolean key1 = false;
   
   public HW3Q3() {
      this.setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));
     
      for(int mofo = 0 ; mofo < 1000 ; mofo++){
    	  RANDOM_NUMBERS.add((int)(Math. random() * (BOX_WIDTH - 2 * EDGE_DISTANCE_FROM_SIDES) + 1));
    	  RANDOM_NUMBERS2.add((int)(Math. random() * (100) + 1));
    	  RANDOM_NUMBERS3.add((int)(Math. random() * (100) + 1));
      }
      
      for(int mofo = 0 ; mofo < 1000 ; mofo++){
    	  int randomNumber = RANDOM_NUMBERS.get(mofo);
    	  int randomNumber2 = RANDOM_NUMBERS2.get(mofo);
    	  int randomNumber3 = RANDOM_NUMBERS3.get(mofo);
    	  Y_OF_EDGES.add(HW3Q3.BOX_HEIGHT - HW3Q3.DELTA_BRICK - (mofo * HW3Q3.DELTA_EDGE) - HW3Q3.EDGE_FIRST_PUSH_UP);
      	  X_OF_EDGES.add(2*EDGE_DISTANCE_FROM_SIDES + randomNumber);
      	  EDGE_LEFT.add(EDGE_DISTANCE_FROM_SIDES + randomNumber);
      	  EDGE_RIGHT.add(BOX_WIDTH - 2 * EDGE_DISTANCE_FROM_SIDES - randomNumber);
      	  X_OF_UPPER_BRICKS.add(EDGE_DISTANCE_FROM_SIDES + randomNumber + randomNumber2);
      	  X_OF_DOWNER_BRICKS.add(EDGE_DISTANCE_FROM_SIDES + randomNumber + randomNumber3);
      	  Y_OF_UPPER_BRICKS.add(BOX_HEIGHT - DELTA_BRICK - (mofo * DELTA_EDGE) - EDGE_FIRST_PUSH_UP + DELTA_BRICK);
      	  Y_OF_DOWNER_BRICKS.add(BOX_HEIGHT - DELTA_BRICK - (mofo * DELTA_EDGE) - EDGE_FIRST_PUSH_UP - DELTA_BRICK);
      }
      Thread gameThread = new Thread() {
    	  
    	  @Override
         public void run() {
			  ballSpeedX = 0;
			  ballSpeedY = 0;
			  Omega = 0;
			  degree = 0;
			  System.out.println("heree");
			  while (!isGameOver) {
               ballSpeedY += ballAccerelationY;
               ballX += ballSpeedX;
               ballY += ballSpeedY;
				for(int i = 0 ; i < 1000 ; i++){
					if(ballY <= Y_OF_EDGES.get(i) && i+1 > Score){
						Score = i+1;
						break;
					}
				}
				degree += Omega;
				int[] temp1= {(int)(ballX-ballRadius*Math.cos(degree)),(int)(ballX+(int)ballRadius*Math.sin(degree)),
						(int)(ballX+ballRadius*Math.cos(degree)),(int)ballX-(int)(ballRadius*Math.sin(degree))};
				int[] temp2 = {(int)(ballY-ballRadius*Math.sin(degree)), (int)(ballY-ballRadius*Math.cos(degree)),
						(int)(ballY+ballRadius*Math.sin(degree)),(int)(ballY+ballRadius*Math.cos(degree))};

               polygon = new Polygon(temp1, temp2, 4);
               ArrayList<Rectangle> nearbyRectangles = nearbyRectangles();
               for(int h = 0 ; h < nearbyRectangles.size() ; h++){
            	   if( checkOverlapRecPoly(  nearbyRectangles.get(h) , polygon )){
						key1 = true;
					   ballSpeedX = 0;
					   ballSpeedY = (float) 6.0;
					   //isGameOver = true;
					   Omega+= BallAccerelationOmega;
					   // TODO
					   ANewKey = true;
					   //break;
					   //removeKeyListenerFromFrame(frame);
            		   System.out.println("yoYo");
            	   }
               }
               //if(checkIntersection(new Rectangle(, arg1, arg2, arg3), new Rectangle(arg0, arg1, arg2, arg3) )
               if (ballX - ballRadius < 0) {
                  ballSpeedX = 0; // Reflect along normal
                  ballX = ballRadius; // Re-position the ball at the edge
               }
               else if (ballX + ballRadius > BOX_WIDTH) {
                  ballSpeedX = 0;
                  ballX = BOX_WIDTH - ballRadius;
               }
               if (ballY - ballRadius < BOX_HEIGHT / 2 && key1 == false) {
            	   ballY -= ballSpeedY;
            	   Y_OF_PIC -= ballSpeedY;
            	  for(int i = 0 ; i < 1000 ; i++){
            		  Y_OF_UPPER_BRICKS.set(i, (int) (Y_OF_UPPER_BRICKS.get(i) - ballSpeedY));
            		  Y_OF_DOWNER_BRICKS.set(i , (int) (Y_OF_DOWNER_BRICKS.get(i) - ballSpeedY));
            		  Y_OF_EDGES.set(i , (int) (Y_OF_EDGES.get(i) - ballSpeedY));
            	  }
               } 
               else if (ballY + ballRadius > BOX_HEIGHT) {
                  ballY = BOX_HEIGHT - ballRadius;
                  ballSpeedY = 0;
                  HW3Q3.playSound("src/gameover.wav");
                  isGameOver = true;
                  this.suspend();
                 // break;
               }
               
               // Refresh the display
               if(ANewKey)
            	   counter++;
               if(counter == 60){
            	   HW3Q3.playSound("src/gameover.wav");
            	   this.suspend();
            	   //break;
               }
               repaint(); // Callback paintComponent()
               // Delay for timing control and give other threads a chance
               try {
                  Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
               } catch (InterruptedException ex) { }
            }
         }
      };
      gameThread.start();  // Callback run()
   }
   
   public void changeColor(Graphics g){//for changing colors of edges & bricks
	  if(g.getColor().equals(Color.BLACK))
		  g.setColor(Color.RED);
 	  else if(g.getColor().equals(Color.RED))
 		  g.setColor(Color.ORANGE);
 	 else if(g.getColor().equals(Color.ORANGE))
		  g.setColor(Color.GREEN);
	  else if(g.getColor().equals(Color.GREEN))
		  g.setColor(Color.YELLOW);
	  else if(g.getColor().equals(Color.YELLOW))
		  g.setColor(Color.BLUE);
	  else if(g.getColor().equals(Color.BLUE))
		  g.setColor(Color.GRAY);
	  else if(g.getColor().equals(Color.GRAY))
		  g.setColor(Color.BLACK);
   }
   
   public void paintComponent(Graphics g) {
      super.paintComponent(g);    // Paint background
  
      // Draw the box
      g.setColor(Color.WHITE);
      
      g.fillRect(0, 0, BOX_WIDTH, BOX_HEIGHT);
      
      // Draw the Picture
      
      g.drawImage(TitlePIC, X_OF_PIC, Y_OF_PIC, TitlePIC.getWidth(), TitlePIC.getHeight(), null);
     // g.drawImage(TitlePIC, 0, (int)( 0.75 * BOX_HEIGHT), TitlePIC.getWidth()/2, TitlePIC.getHeight()/2, null);
      // Draw the ball
      g.setColor(Color.BLACK);
      g.fillPolygon(polygon);
      // Draw edges 
      g.setColor(Color.BLACK);
      for(int i = 0 ; i < 1000 ; i++){
    	  if((i-1)%4 == 3)
    		  this.changeColor(g);
    	  //right side of edge
    	  g.fillRect(0, Y_OF_EDGES.get(i), EDGE_LEFT.get(i), EDGE_DIAMETER);
    	  //left side of edge
    	  g.fillRect(X_OF_EDGES.get(i), Y_OF_EDGES.get(i), EDGE_RIGHT.get(i), EDGE_DIAMETER);
    	  //upper bricks
    	  g.fillRect(X_OF_UPPER_BRICKS.get(i),  Y_OF_UPPER_BRICKS.get(i),  BRICK_LENGTH, BRICK_LENGTH);
    	  //downer bricks
    	  g.fillRect(X_OF_DOWNER_BRICKS.get(i), Y_OF_DOWNER_BRICKS.get(i), BRICK_LENGTH, BRICK_LENGTH);
      }
      
  //    ImagePanel IP = new ImagePanel();
      
     // g.drawImage(IP.image, (BOX_WIDTH/2 - IP.image.getWidth()/6), BOX_HEIGHT/2, IP.image.getWidth()/3, IP.image.getHeight()/3, null);
      
      
      // Display the ball's information
      g.setColor(Color.BLACK);
      g.setFont(new Font("Courier New", Font.PLAIN, 12));
      StringBuilder sb = new StringBuilder();
      Formatter formatter = new Formatter(sb);
      formatter.format("SCORE (%d) HIGHSCORE (38)", Score);
      g.drawString(sb.toString(), 20, 30);
      formatter.close();
      
   }


   public static void main(String[] args) {




	    BOX_WIDTH = 500;
	    BOX_HEIGHT = 800;
	    DELTA_EDGE = 560;
	    DELTA_BRICK = 150;
	    EDGE_DIAMETER = 35;
	    BRICK_LENGTH = 30;
	    EDGE_DISTANCE_FROM_SIDES = 140;
	    EDGE_FIRST_PUSH_UP = 500;
	    Score = 0;



	   RANDOM_NUMBERS = new ArrayList<>();
	    RANDOM_NUMBERS2 = new ArrayList<>();
	   RANDOM_NUMBERS3 = new ArrayList<>();
	   //properties of EDGES & BRICKS
	   X_OF_EDGES = new ArrayList<>();
	   Y_OF_EDGES = new ArrayList<>();
	   EDGE_LEFT = new ArrayList<>();
	   EDGE_RIGHT = new ArrayList<>();
	   X_OF_UPPER_BRICKS = new ArrayList<>();
	    X_OF_DOWNER_BRICKS = new ArrayList<>();
	   Y_OF_UPPER_BRICKS = new ArrayList<>();
	    Y_OF_DOWNER_BRICKS = new ArrayList<>();



	   ballX = BOX_WIDTH/2;
	   ballY = 3 * BOX_HEIGHT/4;
	   ballSpeedX = 0f;
	   ballSpeedY = 0f;
	   ballAccerelationY = 0f;
	   keyboardListener.key = false;
	   key1 = false;
	   X_OF_EDGES = new ArrayList<>();
	   ballRadius = 18f;
	   BallAccerelationOmega = 2.0f;
	   Omega = 0;
	   degree = 0;
	   polygon = new Polygon();

	   keyboardListener keyboardListener = new keyboardListener();
	   frame.getContentPane().removeAll();
	   
	   
    //  SwingUtilities.invokeLater(new Runnable() {
    //     public void run() {
			 HW3Q3 panel = new HW3Q3();
			 frame.addKeyListener(keyboardListener);
			 frame.setLocation(700,100);
			 frame.setResizable(false);
			 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 frame.setContentPane(panel);
			 frame.pack();
			 frame.setVisible(true);
			 //while(true){
				 //if(isGameOver){
				//	 frame.removeKeyListener(keyboardListener);
				//	 //isGameOver = false;
				//	 break;
				// }
			 //}
			 
	//	 }
     // });
      while(true){
    	  if(isGameOver){
    		  frame.removeKeyListener(keyboardListener);
    		  isGameOver = false;
    		  break;
    	  }
      }
   }
}




class keyboardListener implements KeyListener {
	public static boolean key = false;

	@Override
	public void keyPressed(KeyEvent e){
		int a = e.getKeyCode();
		if(a == KeyEvent.VK_LEFT && HW3Q3.ballX > HW3Q3.ballRadius && !HW3Q3.isGameOver){
			System.out.println("chap");
			HW3Q3.ballSpeedX = (float)-2.5;
			HW3Q3.ballSpeedY = (float)-8.0;
			HW3Q3.playSound("src/jump.wav");
			if(!key){
				HW3Q3.ballAccerelationY = (float) 0.25;
				//		   IP.setVisible(false);
				key = true;
			}
		}
		else if(a == KeyEvent.VK_RIGHT && HW3Q3.ballX < HW3Q3.BOX_WIDTH - HW3Q3.ballRadius && !HW3Q3.isGameOver){
			System.out.println("rast");
			HW3Q3.ballSpeedX = (float)2.5;
			HW3Q3.ballSpeedY = (float)-8.0;
			HW3Q3.playSound("src/jump.wav");
			if(!key){
				HW3Q3.ballAccerelationY = (float) 0.25;
//			   IP.setVisible(false);
				key = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e){}

	@Override
	public void keyTyped(KeyEvent e){}
	
	
}
 


class JavaImageIOTest{ 
	public static BufferedImage image;
  public JavaImageIOTest()
  {
    try
    {
      // the line that reads the image file
      image = ImageIO.read(new File("src/title.png"));
      // work with the image here ...
    }
    catch (IOException e)
    {
      // log the exception
      // re-throw if desired
    }
  }
}