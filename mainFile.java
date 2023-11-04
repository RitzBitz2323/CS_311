import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class mainFile {
    public static void main(String[] args) throws Exception {
    FrogGame fg = new FrogGame();
    
    fg.readInput("testing.txt");
    ArrayList<GamePlan> gps = fg.getGamePlans(); // get all game plans
    GamePlan gp; // gameplans for a particular goal location
    ArrayList<int[]> plan; // actual plan
    // for printing
    if (gps == null) System.out.println("No plans"); // For no plans
        else {
            for (int i=0; i<gps.size(); i++) {
                gp = gps.get(i); // get plans for a goal location
                System.out.println("**For Goal Location** " + gp.getgoalLoc());
                System.out.println("Number of plans: " + gp.getnumOfPlans());
                System.out.println("One of the plans: ");
                plan = gp.getPlan(); // these are all the plans
            for (int k=0; k<plan.size(); k++) { // get each location in the plan
                for (int l=0; l<plan.get(k).length; l++) {
                    System.out.print(plan.get(k)[l] + " ");
                }
            System.out.println();
            }
        }
        }
    }
}