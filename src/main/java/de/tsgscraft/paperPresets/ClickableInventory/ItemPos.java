package de.tsgscraft.paperPresets.ClickableInventory;

public class ItemPos {

    private final int index;
    private final int x;
    private final int y;

    public ItemPos(int x, int y){
        index = ((y+1)*9-9) + (x);
        this.x = x;
        this.y = y;
    }

    public ItemPos(int index){
        this.index = index;

        this.x = index % 9;
        this.y = (int) Math.floor(index / 9.0);
    }

    public int getAsIndex(){
        return index;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
