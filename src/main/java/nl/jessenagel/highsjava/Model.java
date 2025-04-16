package nl.jessenagel.highsjava;


public interface Model extends Fragment {
    Fragment add(Fragment fragment);
    Fragment[] add(Fragment[] fragments);
    Fragment remove(Fragment fragment);
    Fragment[] remove(Fragment[] fragments);
}
