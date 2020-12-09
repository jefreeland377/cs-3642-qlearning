# Solving a 2D Maze Using Q-Learning -- CS 3642 Artificial Intelligence Final Project

Given the (a) side length of a two-dimensional maze and (b) a maximum number of timesteps to iterate through, this program attempts to solve a random maze of the given size using Q-learning.
Mazes are generated with a recursive backtracking method. Each cell is a distinct state containing the Q-score of each potential action, initialized to 0. The reward function is the sum of the AI's Manhattan distance from the goal position and the amount of timesteps that have currently elapsed-- the AI seeks to minimize this reward, rather than maximize it.
This project utilizes the epsilon greedy strategy, where the chance that the AI will randomly explore its environment starts at 100% and decreases to 0% linearly throughout the simulation.
