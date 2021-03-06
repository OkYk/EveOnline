package fr.guiguilechat.eveonline.model.sde.translate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.guiguilechat.eveonline.model.sde.industry.Blueprint;
import fr.guiguilechat.eveonline.model.sde.industry.Blueprint.Activity;
import fr.guiguilechat.eveonline.model.sde.industry.Blueprint.Material;
import fr.guiguilechat.eveonline.model.sde.industry.InventionDecryptor;
import fr.guiguilechat.eveonline.model.sde.items.types.decryptors.GenericDecryptor;
import fr.guiguilechat.eveonline.model.sde.load.fsd.Eblueprints;
import fr.guiguilechat.eveonline.model.sde.load.fsd.EcategoryIDs;
import fr.guiguilechat.eveonline.model.sde.load.fsd.EgroupIDs;
import fr.guiguilechat.eveonline.model.sde.load.fsd.EtypeIDs;

public class IndustryTranslater {

	private static final Logger logger = LoggerFactory.getLogger(IndustryTranslater.class);

	/**
	 *
	 * @param args
	 *          should be [database destination root], typically
	 *          src/generated/resources/
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		long timeStart = System.currentTimeMillis();
		File folderOut = new File(args.length == 0 ? "src/generated/resources/" : args[0]);
		folderOut.mkdirs();

		LinkedHashMap<String, Blueprint> blueprints = new LinkedHashMap<>();
		LinkedHashMap<String, InventionDecryptor> decryptors = new LinkedHashMap<>();

		translate(blueprints, decryptors);

		// sort

		Stream.of(blueprints, decryptors).forEach(m -> {
			ArrayList<Entry<String, ? extends Object>> list = new ArrayList<>(m.entrySet());
			Collections.sort(list, (e1, e2) -> e1.getKey().compareTo(e2.getKey()));
			m.clear();
			for (Entry<String, ? extends Object> e : list) {
				((Map<String, Object>) m).put(e.getKey(), e.getValue());
			}
		});

		// save

		Blueprint.export(blueprints, folderOut);
		InventionDecryptor.export(decryptors, folderOut);

		System.err.println("exported industry in " + (System.currentTimeMillis() - timeStart) / 1000 + "s");

	}

	private static void translate(LinkedHashMap<String, Blueprint> blueprints,
			LinkedHashMap<String, InventionDecryptor> decryptors) {
		LinkedHashMap<Integer, EtypeIDs> types = EtypeIDs.load();
		for (Entry<Integer, Eblueprints> e : Eblueprints.load().entrySet()) {
			EtypeIDs type = types.get(e.getValue().blueprintTypeID);
			if (type != null) {
				if (type.published) {
					Blueprint bp2 = makeBlueprint(e.getValue(), types);
					bp2.name = type.enName();
					blueprints.put(type.enName(), bp2);
				} else {
					logger.info("skipping bp for unpublished "+type.enName());
				}
			} else {
				logger.warn("can't find type for blueprint id " + e.getValue().blueprintTypeID);
			}
		}

		for (Entry<String, GenericDecryptor> e : GenericDecryptor.load().entrySet()) {
			decryptors.put(e.getKey(), convertDecryptor(e.getValue()));
		}
	}

	private static Blueprint makeBlueprint(Eblueprints bp, LinkedHashMap<Integer, EtypeIDs> types) {
		Blueprint bp2 = new Blueprint();
		bp2.id = bp.blueprintTypeID;
		bp2.copying = convertEblueprint(bp.activities.copying, types);
		bp2.invention = convertEblueprint(bp.activities.invention, types);
		bp2.manufacturing = convertEblueprint(bp.activities.manufacturing, types);
		bp2.research_material = convertEblueprint(bp.activities.research_material, types);
		bp2.research_time = convertEblueprint(bp.activities.research_time, types);
		bp2.reaction = convertEblueprint(bp.activities.reaction, types);
		return bp2;
	}

	public static Activity convertEblueprint(
			fr.guiguilechat.eveonline.model.sde.load.fsd.Eblueprints.BPActivities.Activity activity,
			LinkedHashMap<Integer, EtypeIDs> types) {
		Activity ret = new Activity();
		ret.time = activity.time;
		activity.materials.stream().map(m -> convertMaterial(m, types)).forEach(ret.materials::add);
		activity.products.stream().map(p -> convertMaterial(p, types)).forEach(ret.products::add);
		activity.skills.stream().forEach(s -> ret.skills.put(types.get(s.typeID).enName(), s.level));
		return ret;
	}

	public static Material convertMaterial(fr.guiguilechat.eveonline.model.sde.load.fsd.Eblueprints.Material sdeMat,
			LinkedHashMap<Integer, EtypeIDs> types) {
		EtypeIDs item = types.get(sdeMat.typeID);
		if (item != null) {
			Material ret = new Material();
			ret.quantity = sdeMat.quantity;
			ret.name = item.enName();
			ret.id = sdeMat.typeID;
			ret.probability = sdeMat.probability;
			EgroupIDs group = EgroupIDs.load().get(item.groupID);
			ret.group = group.enName();
			EcategoryIDs cat = EcategoryIDs.load().get(group.categoryID);
			ret.category = cat.enName();
			return ret;
		} else {
			return null;
		}
	}

	public static InventionDecryptor convertDecryptor(GenericDecryptor dec) {
		InventionDecryptor ret = new InventionDecryptor();
		ret.me = (int) dec.InventionMEModifier;
		ret.te = (int) dec.InventionTEModifier;
		ret.maxrun = (int) dec.InventionMaxRunModifier;
		ret.id = dec.id;
		ret.name = dec.name;
		ret.probmult = dec.InventionPropabilityMultiplier;
		return ret;
	}
}
