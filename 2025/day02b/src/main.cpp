#include <iostream>
#include <fstream>
#include <string>
#include <filesystem>
#include <vector>
#include <sstream>

#define CHAR_RANGE_DELIMITER ','
#define CHAR_INNER_RANGE_DELIMITER '-'

/** ValueRange class represents a range of integer values with a lower and upper bound */
class ValueRange {
    private:
        long lower_bound;
        long upper_bound;

        /**
         * Splits a string exactly in the middle into two equal parts
         * @param string_to_split The string to split. Must have an even number of characters
         * @return A pair of strings representing the first and second halves of the input string
         * @throws std::runtime_error if the string length is not even
         */
        static std::pair<std::string, std::string> split_string_middle(const std::string &string_to_split) {
            std::size_t string_to_split_length = string_to_split.length();
            if (string_to_split_length % 2 != 0) {
                throw std::runtime_error("Could not split a string in two equal part if the number of characters is not even");
            }

            std::size_t middle = (string_to_split_length / 2);

            return {
                string_to_split.substr(0, middle),
                string_to_split.substr(middle)
            };
        }

        static bool is_index_invalid(const long index) {
            std::string index_string = std::to_string(index);

            std::size_t middle = index_string.length() / 2;

            for (std::size_t token_index = 1; token_index < middle; ) {
                std::string token = index_string.substr(0, token_index);
                std::cout<<"Token: "<<token<<"\n";

                std::size_t position = index_string.find(token);
                std::size_t total_occurrences_found = 0;
                std::cout<<position<<"\n";
                while (position != std::string::npos) {
                    ++total_occurrences_found;
                    position = index_string.find(token, position + token.length());
                } 

                if ((token.length() * total_occurrences_found) ==  index_string.length()) {
                    return true;
                }
            }
            return false;
        }

    public:
        long get_lower_bound() { return lower_bound; }
        long get_upper_bound() { return upper_bound; }

        ValueRange(
            const std::string lower_bound_string, 
            const std::string upper_bound_string
        ): 
            lower_bound(std::stol(lower_bound_string)),             
            upper_bound(std::stol(upper_bound_string)) {

        }

        /**
         * Calculates the sum of all "invalid" numbers in the range [lower_bound, upper_bound].
         * A number is invalid if it has an even number of digits and the first half of its digits is equal to the second half
         * @return The sum of all invalid numbers within the range
         */
        long get_invalid_indexes_sum() const {
            long invalid_indexes_sum = 0L;
            for (long current_value = lower_bound; current_value <= upper_bound; ++current_value) {
                if (is_index_invalid(current_value)) {
                    invalid_indexes_sum += current_value;
                }
            }
            return invalid_indexes_sum;
        }
};

/**
 * Splits a given string into a vector of substrings using the specified delimiter
 * @param string_to_split The string to split
 * @param delimiter The character to use as the delimiter
 * @return A vector containing the substrings
 */
std::vector<std::string> split(const std::string& string_to_split, const char delimiter) {
    std::vector<std::string> tokens;
    std::stringstream ss(string_to_split);

    std::string splitted_content;

    while (std::getline(ss, splitted_content, delimiter)) {
        tokens.push_back(splitted_content);
    }

    return tokens;
}

/**
 * Reads a file containing numeric ranges, parses them, and calculates the sum of invalid indexes
 * @param pathToFile Path to the input file containing ranges in the format "lower-upper,lower-upper,..."
 * @return The sum of all invalid numbers in the ranges.
 */
long get_invalid_indexes(const std::string &pathToFile)
{
    long invalid_indexes_sum = 0L;

    std::ifstream file(pathToFile);
    if (!file.is_open()) {
        std::cerr<<"Failed to open file.\n";
        return 1;
    }

    try {
        std::string line;
        std::getline(file, line); // Done it manually once since there's only 1 line
        std::vector<std::string> splitted_vector = split(line, CHAR_RANGE_DELIMITER);

        for (std::string &range : splitted_vector) {
            std::vector<std::string> ranges = split(range, CHAR_INNER_RANGE_DELIMITER);

            if (ranges.size() != 2) {
                std::cerr<<"Could not find exactly two ranges\n";
                exit(1);
            }

            const ValueRange value_range = ValueRange(ranges[0], ranges[1]);
            invalid_indexes_sum += value_range.get_invalid_indexes_sum();
        }

    } catch(const std::exception& e) {
        std::cerr<<e.what();
        exit(-1);
    }

    file.close();
    return invalid_indexes_sum;
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

    const long invalid_indexes_sum = get_invalid_indexes(argv[1]);
    std::cout<<"The sum of invalid id is: "<<invalid_indexes_sum<<std::endl;
    return 0;
}