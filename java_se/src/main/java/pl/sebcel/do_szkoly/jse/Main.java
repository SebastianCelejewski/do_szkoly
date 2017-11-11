package pl.sebcel.do_szkoly.jse;

import pl.sebcel.do_szkoly.engine.*;

public class Main {

    private MainFrame mainFrame = new MainFrame();
    private Engine engine = new Engine();

    public static void main(String[] args) {
        System.out.println("Starting Do Szkoły application");
        new Main().run();
    }

    public void run() {
        engine.addStep("12:00", "Ubieranie się");
        engine.addStep("12:30", "Wychodzenie z domu");
        engine.addStep("12:43", "Autobus 268");
        engine.addStep("13:00", "W szkole");

        engine.addEventListener(timeInformation -> mainFrame.displayTimeInformation(timeInformation));

        engine.start(10);
   }
}