# Orchestrate

This project provides a Java-based interface for working with (Integer) Linear Programs.
It is designed to compatible with the IBM ILOG Concert API. 
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
- [ ] Fully implement the Concert API.
- [ ] Implement support for more open-source solvers.
- [ ] Add more examples and documentation.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments
* HiGHS team for the HiGHS solver.
