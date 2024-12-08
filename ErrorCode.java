public enum ErrorCode {
    X_OVER(1 << 0),  // 0001
    X_UNDER(1 << 1), // 0010
    Y_OVER(1 << 2),  // 0100
    Y_UNDER(1 << 3); // 1000

    private final short code;

    ErrorCode(int code) {
        this.code = (short) code;
    }

    public short getCode() {
        return code;
    }

    /**
     * Combines multiple error codes into a single flag value.
     */
    public static short combine(short combined, ErrorCode code) {
        combined |= code.getCode();
        return combined;
    }

    public static short multiple(ErrorCode... codes) {
        short combined = 0;
        for (ErrorCode code : codes) {
            combined |= code.getCode();
        }
        return combined;
    }

    public static boolean contains(short combinedFlags, ErrorCode code) {
        return (combinedFlags & code.getCode()) != 0;
    }
}
