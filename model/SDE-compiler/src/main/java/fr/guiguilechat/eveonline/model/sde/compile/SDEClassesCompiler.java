package fr.guiguilechat.eveonline.model.sde.compile;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

import fr.guiguilechat.eveonline.model.sde.load.SDECache;
import fr.guiguilechat.eveonline.model.sde.load.bsd.EdgmAttributeTypes;
import fr.guiguilechat.eveonline.model.sde.load.bsd.EdgmTypeAttributes;
import fr.guiguilechat.eveonline.model.sde.load.fsd.EcategoryIDs;
import fr.guiguilechat.eveonline.model.sde.load.fsd.EgroupIDs;
import fr.guiguilechat.eveonline.model.sde.load.fsd.EtypeIDs;

/** Compile the sde tables into java classes */
public class SDEClassesCompiler {

	protected SDECache sde;

	public SDEClassesCompiler() {
		this(new SDECache());
	}

	public SDEClassesCompiler(SDECache sdeCache) {
		sde = sdeCache;
	}

	public static class GroupData {

	}

	protected JCodeModel cm;

	protected JPackage rootPackage() {
		return cm._package("fr.guiguilechat.eveonline.model.sde.compiled");
	}

	protected JPackage annotationsPackage() {
		return rootPackage().subPackage("annotations");
	}

	protected JPackage itemPackage() {
		return rootPackage().subPackage("items");
	}

	LinkedHashMap<Integer, EcategoryIDs> catids;

	protected void loadCatIDs() {
		catids = EcategoryIDs.load();
	}

	LinkedHashMap<Integer, EgroupIDs> groupids;

	protected void loadgroupIDs() {
		groupids = EgroupIDs.load();
	}

	LinkedHashMap<Integer, EtypeIDs> typeids;

	protected void loadTypeIDs() {
		typeids = EtypeIDs.load();
	}

	ArrayList<EdgmTypeAttributes> typeAttributes;

	protected void loadAttributes() {
		typeAttributes = EdgmTypeAttributes.load();
	}

	LinkedHashMap<Integer, EdgmAttributeTypes> attTypes;

	protected void loadAttTypes() {
		attTypes = EdgmAttributeTypes.loadByAttributeID();
	}

	protected void load() {
		long beginTime = System.currentTimeMillis();
		Stream<Runnable> r = Stream.of(this::loadAttributes, this::loadAttTypes, this::loadCatIDs, this::loadgroupIDs,
				this::loadTypeIDs);
		r.parallel().forEach(Runnable::run);
		System.err.println("loaded in " + (System.currentTimeMillis() - beginTime) / 1000 + "s");
	}

	public static class CompiledClassesData {
		public JCodeModel model = new JCodeModel();
		public HashMap<Integer, String> groupID2ClassName = new HashMap<>();
		public HashMap<Integer, String> attID2FieldName = new HashMap<>();
	}

