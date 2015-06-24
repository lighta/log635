# -*- coding: utf-8 -*-

import nltk
from nltk import *

with open ("2_phrase_steve.cfg", "r") as myfile:
    grammaireText=myfile.read()

grammar = grammar.FeatureGrammar.fromstring(grammaireText)
parser = nltk.ChartParser(grammar)
#tokens = "Le Suédois a des chiens comme animaux domestiques".split()
tokens = "La personne qui boit du thé est un Danois".split()


parser = parse.FeatureEarleyChartParser(grammar)
trees = parser.parse(tokens)
for tree in trees:
    print(tree)
    nltk.draw.tree.draw_trees(tree)
    print(tree.label()['SEM'])
