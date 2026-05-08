package com.name.moreThanYAC.event;

//this class tries to combine the event listeners into one so that their order can be regulated

import com.name.moreThanYAC.MoreThanYAC;
import com.name.moreThanYAC.init.ModEnchantmentHandler;
import com.name.moreThanYAC.utils.FunctionCalculator;
import com.name.moreThanYAC.utils.ModConfig;
import com.name.moreThanYAC.utils.ParticleGenerator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.Random;

import static com.name.moreThanYAC.utils.EntityCategoryJudgement.isHostile;

//Attack--当机立断，决定是否击杀,如果击杀，原始伤害值被调至0,一个巨额伤害被存储--
//Hurt--除恶务尽，如果触发，增加a的基础伤害--从天而降，如果触发，则伤害值乘以b--高地/低地/暴击机制，伤害值乘以c
//--避实击虚，一部分伤害被存储，另一部分正常计算，等效为乘以d--
//Damage--当机立断，如果触发，增加一个巨额伤害予以秒杀--避实击虚，如果触发，Damage值增加x（取决于Hurt的结果）--
//Death--除恶务尽，根据被击杀的生物是否符合要求，判断是否触发效果
//最终伤害值，设基础伤害是y，则最终造成的伤害为：{[(y+a)*b*c*d] -> armor+potion+absorption etc.}+(y+a)*b*c*(1-d)其中后一项相当于真伤

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID)
public class GeneralEventHandler
{
    private static final String instant_kill_damage_key = "MYC_decisiveStrike_key";
    private static final String increasing_damage_key = "MYC_totalEradication_key";
    private static final String bypasses_armor_damage_key = "MYC_weaknessFocus_key";

