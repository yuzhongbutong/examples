import random

max_door = 3
exp_times = 100

def get_gifts():
    gifts = ['Car']
    for i in range(max_door - 1):
        gifts.append('Goat' + str(i + 1))
    random.shuffle(gifts)
    print(f'Gifts: {gifts}')
    return gifts

def choose_gift():
    gifts = get_gifts()
    choice = random.randint(0, max_door - 1)
    first_choice = gifts[choice]
    del gifts[choice]
    for i, gift in reversed(list(enumerate(gifts))):
        if len(gifts) > 1:
            if gift.startswith('Goat'):
                del gifts[i]
    second_choice = gifts[0]
    return (first_choice, second_choice)

def get_probability():
    first_result = []
    second_result = []
    for i in range(exp_times):
        first_choice, second_choice = choose_gift()
        first_result.append(first_choice)
        second_result.append(second_choice)
    # print(f'Results of the first selection: {first_result}')
    # print(f'Results of the second selection: {second_result}')

    print(f'Number of experiment: {exp_times}')
    print(f'Number of doors: {max_door}')
    print('Probability of first choice: ' + str(first_result.count('Car') / exp_times))
    print('Probability of second choice: ' + str(second_result.count('Car') / exp_times))

get_probability()