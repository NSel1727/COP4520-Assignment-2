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
        CrystalVase.main(null);
    }
}

class BirthdayParty implements Runnable{
    private static ReentrantLock lock = new ReentrantLock();
    
    private static Map<Thread, Integer> threadIdentity = new HashMap<>();
    private static Map<Integer, Integer> cupcakeFreq = new HashMap<>();

    private static int numGuests;

    private static boolean[] hasEaten;

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

        Scanner in = new Scanner(System.in);

        System.out.print("How many guests are at the party: ");

        numGuests = in.nextInt();

        System.out.println();

        hasEaten = new boolean[numGuests];

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
            System.out.println("Guest " + (i + 1) + " says: I saw that " + ((cupcakeFreq.get(i) == null) ? 0 : cupcakeFreq.get(i)) + " guest(s) have finished");
    }
}

class CrystalVase implements Runnable{
    private static ReentrantLock lock = new ReentrantLock();

    private static int numGuests;

    private static Map<Thread, Integer> threadIdentity = new HashMap<>();

    // Keeps track of how many unique guests have entered
    private static Set<Integer> guestSet = new HashSet<>();

    // Implements the second strategy of setting the showroom
    // to "AVAILABLE" and "BUSY"
    @Override
    public void run(){
        // tryLock() will let the guest in if the 
        // sign is set to "AVAILABLE", and then will
        // make the guest set it to "BUSY"
        while(guestSet.size() < numGuests){
            if(lock.tryLock()){
                System.out.println("Showroom sign says: BUSY");

                int threadNum = threadIdentity.get(Thread.currentThread());

                guestSet.add(threadNum);

                System.out.println("Guest " + (threadNum + 1) + " is currently in the showroom");

                System.out.println("Showroom sign says: AVAILABLE");

                // Unlock the lock, essentially expressing that the room is
                // open now for other guests to enter
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{
        CrystalVase cv = new CrystalVase();

        Scanner in = new Scanner(System.in);

        System.out.print("How many guests want to enter the showroom: ");

        numGuests = in.nextInt();

        System.out.println();

        List<Thread> guests = new ArrayList<>();

        for(int i = 0; i < numGuests; i++){
            Thread t = new Thread(cv);
            threadIdentity.put(t, i);
            guests.add(t);
        }

        for(Thread t : guests)
            t.start();

        for(Thread t : guests)
            t.join();

        System.out.println("\nAll " + numGuests + " guest(s) have visited the showroom");
    }
}