// Noah Seligson
// 2/9/2023
// COP 4520

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Problems{
    public static void main(String[] args) throws InterruptedException{
        System.out.println("Starting Problem 1:\n");
        BirthdayParty.main(null);
        System.out.println("\nStarting Problem 2:\n");

    }
}

class BirthdayParty implements Runnable{
    private static ReentrantLock lock = new ReentrantLock();
    
    private static Map<Thread, Integer> threadIdentity = new HashMap<>();
    private static Map<Integer, Integer> cupcakeFreq = new HashMap<>();

    private final static int numGuests = 8;

    private static boolean[] hasEaten = new boolean[numGuests];

    // Represents the number of instances a guest goes and
    // sees no cupcake
    private static int sawNoCupcake = 0;
    private static boolean noCupcake = false;

    @Override
    public void run(){
        
        while(sawNoCupcake < numGuests){
            if(lock.tryLock()){                
                int threadNum = threadIdentity.get(Thread.currentThread());
                
                if(noCupcake){ // There is no cupcake on the plate
                    sawNoCupcake++;
    
                    // Increments the thread's cupcake frequency by 1
                    cupcakeFreq.put(threadNum, cupcakeFreq.getOrDefault(threadNum, 0) + 1);
    
                    if(!hasEaten[threadNum])
                       hasEaten[threadNum] = true; // Servant puts cupcake there and guest eats it
                    else
                        noCupcake = false;
                }else{ // There is a cupcake on the plate
                    if(!hasEaten[threadNum]){
                        noCupcake = true; 
                        hasEaten[threadNum] = true;
                    }
                }
    
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{
        BirthdayParty bp = new BirthdayParty();

        List<Thread> guests = new ArrayList<>();

        for(int i = 0; i < numGuests; i++){
            Thread t = new Thread(bp);
            threadIdentity.put(t, i);
            guests.add(t);
        }

        for(Thread t : guests)
            t.start();

        for(Thread t : guests)
            t.join();

        System.out.println(numGuests + " guests total\n");

        for(int i = 0; i < numGuests; i++)
            System.out.println("Guest " + (i + 1) + " says: I saw that " + ((cupcakeFreq.get(i) == null) ? 0 : cupcakeFreq.get(i)) + " guests have finished");
    }
}

class CrystalVase implements Runnable{
    private static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run(){
 
    }

    public static void main(String[] args) throws InterruptedException{
        

    }
}