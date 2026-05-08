package com.name.moreThanYAC.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;

public class ModUsefulFunctions
{
    public static BlockPos calculateCanGenerateBlockPos(ServerLevel level,BlockPos originalPos, float radius, int requiredSpaceHeight){
        int posX = originalPos.getX();
        int posY = originalPos.getY();
        int posZ = originalPos.getZ();

        for (int i=0;i<=300;i++){
            BlockPos pos = new BlockPos(posX,posY,posZ);
            Block block = level.getBlockState(pos).getBlock();
            if (isSolid(block)) {
                if (calculateEmptyHeight(requiredSpaceHeight, pos, level)) {
                    return pos;
                }
            }
            posY++;
        }
        return originalPos;
    }
    private static boolean isSolid(Block block){
        return !block.defaultBlockState().isAir();
    }
    private static boolean isAir(Block block){
        return block.defaultBlockState().isAir();
    }
    private static boolean calculateEmptyHeight(int requiredHeight,BlockPos pos,ServerLevel level){
        int posY = pos.getY();
        for (int i=1;i<=requiredHeight+1;i++) {
            posY++;
            BlockPos pos1 = new BlockPos(pos.getX(),posY,pos.getZ());
            Block block = level.getBlockState(pos1).getBlock();
            if (!isAir(block)) return false;
        }
        return true;
    }
}
