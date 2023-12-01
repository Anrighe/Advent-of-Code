conversion_table = {
    'one': 1,
    'two': 2,
    'three': 3,
    'four': 4,
    'five': 5,
    'six': 6,
    'seven': 7,
    'eight': 8,
    'nine': 9
}


def calculate_sum():
    """
    Calculate the sum of all calibration values.

    Reads the input from a file named 'input.txt' and iterates through each line.
    For each line, it finds the first and last digits by searching for either a digit or a word in the conversion_table.
    If the first digit is not found, it uses the second digit as the first digit.
    If the second digit is not found, it uses the first digit as the second digit.
    Finally, it adds the two digits together and calculates the sum of all calibration values.

    Returns:
        int: The sum of all calibration values.
    """
    sum = 0
    first_digit = ''
    last_digit = ''

    found_first = False
    found_last = False

    with open('input.txt', 'r') as f:
        while True:
            found_first = False
            found_last = False
            line = f.readline()
            if not line:
                break

            line = line.strip()

            # First iteration to find the first digit
            for i in range(len(line)):
                if found_first:
                    break

                if str(line[i]).isdigit():
                    found_first = True
                    first_digit = line[i]
                    break

                for j in range(len(line)):
                    if line[i:j] in conversion_table.keys():
                        first_digit = str(conversion_table[line[i:j]])
                        found_first = True
                        break

            j = len(line) - 1
            i = len(line) - 1

            # Second iteration to find the second digit
            while not found_last and j > 0:
                i = j + 1

                while i > 0:
                    i -= 1
                    if line[i:j + 1] in conversion_table.keys():
                        last_digit = str(conversion_table[line[i:j + 1]])
                        found_last = True
                        break

                if str(line[j]).isdigit():
                    last_digit = line[j]
                    found_last = True
                    break
                j -= 1

            if not found_first:
                first_digit = last_digit
            else:
                if not found_last:
                    last_digit = first_digit

            sum += int(f'{first_digit}{last_digit}')

    return sum

if __name__ == '__main__':
    print(f'The sum of all of the calibration values is: {calculate_sum()}')
