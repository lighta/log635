# -*- coding: utf-8 -*-

import nltk
import codecs
import re
from nltk import *

showTextTree = 1 # 0 or 1
showDrawnTree = 1 # 0 or 1

#pattern = '([A-Z][a-z]*)[ ]([A-Z])'
#pattern_obj = re.compile(pattern)
#replacement_string="\\1" + '_' + "\\2"

sentences= []
with codecs.open("Einstein.txt", "r", encoding="UTF-8") as f:
	fileContent = f.readlines()

for lineIndex in range(len(fileContent)):
	print("Checking line number " + str(lineIndex) + "...")
	currentLine = str(fileContent[lineIndex]).replace('\r\n','')
	currentLine = currentLine.replace('Pall Mall','Pall_Mall') # mettre ici tous les replace qu'on a besoin
	#currentLine = pattern_obj.sub(replacement_string, currentLine)
	currentLine = currentLine.replace('\'',' ')
	print("Line is '" + currentLine + "'")
	if (len(currentLine) > 0 and currentLine[0] == "#"):
		print("Line is a comment; will not use.")
	else:
		splitSentence = currentLine.split('.')
		for oneSentence in splitSentence:
			if (len(oneSentence) > 5): # completely arbitrary minimum length
				sentences.append(oneSentence[0].lower() + oneSentence[1:])
				sentences.append(oneSentence[0].upper() + oneSentence[1:])

print("")

for oneSentence in sentences:
		print("One sentence is '" + oneSentence + "'")

print("")

print("Loading grammar file...")
grammarFile = codecs.open("lab.cfg", "r", encoding="UTF-8")
grammaireText = grammarFile.read()

print("Parsing...")
grammar = grammar.FeatureGrammar.fromstring(grammaireText)
parser = nltk.ChartParser(grammar)
parser = parse.FeatureEarleyChartParser(grammar)

print("Generating facts...")
print("")

facts = []
s = 0
for sen in sentences:
	print("")
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
			if showTextTree==1:
				print(tree)
			if showDrawnTree==1:
				nltk.draw.tree.draw_trees(tree)
			facts.append(tree.label()['SEM'])
			print(tree.label()['SEM'])
      
print("")

#write output
with open ("facts.clp", "w") as foutput:
	for sen in facts:
		foutput.write(str(sen)+ '\n')
