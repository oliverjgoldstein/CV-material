% Take ultrasound readings function
function readings=realScan(scan_num,n)
%% Clear and close
COM_CloseNXT all
% clear all
% close all

numScans = scan_num;
anglePerScan = 360/numScans;

%% Constants and so on
Ports = [MOTOR_A];  % motorports for left and right wheel
portUS      = SENSOR_4; % Set up ultrasound port
%WheelTurns = 360; %in degrees of motor rotations
UltraSoundTurningSpeed = 100; 

%% Open Bluetooth connetion
h = COM_OpenNXT('bluetooth.ini');
COM_SetDefaultNXT(h);

%% Set up sensor wheel
mTurn3                      = NXTMotor(Ports(1)); % ports swapped because it's nicer
mTurn3.SpeedRegulation      = false;  % we could use it if we wanted
mTurn3.Power                = UltraSoundTurningSpeed;

if mod(n,2)==0
    mTurn3.Power = mTurn3.Power*(-1); % flips the turning direction each time to avoid cable tangling.
else
    mTurn3.Power = mTurn3.Power;
end

mTurn3.TachoLimit        = floor(anglePerScan*3.2);


%% Initialize ultrasound
OpenUltrasonic(portUS, 'snapshot')

nBytes          = 8;         % bytes the US sensor received
readingsPerAngle      = 3;       % how many readings per angle?
plotcols   = 8;         % how many out of n echos to plot?
outOfRange = 160;       % setting for out of range readings
colors = flipud(hot(8));

allX = (1:readingsPerAngle+1)';

spins = 1; % number of times to alternate clockwise/anticlockwise ultrascans.
dataStore = zeros(numScans,readingsPerAngle);
 
for m = 1:numScans
data = zeros(1, nBytes);
    %ultrasound measuring.
    for i = 1 : readingsPerAngle
        USMakeSnapshot(portUS)
        pause(0.05);            % wait for the sound to travel
        echos = USGetSnapshotResults(portUS);
        echos(echos == 255) = outOfRange;
        echos = [echos(1); diff(echos)];
        data = vertcat(data, echos');
        x = allX(1:i+1);
        
%         hold on
%         set(gca, 'Color', 'black');
%         axis([0 readingsPerAngle 0 outOfRange])
%         for k = plotcols : -1 : 1
%             area(x, data(:, k) , 'FaceColor', colors(k, :))
%         end
        if data(i+1,1) > 100
            data(i+1,1)=90;
        end
        dataStore(m,i)=data(i+1,1); % store the ultrasound reading into the j'th spin and m'th scan.
    end
    clearvars x data
    mTurn3.SendToNXT();
    mTurn3.WaitFor();
    mTurn3.Stop('brake');
end

readings = mode(dataStore,2);% returns the mean of each spin of sensor data, will
% hopefully give more accurate readings.

if mod(n,2)==0
    readings=flipud(readings);
end

readings=readings;

%% Close Bluetooth connection
CloseSensor(portUS)
COM_CloseNXT(h);
end