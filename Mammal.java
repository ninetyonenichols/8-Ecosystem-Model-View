/**
 * AUTHOR: Justin Nichols
 * FILE: Mammal.java
 * ASSIGNMENT: Programming Assignment 8 - CrisprGUIOut
 * COURSE: CSC210 Spring 2019 Section D
 * PURPOSE: provides methods and fields for a Mammal class.
 */

public class Mammal extends Animal {
    private final String dir;
    private String orient;
    private int numTimesBred = 0;
    private final int MAX_NUM_TIMES_BRED = 5;

    /*
     * constructor for Mammal class
     * 
     * @param String species
     * 
     * @param String gender
     * 
     * @param int row (the row in the ecoMtx that this Mammal first inhabits)
     * 
     * @param int col (the col in the ecoMtx that this Mammal first inhabits)
     * 
     * @param Ecosys ecosys, the ecosystem in which this Mammal resides
     * 
     * @param String dir, determines the path that this Mammal will take.
     * If dir.equals("left"), this animal will alternate between going up and
     * left. If dir.equals("right"), this animal will alternate between going
     * down and right.
     */
    public Mammal(String species, String gender, int row, int col,
            Ecosys ecosys,
            String dir) {
        super(species, gender, row, col, ecosys);
        this.dir = dir;
        this.orient = "vert";
    }

    /*
     * move command for this Mammal
     * 
     * @param n/a
     * 
     * @return void
     */
    public void mv() {

        // calculating new row, col, and orient
        switch (orient) {
        case "vert":
            row = dir.equals("left") ? Math.floorMod(row - 1, nRows)
                    : Math.floorMod(row + 1, nRows);
            setOrient("horz");
            break;
        case "horz":
            col = dir.equals("left") ? Math.floorMod(col - 1, nCols)
                    : Math.floorMod(col + 1, nCols);
            setOrient("vert");
        }

        // changing which Pos obj keeps track of this Mammal
        update();
    }


    /*
     * attempts to breed this Mammal with another Animal
     * 
     * @param Animal other, the other Animal in question
     * 
     * @return void
     */
    public void breedWith(Animal other) {
        if (!species.equals(other.getSpecies()) || hasBred || other.hasBred()
                || gender.equals(other.getGender())
                || numTimesBred() >= MAX_NUM_TIMES_BRED
                || ((Mammal) other).numTimesBred() >= MAX_NUM_TIMES_BRED) {
            return;
        }
        String gender = (rand.nextInt(2) == 0) ? "male" : "female";
        String childDir = (rand.nextInt(2) == 0) ? "left" : "right";
        String coords = String.format("(%d,%d)", row, col);
        String[] infoArray = { "CREATE", coords, species, gender, childDir };
        ecosys.mkInhab(infoArray);

        incrNumTimesBred();
        ((Mammal) other).incrNumTimesBred();
    }

    public void setOrient(String orient) { this.orient = orient; }

    public int numTimesBred() { return numTimesBred; }

    public void incrNumTimesBred() { numTimesBred += 1; }
}
