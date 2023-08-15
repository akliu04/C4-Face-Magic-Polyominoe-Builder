# C4-Face-Magic-Polyominoe-Builder
A GUI tool to build C4-polyominoes.

Functionality:
1. Draw Polyominoes: Left-click anywhere in the sandbox to draw a polyominoe. Hold left-click to drag and draw faster. Polyominoes drawn adjacent to each other will share vertices and/or edges.
2. Erase Polyominoes: Left-click again on a Polyominoe to erase it. Right-click (and holding right-click) will also erase Polyominoes. Note: only Polyominoes with non-labeled vertices will be erased.
3. Labeling Vertices: Click on a Vertex to label it. The list in the UI bar indicates which the integer that will be inputted. For keyboard input, hover over a Vertex and type in a valid integer, then move the cursor off the Vertex to confirm input. To remove a label, left-click on the Vertex. The sum of a 'Face' will
   automatically be calculated and displayed.
5. Clear All: Erase all polyominoes.
6. Renumber: Erase all Vertex labelings.
7. Freeze: Freeze all polyominoes. Frozen polyominoes cannot be interacted with further, unless erased with the 'Clear' button.
8. Find C4-Face-Magic labeling: Experimental feature to attempt to find a c4-face-magic labeling for the given (non-frozen) polyominoe. This feature attempts to generate a labeling via generating ALL possible permutations of labelings and displays the first c4-face-magic labeling found. Note that if a polyominoe is
   partially labeled, this process will take those labels into account and attempt to find a c4-face-magic labeling with those labels in the specified position. Runs in n! time and thus should be reserved for either small polyominoes (<13 Vertices) or for partially labeled polyominoes with a small number of remaining unlabeled Vertices.
   
