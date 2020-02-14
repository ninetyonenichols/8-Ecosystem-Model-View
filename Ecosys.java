
/**
 * AUTHOR: Justin Nichols
 * FILE: Ecosys.java
 * ASSIGNMENT: Programming Assignment 8 - CrisprGUIOut
 * COURSE: CSC210 Sring 2019 Section D
 * PURPOSE: provides methods and fields for an Ecosys class. Can be
 * populated and manipulated.
 */

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Ecosys {

    private Pos[][] ecoMtx;
    private final int nRows;
    private final int nCols;

    private final static Set<String> animalClasses = new HashSet<String>(
            Arrays.asList("bird", "mammal", "insect", "mosquito"));

    private final static Set<String> birdTypes = new HashSet<String>(
            Arrays.asList("thrush", "owl", "warbler", "shrike"));
    private final static Set<String> mammalTypes = new HashSet<String>(
            Arrays.asList("elephant", "rhinoceros", "lion", "giraffe",
                    "zebra"));
    private final static Set<String> insectTypes = new HashSet<String>(
            Arrays.asList("mosquito", "bee", "fly", "ant"));

    /*
     * constructs a new Ecosys obj
     * 
     * @param int nRows, the number of rows in the ecosystem
     * 
     * @param int nCols, the number of cols in the ecosystem
     * 
     * @return void
     */
    public Ecosys(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;

        ecoMtx = new Pos[nRows][nCols];
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                ecoMtx[row][col] = new Pos();
            }
        }
    }
    
    /*
     * makes a new animal and adds it to the ecosys
     * 
     * @param String[] infoArray. Contains info about the animal
     * 
     * @return void
     */
    public void mkInhab(String[] infoArray) {
        String coordsStr = infoArray[1];
        String[] coordsList = coordsStr.substring(1, coordsStr.length() - 1)
                .split(",");
        int row = Integer.parseInt(coordsList[0]);
        int col = Integer.parseInt(coordsList[1]);
        String species = infoArray[2].toLowerCase();
        String gender = infoArray[3].toLowerCase();

        Animal inhab = null;
        if (insectTypes.contains(species)) {
            boolean clockwise = Boolean.parseBoolean(infoArray[4]);

            switch (species) {
            case "mosquito":
                boolean gene1CRISPR = Boolean.parseBoolean(infoArray[5]);
                boolean gene2CRISPR = Boolean.parseBoolean(infoArray[6]);
                inhab = new Mosquito(species, gender, row, col, this,
                        clockwise, gene1CRISPR, gene2CRISPR);
                break;
            default:
                inhab = new Insect(species, gender, row, col, this, clockwise);
            }
        } else if (birdTypes.contains(species)) {
            int sideLen = Integer.parseInt(infoArray[4]);
            inhab = new Bird(species, gender, row, col, this, sideLen);
        } else if (mammalTypes.contains(species)) {
            String direction = infoArray[4];
            inhab = new Mammal(species, gender, row, col, this, direction);
        } else {
            System.out.println("invalid species entered");
            System.exit(1);
        }

        ecoMtx[row][col].addInhab(inhab);
    }

    /*
     * tells all Pos objs to move their inhabitants
     * 
     * @param String[] infoArray. Contains info about the command
     * 
     * @return void
     */
    public void mv(String[] infoArray) {
        CmdType cmdType = CmdType.parseCmdType(infoArray);
        String cmd = (infoArray.length == 1) ? null : infoArray[1];
        
        if (cmdType.equals(CmdType.COORDS)) {
            mvOnePos(cmd);
            return;
        }
        for (Pos[] row : ecoMtx) {
            for (Pos pos : row) {
                issueCorrectMvCmd(cmdType, cmd, pos);
            }
        }

        reset("mvd");
    }

    /*
     * an enum which stores info about optional commands in the infile
     */
    public enum CmdType {
        NULL, SPECIES, CLASS, COORDS;

        /*
         * determines which type of optional command was issued in the infile
         * 
         * @param String[] infoArray. Contains info about the command
         *
         * @return CmdType cmdType, an Enum. The type of optional command given
         */
        private static CmdType parseCmdType(String[] infoArray) {

            if (infoArray.length == 1) {
                return CmdType.NULL;
            }

            CmdType cmdType;
            String cmd = infoArray[1].toLowerCase();

            if (birdTypes.contains(cmd) || mammalTypes.contains(cmd)
                    || insectTypes.contains(cmd)) {
                cmdType = CmdType.SPECIES;
            } else if (animalClasses.contains(cmd)) {
                cmdType = CmdType.CLASS;
            } else {
                cmdType = CmdType.COORDS;
            }

            return cmdType;
        }
    }

    /*
     * a smaller version of the mv command. Only tells one Pos obj to move its
     * inhabitants
     * 
     * @param String cmd, an optional argument from the infile specifying which
     * animals to move
     * 
     * @return void
     */
    public void mvOnePos(String cmd) {
        String[] coords = cmd.substring(1, cmd.length() - 1).split(",");
        int row = Integer.parseInt(coords[0]);
        int col = Integer.parseInt(coords[1]);

        ecoMtx[row][col].mvInhabs();
        reset("mvd");
    }

    /*
     * determines which mv command to issue (this command is overloaded to help
     * it handle optional arguments).
     * 
     * @param CmdType cmdType, an Enum. The type of optional command given
     * 
     * @param String cmd, an optional argument from the infile specifying which
     * animals to move
     * 
     * @param Pos pos, the Pos object that the command will be given to.
     * 
     * @return void
     */
    public void issueCorrectMvCmd(CmdType cmdType, String cmd, Pos pos) {
        switch (cmdType) {
        case NULL:
            pos.mvInhabs();
            break;
        case SPECIES:
            pos.mvInhabs(cmd);
            break;
        case CLASS:
            cmd = cmd.substring(0, 1).toUpperCase()
                    + cmd.substring(1).toLowerCase();
            try {
                Class mvClass = Class.forName(cmd);
                pos.mvInhabs(mvClass);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found.");
                e.printStackTrace();
            }
        }
    }


    /*
     * resets a given trait for all animals in the ecosystem
     * 
     * @param String trait, the trait in question.
     * 
     * @return void
     */
    public void reset(String trait) {
        for (Pos[] row : ecoMtx) {
            for (Pos pos : row) {
                switch (trait) {
                case "mvd":
                    pos.reset("mvd");
                    break;
                case "bred":
                    pos.reset("bred");
                }
                pos.reset("mvd");
            }
        }
    }
    
    /*
     * issues breed command to all Pos objs
     * 
     * @param String[] infoArray. Contains info about the command
     * 
     * @return void
     */
    public void breed(String[] infoArray) {
        CmdType cmdType = CmdType.parseCmdType(infoArray);
        String cmd = (infoArray.length == 1) ? null
                : infoArray[1].toLowerCase();

        if (cmdType.equals(CmdType.COORDS)) {
            String[] coords = cmd.substring(1, cmd.length() - 1).split(",");
            int row = Integer.parseInt(coords[0]);
            int col = Integer.parseInt(coords[1]);

            ecoMtx[row][col].breed();
            reset("bred");
            return;
        }

        for (Pos[] row : ecoMtx) {
            for (Pos pos : row) {
                switch (cmdType) {
                case NULL:
                    pos.breed();
                    break;
                case SPECIES:
                    pos.breed(cmd);
                }
            }
        }

        reset("bred");
    }

    /*
     * prints out the inhabs of each spot in the ecosystem
     * 
     * @param n/a
     * 
     * @return void
     */
    public void print() {
        for (Pos[] row : ecoMtx) {
            for (Pos pos : row) {
                System.out.print(pos);
            }
            System.out.println();
        }
    }

    public int getNRows() { return nRows; }

    public int getNCols() { return nCols; }

    public Pos[][] getEcoMtx() { return ecoMtx; }
}
