% Turn on the spot function
function sensorTurn(turn,direction)
%% Clear and close
COM_CloseNXT all
% clear all
% close all

turn = turn;
amountToTurn = calibrate(turn);
amountToTurn=int16(amountToTurn);
%% Constants and so on
%QuarterTurnTicks = 219;      % in motor degrees, how much is a 90° turn of the bot?
Ports = [MOTOR_A];  % motorports for left and right wheel
TurningSpeed     = 50;
QuarterTurnTicks = amountToTurn/2; % in motor degrees, how much is a 90° turn of the bot?

%% Open Bluetooth connetion
h = COM_OpenNXT('bluetooth.ini');
COM_SetDefaultNXT(h);

%% Set up motors for turning


mTurn3                      = NXTMotor(Ports(1)); % ports swapped because it's nicer
mTurn3.SpeedRegulation      = false;  % we could use it if we wanted
mTurn3.Power                = TurningSpeed*direction;
mTurn3.TachoLimit           = QuarterTurnTicks;



 mTurn3.SendToNXT();    
 mTurn3.WaitFor();
    


%% Close Bluetooth connection
COM_CloseNXT(h);

end