    @SubscribeEvent
    public static void onCreatureAttack(LivingAttackEvent event){
        LivingEntity target = event.getEntity();
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        var source = event.getSource();
        Random random = new Random(System.currentTimeMillis());
        if (attacker.level().isClientSide()) return;


        //当机立断,needed to be considered first for its life-based instant kill tech
        if (attacker instanceof Player){
            InteractionHand hand = attacker.getUsedItemHand();
            ItemStack item = attacker.getItemInHand(hand);
            int level = item.getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_DECISIVE_STRIKE.get());
            if (level>0){
                //only instant killing mechanics will be considered here,for its additional damage is basic as sharpness.
                float killingPoint = Math.min(0.05f + level * 0.05f, 1f);
                int killingPossibility = Math.min(5 + level * 2, 100); //percentage
                if (target.getHealth() <= target.getMaxHealth() * killingPoint) {
                    if (random.nextInt(100) < killingPossibility) {
                        //instant kill is activated
                        CompoundTag tag = target.getPersistentData();
                        tag.putFloat(instant_kill_damage_key,target.getMaxHealth()*10f);
                        try {
                            Field amountField = LivingAttackEvent.class.getDeclaredField("amount");
                            amountField.setAccessible(true);
                            amountField.setFloat(event, 0.0f);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                        //killed, avoid other logics
                    }
                }
            }
        }

        /*//bottom priority,实事求是
        if (ModConfig.ENABLE_SHISHIQIUSHI.get() && attacker instanceof Player){
            InteractionHand hand = attacker.getUsedItemHand();
            ItemStack item = attacker.getItemInHand(hand);
            if (item.getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_SHISHIQIUSHI.get())>0){
                CompoundTag tag = target.getPersistentData();
                tag.putFloat("shishiqiushi_damage",event.getAmount());
                try {
                    Field amountField = LivingAttackEvent.class.getDeclaredField("amount");
                    amountField.setAccessible(true);
                    amountField.setFloat(event, 0.0f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    @SubscribeEvent
    public static void onCreatureHurt(LivingHurtEvent event){
        LivingEntity target = event.getEntity();
        float index=1.0f;
        float moreDamage=0;
        CompoundTag targetTag = target.getPersistentData();
        Random random = new Random(System.currentTimeMillis());
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        if (attacker.level().isClientSide()) return;

        //除恶务尽
        if ((event.getSource().getEntity() instanceof Player && attacker.getTags().contains(increasing_damage_key))){
            ItemStack item = attacker.getItemInHand(attacker.getUsedItemHand());
            int level = item.getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_TOTAL_ERADICATION.get());
            if (level > 0){
                if (isHostile(target)){
                    moreDamage += 0.2f*level*target.getHealth();
                }
                attacker.removeTag(increasing_damage_key);
            }
        }
        event.setAmount(event.getAmount()+moreDamage);
        moreDamage=0;
        //从天而降
        if (attacker instanceof Player && attacker.getItemInHand(attacker.getUsedItemHand())
                .getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_HEAVEN_STRIKE.get())>0){
            int level = attacker.getItemInHand(attacker.getUsedItemHand())
                    .getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_HEAVEN_STRIKE.get());
            if (attacker.fallDistance>0){
                float distance = attacker.fallDistance;
                float distanceIndex = Math.min((int) distance/10,10);
                distanceIndex *= (0.05f*level);
                attacker.resetFallDistance();
                index *= distanceIndex;
            }
        }
        event.setAmount(event.getAmount()*index);
        index=1;
        //高地优势和低地劣势
        event.setAmount(FunctionCalculator.onGeneralHurt(event.getAmount(),target,attacker));
        //避实击虚
        float remainingDamageIndex=1.0f;
        if (attacker instanceof Player && !targetTag.contains(bypasses_armor_damage_key)){
            InteractionHand hand = attacker.getUsedItemHand();
            ItemStack item = attacker.getItemInHand(hand);
            int level = item.getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_WEAKNESS_FOCUS.get());
            if(level>0){
                float damage = event.getAmount();
                float armor = target.getArmorValue();
                float transferredDamage = Math.min(0.02f*level*armor,1.0f)*damage;
                remainingDamageIndex = Math.max(0.0f,damage-transferredDamage)/damage;
                targetTag.putFloat(bypasses_armor_damage_key,transferredDamage);
            }
        }
        event.setAmount(event.getAmount()*remainingDamageIndex);
        //前功尽弃,攻击
        if (attacker instanceof Player victim && !victim.level().isClientSide()){
            ItemStack item = victim.getItemInHand(victim.getUsedItemHand());
            if (!(item.getItem() instanceof DiggerItem || item.getItem() instanceof SwordItem)) return;
            if (item.getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_BACK_TO_SQUARE_ONE.get())>0){
                if (target.getHealth()<=target.getMaxHealth()*0.1f){
                    if (ModConfig.ENABLE_COMPENSATION.get()) {
                        victim.addEffect(new MobEffectInstance(
                                MobEffects.DAMAGE_BOOST, 100, 0, true, true));
                    }
                    if (random.nextInt(100)<70) {
                        target.setHealth(Math.max(target.getMaxHealth()*0.5f,target.getHealth()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCreatureDamage(LivingDamageEvent event){
        LivingEntity victim = event.getEntity();
        CompoundTag victimTag = victim.getPersistentData();
        var source = event.getSource();
        if (victim.level().isClientSide()) return;
        float moreDamage=0.0f;
        float damageIndex=1.0f;

        //当机立断
        if (victimTag.contains(instant_kill_damage_key)){
            float damage = victimTag.getFloat(instant_kill_damage_key);
            moreDamage+=damage;
            victim.setHealth(0);
            victimTag.remove(instant_kill_damage_key);
            ParticleGenerator.generateBurstParticles(victim,ParticleTypes.EXPLOSION,0.1,2,0.5);
        }
        //避实击虚
        if (source.getEntity() instanceof Player){
            if (victimTag.contains(bypasses_armor_damage_key)){
                float damage = victimTag.getFloat(bypasses_armor_damage_key);
                victimTag.remove(bypasses_armor_damage_key);
                moreDamage+=damage;
            }
        }
        //实事求是
        /*if (victimTag.contains("shishiqiushi_damage") && ModConfig.ENABLE_SHISHIQIUSHI.get()){
            float damage = victimTag.getFloat("shishiqiushi_damage");
            moreDamage+=damage;
            victimTag.remove("shishiqiushi_damage");
        }*/

        event.setAmount(event.getAmount()*damageIndex+moreDamage);
    }

    @SubscribeEvent
    public static void onCreatureDeath(LivingDeathEvent event){
        if (!(event.getEntity() instanceof LivingEntity)) return;
        var source = event.getSource();
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) return;
        if (source.getEntity() instanceof Player attacker && !attacker.level().isClientSide()){
            ItemStack item = attacker.getItemInHand(attacker.getUsedItemHand());
            if (item.getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_TOTAL_ERADICATION.get())>0) {
                if (isHostile(entity)) {
                    if (!attacker.getTags().contains(increasing_damage_key)){
                        attacker.addTag(increasing_damage_key);
                        ParticleGenerator.generateBurstParticles(attacker, ParticleTypes.ANGRY_VILLAGER,0.8,
                                2,0.3);
                    }
                }
            }
        }
    }
}
