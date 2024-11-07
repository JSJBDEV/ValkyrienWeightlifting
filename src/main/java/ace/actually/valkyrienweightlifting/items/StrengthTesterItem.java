package ace.actually.valkyrienweightlifting.items;

import ace.actually.valkyrienweightlifting.IGymBro;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class StrengthTesterItem extends Item {
    public StrengthTesterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient && hand==Hand.MAIN_HAND)
        {
            IGymBro bro = (IGymBro) user;
            user.sendMessage(Text.of("I think I can lift "+bro.getStrength()+"kg"));
        }
        return super.use(world, user, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
}
