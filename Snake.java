   import java.awt.Graphics;
   import java.awt.Color;
   import java.awt.Image;
   import java.awt.Font;
   import java.awt.event.KeyEvent;
   import java.util.Random;

    public class Snake{
       public static void main(String args[]){
         new Snake();
      }
    
    // general vars
      private final String VERSION = "0.1";
      private final boolean VERBOSE = true;
      private Window win;
      private Image gameImg;
      private Graphics gfx;
      private SnakeObject snake = null;
      private int gameStatus = -1;
      private Color COLOR_BROWN = null;
      private Font COMMON_FONT = null;
      private Font HEADER_FONT = null;
      private Font GAME_OVER_FONT = null;
      private Color COLOR_MAIN_MENU = null;
      private Color COLOR_MAIN_MENU_SELECTED = null;
      private boolean needsUpdate = false;
      private Random random = null;
      private int highScore = 0;
    
    // snake food
      private int foodX = 250;
      private int foodY = 250;
    
    // main menu
      private int curSel = 0;
      private final String[] menuOptions = {"Play Game", "Controls", "About"};
    // FPS
      private int fps = 0;
      private int fpsCounter = 0;
      private long lastSecond = 0; 
    
       public Snake(){
         System.out.println("Snake " + VERSION);
         COMMON_FONT = new Font(null, Font.BOLD, 12);
         HEADER_FONT = new Font(null, Font.PLAIN, 18);
         GAME_OVER_FONT = new Font(null, Font.BOLD, 40);
         COLOR_BROWN = new Color(143, 93, 6);
         COLOR_MAIN_MENU = new Color(0, 174, 238);
         COLOR_MAIN_MENU_SELECTED = new Color(236, 168, 8);
         random = new Random();
         win = new Window("Snake " + VERSION, 500, 500, false, this);//window
         gameImg = win.createImage(500, 500);
         gfx = gameImg.getGraphics();
         gameStatus = 0;
         snake = new SnakeObject();
         while(gameStatus > -1){//process the game
            try{
               drawCommon();
               if(gameStatus == 0){// Main Menu
                  showMainMenu();
               }
               else if(gameStatus == 1){//Main Game
                  processGame();
               }
               else if(gameStatus == 2){//Game Over
                  gameOver();
               }
               else if(gameStatus == 3){//show Options
                  showOptions();
               }
               else if(gameStatus == 4){//show about
                  showAbout();
               }
               if(gameStatus != 1){//dont kill the processor if we dont need it
                  try{
                     Thread.sleep(75);
                  }
                      catch(Exception e){}
               }
            }
                catch(Exception e){
                  e.printStackTrace();
               }        
            //update the screen
            win.updateGraphics(gameImg);
         }
         System.exit(0);
      }
    
    //Draw common things (background, text etc)
       private final void drawCommon() throws Exception{
         gfx.setColor(Color.BLACK);
         gfx.fillRect(0, 0, win.getWidth(), win.getHeight());
        
         gfx.setColor(Color.WHITE);
         gfx.setFont(COMMON_FONT);
        
         if(gameStatus == 0){
            gfx.drawString("Score: 0", 10, 40);
            gfx.drawString("Press `Enter` to make selection. Press `Escape` to exit.", 10, 60);
         }
         else if(gameStatus == 1){
            gfx.drawString("Score: " + snake.numFoodEaten(), 10, 40);
            gfx.drawString("Press `Escape` for main menu.", 10, 60);
            if(snake.numFoodEaten() > highScore)
               highScore = snake.numFoodEaten();
         }
         else if(gameStatus == 2){
            gfx.drawString("Score: " + snake.numFoodEaten(), 10, 40);
            gfx.drawString("Press `Enter` for main menu.", 10, 60);
         }
         else if(gameStatus > 2){
            gfx.drawString("Score: 0", 10, 40);
            gfx.drawString("Press `Enter` for main menu.", 10, 60);
         }
        
         gfx.drawString("High Score: " + highScore, 100, 40);
        
         if(System.currentTimeMillis() - lastSecond >= 1000){
            fps = fpsCounter;
            fpsCounter = 0;
            lastSecond = System.currentTimeMillis();
         }
         fpsCounter++;
         gfx.drawString("FPS: " + fps, 400, 40);
         gfx.setColor(Color.GRAY);  
         gfx.drawLine(0, 70, 501, 70);
         gfx.setColor(Color.WHITE);
      }
    
    //show main menu
       private final void showMainMenu() throws Exception{
         gfx.setFont(HEADER_FONT);
         gfx.setColor(Color.WHITE);
         gfx.drawString("Main Menu", 10, 86);
         gfx.setFont(COMMON_FONT);
         for(int i = 0; i < menuOptions.length; i++){
            if(!gfx.getColor().equals(COLOR_MAIN_MENU))
               gfx.setColor(COLOR_MAIN_MENU);
            if(curSel == i)
               gfx.setColor(COLOR_MAIN_MENU_SELECTED);
            gfx.drawString(menuOptions[i], 25, 100 + i * 14);
         }
      }
    
    //process the main game
       private final void processGame() throws Exception{        
         drawFood();
         snake.move(needsUpdate);
         if(needsUpdate)
            needsUpdate = false;
         drawSnake();
        //did the snake run over it self?
         SnakeBit[] sb = snake.getBits();
         for(int c = 1; c < sb.length; c++){
            if(distanceTo(sb[c].getX() + 5, sb[c].getY() + 5, sb[0].getX() + 5, sb[0].getY() + 5) < 7)
               gameStatus = 2;
         }
      }
    
    //show the game over screen
       private final void gameOver(){
         drawFood();
         if(needsUpdate)
            needsUpdate = false;
         drawSnake();
         gfx.setFont(GAME_OVER_FONT);
         gfx.setColor(Color.WHITE);
         gfx.drawString("Game Over!", 130, 125);
         gfx.setFont(COMMON_FONT);
      }
    
       private final void showOptions(){
         gfx.drawString("Not Implemented Yet", 130, 130);
      }
    
       private final void showAbout(){
         gfx.drawString("Not Implemented Yet", 130, 130);
      }
    
       private final void drawFood(){
         gfx.setColor(Color.RED);
         gfx.fillOval(foodX, foodY, 10, 10);
         gfx.setColor(Color.WHITE);
      }
    
       private final void drawSnake(){
         SnakeBit[] sb = snake.getBits();
         gfx.setColor(COLOR_BROWN);
         for(int i = sb.length - 1; i >= 0; i--){
            if(i == 0){
               gfx.setColor(Color.YELLOW);
                //add 5 to each coord so we get the center of each square
               if(distanceTo(sb[0].getX() + 5, sb[0].getY() + 5, foodX + 5, foodY + 5) < 7){
                    //HACK - Need to make it check to see if the new food coords are already being used by the snake....
                  snake.ateFood();
                  foodX = rand(0, 491);
                  foodY = rand(70, 491);
                    //put the food on the same grid pattern as the snake
                  foodX -= foodX % 10;
                  foodY -= foodY % 10;
               }
            }
            gfx.fillRect(sb[i].getX(), sb[i].getY(), 10, 10);
         }
      }
    
       public final void keyTyped(int code){
      //        System.out.println("Key Pressed: " + code);
         if(gameStatus == 0){//main menu
            if(code == KeyEvent.VK_ENTER){
               if(curSel == 0){ //Single Player Mode 
                  snake = new SnakeObject();
                  foodX = 400;
                  foodY = 400;
                  gameStatus = 1;
                  needsUpdate = true;
                  return;
               }
               else if(curSel == 1){
                  gameStatus = 3;
               }
               else if(curSel == 2){
                  gameStatus = 4;
               }
            }
            else if(code == KeyEvent.VK_UP){
               curSel = curSel < 1 ? 0 : curSel - 1;
            }
            else if(code == KeyEvent.VK_DOWN){
               curSel = curSel >= menuOptions.length - 1 ? curSel : curSel + 1;
            }
            else if(code == KeyEvent.VK_ESCAPE){
               gameStatus = -1;
            }
         }
         else if(gameStatus == 1){//main game
            if(code == KeyEvent.VK_UP)
               snake.setDirection(SnakeObject.NORTH);
            if(code == KeyEvent.VK_RIGHT)
               snake.setDirection(SnakeObject.EAST);
            if(code == KeyEvent.VK_LEFT)
               snake.setDirection(SnakeObject.WEST);
            if(code == KeyEvent.VK_DOWN)
               snake.setDirection(SnakeObject.SOUTH);
            if(code == KeyEvent.VK_ESCAPE)
               gameStatus = 0;
            if(!needsUpdate)
               needsUpdate = true;
         }
         else if(gameStatus == 2){
            if(code == KeyEvent.VK_ENTER)
               gameStatus = 0;
         }
         else if(gameStatus == 3){
            if(code == KeyEvent.VK_ENTER)
               gameStatus = 0;
         }
         else if(gameStatus == 4){
            if(code == KeyEvent.VK_ENTER)
               gameStatus = 0;
         }
      }
   
    //returns distance between two sets of coords.
       private int distanceTo(int x1, int y1, int x2, int y2){
         double tx = Math.pow(Math.abs(x1 - x2), 2);
         double ty = Math.pow(Math.abs(y1 - y2), 2);
         return (int)Math.sqrt(tx + ty);
      }   
    
    //returns a random number between the two values supplied.
       private final int rand(int low, int high){
         return random.nextInt(high - low + 1) + low;
      }
    
       public void log(String s){
         if(VERBOSE)
            System.out.println(s + "\n-");
      }
   }