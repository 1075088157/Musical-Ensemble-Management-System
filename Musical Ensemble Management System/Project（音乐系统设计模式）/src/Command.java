
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public interface Command {

    void execute();

    void undo();

    void redo();
}

class CreateEnsembleCommand implements Command {
    private Vector<Ensemble> ensembles;
    private Ensemble ensemble;
    private Scanner sc;
    private String ensembleId;
    private Caretaker caretaker;
    private SetCurrentEnsembleCommand setCurrentCmd;
    private Ensemble previousCurrentEnsemble; // 新增：保存之前的当前乐团

    public CreateEnsembleCommand(Vector<Ensemble> ensembles, Scanner sc, Caretaker caretaker, SetCurrentEnsembleCommand setCurrentCmd) {
        this.ensembles = ensembles;
        this.sc = sc;
        this.caretaker = caretaker;
        this.setCurrentCmd = setCurrentCmd;
        this.ensemble = null;
        this.previousCurrentEnsemble = setCurrentCmd.getCurrentEnsemble(); // 保存执行前的当前乐团
    }

    @Override
    public void execute() {
        System.out.print("Enter music type (o = orchestra | j = jazz band):- ");
        String type = sc.next();
        
        System.out.print("Ensemble ID:- ");
        this.ensembleId = sc.next();
        
        System.out.print("Ensemble Name:- ");
        sc.nextLine(); // 消耗换行符
        String ensembleName = sc.nextLine(); // 使用nextLine读取完整名称
        
        ensemble = EnsembleFactory.createEnsemble(type, ensembleId, ensembleName);
        if (ensemble != null) {
            ensembles.add(ensemble);
            if (type.equals("o")) {
                System.out.println("Orchestra ensemble is created.");
            } else {
                System.out.println("Jazz band ensemble is created.");
            }
            System.out.println("Current ensemble is changed to " + ensembleId + ".");
            
            // 自动设置为当前乐团
            if (setCurrentCmd != null) {
                setCurrentCmd.setCurrentEnsemble(ensemble);
            }
        }
    }

    @Override
    public void undo() {
        if (ensemble != null) {
            ensembles.remove(ensemble);
            // 恢复之前的当前乐团
            if (setCurrentCmd != null) {
                setCurrentCmd.setCurrentEnsemble(previousCurrentEnsemble);
            }
        }
    }

    @Override
    public void redo() {
        if (ensemble != null) {
            ensembles.add(ensemble);
            // 重新设置当前乐团
            if (setCurrentCmd != null) {
                setCurrentCmd.setCurrentEnsemble(ensemble);
            }
        }
    }
    
    @Override
    public String toString() {
        if (ensemble instanceof OrchestraEnsemble) {
            return "Create orchestra ensemble, " + ensembleId + ", " + ensemble.getEnsembleName();
        } else {
            return "Create jazz band ensemble, " + ensembleId + ", " + ensemble.getEnsembleName();
        }
    }
}

class SetCurrentEnsembleCommand implements Command {
    private Vector<Ensemble> ensembles;
    private Scanner sc;
    private Ensemble currentEnsemble;
    private Ensemble previousEnsemble;

    public SetCurrentEnsembleCommand(Vector<Ensemble> ensembles, Scanner sc) {
        this.ensembles = ensembles;
        this.sc = sc;
        this.currentEnsemble = null;
        this.previousEnsemble = null;
    }

    @Override
    public void execute() {
        System.out.print("Please input ensemble ID:- ");
        String ensembleId = sc.next();
        
        boolean found = false;
        for (Ensemble e : ensembles) {
            if (e.getEnsembleId().equals(ensembleId)) {
                previousEnsemble = currentEnsemble;
                currentEnsemble = e;
                System.out.println("Changed current ensemble to " + ensembleId + ".");
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("Ensemble " + ensembleId + " is not found!!");
        }
    }

    @Override
    public void undo() {
        currentEnsemble = previousEnsemble;
    }

    @Override
    public void redo() {
        // Redo not applicable for set current ensemble
    }
    
    public void setCurrentEnsemble(Ensemble ensemble) {
        this.currentEnsemble = ensemble;
    }
    
    public Ensemble getCurrentEnsemble() {
        return currentEnsemble;
    }
    
    @Override
    public String toString() {
        return "Set current ensemble";
    }
}

class AddMusicianCommand implements Command {
    private Ensemble currentEnsemble;
    private Scanner sc;
    private Musician musician;
    private String ensembleType;
    private SetCurrentEnsembleCommand setCurrentCmd;

    public AddMusicianCommand(Ensemble currentEnsemble, Scanner sc, String ensembleType, SetCurrentEnsembleCommand setCurrentCmd) {
        this.currentEnsemble = currentEnsemble;
        this.sc = sc;
        this.ensembleType = ensembleType;
        this.setCurrentCmd = setCurrentCmd;
        this.musician = null;
    }

