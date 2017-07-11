package fr.ign.cogit.simplu3d.exec.spacereduction;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.experiments.thesis.predicate.UXL3Predicate;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;

public class SimulationWithSpaceReduction {

	public static void main(String[] args) throws Exception {

		int numberIteration = 1;

		Path path = Paths.get("/tmp/out.csv");
		BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE);

		writer.append("id;energy;iteration;number of boxes;percent success; time");
		for (int i = 0; i < numberIteration; i++) {
			
		

			boolean reduceSpace = true;

			// Loading of configuration file that contains sampling space
			// information and simulated annealing configuration
			String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
			String fileName = "building_parameters_project_expthese_3.xml";
			Parameters p = Parameters.unmarshall(new File(folderName + fileName));

			// Load default environment (data are in resource directory)
			Environnement env = LoaderSHP.loadNoDTM(new File(DemoEnvironmentProvider.class.getClassLoader()
					.getResource("fr/ign/cogit/simplu3d/data2/").getPath()));

			// Select a parcel on which generation is proceeded
			BasicPropertyUnit bPU = env.getBpU().get(1);

			// Instantiation of the sampler
			OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

			UXL3Predicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new UXL3Predicate<>(
					bPU);

			IGeometry geom = bPU.getGeom();

			if (reduceSpace) {

				geom = geom.buffer(-5 - p.getDouble("mindim") / 2);

			}
			
			
			Long t = System.currentTimeMillis();

			GraphConfiguration<Cuboid> cc = oCB.process(bPU, geom, p, env, 1, pred);
			
			Long diffT = System.currentTimeMillis() - t;

			writer.newLine();
			String s = i + ";"+ cc.getEnergy() +";" + 0 + ";" + cc.size() + ";" + pred.getSucessRatio()+";"+diffT;
			writer.append(s);
			writer.flush();
		}

		writer.flush();
		writer.close();

		// System.out.println("Number of cuboid : " + cc.size());

	}

}
