from multiprocessing.pool import ThreadPool
from statistics import mean
from statistics import median
from collections import Counter
import multiprocessing
import re
import subprocess
import sys
import time

"""
This script is a testing framework for the Network game.

https://stackoverflow.com/questions/26774781/python-multiple-subprocess-with-a-pool-queue-recover-output-as-soon-as-one-finis
https://stackoverflow.com/questions/6893968/how-to-get-the-return-value-from-a-thread-in-python

"""

def main(argv):
	(player_1, player_2, iterations, threads) = parse_input()
	command = ['java', 'Network', '-q', player_1, player_2]

	print(argv)
	print((player_1, player_2, iterations, threads))

	sys.stdout.flush()

	p1_moves_count = []
	p2_moves_count = []
	p1_wins = 0
	p2_wins = 0
	p1_moves = []
	p2_moves = []
	counter_white_moves = Counter()
	pool = ThreadPool(threads)
	results = [pool.apply_async(run, (command,)) for i in range(0, iterations)]
	for result in results:
		(winner, moves_count, moves) = result.get()
		if (winner == 1):
			p1_wins += 1
			p1_moves_count.append(moves_count)
			p1_moves.extend(moves)
		else:
			p2_wins += 1
			p2_moves_count.append(moves_count)
			p2_moves.extend(moves)
	# print([result.get() for result in results])
	pool.close()
	pool.join()

	print_results(player_1, player_2, p1_wins, p2_wins, p1_moves_count, p2_moves_count, p1_moves, p2_moves)

# sets up initial parameters passed in: 1st and 2nd player, number of iterations, and number of threads
def parse_input():
	if len(sys.argv) < 4:
		raise ValueError('Invalid parameters. Must supply at least 3: 1 and 2. \'m\' or \'r\' for the player, ' +
										 '3. number of iterations, 4. number of threads or blank to use default.')
	if sys.argv[1] == 'm':
		first = 'machine'
	elif sys.argv[1] == 'r':
		first = 'random'
	else:
		raise ValueError('First parameter: must supply \'m\' or \'r\' for the player.')
	if sys.argv[2] == 'm':
		second = 'machine'
	elif sys.argv[2] == 'r':
		second = 'random'
	else:
		raise ValueError('Second parameter: must supply \'m\' or \'r\' for the player.')
	try:
		iterations = int(sys.argv[3])
	except ValueError:
		print("Third parameter: must supply a number of iterations.")
	if (len(sys.argv) == 5):
		threads = int(sys.argv[4])
	else:
		threads = None
	return (first, second, iterations, threads)

# run the program. returns winner and move count
def run(command):
	byte_output = subprocess.Popen(command, stdout=subprocess.PIPE)
	output = byte_output.stdout.read()
	output = output.decode()
	output = output.split('\r\n')

	# get move counts of players
	player_1 = output[0][:-9]
	player_2 = output[1][:-9]
	p1_moves_count = 0
	p2_moves_count = 0
	p1_moves = []
	p2_moves = []
	for line in output:
		if line.find(player_1 + ' makes a move') == 0:
			p1_moves_count += 1
			move = re.search(r'\[[A-Za-z0-9 ]+\]', line).group()
			p1_moves.append(move)
		elif line.find(player_2 + ' makes a move') == 0:
			move = re.search(r'\[[A-Za-z0-9 ]+\]', line).group()
			p2_moves_count += 1
			p2_moves.append(move)
	# update the move counts and winner
	if output[-2].find(player_1) == 5:
		return (1, p1_moves_count, p1_moves)
	else:
		return (2, p2_moves_count, p2_moves)

# print the results of the test run
def print_results(player_1, player_2, p1_wins, p2_wins, p1_moves_count, p2_total_moves, p1_moves, p2_moves):
	p1_counter = Counter(p1_moves)
	p2_counter = Counter(p2_moves)
	if p1_wins == 0:
		print('*Player 1* (%s) wins: 0' % player_1)
	else:
		print("*Player 1* (%s) wins: %d, mean: %.2f, median: %.2f" 
			% (player_1, p1_wins, mean(p1_moves_count), median(p1_moves_count)))
		for value, count in p1_counter.most_common(5):
			print(value, count)
	if p2_wins == 0:
		print('*Player 2* (%s) wins: 0' % player_2)
	else:
		print("*Player 2* (%s) wins: %d, mean: %.2f, median: %.2f" 
			% (player_2, p2_wins, mean(p2_total_moves), median(p2_total_moves)))
		for value, count in p2_counter.most_common(5):
			print(value, count)

if __name__ == '__main__':
	start_time = time.time()
	main(sys.argv)
	print('--- %s seconds ---' % round(time.time() - start_time, 2))