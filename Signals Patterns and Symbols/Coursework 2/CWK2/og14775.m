training_vector = [
'CHAR_T'; 'CHAR_T'; 'CHAR_T'; 'CHAR_T'; 'CHAR_T'; 'CHAR_T'; 'CHAR_T'; 'CHAR_T'; 'CHAR_T'; 'CHAR_T';   
'CHAR_V'; 'CHAR_V'; 'CHAR_V'; 'CHAR_V'; 'CHAR_V'; 'CHAR_V'; 'CHAR_V'; 'CHAR_V'; 'CHAR_V'; 'CHAR_V';
'CHAR_S'; 'CHAR_S'; 'CHAR_S'; 'CHAR_S'; 'CHAR_S'; 'CHAR_S'; 'CHAR_S'; 'CHAR_S'; 'CHAR_S'; 'CHAR_S';
          ];

array_of_files = {
            'T1.GIF'; 'T2.GIF'; 'T3.GIF'; 'T4.GIF'; 'T5.GIF'; 'T6.GIF'; 'T7.GIF'; 'T8.GIF'; 'T9.GIF'; 'T10.GIF';
            'V1.GIF'; 'V2.GIF'; 'V3.GIF'; 'V4.GIF'; 'V5.GIF'; 'V6.GIF'; 'V7.GIF'; 'V8.GIF'; 'V9.GIF'; 'V10.GIF';
            'S1.GIF'; 'S2.GIF'; 'S3.GIF'; 'S4.GIF'; 'S5.GIF'; 'S6.GIF'; 'S7.GIF'; 'S8.GIF'; 'S9.GIF'; 'S10.GIF'
};

% Iterate through all files and append the correct directory.

for file_iterator = 1:30,
    array_of_files{ file_iterator } = strcat( 'characters/', array_of_files{ file_iterator } )
end

% Extract the feature (a 1x2 array) for each character image.
% Put this feature into the 2xn character array for s.
% Do this for all character images. 

 character_t = [];
 for file_iterator = 1:10,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     character_t = [character_t; [feature_one, feature_two]];
 end
 
 character_v = [];
for file_iterator = 11:20,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     character_v = [character_v; [feature_one, feature_two]];
end

 character_s = [];
 for file_iterator = 21:30,
     % gets two integers back
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     % this will be 10 high and two across.
     character_s = [character_s; [feature_one, feature_two]];
 end

%%%%%%%%%%%%%%%%%%%%%%%%%%% character T validation

array_of_files = {
            'normT1.GIF'; 'normT2.GIF'; 'normT3.GIF'; 'normT4.GIF'; 'normT5.GIF'; 'normT6.GIF';
            'normS1.GIF'; 'normS2.GIF'; 'normS3.GIF'; 'normS4.GIF'; 'normS5.GIF'; 'normS6.GIF'; 
            'normV1.GIF'; 'normV2.GIF'; 'normV3.GIF'; 'normV4.GIF'; 'normV5.GIF'; 'normV6.GIF';
            'fatS1.GIF' ; 'fatT1.GIF' ; 'fatV1.GIF' ;
            'distortS.GIF' ; 'distortT.GIF' ; 'distortV.GIF' ;
            'noise.gif'; 'typedS.GIF'; 'typedT.GIF'; 'typedV.GIF';
            'rotateS.GIF' ; 'rotateT.GIF' ; 'rotateV.GIF' ;
            'B1.GIF'; 'A1.GIF'; 'smallS.gif'; 'smallT.gif'; 'smallV.gif';
 };

for file_iterator = 1:36,
    array_of_files { file_iterator } = strcat( 'test-characters/', array_of_files{ file_iterator } )
end

test_t = [];

for file_iterator = 1:6,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     test_t = [test_t; [feature_one, feature_two]];
end

%%%%%%%%%%%%%%%%%%% CHARACTER S validation

test_s = [];

for file_iterator = 7:12,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     test_s = [test_s; [feature_one, feature_two]];
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Character V validation

test_v = [];

for file_iterator = 13:18,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     test_v = [test_v; [feature_one, feature_two]];
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Fat characters:

fat_characters = [];

for file_iterator = 19:21,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     fat_characters = [fat_characters; [feature_one, feature_two]];
end
 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Distorted characters:

