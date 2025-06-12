# Orchestrate

This project provides a Java-based interface for working with (Integer) Linear Programs.
It is designed to be compatible with the IBM ILOG Concert API. 
It is intended to be used with the HiGHS solver, an open-source high-performance linear programming solver.

## Disclaimer
**This project is not affiliated with, endorsed by, or sponsored by IBM or ILOG. ILOG, Concert, and CPLEX are trademarks of IBM Corporation.**

**This project is not affiliated with, endorsed by, or sponsored by HiGHS solver**
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
        Orchestrate solver = new Orchestrate();
        
        //Creating variables
        NumVar x = solver.numVar(0, 10, "x");
        IntVar y = solver.boolVar("y");
        
        //Adding constraints
        solver.addLe(x, solver.constant(5));
        solver.addGe(x, y);
        
        //Setting the Objective
        solver.setObjective(x, ObjectiveSense.Minimize);

        //Solving the model
        solver.solve();
        
       //Getting a value
        double xValue = solver.getValue(x);
    }
}
```


## Roadmap
- [ ] Extend the API to support more features from open-source solvers.
- [ ] Implement support for more open-source solvers.
- [ ] Add more examples and documentation.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments
* HiGHS team for the HiGHS solver.
