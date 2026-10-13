/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.Vector;

public abstract class Ensemble {
    protected String ensembleId;
    protected String ensembleName;
    protected Vector<Musician> musicians;

    public Ensemble(String ensembleId, String ensembleName) {
        this.ensembleId = ensembleId;
        this.ensembleName = ensembleName;
        this.musicians = new Vector<>();
    }

    public String getEnsembleId() {
        return ensembleId;
    }

    public String getEnsembleName() {
        return ensembleName;
    }

    public void setEnsembleName(String ensembleName) {
        this.ensembleName = ensembleName;
    }

    public Vector<Musician> getMusicians() {
        return musicians;
    }

    public void addMusician(Musician musician) {
        musicians.add(musician);
    }

    public void removeMusician(String musicianId) {
        for (int i = 0; i < musicians.size(); i++) {
            if (musicians.get(i).getId().equals(musicianId)) {
                musicians.remove(i);
                break;
            }
        }
    }

    public Musician findMusician(String musicianId) {
        for (Musician musician : musicians) {
            if (musician.getId().equals(musicianId)) {
                return musician;
            }
        }
        return null;
    }

    public abstract void updateMusicianRole(String musicianId, String newInstrument);
    public abstract void showEnsemble();
}

 class OrchestraEnsemble extends Ensemble {
    
    public OrchestraEnsemble(String ensembleId, String ensembleName) {
        super(ensembleId, ensembleName);
    }

    @Override
    public void updateMusicianRole(String musicianId, String newInstrument) {
        Musician musician = findMusician(musicianId);
        if (musician != null) {
            musician.setInstrument(newInstrument);
        }
    }

    @Override
    public void showEnsemble() {
        System.out.println("Orchestra Ensemble " + ensembleName + " (" + ensembleId + ")");
        
        System.out.println("Violinist:");
        boolean hasViolinist = false;
        for (Musician musician : musicians) {
            if (musician.getInstrument().equals("violinist")) {
                System.out.println(musician);
                hasViolinist = true;
            }
        }
        if (!hasViolinist) {
            System.out.println("NIL");
        }
        
        System.out.println("Cellist:");
        boolean hasCellist = false;
        for (Musician musician : musicians) {
            if (musician.getInstrument().equals("cellist")) {
                System.out.println(musician);
                hasCellist = true;
            }
        }
        if (!hasCellist) {
            System.out.println("NIL");
        }
    }
    
    @Override
    public String toString() {
        return "Orchestra Ensemble " + ensembleName + " (" + ensembleId + ")";
    }
}


 class JazzBandEnsemble extends Ensemble {
    
    public JazzBandEnsemble(String ensembleId, String ensembleName) {
        super(ensembleId, ensembleName);
    }

    @Override
    public void updateMusicianRole(String musicianId, String newInstrument) {
        Musician musician = findMusician(musicianId);
        if (musician != null) {
            musician.setInstrument(newInstrument);
        }
    }

    @Override
    public void showEnsemble() {
        System.out.println("Jazz Band Ensemble " + ensembleName + " (" + ensembleId + ")");
        
        System.out.println("Pianist:");
        boolean hasPianist = false;
        for (Musician musician : musicians) {
            if (musician.getInstrument().equals("pianist")) {
                System.out.println(musician);
                hasPianist = true;
            }
        }
        if (!hasPianist) {
            System.out.println("NIL");
        }
        
        System.out.println("Saxophonist:");
        boolean hasSaxophonist = false;
        for (Musician musician : musicians) {
            if (musician.getInstrument().equals("saxophonist")) {
                System.out.println(musician);
                hasSaxophonist = true;
            }
        }
        if (!hasSaxophonist) {
            System.out.println("NIL");
        }
        
        System.out.println("Drummer:");
        boolean hasDrummer = false;
        for (Musician musician : musicians) {
            if (musician.getInstrument().equals("drummer")) {
                System.out.println(musician);
                hasDrummer = true;
            }
        }
        if (!hasDrummer) {
            System.out.println("NIL");
        }
    }
    
    @Override
    public String toString() {
        return "Jazz Band Ensemble " + ensembleName + " (" + ensembleId + ")";
    }
}