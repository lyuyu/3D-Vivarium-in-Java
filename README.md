# 3D-Vivarium-in-Java

 a simulated 3-D world with polyhedral creatures moving around.
 
## Features

1) Used of OpenGL display lists and OpenGL transforms for hierarchical modeling

2) Collision detection and Clipping. 

3) Potential Function

## Description

There are two different creatures using polyhedral (solid) parts.The creatures you design have at least one moving linkage of the basic parts: legs, arms, wings, etc. These linkage(s) move back and forth in a periodic motion, as the creatures move about the vivarium.

In the vivarium program, creatures and their parts are to be stored and manipulated as OpenGL display list objects. Use a hierarchy structure to organize them.

Use OpenGL transforms to control the motion of each creature and its parts.

Creatures face in the direction they are moving. For instance, a bee should be facing the direction in which it flys.

Creatures in the vivarium react to where other creatures are and move accordingly. The creatures have a prey/predator relationship, a courtship/mating relationship. 

The creatures live within the 3D "tank" of fixed width, height, and depth. At no point creatures go outside the 3D tank. If a creature comes to the edge of the tank, it should turn and change direction to stay within the tank.

It is possible for a user to add chunks of food to the vivarium. It disappear once it has been eaten. Food is eaten by the first creature that touches it.
