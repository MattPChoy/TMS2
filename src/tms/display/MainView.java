package tms.display;


import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

/**
 * The main window for the simulation's user interface.
 * @ass2_given
 */
public class MainView {

    public final static double WINDOW_WIDTH = 760;
    public final static double WINDOW_HEIGHT = 560;

    // jfx stages
    private Stage root;
    private Group rootGroup;
    private VBox mainArea;
    private StructureView structureView;

    // Models
    private MainViewModel model;
    private long lastTime = 0;

    // Button press action queue.
    private LinkedList<String> input;

    /**
     * Constructor - Handles the view creation and setting up of the structure.
     *
     * @param parent the root Stage
     * @param viewModel the connected model
     * @ass2_given View code for A2.
     */
    public MainView(Stage parent, MainViewModel viewModel) {
        this.root = parent;
        this.model = viewModel;

        root.setTitle(this.model.getTitle().get());
        // Set the window size.
        root.setWidth(WINDOW_WIDTH);
        root.setHeight(WINDOW_HEIGHT);
        // Create the scene.
        rootGroup = new Group();
        Scene rootScene = new Scene(rootGroup);
        // TODO: To make window prettier: rootScene.getStylesheets().add("style.css");
        root.setScene(rootScene);

        // Create a queue to hold input for the event handlers.
        input = new LinkedList<>();

        // Grab key presses and add each one to the input queue.
        rootScene.setOnKeyPressed(event -> {
            String code = event.getCode().toString();
            input.push(code);
        });
        createWindow();
    }

    /**
     * Creates and returns the details which display the value of
     * {@link MainViewModel#detailsTextProperty()}
     *
     * @return the created text display
     * @ass2_given View code for A2.
     */
    private TextArea createDetails() {
        var details = new TextArea();
        details.textProperty().bind(model.detailsTextProperty());
        details.setEditable(false);
        details.setFocusTraversable(false);
        details.setWrapText(true);
        return details;
    }

    /**
     * Creates and returns the operational buttons in a Vertical stack.
     * The buttons are:
     * <ul>
     * <li>Add an intersection</li>
     * <li>Add a sensor</li>
     * <li>Add an electronic speed sign</li>
     * <li>Change displayed speed</li>
     * <li>Add a one-way connection to here</li>
     * <li>Add a one-way connection to here</li>
     * <li>Add a one-way connection to here</li>6
     * </ul>
     *
     * @return the created button box
     * @ass2_given View code for A2.
     */
    private Pane createButtonBox() {
        var buttonBox = new VBox();
        var addIntersection = new Button("Add an intersection");
        addIntersection.setOnAction(e -> model.takeInstruction(ButtonOptions.ADD_INTERSECTION,
                new ArrayList<>(Collections.singletonList(getResponse("Enter a (unique) ID")))));

        var addSensor = new Button("Add a sensor");
        addSensor.setOnAction(e -> model.takeInstruction(ButtonOptions.ADD_SENSOR,
                new ArrayList<>(Arrays.asList(
                getResponse("Enter a sensor type", "'PP', 'SC' or 'VC'"),
                getResponse("Enter threshold and data", "ie. 'threshold:list,of,data,values'")))));
        addSensor.disableProperty().bind(model.isRouteSelected().not());

        var addSign = new Button("Add an electronic speed sign");
        addSign.setOnAction(e -> model.takeInstruction(ButtonOptions.ADD_SIGN, new ArrayList<>(
                        Collections.singletonList(getResponse("Enter initial speed limit")))));
        addSign.disableProperty().bind(model.isRouteSelected().not());

        var setSpeed = new Button("Change displayed speed");
        setSpeed.setOnAction(e -> model.takeInstruction(ButtonOptions.SET_SPEED, new ArrayList<>(
                Collections.singletonList(getResponse("Enter new speed limit for electronic speed sign")))));
        setSpeed.disableProperty().bind(model.isRouteSelected().not());

        var addConnection = new Button("Add a one-way connection to here");
        addConnection.setOnAction(e -> model.takeInstruction(ButtonOptions.ADD_CONN,
                new ArrayList<>(Arrays.asList(
                        getResponse("Enter an origin intersection ID", "Connect from:"),
                        getResponse("Enter a default speed")))));
        addConnection.disableProperty().bind(model.isIntersectionSelected().not());

        var addTwoWayConnection = new Button("Add a two-way connection");
        addTwoWayConnection.setOnAction(e -> model.takeInstruction(ButtonOptions.ADD_TWO_WAY_CONN,
                new ArrayList<>(Arrays.asList(getResponse("Enter an intersection ID", "Connect from:"),
                        getResponse("Enter a default speed")))));
        addTwoWayConnection.disableProperty().bind(model.isIntersectionSelected().not());

        var addTrafficLight = new Button("Add a set of traffic lights");
        addTrafficLight.setOnAction(e -> model.takeInstruction(ButtonOptions.ADD_LIGHT,
                new ArrayList<>(Arrays.asList(getResponse("Enter a duration (in seconds)"),
                getResponse("Enter the order of intersections (leave blank " +
                                "for default order)","ie. 'list,of,intersection,ids'")))));
        addTrafficLight.disableProperty().bind(model.isIntersectionSelected().not());

        var changeLightDuration = new Button("Change traffic light duration");
        changeLightDuration.setOnAction(e -> model.takeInstruction(ButtonOptions.CHANGE_LIGHT_DURATION,
                new ArrayList<>(Collections.singletonList(
                        getResponse("Enter new duration for traffic lights")))));
        changeLightDuration.disableProperty().bind(model.isIntersectionSelected().not());

        var reduceSpeed = new Button("Reduce incoming speed limits");
        reduceSpeed.setOnAction(e -> model.takeInstruction(ButtonOptions.REDUCE_SPEED, new ArrayList<>()));
        reduceSpeed.disableProperty().bind(model.isNothingSelected());

        buttonBox.getChildren().addAll(addIntersection, addSensor, addSign,
                setSpeed, addConnection, addTwoWayConnection, addTrafficLight,
                changeLightDuration, reduceSpeed);
        return buttonBox;
    }

