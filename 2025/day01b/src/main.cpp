#include <iostream>
#include <fstream>
#include <string>
#include <filesystem>


#define STARTING_POINT 50

/**
 * Extracts the numeric movement value from a rotation command
 * @param input Rotation command (e.g. "L10", "R42")
 * @return Numeric portion of the command as a string
 */
std::string getMovementInput(const std::string input) {
    return input.substr(1);
}

/**
 * Calculates the new dial position after applying a signed rotation and counts every occurrence where the 
 *  dial points at zero during the rotation.
 * The dial is circular with valid positions in the range [0, 99]. Movement is applied one click at a time, 
 *  and wrap-around is handled explicitly:
 *   - Moving right from 99 wraps to 0
 *   - Moving left from 0 wraps to 99 *
 * @param current_position The starting dial position before the rotation
 * @param movement Signed number of clicks to rotate the dial: positive values rotate right, negative values rotate left
 * @param zero_dial_pointer_occurrence_counter Reference to a counter that is incremented every time the dial 
 *                                              points at position 0 during the rotation
 * @return The final dial position after applying the full rotation
 */
int calculateNewPosition(int current_position, const int movement, int &zero_dial_pointer_occurrence_counter) {
    
    int step = (movement > 0) ? 1 : -1;
    int clicks = std::abs(movement);

    for (int i = 0; i < clicks; ++i) {
        current_position += step;

        if (current_position == 100) {
            current_position = 0;
        }
        if (current_position == -1) {
            current_position = 99;
        }

        if (current_position == 0) {
            ++zero_dial_pointer_occurrence_counter;
        }
    }

    return current_position;
}

/**
 * Executes all dial rotation commands from an input file and counts how many times the dial points at position 0
 * @param pathToFile Path to the input command file
 * @return Total number of zero-position occurrences
 */
int findDoorPassword(std::string pathToFile)
{
    
    int dial_pointer_position = STARTING_POINT;
    int zero_dial_pointer_occurrence_counter = 0;

    std::ifstream file(pathToFile);
    if (!file.is_open()) {
        std::cerr<<"Failed to open file.\n";
        return 1;
    }

    try {
        std::string line;
        while (std::getline(file, line)) {

            char direction = line[0];
            if (direction != 'L' && direction != 'R') {
                throw std::runtime_error("Invalid pattern for input: " + line);
            }

            int absolute_movement_amount = std::stoi(getMovementInput(line));
            int movement = (direction == 'L') ? -absolute_movement_amount : absolute_movement_amount;

            dial_pointer_position = calculateNewPosition(dial_pointer_position, movement, zero_dial_pointer_occurrence_counter);
        }
    } catch(const std::exception& e) {
        std::cerr<<e.what();
        exit(-1);
    }

    file.close();
    return zero_dial_pointer_occurrence_counter;
}

/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    if (argc < 2) {
        std::cerr<<"Usage: "<<(argc > 0 ? argv[0] : "program")<<" <input-file>\n";
        return 1;
    }

    const int door_password = findDoorPassword(argv[1]);
    std::cout<<"The password for the door is: "<<door_password<<std::endl;
    return 0;
}