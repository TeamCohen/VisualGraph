import sys
import os
import shutil

""" OUTPUT CONSTs """
edge_delimeter  = ">>>"
ve_delimeter    = ">>"
in_delimeter    = ">"
file_prefix     = sys.argv[1] + ".vg/"

""" INPUT CONSTs """
in_edge_delimiter   = ":"
in_srcdst_delimiter = "->"
in_f_delimiter      = ","
in_fw_delimiter     ="@"


""" Start generator code """
# Define numebr generator for use to index filenames
# for graphs
def numberGenerator():
    n=0
    while True:
        yield n
        n += 1

generator=numberGenerator()
""" End generator code """

# fn to extract feature and create Map to ref
def extractFeatureMap(feature_string):
    features = feature_string.split(":")
    feature_dict = {}
    for i in range(1,len(features)+1):
        # Create Dictionary
        feature_dict[i] = features[i-1]
    return feature_dict

# Find next file to write
def getStoreLine():
    return file_prefix + str(next(generator)) + ".baked"

# Function to map to all edges to parse
# Function Closure for featureMap
def retEdgeStringFn(featureMap):
    def mapFeature(x):
        parts = x.split(in_fw_delimiter)
        return featureMap[int(parts[0])]
    def retEdgeString(e):
        edge_feature_split = e.split(in_edge_delimiter)

        # Parse v1 -> v2
        edge_details = edge_feature_split[0]
        vertices = edge_details.split(in_srcdst_delimiter)
        v1 = vertices[0]
        v2 = vertices[1]
        vstr = v1 + in_delimeter + v2

        # Parse features
        feature_details = edge_feature_split[1]
        feature_indices = feature_details.split(in_f_delimiter)
        try:
            feature_names = map(mapFeature, feature_indices)
        except KeyError:
            print "Problem parsing edge: "+e
            print "In map: "+str(featureMap)
            raise
        fstr = in_delimeter.join(feature_names)

        # Combine Strings
        comb_str = vstr + ve_delimeter + fstr
        return comb_str
    return retEdgeString


def writeGraphToFile(filename_to_store, store_edge_strings):
    # Write data to file
    f = open(filename_to_store,"w")
    write = f.write
    for i in range(len(store_edge_strings)):
        write(store_edge_strings[i])
        if i < len(store_edge_strings) -1:
            write(edge_delimeter)
    f.close()


def parseLine(line):
    # Header:
    filename_to_store = getStoreLine()
    print "Parsing graph to: " + filename_to_store

    # Parse line details
    line = line.rstrip()
    tab_split = line.split('\t')

    # Format of data
    # 0 - 5 := Non-relevant Data
    # 6     := String of features
    # 7:    := Edges
    feature_string = tab_split[6]
    edge_strings = tab_split[7:]

    # Gets all features
    featureMap = extractFeatureMap(feature_string)

    # Map all edges to correct format
    try:
        store_edge_strings = map(retEdgeStringFn(featureMap),edge_strings)
    except KeyError:
        print "Problem parsing line for query:" + tab_split[0]
        sys.exit(-1)

    # Write graph to file
    writeGraphToFile(filename_to_store, store_edge_strings)

def parseData(filename, entryparser):
    f = open(filename)
    lines = f.readlines()
    f.close()
    shutil.rmtree(file_prefix)
    os.mkdir(file_prefix)
    datapoints = map(entryparser, lines)
    return datapoints

def printHelp():
    print "Usage:"
    print "\t" + sys.argv[0] + " graphtxtfile"

def main () :
    # Header + parse arguments
    print "Welcome to ProPPR Graph Parser"
    filename = ""
    if len(sys.argv) < 2:
        printHelp()
        sys.exit(-1)
    else:
        filename=str(sys.argv[1])

    print "Parsing your graph file..."
    print "-" * 80

    # Parse data
    parseData(filename, parseLine)

if __name__ == "__main__":
    main()
