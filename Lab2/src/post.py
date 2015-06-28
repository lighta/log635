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
		print("\tnewsen(np)=>"+str(sen))

	while True:
		m2=re.search("\((\w+|\s)+,(\w+|\s)+\)",str(sen))			# 2 mot separe par une virgule
		if m2 == None: #no more substitution to do
			break;
		m3=re.search("\(",str(sen))
		m4=re.search(",",str(sen))
		m2pos = m2.start()
		m3pos = m3.start()
		m4pos = m4.start()
		
		m6=re.search("(\w+)\((\w+|\s)+,(\w+|\s)+\)",str(sen))			# 2 mot separe par une virgule
		word = m6.group(1)
		tmp = sen[m2pos:m2.end()]
		#print("m6="+str(m6)+ "word="+str(m6.group(1))+ " tmp="+tmp )
		m4=re.search(",",str(tmp))
		
		s = list(str(sen))
		s[m2pos] = '' #replace ( by ' '
		s[m4pos] = ' ' #replace , by ' '
		s[m2.end()-1] = '' #replace ) by ' '
		
		s.insert(m4pos,' ')
		i=1
		for wi in list(str(word)):
			s.insert(m4pos+i,wi)
			i += 1;
		newsen = "".join(s)
		sen = newsen
		sen = sen[:m6.start()] + sen[m2pos:] #remove word from sentence
		print("\tnewsen(vp2)=>"+str(sen))


