import math
import numpy as np
import random
import matplotlib.pyplot as plt
from scipy.signal import argrelextrema

def grapher(x_vals, y_vals, label_x, label_y, title):
    fig = plt.figure()
    graph = fig.add_subplot(111)
    graph.set_title(title)
    graph.plot(x_vals, y_vals)
    graph.set_ylabel(label_y)
    graph.set_xlabel(label_x)
    plt.show()


def partFour(i_init, i_final, increment, h):

    i_vals       = np.arange(i_init, i_final, increment)
    spike_counts = []
    El           = -70e-3
    Rm           = 10e6
    Tm           = 10e-3
    Vr           = -70e-3
    Vt           = -40e-3
    x_lower      = 0.0
    x_upper      = 1.0
    V_initial    = 1.0
    h            = 1e-3

    for x in range(0, len(i_vals)):
        neuron = Neuron()
        neuron.initialiseBodyParams(El, Rm * i_vals[x], Tm, Vr, Vt, V_initial)
        spike_counts.append(neuron.countSpikes(x_lower, x_upper, h))

    grapher(i_vals, spike_counts, 'Current', 'Spike Count', 'Question 4')

class Neuron():

    def initialiseBodyParams(self, El, RmIe, Tm, Vr, Vt, voltage):
        self.El          = El
        self.RmIe        = RmIe
        self.Tm          = Tm
        self.Vr          = Vr
        self.Vt          = Vt
        self.voltage     = voltage
        self.initDefault()
        # The following are preset variables.

    def initDefault(self):
        self.xs           = []
        self.ys           = []
        self.spike_count  = 0
        self.Es           = 0
        self.Ek           = 0
        self.spike        = False
        self.isExcitatory = True
        self.k_strength   = 0
        self.s_strength   = 0

    def setElk(self, Ek):
        self.Ek = Ek

    def voltage_derivative(self):
        term_one   = (self.El - self.voltage + (self.RmIe))
        # In the following lines Rm and G have been accounted for in the strengths.
        term_two   = self.s_strength * (self.Es - self.voltage)
        term_three = self.k_strength * (self.Ek - self.voltage)
        term_four  = (term_one + term_two + term_three) / self.Tm
        return term_four

    def updatePostS(self, s_strength):
        self.s_strength = s_strength

    def updateSlowK(self, k_strength):
        self.k_strength = k_strength

    def simulateNeuron(self, x_lower, x_upper, delta):

        while(x_lower < x_upper):

            self.xs.append(x_lower)
            self.ys.append(self.voltage)

            self.voltage += (delta * self.voltage_derivative())
            x_lower      += delta

            if(self.voltage > self.Vt):
                self.spike_count += 1
                self.spike        = True
                self.voltage      = self.Vr
            else:
                self.spike = False

    def countSpikes(self, x_lower, x_upper, delta):
        self.simulateNeuron(x_lower, x_upper, delta)
        return self.spike_count

    def setExcitatory(self, isExcitatory):
        self.isExcitatory = isExcitatory

    def resetSim(self):
        self.spike_count = 0
        self.xs          = []
        self.ys          = []

    def graph(self, title):
        grapher(self.xs, self.ys, "Time", "Voltage", title)



class FullyConnectedNN():
    def __init__(self, neuron_one, neuron_two, synapse_one, synapse_two, isExcitatory):
        self.neuron_one   = neuron_one
        self.neuron_two   = neuron_two
        self.synapse_one  = synapse_one
        self.synapse_two  = synapse_two
        self.isExcitatory = isExcitatory
        self.neuron_one.setExcitatory(self.isExcitatory)
        self.neuron_two.setExcitatory(self.isExcitatory)

    def propagate(self, x_lower, x_upper, h):

        while x_lower < x_upper:

            strength_one = self.synapse_one.update(h)
            strength_two = self.synapse_two.update(h)

            self.neuron_one.updatePostS(strength_two)
            self.neuron_two.updatePostS(strength_one)

            self.neuron_one.simulateNeuron(x_lower, x_lower+h, h)
            self.neuron_two.simulateNeuron(x_lower, x_lower+h, h)

            self.synapse_one.spikeGS(self.neuron_one.spike)
            self.synapse_two.spikeGS(self.neuron_two.spike)

            x_lower += h

class PotassiumIonChannel():
    def __init__(self, Gk, Gk_Addition, strength, Ts, neuron, Rm):
        self.Gk          = Gk
        self.Gk_Addition = Gk_Addition
        self.strength    = strength
        self.Ts          = Ts
        self.neuron      = neuron
        self.Rm          = Rm

    def decay(self):
        return ((-self.strength) / self.Ts)

    def updateKChannel(self, h):
        if self.neuron.spike == True:
            self.strength    += 1
            self.time_elapsed = 0
        else:
            self.strength += h * self.decay()

        self.neuron.updateSlowK(self.Gk * self.Rm * self.strength)


