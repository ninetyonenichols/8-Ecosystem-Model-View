/**
 * AUTHOR: Justin Nichols
 * FILE: Bird.java
 * ASSIGNMENT: Programming Assignment 8 - CrisprGUIOut
 * COURSE: CSC210 Sring 2019 Section D
 * PURPOSE: provides methods and fields for a Bird class.
 */

public class Bird extends Animal {
    private final int sideLen;
    private final String[] dirs = { "down", "right", "up" };
    private String dir;
    private int dirIndex = 0;
    private int soFar = 0;

    /*
     * constructor for Bird class
     * 
     * @param String species
     * 
     * @param String gender
     * 
     * @param int row (the row in the ecoMtx that this Bird first inhabits)
     * 
     * @param int col (the col in the ecoMtx that this Bird first inhabits)
     * 
     * @param Ecosys ecosys, the ecosystem in which this Bird resides
     * 
     * @param int sideLen, the length that this Bird will go before changing
     * direction
     */
    public Bird(String species, String gender, int row, int col,
            Ecosys ecosys, int sideLen) {
        super(species, gender, row, col, ecosys);
        this.sideLen = sideLen;
    }

    /*
     * moves this Bird
     * 
     * @param n/a
     * 
     * @return void
     */
    public void mv() {
        dir = dirs[dirIndex];

        switch (dir) {
        case "down":
            row = Math.floorMod(row + 1, nRows);
            break;
        case "right":
            col = Math.floorMod(col + 1, nCols);
            break;
        case "up":
            row = Math.floorMod(row - 1, nRows);
        }

        soFar = Math.floorMod(soFar + 1, sideLen);
        dirIndex = (soFar == 0) ? Math.floorMod(dirIndex += 1, dirs.length)
                : dirIndex;

        update();
    }

    /*
     * attempts to breed this Bird with another Animal
     * 
     * @param Animal other, the other animal in question.
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
        String coords = String.format("(%d,%d)", row, col);
        String[] infoArray = { "CREATE", coords, species, gender, "5" };
        ecosys.mkInhab(infoArray);
    }
}
