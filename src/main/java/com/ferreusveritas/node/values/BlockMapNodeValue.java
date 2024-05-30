package com.ferreusveritas.node.values;

import com.ferreusveritas.node.DataSpan;

public class BlockMapNodeValue extends NodeValue {
	
	private BlockMapNodeValue(
		String name,
		DataSpan span,
		boolean nullable
	) {
		super(name, span, nullable);
	}
	
	@Override
	public String getType() {
		return "block";
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder extends NodeValue.Builder<BlockMapNodeValue> {
		
		public Builder(String name) {
			super(name);
		}
		
		@Override
		public BlockMapNodeValue build() {
			return new BlockMapNodeValue(name, span, nullable);
		}
		
	}
	
}
