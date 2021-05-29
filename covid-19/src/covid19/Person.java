package covid19;

import java.awt.*;
import java.util.Random;

public class Person {

    private static final int MAX_SIZE = 20;
    private static final int MIN_SIZE = 10;
    private static final float MAX_SPEED = 4.5f;
    private int size;
    private int x, y;
    // hiz olarak nekadar olacak
    private float xVel = 0, yVel = 0;
   //hastali olasiligi
    private float infectionProb = 0.9f;
    // iletisim suresi   
    private float recoveryTime = 10000.0f;
    private long sickTime = -1l;

    public enum State {
        HEALTHY, RECOVERED, SICK
    }

    private State state = State.HEALTHY;


    public Person(int w, int h, int peopleSpeed, int infectionProbabilty, int recoveryTime) {
        Random rand = new Random();

        size = rand.nextInt((MAX_SIZE - MIN_SIZE) + 1) + MIN_SIZE;

        x = rand.nextInt(w - size);
        y = rand.nextInt(h - size);

         //  hiz random olarak 
        while ((int) xVel == 0)
            xVel = rand.nextFloat() * MAX_SPEED * 2 - MAX_SPEED;

        while ((int) yVel == 0)
            yVel = rand.nextFloat() * MAX_SPEED * 2 - MAX_SPEED;
        xVel = xVel * peopleSpeed / 100f;
        yVel = yVel * peopleSpeed / 100f;
        infectionProb = infectionProbabilty / 100f;
        this.recoveryTime = recoveryTime * 1000;
    }

    // get ve sit mothodes 
    public State getState() {
        return state;
    }


    public void setSick() {
        state = State.SICK;
        sickTime = System.currentTimeMillis();
    }


    public float getxVel() {
        return xVel;
    }


    public void setxVel(float xVel) {
        this.xVel = xVel;
    }


    public float getyVel() {
        return yVel;
    }

    public void setyVel(float yVel) {
        this.yVel = yVel;
    }


    public float getNextX() {
        return x + xVel;
    }

    public float getNextY() {
        return y + yVel;
    }


    public int getSize() {
        return size;
    }


    public void draw(Graphics g) {
        Color color;  // renk

        switch (state) { // durum olarak 

            case HEALTHY: //hastali deglise
                color = Color.GREEN;     // yesil           
                break;
            case RECOVERED:  // iyilesimis ise 
                color = Color.BLUE;   // mavi
                break;
            case SICK:  // hasatli olursa 
                color = Color.RED;  // kirimizi
                break;
            default: 
                color = Color.WHITE;
        }

        g.setColor(color);  // renk koysun
        g.fillOval(x, y, size, size);
    }

    public void update(int w, int h) {

        x += xVel;
        y += yVel;

        Rectangle nextMe = new Rectangle((int) Math.ceil(getNextX()), (int) Math.ceil(getNextY()), size, size);


       // bizim sehirimizden cikitsa donsun
        if (nextMe.intersectsLine(0, 0, 0, h))
            xVel = -xVel;

        if (nextMe.intersectsLine(w, 0, w, h))
            xVel = -xVel;

        if (nextMe.intersectsLine(0, 0, w, 0))
            yVel = -yVel;
        if (nextMe.intersectsLine(0, h, w, h))
            yVel = -yVel;

        //iyilesmis suresi gecerirse iyilessin 
        if (System.currentTimeMillis() - sickTime >= recoveryTime && state == State.SICK && sickTime > 0)
            state = State.RECOVERED;

    }

    public boolean collided(Person p) {  // carpismak motodu  
       // mesafe
        double dist = Math.sqrt(Math.pow(getNextX() - p.getNextX(), 2) + Math.pow(getNextY() - p.getNextY(), 2)); //euclid formu
        // consider collided if too close   
        boolean collided = (dist < size / 2.0 + p.getSize() / 2.0);
        if (collided && p.getState() == State.SICK && state == State.HEALTHY)
           // olasligiye gore hastalansin 
            if (Math.random() < infectionProb)
                setSick(); //hastalansin

        return collided;
    }

}
