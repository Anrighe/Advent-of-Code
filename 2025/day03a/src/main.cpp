#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>

/**
 * A representtation for a single battery bank encoded as a string of digits
 */
class BatteryBank {
    private:
        std::string battery_digits;

        /**
         * Combines two digit characters into a two-digit number
         * @param first_digit First digit character
         * @param second_digit Second digit character
         * @return Two-digit number formed by the digits
         */
        long merge_digits_to_long(const char first_digit, const char second_digit) const {
            long first_value = first_digit - '0';
            long second_value = second_digit - '0';

            return first_value * 10 + second_value;
        }

    public:
        /**
         * Returns the raw battery content string
         * @return Battery digit string
         */
        std::string get_battery_digits() { return battery_digits; }

        /**
         * Constructs a BatteryBank from a digit string
         * @param content String of digits representing battery joltages
         */
        BatteryBank(const std::string battery_digits): battery_digits(battery_digits) {}

        /**
         * Computes the maximum two-digit joltage possible from this bank.
         * Digits must be selected in order and cannot be rearranged
         * @return Largest possible joltage for the bank
         */
        long get_largest_possible_joltage() const {
            long max_joltage = 0L;
            for (std::size_t left_index = 0; left_index < battery_digits.length(); ++left_index) {

                // For this for, the constraint must be less or EQUAL because it needs to check the last digit
                for (std::size_t right_index = left_index + 1; right_index <= battery_digits.length(); ++right_index) { 
                    long current_joltage = merge_digits_to_long(battery_digits[left_index], battery_digits[right_index]);
                    if (max_joltage < current_joltage) {
                        max_joltage = current_joltage;
                    }
                }
            }

            return max_joltage;
        }
};

/**
 * Reads an input file and computes the sum of the largest possible joltage from each battery bank (one per line)
 * @param pathToFile Path to the input text file
 * @return Sum of maximum joltages across all banks
 */
long get_largest_possible_joltage_sum(const std::string &pathToFile)
{
    std::ifstream file(pathToFile);
    if (!file.is_open()) {
        std::cerr<<"Failed to open file.\n";
        return 1;
    }

    long largest_possible_joltage_sum = 0L;

    try {
        std::string line;

        while (std::getline(file, line)) {
            const BatteryBank battery_bank = BatteryBank(line);
            largest_possible_joltage_sum += battery_bank.get_largest_possible_joltage();
        }

    } catch(const std::exception& e) {
        std::cerr<<e.what();
        exit(-1);
    }

    file.close();
    return largest_possible_joltage_sum;
}

/**
 * @param argv The first argument is the path of the input file 
 */
int main(int argc, char *argv[])
{
    if (argc < 2) {
        std::cerr<<"Usage: "<<(argc > 0 ? argv[0] : "program")<<" <input-file>\n";
        return 1;
    }

    const long largest_possible_joltage_sum = get_largest_possible_joltage_sum(argv[1]);
    std::cout<<"The sum of each largest possible joltage: "<<largest_possible_joltage_sum<<std::endl;
    return 0;
}