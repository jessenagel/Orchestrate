package nl.jessenagel.orchestrate;

    /**
     * Represents a model in the Orchestrate Java library.
     * A model is a specialized fragment that allows adding and removing other fragments.
     */
    public interface Model extends Fragment {

        /**
         * Adds a fragment to the model.
         *
         * @param fragment The fragment to add.
         * @return The added fragment.
         */
        Fragment add(Fragment fragment);

        /**
         * Adds multiple fragments to the model.
         *
         * @param fragments An array of fragments to add.
         * @return An array of the added fragments.
         */
        Fragment[] add(Fragment[] fragments);

        /**
         * Removes a fragment from the model.
         *
         * @param fragment The fragment to remove.
         * @return The removed fragment.
         */
        Fragment remove(Fragment fragment);

        /**
         * Removes multiple fragments from the model.
         *
         * @param fragments An array of fragments to remove.
         * @return An array of the removed fragments.
         */
        Fragment[] remove(Fragment[] fragments);
    }