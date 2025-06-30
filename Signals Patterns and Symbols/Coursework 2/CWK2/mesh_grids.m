function [mesh_grid_instance] = mesh_grids(characters, resolution)
   
    % This returns a row vector containing the minimum element from each column.
    [minimum_row_vector] = min(characters);
    % This then takes the first element to be the min x value.
    minimum_x_value = minimum_row_vector(1) - 500;
    % This is the y value of the minimum
    minimum_y_value = minimum_row_vector(2) - 500;
    % This is a row vector containing the max from each column (there are
    % only two columns here)
    [maximum_row_vector] = max(characters);
    maximum_x_value = maximum_row_vector(1) + 500;
    maximum_y_value = maximum_row_vector(2) + 500;
   
    % min_val:difference/resolution:max_val
    x_grid = minimum_x_value:(maximum_x_value-minimum_x_value)/resolution:maximum_x_value;
    y_grid = minimum_y_value:(maximum_y_value-minimum_y_value)/resolution:maximum_y_value;
   
    [X, Y] = meshgrid(x_grid, y_grid);
    mesh_grid_instance = [X(:) Y(:)];
end
