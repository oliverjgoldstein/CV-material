import numpy as np
import matplotlib.pyplot as plt
import math
from matplotlib.collections import LineCollection
import sys
import scipy.fftpack

class PositionFinder():

    def __init__(self, times, xs, ys):
        self.times = times
        self.xs = xs
        self.ys = ys

    def indexOfMinDifference(self, spike_time):
        magList = [abs(time) for time in [time - spike_time for time in self.times]]
        minIndex = magList.index(min(magList))
        return minIndex

    def getLocationsOfSpikesGivenTimes(self, neuron_spike_times):
        xs = []
        ys = []
        for spike_time in neuron_spike_times:
            index = self.indexOfMinDifference(spike_time)
            # print spike_time
            xs.append(self.xs[index])
            ys.append(self.ys[index])
        return [xs, ys]

    def getLocationsOfSpikeGivenTime(self, neuron_spike_time):
        xs = []
        ys = []
        index = self.indexOfMinDifference(neuron_spike_time)
        # print spike_time
        xs.append(self.xs[index])
        ys.append(self.ys[index])

        return [xs, ys]

class AutoCorrelogramGenerator():

    def constructAutoTable(self, spike_times, threshold):
        self.difference_table = []
        for current_time in spike_times:
            for other_time in spike_times:
                time_difference = current_time - other_time
                if abs(time_difference) <= threshold:
                    self.difference_table.append(time_difference)

    def getData(self, scaling_factor, spike_times):
        self.difference_table = [int(d / scaling_factor) for d in self.difference_table]
        return [self.difference_table]


class CrossCorrelogramGenerator():

    def __init__(self, all_neuron_data):
        self.all_neuron_data = all_neuron_data

    def calculateCrossCorrelograms(self, threshold):

        difference_tables = []

        for x in xrange(0, len(self.all_neuron_data)):
            for y in xrange(0, len(self.all_neuron_data)):
                if y < x:
                    pair_correlogram = self.calcCorrelogram(self.all_neuron_data[x], self.all_neuron_data[y], threshold)
                    difference_tables.append([pair_correlogram, x, y])

        return difference_tables

    def calcCorrelogram(self, neuron_a, neuron_b, threshold):

        difference_table = []
        for y in neuron_a:
            for x in neuron_b:
                time_difference = y - x
                if abs(time_difference) <= threshold:
                    difference_table.append(time_difference)

        for y in neuron_b:
            for x in neuron_a:
                time_difference = y - x
                if abs(time_difference) <= threshold:
                    difference_table.append(time_difference)

        return difference_table

class FiringRate():

    def calculateFiringRates(self, spike_times, time_interval):
        xs = []
        ys = []

        first_time = min(spike_times)
        last_time = max(spike_times)

        second_count = 0

        while first_time <= (last_time + time_interval):

            # Count the number of neurons firing in this period.
            valid_entry_count = len([val for val in spike_times if val <= (first_time + time_interval) and val >= first_time])

            xs.append(second_count)
            ys.append(valid_entry_count)

            first_time   += time_interval # 1 second
            second_count += 1

        return [xs, ys]

class timeNormalizer():

    def __init__(self, min_time, max_time, sample_rate):
        self.min_time = min_time
        self.max_time = max_time
        self.sample_rate = sample_rate

    def normalizeTimeSecs(self, time, spike_times):
        data = [(t * 10000) + min(spike_times) for t in time]
        return [int((t - self.min_time) / self.sample_rate) for t in data]

    def normalizeTime(self, time, spike_times):
        data = [(t * 10000) + min(spike_times) for t in time]
        return data


class InterestingPointID():

    def getInterestingPoints(self, firing_rates):
        mean = float(sum(firing_rates)/(len(firing_rates)))
        std_dev1 = sum([(t - mean) * (t - mean) for t in firing_rates])
        std_dev2 = std_dev1 / (len(firing_rates) - 1)
        std_dev = math.sqrt(std_dev2)
        results = [g for g in firing_rates if g >= (mean + 1 * std_dev)]
        indices = [firing_rates.index(g) for g in results]
        return indices

