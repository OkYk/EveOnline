package fr.guiguilechat.eveonline.programs.gui;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import fr.guiguilechat.eveonline.programs.settings.ASettings;

public class Settings extends ASettings {

	@Override
	public String getAppName() {
		return "guiguilechat.evemanager";
	}

	public LinkedHashMap<Integer, String> apiKeys = new LinkedHashMap<>();

	public LinkedHashMap<String, LinkedHashSet<String>> teams = new LinkedHashMap<>();

	public String focusedTeam = null;

	public boolean hideDebug = true;

}