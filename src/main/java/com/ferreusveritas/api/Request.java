package com.ferreusveritas.api;

import com.ferreusveritas.math.AABBI;

public record Request(String context, AABBI area) {
	
	public Request withArea(AABBI area) {
		return new Request(context, area);
	}
	
	public Request withContext(String context) {
		return new Request(context, area);
	}
	
}
