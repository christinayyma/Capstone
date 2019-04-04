from sklearn.externals import joblib
import numpy as np
import sys

loaded_model = joblib.load("models/a2i_model.pkl")

filename = "predict_data/" + sys.argv[-1]
x_data = []
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
}
with open(filename) as fp:
	for cnt, line in enumerate(fp):
		x_data.append(line.split()[1:-1])

Y_predicted = loaded_model.predict(x_data)
for each in Y_predicted:
	print(letterMap[each] + " ", end = "")