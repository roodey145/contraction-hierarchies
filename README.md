# Run the experiments
I tried to make the pipelines as simple as possible. All you need is to open the Main.java class.
Inside the main static method I have the code for all the experiments commented out.
Each part of the code has a title and an explination explaining what it does

Calling the method:
Tests.repeatedTestOnContractedGraph(1000, 50);
will result in the contracted-1000.csv being created inside the csv folder.
To process the data in that file you just need to open the dataProcessor.py file and run it,
that will result in two images being created. The first one is "Relaxed Edges.png" and
"Running Time.png". 