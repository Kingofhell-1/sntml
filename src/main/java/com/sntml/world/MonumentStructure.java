package com.sntml.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class MonumentStructure extends Structure {

    public static final Codec<MonumentStructure> CODEC = simpleCodec(MonumentStructure::new);

    public MonumentStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        BlockPos pos = new BlockPos(x, 6, z);
        return Optional.of(new GenerationStub(pos,
                builder -> builder.addPiece(new MonumentPiece(context.structureTemplateManager(), pos))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.MONUMENT.get();
    }
}
