
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import acm.graphics.*;
import java.awt.Color;
import java.awt.event.*;


public class breakOutRealDeal extends GraphicsProgram {
	
	/*Window and game general information by default */
	private static final int WIN_WIDTH=400;
	private static final int WIN_HEIGHT=800;
	private static final double PAUSE_TIME=1000/48.0;
	
	/*brick in board information */
	private static final int BRICK_NUMBER_ROW=10;
	private static final int BRICK_NUMBER_COLUMN=10;
	private static final int BRICK_WIDTH=35;
	private static final int BRICK_HEIGHT=10;
	private static final int BRICK_SPACE=2;
	private static final int BRICK_TOP_SPACE=30;
	
	
	/*paddle information*/
	private static final int PADDLE_WIDTH=30;
	private static final int PADDLE_HEIGHT=5;
	private static final int PADDLE_VERTICAL_HEIGHT=700;//distance from board bottom
	
	/*ball information */
	private static final int BALL_RADIUS=5;
	private static final double INIT_SPEED_DWN=7.0;
	
	/*below is code dedicated towards playing excluding setup*/
	
	public void run(){
		setSize(WIN_WIDTH,WIN_HEIGHT);
		setup();
		
		waitForClick();//hold on until user clicks
		
		while(!gameOver){
			moveBall();
			checkBoardOrPaddleCollision();
			checkWallCollision();
			
			pause(PAUSE_TIME);
			
		}
		
	}
	
	private void moveBall(){
		
		vx=rgen.nextDouble(1.0,6.0);//random movements across x axis..y is constant
		if(rgen.nextBoolean()){
			vx=-vx;
		}
		ball.move(vx, vy);
	}
	
	
	private void checkWallCollision(){//still needs work
		
		if(ball.getX()+2*BALL_RADIUS>=WIN_WIDTH){//passes wall on right side
			//when collision occurs down deflect upwards
			ball.move((-ball.getX()-ball.getWidth()+getWidth()), 0);
			
		}
		if(ball.getX()<=0){//passes wall on left side
			//when collision occurs down deflect upwards
			ball.move((-ball.getX()-ball.getWidth()),0);
		}
		if(ball.getY()<=0){//passes on top side
			
			vy=-vy;//just reverse upward direction to downward
			
		}
		
		if(ball.getY()+2*BALL_RADIUS>=getHeight()){//IRRELEVANT FOR GAME JUST FOR TESTING
			//vy=-vy;
			//ball.move(0, -ball.getY()-ball.getHeight()+getHeight());
			status=new GLabel("GAMEOVER",200,200);
			add(status);
			gameOver=true;
		}
	}
	private void checkBoardOrPaddleCollision(){
		
		/*below is code regarding ball striking board*/
		
		ball.sendToBack();//place ball behind the brick or paddle in Z stack order so that getElement is false proof
		collided_item=getElementAt(ball.getX(),ball.getY());//hits top left of ball
		for(int i=0;i<BRICK_NUMBER_ROW;i++){
			for(int j=0;j<BRICK_NUMBER_COLUMN;j++){
				if(collided_item==brick[i][j]){
					remove(brick[i][j]);
				}
			}
		}
		collided_item=getElementAt(ball.getX()+2*BALL_RADIUS,ball.getY());//hits top right of ball
		for(int i=0;i<BRICK_NUMBER_ROW;i++){
			for(int j=0;j<BRICK_NUMBER_COLUMN;j++){
				if(collided_item==brick[i][j]){
					remove(brick[i][j]);
				}
			}
		}
		collided_item=getElementAt(ball.getX(),ball.getY()+2*BALL_RADIUS);//hits bottom left of ball
		for(int i=0;i<BRICK_NUMBER_ROW;i++){
			for(int j=0;j<BRICK_NUMBER_COLUMN;j++){
				if(collided_item==brick[i][j]){
					remove(brick[i][j]);
				}
			}
		}
		collided_item=getElementAt(ball.getX()+2*BALL_RADIUS,ball.getY()+2*BALL_RADIUS);//hits bottom right of ball
		for(int i=0;i<BRICK_NUMBER_ROW;i++){
			for(int j=0;j<BRICK_NUMBER_COLUMN;j++){
				if(collided_item==brick[i][j]){
					remove(brick[i][j]);
				}
			}
		}
		
		collided_item=getElementAt(ball.getX(),ball.getY()+2*BALL_RADIUS);
		if(collided_item==paddle){
			vy=-vy;
			
		}
		collided_item=getElementAt(ball.getX()+2*BALL_RADIUS,ball.getY()+2*BALL_RADIUS);
		if(collided_item==paddle){
			vy=-vy;
			
		}
		collided_item=getElementAt(ball.getX()+BALL_RADIUS,ball.getY()+2*BALL_RADIUS);
		if(collided_item==paddle){
			vy=-vy;
		}
	}
	
	public void mouseMoved(MouseEvent e){
		paddle.setLocation(e.getX(),PADDLE_VERTICAL_HEIGHT);//moves wherever mouses moves keeping y constant
	}
	
	/*below code is dedicated mainly for setup only*/
	
	
	private void setup(){//only related to setting up
		setBoard();
		setPaddle();
		setBall();
		addMouseListeners();//listening for mouse action
		
	}
	private void setBoard(){//setting up board 2 rows at a time
		double dx=(WIN_WIDTH/2)-(5*BRICK_WIDTH);
		double dy=BRICK_TOP_SPACE;
		int j;
		
		for(int i=0;i<10;i++){
			for(j=0;j<10;j++){
				brick[i][j]=new GRect(dx,dy,BRICK_WIDTH,BRICK_HEIGHT);
				brick[i][j].setFilled(true);
				
				if(i<2){
					brick[i][j].setFillColor(Color.RED);//first 2 rows red
				}else if(i>=2 && i<4){
					brick[i][j].setFillColor(Color.ORANGE);//next 2 orange
				}else if(i>=4 && i<6){
					brick[i][j].setFillColor(Color.YELLOW);//next 2 yellow
				}else if(i>=6 && i<8){
					brick[i][j].setFillColor(Color.GREEN);//next 2 green
				}else if(i>=8 && i<10){
					brick[i][j].setFillColor(Color.CYAN);//last 2 cyan
				}
				add(brick[i][j]);
				dx=dx+brick[i][j].getWidth()+BRICK_SPACE;
			}
			dx=(WIN_WIDTH/2)-(5*BRICK_WIDTH);
			dy=dy+BRICK_HEIGHT+BRICK_SPACE;
			
			
		}
		
		

		
		
	}
	private void setPaddle(){//set paddle in middle of board
		
		paddle=new GRect(WIN_WIDTH/2-PADDLE_WIDTH/2,PADDLE_VERTICAL_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		add(paddle);
		
		
		
		
	}
	private void setBall(){//set ball in middle of board and paddle
		
		ball=new GOval(WIN_WIDTH/2-BALL_RADIUS,(PADDLE_VERTICAL_HEIGHT-brick[9][9].getY())/2,2*BALL_RADIUS,2*BALL_RADIUS);
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
		add(ball);
		
	}
	
	/*instance variables*/
	
	private GRect brick[][]=new GRect[11][11];	//bricks that make up board
	private GRect paddle;	//paddle to hit ball
	private GOval ball;
	private GObject collided_item;	//ball collides with this either brick or paddle
	private GLabel status;//states gameover
	private double vy=INIT_SPEED_DWN;//the constant speed across y axis
	private double vx=0;	//speed across x axis
	private RandomGenerator rgen=RandomGenerator.getInstance();// random variable to describe random motion over x axis
	private boolean gameOver=false;
}
