package com.name.moreThanYAC.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig
{
    public static final ForgeConfigSpec.BooleanValue ENABLE_COMPENSATION;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SHISHIQIUSHI;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SANGJINTIANLIANG;
    public static final ForgeConfigSpec.BooleanValue ENABLE_HIGH_GROUND_ADVANTAGE;
    public static final ForgeConfigSpec.DoubleValue HIGH_GROUND_ADVANTAGE_DAMAGE_INDEX;
    public static final ForgeConfigSpec.DoubleValue LOWER_GROUND_DISADV_DAMAGE_INDEX;
    public static final ForgeConfigSpec.BooleanValue ENABLE_GENERAL_CRITICAL;
    
    public static final ForgeConfigSpec.BooleanValue ENABLE_SURGE_PARTICLES;


    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec CLIENT_CONFIG;

    static {
        Pair<CommonConfig,ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_CONFIG = commonPair.getRight();

        Pair<ClientConfig, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_CONFIG = clientPair.getRight();

        ENABLE_COMPENSATION = commonPair.getLeft().enableCompensation;
        ENABLE_SHISHIQIUSHI = commonPair.getLeft().enableShiShiQiuShi;
        ENABLE_SANGJINTIANLIANG = commonPair.getLeft().enableSangJinTianLiang;
        ENABLE_HIGH_GROUND_ADVANTAGE = commonPair.getLeft().enableHighGroundAdvantage;
        HIGH_GROUND_ADVANTAGE_DAMAGE_INDEX = commonPair.getLeft().highGroundAdvantageDamageIndex;
        LOWER_GROUND_DISADV_DAMAGE_INDEX = commonPair.getLeft().lowerGroundDisadvDamageIndex;
        ENABLE_GENERAL_CRITICAL = commonPair.getLeft().enableGeneralCritical;
        ENABLE_SURGE_PARTICLES = clientPair.getLeft().enableSurgeParticles;
    }

    private static class CommonConfig{
        public final ForgeConfigSpec.BooleanValue enableCompensation;
        public final ForgeConfigSpec.BooleanValue enableShiShiQiuShi;
        public final ForgeConfigSpec.BooleanValue enableSangJinTianLiang;
        public final ForgeConfigSpec.BooleanValue enableHighGroundAdvantage;
        public final ForgeConfigSpec.DoubleValue highGroundAdvantageDamageIndex;
        public final ForgeConfigSpec.DoubleValue lowerGroundDisadvDamageIndex;
        public final ForgeConfigSpec.BooleanValue enableGeneralCritical;

        public CommonConfig(ForgeConfigSpec.Builder builder){
            builder.push("enchantments");
            enableCompensation = builder.comment("Whether to enable to give players that activates 前功尽弃 a compensation.")
                    .define("enableCompensation",true);
            enableShiShiQiuShi = builder.comment("Whether to enable the enchantment \"实事求是\"" )
                    .define("enableShiShiQiuShi",false);
            builder.pop();

            builder.push("features");
            enableSangJinTianLiang = builder.comment("Whether to enable to punish the player for killing babies by effect\"丧尽天良\"")
                    .define("enableSangJinTianLiang",true);
            enableGeneralCritical = builder.comment("Whether to enable the general critical attack.")
                    .define("enableGeneralCritical",true);
            enableHighGroundAdvantage = builder.comment("Whether to enable an advantage in battle for those on the higher ground.")
                    .define("enableHighGroundAdvantage",true);

                builder.push("highGroundAdvantageConfigs");
                highGroundAdvantageDamageIndex = builder
                        .comment("Define the index for damage bonus.(Damage = Original * (1+index)")
                        .defineInRange("highGroundAdvantageDamageIndex"
                        ,0.5d,0.01d,10.0d);
                lowerGroundDisadvDamageIndex = builder
                        .comment("Define the index for damage disadvantage.(Damage = Original * index)")
                        .defineInRange("lowerGroundDisadvantageDamageIndex"
                                , 0.5d,0.01d,1.0d);

        }
    }
    private static class ClientConfig {
        public final ForgeConfigSpec.BooleanValue enableSurgeParticles;
        
        public ClientConfig(ForgeConfigSpec.Builder builder){
             builder.push("client options");
             enableSurgeParticles = builder.comment("Whether to enable the constant particles for surge enemies.")
                    .define("enableSurgeParticles",true);
        }
    }
}
