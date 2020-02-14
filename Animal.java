import java.util.Random;

/**
 * AUTHOR: Justin Nichols
 * FILE: Animal.java
 * ASSIGNMENT: Programming Assignment 8 - CrisprGUIOut
 * COURSE: CSC210 Sring 2019 Section D
 * PURPOSE: provides methods and fields for an abstract Animal class. Can be
 * extended by different subclasses of animals
 */

abstract class Animal {
    protected final String species;
    protected final String gender;
    protected int row;
    protected int col;
    protected Ecosys ecosys;
    protected Pos[][] ecoMtx;
    protected boolean hasMvd = false;
    protected Pos oldPos;
    protected Pos newPos;
    protected int nRows;
    protected int nCols;
    protected boolean hasBred = false;
    protected Random rand = new Random();

    /*
     * constructor for Animal class
     * 
     * @param String species
     * 
     * @param String gender
     * 
     * @param int row (the row in the ecoMtx that this Animal first inhabits)
     * 
     * @param int col (the col in the ecoMtx that this Animal first inhabits)
     * 
     * @param Ecosys ecosys, the ecosystem in which this Animal resides
     * 
     */
    Animal(String species, String gender, int row, int col,
            Ecosys ecosys) {
        this.species = species;
        this.gender = gender;
        this.row = row;
        this.col = col;
        this.ecosys = ecosys;
        this.ecoMtx = ecosys.getEcoMtx();
        this.oldPos = ecoMtx[row][col];
        this.nRows = ecosys.getNRows();
        this.nCols = ecosys.getNCols();
    }

    /*
     * resets a given trait for this animal
     * 
     * @param String trait, the trait in question
     * 
     * @return void
     */
    void reset(String trait) {
        switch (trait) {
        case "mvd":
            hasMvd = false;
            break;
        case "bred":
            hasBred = false;
        }
    }

    /*
     * updates the Pos obj this Animal inhabits (will be given a better name
     * later)
     * 
     * @param n/a
     * 
     * @return void
     */
    void update() {
        Pos newPos = ecoMtx[row][col];
        oldPos.rm(this);
        newPos.addInhab(this);
        oldPos = newPos;
        hasMvd = true;
    }

    abstract void breedWith(Animal other);

    void mv() { hasMvd = true; }

    boolean hasMvd() { return hasMvd; }

    boolean hasBred() { return hasBred; }

    String getSpecies() { return species; }

    String getGender() { return gender; }

    @Override
    public String toString() {
        return species.substring(0, 1);
    }
}
