package bgu.spl.net.impl.stomp;

public abstract class Frame {
    protected int connectionId;
    public abstract void execute();
    
}
