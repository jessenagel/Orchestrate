package nl.jessenagel.highsjava;

    /**
     * Represents a fragment in the HiGHS Java library.
     * A fragment is a component that has a name and provides methods to get and set it.
     */
    public interface Fragment {

        /**
         * Gets the name of the fragment.
         *
         * @return The name of the fragment.
         */
        String getName();

        /**
         * Sets the name of the fragment.
         *
         * @param name The new name of the fragment.
         */
        void setName(String name);
    }