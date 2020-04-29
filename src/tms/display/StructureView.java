package tms.display;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import tms.intersection.Intersection;
import tms.route.Route;

/**
 * Displays the structure of the traffic network.
 * <p>
 *     The structure should be displayed in a pane split vertically in two. There should be a clear visualisation of a
 *     "row" where the left panel of a row contains buttons for each route that enters the same intersection.
 *     That interstion should be displayed to the right of the split.
 *
 * </p>
 * <p>
 *     The Route buttons display "FROM_ID, Con:CONGESTION, speed:SPEED (See {@link Route#getFrom()},
 *     {@link Intersection#getId()} {@link Route#getCongestion()}, {@link Route#getSpeed()} )
 *     The intersection buttons should display the intersection id (See {@link Intersection#getId()}).
 * </p>
 *
 * @ass2_given
 */
public class StructureView {

    // Pane to hold the Structural Display.
    private Pane structurePane;
    private MainViewModel viewModel;

    // Current scroll position of panes to use when updating display.
    private double connectionsHPos = 0;
    private double structurePaneVPos = 0;

    /**
     * Constructor, see {@link StructureView#makeStructurePane()}.
     *
     * @param viewModel the model used to populate the view
     * @ass2_given View code for A2.
     */
    public StructureView(MainViewModel viewModel) {
        this.viewModel = viewModel;
        this.structurePane = makeStructurePane();
    }

    /**
     * Update the pane by removing and repopulating all children, see
     * {@link StructureView#makeStructurePane()}
     *
     * @ass2_given View code for A2.
     */
    public void update() {
        storeScrollPositions();
        this.structurePane.getChildren().removeAll(this.structurePane.getChildren());
        this.structurePane.getChildren().addAll(makeStructurePane().getChildren());
    }

    /**
     * Store the positions in which the connectionsDisplay and
     * the structurePanel are currently in.
     *
     * @ass2_given View code for A2.
     */
    private void storeScrollPositions() {
        try {
            // This code is dependent on the order in which
            // makeStructurePane adds nodes to its panes.
            ScrollPane sp = (ScrollPane) (this.structurePane.getChildren().get(0));
            structurePaneVPos = sp.getVvalue();
            HBox hb = (HBox) sp.getContent();
            ScrollPane sp2 = (ScrollPane) hb.getChildren().get(0);
            connectionsHPos = sp2.getHvalue();
        } catch (ClassCastException cce) {
            // Consequence of ignoring the exception is that the scroll panes
            // reset to the top left of the view after every update.
            // Minimally, the error should be logged.
        }
    }

