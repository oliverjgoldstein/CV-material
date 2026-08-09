
# Python Standard Libraries Used
import sys
import socket
import multiprocessing
import threading
import time
from threading import Thread


# User functions
import Twitter_Data_Streams
import Global_Enums
import Weights

process_one       = None
process_two       = None

class bcolors:
    WARNING     = '\033[93m'
    ENDC        = '\033[0m'

class Stream():

    def __init__( self ):

        self.__stream_type = None
        self.__process_one = None
        self.__process_two = None

    def initialiseTwitterProcesses(
        self,
        keyword, 
        sentiment_type,
        stream_type,
        upper_bound,
        lower_bound,
        disable_retweets,
        session_id,
        days_in_past ):
    
        self.__process_one = multiprocessing.Process( target = Twitter_Data_Streams.liveTweetStream,
                                                args = (
                                                    keyword, 
                                                    sentiment_type,
                                                    stream_type,
                                                    upper_bound,
                                                    lower_bound,
                                                    disable_retweets,
                                                    session_id ))
        
        self.__process_two = multiprocessing.Process( target = Twitter_Data_Streams.pastStream, 
                                                args = (
                                                    keyword, 
                                                    sentiment_type,
                                                    stream_type,
                                                    upper_bound,
                                                    lower_bound,
                                                    disable_retweets,
                                                    session_id,
                                                    days_in_past )) 

        self.__stream_type = stream_type
    
        # Live or both streams:
        if( stream_type == Global_Enums.Stream_Type.live_stream or stream_type == Global_Enums.Stream_Type.both_streams ):
    
            self.__process_one.start()
    
        # # Past or both streams:
        if( stream_type == Global_Enums.Stream_Type.past_stream or stream_type == Global_Enums.Stream_Type.both_streams ):
    
            self.__process_two.start()

    def killTwitterProcesses( self ):
    
        # Live or both streams:
        if( self.__stream_type == Global_Enums.Stream_Type.live_stream or self.__stream_type == Global_Enums.Stream_Type.both_streams ):
            print bcolors.WARNING + '\nProcess one terminating.\n' + bcolors.ENDC
            self.__process_one.terminate()
            self.__process_one.join()
    
        # # Past or both streams:
        if( self.__stream_type == Global_Enums.Stream_Type.past_stream or self.__stream_type == Global_Enums.Stream_Type.both_streams ):
            print bcolors.WARNING + '\nProcess two terminating.\n' + bcolors.ENDC
            self.__process_two.terminate()
            self.__process_two.join()
    

