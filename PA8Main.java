
/**
 * AUTHOR: Justin Nichols
 * FILE: PA8Main.java
 * ASSIGNMENT: Programming Assignment 8 - CrisprGUIOut
 * COURSE: CSC210 Spring 2019, Section D
 * PURPOSE: simulates an ecosystem to study the effects of releasing mosquitos 
 * with CRISPR genes and displays a graphical output
 *              
 * 
 * USAGE: 
 * java PA8Main infileName
 * 
 * where: infileName is the path to a file. This file will need to provide 
 * well-formed info on how to populate and manipulate the ecosystem
 *     
 *  EXAMPLE INPUT (CREATED BY INSTRUCTORS, NOT BY ME)--
 *      Input File:                       
 *   
 * -----------------------------------
 * | rows: 10                        | 
 * | cols: 10                        |
 * |                                 |
 * | CREATE (1,1) lion female left   |
 * | CREATE (1,1) bee male true      |
 * | REPRODUCE                       |
 * | PRINT                           |
 * | MOVE                            |
 * | PRINT                           |
 * -----------------------------------
 *  
 * Input-file format must match that shown above.
 * No support exists for any further commands
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PA8Main extends Application {

    private static double delay;
    private final static int TEXT_SIZE = 120;
    private final static int RECT_SIZE = 20;
    private static int nRows;
    private static int nCols;
    private static Ecosys ecosys;
    private static Scanner infile;
    private static Map<String, Color> species2Color = new HashMap<String, Color>();

    public static void main(String[] args) {
        infile = getInfile(args);
        ecosys = mkEcosys(infile);
        nRows = ecosys.getNRows();
        nCols = ecosys.getNCols();
        buildSpecies2Color();

        String line = infile.nextLine();
        delay = Double.parseDouble(line.split(" ")[1]);
        line = infile.nextLine();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TextArea command = new TextArea();
        GraphicsContext gc = setupStage(primaryStage, RECT_SIZE * nCols,
                RECT_SIZE * nRows, command);

        primaryStage.show();
        simulateEcosystem(gc, command);
    }

    /*
     * maps each species to a unique color that will be used to represent it
     */
    public static void buildSpecies2Color() {
        // mammals
        species2Color.put("elephant", Color.RED);
        species2Color.put("rhinoceros", Color.ORANGE);
        species2Color.put("lion", Color.YELLOW);
        species2Color.put("giraffe", Color.GREEN);
        species2Color.put("zebra", Color.BLUE);

        // birds
        species2Color.put("thrush", Color.PURPLE);
        species2Color.put("owl", Color.BLACK);
        species2Color.put("warbler", Color.WHITE);
        species2Color.put("shrike", Color.SILVER);

        // insects
        species2Color.put("mosquito", Color.PALEVIOLETRED);
        species2Color.put("bee", Color.LIGHTGOLDENRODYELLOW);
        species2Color.put("fly", Color.BLANCHEDALMOND);
        species2Color.put("ant", Color.CYAN);
    }

    /*
     * returns the infile in Scanner-form
     * 
     * @Param String[] args, the command-line arguments. Args[0] is the
     * infile's name
     * 
     * @Return Scanner infile. The infile in Scanner-form
     */
    public static Scanner getInfile(String[] args) {
        // retrieving the infile
        String infileName = args[0];
        Scanner infile = null;

        try {
            infile = new Scanner(new File(infileName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(1);
        }

        return infile;
    }

    /*
     * initializes the ecosys
     * 
     * @Param Scanner infile. The infile in Scanner-form
     * 
     * @Return void
     */
    public static Ecosys mkEcosys(Scanner infile2) {
        String line = infile.nextLine();
        int nRows = Integer.parseInt(line.split(" ")[1]);

        line = infile.nextLine();
        int nCols = Integer.parseInt(line.split(" ")[1]);

        return new Ecosys(nRows, nCols);
    }

    /*
     * updates the ecosystem with each command from the infile and then redraws
     * it, after a pause of 'delay' seconds
     * 
     * @param gc
     * GraphicsContext for drawing ecosystem to.
     * 
     * @param command
     * Reference to text area to show simulation commands.
     */
    private void simulateEcosystem(GraphicsContext gc, TextArea command) {
        PauseTransition wait = new PauseTransition(Duration.seconds(delay));
        wait.setOnFinished(new CommandHandler(gc, command, wait));
        wait.play();
    }

    /*
     * handles each command from the infile
     */
    class CommandHandler implements EventHandler<ActionEvent> {
        private GraphicsContext gc;
        private TextArea command;
        private PauseTransition wait;

        /*
         * constructor for this EventHandler
         * 
         * @param gc
         * GraphicsContext to which ecosystem will be drawn
         * 
         * @param command
         * TextArea which keeps log of commands
         * 
         * @param wait
         * PauseTransition which handles delay between frames
         */
        CommandHandler(GraphicsContext gc, TextArea command,
                PauseTransition wait) {
            this.gc = gc;
            this.command = command;
            this.wait = wait;
        }

        /*
         * handles each command from the infile, and then draws the updated
         * ecosystem
         */
        @Override
        public void handle(ActionEvent e) {
            if (infile.hasNextLine()) {
                String line = infile.nextLine();

                String[] infoArray = line.split(" ");
                String cmd = infoArray[0].toUpperCase();

                switch (cmd) {
                case "CREATE":
                    ecosys.mkInhab(infoArray);
                    break;
                case "MOVE":
                    ecosys.mv(infoArray);
                case "REPRODUCE":
                    ecosys.breed(infoArray);
                    break;
                }

                ecosystemDraw(gc);
                command.appendText(line + "\n");

                wait.playFromStart();
            } else {
                wait.stop();
            }
        }
    }

    /**
     * Sets up the whole application window and returns the GraphicsContext from
     * the canvas to enable later drawing. Also sets up the TextArea, which
     * should be originally be passed in empty.
     * PA8 Notes: You shouldn't need to modify this method.
     * 
     * @param primaryStage
     *            Reference to the stage passed to start().
     * @param canvas_width
     *            Width to draw the canvas.
     * @param canvas_height
     *            Height to draw the canvas.
     * @param command
     *            Reference to a TextArea that will be setup.
     * @return Reference to a GraphicsContext for drawing on.
     */
    public GraphicsContext setupStage(Stage primaryStage, int canvas_width,
            int canvas_height, TextArea command) {
        // Border pane will contain canvas for drawing and text area underneath
        BorderPane p = new BorderPane();

        // Canvas(pixels across, pixels down)
        // Note this is opposite order of parameters of the Ecosystem in PA6.
        Canvas canvas = new Canvas(canvas_width, canvas_height);

        // Command TextArea will hold the commands from the file
        command.setPrefHeight(TEXT_SIZE);
        command.setEditable(false);

        // Place the canvas and command output areas in pane.
        p.setCenter(canvas);
        p.setBottom(command);

        // Title the stage and place the pane into the scene into the stage.
        primaryStage.setTitle("Ecosystem");
        primaryStage.setScene(new Scene(p));

        return canvas.getGraphicsContext2D();
    }

    /*
     * iterates over the ecoMtx so that each position can be drawn
     * 
     * @param gc
     * GraphicsContext for drawing ecosystem to.
     */
    private void ecosystemDraw(GraphicsContext gc) {
        gc.setFill(Color.TAN);
        gc.fillRect(0, 0, RECT_SIZE * nRows, RECT_SIZE * nCols);

        Pos[][] ecoMtx = ecosys.getEcoMtx();
        for (int i = 0; i < ecoMtx.length; i++) {
            for (int j = 0; j < ecoMtx.length; j++) {
                posDraw(gc, ecoMtx, i, j);
            }
        }
    }

    /*
     * draws a given position
     * 
     * @param gc
     * GraphicsContext for drawing ecosystem to.
     * 
     * @param ecoMtx
     * Pos[][] representation of the ecosystem
     * 
     * @param i
     * int, the position's row in the ecoMtx
     * 
     * @param j
     * int, the position's column in the ecoMtx
     */
    private void posDraw(GraphicsContext gc, Pos[][] ecoMtx, int i, int j) {
        Pos pos = ecoMtx[i][j];
        if (!pos.isEmpty()) {
            Animal firstInhab = pos.getInhabs().get(0);
            String species = firstInhab.getSpecies();
            Color color = species2Color.get(species);
            gc.setFill(color);
            gc.fillRect(RECT_SIZE * j, RECT_SIZE * i, RECT_SIZE, RECT_SIZE);
        }
    }
}
