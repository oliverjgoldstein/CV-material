function [botSim] = localise(botSim,map,target)

COM_CloseNXT all %prepares workspace
h=COM_OpenNXT(); %look for USB devices
COM_SetDefaultNXT(h);
NXT_PlayTone(440, 500);

botSim.setMap(map);


[number_of_points, LoS, finalpos, finalang, points] = pfl(botSim, map, target);

path(finalpos, finalang, number_of_points, LoS, points, botSim, map);

end
