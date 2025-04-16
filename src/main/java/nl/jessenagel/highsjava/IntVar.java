package nl.jessenagel.highsjava;

public interface IntVar extends NumVar{
    int getMax();
    int getMin();
    int setMax(int max);
    int setMin(int min);

}
