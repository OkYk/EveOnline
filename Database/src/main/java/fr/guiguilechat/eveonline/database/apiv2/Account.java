package fr.guiguilechat.eveonline.database.apiv2;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Account {

	private final APIRoot parent;

	public Account(APIRoot parent) {
		this.parent = parent;
	}

	public final String BASEURL = APIRoot.BASEURL + "account/";

	public static class Character {
		public String name;
		public long characterID;
		public String corporationName;
		public int corporationID;
		public int allianceID;
		public String allianceName;
		public int factionID;
		public String factionName;

		@Override
		public String toString() {
			return name;
		}
	}

	public ArrayList<Character> characters() {
		String url = BASEURL + "characters.xml.aspx?keyID=" + parent.key.keyID + "&vCode=" + parent.key.code;
		ArrayList<Character> ret = new ArrayList<>();
		try {
			Document page = Jsoup.connect(url).get();
			Elements elements = page.select("result rowset row");
			for (Element el : elements) {
				Character chara = new Character();
				ret.add(chara);
				chara.name = el.attr("name");
				chara.characterID = Long.parseLong(el.attr("characterID"));
				chara.corporationName = el.attr("corporationName");
				chara.corporationID = Integer.parseInt(el.attr("corporationID"));
				chara.allianceID = Integer.parseInt(el.attr("allianceID"));
				chara.allianceName = el.attr("allianceName");
				chara.factionID = Integer.parseInt(el.attr("factionID"));
				chara.factionName = el.attr("factionName");
			}
		} catch (IOException e) {
			throw new UnsupportedOperationException("catch this", e);
		}
		return ret;
	}

	public static void main(String[] args) {
		System.out.println(new APIRoot(Integer.parseInt(args[0]), args[1]).account.characters());
	}

}