package fr.guiguilechat.eveonline.model.sde.bsd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;

import fr.guiguilechat.eveonline.model.sde.cache.SDECache;

/**
 * an entry in the bsd/dgmEffects.yaml file
 */
public class EdgmEffects {

	public static final File FILE = new File(SDECache.INSTANCE.cacheDir(), "sde/bsd/dgmEffects.yaml");

	public String description;
	public boolean disallowAutoRepeat;
	public String displayName;
	public int effectCategory;
	public int effectID;
	public String effectName;
	public boolean electronicChance;
	public String guid;
	public int iconID;
	public boolean isAssistance;
	public boolean isOffensive;
	public boolean isWarpSafe;
	public int postExpression;
	public int preExpression;
	public boolean propulsionChance;
	public boolean published;
	public boolean rangeChance;
	public int dischargeAttributeID;
	public int distribution;
	public int durationAttributeID;
	public int rangeAttributeID;
	public String sfxName;
	public int falloffAttributeID;
	public int fittingUsageChanceAttributeID;
	public String modifierInfo;
	public int npcActivationChanceAttributeID;

	@SuppressWarnings("unchecked")
	public static ArrayList<EdgmEffects> load() {
		SDECache.INSTANCE.donwloadSDE();
		Constructor cons = new Constructor(ArrayList.class) {

			@Override
			protected Construct getConstructor(Node node) {
				if (node.getNodeId() == NodeId.mapping) {
					node.setType(EdgmEffects.class);
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

	public static LinkedHashMap<Integer, EdgmEffects> loadByEffectID() {
		LinkedHashMap<Integer, EdgmEffects> ret = new LinkedHashMap<>();
		load().stream().forEach(e -> ret.put(e.effectID, e));
		return ret;
	}
}