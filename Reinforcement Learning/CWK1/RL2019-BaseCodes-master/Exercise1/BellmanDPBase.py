from MDP import MDP
import matplotlib.pyplot as plt
import numpy as np

class BellmanDPSolver(object):
	def __init__(self, discount_rate):
		self.MDP = MDP()
		self.discount_rate = discount_rate
		self.policy = dict()
		self.initVs()

	def initVs(self):
		# Initialise the state values to zero.
		self.V = dict()
		for s in self.MDP.S:
			self.V[s] = 0
		self.initPolicy()

	def initPolicy(self):
		# Initialise the policy
		self.policy = dict()
		for s in self.MDP.S:
			self.policy[s] = []

	def BellmanUpdate(self):

		self.initPolicy()

		for s in self.MDP.S:

			current_vals = dict()

			for a in self.MDP.A:
				next_states = []

				state_prob = self.MDP.probNextStates(s, a)

				if sum(state_prob.values()) == 0:
					print("No reward!")

				if(len(state_prob) == 0):
					print("No next states - invalid action")
					continue

				new_val = 0
				for (s_next, prob) in state_prob.items():
					new_val += prob * (self.MDP.getRewards(s, a, s_next) + self.discount_rate * self.V[s_next]) # Can be negative.


				current_vals[a] = new_val

			# Create the policy now we have the maximal value.
			max_val = max(current_vals.values())
			self.V[s] = max_val

			# Get all the actions with this maximum value:
			for (action, val) in current_vals.items():
				if max_val == val:#+0.00000000000001:
					self.policy[s].append(action)

		self.reorder_policy()
		return (self.V, self.policy)

	def reorder_policy(self):
		for s in self.MDP.S:
			new_array = []
			if self.MDP.A[0] in self.policy[s]:
				new_array.append(self.MDP.A[0])
			if self.MDP.A[1] in self.policy[s]:
				new_array.append(self.MDP.A[1])
			if self.MDP.A[2] in self.policy[s]:
				new_array.append(self.MDP.A[2])
			if self.MDP.A[3] in self.policy[s]:
				new_array.append(self.MDP.A[3])
			if self.MDP.A[4] in self.policy[s]:
				new_array.append(self.MDP.A[4])
			self.policy[s] = new_array

if __name__ == '__main__':
	solution = BellmanDPSolver(0.9)
	for i in range(0,1000):
		values, policy = solution.BellmanUpdate()
	print("Values : ", values)
	print("Values : ", policy)
