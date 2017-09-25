package fr.guiguilechat.eveonline.database.apiv2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.jsoup.nodes.Element;

public class APIRoot {

	public final APIKey key;

	public static final String BASEURL = "https://api.eveonline.com/";

	public APIRoot(APIKey key) {
		this.key = key;
	}

	public APIRoot(int id, String code) {
		this(new APIKey(id, code));
	}

	/**
	 *
	 * @param api
	 *          String in the format id:code eg 42:thecode
	 */
	public APIRoot(String api) {
		this(api.split(":"));
	}

	protected APIRoot(String[] keycode) {
		this(Integer.parseInt(keycode[0]), keycode[1]);
	}

	public final Account account = new Account(this);

	public final Char chars = new Char(this);

	public static int getInt(Element el, String field, int defaultVal) {
		return el.hasAttr(field) ? Integer.parseInt(el.attr(field)) : defaultVal;
	}

	public static long getLong(Element el, String field, long defaultVal) {
		return el.hasAttr(field) ? Long.parseLong(el.attr(field)) : defaultVal;
	}

	public static double getDouble(Element el, String field, double defaultVal) {
		return el.hasAttr(field) ? Double.parseDouble(el.attr(field)) : defaultVal;
	}

	public static boolean getBool(Element el, String field, boolean defaultVal) {
		return el.hasAttr(field) ? Integer.parseInt(el.attr(field)) != 0 : defaultVal;
	}

	public static Date getDate(Element el, String field, Date defaultVal) {
		try {
			return el.hasAttr(field) ? sdf.parse(el.attr(field)) : defaultVal;
		} catch (ParseException e) {
			System.err.println(e);
			return defaultVal;
		}
	}

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static {
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	/**
	 * convert a json element on an object of given class. all PUBLIC field of
	 * clazz are used to convert the corresponding attribute of the element . only
	 * primitive types are supported.
	 *
	 * @param e
	 * @param clazz
	 * @return
	 */
	public static <T> T convertElement(Element e, Class<T> clazz) {
		try {
			T ret = clazz.getConstructor(new Class[] {}).newInstance((Object[]) null);
			for (Field f : clazz.getFields()) {
				Class<?> type = f.getType();
				f.setAccessible(true);
				try {
					if (type == int.class || type == Integer.class) {
						f.set(ret, getInt(e, f.getName(), 0));
					} else if (type == long.class || type == Long.class) {
						f.set(ret, getLong(e, f.getName(), 0l));
					} else if (type == double.class || type == Double.class) {
						f.set(ret, getDouble(e, f.getName(), 0.0));
					} else if (type == boolean.class || type == Boolean.class) {
						f.set(ret, getBool(e, f.getName(), false));
					} else if (type == String.class) {
						f.set(ret, e.hasAttr(f.getName()) ? e.attr(f.getName()) : "");
					} else if (type == Date.class) {
						f.set(ret, getDate(e, f.getName(), null));
					} else {
						System.err.println("can't convert field " + f.getName() + " of type " + f.getType().getSimpleName() + " ");
					}
				} catch (ReflectiveOperationException | NumberFormatException roe) {
					throw new UnsupportedOperationException("can't convert corresponding element from " + e + "to ["
							+ f.getType().getSimpleName() + "] " + clazz.getSimpleName() + "." + f.getName(), roe);
				}
			}
			return ret;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e1) {
			throw new UnsupportedOperationException("while converting " + e, e1);
		}
	}

	@Override
	public String toString() {
		return key.keyID + ":" + key.code;
	}

}
