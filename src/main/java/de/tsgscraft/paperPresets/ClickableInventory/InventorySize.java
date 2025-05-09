package de.tsgscraft.paperPresets.ClickableInventory;

public enum InventorySize {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6);

    final int number;

    InventorySize(int number){
        this.number = number;
    }

    Integer getAsInt(){
        return number;
    }
}
