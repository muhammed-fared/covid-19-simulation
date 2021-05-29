package covid19;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

public class SimulationWindow extends JFrame implements Runnable { //Threading  interface implemetayson  
    Canvas canvas = new Canvas();
    public boolean running = false;
    private Thread gameThread;// threading burasi oluyor
    private City city;
    private int peopleCount = 10;
    private int peopleSpeed = 70;
    private int infectionProbabilty = 0;
    private int recoveryTime = 10;
    private ValuesListener listener = (int healthy, int recovered, int infected) -> {
    };

    public SimulationWindow() {
        this.add(canvas);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                canvas.setSize(getWidth() - 10, getHeight() - 38);
                canvas.setLocation(0, 0);
                init();
                start();
            }
        });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void init() {
        canvas.setFocusable(true);
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_R)
                    newCity(peopleCount, peopleSpeed, infectionProbabilty, recoveryTime, listener);
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                canvas.setSize(getWidth() - 10, getHeight() - 38);
                newCity(peopleCount, peopleSpeed, infectionProbabilty, recoveryTime, listener);
            }
        });
        newCity(peopleCount, peopleSpeed, infectionProbabilty, recoveryTime, listener);
    }


    public void run() {
        final double MAX_FRAMES_PER_SECOND = 60.0;
        final double MAX_UPDATES_PER_SECOND = 60.0;
        long startTime = System.nanoTime();
        final double uOptimalTime = 1000000000 / MAX_UPDATES_PER_SECOND;
        final double fOptimalTime = 1000000000 / MAX_FRAMES_PER_SECOND;
        double uDeltaTime = 0, fDeltaTime = 0;
        int frames = 0, updates = 0;
        long timer = System.currentTimeMillis();
        while (running) {
            long currentTime = System.nanoTime();
            uDeltaTime += (currentTime - startTime) / uOptimalTime;
            fDeltaTime += (currentTime - startTime) / fOptimalTime;
            startTime = currentTime;


            while (uDeltaTime >= 1) {
                city.update();
                updates++;
                uDeltaTime--;
            }

            if (fDeltaTime >= 1) {
                render();
                frames++;
                fDeltaTime--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                frames = 0;
                updates = 0;
                timer += 1000;
            }
        }

        stop();
    }

    public void render() {
        BufferStrategy buffer = canvas.getBufferStrategy();
        if (buffer == null) {
            canvas.createBufferStrategy(3);
            buffer = canvas.getBufferStrategy();
        }
        Graphics g = buffer.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        city.draw(g);
        g.dispose();
        buffer.show();
    }

    public void stop() {
        try {
            gameThread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void newCity(int peopleCount, int peopleSpeed, int infectionProbabilty, int recoveryTime, ValuesListener listener) {
        this.peopleCount = peopleCount;
        this.peopleSpeed = peopleSpeed;
        this.infectionProbabilty = infectionProbabilty;
        this.recoveryTime = recoveryTime;
        city = new City(canvas.getWidth(), canvas.getHeight(), peopleCount, peopleSpeed, infectionProbabilty, recoveryTime, listener);
    }

    public synchronized void start() {
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }

    public void setListener(ValuesListener f) {
        this.listener = f;
        this.newCity(peopleCount, peopleSpeed, infectionProbabilty, recoveryTime, listener);
    }
}
