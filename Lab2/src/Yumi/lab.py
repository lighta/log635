# -*- coding: utf-8 -*-

import nltk
import codecs
from nltk import *
#nltk.download()

debug=1 # 0 or 1

sentences= []
file = codecs.open("Einstein.txt", "r", encoding="UTF-8")
for lines in file:
	sentences.append(lines.strip("\r\n").lower())
print(sentences);
""""
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
"""