    /**
     * Creates and returns the bottom panel, which is a vertical split of the details and the buttonBox.
     *
     * @return the bottom panel
     * @ass2_given View code for A2.
     */
    private Pane createBottomPanel () {
        var bottomPanel = new HBox();
        var buttonBox = createButtonBox();
        var details = createDetails();
        bottomPanel.getChildren().addAll(details, buttonBox);
        return bottomPanel;
    }

    /**
     * Creates an information bar displaying the state of the simulation and
     * providing the ability to pause the simulation and to save its state.
     * <p>
     * The information bar has the following properties:
     * <ul>
     * <li>A label with the text from {@link MainViewModel#getTimeElapsed()}</li>
     * <li>A label with the text from {@link MainViewModel#getPausedText()}</li>
     * <li>A button with the text "Save" that calls {@link MainViewModel#save(String)}</li>
     * <li>A button with the text from {@link MainViewModel#getPausedButtonText()} that pauses the system</li>
     * </ul>
     *
     * @return the created info bar
     * @ass2_given View code for A2.
     */
    private Pane createInfoBar() {
        final double labelWidth = 120;
        final int buttonWidth = 65;
        var infoBar = new HBox(10);

        var tick = new Label();
        tick.setId("TickLabel");
        tick.textProperty().bind(model.getTimeElapsed());
        var tickLblBox = new HBox(10, tick);
        tickLblBox.setPrefWidth(labelWidth);

        var paused = new Label();
        paused.setId("PauseLabel");
        paused.textProperty().bind(model.getPausedText());
        var pausedLblBox = new HBox(10, paused);
        pausedLblBox.setPrefWidth(labelWidth);

        var save = new Button("Save");
        save.setId("SaveButton");
        save.setStyle("-fx-background-insets: 0, 1, 2;" +
                "-fx-border-color: #000000;" + "-fx-background-radius: 5, 4, 3;" +
                "-fx-pref-width: " + buttonWidth + "px;");
        save.setMaxHeight(10);
        save.setOnAction(e -> model.takeInstruction(ButtonOptions.SAVE, new ArrayList<>( Collections.singletonList(
                getResponse("Save network to file","File name:")))));

        var pauseButton = new Button("Pause");
        pauseButton.setId("PauseButton");
        pauseButton.textProperty().bind(model.getPausedButtonText());
        pauseButton.setOnAction(e -> model.takeInstruction(ButtonOptions.PAUSE, new ArrayList<>()));
        pauseButton.setStyle("-fx-background-insets: 0, 1, 2;" +
                "-fx-border-color: #000000;" + "-fx-background-radius: 5, 4, 3;" +
                "-fx-pref-width: " + buttonWidth + "px;");

        infoBar.getChildren().addAll(tickLblBox, pausedLblBox, pauseButton, save);
        return infoBar;
    }

    /**
     * Creates the top-level window at the fixed width and height.
     * Adds and creates the infoBar, structureView, bottomPanel and error message to the main window.
     *
     * @ass2_given View code for A2.
     */
    private void createWindow() {
        mainArea = new VBox();
        mainArea.setMaxSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainArea.setId("mainWindow");

        var infoBar = createInfoBar();
        infoBar.setId("infoBar");
        structureView = new StructureView(model);
        var bottomPanel = createBottomPanel();

        var error = new Label();
        error.textProperty().bind(model.errorProperty());
        error.setTextFill(Color.web("#ae0606"));
        error.setTextAlignment(TextAlignment.CENTER);
        error.setAlignment(Pos.CENTER);

        mainArea.getChildren().addAll(infoBar, structureView.getPane(), bottomPanel, error);
        rootGroup.getChildren().add(mainArea);
    }

    /**
     * Updates the required updatable fields (outside the automatic property based fields)
     *
     * @ass2_given View code for A2.
     */
    public void update() {
        structureView.update();
    }


    /**
     * Ticks and updates the simulation though the ViewModel.
     * Also processes queued user input.
     * <p>
     * Once per second, calls {@link MainViewModel#tick()}
     * <p>
     * If the model registers a change, it updates the view.
     *
     * @ass2_given View code for A2.
     */
    public void run() {
        new AnimationTimer() {

            public void handle(long currentNanoTime) {
                while (!input.isEmpty()) {
                    var key = input.pop();
                    model.accept(key);
                }
                if (currentNanoTime - lastTime > 1000000000) {
                    lastTime = currentNanoTime;
                    model.tick();
                }
                if (model.isChanged()) {
                    model.notChanged();
                    update();
                }
            }
        }.start();
        // Show the simulation's screen
        this.root.show();
    }

    /**
     * Gives the user a dialog to enter a response.
     *
     * @param msg the HeaderText of the dialog
     * @param content the Content of the dialog
     * @return the response of the dialog (if any, else " ")
     * @ass2_given View code for A2.
     */
    private Optional<String> getResponse(String msg, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Required");
        dialog.setHeaderText(msg);
        dialog.setContentText(content);
        return dialog.showAndWait();
    }

    /**
     * see {@link MainView#getResponse(String, String)} but content is empty
     *
     * @param msg the prompting message
     * @return the user given response
     * @ass2_given View code for A2.
     */
    private Optional<String> getResponse(String msg) {
        return getResponse(msg, null);
    }

}
