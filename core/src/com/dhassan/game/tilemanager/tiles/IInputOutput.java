package com.dhassan.game.tilemanager.tiles;

import com.dhassan.game.item.ItemStack;

public interface IInputOutput {

    /**
     * Add an ItemStack into inventory of this
     * @param in ItemStack in
     */
    void in(ItemStack in);

    /**
     * Remove ItemStack from inventory of this
     * @param out ItemStack out
     * @param in Target inventory
     */
    void out(ItemStack out, IInputOutput in);

    /**
     * Open inventory
     */
    void open();

    boolean isFull();


}
