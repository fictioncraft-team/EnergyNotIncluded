package com.github.wintersteve25.energynotincluded.common.registration.item;

public class ONIItemRegistryData {
    private final boolean doModelGen;
    private final boolean doLangGen;
    private final boolean includeInCreativeTab;

    public ONIItemRegistryData(boolean doModelGen, boolean doLangGen, boolean includeInCreativeTab) {
        this.doModelGen = doModelGen;
        this.doLangGen = doLangGen;
        this.includeInCreativeTab = includeInCreativeTab;
    }

    public boolean isDoModelGen() {
        return doModelGen;
    }

    public boolean isDoLangGen() {
        return doLangGen;
    }

    public boolean isIncludeInCreativeTab() {
        return includeInCreativeTab;
    }
}
