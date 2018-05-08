package net.h34t.hw;

import java.util.List;

public interface Breeder {

    void setParentPopulation(List<ScoredProgram> pop);

    String breed();
}
