import java.util.Vector;
import java.util.NoSuchElementException;

public class SnakeObject extends Object{
    public SnakeObject(){
        createSnake(snakeX, snakeY, 3);
    }
    
    public SnakeObject(int x, int y){
        createSnake(x, y, 3);
    }
    
    public SnakeObject(int x, int y, int len){
        createSnake(x, y, len);
    }
    
    private Vector<SnakeBit> snakeBits;
    private int snakeX = 20;
    private int snakeY = 100;
    private int curDir = SnakeObject.SOUTH;
    private long lastMove;    
    private int foodAte;
    
    //Possible Directions
    public static int NORTH = 0;
    public static int EAST = 1;
    public static int WEST = 2;
    public static int SOUTH = 3;
    
    private final void createSnake(int x, int y, int len){
        snakeBits = new Vector<SnakeBit>();
        snakeX = x;
        snakeY = y;
        foodAte = 0;
        for(int i = 0; i < len; i++)
            addBit();
    }
    
    public void addBit(){
        SnakeBit nBit = null;
        try{
            nBit = snakeBits.firstElement().copy();
        }catch(NoSuchElementException e){
            nBit = new SnakeBit(snakeX, snakeY, this);
        }
        if(curDir == SnakeObject.NORTH){// 0
            nBit.setCoords(nBit.getX(), nBit.getY() - 10);
        }else if(curDir == SnakeObject.EAST){// 1
            nBit.setCoords(nBit.getX() + 10, nBit.getY());
        }else if(curDir == SnakeObject.WEST){// 2
            nBit.setCoords(nBit.getX() - 10, nBit.getY());
        }else if(curDir == SnakeObject.SOUTH){// 3
            nBit.setCoords(nBit.getX(), nBit.getY() + 10);
        }else{//Shouldnt ever happen....
            System.out.println("Invalid Snake Direction");
            return;
        }
        snakeBits.insertElementAt(nBit, 0);
    }
    
    public void move(boolean needsUpdate){
        try{
            if(System.currentTimeMillis() - lastMove < 50 && !needsUpdate)
                return;
            SnakeBit[] sb = getBits();
            for(int i = sb.length - 1; i >= 0; i--){
                if(i > 0){//update the body
                    sb[i].setCoords(sb[i - 1].getX(), sb[i - 1].getY());//use the coords infront of it.
                }else{//move the head of the snake
                    if(i < 0) //extra check
                        continue;
                    if(curDir == SnakeObject.NORTH){// 0
                        sb[i].setCoords(sb[i].getX(), sb[i].getY() - 10 > 69 ? sb[i].getY() - 10 : 490);
                    }else if(curDir == SnakeObject.EAST){// 1
                        sb[i].setCoords(sb[i].getX() + 10 > 490 ? 0 : sb[i].getX() + 10, sb[i].getY());
                    }else if(curDir == SnakeObject.WEST){// 2
                        sb[i].setCoords(sb[i].getX() - 10 > -1 ? sb[i].getX() - 10 : 490, sb[i].getY());
                    }else if(curDir == SnakeObject.SOUTH){// 3
                        sb[i].setCoords(sb[i].getX(), sb[i].getY() + 10 < 491 ? sb[i].getY() + 10 : 70);
                    }else{//Shouldnt ever happen....
                        return;
                    }
                }
            }
            lastMove = System.currentTimeMillis();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public final SnakeBit[] getBits(){
        SnakeBit[] sb = new SnakeBit[snakeBits.size()];
        snakeBits.toArray(sb);
        return sb;
    }
    
    public void setDirection(int dir){
//        System.out.println("Direction set to: " + dir);
        if(curDir == SnakeObject.NORTH && dir == SnakeObject.SOUTH)
            return;
        if(curDir == SnakeObject.SOUTH && dir == SnakeObject.NORTH)
            return;
        if(curDir == SnakeObject.EAST && dir == SnakeObject.WEST)
            return;
        if(curDir == SnakeObject.WEST && dir == SnakeObject.EAST)
            return;
        curDir = dir;
    }
    
    // returns true if the specified coords are currently being used by this snake.
    public final boolean isUsingCoords(int x, int y){
        //put the coords on same grid as snake
        x -= x % 10;
        y -= y % 10;
        //set the coords to be the center of the 10pixel squares
        x += 5;
        y += 5;
        //match the coords up
        SnakeBit[] sb = getBits();
        for(int i = 0; i < sb.length; i++){
            if(distanceTo(sb[i].getX() + 5, sb[i].getY() + 5, x, y) < 7)
                return true;
        }
        return false;        
    }
    
    public final void ateFood(){
        addBit();
        foodAte++;
    }
    
    public final int numFoodEaten(){
        return foodAte;
    }
    
    //returns distance between two sets of coords.
    private int distanceTo(int x1, int y1, int x2, int y2){
        double tx = Math.pow(Math.abs(x1 - x2), 2);
        double ty = Math.pow(Math.abs(y1 - y2), 2);
        return (int)Math.sqrt(tx + ty);
    }   
}