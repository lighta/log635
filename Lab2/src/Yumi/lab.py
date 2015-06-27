# -*- coding: utf-8 -*-

import nltk
import codecs
import re
from nltk import *
#nltk.download()

pattern = '([A-Z][a-z]*)[ ]([A-Z])'
pattern_obj = re.compile(pattern)
replacement_string="\\1" + '_' + "\\2"

debug=1 # 0 or 1
lines= []
sentences= []
file = codecs.open("Einstein.txt", "r", encoding="UTF-8")

for x in file:
	x = pattern_obj.sub(replacement_string, x)
	print(x);
	lines = x.lower().replace('\'',' ').split('.')
lines = filter(None, lines)
sentences = lines
print(sentences);

grammarFile = codecs.open("lab.cfg", "r", encoding="UTF-8")
grammaireText = grammarFile.read()

grammar = grammar.FeatureGrammar.fromstring(grammaireText)
parser = nltk.ChartParser(grammar)
parser = parse.FeatureEarleyChartParser(grammar)

facts = []
s = 0
for sen in sentences:
	writeFacts = 1
	s+=1
	print ("s="+str(s)+" sen=>"+str(sen))
	tokens = str(sen).split()
	try:
		parser.parse(tokens)
	except:
		writeFacts = 0
	if(writeFacts == 1):
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
                foutput.write(str(sen)+ '\n')