distort_characters = [];

for file_iterator = 22:25,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     distort_characters = [distort_characters; [feature_one, feature_two]];
end
 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Distorted characters:

typed_characters = [];

for file_iterator = 26:28,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     typed_characters = [typed_characters; [feature_one, feature_two]];
end

rotated_characters = [];

for file_iterator = 29:31,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     rotated_characters = [rotated_characters; [feature_one, feature_two]];
end

A_and_B = [];

for file_iterator = 32:33,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     A_and_B = [A_and_B; [feature_one, feature_two]];
end

small_chars = [];

for file_iterator = 34:36,
     [feature_one, feature_two] = feature_shape( array_of_files{ file_iterator } );
     small_chars = [small_chars; [feature_one, feature_two]];
end


% k-nearest neighbour classification:
% Sample data across regions.

 figure;
 hold on;
 % This argument stacks the 10x2 matrices on top of each other = 30x2
 mesh_grid_instance = mesh_grids( [character_s; character_t; character_v], 80);
 knn = knnclassify( mesh_grid_instance, [character_t; character_v; character_s], training_vector, 1 );
 gscatter (mesh_grid_instance(:,1), mesh_grid_instance(:,2), knn);
 plot(character_t(:,1), character_t(:,2), 'X',  test_t(:,1), test_t(:,2), 'V', character_s(:,1), character_s(:,2), 'X', test_s(:,1), test_s(:,2), 'V', character_v(:,1), character_v(:,2), 'X', test_v(:,1), test_v(:,2), 'V')
 plot( fat_characters(1,1), fat_characters(1,2), 'o', fat_characters(2,1), fat_characters(2,2), 'o', fat_characters(3,1), fat_characters(3,2), 'o' );
 plot( distort_characters(1,1), distort_characters(1,2), '>', distort_characters(2,1), distort_characters(2,2), '>', distort_characters(3,1), distort_characters(3,2), '>', distort_characters(4,1), distort_characters(4,2), '+' );
 plot( typed_characters(1,1), typed_characters(1,2), '<', typed_characters(2,1), typed_characters(2,2), '<', typed_characters(3,1), typed_characters(3,2), '<');
 plot( A_and_B(1,1), A_and_B(1,2), '*', A_and_B(2,1), A_and_B(2,2), '*');
 plot( small_chars(1,1), small_chars(1,2), '*', small_chars(2,1), small_chars(2,2), '*', small_chars(3,1), small_chars(3,2), '*');
 plot( rotated_characters(1,1), rotated_characters(1,2), '^', rotated_characters(2,1), rotated_characters(2,2), '^', rotated_characters(3,1), rotated_characters(3,2), '^');
 legend('Area T','Area S','Area V','Character T', 'Validation Set T', 'Character S', 'Validation Set S', 'Character V', 'Validation Set V', 'Fat S', 'Fat T', 'Fat V', 'Distorted S', 'Distorted T', 'Distorted V', 'Uniform Noise', 'Arial Typed S', 'Arial Typed T', 'Arial Typed V','A','B','Small S','Small T', 'Small V','Rotated S','Rotated T', 'Rotated V');
 title('Features plotted for each character.')
 xlabel('Feature One')
 ylabel('Feature Two')
 hold off;

figure;
hold on;
% This argument stacks the 10x2 matrices on top of each other = 30x2
mesh_grid_instance = mesh_grids( [character_s; character_t; character_v], 1000);
knn = knnclassify( mesh_grid_instance, [character_s; character_t; character_v], training_vector, 1 );
gscatter (mesh_grid_instance(:,1), mesh_grid_instance(:,2), knn);
legend('Character T','Character S','Character V');
hold off;

figure;
hold on;
title('Features plotted for each character.')
xlabel('Feature One')
ylabel('Feature Two')
plot(character_t(:,1), character_t(:,2), 'X', character_s(:,1), character_s(:,2), 'X', character_v(:,1), character_v(:,2), 'X')
legend('Character T','Character S','Character V');


ctree = grow_tree( character_s, character_v, character_t, 10 )
view(ctree, 'Mode', 'graph')
for v_value = 1:3
    Ynew = predict(ctree,distort_characters(v_value,:))
    disp(Ynew)
end