class Synapse():
    def initialiseSynapseParams(self, RmGs, P, Ts, strength, use_alpha):
        self.RmGs      = RmGs
        self.P         = P
        self.Ts        = Ts
        self.strength  = strength
        self.use_alpha = use_alpha
        self.t         = 0
        self.previous_synapses = []

    def decay(self):
        return ((-self.strength) / self.Ts)

    def alpha(self):
        return ((self.t/self.Ts) * math.e**(-self.t/self.Ts)) * math.e

    def spikeGS(self, isSpike):
        if isSpike == True:
            if self.use_alpha == True:
                self.t = 0
                self.previous_synapses.append(SynapseAlphaDecay(self.t, self.Ts))
            else:
                self.strength += self.P

    def update(self, h):
        if self.use_alpha == False:
            self.strength += h * self.decay()
            return self.RmGs * self.strength
        else:
            self.t += h
            if len(self.previous_synapses) > 0:
                [x.update(h) for x in self.previous_synapses]
                sum                    = [x.getVal() for x in self.previous_synapses]
                addition               = reduce(lambda x, y: x+y, sum)
                return self.RmGs * (self.alpha() + addition)
            else:
                return self.RmGs * self.alpha()

class SynapseAlphaDecay():
    def __init__(self, t, Ts):
        self.t  = t
        self.Ts = Ts

    def update(self, delta):
        self.t += delta

    def getVal(self):
        return ((self.t/self.Ts) * math.e**(-self.t/self.Ts)) * math.e

if __name__ == "__main__":

    # All parts:
    TimeL = 0
    TimeU = 1
    Delta = 1e-3

    # Part 1:

    El          = -70e-3
    RmIe        = 10e6*3.1e-9
    Tm          = 10e-3
    Vr          = -70e-3
    Vt          = -40e-3
    voltage_I   = -70e-3

    pms = [El, RmIe, Tm, Vr, Vt, voltage_I]
    part_one = Neuron()
    part_one.initialiseBodyParams(pms[0], pms[1], pms[2], pms[3], pms[4], pms[5])
    part_one.simulateNeuron(TimeL, TimeU, Delta)
    # part_one.graph("Question 1")

    # Part 2 done in pages.

    # Part 3:

    El          = -70e-3
    RmIe        = 10e6*2.9e-9
    Tm          = 10e-3
    Vr          = -70e-3
    Vt          = -40e-3
    voltage_I   = -70e-3

    pms = [El, RmIe, Tm, Vr, Vt, voltage_I]
    part_three = Neuron()
    part_three.initialiseBodyParams(pms[0], pms[1], pms[2], pms[3], pms[4], pms[5])
    part_three.simulateNeuron(TimeL, TimeU, Delta)
    part_three.graph("Question 3")

    # Part 4:

    I_initial = 2e-9
    I_final   = 5e-9
    increment = 1e-10

    # partFour(I_initial, I_final, increment, Delta)

    # Part 5:

    Tm   = 20e-3
    El   = -70e-3
    Vr   = -80e-3
    Vt   = -54e-3
    RmIe = 18e-3

    pms = [El, RmIe, Tm, Vr, Vt]

    RmGs         = 0.15
    P            = 0.5
    Ts           = 10e-3
    S            = 0.5
    isExcitatory = False
    useAlpha     = True

    sps = [RmGs, P, Ts, S]

    if isExcitatory == True:
        Es = 0
    else:
        Es = -80e-3

    neuron_one = Neuron()
    neuron_two = Neuron()
    synapse_one = Synapse()
    synapse_two = Synapse()

    neuron_one.initialiseBodyParams(pms[0], pms[1], pms[2], pms[3], pms[4], random.uniform(Vr, Vt))
    neuron_two.initialiseBodyParams(pms[0], pms[1], pms[2], pms[3], pms[4], random.uniform(Vr, Vt))
    synapse_one.initialiseSynapseParams(sps[0], sps[1], sps[2], sps[3], useAlpha)
    synapse_two.initialiseSynapseParams(sps[0], sps[1], sps[2], sps[3], useAlpha)

    neuron_one.Es = Es
    neuron_two.Es = Es

    nn = FullyConnectedNN(neuron_one, neuron_two, synapse_one, synapse_two, isExcitatory)
    nn.propagate(TimeL, TimeU, Delta)

    fig = plt.figure()
    graph = fig.add_subplot(111)
    graph.set_title("Q7: Inhibitory Setup With Alpha")

    graph.plot( neuron_one.xs, neuron_one.ys, label = "Neuron One" )
    graph.plot( neuron_two.xs, neuron_two.ys, label = "Neuron Two" )
    plt.legend(bbox_to_anchor=(0., 1.05, 1., .102), loc=3,
           ncol=2, mode="expand", borderaxespad=0.)

    graph.set_ylabel("Voltage")
    graph.set_xlabel("Time")
    plt.show()

    # plt.show()

    # Part 6, part 1 params:

    El        = -70e-3
    Ie        = 3.1e-9
    Rm        = 10e6
    Tm        = 10e-3
    Vr        = -70e-3
    Vt        = -40e-3
    voltage_I = -70e-3

    # Specific for part 6

    El_K        = -80e-3
    Gk_addition = 5e-9
    Gk          = 5e-9
    TsK         = 200e-3
    S_initial   = 0

    pms         = [El, Rm * Ie, Tm, Vr, Vt, voltage_I]
    neuron_six  = Neuron()
    neuron_six.initialiseBodyParams(pms[0], pms[1], pms[2], pms[3], pms[4], pms[5])
    neuron_six.setElk(El_K)

    k_channel = PotassiumIonChannel(Gk, Gk_addition, S_initial, TsK, neuron_six, Rm)

    while TimeL < TimeU:
        k_channel.updateKChannel(Delta)
        neuron_six.simulateNeuron(TimeL, TimeL + Delta, Delta)
        TimeL += Delta

    # neuron_six.graph("Question 6")












# EOF
