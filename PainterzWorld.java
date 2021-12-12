/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PainterzWorld;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Isaiah Daggett
 */

/**
 * This is the software PainterzWorld!
 * With this software you are able to free draw, draw straight lines, shapes and 
 * many more on a canvas which can be blank or occupied by previously imported photos!
 * All of the buttons for this app are in the same section and all of the methods are at the bottom of the code.
 * There are different features that are kind of spread out throughout the code but mainly everything that has a similar class type or
 * is being called on one another  is in the same section.
 * @author Isaiah Christian Daggett
 */

public class PainterzWorld extends Application {
    private Stage stage;
    private String filepath;
    private File file_obj;
    private double linesize;
    private Canvas canvasObject;
    private GraphicsContext gc;
    private double x_1;
    private double y_1;
    private double x_2;
    private double y_2;
    private String Currenttool;
    private Paint color;
   
   
    @Override
    public void start(Stage stage) throws IOException {
     
        // Set scene/window setup
        BorderPane root = new BorderPane();
        VBox vb = new VBox();
        root.setTop(vb);
        Slider slider_obj = new Slider();
        slider_obj.setMin(1);
        slider_obj.setMax(75);
        slider_obj.valueProperty().addListener((observe, original, new_value) -> {
            linesize = new_value.doubleValue();
        });
       
        /**
         *
         * Tells user what tool they are about to use when they hover over the slider for line thickness.
         *
         */
        Tooltip slidertip = new Tooltip("Slider for the thickness of lines");
        Tooltip.install(slider_obj, slidertip);
        /**
         *
         * Sets the scene stage and give the stage a title. This is pretty much what gives the base for the GUI
         */
        Scene scene = new Scene(root, 800, 200);

        stage.setScene(scene);
        stage.setTitle("Painterz World");

        Image transparent = new Image("PaintLogo.png");
        stage.getIcons().add(transparent);

        stage.show();
        /**
         *
         * CANVAS OBJECT THAT ALLOWS USERS TO UPLOAD PICTURES AND EDIT THEM
         */
        Canvas canvasObject = new Canvas();

       
        /**
         * ALLOWS USER TO HAVE SCROLL BARS FOR BIGGER IMAGES
         */
        ScrollPane SP = new ScrollPane(canvasObject);
       
        /**
         * MENU BAR ALLOWS USER TO SELECT FROM MULTIPLE MENUS WITH DIFFERENT OPTIONS
         */
        MenuBar Menu_bar = new MenuBar();
        /**
         * FILE MENU ALLOWS USERS TO OPEN FILES(IMAGES),EXIT,SAVE IMAGES AND SAVE IMAGES AS
         */
        Menu file_menu = new Menu("_File");
        MenuItem Open = new MenuItem("Open file");
        MenuItem Exit = new MenuItem("Exit");
        MenuItem Save = new MenuItem("Save");
        MenuItem Save_as = new MenuItem("Save as");
        root.setTop(vb);    
        /**
         * HELP MENU ALLOWS USER TO LEARN ABOUT THE APPLICATION BY CLICKING ON THE
         * HELP MENU ITEM AND WHAT VERSION OF THE APPLICATION THEY ARE USING BY USING THE ABOUT MENU ITEM
         */
        Menu help_menu = new Menu("Help");
        MenuItem Help = new MenuItem("Help");
        MenuItem about = new MenuItem("About");
        help_menu.getItems().addAll(Help, about);
        file_menu.getItems().addAll(Open, Exit, Save, Save_as);
        //File Menu setup
        /**
         * THIS IS THE FILE PICKER FOR SELECTING FILES FROM THE COMPUTER IN ORDER TO LOAD
         * ONTO THE CANVAS
         */      
        FileChooser filepicker = new FileChooser();
        filepicker.setTitle("Open Image");
        filepicker.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        root.setCenter(SP);
        GraphicsContext gc = canvasObject.getGraphicsContext2D();
        root.getChildren().addAll(canvasObject, slider_obj);
        //Tools menu setup
        /**
         * TOOLS MENU GIVES THE USER THE ABIILITY TO:
         * FREE DRAW,STRAIGHT DRAW AND DRAW SHAPES
         */
        Menu Tools = new Menu("Tools");
        //All of the menuItems for tools
        MenuItem freedraw = new MenuItem("Free draw");
        MenuItem straightdraw = new MenuItem("Straight Draw");
        MenuItem drawSquare = new MenuItem("Draw square");
        MenuItem drawRect = new MenuItem("Draw rectangle");
        MenuItem drawElipse = new MenuItem("Draw Elipse");
        MenuItem drawCirc = new MenuItem("Draw Circle");
        MenuItem TextTool = new MenuItem("Text tool");
        MenuItem ColorGrabber = new MenuItem("Color grabber");
        MenuItem Eraser = new MenuItem("Eraser");
        MenuItem DrawRoundedRect = new MenuItem("Draw Rounded Rectangle");
        Tools.getItems().addAll(freedraw,straightdraw,drawSquare,TextTool,drawRect,drawElipse,drawCirc,ColorGrabber,
                Eraser,DrawRoundedRect);
       
        //Zoom in/out button
        /**
         * ALLOWS USER TO ZOOM IN/OUT ON IMAGES THAT THEY UPLOAD
         */
        Menu Zoom_in_out = new Menu("Zoom in/out");
        MenuItem Zoom_In = new MenuItem("Zoom In");
        MenuItem Zoom_out = new MenuItem("Zoom out");
        Zoom_in_out.getItems().addAll(Zoom_In,Zoom_out);
        /**
         * THE DEVELOPERS FAVORITE FEATURE WHICH IS THE ThirdDimensional SHAPES MENU FOR SHAPES THAT CREATE DIFFERENT PATTERNS
         */
        Menu Sicko = new Menu("ThirdDimensional SHAPES");
        MenuItem S_square = new MenuItem("ThirdDimensional SQUARE");
        MenuItem S_circle = new MenuItem("ThirdDimensional CIRCLE");
        MenuItem S_rectangle = new MenuItem("ThirdDimensional RECTANGLE");
        Sicko.getItems().addAll(S_square,S_circle,S_rectangle);
        Menu_bar.getMenus().addAll(file_menu, help_menu, Tools,Zoom_in_out,Sicko);
       
               
        //ALL BUTTONS
        /**
         * THIS IS THE OPEN BUTTON THAT ALLOWS USERS TO OPEN IMAGES OF THEIR CHOICE
         */
                Open.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //Opening a dialog box
                File file_obj = filepicker.showOpenDialog(stage);
                Image img_obj = new Image(file_obj.toURI().toString());
                canvasObject.setWidth(img_obj.getWidth());
                canvasObject.setHeight(img_obj.getHeight());
                gc.drawImage(img_obj, 0, 0);
                canvasObject.setScaleX(1);
                canvasObject.setScaleY(1);
                //Creates the ability to choose files and open them by putting an action on the open file button
            }
        }
        );
        Open.setAccelerator(new KeyCodeCombination (KeyCode.O,KeyCombination.CONTROL_DOWN));
       
        /**
         * Open button for opening images types: png,jpg, and jpeg
         *
         *
         */
       
       
        //EXIT BUTTON
        /**
         * THE EXIT BUTTON GIVES THE USER THE OPTION TO ACTUALLY EXIT THE PAGE AND ALSO REMINDS THEM TO SAVE
         */
        Exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Alert exit = new Alert(Alert.AlertType.WARNING);
                exit.setTitle("Warning");
                exit.setHeaderText("Wait a second!");
                String s = "Would you like to save your files before you leave?";
                exit.setContentText(s);

                ButtonType btnSave = new ButtonType("Save");
                ButtonType btnDont = new ButtonType("Don't Save");
                ButtonType btnCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

                exit.getButtonTypes().setAll(btnSave, btnDont, btnCancel);

                Optional<ButtonType> result = exit.showAndWait();

                if (result.get() == btnSave) {
                    if (file_obj != null) {
                        WritableImage wi = new WritableImage((int) canvasObject.getWidth(), (int) canvasObject.getHeight());
                        canvasObject.snapshot(null, wi);
                        try {
                            RenderedImage ri = SwingFXUtils.fromFXImage(wi, null);
                            ImageIO.write(ri, "png", file_obj);
                        } catch (IOException ex) {
                            Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Platform.exit();
                        System.exit(0);
                        stage.close();
                    } else if (result.get() == btnDont) {
                        Platform.exit();
                        System.exit(0);
                        stage.close();
                    }
                } else {
                   
                }
            }
            });
        /**
         * Exit button has a save feature with it so if you press exit without saving you'll still have the reminder to save.
         *
         */

       
        //SAVE AS BUTTON
        /**
         * ALLOWS USERS TO SAVE IMAGES THAT THEY HAVE EDITED AND GIVE THEM A TITLE
         */
        Save_as.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //filepicker.showSaveDialog(stage);
                saveFile(canvasObject);
            }
        });              
        //SAVE BUTTON
        /**
         * ALLOWS USER TO SAVE IMAGES THAT THEY HAVE ALREADY SAVED AS
         */
        Save.setOnAction((ActionEvent event) -> {
            if (file_obj != null) {
                try {
                    SnapshotParameters sp = new SnapshotParameters();
                    sp.setFill(Color.TRANSPARENT);          
                    WritableImage wi = new WritableImage((int) canvasObject.getWidth(), (int) canvasObject.getHeight());
                    canvasObject.snapshot(sp, wi);
                    RenderedImage RI = SwingFXUtils.fromFXImage(
                            wi, null);
                    ImageIO.write(RI, "png", file_obj);
                    ImageIO.write(RI, "jpg", file_obj);
                    ImageIO.write(RI, "jpeg", file_obj);
                } catch (IOException ex) {
                    Logger.getLogger(Paint.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
               
        /**
         * HIGH KEYS FOR SAVE AND FILE MENU
         */
        Save.setAccelerator(new KeyCodeCombination (KeyCode.S,KeyCombination.CONTROL_DOWN));
        file_menu.setAccelerator(new KeyCodeCombination (KeyCode.F,KeyCombination.ALT_DOWN));
       
        //HELP BUTTON
        /**
         *  HELP BUTTON EXPLAINS WHAT THE PAINTERZ WORLD SOFTWARE IS CAPABLE OF!
         */
        Help.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Stage stage2 = new Stage();

                Text aboutText = new Text("With this software you can:\n \n"
                        + "- Draw a line on a picture of your choice.\n \n"
                        + "- Select a color .(Available in file menu)\n \n"
                        + "- Exit .(Available in file menu)\n \n"
                        + "- Save/Save as button(Available in file menu)\n \n"
                        + "- Line Drawer(Available in file menu)\n \n"
                        + "- Color Picker(Available on tool bar)\n \n"
                        + "- A pencil tool (freehand drawing of a line) in addition to drawing a straight line(Available in tool menu)\n \n"
                        + "- Width and color selection for pencil and line tools(Which can be found on the tool bar)\n \n"
                        + "- Smart save (Try it with the exit button ;))\n \n"
                        + "- Software can open and save multiple image file types(Open/save buttons in the file menu)\n \n"
                        + "- Keyboard UI controls (control S/Save, alt F/File menu, etc)\n \n"
                        + "- zoom in/out.(tool bar)\n \n"
                        + "- Draw a square, a rectangle, an ellipse, and a circle(Tools menu)\n \n"
                        + "- Blank canvas(Available on tool bar)\n \n"
                        + "- have an eraser tool(tool bar) \n \n"
                        + "- Live draw for shapes\n \n");

                BorderPane.setAlignment(aboutText, Pos.TOP_CENTER);
                BorderPane root = new BorderPane(aboutText);
                root.setPrefSize(400, 400);
                // Create the Scene
                Scene scene = new Scene(root);
                // Add the scene to the Stage
                stage2.setScene(scene);
                // Set the title of the Stage
                stage2.setTitle("Help");
                // Display the Stage

        	Image transparent = new Image("PaintLogo.png");
        	stage2.getIcons().add(transparent);

                stage2.show();
                Button b = new Button("Exit");
                b.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        stage2.close();
                    }
                });
                root.setBottom(b);
            }
        });
        /**ABOUT BUTTON
         * EXPLAINS WHO MADE THE APP AND WHAT VERSION IT IS
         */
       
        about.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Stage stage2 = new Stage();

                Text aboutText = new Text("Made by: Isaiah Daggett \n \n Painterz World 4.0 Standard Edition \n \n Expect newer features in the future!");

                BorderPane.setAlignment(aboutText, Pos.TOP_CENTER);
                BorderPane root = new BorderPane(aboutText);
                root.setPrefSize(400, 400);
                // Create the Scene
                Scene scene = new Scene(root);
                // Add the scene to the Stage
                stage2.setScene(scene);
                // Set the title of the Stage
                stage2.setTitle("Help");
                // Display the Stage
                
             	Image transparent = new Image("PaintLogo.png");
        	stage2.getIcons().add(transparent);
     
                stage2.show();
                Button b = new Button("Exit");
                b.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        stage2.close();
                    }
                });
                root.setBottom(b);
            }
        });
        /**
         * FIRST COLOR PICKER FOR LINE WHICH ALLOWS USERS TO SELECT THE COLOR OF THE LINE THEY DRAW
         */
        final ColorPicker colorPalette = new ColorPicker();
        colorPalette.setValue(Color.CORAL);
        Label shapes_lines = new Label("Color for lines:");
        final Text cs = new Text("Try the color picker!");
        cs.setFill(colorPalette.getValue());
        ToolBar tb_obj = new ToolBar();
        tb_obj.getItems().add(shapes_lines);
        tb_obj.getItems().add(colorPalette);
        root.getChildren().addAll(colorPalette, cs);
       
        Tooltip cp = new Tooltip("Color picker for lines");
        Tooltip.install(colorPalette,cp);
       
       
        TextField CT = new TextField();
        //CT Stands for current tool
        /**
         * ALLOWS USER TO DRAW A STRAIGHT LINE
         */                
        straightdraw.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Currenttool = "straightdraw";
                GraphicsContext graphicsContext = canvasObject.getGraphicsContext2D();
                if(Currenttool == "straightdraw"){
                    CT.setText("Straight draw");
                }
               
        canvasObject.setOnMousePressed(e -> {
            if(Currenttool == "straightdraw"){
               
            graphicsContext.beginPath();
            graphicsContext.lineTo(e.getX(), e.getY());
            graphicsContext.setLineWidth(linesize);
            }
        });
        canvasObject.setOnMouseDragged(e -> {
            if(Currenttool == "straightdraw"){
            graphicsContext.setLineWidth(linesize);
            graphicsContext.stroke();
            }
        });
        canvasObject.setOnMouseReleased(e -> {
            if(Currenttool == "straightdraw"){
            graphicsContext.lineTo(e.getX(), e.getY());
            graphicsContext.setLineWidth(linesize);
            graphicsContext.stroke();
            graphicsContext.closePath();
            }
        });
       if(Currenttool == "straightdraw"){
        colorPalette.setOnAction(e->graphicsContext.setStroke(colorPalette.getValue()));
       }
        stage.setScene(scene);
        stage.show();

            }
        });
       
        /**
         * IMAGEVIEWS ARE DISPLAYED THROUGHOUT THE CODE IN ORDER TO GIVE THE USER AN ICON IN ORDER TO LET THEM KNOW WHICH
         * TOOL THEY ARE SELECTING
         */
        Image drawIcon = new Image("straightline.png");
        ImageView drawView = new ImageView(drawIcon);
        drawView.setFitWidth(20);
        drawView.setFitHeight(20);
        straightdraw.setGraphic(drawView);
       
       
       
        /**
        *ALLOWS USER TO FREE DRAW ANYTHING WITH COLOR OF THEIR CHOICE
        */
        freedraw.setOnAction(new EventHandler<ActionEvent>() {          
            public void handle(ActionEvent event) {
                Currenttool = "freedraw";
                    GraphicsContext graphicsContext = canvasObject.getGraphicsContext2D();
                    if(Currenttool=="freedraw"){
                        CT.setText("Free draw");
                    }
                 
        canvasObject.setOnMousePressed(e -> {
            if(Currenttool=="freedraw"){
            graphicsContext.beginPath();
            graphicsContext.lineTo(e.getX(), e.getY());
            graphicsContext.setLineWidth(linesize);
            }
        });
       
        canvasObject.setOnMouseDragged(e -> {
            if(Currenttool=="freedraw"){
            graphicsContext.setLineWidth(linesize);
            graphicsContext.stroke();
            graphicsContext.lineTo(e.getX(), e.getY());
            }
        });
        canvasObject.setOnMouseReleased(e -> {
            if(Currenttool=="freedraw"){
            graphicsContext.setLineWidth(linesize);
            graphicsContext.stroke();
            graphicsContext.closePath();
            }
        });
        if(Currenttool=="freedraw"){
        colorPalette.setOnAction(e->graphicsContext.setStroke(colorPalette.getValue()));
        }
            }
        });
       
        /**
         * ANOTHER IMAGE FOR THE USER TO KNOW WHICH TOOL THEY ARE SELECTING
         */
        Image draw = new Image("pencil.png");
        ImageView drawV = new ImageView(draw);
        drawV.setFitWidth(20);
        drawV.setFitHeight(20);
        freedraw.setGraphic(drawV);
       
        /**
        *SECOND COLORPICKER FOR SHAPES THAT ALLOWS THE USER TO SELECT WHAT COLOR THEY WANT THE SHAPE THEY DRAW TO BE
        */
         final ColorPicker colorPalette2 = new ColorPicker();
        colorPalette2.setValue(Color.CORAL);
        final Text cs2 = new Text("Try the color picker!");
        cs2.setFill(colorPalette2.getValue());
        Label cp2_label = new Label("Color picker for shapes");
        tb_obj.getItems().addAll(cp2_label);
        tb_obj.getItems().addAll(colorPalette2);
        root.getChildren().addAll(cs2);
        Label slider_label = new Label("Slider for line thickness of shapes and lines");
        vb.getChildren().addAll(Menu_bar, tb_obj,slider_label, slider_obj);
       
        /**
        *ALLOWS USER TO DRAW SQUARES NON FREE HAND
        */
         
        drawSquare.setOnAction(new EventHandler<ActionEvent>() {          
            public void handle(ActionEvent event) {
                Currenttool="square";
                GraphicsContext graphicsContext = canvasObject.getGraphicsContext2D();
                if(Currenttool=="square"){
                    CT.setText("Square");
                }
               
            canvasObject.setOnMousePressed(e->{
                if(Currenttool=="square"){
                    x_1= e.getX();
                    y_1= e.getY();
                }
               
            });
            canvasObject.setOnMouseDragged(e -> {
                if(Currenttool=="square"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);              
                graphicsContext.strokeRect((double)y2m,(double)x2m,abs_x,abs_x);
                graphicsContext.setLineWidth(10);
                }
            });
           
            canvasObject.setOnMouseReleased(e->{
                if(Currenttool=="square"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeRect((double)y2m,(double)x2m,abs_x,abs_x);
                }
            });
            if(Currenttool=="square"){
                colorPalette2.setOnAction(e->graphicsContext.setStroke(colorPalette2.getValue()));
            }
             stage.setScene(scene);
             stage.show();
            }          
        });
        /**
         * ThirdDimensional SQUARE THAT ALLOWS USER TO DRAW SQUARES REPETITIVELY IN A PATTERN
         */
        S_square.setOnAction(new EventHandler<ActionEvent>() {          
            public void handle(ActionEvent event) {
                Currenttool="S_square";
                GraphicsContext graphicsContext = canvasObject.getGraphicsContext2D();
                if(Currenttool=="S_square"){
                    CT.setText("Sicko Square");
                }
            canvasObject.setOnMousePressed(e->{
                if(Currenttool=="S_square"){
                    x_1= e.getX();
                    y_1= e.getY();
                }
               
            });
            canvasObject.setOnMouseDragged(e -> {
                if(Currenttool=="S_square"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);              
                graphicsContext.strokeRect((double)y2m,(double)x2m,abs_x,abs_x);
                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.setFill(Color.BLACK);
                graphicsContext.setLineWidth(1);
                }
            });
           
            canvasObject.setOnMouseReleased(e->{
                if(Currenttool=="S_square"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeRect((double)y2m,(double)x2m,abs_x,abs_x);
                }
            });
            if(Currenttool=="S_square"){
                colorPalette.setOnAction(e->graphicsContext.setFill(colorPalette.getValue()));
            }
             stage.setScene(scene);
             stage.show();
            }          
        });
       
        Image Square = new Image("square.png");
        ImageView drawSq = new ImageView(Square);
        drawSq.setFitWidth(20);
        drawSq.setFitHeight(20);
        drawSquare.setGraphic(drawSq);
       
        /*
        *ALLOWS USERS TO DRAW RECTANGLES NON FREEHAND
        */
        drawRect.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Currenttool="rectangle";
                GraphicsContext graphicsContext = canvasObject.getGraphicsContext2D();
                if(Currenttool=="rectangle"){
                    CT.setText("rectangle");
                }
               
            canvasObject.setOnMousePressed(e->{
                if(Currenttool=="rectangle"){
                x_1= e.getX();
                y_1= e.getY();
                }
            });
            canvasObject.setOnMouseDragged(e->{
                if(Currenttool=="rectangle"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.setLineWidth(10);
                graphicsContext.strokeRect((double)y2m,(double)x2m,abs_x,abs_y);
                }
               
            });
           
            canvasObject.setOnMouseReleased(e->{
                if(Currenttool=="rectangle"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeRect((double)y2m,(double)x2m,abs_x,abs_y);
                }
               
            });
            if(Currenttool=="rectangle"){
            colorPalette2.setOnAction(e->graphicsContext.setStroke(colorPalette2.getValue()));
            }
            }
        });
        /**
         * THIS IS THE ThirdDimensional RECTANGLE WHICH ALLOWS USERS TO DRAW MULTIPLE RECTANGLES AT ONCE SO THAT IT COMES OUT AS A COOL DESIGN
         */
        S_rectangle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Currenttool="S_rectangle";
                GraphicsContext graphicsContext = canvasObject.getGraphicsContext2D();  
                if(Currenttool=="S_rectangle"){
                    CT.setText("Sicko Rectangle");
                }
            canvasObject.setOnMousePressed(e->{
                if(Currenttool=="S_rectangle"){
                x_1= e.getX();
                y_1= e.getY();
                }
            });
            canvasObject.setOnMouseDragged(e->{
                if(Currenttool=="S_rectangle"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeRect((double)y2m,(double)x2m,abs_x,abs_y);
                }              
            });  
            canvasObject.setOnMouseReleased(e->{
                if(Currenttool=="S_rectangle"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeRect((double)y2m,(double)x2m,abs_x,abs_y);
                }              
            });
            if(Currenttool=="S_rectangle"){
            colorPalette2.setOnAction(e->graphicsContext.setFill(colorPalette2.getValue()));
            }
            }
        });
       
        Image Rect = new Image("rectangle.png");
        ImageView drawRec = new ImageView(Rect);
        drawRec.setFitWidth(20);
        drawRec.setFitHeight(20);
        drawRect.setGraphic(drawRec);
       
       
        /*
        * ALLOWS USER TO DRAW OVALS NON FREEHAND
        */
        drawElipse.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Currenttool="elipse";
                GraphicsContext graphicsContext = canvasObject.getGraphicsContext2D();
                if(Currenttool=="elipse"){
                    CT.setText("Elipse");
                }
            canvasObject.setOnMousePressed(e->{
                if(Currenttool=="elipse"){
                x_1= e.getX();
                y_1= e.getY();
                }
            });    
            canvasObject.setOnMouseDragged(e->{
                if(Currenttool=="elipse"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeOval((double)y2m,(double)x2m,abs_x,abs_y);
                graphicsContext.setLineWidth(5);
                }              
            });
            canvasObject.setOnMouseReleased(e->{
                if(Currenttool=="elipse"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeOval((double)y2m,(double)x2m,abs_x,abs_y);
                }              
            });
            if(Currenttool=="elipse"){
            colorPalette2.setOnAction(e->graphicsContext.setStroke(colorPalette2.getValue()));
            }
            }
        });
       
        Image Elip = new Image("Elipse.png");
        ImageView Elip2 = new ImageView(Elip);
        Elip2.setFitWidth(20);
        Elip2.setFitHeight(20);
        drawElipse.setGraphic(Elip2);
        /*
        *ALLOWS  USER TO DRAW CIRCLES NON FREEHAND
        */
        drawCirc.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Currenttool="Circle";
                GraphicsContext graphicsContext = canvasObject.getGraphicsContext2D();
                if(Currenttool=="Circle"){
                    CT.setText("Circle");
                }
            canvasObject.setOnMousePressed(e->{
                if(Currenttool=="Circle"){
                x_1= e.getX();
                y_1= e.getY();
                }
            });  
            canvasObject.setOnMouseDragged(e->{
                if(Currenttool=="Circle"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeOval((double)y2m,(double)x2m,abs_x,abs_x);
                graphicsContext.setLineWidth(5);
                }                              
            });
            canvasObject.setOnMouseReleased(e->{
                if(Currenttool=="Circle"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeOval((double)y2m,(double)x2m,abs_x,abs_x);
                }                              
            });
            if(Currenttool=="Circle"){
            colorPalette2.setOnAction(e->graphicsContext.setStroke(colorPalette2.getValue()));
            }
            }
        });
        /**
         * ThirdDimensional MODE CIRCLE WHICH ALLOWS USER TO DRAW CIRCLES IN REPEATEDLY IN A NICE PATTERN
         */
        S_circle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Currenttool="S_Circle";
                GraphicsContext graphicsContext = canvasObject.getGraphicsContext2D();
                if(Currenttool=="S_Circle"){
                    CT.setText("Sicko Circle");
                }
            canvasObject.setOnMousePressed(e->{
                if(Currenttool=="S_Circle"){
                x_1= e.getX();
                y_1= e.getY();
                }
            });    
            canvasObject.setOnMouseDragged(e->{
                if(Currenttool=="S_Circle"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeOval((double)y2m,(double)x2m,abs_x,abs_x);
                }                              
            });
            canvasObject.setOnMouseReleased(e->{
                if(Currenttool=="S_Circle"){
                x_2 = e.getX();
                y_2 = e.getY();
                double y2m=Math.min(x_1,x_2);
                double x2m=Math.min(y_1,y_2);              
                double abs_x = Math.abs(x_1-x_2);
                double abs_y = Math.abs(y_1-y_2);
                graphicsContext.strokeOval((double)y2m,(double)x2m,abs_x,abs_x);
                }                              
            });
            if(Currenttool=="S_Circle"){
            colorPalette2.setOnAction(e->graphicsContext.setFill(colorPalette2.getValue()));
            }
            }
        });
       
        Image Circio = new Image("Circle.png");
        ImageView Circ = new ImageView(Circio);
        Circ.setFitWidth(20);
        Circ.setFitHeight(20);
        drawCirc.setGraphic(Circ);
       
        /**
         *ROUNDED RECT TOOL
         */        
        DrawRoundedRect.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Currenttool="Round";
                GraphicsContext graphicsContext = canvasObject.getGraphicsContext2D();
                if(Currenttool=="Round"){
                    CT.setText("Rounded rect");
                }

                canvasObject.setOnMousePressed(e -> {
                    if(Currenttool == "Round"){
                    x_1 = e.getX();
                    y_1 = e.getY();
                    }
                });
                canvasObject.setOnMouseReleased(e -> {
                    if(Currenttool == "Round"){
                    x_2 = e.getX();
                    y_2 = e.getY();
                    double y2m = Math.min(x_1, x_2);
                    double x2m = Math.min(y_1, y_2);
                    double abs_x = Math.abs(x_1 - x_2);
                    double abs_y = Math.abs(y_1 - y_2);
                    graphicsContext.strokeRoundRect((double) y2m, (double) x2m, abs_x, abs_y, 10, 15);
                    }
                });
            }
        });
       
        Image rounded = new Image("rounder.png");
        ImageView rounded2 = new ImageView(rounded);
        rounded2.setFitWidth(20);
        rounded2.setFitHeight(20);
        DrawRoundedRect.setGraphic(rounded2);
       
        //BLANK CANVAS BUTTON
        Button blank = new Button("Blank Canvas");
        blank.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Currenttool="Blank";
                /**
                 * Here is the method for the blank canvas button
                 * The purpose of this method is to allow the user to have a blank canvas to write on
                 *
                 */
                if(Currenttool=="Blank"){
                Image img_obj = new Image("whitepic.png");
                canvasObject.setWidth(img_obj.getWidth());
                canvasObject.setHeight(img_obj.getHeight());
                gc.drawImage(img_obj, 0, 0);
                canvasObject.setScaleX(2);
                canvasObject.setScaleY(2);
               
                }
            }
        });
        Tooltip bc = new Tooltip("Button for blank canvas");
        Tooltip.install(blank, bc);
       
        tb_obj.getItems().addAll(blank);
        //TEXT TOOL
        TextTool.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Currenttool="Text";
                if(Currenttool=="Text"){
                    CT.setText("Text");
                }
                if(Currenttool=="Text"){
                TextField tt = new TextField("Type here to add text");
                tb_obj.getItems().addAll(tt);              
                GraphicsContext gc = canvasObject.getGraphicsContext2D();
                gc.setTextAlign(TextAlignment.LEFT);
                gc.setTextBaseline(VPos.CENTER);
                Button Can_add = new Button("Add to picture");
                tb_obj.getItems().addAll(Can_add);                
                Can_add.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        gc.fillText(tt.getText(),Math.round(canvasObject.getWidth() / 2),Math.round(canvasObject.getHeight() / 2));
                    }
                });  
                }
            }            
        });
       
        Image text = new Image("text.png");
        ImageView text2 = new ImageView(text);
        text2.setFitWidth(20);
        text2.setFitHeight(20);
        TextTool.setGraphic(text2);
       
       
        //COLOR GRABBER/DROPPER
        ColorGrabber.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
               GraphicsContext gc = canvasObject.getGraphicsContext2D();
                canvasObject.setOnMousePressed(new EventHandler<MouseEvent>() {
                   @Override
                   public void handle(MouseEvent e) {
                       WritableImage wi = new WritableImage((int) canvasObject.getWidth(), (int) canvasObject.getHeight());
                       canvasObject.snapshot(null,wi);
                       wi.getPixelReader().getColor((int)e.getX(),(int)e.getY());
                       colorPalette.setValue(wi.getPixelReader().getColor((int)e.getX(),(int)e.getY()));
                   }
               });          
            }
        });
        Image CG = new Image("Color.png");
        ImageView CG2 = new ImageView(CG);
        CG2.setFitWidth(20);
        CG2.setFitHeight(20);
        ColorGrabber.setGraphic(CG2);
        //ZOOM IN BUTTON
      Zoom_In.setOnAction(new EventHandler<ActionEvent>() {        
            public void handle(ActionEvent event) {
                Currenttool="Zoom in";
                if(Currenttool=="Zoom in"){
                double zoomIn = 1.1;
                canvasObject.setScaleX(1 * zoomIn);
                canvasObject.setScaleY(1 * zoomIn);
                }
            }
        });
      //ZOOM OUT BUTTON
      Zoom_out.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Currenttool="Zoom out";
                if(Currenttool=="Zoom out"){
                double zoomIn = 1.1;
                canvasObject.setScaleX( 1/zoomIn);
                canvasObject.setScaleY( 1/zoomIn);
                }
            }
        });
        Eraser.setOnAction(new EventHandler<ActionEvent>() {            
            public void handle(ActionEvent event) {
                Currenttool="Eraser";
                if(Currenttool=="Eraser"){
                    CT.setText("Eraser");
                }
                if(Currenttool=="Eraser"){
                GraphicsContext gc = canvasObject.getGraphicsContext2D();
                canvasObject.setOnMousePressed(e -> {                
                        color = gc.getStroke();
                        x_1 = e.getX();
                        y_1 = e.getY();
                        gc.setStroke(Color.WHITE);
                        gc.setLineWidth(20);                  
                });
                canvasObject.setOnMouseDragged(e -> {          
                        gc.strokeLine(x_1, y_1, e.getX(), e.getY());
                        //gc.setStroke(Color.TRANSPARENT);
                        x_1 = e.getX();
                        y_1 = e.getY();    
                });
                canvasObject.setOnMouseReleased(e -> {                
                        gc.setStroke(color);
                        gc.setLineWidth(5);            
                });
            }
            }
        });  
       
        Image Erase = new Image("eraser.png");
        ImageView EraseSym = new ImageView(Erase);
        EraseSym.setFitWidth(20);
        EraseSym.setFitHeight(20);
        Eraser.setGraphic(EraseSym);
       
        Button undo = new Button("Undo");
        undo.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                Stack Undo = new Stack();
                WritableImage wi = new WritableImage((int) canvasObject.getWidth(), (int) canvasObject.getHeight());
                Undo.push(canvasObject.snapshot(null,wi));
                Image im = (Image) Undo.pop();
                gc.drawImage(im,0,0);
            }
       
        });
        Button redo = new Button("Redo");
        redo.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            }
        });
       
        tb_obj.getItems().addAll(undo);
        tb_obj.getItems().addAll(redo);
        Label indicator = new Label("Current tool");
        tb_obj.getItems().add(indicator);
        tb_obj.getItems().add(CT);
       

       
       
    }
   
       
    //Save Method
    //Save file method
    private void saveFile(Canvas canvasObject) {
        /**
         * This method is for the save as button which allows you to save
         * images that you have made in the paint App
         */
        FileChooser fc = new FileChooser();
        //fc stands for file chooser
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        fc.setTitle("Save Image");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "."),
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter(",.jpg", "*.jpg*"),
                new FileChooser.ExtensionFilter(".jpeg", "*.jpeg"),
                new FileChooser.ExtensionFilter(".png", "*.png"));
        File save = fc.showSaveDialog(stage);
        filepath = save.toURI().toString();
        if (save != null) {
            try {
                //String fileType = file.getName().substring(file.getName().lastIndexOf('.')+1);
                WritableImage wi = new WritableImage((int) canvasObject.getWidth(), (int) canvasObject.getHeight());
                SnapshotParameters sp = new SnapshotParameters();
                sp.setFill(Color.TRANSPARENT);  
                canvasObject.snapshot(sp, wi);
                //BufferedImage BI = SwingFXUtils.fromFXImage(wi,new BufferedImage((int) canvasObject.getWidth(), (int) canvasObject.getHeight(),BufferedImage.TYPE_INT_RGB));
                RenderedImage ri = SwingFXUtils.fromFXImage(wi, null);
                ImageIO.write(ri, "png", save);
               // ImageIO.write(ri, "jpg", save);
               // ImageIO.write(ri, "jpeg", save);
                file_obj = save;
            } catch (IOException e) {e.printStackTrace();}
        }
    }
    int square(int a){
        return a*a;
    }
    int Calc(int x,int y,int x1,int y1){
        return x*x1;
    }
    int Calc2(int x,int y,int x1,int y1){
        return x*y;
    }
    int Calc3(int x,int y,int x1,int y1){
        return x*y1;
    }
    int Calc4(int x,int y,int x1,int y1){
        return y*x1;
    }  
    /**
     * THIS IS THE PANET APP
     */
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}