package net.blay09.mods.balm.platform.config.notoml;

import org.jspecify.annotations.Nullable;

public class NotomlError {

    private final String message;
    @Nullable
    private Throwable cause;
    private int line = -1;

    public NotomlError(String message) {
        this.message = message;
    }

    public NotomlError(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    @Nullable
    public Throwable getCause() {
        return cause;
    }

    public NotomlError at(int line) {
        this.line = line;
        return this;
    }

    public boolean hasLine() {
        return line != -1;
    }

    public int getLine() {
        return line;
    }
}
