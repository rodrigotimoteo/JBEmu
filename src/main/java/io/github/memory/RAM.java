package io.github.memory;

/**
 * This class serves only as a mask to specify the type of memory module used
 *
 * @author rodrigotimoteo
 */

public class RAM extends MemoryModule {

    /**
     * Constructor Method
     *
     * <p>Creates a new Memory Module using by default only one bank</p>
     *
     * @param size   of the memory module to create
     * @param offset to find desired address (due to no alignment between address
     *               space and arrays)
     */
    public RAM(int size, int offset) {
        super(size, offset);
    }

    /**
     * Constructor Method
     *
     * <p>Creates a new Memory Module being able to use more than one
     * simultaneous memory bank</p>
     *
     * @param size              of the memory module to create
     * @param simultaneousBanks accessible at a time
     * @param offset            to find desired address (due to no alignment between address
     *                          space and arrays)
     * @param banks             number of total banks used
     */
    public RAM(int size, int simultaneousBanks, int offset, int banks) {
        super(size, simultaneousBanks, offset, banks);
    }
}
