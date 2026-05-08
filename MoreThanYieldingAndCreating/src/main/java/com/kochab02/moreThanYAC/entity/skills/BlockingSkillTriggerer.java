package com.name.moreThanYAC.entity.skills;

import com.name.moreThanYAC.MoreThanYAC;
import com.name.moreThanYAC.init.ModEnchantmentHandler;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

//this is a multi-player skill, designed to let players care for each other.
@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID)
public class BlockingSkillTriggerer {

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        Entity attacker = event.getSource().getDirectEntity();
        if (attacker == null) return;

        ServerLevel level = (ServerLevel) victim.level();
        AABB range = victim.getBoundingBox().inflate(5.0);
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, range,
                p -> p != victim && p.distanceTo(victim) <= 5);

        if (!nearbyPlayers.isEmpty()) {
            for (int i=0;i<nearbyPlayers.size();i++) {
                Player helper = null;
                BlockPos mid = victim.blockPosition();
                boolean canRemoteBlocking = false;
                for (Player helper1 : nearbyPlayers){
                    if (helper1.equals(attacker)) continue;
                    List<Boolean> helperBool = isInBlockStance(helper1);
                    if (!helperBool.get(0)) continue;

                    mid = getMediatePos(victim.blockPosition(), attacker.blockPosition());
                    helper = helper1;
                    canRemoteBlocking = helperBool.get(1);
                }

                if (helper == null) return;

                if (!canRemoteBlocking) helper.teleportTo(mid.getX() + 0.5, mid.getY(), mid.getZ() + 0.5);

                helper.lookAt(EntityAnchorArgument.Anchor.EYES, attacker.position());

                helper.sendSystemMessage(Component.literal("你挡住了对 " + victim.getName().getString() + " 的攻击！"));
                victim.sendSystemMessage(Component.literal(helper.getName().getString() + " 为你挡住了攻击！"));
                event.setCanceled(true);
            }
        }
    }

    private static BlockPos getMediatePos(BlockPos pos1, BlockPos pos2) {
        int x = (pos1.getX() + pos2.getX()) / 2;
        int y = (pos1.getY() + pos2.getY()) / 2;
        int z = (pos1.getZ() + pos2.getZ()) / 2;
        return new BlockPos(x, y, z);
    }

    private static List<Boolean> isInBlockStance(Player player) {
        List<Boolean> blockingPropertyList = new ArrayList<>();
        //0 for whether to activate blocking;1 for whether to remote
        if (!player.isCrouching()) blockingPropertyList.set(0,false);
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        blockingPropertyList.add(main.getItem() instanceof ShieldItem || off.getItem() instanceof ShieldItem);
        blockingPropertyList.add(main.getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_REMOTE_BLOCKING.get())>0 ||
                off.getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_REMOTE_BLOCKING.get())>0);
        return blockingPropertyList;
    }
}