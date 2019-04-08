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

}


def predict(data):
	filename = sys.argv[-1]

	loaded_model = joblib.load("model.pkl")
	# x_data = []
	# with open(filename) as fp:
	# 	for cnt, line in enumerate(fp):
	# 		x_data.append(line.split()[1:-1])
	# X_test_final = []


	X_test = np.zeros(11)
	X_test[0] = 0
	X_test[1] = 11796
	X_test[2] = 5320
	X_test[3] = 4151
	X_test[4] = 3606
	X_test[5] = -8208
	X_test[6] = 535
	X_test[7] = 777
	X_test[8] = 768
	X_test[9] = 745
	X_test[10] = 867

	data = data.strip();
	data = data.split(' ')
	data = list(map(int, data))

	print('data: ', data)

	# X_test_final.append(X_test)
	Y_predicted = loaded_model.predict([data])
	# for pred, sample in zip(Y_predicted, X_test_final):
	# 	print(letterMap[pred])
	# 	result = letterMap[pred]
	print('Y_predicted: ', Y_predicted)
	print(letterMap[Y_predicted[0]])
	return letterMap[Y_predicted[0]]
		

	# 5488 11796 5320 4151 3606 -8208 535 777 768 745 867 