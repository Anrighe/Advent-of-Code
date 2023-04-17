def are_pair_ordered(left_packet, right_packet):
    '''Returns true if the pairs are ordered. False otherwise.'''
    ordered = None
    for left_element, right_element in zip(left_packet, right_packet):
        if ordered == None:
            if type(left_element) == type(right_element):  # If they share the same type
                
                if isinstance(left_element, int) and isinstance(right_element, int):  # If they are both Integers
                    print(f'{left_element} <--> {right_element}')

                    if left_element < right_element:
                        ordered = True
                        print("ORDERED")
                    elif left_element > right_element:
                        ordered = False
                        print("NOT ORDERED!")

                if isinstance(left_element, list) and isinstance(right_element, list):  # If they are both lists
                    
                    if are_pair_ordered(left_element, right_element):
                        ordered = True
                    else:
                        ordered = False


    return ordered

def find_ordered_packets(filename):
    with open(filename) as file:

        eof = False
        pair_index = 1
        ordered_indices = []

        while not eof:

            left_line = file.readline()
            right_line = file.readline()

            if left_line and right_line:


                left_packet = eval(left_line)
                right_packet = eval(right_line)

                print(f'Pair {pair_index}')
                print(left_packet)  # Debug
                print(right_packet)  # Debug

                if are_pair_ordered(left_packet, right_packet):
                    ordered_indices.append(pair_index)
                print('----------------------')

                file.readline() # Reads the separator

                pair_index += 1
            else:
                eof = True

    return 0

if __name__ == '__main__':
    orderedPackets = find_ordered_packets('inputDebug.txt')
    #sumOrderedPackets = sum(orderedPackets)
    #print(f'\nThe sum of the indices of the ordered pairs is: {sumOrderedPackets}')