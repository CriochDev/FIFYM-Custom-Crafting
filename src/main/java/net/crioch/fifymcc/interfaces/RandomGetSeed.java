package net.crioch.fifymcc.interfaces;

public interface RandomGetSeed {
    default long seed() { return 0L; }
}
