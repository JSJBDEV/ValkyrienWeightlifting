package ace.actually.valkyrienweightlifting.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class ScaleItem extends Item {
    public ScaleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(!context.getWorld().isClient && context.getHand()== Hand.MAIN_HAND)
        {
            if(VSGameUtilsKt.isBlockInShipyard(context.getWorld(),context.getBlockPos()))
            {
                ServerShip ship = (ServerShip) VSGameUtilsKt.getShipManagingPos(context.getWorld(),context.getBlockPos());
                context.getPlayer().sendMessage(Text.of("This seems to weigh "+ship.getInertiaData().getMass()+"kg"));
            }
        }

        return super.useOnBlock(context);
    }
}
