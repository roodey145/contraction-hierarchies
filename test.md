# First attempt
Avg Uni Relaxed: 583349, Avg Uni Time: 177911
Avg Bi Relaxed: 1151371, Avg Bi Time: 448622

# Second Attempt
UniTime: 175522, UniRelaxed: 583349
BiTime: 168778, BiRelaxed: 463569

# Third Attempt
Avg Uni Relaxed: 583349, Avg Uni Time: 223672
Avg Bi Relaxed: 463569, Avg Bi Time: 200019

# Third Plus Contracted Hierarchy
## Normal Dijkstra
Avg Relaxed: 583349, Avg Time: 223672

## BiDirectional
Avg Relaxed: 463569, Avg Time: 200019

## BiDirectional Using Contracted Hierarachy
Avg Relaxed: 14929, Avg Time: 3129


# Fourth All Testing
## Used index
long from = 115724;
long to = 4214353078l;

## Results using the denmark.graph
###  Dijkstra
Expected: 10918, Visited: 873786, Time: 374545
### BiDirectional
Actual: 10918, Visited: 613264, Time: 346117

## Results using the contracted15.graph
