package fr.guiguilechat.eveonline.model.sde.locations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

import fr.guiguilechat.eveonline.model.sde.yaml.CleanRepresenter;
import fr.guiguilechat.eveonline.model.sde.yaml.Tools;

public class Station {

	// loading

	private static LinkedHashMap<String, Station> cache = null;

	public static final String RESOURCE_PATH = "SDE/locations/stations.yaml";

	public static synchronized LinkedHashMap<String, Station> load() {
		if (cache == null) {
			try {
				cache = new Yaml().loadAs(
						new InputStreamReader(Station.class.getClassLoader().getResourceAsStream(RESOURCE_PATH)),
						Container.class).locations;
			} catch (Exception exception) {
				throw new UnsupportedOperationException("catch this", exception);
			}
		}
		return cache;
	}

	private static Map<Integer, String> loadById = null;

	public static Map<Integer, String> loadById() {
		if (loadById == null) {
			LinkedHashMap<String, Station> mcache = load();
			synchronized (mcache) {
				if (loadById == null) {
					loadById = mcache.entrySet().stream().collect(Collectors.toMap(e -> e.getValue().id, e -> e.getKey()));
				}
			}
		}
		return loadById;
	}

	public static void export(LinkedHashMap<String, Station> data, File folderout) {
		File output = new File(folderout, RESOURCE_PATH);
		output.mkdirs();
		output.delete();
		Container c = new Container();
		c.locations = data;
		try {
			new Yaml(new CleanRepresenter(), Tools.blockDumper()).dump(c, new FileWriter(output));
		} catch (IOException e) {
			throw new UnsupportedOperationException("while exporting systems to " + output.getAbsolutePath(), e);
		}
	}

	private static final class Container {
		public LinkedHashMap<String, Station> locations;
	}

	// structure

	public ArrayList<String> services = new ArrayList<>();

	public int id;

	public String solarSystem;

}