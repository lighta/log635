# -*- coding: utf-8 -*-

import nltk
from nltk import *

debug=0 # 0 or 1

sentences= []
#with open ("Einstein.txt", "r") as data:
#	sentences=data.readlines()
sentences.append('Jean tua Marie')

with open ("lab.cfg", "r") as myfile:
	grammaireText=myfile.read()
	

grammar = grammar.FeatureGrammar.fromstring(grammaireText)
parser = nltk.ChartParser(grammar)
parser = parse.FeatureEarleyChartParser(grammar)

facts = []
s = 0
for sen in sentences:
	s+=1
	print ("s="+str(s)+" sen=>"+sen)
	tokens = sen.split()
	trees = parser.parse(tokens)
	for tree in trees:
		if debug==1:
			print(tree)
			nltk.draw.tree.draw_trees(tree)
		facts.append(tree.label()['SEM'])
		print(tree.label()['SEM'])

#write output
with open ("facts.clp", "w") as foutput:
	for sen in facts:
		foutput.write(str(sen))
