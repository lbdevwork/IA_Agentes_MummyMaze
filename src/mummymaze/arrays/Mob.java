package mummymaze.arrays;

public class Mob {
    public char getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private char type;
    private int x;
    private int y;

    public boolean getAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    private boolean action;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public void newSet(int x,int y){
        setX(x);
        setY(y);
    }

    public Mob(char type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }


}
