package rogue.controller.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rogue.model.creature.Player;
import rogue.model.items.Item;
import rogue.model.items.inventory.Inventory;
import rogue.model.items.inventory.OutOfInventoryException;
import rogue.model.items.inventory.ScrollContainer;
import rogue.model.items.scroll.Scroll;

/**
 * An implementation of a {@link InventoryController}.
 *
 */
public class InventoryControllerImpl implements InventoryController {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryController.class);
    private final Player player;

    public InventoryControllerImpl(final Player player) {
        this.player = player;
    }

    /**
     * Checks if the player is dead.
     * @return true if the player is dead, false otherwise.
     */
    private boolean isDead() {
        return this.player.getLife().getHealthPoints() == 0;
    }
    /**
     * Gives the updated number for the Model inventory.
     * @param col
     * @param row
     * @return index for the model inventory.
     */
    public int indexConv(final int col, final int row) {
        return row * 4 + col + 1;
    }


    /**
     * Event triggered when the player clicks on a inventory slot
     * with mouse button 1.
     * @param col of the clicked slot.
     * @param row of the clicked slot.
     * @return true if the item was correctly used, false otherwise.
     */
    public boolean onPrimaryClick(final int col, final int row) {
        if (!isDead()) {
            try {
                if (player.getInventory().getItem(indexConv(col, row)).isPresent()) {
                    if (!player.getInventory().useItem(indexConv(col, row))) {
                        LOG.info("Cannot use item: " + player.getInventory().getItem(indexConv(col, row)).get().toString() + ".");
                        return false;
                    }
                    LOG.info("Item correctly used.");
                    return true;
                }
            } catch (OutOfInventoryException e) {
                LOG.info("Called useItem with invalid Index.");
            }
        }
        return false;
    }

    /**
     * Event triggered when the player clicks on a inventory slot
     * with mouse button 2.
     * @param col of the clicked slot.
     * @param row of the clicked slot.
     * @return true if the item was correctly used, false otherwise.
     */
    public boolean onSecondaryClick(final int col, final int row) {
        if (!isDead()) {
            try {
                if (player.getInventory().getItem(indexConv(col, row)).isPresent()) {
                    if (!player.getInventory().remove(indexConv(col, row))) {
                        LOG.info("Cannot remove item: " + player.getInventory().getItem(indexConv(col, row)).get().toString() + ".");
                        return false;
                    }
                    LOG.info("Item correctly removed.");
                    return true;
                }
            } catch (OutOfInventoryException e) {
                LOG.info("Called removeItem with invalid Index");
            }
        }
        return false;
    }

    /**
     * Event triggered when the player clicks on a inventory slot
     * with middle mouse button.
     * @param col of the clicked slot.
     * @param row of the clicked slot.
     * @param swapping index of the swapping slot.
     * @return true if the item was correctly used, false otherwise.
     */
    public boolean onMiddleClick(final int col, final int row, final int swapping) {
        if (!isDead()) {
            try {
                if (!player.getInventory().swap(swapping, indexConv(col, row))) {
                    LOG.info("Cannot swap items");
                    return false;
                }
                LOG.info("Swap correctly executed.");
                return true;
            } catch (OutOfInventoryException e) {
                LOG.info("Called swap with invalid Indexes.");
            }
        }
        return false;
    }

    /**
     * @return true if the ring was correctly removed, false otherwise.
     */
    public boolean onRingContainer() {
        if (!isDead()) {
            if (player.getEquipment().getRing().isPresent()) {
                /*
                 * Remove the ring and update the inventory.
                 */
                player.getEquipment().getRing().get().stopUsing(player);
                player.getInventory().updateInventory();
                LOG.info("Ring correctly removed.");
                return true;
            }
            /*
             * No active ring nothing to update.
             */
            LOG.info("No active Ring to remove.");
        }
        return false;
    }

    /**
     * Return the item in the slot, null if empty.
     * @param col of the clicked slot.
     * @param row of the clicked slot.
     * @return the item contained in the given index.
     */
    public Item getItem(final int col, final int row) {
        try {
            if (player.getInventory().getItem(indexConv(col, row)).isPresent()) {
                return player.getInventory().getItem(indexConv(col, row)).get();
            }
        } catch (OutOfInventoryException e) {
            LOG.info("Called getItem with invalid Indexes.");
        }
        return null;
    }

    /**
     * Return the item in the slot, null if empty.
     * @param col of the clicked slot.
     * @param row of the clicked slot.
     * @return the item contained in the given index.
     */
    public int getAmount(final int col, final int row) {
        try {
            if (player.getInventory().getItem(indexConv(col, row)).isPresent()) {
                return player.getInventory().getAmount(indexConv(col, row));
            }
        } catch (OutOfInventoryException e) {
            LOG.info("Called getItem with invalid Indexes.");
        }
        return 0;
    }

    /**
     * Return the currently active scroll.
     * @return active scroll, null if empty.
     */
    public Scroll getActiveScroll() {
        if (player.getInventory().getScrollContainer().getActiveScroll().isPresent()) {
            return player.getInventory().getScrollContainer().getActiveScroll().get();
        }
        return null;
    }

    /**
     * Get the currently active scroll duration.
     * @return currently active scroll remaining turns, 0 otherwise.
     */
    public int getActiveScrollDuration() {
        if (player.getInventory().getScrollContainer().getActiveScroll().isPresent()) {
            return player.getInventory().getScrollContainer().getActiveScrollDuration();
        }
        return 0;
    }

    /**
     * Check if there's an active ring.
     * @return true if there's an active ring, false otherwise.
     */
    public boolean checkActiveRing() {
        return player.getEquipment().getRing().isPresent();
    }

    /**
     * Get the player's inventory.
     * @return the player's inventory.
     */
    public Inventory getInventory() {
        return player.getInventory();
    }

    /**
     * Get the player's scroll container.
     * @return the player's scroll container
     */
    public ScrollContainer getScrollContainer() {
        return player.getInventory().getScrollContainer();
    }
}