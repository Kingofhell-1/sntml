package com.sntml.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class MonumentPiece extends StructurePiece {

    public static StructurePieceType TYPE;

    private final int originX;
    private final int originY;
    private final int originZ;
    private final StructureTemplateManager templateManager;

    public MonumentPiece(StructureTemplateManager manager, BlockPos pos) {
        super(TYPE, 0, new BoundingBox(
                pos.getX() - 8, pos.getY(), pos.getZ() - 8,
                pos.getX() + 8, pos.getY() + 16, pos.getZ() + 8
        ));
        this.originX = pos.getX();
        this.originY = pos.getY();
        this.originZ = pos.getZ();
        this.templateManager = manager;
    }

    public MonumentPiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
        super(TYPE, tag);
        this.originX = tag.getInt("ox");
        this.originY = tag.getInt("oy");
        this.originZ = tag.getInt("oz");
        this.templateManager = ctx.structureTemplateManager();
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
        tag.putInt("ox", originX);
        tag.putInt("oy", originY);
        tag.putInt("oz", originZ);
    }

    @Override
    public void postProcess(WorldGenLevel level,
                            StructureManager structureManager,
                            ChunkGenerator generator,
                            RandomSource random,
                            BoundingBox box,
                            ChunkPos chunkPos,
                            BlockPos pos) {
        ResourceLocation rl = new ResourceLocation("sntml", "monument");
        Optional<StructureTemplate> template = templateManager.get(rl);
        if (template.isEmpty()) return;

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(Rotation.NONE)
                .setBoundingBox(box);

        template.get().placeInWorld(level,
                new BlockPos(originX, originY, originZ),
                new BlockPos(originX, originY, originZ),
                settings, random, 2);
    }
}