	public CompiledClassesData compile() {
		load();
		CompiledClassesData ret = new CompiledClassesData();
		cm = ret.model;

		// for each group, list all the attributes
		HashMap<Integer, HashSet<Integer>> groupAttributes = new HashMap<>();
		// we also add all the attributes to the category of the group
		HashMap<Integer, HashSet<Integer>> catAttributes = new HashMap<>();
		for (EdgmTypeAttributes attribute : typeAttributes) {
			int attId = attribute.attributeID;
			int typeID = attribute.typeID;

			int groupID = typeids.get(typeID).groupID;
			HashSet<Integer> groupAttribute = groupAttributes.get(groupID);
			if (groupAttribute == null) {
				groupAttribute = new HashSet<>();
				groupAttributes.put(groupID, groupAttribute);
			}
			groupAttribute.add(attId);

			int catID = groupids.get(groupID).categoryID;
			HashSet<Integer> catAttribute = catAttributes.get(catID);
			if (catAttribute == null) {
				catAttribute = new HashSet<>();
				catAttributes.put(catID, catAttribute);
			}
			catAttribute.add(attId);
		}

		// then for each cat we keep oly the attributes that are present in every
		// group

		for (Entry<Integer, HashSet<Integer>> e : groupAttributes.entrySet()) {
			int catID = groupids.get(e.getKey()).categoryID;
			if (catAttributes.containsKey(catID)) {
				catAttributes.get(catID).retainAll(e.getValue());
			} else {
				System.err.println("error : can't find cat id  " + catID);
			}
		}

		// then once all cats have their attributes, we removed those from their
		// group
		// attributes

		for (Entry<Integer, HashSet<Integer>> e : groupAttributes.entrySet()) {
			int catID = groupids.get(e.getKey()).categoryID;
			if (catAttributes.containsKey(catID)) {
				e.getValue().removeAll(catAttributes.get(catID));
			} else {
				System.err.println("error : can't find cat id  " + catID);
			}
		}

		// build

		// create all categories

		// root class is abstract

		JDefinedClass TypeClass;
		try {
			TypeClass = rootPackage()._class(JMod.ABSTRACT | JMod.PUBLIC, "EveItem");
			TypeClass.method(JMod.PUBLIC | JMod.ABSTRACT, cm.INT, "getCategoryId");
			TypeClass.method(JMod.PUBLIC | JMod.ABSTRACT, cm.ref(Class.class).narrow(cm.wildcard()), "getCategory");
			TypeClass.method(JMod.PUBLIC | JMod.ABSTRACT, cm.INT, "getGroupId");
			TypeClass.method(JMod.PUBLIC | JMod.ABSTRACT, cm.ref(Class.class).narrow(cm.wildcard()), "getGroup");
			TypeClass.field(JMod.PUBLIC, cm.INT, "id");
			TypeClass.field(JMod.PUBLIC, cm.DOUBLE, "volume");

		} catch (JClassAlreadyExistsException e2) {
			throw new UnsupportedOperationException("catch this", e2);
		}

		// categories are abstract classes.

		HashMap<Integer, JDefinedClass> catNameToClass = new HashMap<>();

		for (Entry<Integer, EcategoryIDs> cate : catids.entrySet()) {
			String newName = formatName(cate.getValue().enName());
			try {
				JDefinedClass cl = itemPackage()._class(JMod.PUBLIC | JMod.ABSTRACT, newName);
				cl._extends(TypeClass);
				addAttributes(cl, catAttributes.get(cate.getKey()));
				JMethod catID = cl.method(JMod.PUBLIC, cm.INT, "getCategoryId");
				catID.body()._return(JExpr.lit(cate.getKey()));
				catID.annotate(Override.class);
				JMethod catMeth = cl.method(JMod.PUBLIC, cm.ref(Class.class).narrow(cm.wildcard()), "getCategory");
				catMeth.body()._return(JExpr.dotclass(cl));
				catMeth.annotate(Override.class);
				catNameToClass.put(cate.getKey(), cl);
			} catch (JClassAlreadyExistsException e1) {
				throw new UnsupportedOperationException("catch this", e1);
			}
		}

		// then create all groups

		for (Entry<Integer, EgroupIDs> groupe : groupids.entrySet()) {
			String newName = formatName(groupe.getValue().enName());
			JDefinedClass cat = catNameToClass.get(groupe.getValue().categoryID);
			try {
				JDefinedClass cl = itemPackage().subPackage(cat.name().toLowerCase())._class(formatName(newName));
				cl._extends(cat);
				addAttributes(cl, groupAttributes.get(groupe.getKey()));
				JMethod groupID = cl.method(JMod.PUBLIC, cm.INT, "getGroupId");
				groupID.body()._return(JExpr.lit(groupe.getKey()));
				groupID.annotate(Override.class);
				JMethod groupMeth = cl.method(JMod.PUBLIC, cm.ref(Class.class).narrow(cm.wildcard()), "getGroup");
				groupMeth.body()._return(JExpr.dotclass(cl));
				groupMeth.annotate(Override.class);

				ret.groupID2ClassName.put(groupe.getKey(), cl.fullName());
			} catch (JClassAlreadyExistsException e1) {
				throw new UnsupportedOperationException("catch this", e1);
			}
		}

		// map all attributes ids to the fields names

		for (Entry<Integer, EdgmAttributeTypes> e : attTypes.entrySet()) {
			ret.attID2FieldName.put(e.getKey(), formatName(e.getValue().attributeName));
		}

		return ret;

	}

