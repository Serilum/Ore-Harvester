package com.serilum.oreharvester.data;

import net.minecraft.core.BlockPos;

public class HarvestAttempt {
	public final BlockPos pos;
	public final boolean isOreHarvest;
	public final int oreCount;
	public int lastSeenTick;

	public HarvestAttempt(BlockPos pos, boolean isOreHarvest, int oreCount, int lastSeenTick) {
		this.pos = pos;
		this.isOreHarvest = isOreHarvest;
		this.oreCount = oreCount;
		this.lastSeenTick = lastSeenTick;
	}
}
