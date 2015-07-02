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

npword= []
with codecs.open("dico_np.txt", "r", encoding="UTF-8") as f:
	fileContent2 = f.readlines()
	
for lineIndex2 in range(len(fileContent2)):
	currentLine2 = str(fileContent2[lineIndex2])
	currentLine2 = re.sub("(\r|\n)",'',currentLine2) #replace \r \n , with ' '
	m6=re.search("^#",str(currentLine2))
	if m6 != None:
		print("Line is a comment2; will not use.")
	if len(currentLine2) <= 1:
		print("Line is a comment; will not use.")
	else:
		#print("Dico_np => "+currentLine2)
		npword.append(currentLine2)
	
sentences= []
with codecs.open("Einstein.txt", "r", encoding="UTF-8") as f:
	fileContent = f.readlines()

print("npword="+str(npword))
for lineIndex in range(len(fileContent)):
	#print("Checking line number " + str(lineIndex) + "...")
	currentLine = str(fileContent[lineIndex]) #.replace('\r\n','')
	currentLine = re.sub("(\r|\n)",'',currentLine) #replace \r \n , with ' '
	currentLine = currentLine.replace('Pall Mall','Pall_Mall')
	currentLine = currentLine.replace('Blue Master','Blue_Master')# mettre ici tous les replace qu'on a besoin
	#currentLine = pattern_obj.sub(replacement_string, currentLine)
	currentLine = currentLine.replace('\'',' ')
	currentLine = currentLine.replace(',','')
	#print("Line is '" + currentLine + "'")
	if (len(currentLine) <= 1 or currentLine[0] == "#"):
		print("Line is a comment; will not use.")
	else:
		splitSentence = currentLine.split('.')		#chaque point donne une phrase
		for oneSentence in splitSentence:
			if (len(oneSentence) > 5): # completely arbitrary minimum length
				oneSentence = oneSentence.strip()	#trim both side
				words = oneSentence.split(' ')
				if(str(words[0]) in npword):
					#print("Word in dico") #on ne change pas le case
					sentences.append(oneSentence)
				else:	# on met en minuscules
					#print("word0="+words[0]) 
					sentences.append(oneSentence[0].lower() + oneSentence[1:])

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
	
	doublecheck=0
	while True:
		mf=re.search("\(",sen)
		if mf == None: #no more substitution to do
			break;
				
		#extract (fact)
		#regle 1 we have a simple fact
		while True:
			m=re.search("\((\w+|\s|\|)+\)",str(sen))			#direct (word) without coma
			if m == None: #no more NP substitution to do
				break;
			s = list(str(sen))
			s[m.start()] = ' '
			s[m.end()-1] = ''
			newsen = "".join(s)
			sen = newsen
			#print("\tnewsen(np)=>"+str(sen))
			doublecheck=0

		#move verbe instead coma
		#regle 2 we have a verb
		while True:
			m2=re.search("\((\w+|\s|\|)+,(\w+|\s|\|)+\)",str(sen))			# 2 mot separe par une virgule
			if m2 == None: #no more VP substitution to do
				break;
			m4=re.search(",",str(sen))
			m2pos = m2.start()
			m6=re.search("(\w+)\((\w+|\s|\|)+,(\w+|\s|\|)+\)",str(sen))			# meme regex preceder par un mot
			if m6 == None: #no more VP substitution to do
				break;
			word = m6.group(1)												#le verb est le premier mot
			
			tmp = sen[m2pos:m2.end()]										#tmp = string matched by regex
			m4=re.search(",",str(tmp))
			m4pos = m4.start()
			
			s = list(str(tmp))
			s[0] = ' ' #replace ( by ' '
			s[m4pos] = '' #replace , by ''
			s[len(str(tmp))-1] = ' ' #replace ) by ' '
			
			#newsen = tmp[:m2pos]+tmp[m2pos:m4pos]+" "+word+" "+tmp[m4pos:]
			s.insert(m4pos,' ')
			i=1
			for wi in list(str(word)):
				s.insert(m4pos+i,wi)
				i += 1;
			s.insert(m4pos+i,' ')
			newsen = "".join(s)
			
			#print("\tnewsen=>"+str(newsen))
			sen = sen[:m6.start()]+newsen+sen[m6.end():] #remove word from sentence
			#print("\tnewsen(vp2)=>"+str(sen))
			doublecheck=0
		
		#regle 3 we have a list
		#this should only fire if r1 and r2 not available
		if doublecheck==1:
			m2=re.search("(\w+)\((\w+|\s|,)+(\w+|\s)+\)",str(sen))
			if m2 == None: #no more substitution to do
				break;
			word = m2.group(1)
			sword = len(str(word))
			tmp = sen[m2.start():m2.end()]
			#print("\ttmp=>"+str(tmp))
			
			s = list(str(tmp))
			s[sword] = ' '
			s[len(str(tmp))-1] = '|'
			
			s.insert(0,' ')
			newsen = "".join(s)
			tmp = newsen 
			tmp = re.sub(",",' ',tmp) #replace all , with ' '
			tmp = "|list"+tmp
			#print("\ttmp2=>"+str(tmp))
			
			sen = sen[:m2.start()]+tmp+sen[m2.end():]
			#print("\tnewsen(list)=>"+str(sen))
			doublecheck=0
		else:
			doublecheck=1
	
	m3=re.search("\|(\w+|\s*)+\|",str(sen))
	if m3 != None:
		s = list(str(sen))
		s[m3.start()] = '('
		s[m3.end()-1] = ')'
		newsen = "".join(s)
		sen = newsen
		#print("\tnewsen(list2)=>"+str(sen))
	
	sen = re.sub("( )+",' ',sen)
	print("\tcleaned=>"+str(sen))
	cleaned_facts.append('('+str(sen)+')')

#write output
with open ("facts.clp", "w") as foutput:
	for sen in cleaned_facts:
		foutput.write(str(sen)+'\n')
