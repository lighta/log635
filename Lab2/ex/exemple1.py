# -*- coding: utf-8 -*-

import nltk
from nltk import *

debug=1 # 0 or 1
with open ("exemple1.cfg", "r") as myfile:
    grammaireText=myfile.read()

grammar = grammar.FeatureGrammar.fromstring(grammaireText)
parser = nltk.ChartParser(grammar)
tokens = "Jean tua Marie blsnl fgdfd Marie".split()


parser = parse.FeatureEarleyChartParser(grammar)
trees = parser.parse(tokens)
for tree in trees:
    if debug==1:
        print(tree)
        nltk.draw.tree.draw_trees(tree)
    print(tree.label()['SEM'])

