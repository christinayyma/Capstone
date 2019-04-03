from sklearn.externals import joblib
import numpy as np
import sys
filename = sys.argv[-1]



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
# X_test[10] = 600a)
Y_predicted = loaded_model.predict(x_data)
for pred, sample in zip(Y_predicted, x_data):
	print(str(pred) + " = " + str(sample))
