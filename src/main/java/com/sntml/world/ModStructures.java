package com.sntml.world;

import com.sntml.SNTMLMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, SNTMLMod.MOD_ID);

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, SNTMLMod.MOD_ID);

    public static final RegistryObject<StructureType<AltarStructure>> ALTAR =
            STRUCTURE_TYPES.register("altar", () -> () -> AltarStructure.CODEC);

    public static final RegistryObject<StructureType<MonolithStructure>> MONOLITH =
            STRUCTURE_TYPES.register("monolith", () -> () -> MonolithStructure.CODEC);

    public static final RegistryObject<StructureType<CampStructure>> CAMP =
            STRUCTURE_TYPES.register("camp", () -> () -> CampStructure.CODEC);

    public static final RegistryObject<StructureType<MonumentStructure>> MONUMENT =
            STRUCTURE_TYPES.register("monument", () -> () -> MonumentStructure.CODEC);

    public static final RegistryObject<StructurePieceType> ALTAR_PIECE =
            STRUCTURE_PIECE_TYPES.register("altar_piece", () -> AltarPiece::new);

    public static final RegistryObject<StructurePieceType> MONOLITH_PIECE =
            STRUCTURE_PIECE_TYPES.register("monolith_piece", () -> MonolithPiece::new);

    public static final RegistryObject<StructurePieceType> CAMP_PIECE =
            STRUCTURE_PIECE_TYPES.register("camp_piece", () -> CampPiece::new);

    public static final RegistryObject<StructurePieceType> MONUMENT_PIECE =
            STRUCTURE_PIECE_TYPES.register("monument_piece", () -> MonumentPiece::new);

    public static void register(IEventBus eventBus) {
        STRUCTURE_TYPES.register(eventBus);
        STRUCTURE_PIECE_TYPES.register(eventBus);
    }

    public static void setupPieceTypes() {
        AltarPiece.TYPE = ALTAR_PIECE.get();
        MonolithPiece.TYPE = MONOLITH_PIECE.get();
        CampPiece.TYPE = CAMP_PIECE.get();
        MonumentPiece.TYPE = MONUMENT_PIECE.get();
    }
}
