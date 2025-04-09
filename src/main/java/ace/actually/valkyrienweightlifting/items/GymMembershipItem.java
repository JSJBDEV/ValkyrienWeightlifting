package ace.actually.valkyrienweightlifting.items;

import ace.actually.valkyrienweightlifting.IGymBro;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GymMembershipItem extends Item {
    public GymMembershipItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient && hand==Hand.MAIN_HAND)
        {
            IGymBro bro = (IGymBro) user;
            if(user.isSneaking())
            {
                if(user.getMainHandStack().hasNbt())
                {
                    NbtCompound compound = user.getMainHandStack().getNbt();
                    if(compound.getString("uuid").equals(user.getUuidAsString()) && user.getOffHandStack().isOf(Items.EMERALD))
                    {
                        bro.setStrength(compound.getDouble("strength"));
                        user.getOffHandStack().decrement(1);
                        user.sendMessage(Text.translatable("gym.membership.load"));
                    }
                }
            }
            else
            {

                NbtCompound compound = user.getMainHandStack().getOrCreateNbt();
                compound.putString("uuid",user.getUuidAsString());
                compound.putDouble("strength",bro.getStrength());
                user.sendMessage(Text.translatable("gym.membership.save"));
            }

        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("gym.save_explain"));
        tooltip.add(Text.translatable("gym.need_emeralds"));
    }
}
