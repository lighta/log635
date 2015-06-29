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
cleaned_facts = []
for sen in sentences:
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
	
	
	m2=re.search("\((\w+|\s|,)+(\w+|\s)+\)",str(sen))
	if m2 == None: #no more substitution to do
			break;
	print("\tm2=>"+str(m2))
	
			
	sen = re.sub("  ",' ',sen)
	print("\tcleaned=>"+str(sen))
	cleaned_facts.append('('+str(sen)+')')


