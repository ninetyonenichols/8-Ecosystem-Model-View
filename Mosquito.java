/**
 * AUTHOR: Justin Nichols
 * FILE: Mosquito.java
 * ASSIGNMENT: Programming Assignment 8 - CrisprGUIOut
 * COURSE: CSC210 Sring 2019 Section D
 * PURPOSE: provides methods and fields for a Mosquito class.
 */

public class Mosquito extends Insect {
    private final boolean gene1CRISPR;
    private final boolean gene2CRISPR;

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
     * 
     * @param boolean gene1CRISPR, determines whether first gene is CRISPR
     * 
     * @param boolean gene2CRISPR, determines whether second gene is CRISPR
     */
    public Mosquito(String species, String gender, int row, int col,
            Ecosys ecosys,
            boolean clockwise, boolean
            gene1CRISPR, boolean gene2CRISPR) {
        super(species, gender, row, col, ecosys, clockwise);
        this.gene1CRISPR = gene1CRISPR;
        this.gene2CRISPR = gene2CRISPR;
    }

    /*
     * attempts to breed this Mosquito with another Animal
     * 
     * @param Animal other, the other Animal in question.
     * 
     * @return void
     */
    @Override
    public void breedWith(Animal other) {
        if (!species.equals(other.getSpecies())
                || gender.equals(other.getGender()) || this.hasBred()
                || other.hasBred()) {
            return;
        }

        if ((gene1CRISPR && gene2CRISPR)
                || ((Mosquito) other).gene1CRISPR()
                        && ((Mosquito) other).gene2CRISPR) {
            return;
        }
                        
        boolean babyGene1CRISPR = (gene1CRISPR || gene2CRISPR);
        boolean babyGene2CRISPR = (((Mosquito) other).gene1CRISPR || ((Mosquito) other).gene2CRISPR);

        String gender = (rand.nextInt(2) == 0) ? "male" : "female";
        String cw = (rand.nextInt(2) == 0) ? "false" : "true";
        String coords = String.format("(%d,%d)", row, col);
        String[] infoArray = { "CREATE", coords, species, gender, cw,
                Boolean.toString(babyGene1CRISPR), Boolean.toString(babyGene2CRISPR) };
        ecosys.mkInhab(infoArray);

    }

    public boolean gene1CRISPR() { return gene1CRISPR; }

    public boolean gene2CRISPR() { return gene2CRISPR; }
}
