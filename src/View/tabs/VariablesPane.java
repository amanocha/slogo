package View.tabs;

import java.util.Observable;

import Controller.Data;
import View.helper.Console;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import model.variable.Variable;
/**
 * This class is responsible for creating the variables pane that stores all slogo variables. 
 * It is updated by the Data Singleton and UIUpdate class, which detect when the stored variables map is changed, and adds 
 * the variables to the pane accordingly.
 * @author Jordan Frazier
 * @author lucyzhang
 *
 */
public class VariablesPane implements ListViewPane, GenericPane<String> {

	private String displayName = "Variables";
	private ListView<String> content;
	private Console myConsole;

	public VariablesPane(Console console) {
		myConsole = console;
		initializeListView();
		makeClickable();
	}
	
	@Override
	public void initializeListView() {
		content = new ListView<>();
		content.setStyle("-fx-background:white");
	}

	@Override
	public String getTabName() {
		return displayName;
	}

	@Override
	public void makeClickable() {
		content.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					String[] split = content.getSelectionModel().getSelectedItem().split("=");
					split[1] = split[1].trim();
					myConsole.getConsoleArea().setText(myConsole.getConsoleArea().getText() + split[1]);
				}
			}
		});
	}

	@Override
	public void addItem(String value) {
		content.getItems().add(value);
	}

	@Override
	public ObservableList<String> getAllItems() {
		return content.getItems();
	}

	@Override
	public ListView<String> getTabContent() {
		return this.content;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
