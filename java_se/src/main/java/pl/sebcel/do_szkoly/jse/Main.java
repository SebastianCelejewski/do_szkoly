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
        engine.addEvent("10:00", "Ubieranie się");
        engine.addEvent("10:30", "Wychodzenie z domu");
        engine.addEvent("10:43", "Autobus 268");
        engine.addEvent("11:00", "W szkole");

        engine.addEventListener(timeInformation -> mainFrame.displayTimeInformation(timeInformation));

        engine.start();
   }
}