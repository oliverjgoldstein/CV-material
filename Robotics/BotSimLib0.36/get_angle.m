function theta = get_angle(x, y, alpha, x1, y1)
    
    vec1 = [cosd(alpha); sind(alpha)];
    vec2 = [x1; y1] - [x; y];
    
    vec1 = vec1/norm(vec1);
    vec2 = vec2/norm(vec2);
    
    inv_dot = dot(vec1, vec2)/(norm(vec1)*norm(vec2));
    theta = acosd(inv_dot);

    vec1 = [vec1; 0]
    vec2 = [vec2; 0]
    
    if(vec1(1) * vec2(2) > vec2(1) * vec1(2))
        theta = - theta
    end
    
    if x == x1
        if y == y1
            theta = 0
        end
    end
        
end