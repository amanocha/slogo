package View.tabs;

import java.util.HashMap; 
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import Controller.Data;
import View.AnimalPaneGUI;
import View.SlogoView;
import View.Workspace;
import View.helper.Buttons;
import View.helper.Colors;
import View.helper.Console;
import View.helper.Graphics;
import View.helper.PenContainer;
import View.helper.TextInput;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

/**
 * This pane implements GenericPane and houses the contents of the options pane. 
 * Several buttons are present, including all the Pen options. 
 * @author Jordan Frazier
 * @author Lucy Zhang
 *
 */
public class OptionsPane extends Observable implements GenericPane<HBox>, Observer  {

	private String displayName = "Options";
	private ListView<HBox> content;
	private Graphics graphics;
	private AnimalPaneGUI animalPaneGUI;
	private Graphics graphic = new Graphics();
	private Workspace workspace;
	private SlogoView slogoView;
	private Buttons buttons = new Buttons();
	private TextInput textInput = new TextInput();
	private ComboBox<String> colors;

	private static final String BACKGROUND_COLOR = "Background Color: ";

	private static final Map<String,String> colorHexVals = new HashMap<String,String>();
	private static final String[] BACKGROUND_COLORS = { Colors.WHITE.toString(), Colors.BLACK.toString(), Colors.BLUE.toString(), Colors.GREEN.toString(), Colors.RED.toString() };

	
	public OptionsPane(){
		
	}
	
	public OptionsPane(AnimalPaneGUI animalPaneGUI, Workspace workspace, SlogoView mainView) {
		Data.getInstance().addObserver(this);
		this.slogoView = mainView;
		this.animalPaneGUI = animalPaneGUI;
		graphics = new Graphics();
		content = new ListView<>();
		this.workspace=workspace;
		createAllOptions(animalPaneGUI);
		populateColorHexVals();
	}
	
	private void populateColorHexVals(){
		for(Colors c : Colors.values() ){ 
			colorHexVals.put(c.toString(), c.getHexColor());
		}		
	}
	
	private void createAllOptions(AnimalPaneGUI animalPaneGUI) {

		PenContainer penColor = animalPaneGUI.getAnimalPane().getPenContainer();
		
		HBox backgroundColor = createBackgroundColorOptions();
		content.getItems().add(penColor.getColorContainer());
	    content.getItems().addAll(penColor.getDashContainer(), penColor.getWidthContainer(), backgroundColor);
		addToPane(addButtonsToHBox(createButtons()));
		addToPane(createTextInputImages());
		
	}
	
	private HBox[] createTextInputImages(){
		HBox box1 = textInput.getTextInputBox(workspace.getAnimalClick(), "Image URL for active turtle", true);
		HBox box2 = textInput.getTextInputBox(workspace.getAnimalClick(), "Image URL for dead turtle", false);
		HBox[] input= {box1, box2};
		return input;
	}
	
	private Button[] createButtons(){
		Button wkspc = buttons.createNewWorkspaceButton(slogoView);
		Button saveWkspc = buttons.createSaveWorkspaceButton(slogoView);
		Button addTurtle = buttons.createAddNumTurtlesButton(workspace);
		Button clearAll = buttons.resetAndClearScreenButton(workspace.getConsole(), slogoView, animalPaneGUI);
		Button newWkspc=buttons.newWorkspaceFromFileButton();
		Button[] buttons = {wkspc, saveWkspc, newWkspc, addTurtle, clearAll};
		return buttons;
	}
	
	private HBox[] addButtonsToHBox(Button[] buttons){
		HBox[] btns = new HBox[buttons.length];
		for (int i=0; i<buttons.length; i++){
			HBox hbox = new HBox(); 
			hbox.getChildren().add(buttons[i]);
			btns[i]=hbox;
		}
		return btns;
	}
	
	private void addToPane(HBox[] components){
		for (int i=0; i<components.length; i++){
			content.getItems().add(components[i]);
		}
	}
	
	private HBox createBackgroundColorOptions(){
		colors = createComboBoxOption(BACKGROUND_COLORS);
		colors.valueProperty().addListener((o, oldValue, newValue) -> {
			workspace.changeAnimalBackgroundColor(newValue);
			Data.getInstance().setBackgroundColor(Colors.BLACK.getColorIdMap().get(newValue));
		});
		HBox backgroundColor = setComboBoxInContainer(colors, BACKGROUND_COLOR);
		return backgroundColor;
	}

	private HBox setComboBoxInContainer(ComboBox<String> box, String name){
		HBox container = new HBox(10);
		Label lbl = new Label(name);
		container.getChildren().addAll(lbl, box);
		return container;
	}

	private ComboBox<String> createComboBoxOption(String[] options) {
		ComboBox<String> combobox = graphic.createComboBox(options);
		combobox.setValue(options[0]);
		return combobox;
	}
	
	@Override
	public String getTabName() {
		return displayName;
	}

	@Override
	public void makeClickable() {

	}

	@Override
	public ObservableList<HBox> getAllItems() {
		return content.getItems();
	}

	@Override
	public ListView<HBox> getTabContent() {
		return this.content;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Data) {
			colors.setValue(Data.getInstance().getBackgroundColor());
		}
		
	}

}
