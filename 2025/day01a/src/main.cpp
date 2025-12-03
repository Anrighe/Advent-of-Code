#include <iostream>
#include <fstream>
#include <string>
#include <filesystem>


#define STARTING_POINT 50

/**
 * Extracts the numeric movement portion of a command
 * @param input Command string (e.g. "L10")
 * @return Substring after the first character
 */
std::string getMovementInput(const std::string input) {
    return input.substr(1);
}

/**
 * Applies movement to the dial with wrap-around in [0,99]
 * @param current_position Current dial index
 * @param movement Signed movement amount
 * @return New wrapped dial position
 */
int calculateNewPosition(int current_position, const int movement) {
    current_position = current_position + movement;
    if (current_position >= 100) {
        while (current_position >= 100) {
            current_position -= 100;
        }
    } else if (current_position < 0) {
        while (current_position < 0) {
            current_position += 100;
        }
    }

    return current_position;
}

/**
 * Computes how many times the dial reaches zero based on file commands
 * @param pathToFile Path to the command input file
 * @return Count of zero-position occurrences
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
        int movement;
        while (std::getline(file, line)) {

            char direction = line[0];
            if (direction != 'L' && direction != 'R') {
                throw std::runtime_error("Invalid pattern for input: " + line);
            }

            int absolute_movement_amount = std::stoi(getMovementInput(line));
            int movement = (direction == 'L') ? -absolute_movement_amount : absolute_movement_amount;

            dial_pointer_position = calculateNewPosition(dial_pointer_position, movement);

            if (dial_pointer_position == 0) {
                zero_dial_pointer_occurrence_counter++;
            }

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