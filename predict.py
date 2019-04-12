from sklearn.externals import joblib
import numpy as np
import sys
filename = "predict_data/" + sys.argv[-1]

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
25: "z"
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

loaded_model = joblib.load("model.pkl")
x_data = []
with open(filename) as fp:
	for cnt, line in enumerate(fp):
		x_data.append(line.split()[1:-1])
# X_test_final = []
# X_test = np.zeros(11)
# X_test[0] = 0
# X_test[1] = 0
# X_test[2] = 0
# X_test[3] = 0
# X_test[4] = 0
# X_test[5] = 0
# X_test[6] = 600
# X_test[7] = 600
# X_test[8] = 600
# X_test[9] = 600
# X_test[10] = 600
Y_predicted = loaded_model.predict(x_data)
for pred, sample in zip(Y_predicted, x_data):
	print(str(wordMap[pred]), end=" ")
