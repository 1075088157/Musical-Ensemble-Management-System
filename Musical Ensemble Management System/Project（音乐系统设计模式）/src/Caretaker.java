import java.util.Stack;

public class Caretaker {
    private Stack<Memento> undoList = new Stack<>();
    private Stack<Memento> redoList = new Stack<>();
    
    public void saveState(Ensemble ensemble) {
        redoList.clear();
        Memento m = new Memento(ensemble);
        undoList.push(m);
    }
    
    public void undo() {
        if (!undoList.isEmpty()) {
            Memento m = undoList.pop();
            Memento rm = new Memento(m.getEnsemble());
            m.restore();
            redoList.push(rm);
        } else {
            System.out.println("Nothing to undo!");
        }
    }
        
    public void redo() {
        if (!redoList.isEmpty()) {
            Memento m = redoList.pop();
            Memento um = new Memento(m.getEnsemble());
            m.restore();
            undoList.push(um);
        } else {
            System.out.println("Nothing to redo!");
        }
    }
    
    public Stack<Memento> getUndoList() {
        return undoList;
    }
    
    public Stack<Memento> getRedoList() {
        return redoList;
    }
}

class Memento {
    private Ensemble ensemble;
    private String mementoEnsembleId;
    private String mementoEnsembleName;
    
    public Memento(Ensemble ensemble) {
        this.ensemble = ensemble;
        this.mementoEnsembleId = ensemble.getEnsembleId();
        this.mementoEnsembleName = ensemble.getEnsembleName();
    }
    
    public Ensemble getEnsemble() {
        return ensemble;
    }
    
    public void restore() {
        ensemble.setEnsembleName(mementoEnsembleName);
    }
    
    @Override
    public String toString() {
        return "Ensemble: " + mementoEnsembleId + ", " + mementoEnsembleName;
    }
}