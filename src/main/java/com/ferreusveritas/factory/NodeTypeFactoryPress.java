package com.ferreusveritas.factory;

import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public interface NodeTypeFactoryPress {
	NodeLoader press(LoaderSystem loaderSystem, UUID uuid, JsonObj src);
}
