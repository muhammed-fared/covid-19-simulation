package covid19;

import javax.swing.*;

public class Main {


    public static void main(String[] args) {
    
         
        SimulationWindow simulationWindow = new SimulationWindow();
        simulationWindow.setSize(700, 500);
        simulationWindow.setLocationByPlatform(true);
        simulationWindow.setVisible(true);

        SettingsForm f = new SettingsForm(simulationWindow);
        simulationWindow.setListener(f);
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setContentPane(f.contentspanel);
        frame.setVisible(true);
    }
}
