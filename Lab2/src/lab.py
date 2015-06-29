# -*- coding: utf-8 -*-

import nltk
import codecs
import re
from nltk import *

showTextTree = 0 # 0 or 1
showDrawnTree = 0 # 0 or 1

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
		splitSentence = currentLine.split('.')		#chaque point donne une phrase
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
	s+=1
	print ("s="+str(s)+" sen=>"+str(sen))
	tokens = str(sen).split()
	try:
		trees = parser.parse(tokens)
		for tree in trees:
			if showTextTree==1:
				print(tree)
			if showDrawnTree==1:
				nltk.draw.tree.draw_trees(tree)
			facts.append(tree.label()['SEM'])
			print(tree.label()['SEM'])
	except:
		print("An exception occur while parsing tokens")
		
      
print("")


#post traitement
cleaned_facts = []
for sen in facts:
	sen = str(sen)
	print("sen="+str(sen))
	
	#extract (fact) 
	while True:
		m=re.search("\((\w+|\s)+\)",str(sen))			#direct (word) without coma
		if m == None: #no more substitution to do
			break;
		s = list(str(sen))
		s[m.start()] = ' '
		s[m.end()-1] = ''
		newsen = "".join(s)
		sen = newsen
		#print("match="+str(m))
		print("\tnewsen(np)=>"+str(sen))

	#move verbe instead coma
	while True:
		m2=re.search("\((\w+|\s)+,(\w+|\s)+\)",str(sen))			# 2 mot separe par une virgule
		if m2 == None: #no more substitution to do
			break;
		m4=re.search(",",str(sen))
		m2pos = m2.start()
		
		m6=re.search("(\w+)\((\w+|\s)+,(\w+|\s)+\)",str(sen))			# meme regex preceder par un mot
		word = m6.group(1)												#le verb est le premier mot
		
		tmp = sen[m2pos:m2.end()]										#tmp = string matched by regex
		m4=re.search(",",str(tmp))
		m4pos = m4.start()
		
		s = list(str(tmp))
		s[0] = ' ' #replace ( by ' '
		s[m4pos] = '' #replace , by ''
		s[len(str(tmp))-1] = ' ' #replace ) by ' '
		
		s.insert(m4pos,' ')
		i=1
		for wi in list(str(word)):
			s.insert(m4pos+i,wi)
			i += 1;
		s.insert(m4pos+i,' ')
		newsen = "".join(s)
		
		#print("\tnewsen=>"+str(newsen))
		sen = sen[:m6.start()]+newsen+sen[m6.end():] #remove word from sentence
		print("\tnewsen(vp2)=>"+str(sen))
	
	sen = re.sub("  ",' ',sen)
	print("\tcleaned=>"+str(sen))
	cleaned_facts.append('('+str(sen)+')')

#write output
with open ("facts.clp", "w") as foutput:
	for sen in cleaned_facts:
		foutput.write(str(sen)+'\n')
