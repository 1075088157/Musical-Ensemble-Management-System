import java.util.Scanner;

public class EnsembleFactory {
    
    public static Ensemble createEnsemble(String type, String ensembleId, String ensembleName) {
        switch (type.toLowerCase()) {
            case "o":
            case "orchestra":
                return new OrchestraEnsemble(ensembleId, ensembleName);
            case "j":
            case "jazz":
                return new JazzBandEnsemble(ensembleId, ensembleName);
            default:
                return null;
        }
    }
    
    public static Musician createMusician(String musicianId, String musicianName, String instrumentType, String ensembleType) {
        String instrument = "";
        
        if (ensembleType.equals("o") || ensembleType.equals("orchestra")) {
            switch (instrumentType) {
                case "1":
                    instrument = "violinist";
                    break;
                case "2":
                    instrument = "cellist";
                    break;
                default:
                    instrument = "unknown";
            }
        } else if (ensembleType.equals("j") || ensembleType.equals("jazz")) {
            switch (instrumentType) {
                case "1":
                    instrument = "pianist";
                    break;
                case "2":
                    instrument = "saxophonist";
                    break;
                case "3":
                    instrument = "drummer";
                    break;
                default:
                    instrument = "unknown";
            }
        }
        
        return new Musician(musicianId, musicianName, instrument);
    }
}