if __name__ == "__main__":

    f = open("data/neuron1.csv")
    neuron1_data = map( lambda x: int(x.strip()), f.readlines() )
    f = open("data/neuron2.csv")
    neuron2_data = map( lambda x: int(x.strip()), f.readlines() )
    f = open("data/neuron3.csv")
    neuron3_data = map( lambda x: int(x.strip()), f.readlines() )
    f = open("data/neuron4.csv")
    neuron4_data = map( lambda x: int(x.strip()), f.readlines() )

    f = open("data/x.csv")
    x_data = map( lambda x: float(x.strip()), f.readlines() )
    f = open("data/y.csv")
    y_data = map( lambda x: float(x.strip()), f.readlines() )

    f = open("data/time.csv")
    time = map( lambda x: float(x.strip()), f.readlines() )

    colorsX         = {0 : 'rx', 1 : 'bx', 2 : 'gx', 3 : 'cx'}
    colorsL         = {0 : 'r-', 1 : 'b-', 2 : 'g-', 3 : 'c-'}
    all_neuron_data = [neuron1_data, neuron2_data, neuron3_data, neuron4_data]
    one_second      = 10000

    # Location map
    #
    xlength = float(len(x_data))
    cols = ['b-', 'g-', 'r-', 'c-', 'm-', 'y-']
    for i in xrange(0, 6):
        be = int(float(xlength)*(float(i)/float(6)))
        af = int(float(xlength)*(float(i+1)/float(6)))
        plt.plot(x_data[be:af], y_data[be:af], cols[i], lw=1)


    # Location map with spikes.
    plt.plot(x_data, y_data, 'k-', lw=1, color = '0.85')
    x = PositionFinder(time, x_data, y_data)

    for i in xrange(0, len(all_neuron_data)):
        locs = x.getLocationsOfSpikesGivenTimes(all_neuron_data[i])
        plt.plot(locs[0], locs[1], colorsX[i], label = "Neuron " + str(i+1))

    plt.show()


    # # Auto correlograms:


    scaling_factor = 1
    bin_count      = 100
    threshold      = one_second / 2 # 1 second


    bins = np.linspace(-threshold, threshold, bin_count)
    for j in xrange(0, len(all_neuron_data)):
        x = AutoCorrelogramGenerator()
        x.constructAutoTable(all_neuron_data[j], threshold)
        data = x.getData(scaling_factor, all_neuron_data[j])

        data[0] = [d for d in data[0] if d > (threshold / (bin_count/2)) or d < -(threshold / (bin_count/2))]

        fig = plt.figure()
        histogram = fig.add_subplot(111)
        histogram.set_title("Neuron " + str(j + 1) + " Auto-Correlogram")
        histogram.hist(data, bins)
        histogram.set_ylabel("Frequency")
        histogram.set_xlabel("Time")
        plt.show()





    # Cross correlograms:

    scaling_factor = 1
    bin_count      = 200
    threshold      = one_second / 2 # 1 second

    x = CrossCorrelogramGenerator(all_neuron_data)
    data = x.calculateCrossCorrelograms(threshold)
    bins = np.linspace(-threshold, threshold, bin_count)

    for d in xrange(0, len(data)):
        fig = plt.figure()
        histogram = fig.add_subplot(111)
        histogram.set_title("Neuron " + str(data[d][1] + 1) + " vs. Neuron " + str(data[d][2] + 1) + " Cross Correlogram")
        histogram.hist(data[d][0], bins)
        histogram.set_ylabel("Frequency")
        histogram.set_xlabel("Time")
        plt.show()





    # Firing rates:

    t = timeNormalizer(min(time), max(time), one_second)
    firing_data = []
    time_resolution = one_second

    for j in xrange(0, len(all_neuron_data)):
        y = FiringRate()
        data = y.calculateFiringRates(all_neuron_data[j], time_resolution)
        data[0] = t.normalizeTimeSecs(data[0], all_neuron_data[j])
        firing_data.append(data)

    fig = plt.figure()
    graph = fig.add_subplot(111)
    graph.set_title("Firing rate for neurons")

    for j in xrange(0, len(firing_data)):
        graph.plot(firing_data[j][0], firing_data[j][1], colorsL[j], label = "Neuron " + str(j+1))

    graph.set_ylabel("Frequency")
    graph.set_xlabel("Time")

    plt.legend(bbox_to_anchor=(0., 1.0, 1., .102), loc=3,
           ncol=4, mode="expand", borderaxespad=0.)

    plt.show()

    # High firing rate points

    y = InterestingPointID()
    x = PositionFinder(time, x_data, y_data)
    t = timeNormalizer(min(time), max(time), one_second)
    interesting_pts = []

    plt.plot(x_data, y_data, 'k-', lw=1, color = '0.85')

    for j in xrange(0, len(firing_data)):
        times = y.getInterestingPoints(firing_data[j][1])
        times = t.normalizeTime(times, all_neuron_data[j])
        locs = x.getLocationsOfSpikesGivenTimes(times)
        plt.plot(locs[0], locs[1], colorsX[j], label = "Neuron " + str(j+1))

    plt.legend(bbox_to_anchor=(0., 1.0, 1., .102), loc=3,
           ncol=4, mode="expand", borderaxespad=0.)

    plt.show()



    # Phase precession using firing rate.
    time_frame = one_second/8
    t = timeNormalizer(min(time), max(time), time_frame)
    time_resolution = one_second/8

    neuron = 1

    y = FiringRate()
    data = y.calculateFiringRates(all_neuron_data[neuron], time_frame)
    data[0] = t.normalizeTimeSecs(data[0], all_neuron_data[neuron])
    firing_data = data[1]

    fig = plt.figure()
    graph = fig.add_subplot(111)
    graph.set_title("Phase of neuron "+str(neuron+1)+" against time")

    lower_val = 0
    upper_val = len(firing_data)
    diff = 16
    phase_data = []
    time_data = []

    p = PositionFinder(time, x_data, y_data)
    while(lower_val < upper_val):
        if(lower_val + diff <= upper_val):
            f_data = firing_data[lower_val:upper_val]
            fft_data = np.fft.fft(f_data)

            # Take the weighted average phase value in the area.
            largest_valid_f = 0
            weighted_av = 0
            for f in fft_data:
                I = f.imag
                R = f.real
                phase = np.arctan2(I, R) * 180 / np.pi
                size = np.abs(f)
                if phase > 5: # Ensure a non zero phase.
                    if(np.abs(largest_valid_f) < size):
                        largest_valid_f = f

            I = largest_valid_f.imag
            R = largest_valid_f.real

            phase = np.rad2deg(np.arctan2(I, R))

            phase_data.append(phase)

            time_data.append((min(all_neuron_data[neuron]) - min(time) + lower_val * time_frame * diff)/160000)

        lower_val += diff


    graph.set_ylabel("Phase (Degrees)")
    graph.set_xlabel("Time (S)")

    plt.plot(time_data, phase_data, colorsL[neuron])

    # Finally find the points where the rat enters and leaves the place point.
    locs = p.getLocationsOfSpikesGivenTimes(all_neuron_data[neuron])
    start_points = []
    end_points = []

    in_place = False

    for j in xrange(0, len(locs[0])):
        fire_time = (all_neuron_data[neuron][j] - min(time))/10000
        if locs[0][j] >= 80 and locs[0][j] <= 160:
            if locs[1][j] >= 100 and locs[1][j] <= 140:
                if in_place == False:
                    start_points.append(fire_time)
                in_place = True
            elif in_place == True:
                end_points.append(fire_time)
                in_place = False
        elif in_place == True:
                end_points.append(fire_time)
                in_place = False

    plt.plot(start_points, [10 for r in (start_points)], 'bx')
    plt.plot(end_points, [10 for r in (end_points)], 'rx')

    plt.grid()
    plt.show()




























    # EOF
