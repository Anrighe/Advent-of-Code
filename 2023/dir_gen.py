import os
import sys

USAGE_MSG = 'Usage: python dir_gen.py <dir_number> <\'a\' or \'b\'>'

if __name__ == '__main__':

    if len(sys.argv) != 3:
        print(USAGE_MSG)
        exit(1)

    if not sys.argv[1].isdigit():
        print(USAGE_MSG)
        exit(2)

    if sys.argv[2] != 'a' and sys.argv[2] != 'b':
        print(USAGE_MSG)
        exit(3)

    current_path = os.getcwd()
    dir_name = f'day{sys.argv[1].zfill(2)}{sys.argv[2]}'
    
    if not os.path.exists(dir_name):
        os.mkdir(dir_name)
        
        with open(os.path.join(current_path, dir_name, 'input.txt'), 'w') as f:
            pass

        os.chdir(os.path.join(current_path, dir_name))
        os.mkdir('src')
    else:
        print(f"Directory {dir_name} already exists")
        exit(4)