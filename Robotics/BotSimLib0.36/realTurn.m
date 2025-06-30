% Turn on the spot function
function realTurn(turn,direction)
%% Clear and close
COM_CloseNXT all
% clear all
% close all

turn = turn + deg2rad(10);
amountToTurn = calibrate(turn);
amountToTurn=int16(amountToTurn);
%% Constants and so on
%QuarterTurnTicks = 219;      % in motor degrees, how much is a 90° turn of the bot?
Ports = [MOTOR_A; MOTOR_B; MOTOR_C];  % motorports for left and right wheel
TurningSpeed     = 100;
QuarterTurnTicks = amountToTurn; % in motor degrees, how much is a 90° turn of the bot?

%% Open Bluetooth connetion
h = COM_OpenNXT('bluetooth.ini');
COM_SetDefaultNXT(h);

%% Set up motors for turning

%right wheel
mTurn1                      = NXTMotor(Ports(3)); % ports swapped because it's nicer
mTurn1.SpeedRegulation      = false;  % we could use it if we wanted
mTurn1.Power                = TurningSpeed*direction;
mTurn1.TachoLimit           = QuarterTurnTicks;

% left wheel
mTurn2          = mTurn1;
mTurn2.Port     = Ports(2);   % ports swapped again...
mTurn2.Power    = - mTurn1.Power;

for j = 1 : 1
    mTurn1.SendToNXT();
    mTurn2.SendToNXT();
    mTurn2.WaitFor();
    mTurn1.WaitFor();
end

%% Close Bluetooth connection
COM_CloseNXT(h);

end
