package com.ferreusveritas.factory;

import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

public interface NodeTypeFactoryPress {
	NodeLoader press(LoaderSystem loaderSystem, JsonObj src);
}
