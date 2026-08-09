function matrix = nearest_neighbour()
    testData = load('og14775.test.txt');
    trainData = load('og14775.train.txt');
    featureSet = horzcat(testData(:, 1), testData(:, 2));
    featureSetTraining = horzcat(trainData(:, 1), trainData(:, 2));
    rng(1);
    [idx, C] = kmeans(featureSetTraining, 3)
   
    matrix = []
    matrix = [(pdist2(featureSet, C, 'euclidean'))]
    newMatrix = []
    labelMatrix = []
     for id = 1:numel(featureSet(:,1))
         newMatrix = [newMatrix; min(matrix(id,:))]
         if(min(matrix(id, :)) == (pdist2(featureSet(id,:), C(1,:), 'euclidean')))
             labelMatrix = [labelMatrix; 1]
         elseif(min(matrix(id, :)) == (pdist2(featureSet(id,:), C(2,:), 'euclidean')))
             labelMatrix = [labelMatrix; 2]
         else (min(matrix(id, :)) == (pdist2(featureSet(id,:), C(3,:), 'euclidean')))
             labelMatrix = [labelMatrix; 3]
         end
     end
    matrix = labelMatrix
end
    