package com.ferreusveritas.testing;

import com.ferreusveritas.BaseTestSupport;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.misc.MiscHelper;
import com.ferreusveritas.node.*;
import com.ferreusveritas.node.ports.InputPort;
import com.ferreusveritas.node.ports.OutputPort;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.shape.Shape;
import com.ferreusveritas.scene.Scene;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeTest extends BaseTestSupport {
	
	@Test
	void testInfinity() {
	
		double inf = Double.POSITIVE_INFINITY;
		double ninf = Double.NEGATIVE_INFINITY;
		double nan = Double.NaN;
		double zero = 0.0;
		
		String infStr = MiscHelper.write(inf);
		String ninfStr = MiscHelper.write(ninf);
		String nanStr = MiscHelper.write(nan);
		String zeroStr = MiscHelper.write(zero);
		
		assertEquals("\"Infinity\"", infStr);
		assertEquals("\"-Infinity\"", ninfStr);
		assertEquals("\"NaN\"", nanStr);
		assertEquals("0.0", zeroStr);
		
		double inf2 = MiscHelper.read(infStr, Double.class);
		double ninf2 = MiscHelper.read(ninfStr, Double.class);
		double nan2 = MiscHelper.read(nanStr, Double.class);
		double zero2 = MiscHelper.read(zeroStr, Double.class);
		
		assertEquals(inf, inf2);
		assertEquals(ninf, ninf2);
		assertEquals(nan, nan2);
		assertEquals(zero, zero2);
	}
	
	@Test
	void testBooleanNode() {
		ConstantNode<Boolean> constantNode1 = new ConstantNode<>(true, PortDataTypes.BOOLEAN);
		ConstantNode<Boolean> constantNode2 = new ConstantNode<>(false, PortDataTypes.BOOLEAN);
		
		BooleanNode booleanNode = new BooleanNode(Boolean::logicalAnd);
		
		InputPort<Boolean> inputPort1 = booleanNode.getInputPort("in1", Boolean.class).orElseThrow();
		InputPort<Boolean> inputPort2 = booleanNode.getInputPort("in2", Boolean.class).orElseThrow();
		OutputPort<Boolean> outputPort1 = constantNode1.getOutputPort("out", Boolean.class).orElseThrow();
		OutputPort<Boolean> outputPort2 = constantNode2.getOutputPort("out", Boolean.class).orElseThrow();
		OutputPort<Boolean> outputPort = booleanNode.getOutputPort("out", Boolean.class).orElseThrow();
		inputPort1.connect(outputPort1);
		inputPort2.connect(outputPort2);
		
		Boolean result = outputPort.read();
		
		assertEquals(false, result);
	}
	
	@Test
	void testNodeLoadingFromJson() {
		Scene document = readResourceAs("cavitatedCube.json", Scene.class);
		Shape shape = document.root().getOutputPort("shape", Shape.class).flatMap(OutputPort::readOpt).orElseThrow();
		
		assertEquals(1.0, shape.getVal(new Vec3D(0.1, 0.1, 0.1)));
		assertEquals(0.0, shape.getVal(new Vec3D(-0.1, 0.0, 0.0)));
		assertEquals(0.0, shape.getVal(new Vec3D(3.0, 3.0, 3.0)));
		assertEquals(1.0, shape.getVal(new Vec3D(1.1, 0.1, 0.1)));
		assertEquals(0.0, shape.getVal(new Vec3D(1.0, 1.0, 1.0)));
		assertEquals(1.0, shape.getVal(new Vec3D(1.0, 1.0, 0.9999)));
	}
	
}
