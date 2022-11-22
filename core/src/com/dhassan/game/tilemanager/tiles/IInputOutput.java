package com.dhassan.game.tilemanager.tiles;

import com.dhassan.game.item.ItemStack;

public interface IInputOutput {

    void in(ItemStack in);

    void out(ItemStack out, IInputOutput in);

    void open();

    boolean isFull();


}
