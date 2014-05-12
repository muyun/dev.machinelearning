package svm;

public class SVMTest {
	public static void main(String [] args) {  
		SVM s = new SVM();
		
		Problem train = new Problem();
		Problem test = new Problem();
		train.loadBinaryProblem("/home/zhaowenlong/workspace/proj/dev.machinelearning/machinelearning/src/svm/train");
		test.loadBinaryProblem("/home/zhaowenlong/workspace/proj/dev.machinelearning/machinelearning/src/svm/test");

		System.out.println("Training...");
		KernelParams kp = new KernelParams(1,1,1,1);
		s.svmTrain(train, kp, 1);
//		s.svmTrain(train);
		System.out.println("Testing...");
		int [] pred = s.svmTest(test);
		for (int i=0; i<pred.length; i++)
			System.out.println(pred[i]);
		
		EvalMeasures e = new EvalMeasures(test, pred, 2);
		System.out.println("Accuracy=" + e.Accuracy());
		
		System.out.println("Done.");
	}
}
