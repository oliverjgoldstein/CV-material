% Move forward function
function realMove(move_distance)
%% Clear and close
COM_CloseNXT all
% clear all
% close all

move_distance=int16(move_distance);
%% Constants and so on
%QuarterTurnTicks = 219;      % in motor degrees, how much is a 90° turn of the bot?
Ports = [MOTOR_A; MOTOR_B; MOTOR_C];  % motorports for left and right wheel
DrivingSpeed     = 80;
WheelTurns = 360*(move_distance/10.3); %in degrees of motor rotations
WheelTurns=floor(WheelTurns);
%% Open Bluetooth connetion
h = COM_OpenNXT('bluetooth.ini');
COM_SetDefaultNXT(h);

%% Set up motors for driving.

mStraight                   = NXTMotor([MOTOR_B; MOTOR_C]);
% next command since we are driving in SYNC-mode. This should not be
% necessary with correct default values, but at the moment, I have to set
% it manually,
mStraight.SpeedRegulation   = false;  % not for sync mode
mStraight.Power             = DrivingSpeed;
mStraight.TachoLimit        = WheelTurns;
mStraight.ActionAtTachoLimit = 'Brake';

for j = 1 : 1
    mStraight.SendToNXT();
    mStraight.WaitFor();
    %pause(1);
end

%% Close Bluetooth connection
COM_CloseNXT(h);

end