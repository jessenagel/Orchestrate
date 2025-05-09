# HiGHS Java Integration

This project provides a Java-based interface for working with the HiGHS solver, a high-performance open-source optimization solver for linear programming (LP), mixed-integer programming (MIP), and quadratic programming (QP).


## Disclaimer
This project is still in early development and may not be fully functional. The API and features are subject to change as the project evolves. 
Right now only the basic functionality is implemented. IP/LP problems can be solved, but the API is not yet complete.
## Features

- Define and manage variables, constraints, and objectives.
- Support for numerical and integer expressions.
- Integration with the HiGHS solver for solving optimization problems.
- Export models to `.lp` format and retrieve solutions.

## Requirements

- **Java**: Version 11 or higher.
- **Maven**: For dependency management and building the project.
- **HiGHS Solver**: Ensure the HiGHS binary is installed and accessible in your system's PATH.

## Installation
Download the latest release, or clone the repository and build it from source:
1. Clone the repository:
   ```bash
   git clone https://github.com/jessenagel/highs-java.git
   cd highs-java
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```
3. Ensure the HiGHS binary is in your system's PATH. You can download it from the [
   JuliaBinaryWrappers GitHub page](https://github.com/JuliaBinaryWrappers/HiGHSstatic_jll.jl/releases)

## Usage
### Example
```java
public class Example {
    public static void main(String[] args) {
        HiGHS highs = new HiGHS();
        
        //Creating variables
        NumVar x = highs.numVar(0, 10, "x");
        IntVar y = highs.boolVar("y");
        
        //Adding constraints
        highs.addLe(x, highs.constant(5));
        highs.addGe(x, y);
        
        //Setting the Objective
        highs.setObjective(x, ObjectiveSense.Minimize);

        //Solving the model
        highs.solve();
        
       //Getting a value
        double xValue = highs.getValue(x);
    }
}
```


## Roadmap
Currently, this API works by generating a `.lp` file and calling the HiGHS binary to solve the problem.
A more efficient approach would be to use the HiGHS C++ API directly from Java.
The HiGHS4j project, which provides a Java interface to the HiGHS C++ API, does this but was not compatible with my requirements.
Therefore, I decided to create this project.
## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments
* HiGHS team for the HiGHS solver.
