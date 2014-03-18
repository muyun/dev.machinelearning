#!/usr/bin/python
# -*- coding: utf-8 -*-
#
# Name: CalMean.py
# Function:  Calculate the Mean based on N mapper and N reducer
#
import sys
# a getopt module helps to parse command-line options and arguments
import getopt
import os

def main(argv):
    mappernum = ''
    reducernum = ''
    try:
        opts, args = getopt.getopt(argv, "hm:r:",["map=","reduce="])
    except getopt.GetoptError:
        print 'CalMean.py -m <mapper> -r <reducer>'
        sys.exit(2)
        
    for opt, arg in opts:
        if opt == '-h':
            print 'CalMean.py -m <mapper> -r <reducer>'
            sys.exit()
        elif opt in ("-m", "--map"):
            mappernum = arg
            print 'Mapper num:', mappernum
            #print(mappernum)
        elif opt in ("-r", "--reduce"):
            reducernum = arg
            print 'Reducer num:', reducernum
            #print(reducernum)

    import subprocess
    import time
    
    #rm the files in the local input, bad code
    subprocess.call(["rm", "-rf", "/opt/hadoop-2.2.0/mydemo/MapReduce/input/*"]);
    #usleep = lambda x: time.sleep(x/1000.0)
    
    timeout = 60 * 5 #5 minutes from now
    firsttime = time.time()
    lasttime = firsttime
    print "Begging time: %f" % firsttime
    
    dir = "/opt/hadoop-2.2.0/mydemo/MapReduce/output/"
    while True:
        print("Generate the input data:")
        subprocess.call(["java", "average.DataOutputV2", str(mappernum) ]);
        time.sleep(0.1) #sleep during 100ms
        
        print("Do with the data on hadoop:")
        subprocess.call(["./runhadoop.sh",str(reducernum)]);
        
        #mkdir
        t = time.strftime("%I-%M-%S")
        currdir = dir + t
        if not os.path.exists(currdir):
            os.makedirs(currdir)

        #copyToLocal
        subprocess.call(["hadoop", "fs", "-copyToLocal", "/output/*", currdir ])

        subprocess.call(["rm", "-rf", "/opt/hadoop-2.2.0/mydemo/MapReduce/input/*"]); 
        
        newtime = time.time()
        print "End time: %f" % newtime
        print "Its been %f seconds" % (newtime - firsttime)
        if newtime - lasttime > timeout:
            lasttime = newtime
            print "Running %f seconds" % (newtime - firsttime)
            break

if __name__ == "__main__":
    main(sys.argv[1:])
