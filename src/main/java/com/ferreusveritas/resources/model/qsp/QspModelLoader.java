package com.ferreusveritas.resources.model.qsp;

import com.ferreusveritas.resources.ResourceLoader;
import com.ferreusveritas.resources.model.obj.FullFace;
import com.ferreusveritas.resources.model.obj.ObjModel;
import com.ferreusveritas.resources.model.obj.ObjModelLoader;

import java.io.InputStream;
import java.util.List;

public class QspModelLoader implements ResourceLoader<QspModel> {
	
	@Override
	public QspModel load(InputStream stream) {
		ObjModel objModel = ObjModelLoader.load(stream);
		QspModel.Builder qspModelBuilder = new QspModel.Builder();
		
		List<String> partNames = objModel.getPartNames();
		for (String partName : partNames) {
			List<SimpleFace> faces = objModel.getPart(partName).orElseThrow().getFaces().stream().map(FullFace::toSimpleFace).toList();
			QspModelPart qspModel = new QspModelPart(faces);
			qspModelBuilder.addPart(partName, qspModel);
		}
		
		return qspModelBuilder.build();
	}

}
