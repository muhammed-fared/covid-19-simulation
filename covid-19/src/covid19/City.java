package covid19;

import java.awt.*;

public final class City { 

    private int width, height;  // sehirinin boyutu olarak 
    private Person[] people; // kisiler
    private ValuesListener listener;  // listenere (input )

    // constructor  
    public City(int w, int h, int peopleCount, int peopleSpeed, int infectionProbabilty, int recoveryTime, ValuesListener listener) {
        this.listener = listener;
        setSize(w, h); 
        
        people = new Person[peopleCount];

        for (int i = 0; i < people.length; i++)
            people[i] = new Person(w, h, peopleSpeed, infectionProbabilty, recoveryTime);
         // set first person sick
        people[0].setSick();
    }

    public void setSize(int w, int h) {
        width = w;
        height = h;
    }


    public void draw(Graphics g) {
        for (int i = 0; i < people.length; i++)
            people[i].draw(g);
    }


    public void update() {
        for (int i = 0; i < people.length; i++) {
           // kisilerin goncellemesi
            people[i].update(width, height);

            for (int j = 0; j < people.length; j++)
                if (i != j) {
                    Person p1 = people[i];
                    Person p2 = people[j];

                     // carbisma hale varmi yada yokmu 
                    boolean collided = p1.collided(p2);

                    if (collided) {
                       // varsa ters yonu gitsin
                        float xv1 = p1.getxVel();
                        float yv1 = p1.getyVel();

                        float xv2 = p2.getxVel();
                        float yv2 = p2.getyVel();

                        p1.setxVel(xv2);
                        p1.setyVel(yv2);

                        p2.setxVel(xv1);
                        p2.setyVel(yv1);

                    }

                }

        }
        int infectedCount = 0;// kac kisi hasta
        int healthyCount = 0;//kac kisi hasta olmadi
        int recoveredCount = 0;//kac kisi iyilesen 
        for (Person person : people) {
            if (person.getState() == Person.State.HEALTHY)
                healthyCount++;
            if (person.getState() == Person.State.SICK)
                infectedCount++;
            if (person.getState() == Person.State.RECOVERED)
                recoveredCount++;
        }
        listener.onValuesChanged(healthyCount, recoveredCount, infectedCount);  
    }

}
