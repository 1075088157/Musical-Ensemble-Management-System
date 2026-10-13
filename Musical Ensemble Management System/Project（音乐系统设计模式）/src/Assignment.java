
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class Assignment {
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Vector<Ensemble> ensembles = new Vector<>();
        Stack<Command> undoCommands = new Stack<>();
        Stack<Command> redoCommands = new Stack<>();
        Caretaker caretaker = new Caretaker();
        
        SetCurrentEnsembleCommand setCurrentCmd = new SetCurrentEnsembleCommand(ensembles, sc);
        Ensemble currentEnsemble = null;
        
        String command;

        while (true) {
            // 在显示菜单前更新当前乐团
            if (setCurrentCmd.getCurrentEnsemble() != null) {
                currentEnsemble = setCurrentCmd.getCurrentEnsemble();
            }
            
            displayMenu(currentEnsemble);
            command = sc.next();
            
            Command com = CommandFactory.createCommand(command, ensembles, currentEnsemble, sc, 
                                                     undoCommands, redoCommands, caretaker, setCurrentCmd);
            if (com != null) {
                com.execute();
                
                // 添加命令到undo栈
                if (com instanceof CreateEnsembleCommand || com instanceof AddMusicianCommand ||
                    com instanceof ModifyMusicianInstrumentCommand || com instanceof DeleteMusicianCommand ||
                    com instanceof ChangeEnsembleNameCommand) {
                    undoCommands.push(com);
                    redoCommands.clear();
                }
                
                // 执行命令后再次更新当前乐团
                if (setCurrentCmd.getCurrentEnsemble() != null) {
                    currentEnsemble = setCurrentCmd.getCurrentEnsemble();
                }
            }
        }
    }
    
    public static void displayMenu(Ensemble currentEnsemble) {
        System.out.println("\nMusic Ensembles Management System (MEMS)");
        System.out.println("c = create ensemble, s = set current ensemble, a = add musician, m = modify musician's instrument,");
        System.out.println("d = delete musician, se = show ensemble, sa = display all ensembles, cn = change ensemble's name,");
        System.out.println("u = undo, r = redo, l = list undo/redo, x = exit system");
        
        if (currentEnsemble != null) {
            System.out.println("The current ensemble is " + currentEnsemble.getEnsembleId() + " " + currentEnsemble.getEnsembleName() + ".");
        }
        System.out.print("Please enter command [ c | s | a | m | d | se | sa | cn | u | r | l | x ] :- ");
    }
}