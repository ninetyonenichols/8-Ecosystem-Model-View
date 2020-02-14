
/**
 * AUTHOR: Justin Nichols
 * FILE: Pos.java
 * ASSIGNMENT: Programming Assignment 8 - CrisprGUIOut
 * COURSE: CSC210 Spring 2019 Section D
 * PURPOSE: provides methods and fields for a Position class, which manages
 * info about one position within an ecoMtx (Matrix-representation of an 
 * ecosystem) and can perform operations on that position
 */

import java.util.ArrayList;
import java.util.List;

public class Pos {
    private List<Animal> inhabs;

    public Pos() {
        this.inhabs = new ArrayList<Animal>();
    }

    /*
     * issues move command to all inhabitants of Pos obj
     * 
     * @param n/a
     * 
     * @return void
     */
    public void mvInhabs() {
        int i = 0;
        while (i < inhabs.size()) {
            Animal inhab = inhabs.get(i);
            if (!inhab.hasMvd()) {
                inhab.mv();
            } else {
                i++;
            }
        }
    }

    /*
     * issues move command to all inhabitants of Pos obj that match given
     * species
     * 
     * @param String species, the species in question.
     * 
     * @return void
     */
    public void mvInhabs(String species) {
        int i = 0;
        while (i < inhabs.size()) {
            Animal inhab = inhabs.get(i);
            
            if (!inhab.hasMvd() && inhab.getSpecies().equals(species)) {
                inhab.mv();
            } else {
                i++;
            }
        }
    }

    /*
     * issues move command to all inhabitants of Pos obj that are subclasses
     * of given class
     * 
     * @param Class mvClass, the class in question
     * 
     * @return void
     */
    public void mvInhabs(Class mvClass) {
        int i = 0;
        while (i < inhabs.size()) {
            Animal inhab = inhabs.get(i);
            Class inhabClass = inhab.getClass();
            if (!inhab.hasMvd() && (inhabClass.equals(mvClass)
                    || mvClass.isInstance(inhab))) {
                inhab.mv();
            } else {
                i++;
            }
        }
    }

    /*
     * issues command to all inabitants to reset a given trait
     * 
     * @param String trait, the trait in question.
     * 
     * @return void
     */
    public void reset(String trait) {
        for (Animal inhab : inhabs) {
            switch (trait) {
            case "mvd":
                inhab.reset("mvd");
                break;
            case "bred":
                inhab.reset("bred");
            }
        }
    }

    /*
     * issues breed command to first two inhabitants (does nothing if there are
     * fewer than two inhabs)
     * 
     * @param n/a
     * 
     * @return void
     */
    void breed() {
        if (inhabs.size() < 2) {
            return;
        }

        Animal inhab1 = inhabs.get(0);
        Animal inhab2 = inhabs.get(1);

        inhab1.breedWith(inhab2);
    }

    /*
     * issues breed command to first two inhabitants iff they are both members
     * of the given species (does nothing if there are fewer than two inhabs)
     * 
     * @param String species, the species in question
     * 
     * @return void
     */
    void breed(String species) {
        if (inhabs.size() < 2) {
            return;
        }

        Animal inhab1 = inhabs.get(0);
        Animal inhab2 = inhabs.get(1);
        
        if (inhab1.getSpecies().equals(species)
                && inhab2.getSpecies().equals(species)) {
            inhab1.breedWith(inhab2);
        }
    }

    public List<Animal> getInhabs() { return inhabs; }

    public void addInhab(Animal inhab) { inhabs.add(inhab); }

    public void rm(Animal inhab) { inhabs.remove(inhab); }

    public boolean isEmpty() { return inhabs.size() == 0; }

    @Override
    public String toString() {
        if (isEmpty()) {
            return ".";
        }
        Animal firstInhab = inhabs.get(0);
        return firstInhab.toString();
    }
}
