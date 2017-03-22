package fr.guiguilechat.eveonline.database.retrieval.sde.bsd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;

import fr.guiguilechat.eveonline.database.retrieval.sde.SDEDumper;

/**
 * an entry in the bsd/dgmTypeAttributes.yaml file
 */
public class EdgmTypeAttributes {

	public static final File FILE = new File(SDEDumper.CACHEDIR, "sde/bsd/dgmTypeAttributes.yaml");
	public int attributeID;
	public int typeID;
	public int valueInt;
	public float valueFloat;

	@SuppressWarnings("unchecked")
	public static ArrayList<EdgmTypeAttributes> load() {
		SDEDumper.donwloadSDE();
		Constructor cons = new Constructor(ArrayList.class) {

			@Override
			protected Construct getConstructor(Node node) {
				if (node.getNodeId() == NodeId.mapping) {
					node.setType(EdgmTypeAttributes.class);
				}
				Construct ret = super.getConstructor(node);
				return ret;
			}
		};
		Yaml yaml = new Yaml(cons);
		try {
			return yaml.loadAs(new FileReader(FILE), ArrayList.class);
		} catch (FileNotFoundException e) {
			throw new UnsupportedOperationException("catch this", e);
		}
	}

	public static HashMap<Integer, HashMap<Integer, EdgmTypeAttributes>> loadByTypeIDAttributeID() {
		HashMap<Integer, HashMap<Integer, EdgmTypeAttributes>> ret = new HashMap<>();
		for (EdgmTypeAttributes e : load()) {
			HashMap<Integer, EdgmTypeAttributes> m = ret.get(e.typeID);
			if (m == null) {
				m = new HashMap<>();
				ret.put(e.typeID, m);
			}
			m.put(e.attributeID, e);
		}
		return ret;
	}
}
