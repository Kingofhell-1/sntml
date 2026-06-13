package com.sntml.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class AltarPiece extends StructurePiece {

    public static StructurePieceType TYPE;

    private final int originX;
    private final int originY;
    private final int originZ;

    public AltarPiece(BlockPos pos) {
        super(TYPE, 0, new BoundingBox(
                pos.getX() - 3, pos.getY(), pos.getZ() - 3,
                pos.getX() + 3, pos.getY() + 5, pos.getZ() + 3
        ));
        this.originX = pos.getX();
        this.originY = pos.getY();
        this.originZ = pos.getZ();
    }

    public AltarPiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
        super(TYPE, tag);
        this.originX = tag.getInt("ox");
        this.originY = tag.getInt("oy");
        this.originZ = tag.getInt("oz");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
        tag.putInt("ox", originX);
        tag.putInt("oy", originY);
        tag.putInt("oz", originZ);
    }

    private void place(WorldGenLevel level, BoundingBox box, BlockPos p, net.minecraft.world.level.block.state.BlockState s) {
        if (box.isInside(p)) level.setBlock(p, s, 2);
    }

    @Override
    public void postProcess(WorldGenLevel level,
                            StructureManager structureManager,
                            ChunkGenerator generator,
                            RandomSource random,
                            BoundingBox box,
                            ChunkPos chunkPos,
                            BlockPos pos) {

        // Основание 5x5
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                place(level, box, new BlockPos(originX + dx, originY, originZ + dz),
                        Blocks.COBBLESTONE.defaultBlockState());
            }
        }

        // Столбы по углам
        for (int dy = 1; dy <= 4; dy++) {
            place(level, box, new BlockPos(originX - 2, originY + dy, originZ - 2), Blocks.STONE_BRICKS.defaultBlockState());
            place(level, box, new BlockPos(originX + 2, originY + dy, originZ - 2), Blocks.STONE_BRICKS.defaultBlockState());
            place(level, box, new BlockPos(originX - 2, originY + dy, originZ + 2), Blocks.STONE_BRICKS.defaultBlockState());
            place(level, box, new BlockPos(originX + 2, originY + dy, originZ + 2), Blocks.STONE_BRICKS.defaultBlockState());
        }

        // Центр алтаря
        place(level, box, new BlockPos(originX, originY + 1, originZ),
                Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
    }
}
