/**
 * AUTHOR: Justin Nichols
 * FILE: Insect.java
 * ASSIGNMENT: Programming Assignment 8 - CrisprGUIOut
 * COURSE: CSC210 Sring 2019 Section D
 * PURPOSE: provides methods and fields for an Insect class.
 */

public class Insect extends Animal {
    protected final boolean clockwise;
    private final String[] dirs = { "left", "up", "right", "down" };
    private int dirIndex = 0;
    private String dir;
    private int sideLen = 1;
    private int stepsTaken = 0;

    /*
     * constructor for Mosquito class
     * 
     * @param String species
     * 
     * @param String gender
     * 
     * @param int row (the row in the ecoMtx that this Insect first inhabits)
     * 
     * @param int col (the col in the ecoMtx that this Insect first inhabits)
     * 
     * @param Ecosys ecosys, the ecosystem in which this Insect resides
     * 
     * @param boolean clockwise, determines whether the insect moves clockwise
     * (if true) or counterclockwise (if false)
     */
    public Insect(String species, String gender, int row, int col,
            Ecosys ecosys,
            boolean clockwise) {
        super(species, gender, row, col, ecosys);
        this.clockwise = clockwise;
    }

    /*
     * move command for this Insect
     * 
     * @param n/a
     * 
     * @return void
     */
    public void mv() {
        dir = dirs[dirIndex];

        switch (dir) {
        case "left":
            col = Math.floorMod(col - 1, nCols);
            break;
        case "up":
            row = Math.floorMod(row - 1, nRows);
            break;
        case "right":
            col = Math.floorMod(col + 1, nCols);
            break;
        case "down":
            row = Math.floorMod(row + 1, nRows);
        }

        stepsTaken = Math.floorMod(stepsTaken + 1, sideLen);
        if (stepsTaken == 0) {
            dirIndex = (clockwise) ? Math.floorMod(dirIndex + 1, dirs.length)
                    : Math.floorMod(dirIndex - 1, dirs.length);
            sideLen = (dirIndex == 0) ? sideLen + 1 : sideLen;
        }

        update();
    }

    /*
     * attempts to breed this Insect with another Animal
     * 
     * @param Animal other, the other Animal in question
     * 
     * @return void
     */
    public void breedWith(Animal other) {
        if (!species.equals(other.getSpecies())
                || gender.equals(other.getGender()) || this.hasBred()
                || other.hasBred()) {
            return;
        }

        String gender = (rand.nextInt(2) == 0) ? "male" : "female";
        String cw = (rand.nextInt(2) == 0) ? "false" : "true";
        String coords = String.format("(%d,%d)", row, col);
        String[] infoArray = { "CREATE", coords, species, gender, cw };
        ecosys.mkInhab(infoArray);
    }
}
