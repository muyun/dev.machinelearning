#!/usr/bin/env
# -*- coding: utf-8 -*-

"""
@author wenlong
"""
# this is the library for the code
import mglearn
import matplotlib.pyplot as plt
import numpy as np

def load_data():
    # y = f(X)
    X, y = mglearn.datasets.make_forge()
    print("X.shape: {}".format(X.shape))
    # create a scatter matrix
    mglearn.discrete_scatter(X[:, 0], X[:,1], y)
    plt.xlabel("First feature")
    plt.ylabel("Second feature")
    plt.legend(["Class 0", "Class 1"], loc='lower right')
    plt.show()

def load_cancer_data():
    # clinical measurements of breast cancer tumors
    # for the classification
    from sklearn.datasets import load_breast_cancer
    cancer = load_breast_cancer() # cancer is like dict

    #import pdb; pdb.set_trace()
    # Format contain "replacement fields" surrounded by {}
    print("cancer.keys(): \n{}".format(cancer.keys()))
    print("shape of cancer data: {}".format(cancer.data.shape))
    print("sample counts per class:\n{}".format(
        {n:v for n, v in zip(cancer.target_names, np.bincount(cancer.target))}
    )) # bincount counts number of occurrences of each value in array of ints
    print("Feature names:\n{}".format(cancer.feature_names))

def load_boston_data():
    from sklearn.datasets import load_boston
    boston = load_boston()
    print("boston.keys(): \n{}".format(boston.keys()))
    print("Data shape: {}".format(boston.data.shape))

    # get more features
    X, y = mglearn.datasets.load_extended_boston()
    print("X.shape: {}".format(X.shape))

def main():
    #import pdb; pdb.set_trace()
    #load_data_data()

    # a classification dataset
    #load_cancer_data()
    # a real-world regression dataset
    load_boston_data()
#
if __name__ == '__main__':
    main()
