import java.util.HashSet;
import java.util.Set;

public class Plot {
    private Set<Coordinate> plotRegion;

    public Plot() {
        plotRegion = new HashSet<>();
    }

    public Plot(Coordinate coordinate) {
        plotRegion = new HashSet<>();
        plotRegion.add(coordinate);
    }

    public Set<Coordinate> getPlotRegion() {
        return plotRegion;
    }

    private void setPlotRegion(Set<Coordinate> region) {
        this.plotRegion = region;
    }

    /**
     * Checks if the given coordinate is adjacent to any coordinate in the plot region.
     *
     * @param coordinate the coordinate to check for adjacency
     * @return true if the given coordinate is adjacent to any coordinate in the plot region, false otherwise
     */
    public boolean isCoordinateAdjacentToPlotRegion(Coordinate coordinate) {

        for (Coordinate plotRegionCoordinate : plotRegion) {
            if (plotRegionCoordinate.isAdjacentTo(coordinate))
                return true;
        }

        return false;
    } 

    /**
     * Adds a coordinate to the plot region if it is adjacent to the existing plot region.
     *
     * @param coordinate the coordinate to be added to the plot region
     * @throws RuntimeException if the coordinate is not adjacent to the plot region
     */
    public void addCoordinateToPlotRegion(Coordinate coordinate) throws RuntimeException {
        if (isCoordinateAdjacentToPlotRegion(coordinate)) {
            plotRegion.add(coordinate);
        } else {
            throw new RuntimeException("Could not add a non-adjacent coordinate to the plot region");
        }
    }

    @Override
    public String toString() {
        return plotRegion.toString();
    }

    
    public int getArea() {
        return plotRegion.size();
    }

    /**
     * Calculates the perimeter of a region in a given matrix.
     * The region is defined by a collection of coordinates.
     *
     * @param matrix A 2D character array representing the matrix.
     * @return The perimeter of the region.
     */
    public int getPerimeter(char[][] matrix) {
        int perimeter = 0;

        int rows = matrix.length;
        int cols = matrix[0].length;
        assert rows == cols;

        for (Coordinate coordinate : plotRegion) {
            int currentRow = coordinate.getRow();
            int currentCol = coordinate.getCol();
            
            // UP
            if (currentRow == 0 || (matrix[currentRow][currentCol] != matrix[currentRow-1][currentCol]))
                perimeter++;

            // DOWN
            if (currentRow == rows - 1 || (matrix[currentRow][currentCol] != matrix[currentRow+1][currentCol]))
                perimeter++;

            // LEFT
            if (currentCol == 0 || (matrix[currentRow][currentCol] != matrix[currentRow][currentCol-1]))
                perimeter++;

            // RIGHT
            if (currentCol == cols - 1 || (matrix[currentRow][currentCol] != matrix[currentRow][currentCol+1]))
                perimeter++; 
        }
        return perimeter;
    }

    /**
     * Checks if the current plot is adjacent to the specified plot.
     * Two plots are considered adjacent if any coordinate in the current plot's region
     * is adjacent to any coordinate in the specified plot's region.
     *
     * @param plotToVerify the plot to check adjacency against
     * @return true if the current plot is adjacent to the specified plot, false otherwise
     */
    public boolean isAdjacentTo(Plot plotToVerify) {
        for (Coordinate coord1 : plotRegion) {
            for (Coordinate coord2 : plotToVerify.getPlotRegion()) {
                if (coord1.isAdjacentTo(coord2))
                    return true;
            }
        }
        return false;
    }


}
