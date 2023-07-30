package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntityShulkerBox;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.BlockEventPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;

public class ShulkerBoxInventory extends ContainerInventory {

    public ShulkerBoxInventory(BlockEntityShulkerBox box) {
        super(box, InventoryType.SHULKER_BOX);
    }

    @Override
    public BlockEntityShulkerBox getHolder() {
        return (BlockEntityShulkerBox) this.holder;
    }

    @Override
    public void onOpen(Player who) {
        super.onOpen(who);

        if (this.getViewers().size() == 1) {
            BlockEventPacket pk = new BlockEventPacket();
            pk.x = (int) this.getHolder().getX();
            pk.y = (int) this.getHolder().getY();
            pk.z = (int) this.getHolder().getZ();
            pk.eventType = 1;
            pk.eventData = 1;

            Level level = this.getHolder().getLevel();
            if (level != null) {
                level.addLevelSoundEvent(this.getHolder().add(0.5, 0.5, 0.5), LevelSoundEventPacket.SOUND_SHULKERBOX_OPEN);
                level.addChunkPacket((int) this.getHolder().getX() >> 4, (int) this.getHolder().getZ() >> 4, pk);
            }
        }
    }

    @Override
    public void onClose(Player who) {
        if (this.getViewers().size() == 1) {
            BlockEventPacket pk = new BlockEventPacket();
            pk.x = (int) this.getHolder().getX();
            pk.y = (int) this.getHolder().getY();
            pk.z = (int) this.getHolder().getZ();
            pk.eventType = 1;
            pk.eventData = 0;

            Level level = this.getHolder().getLevel();
            if (level != null) {
                level.addLevelSoundEvent(this.getHolder().add(0.5, 0.5, 0.5), LevelSoundEventPacket.SOUND_SHULKERBOX_CLOSED);
                level.addChunkPacket((int) this.getHolder().getX() >> 4, (int) this.getHolder().getZ() >> 4, pk);
            }
        }

        super.onClose(who);
    }

    @Override
    public boolean canAddItem(Item item) {
        return allowedToAdd(item) && super.canAddItem(item);
    }

    @Override
    public boolean allowedToAdd(Item item) {
        // Do not allow nested shulker boxes
        return item.getId() != BlockID.SHULKER_BOX && item.getId() != BlockID.UNDYED_SHULKER_BOX;
    }
}