    /**
     * Creates the structure of the structure pane.
     *
     * @return the structural panels
     * @ass2_given View code for A2.
     */
    private Pane makeStructurePane() {
        final double INTERSECTION_DISPLAY_WIDTH = 200;
        final double DISPLAY_HEIGHT = 250;
        final double SCROLLBAR_SIZE = 15;

        // structurePanel contains the scrollPane that allows this pane to scroll.
        // structureContainer contains the panes that make up the display components.
        // Nesting of an HBox in a ScrollPane in an HBox is needed to accommodate the
        // other parts of the GUI that require a Pane and not a ScrollPane.
        var structurePanel = new HBox();
        var structureContainer = new HBox();
        var scrollPane = new ScrollPane();
        scrollPane.setContent(structureContainer);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVvalue(structurePaneVPos);

        // Pane that contains the buttons for the routes connecting to an intersection.
        var connectionsDisplay = new VBox(2);
        connectionsDisplay.setMinWidth(MainView.WINDOW_WIDTH - INTERSECTION_DISPLAY_WIDTH - SCROLLBAR_SIZE);
        connectionsDisplay.setMinHeight(DISPLAY_HEIGHT);
        connectionsDisplay.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        var scrollableConnectionsDisplay = new ScrollPane();
        scrollableConnectionsDisplay.setContent(connectionsDisplay);
        scrollableConnectionsDisplay.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollableConnectionsDisplay.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollableConnectionsDisplay.setPrefSize(MainView.WINDOW_WIDTH - INTERSECTION_DISPLAY_WIDTH - SCROLLBAR_SIZE,
                DISPLAY_HEIGHT);
        scrollableConnectionsDisplay.setHvalue(connectionsHPos);

        // Pane of all the intersection buttons.
        var intersectionDisplay = new VBox(2);
        intersectionDisplay.setMinWidth(INTERSECTION_DISPLAY_WIDTH);
        intersectionDisplay.setMinHeight(DISPLAY_HEIGHT);
        intersectionDisplay.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // Labels describing the contents of the two panes.
        var connectionsLabel = new Label("Connections going Into: ");
        connectionsLabel.setAlignment(Pos.TOP_RIGHT);
        connectionsLabel.setTextAlignment(TextAlignment.LEFT);

        var intersectionLabel = new Label("Intersections ");
        intersectionLabel.setAlignment(Pos.TOP_LEFT);

        connectionsDisplay.getChildren().addAll(connectionsLabel);
        intersectionDisplay.getChildren().addAll(intersectionLabel);

        // Display details of each intersection
        for (Intersection intersection : viewModel.getIntersections()) {
            var intersectionButton = new Button(intersection.getId());
            intersectionButton.setMaxWidth(INTERSECTION_DISPLAY_WIDTH - 20);
            intersectionButton.setAlignment(Pos.TOP_LEFT);
            intersectionButton.setOnAction(e -> viewModel.setSelected(intersection));
            intersectionButton.setStyle("-fx-background-insets: 0, 1, 2;" +
                    "-fx-border-color: #000000;" + "-fx-background-radius: 5, 4, 3;");
            if (intersection.equals(viewModel.getSelectedIntersection())) {
                intersectionButton.setStyle("-fx-background-insets: 0, 1, 2;" +
                        "-fx-border-color: #999999;" + "-fx-background-radius: 5, 4, 3;");
            }
            intersectionButton.setPrefHeight(25);

            // Display details of all connections going into this intersection.
            var routeBox = new HBox(2);
            for (Route route: intersection.getConnections()) {
                var routeButton = new Button(route.getFrom().getId() + ", Con:" + route.getCongestion() +
                        ", speed:" + route.getSpeed());

                routeButton.setStyle("-fx-background-color: " + MainViewModel.getColor(route.getCongestion()) + ";" +
                        "-fx-background-insets: 0, 1, 2;" + "-fx-border-color: #000000;" +
                        "-fx-background-radius: 5, 4, 3;");
                if (route.equals(viewModel.getSelectedRoute())) {
                    routeButton.setStyle("-fx-background-color: " +
                            MainViewModel.getColor(route.getCongestion()) + ";" +
                            "-fx-background-insets: 0, 1, 2;" + "-fx-border-color: #999999;" +
                            "-fx-background-radius: 5, 4, 3;");
                }

                // TODO: Make buttons prettier: routeButton.setStyle();
                routeButton.setMinWidth(130);
                routeButton.setMinHeight(30);
                routeButton.setPrefHeight(25);
                routeButton.setOnAction(e -> viewModel.setSelected(route, intersection));
                routeBox.getChildren().addAll(routeButton);
            }

            routeBox.setAlignment(Pos.TOP_RIGHT);
            routeBox.setMinHeight(30);
            intersectionButton.setMinHeight(30);
            intersectionDisplay.getChildren().addAll(intersectionButton);
            connectionsDisplay.getChildren().addAll(routeBox);
        }

        // Empty label added to bottom of intersectionDisplay to align with
        // space taken by scroll bar in scrollableConnectionsDisplay.
        intersectionDisplay.getChildren().addAll(new Label());
        structureContainer.getChildren().addAll(scrollableConnectionsDisplay, intersectionDisplay);
        structurePanel.getChildren().addAll(scrollPane);
        return structurePanel;
    }

    /**
     * Gets the pane displayed
     *
     * @return the structural panel to display
     * @ass2_given View code for A2.
     */
    public Pane getPane() {
        return structurePane;
    }
}
