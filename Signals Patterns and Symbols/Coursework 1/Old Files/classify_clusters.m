function success = classify_clusters()
    trainingData = load('og14775.train.txt');
    testData = load('og14775.test.txt');
    testSet = horzcat(testData(:, 1), testData(:, 2));
    featureSet = horzcat(trainingData(:, 1), trainingData(:, 2));
    rng(1);
    
    
    [idx, C, sumd] = kmeans(featureSet, 3, 'Distance', 'sqeuclidean')
%   [idx, C, sumd] = kmeans(featureSet, 3, 'Distance', 'sqeuclidean', 'Start',  [2 6; 100000 100000; 1 5], 'MaxIter',5)
%   subplot(1,1,1);

    knn = knnsearch(C, testSet);
    figure;
    hold on
    
    meanOne = mean(horzcat(featureSet(idx==1,1),featureSet(idx==1,2)))
    meanTwo = mean(horzcat(featureSet(idx==2,1),featureSet(idx==2,2)))
    meanThree = mean(horzcat(featureSet(idx==3,1),featureSet(idx==3,2)))
    
    covOne = cov(horzcat(featureSet(idx==1,1),featureSet(idx==1,2)))
    covTwo = cov(horzcat(featureSet(idx==2,1),featureSet(idx==2,2)))
    covThree = cov(horzcat(featureSet(idx==3,1),featureSet(idx==3,2)))
   
    [x, y] = meshgrid(linspace(0, 10)', linspace(0, 10)');
    G = [x(:) y(:)];
    
    multivariateDistOne = mvnpdf(G, meanOne, covOne)
    multivariateDistOne = reshape(multivariateDistOne, [100, 100])
    multivariateDistTwo = mvnpdf(G, meanTwo, covTwo)
    multivariateDistTwo = reshape(multivariateDistTwo, [100, 100])
    multivariateDistThree = mvnpdf(G, meanThree, covThree)
    multivariateDistThree = reshape(multivariateDistThree, [100, 100])
    
    level = clclevel(meanOne, covOne);
    
        level = mvnpdf([class_mu_x v], mu , sigma)
    
    contour(linspace(0, 10), linspace(0, 10), multivariateDistOne, [level level])
    contour(linspace(0, 10), linspace(0, 10), multivariateDistTwo, 30)
    contour(linspace(0, 10), linspace(0, 10), multivariateDistThree, 200)
    
    plot(featureSet(idx==1,1),featureSet(idx==1,2),'b+','MarkerSize', 10)
    plot(featureSet(idx==2,1),featureSet(idx==2,2),'g+','MarkerSize', 10)
    plot(featureSet(idx==3,1),featureSet(idx==3,2),'r+','MarkerSize', 10)
    plot(testSet(knn==1,1),testSet(knn==1,2),'bx','MarkerSize', 10)
    plot(testSet(knn==2,1),testSet(knn==2,2),'gx','MarkerSize', 10)
    plot(testSet(knn==3,1),testSet(knn==3,2),'rx','MarkerSize', 10)
    plot(C(:,1),C(:,2),'kx','MarkerSize',13,'LineWidth',1)
    voronoi(C(:,1),C(:,2))
    legend('Cluster 1','Cluster 2','Cluster 3','Centroids')
    title 'Big Data Assignment One'
    hold off
    success = 1;
%     disp(covOne)
    
end