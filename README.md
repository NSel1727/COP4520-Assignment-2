# COP4520 Assignment 2 Explanation & Analysis


## Minotaur's Birthday Party

In terms of implementation, this problem was solved using a number of threads the user types in, each representing a guest. There was one reentrant lock used to allow only one thread at a time to access global data, this lock being unlocked once the current guest is done.

### Guest Strategy
Run while the number of times a guest has seen a cupcake is less than the number of guests:
1.  If there is no cupcake present:
    1. Increment the number of times a guest has not seen a cupcake
    2. Increment the guest's counter for not seeing a cupcake
    3. Put a cupcake on the plate, and if the guest had already eaten one, leave it there, else have the guest eat it
2.  If there is a cupcake present:
    1.  If the guest has already eaten a cupcake, leave it there else eat it.
    
This strategy requires the guests to maintain a counter of the amount of times they don't see a cupcake. There will only not be a cupcake if the last guest ate it, and a guest can only eat a cupcake once. Once the game ends, every guest will say their counter, which will always be equal to the number of guests, thus satisfying the wish of the Minotaur.

### Experimental Evaluation
Number of Guests  | Runtime (ms)
------------- | -------------
10 | 51
50  | 5435
100 | 25424

## Minotaur's Crystal Vase

Just like problem 1, my program asks for the number of threads used where each thread represents a guest. A reentrant lock is again utilized to allow only one guest/thread at a time to access the showroom.

### Chosen Strategy
I chose the second strategy of switching between a sign saying "AVAILABLE" when the room is not occupied, otherwise "BUSY" when it is in use. The advantage of this strategy is that it ensures that every guest will know when they can enter, and that way no two guests can be in the room at the same time. However, a disadvantage is that there is no proper ordering among who can enter, and the guest that had just been in the room may have an advantage at the moment when they immeditately leave in reentering the room.

It terms of the algorithm used, it is a simpler version of the one in problem 1. A guest will enter the room, lock it, state that it is busy, state that they are in it, then say it is available and unlock it. This is ran until every guest has visited the room at least once.

### Experimental Evaluation

<b>Note:</b> The runs of the program used to acquire the evaluation comment out the print statements, as printing to the console adds a heavy bulk of overhead to where the program could not be completed with a high number in guests in a reasonable amount of time.

Number of Guests  | Runtime (ms)
------------- | -------------
10 | 64
50  | 6494
100 | 38822
