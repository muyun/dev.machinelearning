#! /usr/bin/ipython
import os; os.chdir("/home/zhaowenlong/workspace/proj/dev.machinelearning/")
#DecitionTree.py : wenlong
#Description: show code about the decision tree; CART algorithms is used
#Reference:   ch7 in "Programming Collective Intelligence"

my_data=[['slashdot','USA','yes',18,'None'],
        ['google','France','yes',23,'Premium'],
        ['digg','USA','yes',24,'Basic'],
        ['kiwitobes','France','yes',23,'Basic'],
        ['google','UK','no',21,'Premium'],
        ['(direct)','New Zealand','no',12,'None'],
        ['(direct)','UK','no',21,'Basic'],
        ['google','USA','no',24,'Premium'],
        ['slashdot','France','yes',19,'None'],
        ['digg','USA','no',18,'None'],
        ['google','UK','no',18,'None'],
        ['kiwitobes','UK','no',19,'None'],
        ['digg','New Zealand','yes',12,'Basic'],
        ['slashdot','UK','no',21,'None'],
        ['google','UK','yes',18,'Basic'],
        ['kiwitobes','France','yes',19,'Basic']]
        
class DecisionNode:
    """ each node in the tree,
        create a tree return the root node, 
        which can be traversed by following its True or False branches until a branch with results is reached
    """
    def __init__(self, column_index = -1, value = None, true_node=None, false_node=None, results= None):
        self.column_index = column_index
        self.value = value
        self.true_node = true_node
        self.false_node = false_node
        self.results = results


def UniqueCounts(rows):
    """counts of the results
    """
    results = {}
    for row in rows:
        r = row[len(row) - 1]  #the last column of each row is the result  
        if r not in results: results[r]=0
        results[r] += 1
        
    return results   
        
        
def Gini(rows):
    """Divide set into k classes,
        Gini(p)= p1*(1-p1) + p2*(1-p2) + ... + Pk*(1-pk)
               = 1 - (p1*p1 + p2*p2 + ... + pk*pk)
        Bi-division: Gini(p) = 2*p*(1-p)       
    """
    total = len(rows)
    # count for some class
    counts = UniqueCounts(rows)

    pr = 0
    for k in counts:
        p = float(counts[k])/total
        pr = p*p + pr
    
    return 1 - pr


def DivideSet(rows, column, value):
    """Divides a set on a specific column,
    tell us if a row 1st set(true) or 2nd set
    """
    split_function = None
    if isinstance(value, int) or isinstance(value, float):
        split_function = lambda row:row[column]>=value
    else:
        split_function = lambda row:row[column]==value

    set1 = [row for row in rows if split_function(row)]
    set2 = [row for row in rows if not split_function(row)]

    return (set1, set2)

    
def BuildTree(rows, scoref=Gini):
    if len(rows) == 0: 
        return DecisionNode()
    
    current_score = scoref(rows)

    best_gain = 0.0
    best_criteria = None
    best_sets = None

    column_count = len(rows[0])-1 #remove the last column
    for col in range(0, column_count):
        #each character Ai
        column_values = {}
        for row in rows:
            #each unique type in this character
            column_values[row[col]] = 1

        #calculate gini for each Ai
        #Definition: Gini(D,Ai)= (D1/D)*Gini(D1) + (D2/D)*Gini(D2)
        for value in column_values.keys():    
            #get the sets D1 and D2  when Ai = value
            (set1, set2)=DivideSet(rows, col, value)
            
            #p=(D1/D)
            p = float(len(set1))/len(rows)
            #Information gain gain(D,Ai) = H(D) - H(D|Ai)
            #which descripes the decreasing scale of the unceratin
            gain = current_score - (p*scoref(set1) + (1-p)*scoref(set2))
            if gain > best_gain and len(set1) > 0 and len(set2) > 0:
                best_gain = gain
                best_criteria = (col, value)
                best_sets = (set1, set2)

    #create subbranch
    if best_gain > 0: #still have uncertain,continue
        TrueBranch = BuildTree(best_sets[0])
        FalseBranch = BuildTree(best_sets[1])
        #import pdb; pdb.set_trace()
        return DecisionNode(column_index = best_criteria[0], value = best_criteria[1], 
                            true_node = TrueBranch, false_node = FalseBranch ) 
    else:
        return DecisionNode(results = UniqueCounts(rows))

        
def PrintTree(tree, indent=''):
    #a leaf?
    if tree.results != None:
        print str(tree.results)
    else:
        print str(tree.column_index) + ':' + str(tree.value)

        #branch
        print indent+'T->',
        PrintTree(tree.true_node, indent+' ')
        print indent+'F->',
        PrintTree(tree.false_node, indent+' ')


def Prune(tree, mingain):
    """see if merging them would increase the entropy by less than a specified threshold
    """
    #not leaves, prune them
    if tree.true_node.results == None:
        Prune(tree.true_node, mingain)
    if tree.false_node.results == None:
        Prune(tree.false_node, mingain)

    #if both subbranches are leaves,
    if tree.true_node.results != None and tree.false_node.results != None:
        true_node = []
        false_node = []
        for n, c in tree.true_node.results.items():
            true_node += [[n]]*c        
        for n, c in tree.false_node.results.items(): 
            false_node += [[n]]*c

                #import pdb; pdb.set_trace()
        #the reduction in Gini
        # parameter delta a = ( Ca(T) - C(T) ) / |T|
        delta = Gini(true_node + false_node) - (Gini(true_node) + Gini(false_node))
        
        if delta < mingain:
            #merge the branches
            tree.results = UniqueCounts(true_node+false_node)
            tree.true_node.results, tree.false_node.results = None, None
       
            
def main():
    #my_data = [line.split('\t') for line in file('decision_tree_example.txt')]
    my_data=[['slashdot','USA','yes',18,'None'],
        ['google','France','yes',23,'Premium'],
        ['digg','USA','yes',24,'Basic'],
        ['kiwitobes','France','yes',23,'Basic'],
        ['google','UK','no',21,'Premium'],
        ['(direct)','New Zealand','no',12,'None'],
        ['(direct)','UK','no',21,'Basic'],
        ['google','USA','no',24,'Premium'],
        ['slashdot','France','yes',19,'None'],
        ['digg','USA','no',18,'None'],
        ['google','UK','no',18,'None'],
        ['kiwitobes','UK','no',19,'None'],
        ['digg','New Zealand','yes',12,'Basic'],
        ['slashdot','UK','no',21,'None'],
        ['google','UK','yes',18,'Basic'],
        ['kiwitobes','France','yes',19,'Basic']]

    tree=BuildTree(my_data)
    PrintTree(tree)
  
    Prune(tree, 0.5)
    PrintTree(tree)

    
if __name__ == "__main__":
    main()

