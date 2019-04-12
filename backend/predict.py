from sklearn.externals import joblib
import numpy as np
import sys

letterMap = {
	0: "a",
	1: "b",
	2: "c",
	3: "d",
	4: "e",
	5: "f",
	6: "g",
	7: "h",
	8: "i",
	9: "j",
	10: "k",
	11: "l",
	12: "m",
	13: "n",
	14: "o",
	15: "p",
	16: "q",
	17: "r",
	18: "s",
	19: "t",
	20: "u",
	21: "v",
	22: "w",
	23: "x",
	24: "y",
	25: "z",
}


wordMap = {
	0: "hello",
	1: "yes",
	2: "no",
	3: "my",
	4: "name is",#take care
	5: "you",
	6: "thank you",
	7: "how are",
	8: "I'm fine",
	9: "deaf",
	10: "I'm",
	11: "no problem",
	12: "what's up?",
	13: "i agree",
	14: "take care",
	15: "i love you",
	16: "drink",
	17: "eat",
	18: "water", #no problem
	19: "bathroom"
}


def predict(data):
	filename = sys.argv[-1]

	print('data: ', data)


	data = data.strip()
	data = data.split(' ')
	orientation = data[0]
	if (len(data) == 13):
		del data[12]
		del data[0]
	data = list(map(int, data))

	if (orientation == "RSTART"):
		loaded_model = joblib.load("model-letters.pkl")
		print("right hand")
	elif (orientation == "LSTART"):
		loaded_model = joblib.load("model-words.pkl")
		print("left hand")
	else:
		loaded_model = joblib.load("model.pkl")

	# X_test_final.append(X_test)
	Y_predicted = loaded_model.predict([data])
	probability = loaded_model.predict_proba([data])
	
	probability[0] = sorted(probability[0], reverse = True)

	# if the highest probability is less than 0.3, return 0
	if (probability[0][0] < 0.3):
		return "0"

	print('probability: ', probability)
	print(letterMap[Y_predicted[0]])
	# print wordmap here if left hand
	if (orientation == "LSTART"):
		return wordMap[Y_predicted[0]]
	else:
		return letterMap[Y_predicted[0]]