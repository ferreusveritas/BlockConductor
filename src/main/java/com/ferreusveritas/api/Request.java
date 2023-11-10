package com.ferreusveritas.api;

import com.ferreusveritas.math.AABB;

public record Request(String context, AABB area) {
	
	public Request withArea(AABB area) {
		return new Request(context, area);
	}
	
}
