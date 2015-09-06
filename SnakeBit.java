public class SnakeBit extends Object implements Cloneable{
    public SnakeBit(int x, int y, SnakeObject so){
        this.parent = so;
        this.bitX = x;
        this.bitY = y;
    }
    
    public SnakeObject getParent(){
        return parent;
    }
    
    public final int getX(){
        return bitX;
    }
    
    public final int getY(){
        return bitY;
    }
    
    public final void setCoords(int x, int y){
        bitX = x;
        bitY = y;
    }
    
    public final SnakeBit copy(){
        try{
            return (SnakeBit)super.clone();
        }catch(CloneNotSupportedException e){
            return new SnakeBit(bitX, bitY, parent);
        }
    }
    
    private SnakeObject parent;
    private int bitX = -1;
    private int bitY = -1;
}