    @Override
    public void execute() {
        if (currentEnsemble == null) {
            System.out.println("No current ensemble selected!");
            return;
        }

        System.out.print("Please input musician information (id, name):- ");
        sc.nextLine(); // 消耗换行符
        String input = sc.nextLine();
        String[] parts = input.split(",");

        if (parts.length < 2) {
            System.out.println("Invalid input format!");
            return;
        }

        String musicianId = parts[0].trim();
        String musicianName = parts[1].trim();

        if (ensembleType.equals("o")) {
            System.out.print("Instrument (1 = violinist | 2 = cellist ):- ");
        } else {
            System.out.print("Instrument (1 = pianist | 2 = saxophonist | 3 = drummer):- ");
        }

        String instrumentType = sc.next();
        musician = EnsembleFactory.createMusician(musicianId, musicianName, instrumentType, ensembleType);

        if (musician != null) {
            currentEnsemble.addMusician(musician);
            System.out.println("Musician is added.");
        }
    }

    @Override
    public void undo() {
        if (musician != null && currentEnsemble != null) {
            currentEnsemble.removeMusician(musician.getId());
            // 在撤销添加音乐家操作时，确保当前乐团正确设置
            setCurrentCmd.setCurrentEnsemble(currentEnsemble);
        }
    }

    @Override
    public void redo() {
        if (musician != null && currentEnsemble != null) {
            currentEnsemble.addMusician(musician);
            // 在重做添加音乐家操作时，确保当前乐团正确设置
            setCurrentCmd.setCurrentEnsemble(currentEnsemble);
        }
    }

    @Override
    public String toString() {
        if (musician != null) {
            return "Add musician, " + musician.getId() + ", " + musician.getName() + ", " + musician.getInstrument();
        }
        return "Add musician";
    }
}

class ModifyMusicianInstrumentCommand implements Command {
    private Ensemble currentEnsemble;
    private Scanner sc;
    private String musicianId;
    private String oldInstrument;
    private String newInstrument;
    private Caretaker caretaker;
    private SetCurrentEnsembleCommand setCurrentCmd;

    public ModifyMusicianInstrumentCommand(Ensemble currentEnsemble, Scanner sc, Caretaker caretaker, SetCurrentEnsembleCommand setCurrentCmd) {
        this.currentEnsemble = currentEnsemble;
        this.sc = sc;
        this.caretaker = caretaker;
        this.setCurrentCmd = setCurrentCmd;
    }

    @Override
    public void execute() {
        if (currentEnsemble == null) {
            System.out.println("No current ensemble selected!");
            return;
        }

        caretaker.saveState(currentEnsemble);

        System.out.print("Please input musician ID:- ");
        this.musicianId = sc.next();

        Musician musician = currentEnsemble.findMusician(musicianId);
        if (musician == null) {
            System.out.println("Musician not found!");
            return;
        }

        this.oldInstrument = musician.getInstrument();

        if (currentEnsemble instanceof OrchestraEnsemble) {
            System.out.print("Instrument (1 = violinist | 2 = cellist ):- ");
        } else {
            System.out.print("Instrument (1 = pianist | 2 = saxophonist | 3 = drummer):- ");
        }

        String instrumentType = sc.next();

        if (currentEnsemble instanceof OrchestraEnsemble) {
            switch (instrumentType) {
                case "1":
                    this.newInstrument = "violinist";
                    break;
                case "2":
                    this.newInstrument = "cellist";
                    break;
                default:
                    this.newInstrument = oldInstrument;
            }
        } else {
            switch (instrumentType) {
                case "1":
                    this.newInstrument = "pianist";
                    break;
                case "2":
                    this.newInstrument = "saxophonist";
                    break;
                case "3":
                    this.newInstrument = "drummer";
                    break;
                default:
                    this.newInstrument = oldInstrument;
            }
        }

        musician.setInstrument(newInstrument);
        System.out.println("Instrument is updated.");
    }

    @Override
    public void undo() {
        if (currentEnsemble != null) {
            Musician musician = currentEnsemble.findMusician(musicianId);
            if (musician != null) {
                musician.setInstrument(oldInstrument);
            }
            // 在撤销修改乐器操作时，确保当前乐团正确设置
            setCurrentCmd.setCurrentEnsemble(currentEnsemble);
        }
    }

    @Override
    public void redo() {
        if (currentEnsemble != null) {
            Musician musician = currentEnsemble.findMusician(musicianId);
            if (musician != null) {
                musician.setInstrument(newInstrument);
            }
            // 在重做修改乐器操作时，确保当前乐团正确设置
            setCurrentCmd.setCurrentEnsemble(currentEnsemble);
        }
    }