	protected JDefinedClass highIsGoodAnnotation;

	protected JDefinedClass getHighIsGoodAnnotation() {
		if (highIsGoodAnnotation == null) {
			try {
				highIsGoodAnnotation = annotationsPackage()._annotationTypeDeclaration("HighIsGood");
				highIsGoodAnnotation.annotate(Retention.class).param("value",
						cm.ref(RetentionPolicy.class).staticRef("RUNTIME"));
				highIsGoodAnnotation.method(JMod.PUBLIC, cm.BOOLEAN, "value");
			} catch (JClassAlreadyExistsException e) {
				throw new UnsupportedOperationException("catch this", e);
			}
		}
		return highIsGoodAnnotation;
	}

	protected JDefinedClass stackableAnnotation;

	protected JDefinedClass getStackableAnnotation() {
		if (stackableAnnotation == null) {
			try {
				stackableAnnotation = annotationsPackage()._annotationTypeDeclaration("Stackable");
				stackableAnnotation.annotate(Retention.class).param("value",
						cm.ref(RetentionPolicy.class).staticRef("RUNTIME"));
				stackableAnnotation.method(JMod.PUBLIC, cm.BOOLEAN, "value");
			} catch (JClassAlreadyExistsException e) {
				throw new UnsupportedOperationException("catch this", e);
			}
		}
		return stackableAnnotation;
	}

	protected JDefinedClass defaultValueAnnotation;

	protected JDefinedClass getDefaultValueAnnotation() {
		if (defaultValueAnnotation == null) {
			try {
				defaultValueAnnotation = annotationsPackage()._annotationTypeDeclaration("DefaultValue");
				defaultValueAnnotation.annotate(Retention.class).param("value",
						cm.ref(RetentionPolicy.class).staticRef("RUNTIME"));
				defaultValueAnnotation.method(JMod.PUBLIC, cm.DOUBLE, "value");
			} catch (JClassAlreadyExistsException e) {
				throw new UnsupportedOperationException("catch this", e);
			}
		}
		return defaultValueAnnotation;
	}

	protected void addAttributes(JDefinedClass cl, HashSet<Integer> attributeIDs) {
		if (attributeIDs == null) {
			return;
		}
		for (Integer attributeID : attributeIDs) {
			EdgmAttributeTypes attr = attTypes.get(attributeID);
			JFieldVar f = cl.field(JMod.PUBLIC, cm.DOUBLE, formatName(attr.attributeName));
			f.annotate(getHighIsGoodAnnotation()).param("value", attr.highIsGood);
			f.annotate(getStackableAnnotation()).param("value", attr.stackable);
			f.annotate(getDefaultValueAnnotation()).param("value", attr.defaultValue);
			f.javadoc().add(attr.description);

			// f.init(JExpr.lit(attr.defaultValue));
		}
	}

	public static String formatName(String name) {
		if (name.equals("Abstract")) {
			return "Abstrct";
		}
		name = name.replaceAll("♦", "NPC");
		char[] newName = new char[name.length()];
		int skipped = 0;
		for (int charIndex = 0; charIndex < name.length(); charIndex++) {
			char totrans = name.charAt(charIndex);
			if (skipChar(totrans)) {
				skipped++;
				continue;
			} else {
				if (charIndex - skipped == 0 || skipChar(name.charAt(charIndex - 1))) {
					newName[charIndex - skipped] = Character.toUpperCase(totrans);
				} else {
					newName[charIndex - skipped] = totrans;
				}
			}
		}
		String ret = new String(newName, 0, name.length() - skipped);
		if (ret.charAt(0) >= 0 && ret.charAt(0) <= '9') {

			ret = "Max" + ret;
		}
		return ret;
	}

	public static boolean skipChar(char c) {
		return !(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9');
	}

	public static void main(String[] args) throws IOException {
		File target = new File("target/compiledSDE/");
		target.mkdir();
		new SDEClassesCompiler().compile().model.build(target, (PrintStream) null);
	}

}
