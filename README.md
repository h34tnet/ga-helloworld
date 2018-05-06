# Hello, World!

The easiest way to print "Hello, World!"? Genetic programming, of course!

1. Write a brainfuck ([wikipedia](https://en.wikipedia.org/wiki/Brainfuck) 
  or [esolang](https://esolangs.org/wiki/Brainfuck)) interpreter (see VM.java).
  
2. Use a hillclimber or a genetic algorithm to create a brainfuck program that prints "Hello, World!".

3. Use the interpreter from (1) to execute the program from (2). 

Done.

Supplied are, respectively, HillClimberMain and GeneticMain. Note that both supply suboptimal 
solutions at best and might fail at coming up with ANY solution at all (especially the GeneticMain).

Generally, the HillClimber quickly finds a bad solution (local minimum) while the Genetic Algorithm 
finds a better one but takes a lot longer.  