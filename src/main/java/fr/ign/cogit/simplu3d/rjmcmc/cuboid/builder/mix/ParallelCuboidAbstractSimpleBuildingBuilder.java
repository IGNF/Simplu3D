package fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.mix;

import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.operation.distance.DistanceOp;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.AbstractParallelCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.ParallelCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.ParallelCuboid2;
import fr.ign.mpp.kernel.ObjectBuilder;


/**
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see http://www.cecill.info/
 *        
 * Basic class for ParallelCuboid Sampler. Cuboid are parallel to limits linear geometries
 * 
 * @author MBrasebin
 *
 */
public class ParallelCuboidAbstractSimpleBuildingBuilder implements ObjectBuilder<AbstractSimpleBuilding>{


	GeometryFactory factory;
	MultiLineString limits;
	int bandType;

	public ParallelCuboidAbstractSimpleBuildingBuilder(IGeometry[] limits, int bandType) throws Exception {
		factory = new GeometryFactory();
		LineString[] lineStrings = new LineString[limits.length];
		for (int i = 0; i < limits.length; i++) {
			lineStrings[i] = (LineString) AdapterFactory.toGeometry(factory, limits[i]);
		}
		this.limits = factory.createMultiLineString(lineStrings);
		this.bandType = bandType;
	}

	@Override
	public AbstractSimpleBuilding build(double[] coordinates) {
		Coordinate p = new Coordinate(coordinates[0], coordinates[1]);
		DistanceOp op = new DistanceOp(this.limits, factory.createPoint(p));
		Coordinate projected = op.nearestPoints()[0];
		double distance = op.distance();
		double orientation = Angle.angle(p, projected);
		AbstractParallelCuboid result;
		if (bandType == 1) {

			result = new ParallelCuboid(coordinates[0], coordinates[1], coordinates[2], distance * 2,
					coordinates[3], orientation + Math.PI / 2);

		} else {
			result = new ParallelCuboid2(coordinates[0], coordinates[1], coordinates[2], distance * 2,
					coordinates[3], orientation + Math.PI / 2);

		}

		return result;
	}

	@Override
	public int size() {
		return 4;
	}

	@Override
	public void setCoordinates(AbstractSimpleBuilding t, double[] coordinates) {
		AbstractParallelCuboid pc = (AbstractParallelCuboid) t;
		coordinates[0] = pc.centerx;
		coordinates[1] = pc.centery;
		coordinates[2] = pc.length;
		coordinates[3] = pc.height;

	}
}
