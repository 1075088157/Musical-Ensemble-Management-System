
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class CommandFactory {

    public static Command createCommand(String command, Vector<Ensemble> ensembles,
            Ensemble currentEnsemble, Scanner sc,
            Stack<Command> undoCommands, Stack<Command> redoCommands,
            Caretaker caretaker, SetCurrentEnsembleCommand setCurrentCmd) {
        Command com;
        switch (command) {
            case "c":
                com = new CreateEnsembleCommand(ensembles, sc, caretaker, setCurrentCmd);
                break;
            case "s":
                com = setCurrentCmd;
                break;
            case "a":
                String ensembleType = "";
                if (currentEnsemble instanceof OrchestraEnsemble) {
                    ensembleType = "o";
                } else if (currentEnsemble instanceof JazzBandEnsemble) {
                    ensembleType = "j";
                }
                com = new AddMusicianCommand(currentEnsemble, sc, ensembleType, setCurrentCmd);
                break;
            case "m":
                com = new ModifyMusicianInstrumentCommand(currentEnsemble, sc, caretaker, setCurrentCmd);
                break;
            case "d":
                com = new DeleteMusicianCommand(currentEnsemble, sc, setCurrentCmd);
                break;
            case "se":
                com = new ShowEnsembleCommand(currentEnsemble);
                break;
            case "sa":
                com = new ShowAllEnsemblesCommand(ensembles);
                break;
            case "cn":
                com = new ChangeEnsembleNameCommand(currentEnsemble, sc, caretaker, setCurrentCmd);
                break;
            case "u":
                com = new UndoCommand(undoCommands, redoCommands, setCurrentCmd);
                break;
            case "r":
                com = new RedoCommand(redoCommands, undoCommands, setCurrentCmd);
                break;
            case "l":
                com = new ShowUndoRedoListCommand(undoCommands, redoCommands);
                break;
            case "x":
                com = new ExitCommand();
                break;
            default:
                System.out.println("Invalid command!");
                com = null;
        }
        return com;
    }
}