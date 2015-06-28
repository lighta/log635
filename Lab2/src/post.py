import codecs
import re

sentences = []
with codecs.open("facts.clp", "r", encoding="UTF-8") as f:
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
#				sentences.append(oneSentence[0].lower() + oneSentence[1:])
				sentences.append(oneSentence[0].upper() + oneSentence[1:])

print("")

s = 0
for sen in sentences:
	print("sen="+str(sen))
	
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
		print("\tnewsen=>"+str(newsen))

	#while True:
	#	m=re.search(",")",str(newsen))			#2 mot separe par une virgule
	#	if m == None: #no more substitution to do
	#		break;
	#	m2=re.search("\(",str(sen))
	#	m2pos = m2.start()
	#	if m2pos != m.start(): #we are in an inner block
	#		print("innner block found")
		
		#s = list(str(sen))
		#s[m.start()] = ' '
		#s[m.end()-1] = ''
		#newsen = "".join(s)
		#sen = newsen
		#print("match="+str(m))
		#print("\tnewsen=>"+str(newsen))