    @Override
    public String toString() {
        return "Modify musician's instrument, " + musicianId + ", " + newInstrument;
    }
}

class DeleteMusicianCommand implements Command {
    private Ensemble currentEnsemble;
    private Scanner sc;
    private Musician musician;
    private String musicianId;
    private SetCurrentEnsembleCommand setCurrentCmd;

    public DeleteMusicianCommand(Ensemble currentEnsemble, Scanner sc, SetCurrentEnsembleCommand setCurrentCmd) {
        this.currentEnsemble = currentEnsemble;
        this.sc = sc;
        this.setCurrentCmd = setCurrentCmd;
        this.musician = null;
    }

    @Override
    public void execute() {
        if (currentEnsemble == null) {
            System.out.println("No current ensemble selected!");
            return;
        }

        System.out.print("Please input musician ID:- ");
        this.musicianId = sc.next();

        musician = currentEnsemble.findMusician(musicianId);
        if (musician != null) {
            currentEnsemble.removeMusician(musicianId);
            System.out.println("Musician is deleted.");
        } else {
            System.out.println("Musician not found!");
        }
    }

    @Override
    public void undo() {
        if (musician != null && currentEnsemble != null) {
            currentEnsemble.addMusician(musician);
            // 在撤销删除音乐家操作时，确保当前乐团正确设置
            setCurrentCmd.setCurrentEnsemble(currentEnsemble);
        }
    }

    @Override
    public void redo() {
        if (musician != null && currentEnsemble != null) {
            currentEnsemble.removeMusician(musicianId);
            // 在重做删除音乐家操作时，确保当前乐团正确设置
            setCurrentCmd.setCurrentEnsemble(currentEnsemble);
        }
    }

    @Override
    public String toString() {
        return "Delete musician, " + musicianId;
    }
}

class ShowEnsembleCommand implements Command {

    private Ensemble currentEnsemble;

    public ShowEnsembleCommand(Ensemble currentEnsemble) {
        this.currentEnsemble = currentEnsemble;
    }

    @Override
    public void execute() {
        if (currentEnsemble == null) {
            System.out.println("No current ensemble selected!");
            return;
        }
        currentEnsemble.showEnsemble();
    }

    @Override
    public void undo() {
        // Not applicable
    }

    @Override
    public void redo() {
        // Not applicable
    }

    @Override
    public String toString() {
        return "Show ensemble";
    }
}

class ShowAllEnsemblesCommand implements Command {

    private Vector<Ensemble> ensembles;

    public ShowAllEnsemblesCommand(Vector<Ensemble> ensembles) {
        this.ensembles = ensembles;
    }

    @Override
    public void execute() {
        for (Ensemble ensemble : ensembles) {
            System.out.println(ensemble);
        }
    }

    @Override
    public void undo() {
        // Not applicable
    }

    @Override
    public void redo() {
        // Not applicable
    }

    @Override
    public String toString() {
        return "Show all ensembles";
    }
}

class ChangeEnsembleNameCommand implements Command {
    private Ensemble currentEnsemble;
    private Scanner sc;
    private String oldName;
    private String newName;
    private Caretaker caretaker;
    private SetCurrentEnsembleCommand setCurrentCmd;

    public ChangeEnsembleNameCommand(Ensemble currentEnsemble, Scanner sc, Caretaker caretaker, SetCurrentEnsembleCommand setCurrentCmd) {
        this.currentEnsemble = currentEnsemble;
        this.sc = sc;
        this.caretaker = caretaker;
        this.setCurrentCmd = setCurrentCmd;
    }

    @Override
    public void execute() {
        if (currentEnsemble == null) {
            System.out.println("No current ensemble selected!");
            return;
        }

        caretaker.saveState(currentEnsemble);

        this.oldName = currentEnsemble.getEnsembleName();
        System.out.print("Please input new name of the current ensemble:- ");
        sc.nextLine(); // 消耗换行符
        this.newName = sc.nextLine();

        currentEnsemble.setEnsembleName(newName);
        System.out.println("Ensemble's name is updated.");
    }

    @Override
    public void undo() {
        if (currentEnsemble != null) {
            currentEnsemble.setEnsembleName(oldName);
            // 在撤销修改名称操作时，确保当前乐团正确设置
            setCurrentCmd.setCurrentEnsemble(currentEnsemble);
        }
    }

    @Override
    public void redo() {
        if (currentEnsemble != null) {
            currentEnsemble.setEnsembleName(newName);
            // 在重做修改名称操作时，确保当前乐团正确设置
            setCurrentCmd.setCurrentEnsemble(currentEnsemble);
        }
    }

