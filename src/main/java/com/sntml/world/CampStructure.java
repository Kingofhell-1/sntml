package com.sntml.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class CampStructure extends Structure {

    public static final Codec<CampStructure> CODEC = simpleCodec(CampStructure::new);

    public CampStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        int y = context.chunkGenerator().getFirstOccupiedHeight(
                x, z, Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(), context.randomState());

        if (y < 5) return Optional.empty();

        BlockPos pos = new BlockPos(x, y, z);
        return Optional.of(new GenerationStub(pos,
                builder -> builder.addPiece(new CampPiece(context.structureTemplateManager(), pos))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.CAMP.get();
    }
}
