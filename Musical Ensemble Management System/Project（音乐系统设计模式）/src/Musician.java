/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
public class Musician {
    private String id;
    private String name;
    private String instrument;

    public Musician(String id, String name, String instrument) {
        this.id = id;
        this.name = name;
        this.instrument = instrument;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    @Override
    public String toString() {
        return id + ", " + name;
    }
}
