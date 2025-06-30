function [] = plotRaster ( spikeMat , tVec )
% Visualize raster plot
hold all ;
for trialCount = 1: size (spikeMat)
spikePos = tVec ( spikeMat ( trialCount ,:) ) ;
for spikeCount = 1: length ( spikePos )
plot ([ spikePos ( spikeCount ) spikePos ( spikeCount ) ] , [trialCount]) ;
end
end

% ylim ([0, size (spikeMat,1) + 1]);