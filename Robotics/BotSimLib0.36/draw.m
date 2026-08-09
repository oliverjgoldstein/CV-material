function [] = draw(botSim, first_particle_reallocate, number_of_particles_kept, particles, targets, converged, num, target, I)
    figure(1);
    hold off;
    botSim.drawMap();
    botSim.drawBot(30,'g'); % draw robot with line length 30 and green

    % Draw reallocated particles.
    for i = first_particle_reallocate:num
        particles(i).drawBot(3);
    end

    % Draw kept particles.
    for i =1:number_of_particles_kept
        particles(i).drawBot(5,'r');
        targets.setBotPos(target);
        targets.drawBot(50,'g');
    end
    particles(I).drawBot(70,'k');

    
    drawnow;
end
