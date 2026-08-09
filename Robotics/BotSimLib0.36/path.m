function [] = path(finalpos, finalang, number_of_points, LoS, points,botSim,map)
%% Path calculation and planning occurs from this point.
  % Use the final target as first point
  % LoS is a grid, where if there is a zero then it is free, if it is a 1
  % then it is not. Here a path connecting the robot to the target is used.

    Tpoints     = LoS(:,number_of_points);
    % Returns an empty set of matrices:
    path        = cell(2,1);
    % The first path is a list of the number of points.
    path{1,1}   = [number_of_points];
    path{2,1}   = find(Tpoints);
    foundstart  = sum(path{2,1}==(number_of_points-1));

    %% If target can see start, go straight away
    if foundstart ~=0
        possp = [number_of_points,number_of_points-1];
    end

    m = 0;
    n = 0;

    %% If target not found
    while foundstart == 0
        m=m+1;
        n=n+1;
        if m == 1
            %%create cells to hold path data
            connections = cell(1,number_of_points);
            for i = 1:number_of_points
                connections{i} = find(LoS(1:number_of_points,i));
            end
        end
        [sizey,sizex]=size(path);
        %Check if converged, cell fun applys a function to each cell.
        if n > 1
           work     = path(sizey,1:sizex);
           index    = cellfun(@(x) x==number_of_points-1, work, 'UniformOutput',false);
           cellsum  = cellfun(@sum,index);
           %If complete pick path
           if sum(cellsum) > 0
                which = find(cellsum);

                [~,d] = size(which);
                
                %Create all possible paths
                for i=1:d
                    posspaths(:,i) = path(sizey-1,which(i));
                end
                [~, choose]     = size(posspaths);
                pat             = posspaths(1,ceil(choose/2));
                possp           = cell2mat(pat);
                possp           = [possp,number_of_points-1];
                break;
           end
        end
        
        j=0;
        z=0;
        %Find connections to each current point
        for q = 1:sizex
            z                       = z+1;
            [numberofconnections,~] = size(path{sizey,q});
            connectofthis           = path{sizey,q};
            %connectionnumbers=find(path{sizey,q});
            for i = z:z+numberofconnections-1
                j = j+1;
                %Work out points connected to each of these points
                %Write current path up to this point
                path{sizey+1,i}=[ path{sizey-1,q},connectofthis(j)];
                path{sizey+2,i}=connections{1,connectofthis(j)};


            end
            j=0;
            z=z+numberofconnections-1;
        end
    end

    currentpos=finalpos;
    currentang=finalang;

    %%Follow path found
    z=1;
    possp=fliplr(possp);
    for i = 2:size(possp,2)
        while currentang >= 2*pi
            currentang=currentang-2*pi;
        end
        %currentang=currentang-pi/2;
        z=z+1;
        nextpoint=possp(1,z);
        nextpos=points(nextpoint,:);
        %Work out which way to use angel
        move_distance=sqrt(((nextpos(1)-currentpos(1))^2)+((nextpos(2)-currentpos(2))^2));
        angletwo = abs(atan(-(nextpos(1)-currentpos(1))/-(nextpos(2)-currentpos(2))));
        if nextpos(1)>currentpos(1)
            if nextpos(2)>currentpos(2)
                angle = -angletwo;
            else
                angle =-pi+angletwo;
            end
        else
            if nextpos(2)>currentpos(2)
                angle = angletwo;
            else
                angle=pi-angletwo;
            end
        end
        correct=(2*pi)-currentang+(pi/2);
        angle2=angle+correct;
        
        direction=1;
        while angle2 > 2*pi
            angle2=angle2-2*pi;
        end
        
        if ge(angle2,pi)
            angle2=(2*pi)-angle2;
            direction = -1;
        end

        %Move bot in way needed

        realTurn(angle2,direction);
        sensorTurn(pi/2,1);
        
        [mapy, mapx]    = size(map);
        walls           = zeros(mapy, 2);
        z = 0;
        for i=1:mapy
            z          = z+1;
            walls(i,1) = map(z,1);
            walls(i,2) = map(z,2);
        end

        dataScan1=mode(scanCheck());
        realMove(move_distance/3);
        dataScan2=mode(scanCheck());        
        correction=wall(dataScan1,dataScan2,currentpos,nextpos,move_distance,map,mapy,walls);
        dir = 1;
        if sign(correction)==-1
            dir = -1;
        end
        realTurn(correction,dir);
        sensorTurn(pi/2,-1);
%         turn = pi/2;
%             realTurn(turn,1);
%             scanData1 = scanCheck();
%             move_distance1 = move_distance/3;
%             realMove(move_distance);
%             scanData2 = scanCheck();
%             origin_point=(x,y);
%             theta=atan((scanData2-scanData1)/move_distance1);
%             path_gradient=1/((nextpos(2)-currentpos(2))/(nextpos(1)-currentpos(1)));
%             x = zeros(2,2);
%             y = zeros(2,2);        
%             x(1,1) = currentpos(1);
%             y(1,1) = currentpos(2);            
%             x(2,1)= points(q,1);
%             y(2,1)= points(q,2);
%                 if i == q
%                     LoS(i,q)=0;
%                     continue
%                 end
%                 %%Work out if the lines between the walls and the lines between the points intersect
%                 for z=1:mapy-1 %add way so matrix loops back on itself
%                     x(1,2) = walls(z,1);
%                     y(1,2) = walls(z,2);
%                     x(2,2) = walls(z+1,1);
%                     y(2,2) = walls(z+1,2);
%                     dx     = diff(x);  %# Take the differences down each column
%                     dy     = diff(y);
%                     den    = dx(1)*dy(2)-dy(1)*dx(2);  %# Precompute the denominator
%                     ua     = (dx(2)*(y(1)-y(3))-dy(2)*(x(1)-x(3)))/den;
%                     ub     = (dx(1)*(y(1)-y(3))-dy(1)*(x(1)-x(3)))/den;
% 
%                     isInSegment = all(([ua ub] >= 0) & ([ua ub] <= 1));
% 
%                     if isInSegment == 1
%                         LoS(i,q) = 0;
%                         continue
%                     end
%                 end
%             
%         
%     
%             
%             Pick which wall
%             Find angle between the two
%             Minus angle 
%             Turn robot
            
        realMove(2*move_distance/3);

        figure (1)
        hold on
        line([nextpos(1) currentpos(1)], [nextpos(2) currentpos(2)])
        botSim.drawBot(30,'g'); %draw robot with line length 30 and green
        currentpos=nextpos;
        currentang=currentang+angle2;



    end
end