package com.sntml.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class MonolithStructure extends Structure {

    public static final Codec<MonolithStructure> CODEC =
            simpleCodec(MonolithStructure::new);

    public MonolithStructure(StructureSettings settings) {
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
        return Optional.of(new GenerationStub(pos, builder -> builder.addPiece(new MonolithPiece(pos))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.MONOLITH.get();
    }
}
