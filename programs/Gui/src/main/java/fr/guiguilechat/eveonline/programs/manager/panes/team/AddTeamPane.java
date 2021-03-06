package fr.guiguilechat.eveonline.programs.manager.panes.team;

import fr.guiguilechat.eveonline.programs.manager.Manager;
import fr.guiguilechat.eveonline.programs.manager.Settings.TeamDescription;
import fr.guiguilechat.eveonline.programs.manager.panes.EvePane;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class AddTeamPane extends HBox implements EvePane {

	protected final Manager parent;

	@Override
	public Manager parent() {
		return parent;
	}

	protected TextField addTeamField = new TextField();
	protected Button addTeamButton = new Button("create a new team");

	public AddTeamPane(Manager parent) {
		this.parent = parent;
		addTeamField.setPromptText("new team name");
		addTeamButton.setOnAction(this::addTeam);
		getChildren().addAll(addTeamField, addTeamButton);
	}

	public void addTeam(Event e) {
		String name = addTeamField.getText();
		if (!parent.settings.teams().containsKey(name)) {
			parent.settings.teams().put(name, new TeamDescription());
			addTeamField.clear();
		}
	}

}
