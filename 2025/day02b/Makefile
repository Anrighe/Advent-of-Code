CXX      := g++
CXXFLAGS := -std=c++17 -Wall -Wextra -Wpedantic -O3 -g
TARGET   := gift_shop
SRC      := src/main.cpp

all: $(TARGET)

$(TARGET): $(SRC)
	$(CXX) $(CXXFLAGS) $(SRC) -o $(TARGET)

clean:
	rm -f $(TARGET)

.PHONY: all clean