    @Override
    public String toString() {
        return "Change ensemble's name, " + currentEnsemble.getEnsembleId() + ", " + newName;
    }
}

class UndoCommand implements Command {
    private Stack<Command> commands;
    private Stack<Command> redos;
    private SetCurrentEnsembleCommand setCurrentCmd;

    public UndoCommand(Stack<Command> commands, Stack<Command> redos, SetCurrentEnsembleCommand setCurrentCmd) {
        this.commands = commands;
        this.redos = redos;
        this.setCurrentCmd = setCurrentCmd;
    }

    @Override
    public void execute() {
        if (!commands.isEmpty()) {
            Command com = commands.pop();
            System.out.println("Command (" + com + ") is undone.");
            
            // 在执行撤销前保存当前乐团状态
            Ensemble previousEnsemble = setCurrentCmd.getCurrentEnsemble();
            
            com.undo();
            redos.push(com);
            
            // 检查撤销操作是否改变了当前乐团
            Ensemble currentEnsembleAfterUndo = setCurrentCmd.getCurrentEnsemble();
            if (currentEnsembleAfterUndo != previousEnsemble) {
                if (currentEnsembleAfterUndo != null) {
                    System.out.println("The current ensemble is changed to " + 
                        currentEnsembleAfterUndo.getEnsembleId() + " " + 
                        currentEnsembleAfterUndo.getEnsembleName() + ".");
                } else if (previousEnsemble != null) {
                    System.out.println("The current ensemble is changed to null.");
                }
            }
        } else {
            System.out.println("Nothing to undo!");
        }
    }

    @Override
    public void undo() {
        // Not applicable
    }

    @Override
    public void redo() {
        // Not applicable
    }

    @Override
    public String toString() {
        return "Undo";
    }
}

class RedoCommand implements Command {
    private Stack<Command> commands;
    private Stack<Command> redos;
    private SetCurrentEnsembleCommand setCurrentCmd;

    public RedoCommand(Stack<Command> redos, Stack<Command> commands, SetCurrentEnsembleCommand setCurrentCmd) {
        this.redos = redos;
        this.commands = commands;
        this.setCurrentCmd = setCurrentCmd;
    }

    @Override
    public void execute() {
        if (!redos.isEmpty()) {
            Command com = redos.pop();
            System.out.println("Command (" + com + ") is redone.");
            
            // 在执行重做前保存当前乐团状态
            Ensemble previousEnsemble = setCurrentCmd.getCurrentEnsemble();
            
            com.redo();
            commands.push(com);
            
            // 检查重做操作是否改变了当前乐团
            Ensemble currentEnsembleAfterRedo = setCurrentCmd.getCurrentEnsemble();
            if (currentEnsembleAfterRedo != previousEnsemble) {
                if (currentEnsembleAfterRedo != null) {
                    System.out.println("The current ensemble is changed to " + 
                        currentEnsembleAfterRedo.getEnsembleId() + " " + 
                        currentEnsembleAfterRedo.getEnsembleName() + ".");
                } else if (previousEnsemble != null) {
                    System.out.println("The current ensemble is changed to null.");
                }
            }
        } else {
            System.out.println("Nothing to redo!");
        }
    }

    @Override
    public void undo() {
        // Not applicable
    }

    @Override
    public void redo() {
        // Not applicable
    }

    @Override
    public String toString() {
        return "Redo";
    }
}

class ShowUndoRedoListCommand implements Command {

    private Stack<Command> undoCommands;
    private Stack<Command> redoCommands;

    public ShowUndoRedoListCommand(Stack<Command> undoCommands, Stack<Command> redoCommands) {
        this.undoCommands = undoCommands;
        this.redoCommands = redoCommands;
    }

    @Override
    public void execute() {
        System.out.println("Undo List");
        if (!undoCommands.isEmpty()) {
            for (Command com : undoCommands) {
                System.out.println(com);
            }
        } else {
            System.out.println("-- End of undo list --");
        }

        System.out.println("\nRedo List");
        if (!redoCommands.isEmpty()) {
            for (Command com : redoCommands) {
                System.out.println(com);
            }
        } else {
            System.out.println("-- End of redo list --");
        }
    }

    @Override
    public void undo() {
        // Not applicable
    }

    @Override
    public void redo() {
        // Not applicable
    }

    @Override
    public String toString() {
        return "Show undo/redo list";
    }
}

class ExitCommand implements Command {

    public ExitCommand() {
        // 无参构造函数
    }

    @Override
    public void execute() {
        System.out.println("Exiting system...");
        System.exit(0);
    }

    @Override
    public void undo() {
        // Not applicable
    }

    @Override
    public void redo() {
        // Not applicable
    }

    @Override
    public String toString() {
        return "Exit";
    }
}
