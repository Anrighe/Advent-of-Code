#include <iostream>
#include <fstream>
#include <string>
#include <filesystem>
#include <vector>
#include <sstream>
#include <algorithm>


class BatteryBank {
    private:
        std::string content;

    public:
        std::string getContent() { return content; }

        BatteryBank(const std::string content): content(content) {}

        long get_largest_possible_joltage() const {
            

            
            std::string sorted_content = content;
            std::sort(sorted_content.begin(), sorted_content.end(), std::greater<char>());
            
            //std::string first_highest_value_

            long first_highest_value = sorted_content[0] - '0';
            long second_highest_value = sorted_content[1] - '0';
            std::cout<<"first: "<<first_highest_value<<", second: "<<second_highest_value<<"\n";

            

            std::size_t first_highest_value_position = content.find(sorted_content[0]);
            std::size_t second_highest_value_position = content.find(sorted_content[1]);

            std::cout<<first_highest_value_position<<", "<<second_highest_value_position<<"\n";
                
            if (first_highest_value_position < second_highest_value_position) {
                std::cout<<"Returning: "<<(sorted_content[0] - '0') * 10 + (sorted_content[1] - '0')<<"\n";
                return (sorted_content[0] - '0') * 10 + (sorted_content[1] - '0');
            } else {
                std::cout<<"Returning: "<<(sorted_content[1] - '0') * 10 + (sorted_content[0] - '0')<<"\n";
                return (sorted_content[1] - '0') * 10 + (sorted_content[0] - '0');
            }
        }
};


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
            std::cout<<line<<"\n";
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
 * @param argv The first argument is the path of the input.txt file 
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