package com.ferreusveritas.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

public abstract class Transform implements Jsonable {
	
	public abstract Matrix4X4 getMatrix();
	
	public abstract String getType();
	
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("type", getType());
	}
	
}

