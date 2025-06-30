package solution;

public class ScotlandYardApplication
{
    // All we do is initialise the controller with a view.
    // The model is initialised after the number of players has been entered.
    // This is then passed to the controller. It is within model initialiser.
    public static void main(String args[]) {
        View view = new View();
        Controller controller = new Controller(view);
    }
}