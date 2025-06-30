%function success = classify_clusters()
%change the directory to load your data
trainingData = load('../Data/sg14776.train.txt');
testData = load('../Data/sg14776.test.txt');
testSet = horzcat(testData(:, 1), testData(:, 4));
featureSet = horzcat(trainingData(:, 1), trainingData(:, 4));
rng(1);
% Comment in the following line to use plotmatrix.
%plotmatrix(trainingData)
[idx, C, sumd] = kmeans(featureSet, 3, 'Distance', 'sqeuclidean');
% comment in the line below in order to break kmeans. One can also set
% 'Maxiter', '5' for instance to break it more.
% [idx, C, sumd] = kmeans(featureSet, 3, 'Distance', 'sqeuclidean', 'Start',  [2 6; 100000 100000; 1 5])

knn = knnsearch(C, testSet);
figure;
hold on

meanOne = mean(horzcat(featureSet(idx==1,1),featureSet(idx==1,2)));
meanTwo = mean(horzcat(featureSet(idx==2,1),featureSet(idx==2,2)));
meanThree = mean(horzcat(featureSet(idx==3,1),featureSet(idx==3,2)));

covOne = cov(horzcat(featureSet(idx==1,1),featureSet(idx==1,2)));
covTwo = cov(horzcat(featureSet(idx==2,1),featureSet(idx==2,2)));
covThree = cov(horzcat(featureSet(idx==3,1),featureSet(idx==3,2)));

x = linspace(0, 10);
y = linspace(0, 10);

[X, Y] = meshgrid(x,y);
G = [X(:) Y(:)];

multivariateDistOne = mvnpdf(G, meanOne, covOne);
multivariateDistTwo = mvnpdf(G, meanTwo, covTwo);
multivariateDistThree = mvnpdf(G, meanThree, covThree);

%Following taken from bivar.m

for i = 1:length(x)
    for (j = 1:length(y))
        Pone(j,i) = getprob(x(i),y(j),meanOne',covOne);
        Ptwo(j,i) = getprob(x(i),y(j),meanTwo',covTwo);
        Pthree(j,i) = getprob(x(i),y(j),meanThree',covThree);
        
    end
end


% Add the 2* below in order to change the decision boundary.
%LR = (2*Pone)./Ptwo;
%LR2 = (2*Pone)./Pthree;
LR = (Pone)./Ptwo;
LR2 = (Pone)./Pthree;
LR3 = Ptwo./Pthree;

contour(X,Y,Pone,[6 6], 'k');
contour(X,Y,Ptwo,[6 6], 'k');
contour(X,Y,Pthree,[6 6], 'k');

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% use mvnpdf to calculate Pone, ..., Pthree again

contour(x,y,LR,[1 1],'g');
contour(x,y,LR2,[1 1],'k');
contour(x,y,LR3,[1 1],'b');



%level = mvnpdf([class_mu_x v], mu, sigma)

plot(featureSet(idx==1,1),featureSet(idx==1,2),'b+','MarkerSize', 10)
plot(featureSet(idx==2,1),featureSet(idx==2,2),'g+','MarkerSize', 10)
plot(featureSet(idx==3,1),featureSet(idx==3,2),'r+','MarkerSize', 10)
plot(testSet(knn==1,1),testSet(knn==1,2),'bx','MarkerSize', 10)
plot(testSet(knn==2,1),testSet(knn==2,2),'gx','MarkerSize', 10)
plot(testSet(knn==3,1),testSet(knn==3,2),'rx','MarkerSize', 10)
plot(C(:,1),C(:,2),'kx','MarkerSize',13,'LineWidth',1)
voronoi(C(:,1),C(:,2))
legend('Contour boundary','Contour boundary','Contour boundary','Dec boundary Cluster 1, Cluster 2','Dec boundary Cluster 1 Cluster 3','Dec boundary Cluster 2 Cluster 3','Cluster 1','Cluster 2','Cluster 3','Test Points','Test Points','Test Points','Centroids')
title 'Figure 4a'
hold off
success = 1;


