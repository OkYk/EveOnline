package fr.guiguilechat.eveonline.database.retrieval;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import fr.guiguilechat.eveonline.database.Database;
import fr.guiguilechat.eveonline.database.Parser;
import fr.guiguilechat.eveonline.database.elements.Hull;

public class ChukerDumper {

	public static final String SHIPS_PAGE = "http://games.chruker.dk/eve_online/market.php?group_id=4";
	public static final String HERETIC_PAGE = "http://games.chruker.dk/eve_online/market.php?group_id=826";
	public static final String CHRUKERFILE = "src/main/resources/chrukerdump/hulls.yml";

	public static final File CHRUKER_CACHE = new File("target/chruker/");

	public static void main(String[] args) throws IOException {
		Database db = dumpChruker();
		Parser.write(db, new File(CHRUKERFILE));
	}

	public static Database dumpChruker() throws IOException {
		CHRUKER_CACHE.mkdirs();
		Database db = new Database();
		updateDatabaseShips(SHIPS_PAGE, db);
		return db;
	}

	public static Document getCached(String id, String url) {
		Document page = null;
		File cached = new File(CHRUKER_CACHE, id + ".html");
		if (cached.exists()) {
			try {
				page = Jsoup.parse(cached, "UTF-8", url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (page == null) {
			System.err.println("caching element " + id);
			try {
				page = Jsoup.connect(url).get();
				FileWriter w = new FileWriter(cached);
				w.write(page.outerHtml());
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return page;
	}

	protected static void updateDatabaseShips(String url, Database db) throws IOException {
		try {
			int gid = Integer.parseInt(url.replaceAll(".*group_id=", ""));
			Document page = getCached("g_" + gid, url);
			Set<String> subgroups = page.select("td[align=center] a[href]").stream()
					.map(e -> e.absUrl("href"))
					.collect(Collectors.toSet());
			for (String subURL : subgroups) {
				updateDatabaseShips(subURL, db);
			}
			Set<String> s = page.select("a[href~=.*type_id.*]").stream().map(e -> e.absUrl("href")).collect(Collectors.toSet());
			for (String u : s) {
				addShipData(u, db);
			}
		} catch (IOException | RuntimeException e) {
			System.err.println("while loading group " + url + " : " + e);
			throw e;
		}
	}

	protected final static String[] SENSOR_TYPES = { "RADAR", "LADAR", "Magnetometric", "Gravimetric" };

	protected static void addShipData(String url, Database db) throws IOException {
		Hull hull = new Hull();
		int id = Integer.parseInt(url.replaceAll(".*type_id=", ""));
		db.hulls.put(id, hull);

		Document page = getCached("i_" + id, url);
		hull.name = page.select("h1").get(0).html();
		hull.fitting.high = getAttributeInt(page, "High Slots:");
		hull.fitting.medium = getAttributeInt(page, "Medium Slots:");
		hull.fitting.low = getAttributeInt(page, "Low Slots:");
		hull.fitting.launcher = getAttributeInt(page, "Launcher Hardpoints:");
		hull.fitting.turret = getAttributeInt(page, "Turret Hardpoints:");

		hull.fitting.cpu = getAttributeInt(page, "CPU:");
		hull.fitting.powergrid = getAttributeInt(page, "Powergrid:");
		hull.fitting.capacitor = getAttributeFloat(page, "Capacitor Capacity:");
		hull.fitting.capacitorTime = getAttributeFloat(page, "Capacitor Recharge Time:");

		hull.fitting.rigSlots = getAttributeInt(page, "Rig Slots:");
		hull.fitting.rigCalibration = getAttributeInt(page, "Rig Calibration:");
		hull.fitting.rigSize = getAttribute(page, "Rig Size:");

		hull.fitting.droneCapa = getAttributeInt(page, "Drone Capacity:");
		hull.fitting.droneBandwidth = getAttributeInt(page, "Drone Bandwidth:");

		hull.attributes.velocity = getAttributeInt(page, "Maximum Velocity:");
		hull.attributes.warpSpeed = getAttributeFloat(page, "Warp Speed:");
		hull.attributes.inertiaModifier = getAttributeFloat(page, "Inertia Modifier:");

		hull.attributes.targetRange = getAttributeInt(page, "Maximum Targeting Range:");
		hull.attributes.scanRes = getAttributeInt(page, "Scan Resolution:");
		hull.attributes.maxTargets = getAttributeInt(page, "Maximum Locked Targets:");

		for (String sensorType : SENSOR_TYPES) {
			int str = getAttributeInt(page, sensorType + " Sensor Strength:");
			if (str != 0) {
				hull.attributes.scanType = sensorType;
				hull.attributes.scanStr = str;
				break;
			}
		}
		System.err.println(hull.name);
	}

	/**
	 * find the text of an element which contains another element with given
	 * text<br />
	 * eg
	 *
	 * <pre>
	 * <td><b>bla</b>text
	 * </pre>
	 *
	 * should return "text" when requested "bla".
	 *
	 * @param page
	 *          the page data
	 * @param attribute
	 *          the attribute we want the data
	 * @return the data of the attribute if exists, null otherwise
	 */
	protected static String getAttribute(Document page, String attribute) {
		Elements els = page.select(":containsOwn(" + attribute + ")");
		if (els.isEmpty()) {
			return null;
		} else {
			return els.get(0).parent().ownText();
		}
	}

	/**
	 * convert {@link #getAttribute(Document, String)} to int, after removing the
	 * possible unit
	 *
	 * @param page
	 * @param attribute
	 * @return
	 */
	protected static int getAttributeInt(Document page, String attribute) {
		String val = getAttribute(page, attribute);
		if (val == null) {
			return 0;
		}
		return Integer.parseInt(val.split(" ")[0].replaceAll(",", ""));
	}

	/**
	 * convert {@link #getAttribute(Document, String)} to int, after removing the
	 * possible unit
	 *
	 * @param page
	 * @param attribute
	 * @return
	 */
	protected static float getAttributeFloat(Document page, String attribute) {
		String val = getAttribute(page, attribute);
		if (val == null) {
			return 0;
		}
		return Float.parseFloat(val.split(" ")[0].replaceAll("[,a-zA-Z]", ""));
	}
}
