package fr.guiguilechat.eveonline.model.sde.translate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCatchBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JTryBlock;
import com.sun.codemodel.JVar;

import fr.guiguilechat.eveonline.model.sde.compile.SDECompiler.CompiledClassesData;
import fr.guiguilechat.eveonline.model.sde.compile.inmemory.DynamicClassLoader;
import fr.guiguilechat.eveonline.model.sde.load.bsd.EdgmTypeAttributes;
import fr.guiguilechat.eveonline.model.sde.load.fsd.EtypeIDs;
import fr.guiguilechat.eveonline.model.sde.yaml.CleanRepresenter;

/**
 * translates sde into yaml files using compiled data. Also modifies the
 * compilation unit to add load functions.
 *
 */
public class ItemsTranslater {

	private static final Logger logger = LoggerFactory.getLogger(ItemsTranslater.class);

	public void translate(CompiledClassesData classes, File destFolder, String resFolder) {
		long startTime = System.currentTimeMillis();
		JCodeModel cm = classes.model;
		DynamicClassLoader cl = new DynamicClassLoader(getClass().getClassLoader()).withCode(cm);
		// filepath->item name -> object
		// eg mycategory/mygroup.yaml -> item1-> new MyGroup()
		HashMap<String, LinkedHashMap<String, Object>> exportItems = new HashMap<>();
		HashMap<Integer, Object> builtItems = new HashMap<>();

		LinkedHashMap<Integer, EtypeIDs> typeids = EtypeIDs.load();
		for (Entry<Integer, EtypeIDs> e : typeids.entrySet()) {
			EtypeIDs type = e.getValue();
			String className = classes.groupID2ClassName.get(type.groupID);
			Object item = makeObjectDefault(className, cl);
			String fileName = item.getClass().getSuperclass().getSimpleName().toLowerCase() + "/"
					+ item.getClass().getSimpleName()
					+ ".yaml";
			LinkedHashMap<String, Object> m = exportItems.get(fileName);
			if (m == null) {
				m = new LinkedHashMap<>();
				exportItems.put(fileName, m);
				// also add a static final field into the class.
				JDefinedClass clazz = cm._getClass(className);
				clazz.field(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, cm.ref(String.class), "RESOURCE_PATH")
				.init(JExpr.lit(resFolder + fileName));

				// create a Container class that contains only a field
				// LinkedHashMap<String, thisclass>
				// this allows to have snakeyaml parse a text file into a hahsmap
				try {
					clazz._class(JMod.PRIVATE | JMod.STATIC, "Container").field(JMod.PUBLIC,
							cm.ref(LinkedHashMap.class).narrow(cm.ref(String.class), clazz), "items");
				} catch (JClassAlreadyExistsException e1) {
					throw new UnsupportedOperationException("catch this", e1);
				}

				// create the load method
				JClass retType = cm.ref(LinkedHashMap.class).narrow(cm.ref(String.class), clazz);
				// the cache of the load
				JVar cache = clazz
						.field(JMod.PRIVATE | JMod.STATIC, retType, "cache")
						.init(JExpr.direct("null"));
				// body method for load
				JMethod load = clazz.method(JMod.PUBLIC | JMod.STATIC, retType, "load");
				JBlock ifblock = load.body()._if(JExpr.direct("cache==null"))._then();
				JTryBlock tryblock = ifblock._try();
				tryblock.body().assign(cache, JExpr._new(cm.ref(Yaml.class)).invoke("loadAs")
						.arg(JExpr._new(cm.ref(InputStreamReader.class)).arg(clazz.dotclass().invoke("getClassLoader")
								.invoke("getResourceAsStream").arg(JExpr.direct("RESOURCE_PATH"))))
						.arg(JExpr.direct("Container.class"))
						.ref("items"));
				JCatchBlock catchblk = tryblock._catch(cm.ref(Exception.class));
				catchblk.body()._throw(JExpr._new(cm.ref(UnsupportedOperationException.class)).arg(JExpr.lit("catch this"))
						.arg(catchblk.param("exception")));
				load.body()._return(JExpr.direct("cache"));

			}
			m.put(type.enName(), item);
			builtItems.put(e.getKey(), item);
		}
		for (Entry<Integer, HashMap<Integer, EdgmTypeAttributes>> e : EdgmTypeAttributes.loadByTypeIDAttributeID()
				.entrySet()) {
			try {
				Object built = builtItems.get(e.getKey());
				for (Entry<Integer, EdgmTypeAttributes> c : e.getValue().entrySet()) {
					String fieldName = classes.attID2FieldName.get(c.getKey());
					Field f = built.getClass().getField(fieldName);
					f.setAccessible(true);
					if (f.getType() == double.class) {
						f.set(built, c.getValue().valueFloat);
					} else {
						if (c.getValue().valueFloat != 0) {
							f.set(built, (int)c.getValue().valueFloat);
						} else {
							f.set(built, c.getValue().valueInt);
						}
					}
				}
			} catch (Exception ex) {
				throw new UnsupportedOperationException(ex);
			}
		}

		// write the items

		destFolder.mkdirs();
		for (Entry<String, LinkedHashMap<String, Object>> e : exportItems.entrySet()) {
			LinkedHashMap<String, Object> map = e.getValue();
			ArrayList<Entry<String, Object>> sortingList = new ArrayList<>(map.entrySet());
			Collections.sort(sortingList, (e1, e2) -> e1.getKey().compareTo(e2.getKey()));
			map.clear();
			for (Entry<String, Object> e2 : sortingList) {
				map.put(e2.getKey(), e2.getValue());
			}
			File out = new File(destFolder, e.getKey());
			out.mkdirs();
			out.delete();
			try {
				DumperOptions options = new DumperOptions();
				options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
				Yaml yaml = new Yaml(new CleanRepresenter(), options);
				yaml.dump(new Object() {
					@SuppressWarnings("unused")
					public LinkedHashMap<String, Object> items = map;
				}, new FileWriter(out));
			} catch (IOException e1) {
				throw new UnsupportedOperationException("catch this", e1);
			}
		}
		logger.info("translated items in " + (System.currentTimeMillis() - startTime) / 1000 + "s");
	}

	protected Object makeObjectDefault(String string, DynamicClassLoader cl) {
		try {
			Class<?> clazz = cl.loadClass(string);
			Object ret = clazz.newInstance();

			ret.getClass().getMethod("loadDefault").invoke(ret);
			return ret;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
				| SecurityException | IllegalArgumentException | InvocationTargetException e) {
			throw new UnsupportedOperationException("catch this", e);
		}
	}
}
